package network.data;

import math.Array2D;

public abstract class Data {
    private Array2D label;
    private Array2D dataMatrix;

    public Data(Array2D data) {
        this.dataMatrix = data;
    }

    public Data(Array2D data, Array2D label) {
        this(data);

        this.label = label;
    }

    public Array2D getLabel() {
        return this.label;
    }

    public Array2D getDataMatrix() {
        return this.dataMatrix;
    }
}
