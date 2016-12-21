package freezer.conditions;

import freezer.Freezer;

/**
 * Used to check for Freezer conditions
 *
 * @author Nicolas Weber
 */
public interface Condition {
	/**
	 * Checks if the Freezer fullfills the requirements
	 * 
	 * @param freezer the concrete freezer
	 * @return true if the freezer passed in fulfills the condition, else false.
	 */
	public boolean isFullfilled(Freezer freezer);
}
