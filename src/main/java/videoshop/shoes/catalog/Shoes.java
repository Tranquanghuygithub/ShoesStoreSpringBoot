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
		BLURAY, DVD;
	}

	private String image;
	private ShoesType type;

	@OneToMany(cascade = CascadeType.ALL) //
	private List<Comment> comments = new ArrayList<>();

	@SuppressWarnings("unused")
	private Shoes() {}

	public Shoes(String name, String image, Money price, ShoesType type) {

		super(name, price);

		this.image = image;
		this.type = type;
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

	public ShoesType getType() {
		return type;
	}
}
