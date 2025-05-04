package math;


import math.activationFunction.Deactivation;
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

    public Array2D(int height, int width) {
        this.array = new double[height][width];
    }

    public Array2D(double[][] array) {
        this.array = array;
    }

    public Array2D(int height) {
        this.array = new double[height][1];
    }

    public double get(int row, int col) {
        return this.array[row][col];
    }

    public Array2D add(Array2D b) {
        return this.oneToOneCompute(b, new Addition());
    }

    public Array2D add(double c) {
        return this.oneToOneCompute(c, new Addition());
    }

    public Array2D subtract(Array2D b) {
        return this.oneToOneCompute(b, new Subtraction());
    }

    public Array2D subtract(double c) {
        return this.oneToOneCompute(c, new Subtraction());
    }

    public Array2D hadamard(Array2D b) {
        return this.oneToOneCompute(b, new Multiplication());
    }

    public Array2D multiply(double c) {
        return this.oneToOneCompute(c, new Multiplication());
    }

    public Array2D divide(Array2D b) {
        return this.oneToOneCompute(b, new Division());
    }

    public Array2D divide(double c) {
        return this.oneToOneCompute(c, new Division());
    }

    public void randomize() {
        Random r = new Random();

        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                this.array[i][j] = r.nextDouble() * 2 - 1;
            }
        }

    }

    private Array2D oneToOneCompute(double c, Operation op) {
        var copy = this.copy().getArray();
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                copy[i][j] = op.compute(copy[i][j], c);
            }
        }
        return new Array2D(copy);
    }

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

    public Array2D activate(NeuralMathFunction function) {
        return this.function(function, true);
    }

    public Array2D deactivate(NeuralMathFunction function) {
        return this.function(function, false);
    }

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


    public int height() {
        return this.array.length;
    }

    public int width() {
        return this.array[0].length;
    }

    private double[][] getArray() {
        var copy = new double[this.height()][this.width()];

        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                copy[i][j] = this.array[i][j];
            }
        }

        return copy;
    }

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


    public Array2D transposed() {
        double[][] transposedMatrix = new double[this.width()][this.height()];

        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                transposedMatrix[j][i] = this.array[i][j];
            }
        }

        return new Array2D(transposedMatrix);

    }

    public Array2D deactivate(Deactivation deactivation) {
        return null;
    }

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
