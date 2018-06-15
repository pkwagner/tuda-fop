package hospital.patients;

import java.time.LocalTime;

/**
 * Represents a slightly injured patient
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class SlightlyInjuredPatient extends AbstractPatient {

    /**
     * Creates a new patient object using the specified name and arrival time.
     *
     * @param patientName        the name of the patient
     * @param patientArrivalTime the arrival time of the patient
     */
    public SlightlyInjuredPatient(String patientName, LocalTime patientArrivalTime) {
        super(patientName, patientArrivalTime);
    }

    @Override
    public TreatmentPriority getPriority() {
        return TreatmentPriority.LOW;
    }

    @Override
    public String getCatInfo() {
        return "T3/Green";
    }

    @Override
    public int compareTo(AbstractPatient other) {
        if (other.getPriority() == TreatmentPriority.MEDIUM || other.getPriority() == TreatmentPriority.HIGH)
            return 1;

        return this.arrivalTime.compareTo(other.arrivalTime);
    }
}
