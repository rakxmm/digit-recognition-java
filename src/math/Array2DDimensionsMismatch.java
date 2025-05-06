package math;

public class Array2DDimensionsMismatch extends ArithmeticException {
    /**
     * Vynimka, ktora nastane ked dve matice/ vektory nie su rovnake
     * @param s
     */
    public Array2DDimensionsMismatch(String s) {
        super(s);
    }
}
