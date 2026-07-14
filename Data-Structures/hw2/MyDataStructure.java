import java.util.LinkedList;

public class MyDataStructure {

	private MyAVLTree<Product> allOtherPTree;
	private MyAVLTree<Product> zeroTree;
	private MyLinkedList<Product>[] qualityLists;
	private MyLinkedList<Product> zeroList;

	private int[] productsPerQuality;
	private int totalCount;
	private long totalQualitySum;

	/***
     * This function is the Init function.
     */
	public MyDataStructure() {
		allOtherPTree = new MyAVLTree<Product>();
		zeroTree = new MyAVLTree<Product>();
		zeroList = new MyLinkedList<Product>();
		totalCount = 0;
		totalQualitySum = 0;
		productsPerQuality = new int[6];
		qualityLists = (MyLinkedList<Product>[]) new MyLinkedList[6];
		for (int i = 0; i < 6; i = i + 1){
			qualityLists[i] = new MyLinkedList<Product>();
		}
	}
	
	public void insert(int id, int quality) {
		Product newProduct = new Product(id,quality);
		TreeNode<Product> newTreeNode = new TreeNode<Product>(id,newProduct);
		Link<Product> newLink = new Link<Product>(id,newProduct);

		totalCount = totalCount + 1;
		productsPerQuality[quality] = productsPerQuality[quality] + 1;

		if(quality == 0) {
			zeroTree.insert(newTreeNode);
			zeroList.insert(newLink);
			newProduct.setListLink(newLink);
		}
		else {
			allOtherPTree.insert(newTreeNode);
			qualityLists[quality].insert(newLink);
			newProduct.setListLink(newLink);

			totalQualitySum = totalQualitySum + quality;
		}
	}
	
	public void delete(int id) {
		TreeNode<Product> toDelete = (TreeNode<Product>)allOtherPTree.search(id);
		MyAVLTree<Product> targetTree = allOtherPTree;
		boolean isZeroJank = false;

		if (toDelete == null){
			toDelete = (TreeNode<Product>)zeroTree.search(id);
			targetTree = zeroTree;
			isZeroJank = true;
		}
		if (toDelete != null) {
			Product pToDelete = toDelete.satelliteData();
			int quality = pToDelete.quality();
			if (isZeroJank){
				this.zeroList.delete(pToDelete.getListLink());
			}
			else {
				qualityLists[quality].delete(pToDelete.getListLink());
			}
			targetTree.delete(toDelete);

			productsPerQuality[quality] = productsPerQuality[quality] - 1;
			totalCount = totalCount - 1;
			if (quality != 0){
				totalQualitySum = totalQualitySum - quality;
			}
		}

	}
	
	public int medianQuality() {
		if (totalCount == 0){
			return -1;
		}
		int medianIndex = (totalCount - 1) / 2; // ⌈n/2⌉
		int count = 0;

		for (int i = 0; i < 6; i = i + 1) {
			count = count + this.productsPerQuality[i];
			if (count > medianIndex) {
				return i;
			}
		}
		return -1;
	}
	
	public double avgQuality() {
		if (totalCount == 0){
			return -1;
		}
		return (double) totalQualitySum / totalCount;
	}
	
	public MyLinkedList<Product> junkWorst() {

		int k = productsPerQuality[0];
		if (k == 0){
			return new MyLinkedList<Product>();
		}

		MyLinkedList<Product> junkList = zeroList;
		totalCount = totalCount - k;
		productsPerQuality[0] = 0;
		zeroList = new MyLinkedList<Product>();
		return junkList;
	}
	
}
