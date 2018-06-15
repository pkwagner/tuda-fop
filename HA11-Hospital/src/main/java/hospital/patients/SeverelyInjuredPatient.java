package hospital.patients;

import java.time.LocalTime;

/**
 * Represents a severely injured patient
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class SeverelyInjuredPatient extends AbstractPatient {

    private final int injuryRating;

    /**
     * Creates a new patient object using the specified name and arrival time.
     *
     * @param patientName        the name of the patient
     * @param patientArrivalTime the arrival time of the patient
     * @param injuryRating       A score to measure the injury importance. A higher score means a higher priority.
     */
    public SeverelyInjuredPatient(String patientName, LocalTime patientArrivalTime, int injuryRating) {
        super(patientName, patientArrivalTime);

        this.injuryRating = injuryRating;
    }

    /**
     * Gets rating value how bad the injury is. It's a positive integer.
     * A higher value means a worse injury
     *
     * @return the injury rating
     */
    public int getInjuryRating() {
        return this.injuryRating;
    }

    @Override
    public TreatmentPriority getPriority() {
        return TreatmentPriority.MEDIUM;
    }

    @Override
    public String getCatInfo() {
        return "T2/Yellow-" + String.valueOf(this.injuryRating);
    }

    @Override
    public int compareTo(AbstractPatient o) {
        if (o.getPriority() == TreatmentPriority.LOW)
            return -1;
        if (o.getPriority() == TreatmentPriority.HIGH)
            return 1;

        if (!(o instanceof SeverelyInjuredPatient))
            return 0;
        if (this.injuryRating > ((SeverelyInjuredPatient) o).injuryRating)
            return -1;
        if (this.injuryRating < ((SeverelyInjuredPatient) o).injuryRating)
            return 1;

        return this.arrivalTime.compareTo(o.arrivalTime);
    }
}