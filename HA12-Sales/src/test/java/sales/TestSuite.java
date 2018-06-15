package sales;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestSuite {

    private static double PRECISION = 0.001;

    // The seed value for the random generator
    private final long seed = 1234;
    // The number of samples -> the number of Sale objects in the list
    private final int samples = 30;

    //****************************************************************
    //********* INPUT vars: Used as input during the tests ***********
    //****************************************************************
    // The tested statistics interface
    private final ISaleStatistics INPUT_Stats = new SaleStats();
    // The list of sales that should be processed in the tests
    private final List<Sale<?>> INPUT_Sales = sales.CarSalesExample.generateSamples(samples, seed);
    // The month for testing the month filter
    private final Month INPUT_Month = sales.CarSalesExample.sampleRandomMonth(seed, INPUT_Sales);
    // The start and end dates for testing the time period filter
    private final LocalDate[] tmpDates = sales.CarSalesExample.sampleRandomDates(seed, INPUT_Sales);
    private final LocalDate INPUT_StartDate = tmpDates[0];
    private final LocalDate INPUT_EndDate = tmpDates[1];

    //****************************************************************
    //********* CORRECT_OUTPUT variables: Used for comparison ********
    //****************************************************************
    // The correct month filter result
    private Set<Sale<?>> CORRECT_OUTPUT_SalesFilteredByMonth;
    // The correct time period filter result
    private Set<Sale<?>> CORRECT_OUTPUT_SalesFilteredByTimePeriod;
    // The correct sorted by date result
    private List<Sale<?>> CORRECT_OUTPUT_SalesSortedByDate;
    // The correct total, average and standard deviation values
    private double CORRECT_OUTPUT_Total;
    private double CORRECT_OUTPUT_Average;
    private double CORRECT_OUTPUT_StdDev;

    /*
     * Initializes tests. DO NOT CHANGE! 
     */
    @Before
    public void initTests() {
        //*********************************************************
        //****************** Prepare filter test ******************
        //*********************************************************
        CORRECT_OUTPUT_SalesFilteredByMonth = new HashSet<>();
        CORRECT_OUTPUT_SalesFilteredByTimePeriod = new HashSet<>();
        for (Sale<?> s : INPUT_Sales) {
            if (s.getDate().getMonth() == INPUT_Month) {
                CORRECT_OUTPUT_SalesFilteredByMonth.add(s);
            }
            if (!s.getDate().isBefore(INPUT_StartDate) && !s.getDate().isAfter(INPUT_EndDate)) {
                CORRECT_OUTPUT_SalesFilteredByTimePeriod.add(s);
            }
        }

        //*********************************************************
        //****************** Prepare sorting test *****************
        //*********************************************************
        CORRECT_OUTPUT_SalesSortedByDate = new ArrayList<>(INPUT_Sales);
        Collections.sort(CORRECT_OUTPUT_SalesSortedByDate, Comparator.comparing(Sale::getDate));

        //*********************************************************
        //**************** Prepare statistics test ****************
        //*********************************************************
        CORRECT_OUTPUT_Total = 0;
        CORRECT_OUTPUT_Average = 0;
        CORRECT_OUTPUT_StdDev = 0;

        // Calc total
        for (Sale<?> s : INPUT_Sales) {
            CORRECT_OUTPUT_Total += s.getPrice();
        }
        // Calc avg
        CORRECT_OUTPUT_Average = CORRECT_OUTPUT_Total / INPUT_Sales.size();

        // Calc standard deviation
        for (Sale<?> s : INPUT_Sales) {
            CORRECT_OUTPUT_StdDev += (s.getPrice() - CORRECT_OUTPUT_Average) * (s.getPrice() - CORRECT_OUTPUT_Average);
        }
        CORRECT_OUTPUT_StdDev /= INPUT_Sales.size();
        CORRECT_OUTPUT_StdDev = Math.sqrt(CORRECT_OUTPUT_StdDev);
    }

    /*
     * ISaleStatistics::getMonthFilter
     * Input: 
     *    - INPUT_Stats: The tested statistics interface
     *    - INPUT_Sales: The list of sales that should be processed in the test
     *    - INPUT_Month: The month for testing the month filter
     * Test:
     *    - Check against CORRECT_OUTPUT_SalesFilteredByMonth
     */
    @Test
    public void testGetFilteredSalesAsSet_Month() {
        Predicate<Sale<?>> monthFilter = INPUT_Stats.getMonthFilter(INPUT_Month);

        Set<Sale<?>> filteredSales = INPUT_Stats.getFilteredSalesAsSet(INPUT_Sales, monthFilter);
        assertEquals(CORRECT_OUTPUT_SalesFilteredByMonth, filteredSales);
    }

    /*
     * ISaleStatistics::getTimePeriodFilter
     * Input: 
     *    - INPUT_Stats: The tested statistics interface
     *    - INPUT_Sales: The list of sales that should be processed in the test
     *    - INPUT_StartDate: The start date for testing the time period filter
     *    - INPUT_EndDate: The end date for testing the time period filter
     * Test:
     *    - Check against CORRECT_OUTPUT_SalesFilteredByTimePeriod
     */
    @Test
    public void testGetFilteredSalesAsSet_TimePeriod() {
        Predicate<Sale<?>> timeFilter = INPUT_Stats.getTimePeriodFilter(INPUT_StartDate, INPUT_EndDate);

        Set<Sale<?>> filteredSales = INPUT_Stats.getFilteredSalesAsSet(INPUT_Sales, timeFilter);
        assertEquals(CORRECT_OUTPUT_SalesFilteredByTimePeriod, filteredSales);
    }

    /*
     * order of the sale dates
     * Input: 
     *    - INPUT_Stats: The tested statistics interface
     *    - INPUT_Sales: The list of sales that should be processed in the test 
     * Test:
     *    - Check against CORRECT_OUTPUT_SalesSortedByDate
     */
    @Test
    public void testGetSortedSalesList() {
        Comparator<Sale<?>> dateComparator = ((o1, o2) -> {
            if (o1.getDate().isBefore(o2.getDate()))
                return -1;
            if (o1.getDate().isAfter(o2.getDate()))
                return 1;

            return 0;
        });

        assertEquals(CORRECT_OUTPUT_SalesSortedByDate, INPUT_Stats.getSortedSalesList(INPUT_Sales, dateComparator));
    }

    /*
     * Input: 
     *    - INPUT_Stats: The tested statistics interface 
     *    - INPUT_Sales: The list of sales that should be processed in the test 
     * Test:
     *    - Check against CORRECT_OUTPUT_Total
     */
    @Test
    public void testCalculateTotal() {
        assertEquals(CORRECT_OUTPUT_Total, INPUT_Stats.getTotalRevenue(INPUT_Sales), PRECISION);
    }

    /*
     * Input: 
     *    - INPUT_Stats: The tested statistics interface
     *    - INPUT_Sales: The list of sales that should be processed in the test
     * Test:
     *    - Check against CORRECT_OUTPUT_Average
     */
    @Test
    public void testCalculateAverage() {
        assertEquals(CORRECT_OUTPUT_Average, INPUT_Stats.getAverageRevenue(INPUT_Sales), PRECISION);
    }

    /*
     * Input: 
     *    - INPUT_Stats: The tested statistics interface 
     *    - INPUT_Sales: The list of sales that should be processed in the test
     * Test:
     *    - Check against CORRECT_OUTPUT_StdDev
     */
    @Test
    public void testCalculateStandardDeviation() {
        assertEquals(CORRECT_OUTPUT_StdDev, INPUT_Stats.getStdDev(INPUT_Sales), PRECISION);
    }
}
