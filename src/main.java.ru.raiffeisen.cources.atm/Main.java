package main.java.ru.raiffeisen.cources.atm;

import main.java.ru.raiffeisen.cources.atm.model.score.DumpType;

public class Main {

    public static void main(String[] args) {
	    ATM atm = new ATM();
        atm.dump(DumpType.JSON);
        atm.restore(DumpType.JSON);
    }
}
