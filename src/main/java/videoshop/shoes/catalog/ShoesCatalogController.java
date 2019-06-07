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
import videoshop.shoes.inventory.ShoesInventoryDataInitializer;
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
		ShoesCatalogDataInitializer shoesCatalogDataInitialize  = new ShoesCatalogDataInitializer(shoesCatalog);


		
//		shoesCatalogDataInitialize.addShoes(new Shoes("Adidas Grey", "p1", Money.of(99, EURO), ShoesType.ADIDAS, "This is a new shoes in 2019, compare with old stype, it's good"));
//		
//		shoesCatalogDataInitialize.addShoes(new Shoes("Adidas Grey with yellow line", "p2", Money.of(79, EURO), ShoesType.ADIDAS, "ADIDAS good with yellow"));
//		
//		shoesCatalogDataInitialize.addShoes(new Shoes("Van slip on dark", "p3", Money.of(80, EURO), ShoesType.VANS, "Classic Vans is good and excenlent"));
//		
//		shoesCatalogDataInitialize.addShoes(new Shoes("Nike air on max", "p4", Money.of(125, EURO), ShoesType.NIKE, "Nike with brown classic"));
//		
//		shoesCatalogDataInitialize.addShoes(new Shoes("Converse classic white", "p5", Money.of(119, EURO), ShoesType.CONVERSE, "Converse new style in 2017"));
//		
//		shoesCatalogDataInitialize.addShoes(new Shoes("Van red high", "p6", Money.of(59, EURO), ShoesType.VANS, "Van with background red"));
//
//		
//		ShoesInventoryDataInitializer dataInitializer = new ShoesInventoryDataInitializer(inventory, shoesCatalog);
//		dataInitializer.deleteAll();
//		shoesCatalog.deleteAll();

		model.addAttribute("catalog", shoesCatalog.findAll());
		
		
		//model.addAttribute("title", "catalog.dvd.title");

		return "shoescatalog";
	}
	
	@GetMapping("/detail/{shoes}")
	String detail(@PathVariable Shoes shoes, Model model) {

//		Optional<InventoryItem> item = inventory.findByProductIdentifier(disc.getId());
//		Quantity quantity = item.map(InventoryItem::getQuantity).orElse(NONE);
		
		Optional<InventoryItem> item = inventory.findByProductIdentifier(shoes.getId());
		Quantity quantity = item.map(InventoryItem::getQuantity).orElse(NONE);
		
//		System.out.println(shoes.getImage());
		
		model.addAttribute("shoes", shoes);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));
//		model.addAttribute("disc", disc);
//		model.addAttribute("quantity", quantity);
//		model.addAttribute("orderable", quantity.isGreaterThan(NONE));
		
		

		return "shoesdetail";
	}
	
	@GetMapping("/catalog/{type}")
	String FindTypeShoes(Model model, @PathVariable ShoesType type ) {
		
		model.addAttribute("typeShoes", shoesCatalog.findByType(type));
		
		return "shoescatalog";
	}
}
