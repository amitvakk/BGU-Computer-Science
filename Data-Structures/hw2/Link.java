/**
 * This class represents a specific element in a specific data structure: 
 * a link in a linked-list.
 * @param <T> The type of the satellite data.
 */
public class Link<T> extends Element<T> {
	/*
     * You may add any fields that you wish to add.
     * Remember that the use of built-in Java classes is not allowed,
     * the only variables types you can use are: 
     * 	-	the given classes in the assignment
     * 	-	basic arrays
     * 	-	primitive variables
     */
	private Link<T> next;
	private Link<T> prev;
	
	public Link(int key, T satelliteData) {
		super(key, satelliteData);
		this.next = null;
		this.prev = null;
	}
	
	public Link(int key) {
		this(key, null);
	}
	
	public Link(Element<T> element) {
		this(element.key(), element.satelliteData());
	}
	
	public Link<T> getNext() {
		return this.next;
	}
	
	public Link<T> getPrev() {
		return this.prev;
	}
	
	public void setNext(Link<T> next) {
		this.next = next;
	}
	
	public void setPrev(Link<T> prev) {
		this.prev = prev;
	}

}
