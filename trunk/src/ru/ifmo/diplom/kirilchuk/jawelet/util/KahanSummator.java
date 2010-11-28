package ru.ifmo.diplom.kirilchuk.jawelet.util;

/**
 *
 * @author Kirilchuk V.E.
 */
public class KahanSummator {

    /* Current sum */
    private double sum;

    /* Current correction */
    private double correction;

    /* Current corrected addend */
    private double correctedAddend;

    public double getCorrectSumm() {
        return sum + correction;
    }

    public void add(double addend) {
        correctedAddend = addend + correction;

        double tempSum = sum + correctedAddend;

        correction = correctedAddend - (tempSum - sum);
        sum = tempSum;
    }

    public void clear() {
        sum = 0;
        correction = 0;
        correctedAddend = 0;
    }

    public static void main(String[] args) {
        KahanSummator summator = new KahanSummator();
        summator.add(100000000000.0);
        summator.add(0.03333);
        summator.add(0.06667);
        System.out.println(summator.getCorrectSumm());
        System.out.println(100000000000.0 + 0.03333 + 0.06667);//see the difference
    }
}

