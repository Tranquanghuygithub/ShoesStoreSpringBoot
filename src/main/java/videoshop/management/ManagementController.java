package videoshop.management;

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
import org.springframework.web.bind.annotation.RequestMapping;

import videoshop.customer.Customer;
import videoshop.customer.CustomerManagement;
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
	String AddShoes(Model model) {

		//model.addAttribute("stock", inventory.findAll());

		return "add_shoes";
	}
	
	
}
