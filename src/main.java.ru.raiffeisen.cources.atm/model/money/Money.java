package main.java.ru.raiffeisen.cources.atm.model.money;

import main.java.ru.raiffeisen.cources.atm.model.constants.CurrencyHolder;

public class Money {
    private Currency currency;
    private double value;

    public Money(double value, String currencyName) {
        this.value = value;
        this.currency = CurrencyHolder.getCurrencyByName(currencyName);
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (Double.compare(money.value, value) != 0) return false;
        return currency != null ? currency.equals(money.currency) : money.currency == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = currency != null ? currency.hashCode() : 0;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Money{" +
                "currency=" + currency +
                ", value=" + value +
                '}';
    }
}
