package tests.java.ru.raifeisen.cources.atm.data;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import main.java.ru.raiffeisen.cources.atm.model.score.CreditScore;
import main.java.ru.raiffeisen.cources.atm.model.score.CurrentScore;
import main.java.ru.raiffeisen.cources.atm.model.score.DebetScore;

import java.lang.reflect.Field;
import java.util.*;

public class AtmDataSupplier {
    public Map<String, Object> getStartData() {
        Map<String, Object> atmData = new HashMap<>();

        Money moneyCredit =
                new Money(0, "RUR");
        CreditScore creditScore =
                new CreditScore(moneyCredit, null, 1);
        atmData.put("creditScore", creditScore);

        Money moneyDebet =
                new Money(0, "RUR");
        DebetScore debetScore =
                new DebetScore(moneyDebet,
                        null,
                        2,
                        creditScore);
        atmData.put("debetScore", debetScore);

        Money moneyCurrent =
                new Money(0, "RUR");
        CurrentScore currentScore =
                new CurrentScore(moneyCurrent,
                        null,
                        3,
                        debetScore);
        atmData.put("currentScore", currentScore);

        return atmData;
    }

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

    public void fillATMcreditScore(ATM atm) {
        Money moneyCredit = new Money(1000, "RUR");
        setMoneyToCreditScore(moneyCredit, atm, "creditScore");

        /*Money moneyDebet = new Money(1000, "RUR");
        setMoneyToCreditScore(moneyDebet, atm, "debetScore");

        Money moneyCurrent = new Money(1000, "RUR");
        setMoneyToCreditScore(moneyCurrent, atm, "currentScore");*/
    }

    public void fillATMcurrentScore(ATM atm) {
        Money moneyCurrent = new Money(950, "RUR");
        setMoneyToCurrentScore(moneyCurrent, atm, "currentScore");

        Money moneyDebet = new Money(300, "RUR");
        setMoneyToDebetScore(moneyDebet, atm, "debetScore");

//        Money moneyCredit = new Money(20000, "RUR");
//        setMoneyToCreditScore(moneyCredit, atm, "creditScore");

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

    private void setMoneyToCurrentScore(Money money, ATM atm, String scoreName) {
        Class atmClass = atm.getClass();
        try {
            Field currentScoreField = atmClass.getDeclaredField(scoreName);
            currentScoreField.setAccessible(true);

            CurrentScore currentScore = (CurrentScore) currentScoreField.get(atm);
            Class scoreClass = currentScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);
            moneyField.set(currentScore, money);

            currentScoreField.set(atm, currentScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Money> getTestData() {
        Map<Integer, Money> testDataMap = new TreeMap<>();

        Money money1 = new Money(100, "RUR");
        testDataMap.put(1, money1);
        Money money2 = new Money(0, "RUR");
        testDataMap.put(2, money2);
        Money money3 = new Money(-100, "RUR");
        testDataMap.put(3, money3);
        Money money4 = new Money(100, "USD");
        testDataMap.put(4, money4);
        Money money5 = new Money(0, "USD");
        testDataMap.put(5, money5);
        Money money6 = new Money(-100, "USD");
        testDataMap.put(6, money6);
        Money money7 = new Money(1000000, "USD");
        testDataMap.put(7, money7);
        Money money8 = new Money(1000000, "RUR");
        testDataMap.put(8, money8);
        Money money9 = new Money(Double.MAX_VALUE, "USD");
        testDataMap.put(9, money9);
        Money money10 = new Money(Double.MAX_VALUE, "RUR");
        testDataMap.put(10, money10);

        return testDataMap;
    }

    public Map<Integer, Money> getExpectedData(ATM atm) {
        Map<Integer, Money> expectedDataMap = new TreeMap<>();
        double usdCourse = atm
                .getCreditScore()
                .getMoneyWithoutLess()
                .getCurrency()
                .getUsdCource();

        Money money1 = new Money(1100, "RUR");
        expectedDataMap.put(1, money1);
        Money money2 = new Money(1000, "RUR");
        expectedDataMap.put(2, money2);
        Money money3 = new Money(1000, "RUR"); //нельзя внести отрицательное значение
        expectedDataMap.put(3, money3);
        Money money4 = new Money((1000 + 100 * 65.5), "RUR");
        expectedDataMap.put(4, money4);
        Money money5 = new Money((1000 + 0 * 65.5), "RUR");
        expectedDataMap.put(5, money5);
        Money money6 = new Money(1000, "RUR"); //нельзя внести отрицательное значение
        expectedDataMap.put(6, money6);
        Money money7 = new Money(1000, "RUR");
        expectedDataMap.put(7, money7);
        Money money8 = new Money(1000, "RUR");
        expectedDataMap.put(8, money8);
        Money money9 = new Money(1000, "RUR");
        expectedDataMap.put(9, money9);
        Money money10 = new Money(1000, "RUR");
        expectedDataMap.put(10, money10);

        return expectedDataMap;
    }

    public Map<Integer, Money> getTestDataForCurrent() {
        Map<Integer, Money> testDataMap = new TreeMap<>();

        Money money1 = new Money(100.39, "RUR");
        testDataMap.put(1, money1);
        Money money2 = new Money(1000000, "RUR");
        testDataMap.put(2, money2);
        Money money3 = new Money(1000001.01, "RUR");
        testDataMap.put(3, money3);
        Money money4 = new Money(-0.01, "RUR");
        testDataMap.put(4, money4);
        return testDataMap;
    }

    public Map<Integer, Money> getExpectedDataForCurrent(ATM atm) {
        Map<Integer, Money> expectedDataCurrentMap = new TreeMap<>();
        double usdCourse = atm
                .getCreditScore()
                .getMoneyWithoutLess()
                .getCurrency()
                .getUsdCource();

        Money money1 = new Money(950 + 100.39, "RUR");
        expectedDataCurrentMap.put(1, money1);
        Money money2 = new Money(950 + 1000000, "RUR");
        expectedDataCurrentMap.put(2, money2);
        Money money3 = new Money(950+1000001.01, "RUR");
        expectedDataCurrentMap.put(3, money3);
        Money money4 = new Money(950, "RUR");
        expectedDataCurrentMap.put(4, money4);


        return expectedDataCurrentMap;
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

    public List<TestPair<Money>> getTestListData(ATM atm) {
        List<TestPair<Money>> testPairList = new ArrayList<>();

//        Money money1 = new Money(100, "RUR");
//        Money money1Ex = new Money(1100, "RUR");
//        TestPair<Money> pair1 = new TestPair<>(money1, money1Ex);
//        testPairList.add(pair1);

        Money money2 = new Money(0, "RUR");
        Money money2Ex = new Money(1000, "RUR");
        TestPair<Money> pair2 = new TestPair<>(money2, money2Ex);
        testPairList.add(pair2);

        Money money3 = new Money(-100, "RUR");
        Money money3Ex = new Money(1000, "RUR");
        TestPair<Money> pair3 = new TestPair<>(money3, money3Ex);
        testPairList.add(pair3);

        Money money4 = new Money(100, "USD");
        Money money4Ex = new Money(1000 + 100 * 65.5, "RUR");
        TestPair<Money> pair4 = new TestPair<>(money4, money4Ex);
        testPairList.add(pair4);

        Money money5 = new Money(0, "USD");
        Money money5Ex = new Money(1000, "RUR");
        TestPair<Money> pair5 = new TestPair<>(money5, money5Ex);
        testPairList.add(pair5);

        Money money6 = new Money(-100, "USD");
        Money money6Ex = new Money(1000, "RUR");
        TestPair<Money> pair6 = new TestPair<>(money6, money6Ex);
        testPairList.add(pair6);

        Money money7 = new Money(1000000, "USD");
        Money money7Ex = new Money(1000, "RUR");
        TestPair<Money> pair7 = new TestPair<>(money7, money7Ex);
        testPairList.add(pair7);

        Money money8 = new Money(1000000, "RUR");
        Money money8Ex = new Money(1000, "RUR");
        TestPair<Money> pair8 = new TestPair<>(money8, money8Ex);
        testPairList.add(pair8);

        Money money9 = new Money(Double.MAX_VALUE, "USD");
        Money money9Ex = new Money(1000, "RUR");
        TestPair<Money> pair9 = new TestPair<>(money9, money9Ex);
        testPairList.add(pair9);

        Money money10 = new Money(Double.MAX_VALUE, "RUR");
        Money money10Ex = new Money(1000, "RUR");
        TestPair<Money> pair10 = new TestPair<>(money10, money10Ex);
        testPairList.add(pair10);


        return testPairList;
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


    public Money getMoneyFromCredit(ATM atm) {
        Money money = null;

        Class atmClass = atm.getClass();
        try {
            Field creditScoreField = atmClass.getDeclaredField("creditScore");
            creditScoreField.setAccessible(true);

            CreditScore creditScore = (CreditScore) creditScoreField.get(atm);
            Class scoreClass = creditScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);

            money = (Money) moneyField.get(creditScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return money;
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

}
