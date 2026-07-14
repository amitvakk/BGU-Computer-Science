/**
 * @param <T> The type of the satellite data of the elements in the dynamic-set.
 */
public class MyDynamicSet<T> {
	/*
     * You may add any fields that you wish to add.
     * Remember that the use of built-in Java classes is not allowed,
     * the only variables types you can use are: 
     * 	-	the given classes in the assignment
     * 	-	basic arrays
     * 	-	primitive variables
     */

	private MySortedLinkedList<T> listSet;
	/**
	 * The constructor should initiate an empty dynamic-set.
	 * @param N The maximum number of elements in the dynamic set at each time.
	 */
	public MyDynamicSet(int N) {
		this.listSet = new MySortedLinkedList<>();
	}
	
	public Element<T> search(int k) {
		return this.listSet.search(k);
	}
	
	public void insert(Element<T> x) {
		ListLink<T> newLink = new ListLink<>(x);
		this.listSet.insert(newLink);
	}
	
	public void delete(Element<T> x) {
		ListLink<T> toLink = (ListLink<T>)x;
		this.listSet.delete(toLink);
	}
	
	public Element<T> minimum() {
		return this.listSet.head();
	}
	
	public Element<T> maximum() {
		return this.listSet.tail();
	}
	
	public Element<T> successor(Element<T> x) {
		Element<T> element = ((ListLink<T>)x).getNext();
		return element;
	}
	
	public Element<T> predecessor(Element<T> x) {
		Element<T> element = ((ListLink<T>)x).getPrev();
		return element;
	}
}
