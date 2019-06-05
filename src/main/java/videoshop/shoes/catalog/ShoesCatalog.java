package videoshop.shoes.catalog;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

import videoshop.shoes.catalog.Shoes.ShoesType;


public interface ShoesCatalog extends Catalog<Shoes>{
	static final Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

	/**
	 * Returns all {@link Disc}s by type ordered by the given {@link Sort}.
	 *
	 * @param type must not be {@literal null}.
	 * @param sort must not be {@literal null}.
	 * @return the discs of the given type, never {@literal null}.
	 */
	Iterable<Shoes> findByType(ShoesType type, Sort sort);

	/**
	 * Returns all {@link Disc}s by type ordered by their identifier.
	 *
	 * @param type must not be {@literal null}.
	 * @return the discs of the given type, never {@literal null}.
	 */
	default Iterable<Shoes> findByType(ShoesType type) {
		return findByType(type, DEFAULT_SORT);
	}
}
