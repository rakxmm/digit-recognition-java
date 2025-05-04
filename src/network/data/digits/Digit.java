package network.data.digits;

import math.Array2D;
import network.data.Data;

public class Digit extends Data {
    public Digit(Array2D digitData, int label) {
        double[][] l = new double[10][1];
        l[label][0] = 1;
        super(digitData, new Array2D(l));
    }



}
