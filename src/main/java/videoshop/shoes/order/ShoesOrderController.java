package videoshop.shoes.order;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import videoshop.catalog.Disc;
import videoshop.shoes.catalog.Shoes;
import videoshop.shoes.catalog.ShoesCatalog;
import videoshop.shoes.inventory.ShoesInventoryDataInitializer;
import videoshop.shoes.catalog.ShoesCatalogDataInitializer;


@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
@RequestMapping("/orders")
class ShoesOrderController
{
	private final OrderManager<Order> orderManager;

	/**
	 * Creates a new {@link OrderController} with the given {@link OrderManager}.
	 * 
	 * @param orderManager must not be {@literal null}.
	 */
	private final ShoesCatalog shoesCatalog;
	private final Inventory<InventoryItem> inventory;
	ShoesOrderController(OrderManager<Order> orderManager, ShoesCatalog shoesCatalog, Inventory<InventoryItem> inventory) {
		this.shoesCatalog = shoesCatalog;
		this.inventory = inventory;
		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
	}

	/**
	 * Creates a new {@link Cart} instance to be stored in the session (see the class-level {@link SessionAttributes}
	 * annotation).
	 * 
	 * @return a new {@link Cart} instance.
	 */
	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	/**
	 * Adds a {@link Disc} to the {@link Cart}. Note how the type of the parameter taking the request parameter
	 * {@code pid} is {@link Disc}. For all domain types extending {@link AbstractEntity} (directly or indirectly) a tiny
	 * Salespoint extension will directly load the object instance from the database. If the identifier provided is
	 * invalid (invalid format or no {@link Product} with the id found), {@literal null} will be handed into the method.
	 * 
	 * @param disc the disc that should be added to the cart (may be {@literal null}).
	 * @param number number of discs that should be added to the cart.
	 * @param cart must not be {@literal null}.
	 * @return the view name.
	 */
	@PostMapping("/shoescart")
	String addDisc(@RequestParam("pid") Disc disc, @RequestParam("number") int number, @ModelAttribute Cart cart) {

		// (｡◕‿◕｡)
		// Das Inputfeld im View ist eigentlich begrenzt, allerdings sollte man immer auch serverseitig validieren
		int amount = number <= 0 || number > 5 ? 1 : number;

		// (｡◕‿◕｡)
		// Wir fügen dem Warenkorb die Disc in entsprechender Anzahl hinzu.
		cart.addOrUpdateItem(disc, Quantity.of(amount));

		// (｡◕‿◕｡)
		// Je nachdem ob disc eine DVD oder eine Bluray ist, leiten wir auf die richtige Seite weiter

		switch (disc.getType()) {
			case DVD:
				return "redirect:dvds";
			case BLURAY:
			default:
				return "redirect:blurays";
		}
		
	}
	@PostMapping("addshoes")
	String addShoes(@RequestParam("shoes") Shoes shoes, @RequestParam("number") int number, @ModelAttribute Cart cart,HttpSession session) {
		int amount = number <= 0 || number > 5 ? 1 : number;
		
		cart.addOrUpdateItem(shoes, Quantity.of(amount));
		
		int cartsize = cart.toList().size();
		session.setAttribute("cartsize", cartsize);
		
		return "redirect:../shoes/catalog";
	}
	
	@RequestMapping("/addtobag/{shoes}")
	String addToBag(@PathVariable Shoes shoes, @ModelAttribute Cart cart,HttpSession session) {
		
		int amount = 1;
		cart.addOrUpdateItem(shoes, Quantity.of(amount));
		
		int cartsize = cart.toList().size();
		session.setAttribute("cartsize", cartsize);
		
		return "redirect:../../shoes/catalog";
	}
	
	@RequestMapping("/shoescart/{shoes}")
	String deleteItemFromCart(@PathVariable Shoes shoes, @ModelAttribute Cart cart, HttpSession session) {
		
		CartItem itemRemove = cart.toList().get(0);
		for(int i=0; i <cart.toList().size(); i++) {
			if(cart.toList().get(i).getProduct().getId().toString().contentEquals(shoes.getId().toString())) {
				itemRemove = cart.toList().get(i);
			}
		}
		cart.removeItem(itemRemove.getId().toString());
		
		int cartsize = cart.toList().size();
		session.setAttribute("cartsize", cartsize);
		
		return "redirect:http://localhost:8080/orders/shoescart";
	}
	@PostMapping("updatecart")
	String updatecart(@RequestParam("qty") int[] quantity,@RequestParam("id") Shoes[] shoes, @ModelAttribute Cart cart ) {
		cart.clear();
		for(int i =0; i < quantity.length; i++) {
			cart.addOrUpdateItem(shoes[i], Quantity.of(quantity[i]));
		}

		return "redirect:http://localhost:8080/orders/shoescart";
	}
	
	
	@GetMapping("/shoescart")
	String basket(Model model) {
//		int cartsize = cart.toList().size();
//		session.setAttribute("cartsize", cartsize);
		List<Product> exceedsShoes = new ArrayList<Product>();
		model.addAttribute("exceedsShoes", exceedsShoes);
		return "shoescart";
	}
	
	@GetMapping("/shoesconfirmation")
	String shoesconfirmation() {
		return "shoesconfirmation";
	}
	
	@GetMapping("/shoescheckout")
	String checkout(Model model, @ModelAttribute Cart cart) {
		ShoesInventoryDataInitializer dataInitializer = new ShoesInventoryDataInitializer(inventory, shoesCatalog);
		List<Product> exceedsShoes = new ArrayList<Product>();
		exceedsShoes = dataInitializer.checkInventory(cart.toList());
		if(exceedsShoes.size() == 0) {
			return "shoescheckout";
		}
		else {
			model.addAttribute("exceedsShoes", exceedsShoes);
			System.out.println(exceedsShoes);
			return "shoescart";
		}
		
	}

	/**
	 * Checks out the current state of the {@link Cart}. Using a method parameter of type {@code Optional<UserAccount>}
	 * annotated with {@link LoggedIn} you can access the {@link UserAccount} of the currently logged in user.
	 * 
	 * @param cart will never be {@literal null}.
	 * @param userAccount will never be {@literal null}.
	 * @return the view name.
	 */
	@PostMapping("/checkout1")
	String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, HttpSession session) {
		
		UserAccount user = userAccount.get();
		
	
		

		if(user != null) {
			Order order = new Order(user, Cash.CASH);
			if(cart.isEmpty()) {
				return "redirect:/orders/shoescart";
			}
			else {
				cart.addItemsTo(order);
				
				orderManager.payOrder(order);
				orderManager.completeOrder(order);
				
				cart.clear();
			}
			
			int cartsize = cart.toList().size();
			session.setAttribute("cartsize", cartsize);
			
			return "redirect:/";
		}
		
		return "redirect:/orders/shoescart";
	}

	@GetMapping("/orders1")
	@PreAuthorize("hasRole('BOSS')")
	String orders(Model model) {

		model.addAttribute("ordersCompleted", orderManager.findBy(OrderStatus.COMPLETED));
	
		return "orders";
	}
	
	@GetMapping("/delete/{order}")
	String DeleteShoes(@PathVariable Order order, Model model) {
		orderManager.delete(order);
		return "redirect:../../management/orders";
	}
	
}