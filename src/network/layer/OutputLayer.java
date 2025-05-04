package network.layer;

import math.Array2D;

public class OutputLayer extends HiddenLayer {


    public OutputLayer(int size, Layer prev) {
        super(size, prev);
        this.setName("Layer:Output");
    }

    @Override
    public void backPropagate(Array2D delta, double learningRate) {

        var error = this.getOutput().subtract(delta);
        var deactivatedZ = this.getZ().deactivate(this.getActivationFunction());

        delta  = error.hadamard(deactivatedZ);
        var prevNeuronsT = this.getPrev().getOutput().transposed();

        var nW = delta.product(prevNeuronsT);

        this.updateWeights(nW, learningRate);
        this.updateBiases(delta, learningRate);

        var prev = this.getPrev();

        if (prev instanceof HiddenLayer hidden) {
            hidden.backPropagate(delta, learningRate);
        }

    }

    public Array2D getPrediction() {
        return this.getOutput();
    }
}
