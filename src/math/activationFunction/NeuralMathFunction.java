package math.activationFunction;

public interface NeuralMathFunction {
    double activate(double z);

    double deactivate(double z);

    String getName();
}
