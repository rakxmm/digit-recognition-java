package network.layer;

import math.Array2D;
import math.activationFunction.NeuralMathFunction;

public abstract class Layer {

    private Layer prev;
    private Layer next;

    private final int size;

    private Array2D biases;
    private Array2D weights;
    private Array2D neurons;

    private String name = "Layer";

    public Layer(int size) {
        this.size = size;
        this.neurons = new Array2D(size, 1);
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    protected Layer getPrev() {
        return this.prev;
    }

    protected Layer getNext() {
        return this.next;
    }

    protected void setPrev(Layer l) {
        this.prev = l;
    }

    protected void setNext(Layer l) {
        this.next = l;
    }

    protected void setNeurons(Array2D neurons) {
        this.neurons = neurons;
    }

    public void setWeights(Array2D weights) {
        this.weights = weights;
    }

    public void setBiases(Array2D biases) {
        this.biases = biases;
    }





    public int getSize() {
        return this.size;
    }


    protected Array2D getOutput() {
        return this.neurons.copy();
    }

    public Array2D getWeights() {
        if (this.weights == null) {
            return null;
        }
        return this.weights.copy();
    }
    public Array2D getBiases() {
        if (this.biases == null) {
            return null;
        }
        return this.biases.copy();
    }

    @Override
    public String toString() {
        return "Layer: " + this.size;
    }
}
