package videoshop.management;

import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management")
public class ManagementController {
	private final Inventory<InventoryItem> inventory;

	ManagementController(Inventory<InventoryItem> inventory) {
		this.inventory = inventory;
	}
	
	@GetMapping("/shoesStock")
	//@PreAuthorize("hasRole('BOSS')")
	String ShoesStock(Model model) {

		//model.addAttribute("stock", inventory.findAll());

		return "shoesstock";
	}
	
	@GetMapping("/customers")
	//@PreAuthorize("hasRole('BOSS')")
	String CustomerManagement(Model model) {

		//model.addAttribute("stock", inventory.findAll());

		return "customer_management";
	}
	
	@GetMapping("/orders")
	//@PreAuthorize("hasRole('BOSS')")
	String OrdersManagement(Model model) {

		//model.addAttribute("stock", inventory.findAll());

		return "orders_management";
	}
	
	@GetMapping("/addShoes")
	//@PreAuthorize("hasRole('BOSS')")
	String AddShoes(Model model) {

		//model.addAttribute("stock", inventory.findAll());

		return "add_shoes";
	}
}
