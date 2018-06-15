package hospital.patients;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Base class to model patients.
 *
 * @author Martin Hess
 * @version 1.0
 */
public abstract class AbstractPatient implements Comparable<AbstractPatient> {

    /**
     * The patient's name
     */
    private final String name;

    /**
     * The time the patient arrived.
     */
    protected final LocalTime arrivalTime;

    /**
     * Creates a new patient object using the specified name and arrival time.
     *
     * @param patientName        the name of the patient
     * @param patientArrivalTime the arrival time of the patient
     */
    public AbstractPatient(String patientName, LocalTime patientArrivalTime) {
        name = patientName;
        arrivalTime = patientArrivalTime;
    }

    /**
     * Returns the treatment priority of this patient.
     *
     * @return the treatment priority of this patient
     */
    public abstract TreatmentPriority getPriority();

    /**
     * Returns the category info string of this patient.
     *
     * @return the category info string of this patient
     */
    public abstract String getCatInfo();

    /**
     * Returns a string representation of this patient.
     *
     * @return a String representation of this patient.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String info = getCatInfo();
        sb.append(String.format("%-18s", info)).append("-> ");
        sb.append(DateTimeFormatter.ofPattern("HH:mm").format(arrivalTime));
        sb.append(" - ").append(name);
        return sb.toString();
    }
}
