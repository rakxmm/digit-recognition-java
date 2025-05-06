package math;

public class Shape {

    private Shape() {
    }

    /**
     * Vypise tvar Array2D
     * @param a Array2D
     */
    public static void shape(Array2D a) {
        System.out.printf("(%d, %d)%n", a.height(), a.width());
    }

    /**
     * Vrati pravdivostnu hodnotu o tom ci dane polia su rovnake
     * @param a Array2D
     * @param b Array2D
     * @return boolean
     */
    public static boolean equals(Array2D a, Array2D b) {
        return a.height() == b.height() && a.width() == b.width();
    }

}
