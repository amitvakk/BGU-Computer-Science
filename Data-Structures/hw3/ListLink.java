public class ListLink<T> extends Element<T> {
	private ListLink<T> next;
	private ListLink<T> prev;
	
	public ListLink(int key, T satelliteData) {
		super(key, satelliteData);
		this.next = null;
		this.prev = null;
	}
	
	public ListLink(int key) {
		this(key, null);
	}
	
	public ListLink(Element<T> element) {
		this(element.key(), element.satelliteData());
	}
	
	public ListLink<T> getNext() {
		return this.next;
	}
	
	public ListLink<T> getPrev() {
		return this.prev;
	}
	
	public void setNext(ListLink<T> next) {
		this.next = next;
	}
	
	public void setPrev(ListLink<T> prev) {
		this.prev = prev;
	}

}
