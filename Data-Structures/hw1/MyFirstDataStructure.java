/**
 * @param <T> The type of the satellite data of the elements in the data structure.
 */
public class MyFirstDataStructure<T> {
	/*
     * You may add any fields that you wish to add.
     * Remember that the use of built-in Java classes is not allowed,
     * the only variables types you can use are:
     * 	-	the given classes in the assignment
     * 	-	basic arrays
     * 	-	primitive variables
     */
	private MyAVLTree<T> firstDS;
	private MyLinkedList<T> orderList;
	private TreeNode<T> maxValue;
	/***
     * This function is the Init function.
	 * @param N The maximum number of elements in the data structure at each time.
     */
	public MyFirstDataStructure(int N) {
		this.firstDS = new MyAVLTree<>();
		this.orderList = new MyLinkedList<>();
		this.maxValue = null;
	}
	
	public void insert(Element<T> x) {
		TreeNode<T> newNode = new TreeNode<>(x);
		ListLink<T> newLink = new ListLink<>(x);
		if (maxValue == null || (maxValue.key() < newNode.key())){
			maxValue = newNode;
		}
		newNode.setLinkPointer(newLink);
		this.firstDS.insert(newNode);
		this.orderList.insert(newLink); //the tail will point to the first that we inserted
	}

	//utility function for findAndRemove(), the function return the node with the greatest key
	//time complexity of O(log n)
	private TreeNode<T> findNewMax(){
		TreeNode<T> node = this.firstDS.root();
		while (node.getRight() != null) {
			node = node.getRight();
		}
		return node;
	}

	public void findAndRemove(int k) {
		if (maxValue.key() == k){
			TreeNode<T> toDelete = this.firstDS.search(k);
			this.orderList.delete(toDelete.getLinkPointer());
			this.firstDS.delete(toDelete);
			maxValue = findNewMax();
		}
		else {
			TreeNode<T> toDelete = this.firstDS.search(k);
			this.orderList.delete(toDelete.getLinkPointer());
			this.firstDS.delete(toDelete);
		}
	}

	public Element<T> maximum() {
		return this.maxValue;
	}

	public Element<T> first() {
		return this.orderList.tail(); // Tail always points to the earliest inserted element, we insert from the head
	}

	public Element<T> last() {
		return this.orderList.head();
	}

}
