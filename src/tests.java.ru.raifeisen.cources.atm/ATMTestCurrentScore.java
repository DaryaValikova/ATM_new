package tests.java.ru.raifeisen.cources.atm;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.ScoreTypeEnum;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import org.junit.jupiter.api.*;
import tests.java.ru.raifeisen.cources.atm.data.AtmDataSupplierCurrent;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMTestCurrentScore {
    private static ATM atm;
    private static final AtmDataSupplierCurrent atmDataSupplier = new AtmDataSupplierCurrent();

    private static final String DUMP_STR = "{\"currentScore\":{\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"operLimit\":0,\"currentOpers\":0,\"operLimitToggl\":false}";

    @BeforeAll
    static void init() {
        atm = atmDataSupplier.getStartDataATM();
    }

    @BeforeEach
    void fillData() {
        atmDataSupplier.fillATMcurrentScore(atm);//на текущем счете 950р
    }


    @Test
    void addMoneyToCurrentScore() {  //проверка добавления средств на текущий счет
        Map<Integer, Money> testData = atmDataSupplier.getTestDataForCurrentScore();
        Map<Integer, Money> expectedDataCurrent = atmDataSupplier.getExpectedDataForAddMoney(atm);
        Map<Integer, Money> expectedDataDebet = atmDataSupplier.getExpectedDataForDebet(atm);
        for (Integer key :
                testData.keySet()) {
            Money tempMoney = testData.get(key);
            atm.addMoneyToScore(tempMoney, ScoreTypeEnum.CURRENT); //добаляем на текущий счет сумму из testData

            Money expectedCurrentMoney = expectedDataCurrent.get(key);
            Money newCurrentMoney = atmDataSupplier.getMoneyFromCurrent(atm);

            Money expectedDebetMoney = expectedDataDebet.get(key);
            Money newDebetMoney = atmDataSupplier.getMoneyFromDebet(atm);

            assertEquals(expectedCurrentMoney.getValue(), newCurrentMoney.getValue()); //сравниваем текущие счета
            assertEquals(expectedDebetMoney.getValue(), newDebetMoney.getValue()); //сравниваем дебетовый счет после работы с текущим счетом
            atmDataSupplier.fillATMcurrentScore(atm);
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
    void getMoneyFromCurrentScore() {
        Map<Integer, Money> testData = atmDataSupplier.getTestDataForCurrentScore();
        Map<Integer, Money> expectedData = atmDataSupplier.getExpectedDataForGetMoney(atm);
        for (Integer key :
                testData.keySet()) {
            Money tempMoney = testData.get(key);

            try {
                atm.getMoneyFromScore(tempMoney, ScoreTypeEnum.CURRENT); //снимаем со счета сумму из testData
            } catch (IllegalArgumentException e) { //используем заглушку, если поймали exception
                mockCurrentScore(tempMoney, expectedData, key);
            } finally {
                Money expectedMoney = expectedData.get(key);
                Money newMoney = atmDataSupplier.getMoneyFromCurrent(atm);

                assertEquals(expectedMoney.getValue(), newMoney.getValue());
                atmDataSupplier.fillATMcurrentScore(atm);
            }

        }
    }

    @Test
    void getATMFromJSONString() {
        StringBuilder stringBuilder = mock(StringBuilder.class);
        when(stringBuilder.toString()).thenReturn(DUMP_STR);

        ATM newAtm = atm.getATMFromJSONString(stringBuilder);

        assertEquals(atm, newAtm);
    }


    @AfterEach
    void cleanData() {
        atmDataSupplier.fillATMcurrentScore(atm);
    }

    @AfterAll
    static void cleanUp() {
        atm = null;
    }

}