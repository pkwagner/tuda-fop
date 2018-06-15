package hospital;

import hospital.patients.AbstractPatient;
import hospital.patients.EmergencyPatient;
import hospital.patients.SeverelyInjuredPatient;
import hospital.patients.SlightlyInjuredPatient;
import hospital.patients.TreatmentPriority;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for various AbstractPatient child classes
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class TestPatientQueue {

    private AbstractPatient hanka, honey, kader, ginaLisa, futureHanka, futureHoney, futureKader, futureGinaLisa;

    //**************************************************************
    //********************** Helper method *************************
    //**************************************************************

    /**
     * Takes an array of patients and returns a shuffled copy.
     *
     * @param patients the patients
     * @return the patients in shuffled order
     */
    private static AbstractPatient[] getShuffledArray(AbstractPatient[] patients) {
        // Init random number generator with fixed seed
        Random random = new Random(12345);

        // Add patients to new list
        List<AbstractPatient> list = new ArrayList<>();
        for (AbstractPatient p : patients) {
            list.add(p);
        }

        AbstractPatient[] shuffledArray = new AbstractPatient[list.size()];
        Collections.shuffle(list, random);
        list.toArray(shuffledArray);
        return shuffledArray;
    }


    @Before
    public void setUp() throws Exception {
        // Create instances for every patient
        LocalTime currentTime = LocalTime.of(17, 30, 0);
        hanka = new SlightlyInjuredPatient("Hanka", currentTime);
        honey = new SeverelyInjuredPatient("Honey", currentTime, 7);
        kader = new SeverelyInjuredPatient("Kader", currentTime, 3);
        ginaLisa = new EmergencyPatient("Gina-Lisa", currentTime);

        // Creates duplicates of the patients above who arrive later
        LocalTime futureTime = LocalTime.of(18, 0, 0);
        futureHanka = new SlightlyInjuredPatient("Hanka", futureTime);
        futureHoney = new SeverelyInjuredPatient("Honey", futureTime, 7);
        futureKader = new SeverelyInjuredPatient("Kader", futureTime, 3);
        futureGinaLisa = new EmergencyPatient("Gina-Lisa", futureTime);
    }


    //**************************************************************
    //****************** Test patient categories *******************
    //**************************************************************

    // Check correct behavior of the getPriority() method
    // for each patient class.
    @Test
    public void validateGetPriority() {
        List<AbstractPatient> patients = new ArrayList<>();
        patients.add(new SlightlyInjuredPatient("Hanka", LocalTime.now()));
        patients.add(new SeverelyInjuredPatient("Honey", LocalTime.now(), 5));
        patients.add(new EmergencyPatient("Gina-Lisa", LocalTime.now()));

        assertEquals(TreatmentPriority.LOW, patients.get(0).getPriority());
        assertEquals(TreatmentPriority.MEDIUM, patients.get(1).getPriority());
        assertEquals(TreatmentPriority.HIGH, patients.get(2).getPriority());
    }

    // Check correct behavior of the getCatInfo() method
    // for each patient class.
    @Test
    public void validateGetCatInfo() {
        SlightlyInjuredPatient hanka = new SlightlyInjuredPatient("Hanka", LocalTime.now());
        SeverelyInjuredPatient honey = new SeverelyInjuredPatient("Honey", LocalTime.now(), 5);
        EmergencyPatient ginaLisa = new EmergencyPatient("Gina-Lisa", LocalTime.now());

        assertEquals("T3/Green", hanka.getCatInfo());
        assertEquals("T2/Yellow-5", honey.getCatInfo());
        assertEquals("T1/Red", ginaLisa.getCatInfo());
    }

    // Check correct behavior of the compareTo(...) method WITHIN each patient
    // class, i.e. check ordering for each category separately. Use at least
    // three object instances per patient class!
    @Test
    public void validateCompareTo() {
        // Test hanka against everybody else
        assertEquals(0, hanka.compareTo(hanka));
        assertEquals(-1, hanka.compareTo(futureHanka));
        assertEquals(1, hanka.compareTo(honey));
        assertEquals(1, hanka.compareTo(kader));
        assertEquals(1, hanka.compareTo(ginaLisa));

        // Test honey against everybody else
        assertEquals(-1, honey.compareTo(hanka));
        assertEquals(0, honey.compareTo(honey));
        assertEquals(-1, honey.compareTo(futureHoney));
        assertEquals(-1, honey.compareTo(kader));
        assertEquals(1, honey.compareTo(ginaLisa));

        // Test kader against everybody else
        assertEquals(-1, kader.compareTo(hanka));
        assertEquals(1, kader.compareTo(honey));
        assertEquals(0, kader.compareTo(kader));
        assertEquals(-1, kader.compareTo(futureKader));
        assertEquals(1, kader.compareTo(ginaLisa));

        // Test ginaLisa against everybody else
        assertEquals(-1, ginaLisa.compareTo(hanka));
        assertEquals(-1, ginaLisa.compareTo(honey));
        assertEquals(-1, ginaLisa.compareTo(kader));
        assertEquals(0, ginaLisa.compareTo(ginaLisa));
        assertEquals(-1, ginaLisa.compareTo(futureGinaLisa));
    }

    //**************************************************************
    //********************* Test patient queue *********************
    //**************************************************************

    // Check correct behavior of the process method of the patient queue class,
    // i.e. patients are processed in correct order. Initialize test by adding
    // patients in random order. To generate a randomly ordered patient list,
    // you can use the provided getShuffledArray(AbstractPatient[] patients) method.
    @Test
    public void validateProcessQueue() {
        // Create unsorted and shuffled array with all patients
        AbstractPatient[] allPatients = {hanka, honey, kader, ginaLisa, futureHanka, futureHoney, futureKader, futureGinaLisa};
        AbstractPatient[] shuffledPatients = getShuffledArray(allPatients);

        // Push array element by element to a new PatientQueue instance to sort it
        // 'AbstractPatient' is used because the array contains members of various 'AbstractPatient' subclasses
        PatientQueue<AbstractPatient> patientQueue = new PatientQueue<>();
        Arrays.stream(shuffledPatients).forEach(patientQueue::addPatient);

        // Compare returned ArrayList with the correct sorted one
        List<String> patientsString = patientQueue.processQueue();
        assertEquals(Arrays.asList(
                new String[]{
                        ginaLisa.toString(),
                        futureGinaLisa.toString(),
                        honey.toString(),
                        futureHoney.toString(),
                        kader.toString(),
                        futureKader.toString(),
                        hanka.toString(),
                        futureHanka.toString()}),
                patientsString);
    }

    @Test
    public void validatePatientToString() {
        // Check if the Patient classes 'toString' function works
        assertEquals("T3/Green          -> 17:30 - Hanka", hanka.toString());
        assertEquals("T2/Yellow-3       -> 17:30 - Kader", kader.toString());
        assertEquals("T2/Yellow-7       -> 18:00 - Honey", futureHoney.toString());
        assertEquals("T1/Red            -> 17:30 - Gina-Lisa", ginaLisa.toString());
    }
}
