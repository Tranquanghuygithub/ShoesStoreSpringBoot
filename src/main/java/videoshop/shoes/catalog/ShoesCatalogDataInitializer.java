package videoshop.shoes.catalog;


import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import videoshop.shoes.inventory.ShoesInventoryDataInitializer;

@Component
@Order(20)
public class ShoesCatalogDataInitializer{

	private static final Logger LOG = LoggerFactory.getLogger(ShoesCatalogDataInitializer.class);

	private final ShoesCatalog shoesCatalog;

	ShoesCatalogDataInitializer(ShoesCatalog shoesCatalog) {

		Assert.notNull(shoesCatalog, "ShoesCatalog must not be null!");

		this.shoesCatalog = shoesCatalog;
	}
	
	public void addShoes(Shoes shoes) {
		shoesCatalog.save(shoes);
	}
	
	public void updateShoes(Shoes shoes) {
		shoesCatalog.save(shoes);
	}
	
	public void deleteShoes(Inventory<InventoryItem> inventory, Shoes shoes) {
		ShoesInventoryDataInitializer shoesInventoryInitializer = new ShoesInventoryDataInitializer(inventory, shoesCatalog);
		shoesInventoryInitializer.deleteShoesInventoryItem(shoes);
		shoesCatalog.delete(shoes);
	}
	

}
