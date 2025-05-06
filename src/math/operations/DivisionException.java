package math.operations;

public class DivisionException extends ArithmeticException {
    /**
     * Vynimka pre delenie nulou
     * @param s sprava, ktoru chceme aby sa zobrazila v terminali
     */
    public DivisionException(String s) {
        super(s);
    }
}
