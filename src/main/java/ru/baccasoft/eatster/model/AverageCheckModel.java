package ru.baccasoft.eatster.model;

public class AverageCheckModel extends CommonNamedModel {
    private int amount;

    public AverageCheckModel() {
        super();
        amount = 0;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
