package tests.java.ru.raifeisen.cources.atm;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.ScoreTypeEnum;
import main.java.ru.raiffeisen.cources.atm.model.db.DAO.AtmDAO;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import org.junit.jupiter.api.*;
import tests.java.ru.raifeisen.cources.atm.data.AtmDataSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMTestCurrentScore {
    private static ATM atm;
    private static final AtmDataSupplier atmDataSupplier = new AtmDataSupplier();

    private static final String DUMP_STR = "{\"currentScore\":{\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"debetScore\":{\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"creditScore\":{\"balance\":{\"currency\":{\"name\":\"RUR\",\"usdCource\":65.5},\"value\":1000.0},\"number\":1,\"methodConstraintMap\":{},\"methodCallMap\":{},\"methodConstraintToggl\":false},\"operLimit\":0,\"currentOpers\":0,\"operLimitToggl\":false}";

    @BeforeAll
    static void init() {
        atm = atmDataSupplier.getStartDataATM();
    }

    @BeforeEach
    void fillData() {
        atmDataSupplier.fillATMcurrentScore(atm);//на счете 950р
    }

//    @Test
//    void addMoneyToCurrentScore() {
//        System.out.println(atm.getCurrentScore().getBalance());
//        System.out.println(atm.getDebetScore().getBalance());
//        System.out.println(atm.getCreditScore().getBalance());
//
//        for (TestPair<Money> pair:
//                atmDataSupplier.getTestListDataCurrentScore(atm)) {
//            assertEquals(pair.getExpectedValue(), pair.getTestValue());
//        }
//    }

    @Test
    void addMoneyToCurrentScore2() {
        System.out.println(atm.getCurrentScore().getBalance());
        System.out.println(atm.getDebetScore().getBalance());
        System.out.println(atm.getCreditScore().getBalance());

        Money addMoney = new Money(100.39, "RUR");
        atm.addMoneyToScore(addMoney, ScoreTypeEnum.CURRENT);

        Money expectedMoney = new Money(950 + 100.39, "RUR");

        assertEquals(expectedMoney, atm.getCurrentScore().getBalance());
//        }
    }


    @Test
    void getATMFromJSONString() {
        StringBuilder stringBuilder = mock(StringBuilder.class);
        when(stringBuilder.toString()).thenReturn(DUMP_STR);

        ATM newAtm = atm.getATMFromJSONString(stringBuilder);

        assertEquals(atm, newAtm);
    }

    @Test
    void getAllScoresBalanceFromDB() {
        AtmDAO atmDAO = mock(AtmDAO.class);
        when(atmDAO.getAtm()).thenReturn(atmDataSupplier.getStartDataATM());
        atm.setAtmDAO(atmDAO);

        double actualSum = atm.getAllScoresBalanceFromDB();

        assertEquals(0, actualSum);
    }


    @AfterEach
    void cleanData() {
        atmDataSupplier.fillATMcreditScore(atm);
    }

    @AfterAll
    static void cleanUp() {
        atm = null;
    }

}