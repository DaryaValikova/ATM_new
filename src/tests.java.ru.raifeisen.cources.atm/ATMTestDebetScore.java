package tests.java.ru.raifeisen.cources.atm;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.ScoreTypeEnum;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.java.ru.raifeisen.cources.atm.data.AtmDataSupplierDebet;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMTestDebetScore {
    private static ATM atm;
    private static final AtmDataSupplierDebet atmDataSupplier = new AtmDataSupplierDebet();
//    private static final String DUMP_STR = "{\"currentScore\":{\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"operLimit\":0,\"currentOpers\":0,\"operLimitToggl\":false}";

    @BeforeAll
    static void init() {
        atm = atmDataSupplier.getStartDataATM();
    }

    @BeforeEach
    void fillData() {
        atmDataSupplier.fillATMdebetScore(atm);
    }

    @Test
    void addMoneyToDebetScore() {
        Map<Integer, Money> testData = atmDataSupplier.getTestDataForDebetScore();
        Map<Integer, Money> expectedData = atmDataSupplier.getExpectedDataForAddMoneyToDebet();
        for (Integer key :
                testData.keySet()) {
            Money tempMoney = testData.get(key);

            atm.addMoneyToScore(tempMoney, ScoreTypeEnum.DEBET); //добаляем на счет сумму из testData

            Money expectedMoney = expectedData.get(key);
            Money newMoney = atmDataSupplier.getMoneyFromDebet(atm);

            assertEquals(expectedMoney.getValue(), newMoney.getValue());
            atmDataSupplier.fillATMdebetScore(atm);
        }
    }

    void mockCurrentScore(Money tempMoney, Map<Integer, Money> expectedData, Integer key) {
        Money moneyMock = atmDataSupplier.getMoneyFromCurrent(atm);
        ATM atm = mock(ATM.class);
        Money expectedMoney = expectedData.get(key);
        when(atm.getMoneyFromScore(tempMoney, ScoreTypeEnum.CREDIT)).thenReturn(moneyMock);
        assertEquals(expectedMoney.getValue(), moneyMock.getValue());
    }

    @Test
    void getMoneyFromDebetScore() {
        Map<Integer, Money> testData = atmDataSupplier.getTestDataForDebetScore();
        Map<Integer, Money> expectedData = atmDataSupplier.getExpectedDataForGetMoneyFromDebet();
        for (Integer key :
                testData.keySet()) {
            Money tempMoney = testData.get(key);

            try {
                atm.getMoneyFromScore(tempMoney, ScoreTypeEnum.DEBET); //снимаем со счета сумму из testData
            } catch (IllegalArgumentException e) { //используем заглушку, если поймали exception
                mockCurrentScore(tempMoney, expectedData, key);
            } finally {
                Money expectedMoney = expectedData.get(key);
                Money newMoney = atmDataSupplier.getMoneyFromCurrent(atm);

                assertEquals(expectedMoney.getValue(), newMoney.getValue());
                atmDataSupplier.fillATMdebetScore(atm);
            }

        }
    }
}
