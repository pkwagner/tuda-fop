//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Test;
//
//import junit.framework.JUnit4TestAdapter; // for main
//import junit.textui.TestRunner; // for the test runner
//
//public class StudentTests {
//	// Final result for array 'current'
//	private static final double[][] refFinalCurrent = new double[][] {
//			{ 0.99502953, 0.990178298, 0.985561214, 0.981285109, 0.977445441, 0.974124404, 0.971389541, 0.969293817,
//					0.9678758, 0.967160637, 0.967160637, 0.9678758, 0.969293817, 0.971389541, 0.974124404, 0.977445441,
//					0.981285109, 0.985561214, 0.990178298, 0.99502953 },
//			{ 0.990461748, 0.981152274, 0.972292198, 0.964086356, 0.956718152, 0.950345063, 0.94509696, 0.941075245,
//					0.938354127, 0.936981719, 0.936981719, 0.938354127, 0.941075245, 0.94509696, 0.950345063,
//					0.956718152, 0.964086356, 0.972292198, 0.981152274, 0.990461748 },
//			{ 0.986666682, 0.973653212, 0.961267861, 0.949797166, 0.939497246, 0.930588525, 0.92325225, 0.917630444,
//					0.913826612, 0.911908177, 0.911908177, 0.913826612, 0.917630444, 0.92325225, 0.930588525,
//					0.939497246, 0.949797166, 0.961267861, 0.973653212, 0.986666682 },
//			{ 0.983951821, 0.968288576, 0.953381419, 0.939575054, 0.927177974, 0.916455233, 0.907625239, 0.900858695,
//					0.89628038, 0.893971302, 0.893971302, 0.89628038, 0.900858695, 0.907625239, 0.916455233,
//					0.927177974, 0.939575054, 0.953381419, 0.968288576, 0.983951821 },
//			{ 0.982537073, 0.965493038, 0.949271701, 0.934248256, 0.920758263, 0.909090286, 0.899481841, 0.892118815,
//					0.887136874, 0.884624249, 0.884624249, 0.887136874, 0.892118815, 0.899481841, 0.909090286,
//					0.920758263, 0.934248256, 0.949271701, 0.965493038, 0.982537073 },
//			{ 0.982537073, 0.965493038, 0.949271701, 0.934248256, 0.920758263, 0.909090286, 0.899481841, 0.892118815,
//					0.887136874, 0.884624249, 0.884624249, 0.887136874, 0.892118815, 0.899481841, 0.909090286,
//					0.920758263, 0.934248256, 0.949271701, 0.965493038, 0.982537073 },
//			{ 0.983951821, 0.968288576, 0.953381419, 0.939575054, 0.927177974, 0.916455233, 0.907625239, 0.900858695,
//					0.89628038, 0.893971302, 0.893971302, 0.89628038, 0.900858695, 0.907625239, 0.916455233,
//					0.927177974, 0.939575054, 0.953381419, 0.968288576, 0.983951821 },
//			{ 0.986666682, 0.973653212, 0.961267861, 0.949797166, 0.939497246, 0.930588525, 0.92325225, 0.917630444,
//					0.913826612, 0.911908177, 0.911908177, 0.913826612, 0.917630444, 0.92325225, 0.930588525,
//					0.939497246, 0.949797166, 0.961267861, 0.973653212, 0.986666682 },
//			{ 0.990461748, 0.981152274, 0.972292198, 0.964086356, 0.956718152, 0.950345063, 0.94509696, 0.941075245,
//					0.938354127, 0.936981719, 0.936981719, 0.938354127, 0.941075245, 0.94509696, 0.950345063,
//					0.956718152, 0.964086356, 0.972292198, 0.981152274, 0.990461748 },
//			{ 0.99502953, 0.990178298, 0.985561214, 0.981285109, 0.977445441, 0.974124404, 0.971389541, 0.969293817,
//					0.9678758, 0.967160637, 0.967160637, 0.9678758, 0.969293817, 0.971389541, 0.974124404, 0.977445441,
//					0.981285109, 0.985561214, 0.990178298, 0.99502953 } };
//
//	@Test
//	public void test() {
//		int width = 20;
//		int height = 10;
//
//		JacobiSimulation sim = new JacobiSimulation(new String[] { "" + width, "" + height, "100", "10" });
//
//		double[][] current = sim.getCurrent();
//		assertEquals("Init: number of lines (1st dimenson) must be " + height + " ", height, current.length);
//		assertNotNull("First line of current must not be null ", current[0]);
//		assertEquals("Init: number of rows (2nd dimension) must be " + width + " ", width, current[0].length);
//		double[][] previous = sim.getPrevious();
//
//		// ******************************************************************************************************
//		// **************************************** Check initialization ****************************************
//		// ******************************************************************************************************
//
//		assertEquals("Init: Abs diff is not correctly initialized with zero", 0.0, sim.getAbsoluteDifference(), 0.0);
//
//		// Check null refs
//		assertNotNull("Init: Array 'current' is not initialized", current);
//		assertNotNull("Init: Array 'previous' is not initialized", previous);
//		// Check identical refs
//		assertEquals("Init: Array 'current' and array 'previous' are the same", false, current == previous);
//
//		// Check if width and height is correctly set (also for transposed
//		// matrix)
//		boolean dimOneIsHeight = current.length == height && current[0].length == width;
//		boolean dimOneIsWidth = current.length == width && current[0].length == height;
//		assertTrue("Init: Height and/or width values are not correctly set in array 'current'", dimOneIsHeight || dimOneIsWidth);
//		boolean previousHasSameDimAsCurrent = current.length == previous.length	&& current[0].length == previous[0].length;
//		assertTrue("Init: Height and/or width values of array 'previous' are not the same as for array 'current'", previousHasSameDimAsCurrent);
//
//		// Check if arrays are filled with zero
//		for (int i = 0; i < current.length; i++) {
//			for (int j = 0; j < current[0].length; j++) {
//				assertEquals("Init: Value in array 'current' is not zero.", 0.0, current[i][j], 0.0);
//				assertEquals("Init: Value in array 'previous' is not zero.", 0.0, previous[i][j], 0.0);
//			}
//		}
//
//		// ******************************************************************************************************
//		// ****************************************** Check simulation ******************************************
//		// ******************************************************************************************************
//		// Start simulation
//		sim.start();
//
//		current = sim.getCurrent();
//		// Transpose results if necessary
//		if (dimOneIsWidth)
//			current = transposeArray(current);
//
//		// Check final values in array 'current'
//		boolean checkValues = compareValues(refFinalCurrent, current);
//		assertTrue("Final values in array 'current' are not correct.", checkValues);
//		assertEquals("Final absolute difference value is not correct.", 0.2895285257484368, sim.getAbsoluteDifference(), 0.0001);
//	}
//
//	private boolean compareValues(double[][] a1, double[][] a2){
//		for (int i = 0; i < a1.length; i++) {
//			for (int j = 0; j < a1[0].length; j++) {
//				if (Math.abs(a1[i][j] - a2[i][j]) >= 0.0001) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	private double[][] transposeArray(double[][] array){
//		double[][] transposed = new double[array[0].length][array.length];
//		for (int i = 0; i < array.length; i++){
//			for (int j = 0; j < array[0].length; j++){
//				transposed[j][i] = array[i][j];
//			}
//		}
//		return transposed;
//	}
//
//	/**
//	 * Create a test suite for testing from the shell
//	 *
//	 * @return a junit.framework.Test instance containing all tests of this
//	 *         class
//	 */
//	public static junit.framework.Test suite() {
//		return new JUnit4TestAdapter(StudentTests.class);
//	}
//
//	/**
//	 * Main method, used to create a junit.textui.TestRunner
//	 *
//	 * @param args
//	 *            ignored for this test
//	 */
//	public static void main(String[] args) {
//		TestRunner.runAndWait(suite());
//	}
//
//}
