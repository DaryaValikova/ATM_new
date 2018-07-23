package tests.java.ru.raifeisen.cources.atm.data;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import main.java.ru.raiffeisen.cources.atm.model.score.CreditScore;
import main.java.ru.raiffeisen.cources.atm.model.score.CurrentScore;
import main.java.ru.raiffeisen.cources.atm.model.score.DebetScore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AtmDataSupplierDebet {
    public ATM getStartDataATM() {
        Money moneyCredit =
                new Money(0, "RUR");
        CreditScore creditScore =
                new CreditScore(moneyCredit, null, 1);

        Money moneyDebet =
                new Money(0, "RUR");
        DebetScore debetScore =
                new DebetScore(moneyDebet,
                        null,
                        2,
                        creditScore);

        Money moneyCurrent =
                new Money(0, "RUR");
        CurrentScore currentScore =
                new CurrentScore(moneyCurrent,
                        null,
                        3,
                        debetScore);

        /*CreditScore creditScoreMock = mock(CreditScore.class);
        Money moneyMock = new Money(30000, "RUR");
        when(creditScore.getMoneyWithoutLess()).thenReturn(moneyMock).thenReturn(moneyMock).thenReturn(moneyMock);
        debetScore.setCreditScore(creditScoreMock);*/

        return new ATM(currentScore, debetScore, creditScore);
    }

    public void fillATMdebetScore(ATM atm) {
//        Money moneyCurrent = new Money(950, "RUR");
//        setMoneyToCurrentScore(moneyCurrent, atm, "currentScore");

        Money moneyDebet = new Money(198, "RUR");
        setMoneyToDebetScore(moneyDebet, atm, "debetScore");

        Money moneyCredit = new Money(-5000, "RUR");
        setMoneyToCreditScore(moneyCredit, atm, "creditScore");

    }

    private void setMoneyToCreditScore(Money money, ATM atm, String scoreName) {
        Class atmClass = atm.getClass();
        try {
            Field creditScoreField = atmClass.getDeclaredField(scoreName);
            creditScoreField.setAccessible(true);

            CreditScore creditScore = (CreditScore) creditScoreField.get(atm);
            Class scoreClass = creditScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);
            moneyField.set(creditScore, money);

            creditScoreField.set(atm, creditScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setMoneyToDebetScore(Money money, ATM atm, String scoreName) {
        Class atmClass = atm.getClass();
        try {
            Field debetScoreField = atmClass.getDeclaredField(scoreName);
            debetScoreField.setAccessible(true);

            DebetScore debetScore = (DebetScore) debetScoreField.get(atm);
            Class scoreClass = debetScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);
            moneyField.set(debetScore, money);

            debetScoreField.set(atm, debetScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Money> getTestDataForDebetScore() {
        Map<Integer, Money> testDataMap = new TreeMap<>();

        Money money1 = new Money(0.01, "RUR");
        testDataMap.put(1, money1);
        Money money2 = new Money(0, "RUR");
        testDataMap.put(2, money2);
        Money money3 = new Money(99.99, "RUR");
        testDataMap.put(3, money3);
        Money money4 = new Money(-1, "RUR");
        testDataMap.put(4, money4);
        Money money5 = new Money(10, "USD");
        testDataMap.put(5, money5);
        return testDataMap;
    }

    public float getTestDataCourse(int keyTestDataMap) {
        float currencyCourse = getTestDataForDebetScore().get(keyTestDataMap).getCurrency().getUsdCource();
        return currencyCourse;
    }

    public Map<Integer, Money> getExpectedDataForAddMoneyToDebet() {
        Map<Integer, Money> expectedDataDebetScore = new TreeMap<>();

        Money money1 = new Money(198 + 0.01, "RUR");
        expectedDataDebetScore.put(1, money1);
        Money money2 = new Money(198, "RUR");
        expectedDataDebetScore.put(2, money2);
        Money money3 = new Money(198 + 99.99, "RUR");
        expectedDataDebetScore.put(3, money3);
        Money money4 = new Money(198, "RUR");
        expectedDataDebetScore.put(4, money4);
        Money money5 = new Money(198 + 10 * getTestDataCourse(5), "RUR");
        expectedDataDebetScore.put(5, money5);

        return expectedDataDebetScore;
    }

    public Map<Integer, Money> getExpectedDataForGetMoneyFromDebet() {
        Map<Integer, Money> expectedDataMap = new TreeMap<>();
        Money money1 = new Money(198 - 0.01, "RUR");
        expectedDataMap.put(1, money1);
        Money money2 = new Money(198, "RUR");
        expectedDataMap.put(2, money2);
        Money money3 = new Money(198 - 99.99, "RUR");
        expectedDataMap.put(3, money3);
        Money money4 = new Money(198, "RUR");
        expectedDataMap.put(4, money4);
        Money money5 = new Money(198 - 10 * getTestDataCourse(5), "RUR");
        expectedDataMap.put(5, money5);
        return expectedDataMap;

    }

    public Map<Integer, Money> getExpectedDataForDebet(ATM atm) {
        Map<Integer, Money> expectedDataDebetMap = new TreeMap<>();
        double usdCourse = atm
                .getCreditScore()
                .getMoneyWithoutLess()
                .getCurrency()
                .getUsdCource();

        Money money1 = new Money(300, "RUR");
        expectedDataDebetMap.put(1, money1);
        Money money2 = new Money(300, "RUR");
        expectedDataDebetMap.put(2, money2);
        Money money3 = new Money(300 + 2000, "RUR"); //проверяем, что при добавлении суммы>1 млн на дебетовый счет зачисляется 2к
        expectedDataDebetMap.put(3, money3);
        Money money4 = new Money(300, "RUR");
        expectedDataDebetMap.put(4, money4);


        return expectedDataDebetMap;
    }

    public Money getMoneyFromCurrent(ATM atm) {
        Money money = null;

        Class atmClass = atm.getClass();
        try {
            Field currentScoreField = atmClass.getDeclaredField("currentScore");
            currentScoreField.setAccessible(true);

            CurrentScore currentScore = (CurrentScore) currentScoreField.get(atm);
            Class scoreClass = currentScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);

            money = (Money) moneyField.get(currentScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return money;
    }

    public Money getMoneyFromDebet(ATM atm) {
        Money money = null;

        Class atmClass = atm.getClass();
        try {
            Field debetScoreField = atmClass.getDeclaredField("debetScore");
            debetScoreField.setAccessible(true);

            DebetScore debetScore = (DebetScore) debetScoreField.get(atm);
            Class scoreClass = debetScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);

            money = (Money) moneyField.get(debetScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return money;
    }

    public List<TestPair<Money>> getTestListDataCurrentScore(ATM atm) {
        List<TestPair<Money>> testCurrentPairList = new ArrayList<>();

        Money money1current = new Money(100.39, "RUR");
        Money money1debet = new Money(0, "RUR");
        TestPair<Money> pair1 = new TestPair<>(money1current, money1debet);
        testCurrentPairList.add(pair1);

        Money money2current = new Money(1000000, "RUR");
        Money money2debet = new Money(100, "RUR");
        TestPair<Money> pair2 = new TestPair<>(money2current, money2debet);
        testCurrentPairList.add(pair2);

        Money money3current = new Money(1000000.01, "RUR");
        Money money3debet = new Money(60, "RUR");
        TestPair<Money> pair3 = new TestPair<>(money3current, money3debet);
        testCurrentPairList.add(pair3);

        return testCurrentPairList;
    }

    public List<TestPair<Money>> getExpectedListDataCurrentScore(ATM atm) {
        List<TestPair<Money>> testExpectedPairList = new ArrayList<>();

        Money money1current = new Money(950 + 100.39, "RUR");
        Money money1debet = new Money(0, "RUR");
        TestPair<Money> pair1 = new TestPair<>(money1current, money1debet);
        testExpectedPairList.add(pair1);

        Money money2current = new Money(950 + 1000000, "RUR");
        Money money2debet = new Money(100, "RUR");
        TestPair<Money> pair2 = new TestPair<>(money2current, money2debet);
        testExpectedPairList.add(pair2);

        Money money3current = new Money(950 + 1000000.01, "RUR");
        Money money3debet = new Money(60 + 2000, "RUR");
        TestPair<Money> pair3 = new TestPair<>(money3current, money3debet);
        testExpectedPairList.add(pair3);

        return testExpectedPairList;
    }
}
