package math.operations;

public class Addition implements Operation {
    /**
     * Vykona scitanie
     * @param a double
     * @param b double
     * @return double
     */
    @Override
    public double compute(double a, double b) {
        return a + b;
    }
}
