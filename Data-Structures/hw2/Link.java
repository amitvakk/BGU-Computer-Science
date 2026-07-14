public class Link<T> extends Element<T> {
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
