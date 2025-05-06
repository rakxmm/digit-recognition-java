package math.activationFunction;

public class Sigmoid implements NeuralMathFunction {
    /**
     * Vykona deaktivaciu
     * @param z vazeny vstup neuronu
     * @return double
     */
    @Override
    public double deactivate(double z) {
        var activated = this.activate(z);
        return activated * (1.0 - activated);
    }

    /**
     * Vykona aktivaciu
     * @param z vazeny vstup neuronu
     * @return double
     */
    @Override
    public double activate(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    /**
     * Getter mena
     * @return String
     */
    @Override
    public String getName() {
        return "Sigmoid";
    }
}
