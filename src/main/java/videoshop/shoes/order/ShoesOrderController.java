package videoshop.shoes.order;

import java.util.Optional;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.Cart;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import videoshop.catalog.Disc;


@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("storecart")
class ShoesOrderController
{
	private final OrderManager<Order> orderManager;

	/**
	 * Creates a new {@link OrderController} with the given {@link OrderManager}.
	 * 
	 * @param orderManager must not be {@literal null}.
	 */
	ShoesOrderController(OrderManager<Order> orderManager) {

		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
	}

	/**
	 * Creates a new {@link Cart} instance to be stored in the session (see the class-level {@link SessionAttributes}
	 * annotation).
	 * 
	 * @return a new {@link Cart} instance.
	 */
	@ModelAttribute("storecart")
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

	@GetMapping("/shoescart")
	String basket() {
		return "shoescart";
	}
	
	@GetMapping("/shoesconfirmation")
	String shoesconfirmation() {
		return "shoesconfirmation";
	}
	
	@GetMapping("/shoescheckout")
	String checkout() {
		return "shoescheckout";
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
	String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount) {

		return userAccount.map(account -> {

			// (｡◕‿◕｡)
			// Mit completeOrder(…) wird der Warenkorb in die Order überführt, diese wird dann bezahlt und abgeschlossen.
			// Orders können nur abgeschlossen werden, wenn diese vorher bezahlt wurden.
			Order order = new Order(account, Cash.CASH);

			cart.addItemsTo(order);

			orderManager.payOrder(order);
			orderManager.completeOrder(order);

			cart.clear();

			return "redirect:/";
		}).orElse("redirect:/cart");
	}

	@GetMapping("/orders1")
	@PreAuthorize("hasRole('BOSS')")
	String orders(Model model) {

		model.addAttribute("ordersCompleted", orderManager.findBy(OrderStatus.COMPLETED));

		return "orders";
	}
}