package network.layer;

import math.Array2D;

public class InputLayer extends Layer {

    public InputLayer(int size) {
        super(size);
        this.setName("Layer:Input");
    }

    public void setInput(Array2D input) {
        super.setNeurons(input);
    }
}
