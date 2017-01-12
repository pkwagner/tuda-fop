package freezer.tests;

import freezer.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.LinkedList;

public class StudentTest {
	class TestCondition implements freezer.conditions.Condition {
		@Override
		public boolean isFullfilled(Freezer freezer) {
			return freezer.getArticleNumber().startsWith("FSDN10101001S");
		}
	}

	@Test
	public void testFreezer() {
		Freezer f = new Freezer(1.0, 1.0, 1.0, 0.05);
		f.setDoor(new freezer.doors.Standard());
		f.setInterior(new freezer.interiors.ArcticSpecial(f));

		assertEquals(1.0, f.getWidth(), 0.0);
		assertEquals(1.0, f.getHeight(), 0.0);
		assertEquals(1.0, f.getDepth(), 0.0);
		assertEquals(0.05, f.getWallThickness(), 0.0);

		assertEquals("FSDN10101005SA", f.getArticleNumber());
		assertEquals(1.0, f.getOuterVolume(), 0.0);
		assertEquals(6.0, f.getOuterSurfaceArea(), 0.0);

		assertEquals(0.729, f.getInnerVolume(), 0.0001);
		assertEquals(4.86, f.getInnerSurfaceArea(), 0.0001);

		assertEquals(0.15, f.getEnergyEfficiency(), 0.0001);
		// TODO Was this what you wanted to check?
		assertEquals(EnergyClass.A, EnergyClass.getEnergyClass(f.getEnergyEfficiency()));

		assertEquals(51.57, f.getPrice(), 0.0);
	}

	@Test
	public void testGetAvailableFreezers() {
		LinkedList list = Freezer.getAvailableFreezers(new freezer.conditions.Condition[]{ new TestCondition() });

		assertEquals(2, list.size());

		assertEquals("FSDN10101001SS", ((Freezer)list.get(0)).getArticleNumber());
		assertEquals("FSDN10101001S_", ((Freezer)list.get(1)).getArticleNumber());
	}
}
