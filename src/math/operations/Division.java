package math.operations;

public class Division implements Operation {
    /**
     * Vykona delenie
     * @param a double
     * @param b double
     * @return double
     */
    @Override
    public double compute(double a, double b) {
        if (b == 0) {
            throw new DivisionException("Snazil si sa delit nulou!");
        }
        return a / b;
    }
}
