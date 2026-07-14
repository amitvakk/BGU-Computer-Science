/**
 * @param <T> The type of the satellite data of the elements in the dynamic-set.
 */
public class MyDynamicSet<T> {

	private MySortedArray<T> dynamicSet;
	private static final int DEFAULT_CAPACITY = 20;

	/***
     * The constructor should initiate an empty dynamic-set.
     */
	public MyDynamicSet() {
		this.dynamicSet = new MySortedArray<T>(DEFAULT_CAPACITY);
	}
	
	public Element<T> search(int k) {
		return dynamicSet.search(k);
	}
	
	public void insert(Element<T> x) {
		ArrayElement<T> toInsert;
		if (x instanceof ArrayElement<?>){
			toInsert = (ArrayElement<T>)x;
		}
		else{
			toInsert = new ArrayElement<>(x);
		}
		dynamicSet.insert(toInsert);
	}
	
	public void delete(Element<T> x) {
		dynamicSet.delete((ArrayElement<T>)x);
	}
	
	public Element<T> minimum() {
		if (dynamicSet.size()==0){
			return null;
		}
		return dynamicSet.get(0);
	}
	
	public Element<T> maximum() {
		if (dynamicSet.size()==0){
			return null;
		}
		return dynamicSet.get(dynamicSet.size()-1);
	}
	
	public Element<T> successor(Element<T> x) {
		if (!(x instanceof ArrayElement<?>)){
			return null;
		}
		ArrayElement<T> castedX = (ArrayElement<T>)x;
		int followIndex = castedX.index() + 1;
		if (followIndex >= dynamicSet.size()){
			 return null;
		}
		return dynamicSet.get(followIndex);
	}
	
	public Element<T> predecessor(Element<T> x) {
		if (!(x instanceof ArrayElement<?>)){
			return null;
		}
		ArrayElement<T> castedX = (ArrayElement<T>)x;
		int prevIndex = castedX.index() - 1;
		if (prevIndex < 0){
			return null;
		}
		return dynamicSet.get(prevIndex);
	}
}
