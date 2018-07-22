package tests.java.ru.raifeisen.cources.atm;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.java.ru.raifeisen.cources.atm.data.AtmDataSupplierCredit;

import java.util.Map;

public class ATMTestGetMoneyFromScore {
    private static ATM atm;
    private static final AtmDataSupplierCredit atmDataSupplier = new AtmDataSupplierCredit();

    private static final String DUMP_STR = "{\"currentScore\":{\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"operLimit\":0,\"currentOpers\":0,\"operLimitToggl\":false}";

    @BeforeAll
    static void init() {
        atm = atmDataSupplier.getStartDataATM();
    }

    @BeforeEach
    void fillData() {
        atmDataSupplier.fillATMcreditScore(atm);
    }

    @Test
    void getMoneyFromDebetScore(){
        Map<Integer, Money> testData = atmDataSupplier.getTestDataForCreditScore();
        Map<Integer, Money> expectedData = atmDataSupplier.getExpectedDataForAddMoney(atm);
//        for (Integer key:
//                testData.keySet()) {
//            Money tempMoney = testData.get(key);
//            atm.addMoneyToScore(tempMoney, ScoreTypeEnum.CREDIT);
//
//            Money expectedMoney = expectedData.get(key);
//            Money newMoney = atmDataSupplier.getMoneyFromCredit(atm);
//
//            assertEquals(expectedMoney.getValue() , newMoney.getValue());
//        }

    }

}
