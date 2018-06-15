package hospital.patients;

import java.time.LocalTime;

/**
 * Represents a emergency patient
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class EmergencyPatient extends AbstractPatient {

    /**
     * Creates a new patient object using the specified name and arrival time.
     *
     * @param patientName        the name of the patient
     * @param patientArrivalTime the arrival time of the patient
     */
    public EmergencyPatient(String patientName, LocalTime patientArrivalTime) {
        super(patientName, patientArrivalTime);
    }

    @Override
    public TreatmentPriority getPriority() {
        return TreatmentPriority.HIGH;
    }

    @Override
    public String getCatInfo() {
        return "T1/Red";
    }

    @Override
    public int compareTo(AbstractPatient o) {
        if (o.getPriority() == TreatmentPriority.MEDIUM || o.getPriority() == TreatmentPriority.LOW)
            return -1;

        return this.arrivalTime.compareTo(o.arrivalTime);
    }
}
