package videoshop.shoes.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import videoshop.catalog.Comment;

@Entity
public class Shoes extends Product{
	public static enum ShoesType {
		NIKE, VANS, CONVERSE, ADIDAS, BALENCIA;
	}

	private String image;
	private ShoesType type;
	private String description;

	
	@OneToMany(cascade = CascadeType.ALL) //
	private List<Comment> comments = new ArrayList<>();

	@SuppressWarnings("unused")
	private Shoes() {}

	public Shoes(String name, String image, Money price, ShoesType type, String description) {

		super(name, price);

		this.image = image;
		this.type = type;
		this.description = description;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}

	public Iterable<Comment> getComments() {
		return comments;
	}

	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	public ShoesType getType() {
		return type;
	}
	
	public void setType(ShoesType type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
