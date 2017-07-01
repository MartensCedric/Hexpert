package com.cedricmartens.hexpert.social;

/**
 * Created by martens on 6/30/17.
 */

public interface Purchasing
{
    void purchase(Amount amount);
    boolean hasPurchased();

    enum Amount
    {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN
    }
}
