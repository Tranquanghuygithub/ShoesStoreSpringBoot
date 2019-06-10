package videoshop.shoes.catalog;

import javax.validation.constraints.NotEmpty;
public interface AddShoesForm {
	@NotEmpty(message = "{AddShoesForm.name.NotEmpty}") //
	String getName();

	@NotEmpty(message = "{AddShoesForm.price.NotEmpty}") //
	String getPrice();
	

//	@NotEmpty(message = "{AddShoesForm.shoestype.value.NotEmpty}") //
//	String getShoesType();
	
	@NotEmpty(message = "{AddShoesForm.description.NotEmpty}") //
	String getDescription();
}
