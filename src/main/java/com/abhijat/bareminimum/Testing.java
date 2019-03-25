package com.abhijat.bareminimum;

public class Testing {

    private static <T> void examine(T a, T b) {
        if (a instanceof Comparable<?>) {
            System.out.println(((Comparable) a).compareTo(b));
        }
    }

}
