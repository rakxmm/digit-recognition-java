package network.layer;

import math.Array2D;

import math.activationFunction.NeuralMathFunction;
import math.activationFunction.ReLU;
import math.activationFunction.Sigmoid;

public class HiddenLayer extends Layer {


    // Nastavim na default Sigmoida
    private NeuralMathFunction activationFunction = new Sigmoid();

    private Array2D delta;
    private Array2D z;

    public HiddenLayer(int size, Layer prev) {
        super(size);
        this.setName("Layer:Hidden");
        super.setPrev(prev);
        prev.setNext(this);

        this.initBiases();
        this.initWeights();
    }

    private void initWeights() {
        var weights = new Array2D(super.getSize(), super.getPrev().getSize());
        weights.randomize();
        super.setWeights(weights);
    }

    private void initBiases() {
        var biases = new Array2D(super.getSize());
        super.setBiases(biases);
    }

    public void setActivationFunction(NeuralMathFunction a) {
        this.activationFunction = a;
    }

    protected void setDelta(Array2D delta) {
        this.delta = delta;
    }



    public void updateNeurons() {

        var prevNeurons = super.getPrev().getOutput();
        this.z = super.getWeights().product(prevNeurons).add(super.getBiases());
        var updated = this.z.activate(this.activationFunction);

        super.setNeurons(updated);

    }

    protected Array2D getZ() {
        return this.z;
    }

    public void backPropagate(Array2D delta, double learningRate) {
        var next = super.getNext();
        var nextWT = next.getWeights().transposed();
        delta = nextWT.product(delta).hadamard(this.z.deactivate(this.activationFunction));

        var nW = delta.product(this.getPrev().getOutput().transposed());
        this.updateWeights(nW, learningRate);

        this.updateBiases(delta, learningRate);


        var prev = this.getPrev();
        if (prev instanceof HiddenLayer hidden) {
            hidden.backPropagate(delta, learningRate);
        }
    }

    protected void updateBiases(Array2D nB, double learningRate) {
        var nBxETA = nB.multiply(learningRate);
        var updated = this.getBiases().subtract(nBxETA);
        super.setBiases(updated);
    }

    protected void updateWeights(Array2D nW, double learningRate) {
        var nWxETA = nW.multiply(learningRate);
        var updated = this.getWeights().subtract(nWxETA);
        this.setWeights(updated);
    }

    public NeuralMathFunction getActivationFunction() {
        return this.activationFunction;
    }
}
