package freezer.conditions;

import freezer.EnergyClass;
import freezer.Freezer;

/**
 * Checks for the energy efficiency requirement of the EU
 * 
 * @author Nicolas Weber
 */
public class EUEnergyEfficiency implements Condition {
	@Override
	public boolean isFullfilled(Freezer freezer) {
		return freezer.getEnergyEfficiency() >= EnergyClass.A.getMinEfficiency();
	}
}