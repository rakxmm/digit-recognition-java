package math.activationFunction;

public interface NeuralMathFunction {
    /**
     * Vykona aktivaciu
     * @param z vazeny vstup neuronu
     * @return double
     */
    double activate(double z);

    /**
     * Vykona deaktivaciu
     * @param z vazeny vstup neuronu
     * @return double
     */
    double deactivate(double z);

    /**
     * Getter na zistenie mena funkcie
     * @return String
     */
    String getName();
}
