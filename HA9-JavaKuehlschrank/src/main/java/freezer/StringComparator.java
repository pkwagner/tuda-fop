package freezer;

import java.util.Comparator;

public class StringComparator implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		return o1.toString().compareTo(o2.toString());
	}
}
