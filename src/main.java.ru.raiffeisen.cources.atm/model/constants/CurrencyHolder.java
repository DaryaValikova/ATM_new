package main.java.ru.raiffeisen.cources.atm.model.constants;

import main.java.ru.raiffeisen.cources.atm.model.money.Currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyHolder {
    private static final Map<String,Currency> currencies = new HashMap<String, Currency>();
    static {
        currencies.put("USD",new Currency("USD", 65.5f));
        currencies.put("RUR",new Currency("RUR", 1));
    }

    public static Currency getCurrencyByName(String name) {
        return currencies.get(name);
    }
}
