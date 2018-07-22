package tests.java.ru.raifeisen.cources.atm;

import tests.java.ru.raifeisen.cources.atm.data.AtmDataSupplier;
import tests.java.ru.raifeisen.cources.atm.data.TestPair;
import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.ScoreTypeEnum;
import main.java.ru.raiffeisen.cources.atm.model.db.DAO.AtmDAO;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMTestCreditScore {
    private static ATM atm;
    private static final AtmDataSupplier atmDataSupplier = new AtmDataSupplier();

    private static final String DUMP_STR = "{\"currentScore\":{\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"operLimit\":0,\"currentOpers\":0,\"operLimitToggl\":false}";

    @BeforeAll
    static void init(){
        atm = atmDataSupplier.getStartDataATM();
    }

    @BeforeEach
    void fillData(){
        atmDataSupplier.fillATMcreditScore(atm);
    }

    @Test
    void addMoneyToCreditScore() {
        Map<Integer, Money> testData = atmDataSupplier.getTestData();
        Map<Integer, Money> expectedData = atmDataSupplier.getExpectedData(atm);
        for (Integer key:
                testData.keySet()) {
            Money tempMoney = testData.get(key);
             atm.addMoneyToScore(tempMoney, ScoreTypeEnum.CREDIT); //добаляем на счет сумму из testData

            Money expectedMoney = expectedData.get(key);
            Money newMoney = atmDataSupplier.getMoneyFromCredit(atm);

            assertEquals(expectedMoney.getValue() , newMoney.getValue());
            atmDataSupplier.fillATMcreditScore(atm);
        }
    }


    @Test
    void addMoneyToScoreSecond() {
        for (TestPair<Money> pair:
             atmDataSupplier.getTestListData(atm)) {
            assertEquals(pair.getExpectedValue(), pair.getTestValue());
        }
    }

    @Test
    void getATMFromJSONString(){
        StringBuilder stringBuilder = mock(StringBuilder.class);
        when(stringBuilder.toString()).thenReturn(DUMP_STR);

        ATM newAtm = atm.getATMFromJSONString(stringBuilder);

        assertEquals(atm, newAtm);
    }

    @Test
    void getAllScoresBalanceFromDB(){
        AtmDAO atmDAO = mock(AtmDAO.class);
        when(atmDAO.getAtm()).thenReturn(atmDataSupplier.getStartDataATM());
        atm.setAtmDAO(atmDAO);

        double actualSum = atm.getAllScoresBalanceFromDB();

        assertEquals(0, actualSum);
    }


    @AfterEach
    void cleanData(){
        atmDataSupplier.fillATMcreditScore(atm);
    }

    @AfterAll
    static void cleanUp(){
        atm = null;
    }

}