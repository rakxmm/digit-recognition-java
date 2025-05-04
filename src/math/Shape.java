package math;

public class Shape {

    private Shape() {

    }

    public static void shape(Array2D a) {
        System.out.printf("(%d, %d)%n", a.height(), a.width());
    }

    public static boolean equals(Array2D a, Array2D b) {
        return a.height() == b.height() && a.width() == b.width();
    }

}
