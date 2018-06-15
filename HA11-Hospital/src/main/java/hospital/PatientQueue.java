package hospital;

import hospital.heap.ArrayListHeap;
import hospital.patients.AbstractPatient;

import java.util.ArrayList;

/**
 * Represents a patient queue where patients with
 * a higher priority (according to their compareTo - no only TreatmentPriority) gets first.
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class PatientQueue<T extends AbstractPatient> {

    private final ArrayListHeap<AbstractPatient> patients = new ArrayListHeap<>();

    /**
     * Adds a new patient to the queue
     *
     * @param patient the new patient
     */
    public void addPatient(T patient) {
        patients.push(patient);
    }

    /**
     * Travels through the list and deletes the elements out of it.
     * Every removed element will be added to the result list.
     *
     * <p>These operations are ordered according their compareTo() method.
     * More urgent operations comes first and so are these elements in that list.
     *
     * @return a ordered list with the to string representation of the patients
     */
    public ArrayList<String> processQueue() {
        ArrayList<String> patientStrings = new ArrayList<>();

        while (!patients.isEmpty())
            patientStrings.add(patients.pop().toString());

        return patientStrings;
    }
}
