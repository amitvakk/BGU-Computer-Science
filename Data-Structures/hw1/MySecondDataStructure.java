
public class MySecondDataStructure {
	private MyArray<Product> allProducts;
	private Product mostExpensive;
	private int[] raisePerQuality;
	private int[] productsPerQuality;
	private int totalQuality;
	private Product[] mostExpensivePerQuality;

	public MySecondDataStructure(int N) {
		this.allProducts = new MyArray<>(N);
		this.mostExpensive = null;
		this.raisePerQuality = new int[6];
		this.productsPerQuality = new int[6];
		this.totalQuality = 0;
		this.mostExpensivePerQuality = new Product[6];
	}
	
	public void insert(Product product) {
		ArrayElement<Product> toInsert = new ArrayElement<>(product.id(), product);
		this.allProducts.insert(toInsert);

		int q = product.quality();
		this.totalQuality = this.totalQuality + q;
		this.productsPerQuality[q] = this.productsPerQuality[q] + 1;

		if (this.mostExpensive == null || (this.mostExpensive.price()+ this.raisePerQuality[this.mostExpensive.quality()]) < product.price()){
			this.mostExpensive = product;
			if (this.mostExpensive.quality() == q){
				this.mostExpensivePerQuality[q]=product;
			}
		}
		if (this.mostExpensivePerQuality[q] == null || this.mostExpensivePerQuality[q].price()+ this.raisePerQuality[q] < product.price()){
			this.mostExpensivePerQuality[q] = product;
		}
		product.setPrice(product.price() - this.raisePerQuality[q]);
	}

	//utility function for findAndRemove()
	//the function return the Product with the most expensive price
	private Product findMostExpensive(){
		Product newMax = null;
		for (int i = 0; i < this.allProducts.size(); i = i + 1){
			Product toCompare = this.allProducts.get(i).satelliteData();
			if (newMax == null || newMax.price() + raisePerQuality[newMax.quality()] < toCompare.price()+ raisePerQuality[toCompare.quality()]) {
				newMax = toCompare;
			}
		}
		return newMax;
	}

	public void findAndRemove(int id) {
		ArrayElement<Product> toRemove = this.allProducts.search(id);
		int q = toRemove.satelliteData().quality();
		this.productsPerQuality[q] = this.productsPerQuality[q] - 1;
		this.totalQuality = this.totalQuality - q;
		if (mostExpensive.id() == id){
			this.allProducts.delete(toRemove);
			mostExpensive = findMostExpensive();
		}
		else {
			this.allProducts.delete(toRemove);
		}
	}
	
	public int medianQuality() {
		if (this.allProducts.size() == 0){
			return -1;
		}
		else {
			int totalProducts = 0;
			for (int i = 0; i < 6; i = i + 1) {
				totalProducts = totalProducts + this.productsPerQuality[i];
			}
			int medianIndex = (totalProducts - 1)/ 2; // ⌈n/2⌉
			int count = 0;

			for (int i = 0; i < 6; i = i + 1) {
				count = count + this.productsPerQuality[i];
				if (count > medianIndex) {
					return i;
				}
			}
			return -1;
		}
	}
	
	public double avgQuality() {
		if (this.allProducts.size() == 0){
			return -1;
		}
		double avg = ((double) this.totalQuality / this.allProducts.size()) ;
		return avg;
	}

	public void raisePrice(int raise, int quality) {
		this.raisePerQuality[quality] = this.raisePerQuality[quality] + raise;

		int mostRaise = this.raisePerQuality[this.mostExpensive.quality()];
		int theNewMostExpensivePrice = this.mostExpensivePerQuality[quality].price() + this.raisePerQuality[quality];
		if (this.mostExpensive.price() + mostRaise < theNewMostExpensivePrice){
			this.mostExpensive = this.mostExpensivePerQuality[quality];
		}
	}

	public Product mostExpensive() {
		return this.mostExpensive;
	}

}
