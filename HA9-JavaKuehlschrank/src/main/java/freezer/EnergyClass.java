package freezer;

/**
 * Created by alphath on 1/11/17.
 */
public class EnergyClass {
    private static final int Appp = 429;
    private static final int App = 100;
    private static final int Ap = 428;
    private static final int A = 174;
    private static final int B = 272;
    private static final int C = 925;
    private static final int D = 48;

    private int energyClass;

    public EnergyClass (int energyClass) {
        this.energyClass = energyClass;
    }

    public double getMinEfficiency () {
        switch (this.energyClass) {
            case EnergyClass.Appp:
                return 0.2;
            case EnergyClass.App:
                return 0.18;
            case EnergyClass.Ap:
                return 0.16;
            case EnergyClass.A:
                return 0.14;
            case EnergyClass.B:
                return 0.12;
            case EnergyClass.C:
                return 0.1;
            default:
                return 0;
        }
    }

    public static EnergyClass getEnergyClass (double efficiency) {
        // TODO Needs to be filled up with code!
        return null;
    }

    public String toString () {
        switch (this.energyClass) {
            case EnergyClass.Appp:
                return "A+++";
            case EnergyClass.App:
                return "A++";
            case EnergyClass.Ap:
                return "A+";
            case EnergyClass.A:
                return "A";
            case EnergyClass.B:
                return "B";
            case EnergyClass.C:
                return "C";
            case EnergyClass.D:
                return "D";
            default:
                return "undefined";
        }
    }
}
