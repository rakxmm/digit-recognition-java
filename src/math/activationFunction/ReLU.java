package math.activationFunction;

public class ReLU implements NeuralMathFunction {
    @Override
    public double activate(double z) {
        return (z > 0) ? Math.max(0, z) : 0;
    }

    @Override
    public double deactivate(double z) {
        return (z > 0) ? 1 : 0;
    }
    @Override
    public String getName() {
        return "ReLU";
    }
}
