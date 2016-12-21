package freezer.conditions;

import freezer.Freezer;

/**
 * Checks for the social requirement of the EU, that every citizen should have a Freezer
 * for less than 100 &euro;
 *
 * @author Nicolas Weber
 */
public class EUSocial implements Condition {
	@Override
	public boolean isFullfilled(Freezer freezer) {
		return freezer.getPrice() <= 100.0;
	}
}
