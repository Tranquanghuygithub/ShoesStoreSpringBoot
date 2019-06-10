package videoshop.management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.customer.Customer;
import videoshop.customer.CustomerManagement;
import videoshop.shoes.catalog.AddShoesForm;
import videoshop.shoes.catalog.Shoes;


@Controller
@RequestMapping("/management")
public class ManagementController {
	private final Inventory<InventoryItem> inventory;
	
	private final OrderManager<Order> orderManager;
	private final CustomerManagement customerManagement;

	ManagementController(Inventory<InventoryItem> inventory, OrderManager<Order> orderManager, CustomerManagement customerManagement) {
		this.inventory = inventory;
		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
		this.customerManagement = customerManagement;
	}
	
	@GetMapping("/shoesStock")
	//@PreAuthorize("hasRole('BOSS')")
	String ShoesStock(Model model) {

		model.addAttribute("stock", inventory.findAll());

		return "shoesstock";
	}
	
	@GetMapping("/customers")
	//@PreAuthorize("hasRole('BOSS')")
	String CustomerManagement(Model model) {

		//model.addAttribute("stock", inventory.findAll());
		
		model.addAttribute("customerList", customerManagement.findAll());
		
		

		return "customer_management";
	}
	
	@GetMapping("/orders")
	//@PreAuthorize("hasRole('BOSS')")
	String OrdersManagement(Model model) {

		//model.addAttribute("stock", inventory.findAll());
		
		model.addAttribute("ordersCompleted", orderManager.findBy(OrderStatus.COMPLETED));

		return "orders_management";
	}
	
	@GetMapping("/addShoes")
	//@PreAuthorize("hasRole('BOSS')")
	String AddShoes(Model model, AddShoesForm Shoesform) {

		model.addAttribute("productform", Shoesform);

		return "add_shoes";
	}
	
	@GetMapping("/updateShoes")
	//@PreAuthorize("hasRole('BOSS')")
	String UpdateShoes(Model model, AddShoesForm Shoesform) {

		model.addAttribute("productform", Shoesform);

		return "edit_shoes";
	}
	
	@GetMapping(value = "/shoesStock/")
	String SearchShoes(@RequestParam(value = "search", required = false) String q, Model model) {

		if (q.isEmpty()) {
			model.addAttribute("stock", inventory.findAll());
		} else {
			
			List<InventoryItem> shoesList = (List<InventoryItem>) inventory.findAll();
			
			List<InventoryItem> shoesListResult = new ArrayList<InventoryItem>(); 
						
			for (InventoryItem inventoryItem : shoesList) {
				if (inventoryItem.getProduct().getName().contains(q)) {
					shoesListResult.add(inventoryItem);
				}
			}
	
			model.addAttribute("stock", shoesListResult);
			
			model.addAttribute("search", q);

		}

		return "shoesstock";
	}
	
	
	@GetMapping(value = "/orders/")
	String SearchOrder(@RequestParam(value = "search", required = false) String q, Model model) {

		if (q.isEmpty()) {
			model.addAttribute("ordersCompleted", orderManager.findBy(OrderStatus.COMPLETED));
		} else {
			
			Streamable<Order> orderList = orderManager.findBy(OrderStatus.COMPLETED);
			
			List<Order> orderListResult = new ArrayList<Order>(); 
						
			for (Order item : orderList) {
				if (item.getUserAccount().getUsername().contains(q)) {
					orderListResult.add(item);
				}
			}
	
			model.addAttribute("ordersCompleted", orderListResult);
			
			model.addAttribute("search", q);

		}

		return "orders_management";
	}
	
	@GetMapping(value = "/customers/")
	String SearchCustomer(@RequestParam(value = "search", required = false) String q, Model model) {

		if (q.isEmpty()) {
			model.addAttribute("customerList", customerManagement.findAll());
		} else {
			
			Streamable<Customer> customerList = customerManagement.findAll();
			
			List<Customer> customerListResult = new ArrayList<Customer>(); 
						
			for (Customer item : customerList) {
				if (item.getUserAccount().getUsername().contains(q)) {
					customerListResult.add(item);
				}
			}
	
			model.addAttribute("customerList", customerListResult);
			
			model.addAttribute("search", q);

		}

		return "customer_management";
	}
	
	
	@GetMapping("/orders/{order}")
	//@PreAuthorize("hasRole('BOSS')")
	String DeleteOrder(@PathVariable Order order, Model model) {

		

		return "redirect:";
	}
	
	
}
