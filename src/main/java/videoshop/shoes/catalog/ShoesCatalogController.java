package videoshop.shoes.catalog;

import static org.salespointframework.core.Currencies.EURO;

import java.io.File;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Iterables;

import videoshop.catalog.CatalogDataInitializer;
import videoshop.catalog.Comment;
import videoshop.catalog.Disc;
import videoshop.catalog.VideoCatalog;
import videoshop.shoes.catalog.Shoes.ShoesType;
import videoshop.shoes.inventory.ShoesInventoryDataInitializer;
import videoshop.catalog.Disc.DiscType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



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


		
//		shoesCatalogDataInitialize.addShoes(new Shoes("Vans Old Skools", "p4", Money.of(60, EURO), ShoesType.VANS, "This is a new shoes"));
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
	

	private static String UPLOADED_FOLDER = "D:\\ShoesStoreSpringBoot\\src\\main\\resources\\static\\resources\\img\\product\\";
	@PostMapping("/addshoes")
	String AddShoes(@Valid AddShoesForm productform, @RequestParam("shoestype") String shoestype, @RequestParam("file") MultipartFile file) {
		
	
       
		
		ShoesType type = checkShoesType(shoestype);
		Shoes shoes = new Shoes(productform.getName(), file.getOriginalFilename(), Money.of(Integer.parseInt(productform.getPrice()), EURO), type, productform.getDescription());
		ShoesCatalogDataInitializer catalogData = new ShoesCatalogDataInitializer(shoesCatalog);
		catalogData.addShoes(shoes);
		
		byte[] bytes;
		try {
			bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
		    Files.write(path, bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(productform.getName());
//		System.out.println(productform.getPrice());
//		System.out.println(file.getOriginalFilename());
//		System.out.println(shoestype);
//		System.out.println(productform.getDescription());
		return "redirect:/";
	}
	
	public ShoesType checkShoesType(String value) {
		if(value.contentEquals("adidas"))
			return ShoesType.ADIDAS;
		else if(value.contentEquals("nike"))
			return ShoesType.NIKE;
		else if(value.contentEquals("converse"))
			return ShoesType.CONVERSE;
		
		return ShoesType.VANS;
	}
}
