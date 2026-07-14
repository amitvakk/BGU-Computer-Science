public class ArrayElement<T> extends Element<T> {
	private int index;		//The index of the element in the array.
	
	public ArrayElement(int key, T satelliteData) {
		super(key, satelliteData);
	}
	
	public ArrayElement(int key) {
		this(key, null);
	}
	
	public ArrayElement(Element<T> element) {
		this(element.key(), element.satelliteData());
	}

	public int index() {
		return this.index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
}
