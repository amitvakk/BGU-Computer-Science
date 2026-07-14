public class MyDynamicSet<T> {
	private MySortedLinkedList<T> listSet;
	
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
