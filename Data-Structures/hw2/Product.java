/**
 * This class represents a product,
 * with id and quality.
 */
public class Product {
	private int id;
	private int quality;
	private Link<Product> listLink;
	
	public Product(int id, int quality) {
		this.id = id;
		this.quality = quality;
		listLink = null;
	}
	
	public int id() {
		return this.id;
	}
	
	public int quality() {
		return this.quality;
	}

	public Link<Product> getListLink() {
		return listLink;
	}

	public void setListLink(Link<Product> link) {
		listLink = link;
	}

	public boolean equals(Object other){
		boolean ans = false;        
		if (other instanceof Product) {
			Product castedOther = (Product) other;  
            ans = this.id() == castedOther.id() & this.quality() == castedOther.quality();
        }        
        return ans;
	}
	
}
