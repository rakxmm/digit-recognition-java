package math;


import math.activationFunction.NeuralMathFunction;
import math.operations.Addition;
import math.operations.Division;
import math.operations.Multiplication;
import math.operations.Operation;
import math.operations.Subtraction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Array2D {
    private final double[][] array;

    /**
     * Vytvori instanciu nulovej matice s danymi parametrami
     * @param height pocet riadkov
     * @param width pocet stlpcov
     */
    public Array2D(int height, int width) {
        this.array = new double[height][width];
    }

    /**
     * Vytvori maticu podla dvojrozmerneho pola
     * @param array double[][]
     */
    public Array2D(double[][] array) {
        this.array = array;
    }

    /**
     * Vytvori transponovany vektor danej velkosti
     * @param height velkost vektora
     */
    public Array2D(int height) {
        this.array = new double[height][1];
    }

    /**
     * Getter na prvok matice
     * @param row ktory riadok
     * @param col ktory stlpec
     * @return double prvok
     */
    public double get(int row, int col) {
        return this.array[row][col];
    }

    /**
     * Scita dve Array2D
     * @param b Array2D
     * @return
     */
    public Array2D add(Array2D b) {
        return this.oneToOneCompute(b, new Addition());
    }

    /**
     * Prida vsetkym prvkom konstantu
     * @param c konstanta double
     * @return Array2D
     */
    public Array2D add(double c) {
        return this.oneToOneCompute(c, new Addition());
    }

    /**
     * Odcita dve Array2D
     * @param b Array2D
     * @return
     */
    public Array2D subtract(Array2D b) {
        return this.oneToOneCompute(b, new Subtraction());
    }

    /**
     * Odobere vsetkym prvkom konstantu
     * @param c konstanta double
     * @return Array2D
     */
    public Array2D subtract(double c) {
        return this.oneToOneCompute(c, new Subtraction());
    }

    /**
     * Vykona hadamardov sucin pre dve Array2D
     * @param b Array2D
     * @return Array2D
     */
    public Array2D hadamard(Array2D b) {
        return this.oneToOneCompute(b, new Multiplication());
    }

    /**
     * Vynasobi Array2D konstantou
     * @param c konstanta double
     * @return Array2D
     */
    public Array2D multiply(double c) {
        return this.oneToOneCompute(c, new Multiplication());
    }

    /**
     * Priradi nahodne prvky pre maticu
     */
    public void randomize() {
        Random r = new Random();

        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                this.array[i][j] = r.nextDouble() * 2 - 1;
            }
        }

    }

    /**
     * Vykona operaciu pre maticu a konstantu
     * @param c konstanta
     * @param op operacia
     * @return Array2D
     */
    private Array2D oneToOneCompute(double c, Operation op) {
        var copy = this.copy().getArray();
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                copy[i][j] = op.compute(copy[i][j], c);
            }
        }
        return new Array2D(copy);
    }

    /**
     * Vykona operaciu pre dve matice
     * @param b druha matica
     * @param op operacia
     * @return Array2D
     */
    private Array2D oneToOneCompute(Array2D b, Operation op) {
        if (!Shape.equals(this, b)) {
            throw new Array2DDimensionsMismatch("Snazis sa operovat s poliami roznych velkosti!");
        }
        var copy = this.copy().getArray();
        var bArray = b.getArray();
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                copy[i][j] = op.compute(copy[i][j], bArray[i][j]);
            }
        }
        return new Array2D(copy);
    }

    /**
     * Vykona aktivaciu na celu maticu
     * @param function aktivacna funkcia
     * @return Array2D
     */
    public Array2D activate(NeuralMathFunction function) {
        return this.function(function, true);
    }

    /**
     * Vykona deaktivaciu na celu maticu
     * @param function aktivacna funkcia
     * @return Array2D
     */
    public Array2D deactivate(NeuralMathFunction function) {
        return this.function(function, false);
    }

    /**
     * Vykona de/aktivaciu na celu maticu podla parametra
     * @param activate boolean, true - aktivacia, false - deaktivacia
     * @param function aktivacna funkcia
     * @return Array2D
     */
    private Array2D function(NeuralMathFunction function, boolean activate) {
        var copy = this.copy().getArray();
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[i].length; j++) {
                if (activate) {
                    copy[i][j] = function.activate(copy[i][j]);
                } else {
                    copy[i][j] = function.deactivate(copy[i][j]);
                }
            }
        }
        return new Array2D(copy);
    }

    /**
     * Getter na pocet stlpcov
     * @return int
     */
    public int height() {
        return this.array.length;
    }

    /**
     * Getter na pocet riadkov
     * @return int
     */
    public int width() {
        return this.array[0].length;
    }

    /**
     * Getter na pole matice
     * @return double[][]
     */
    private double[][] getArray() {
        var copy = new double[this.height()][this.width()];

        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                copy[i][j] = this.array[i][j];
            }
        }

        return copy;
    }

    /**
     * Vrati index najvacsej hodnoty
     * @return int
     */
    public int getMaxIndex() {

        double max = 0;
        int index = 0;

        for (int i = 0; i < this.height(); i++) {
            if (max < this.array[i][0]) {
                max = this.array[i][0];
                index = i;
            }
        }

        return index;

    }

    /**
     * Vykona nasobenie dvoch matic
     * @param b Array2D
     * @return
     */
    public Array2D product(Array2D b) {
        if (this.width() != b.height()) {
            Shape.shape(this);
            Shape.shape(b);
            throw new Array2DDimensionsMismatch("Snazis sa spravit sucin dvoch matic, ktore su navzajom nekompatibilne.");
        }

        double[][] result = new double[this.height()][b.width()];
        var bArray = b.getArray();
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < b.width(); j++) {
                for (int k = 0; k < this.width(); k++) {
                    result[i][j] += this.array[i][k] * bArray[k][j];
                }
            }
        }

        return new Array2D(result);
    }

    /**
     * Textova forma matice
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < this.height(); i++) {
            sb.append("[");
            for (int j = 0; j < this.width(); j++) {
                if (j == this.width() - 1) {
                    sb.append(this.array[i][j]);
                } else {
                    sb.append(this.array[i][j] + ", ");
                }
            }
            if (i == this.height() - 1) {
                sb.append("]");
            } else {
                sb.append("],\n ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Transponuje maticu
     * @return  Array2D
     */
    public Array2D transposed() {
        double[][] transposedMatrix = new double[this.width()][this.height()];

        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                transposedMatrix[j][i] = this.array[i][j];
            }
        }

        return new Array2D(transposedMatrix);

    }

    /**
     * Vrati novu kopiu matice
     * @return Array2D
     */
    public Array2D copy() {
        return new Array2D(this.array);
    }

    /**
     * Ulozi vektor do suboru.
     * @param filePath cielova adresa suboru
     */
    public void saveMatrixToFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            int index = 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.height(); i++) {
                for (int j = 0; j < this.width(); j++) {
                    if (index % 28 == 0) {
                        sb.append(String.format("%.1f\n", this.get(i, j)));
                        bw.write(sb.toString());
                        sb = new StringBuilder();
                    } else {
                        sb.append(String.format("%.1f, ", this.get(i, j)));
                    }
                    index++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
