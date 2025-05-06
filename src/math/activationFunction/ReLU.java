package math.activationFunction;

public class ReLU implements NeuralMathFunction {
    /**
     * Vykona aktivaciu
     * @param z vazeny vstup neuronu
     * @return double
     */
    @Override
    public double activate(double z) {
        return (z > 0) ? Math.max(0, z) : 0;
    }

    /**
     * Vykona deaktivaciu
     * @param z vazeny vstup neuronu
     * @return double
     */
    @Override
    public double deactivate(double z) {
        return (z > 0) ? 1 : 0;
    }

    /**
     * Getter mena
     * @return String
     */
    @Override
    public String getName() {
        return "ReLU";
    }
}
