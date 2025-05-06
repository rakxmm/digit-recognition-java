package math.operations;

public class Subtraction implements Operation {
    /**
     * Vykona odcitanie
     * @param a double
     * @param b double
     * @return double
     */
    @Override
    public double compute(double a, double b) {
        return a - b;
    }
}
