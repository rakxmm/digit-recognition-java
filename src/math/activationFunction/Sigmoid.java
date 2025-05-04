package math.activationFunction;

public class Sigmoid extends NeuralMathFunction {
    @Override
    public double deactivate(double z) {
        var activated = this.activate(z);
        return activated * (1.0 - activated);
    }

    @Override
    public double activate(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    @Override
    public String toString() {
        return "Sigmoid";
    }
}
