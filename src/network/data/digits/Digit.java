package network.data.digits;

import math.Array2D;

public class Digit {
    private final Array2D data;
    private Array2D label;

    /**
     * Vytvori testovaciu cislicu bez labelu
     * @param digitData Array2D data
     */
    public Digit(Array2D digitData) {
        this.data = digitData;
    }

    /**
     * Vytvori trenovaciu cislicu
     * @param digitData Array2D data
     * @param label Array2D label
     */
    public Digit(Array2D digitData, int label) {
        this.data = digitData;

        var a = new double[10][1];
        a[label][0] = 1;
        this.label = new Array2D(a);
    }

    /**
     * Getter na data
     * @return Array2D
     */
    public Array2D getData() {
        return this.data;
    }

    /**
     * Getter na label
     * @return Array2D
     */
    public Array2D getLabel() {
        return this.label;
    }



}
