package math.operations;

public class Multiplication implements Operation {
    /**
     * Vykona nasobenie
     * @param a double
     * @param b double
     * @return double
     */
    @Override
    public double compute(double a, double b) {
        return a * b;
    }
}
