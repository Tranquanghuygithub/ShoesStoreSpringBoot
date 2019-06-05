package videoshop.shoes.catalog;

import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.javamoney.moneta.Money;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Iterables;

import videoshop.catalog.CatalogDataInitializer;
import videoshop.catalog.Comment;
import videoshop.catalog.Disc;
import videoshop.catalog.VideoCatalog;
import videoshop.shoes.catalog.Shoes.ShoesType;
import videoshop.catalog.Disc.DiscType;

@Controller
@RequestMapping("/shoes")
public class ShoesCatalogController {
	private static final Quantity NONE = Quantity.of(0);

	private final ShoesCatalog shoesCatalog;
	private final Inventory<InventoryItem> inventory;
	private final BusinessTime businessTime;

	ShoesCatalogController(ShoesCatalog shoesCatalog, Inventory<InventoryItem> inventory, BusinessTime businessTime) {

		this.shoesCatalog = shoesCatalog;
		this.inventory = inventory;
		this.businessTime = businessTime;
	}

	@GetMapping("/catalog")
	String dvdCatalog(Model model) {
		//ShoesCatalogDataInitializer shoesCatalogDataInitialize  = new ShoesCatalogDataInitializer(shoesCatalog);
		
//		Iterable<Shoes> ds = shoesCatalog.findAll();
//		
//		for (Shoes item : ds) {
//			System.out.println(item.getId());
//			System.out.println(item.getName());
//		}
//		
//		Shoes delShoes = Iterables.get(ds, 1);
//	
		ShoesCatalogDataInitializer shoesCatalogDataInitialize  = new ShoesCatalogDataInitializer(shoesCatalog);
//		shoesCatalogDataInitialize.deleteShoes(inventory, delShoes);
		
		shoesCatalogDataInitialize.addShoes(new Shoes("Nike", "lac", Money.of(200, EURO), ShoesType.DVD));
		model.addAttribute("catalog", shoesCatalog.findAll());
		//model.addAttribute("title", "catalog.dvd.title");

		return "shoescatalog";
	}
	
	@GetMapping("/detail/{shoes}")
	String detail(@PathVariable Shoes shoes, Model model) {

//		Optional<InventoryItem> item = inventory.findByProductIdentifier(disc.getId());
//		Quantity quantity = item.map(InventoryItem::getQuantity).orElse(NONE);
//
		
		
//		model.addAttribute("disc", disc);
//		model.addAttribute("quantity", quantity);
//		model.addAttribute("orderable", quantity.isGreaterThan(NONE));
		
		

		return "shoesdetail";
	}

	
}
