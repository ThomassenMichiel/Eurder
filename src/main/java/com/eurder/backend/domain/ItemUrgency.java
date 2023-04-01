package com.eurder.backend.domain;

public enum ItemUrgency {
    STOCK_LOW(5),
    STOCK_MEDIUM(10),
    STOCK_HIGH(Integer.MAX_VALUE);

    private final int maxValue;

    ItemUrgency(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public static ItemUrgency getByAmount(int amount) {
        ItemUrgency selection = STOCK_HIGH;
        if (amount < STOCK_MEDIUM.getMaxValue()) {
            selection = STOCK_MEDIUM;
        }
        if (amount < STOCK_LOW.getMaxValue()) {
            selection = STOCK_LOW;
        }
        return selection;
    }
}
