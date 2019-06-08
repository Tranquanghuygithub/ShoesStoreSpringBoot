package videoshop.shoes.inventory;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import videoshop.catalog.VideoCatalog;
import videoshop.shoes.catalog.Shoes;
import videoshop.shoes.catalog.ShoesCatalog;

@Component
@Order(20)
public class ShoesInventoryDataInitializer implements DataInitializer{
	private final Inventory<InventoryItem> inventory;
	private final ShoesCatalog shoesCatalog;

	public ShoesInventoryDataInitializer(Inventory<InventoryItem> inventory, ShoesCatalog shoesCatalog) {

		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(shoesCatalog, "VideoCatalog must not be null!");

		this.inventory = inventory;
		this.shoesCatalog = shoesCatalog;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// (｡◕‿◕｡)
		// Über alle Discs iterieren und jeweils ein InventoryItem mit der Quantity 10 setzen
		// Das heißt: Von jeder Disc sind 10 Stück im Inventar.

		shoesCatalog.findAll().forEach(shoes -> {

			// Try to find an InventoryItem for the project and create a default one with 10 items if none available
			inventory.findByProduct(shoes) //
					.orElseGet(() -> inventory.save(new InventoryItem(shoes, Quantity.of(10))));
		});
	}
	
	public void deleteShoesInventoryItem(Shoes shoes) {
		for ( InventoryItem inventoryItem : inventory.findAll()) {
			if (inventoryItem.getProduct().getId().equals(shoes.getId())) {
				inventory.delete(inventoryItem);
			}
		}
	}
	
	public void deleteAll() {
		inventory.deleteAll();
	
	}
}
