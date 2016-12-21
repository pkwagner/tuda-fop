package freezer;

/**
 * Represents an abstract part of a freezer 
 * @author Nicolas Weber
 */
public interface Part {
	/**
	 * Returns the article number of this part
	 *
	 * @return the article number as a String
	 */
	public String getArticleNumber();
	
	/**
	 * Returns the price of this part
	 *
	 * @return the price for this part
	 */
	public double getPrice();
}
