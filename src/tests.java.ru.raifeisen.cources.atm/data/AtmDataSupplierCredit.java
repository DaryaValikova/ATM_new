package tests.java.ru.raifeisen.cources.atm.data;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import main.java.ru.raiffeisen.cources.atm.model.score.CreditScore;
import main.java.ru.raiffeisen.cources.atm.model.score.CurrentScore;
import main.java.ru.raiffeisen.cources.atm.model.score.DebetScore;

import java.lang.reflect.Field;
import java.util.*;

public class AtmDataSupplierCredit {
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

    public Map<Integer, Money> getTestDataForCreditScore() {
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
        Money money11 = new Money(30000, "RUR");
        testDataMap.put(11, money11);

        return testDataMap;
    }

    public float getTestDataCourse(int keyTestDataMap) {
        float currencyCourse = getTestDataForCreditScore().get(keyTestDataMap).getCurrency().getUsdCource();
        return currencyCourse;
    }

    public double getTestDataValue(int keyTestDataMap) {
        double testValue = getTestDataForCreditScore().get(keyTestDataMap).getValue();
        return testValue;
    }

    public Map<Integer, Money> getExpectedDataForAddMoney(ATM atm) {
        Map<Integer, Money> expectedDataMap = new TreeMap<>();

        Money money1 = new Money(1000 + (getTestDataValue(1) * getTestDataCourse(1)), "RUR");
        expectedDataMap.put(1, money1);
        Money money2 = new Money(1000 + (getTestDataValue(2) * getTestDataCourse(2)), "RUR");
        expectedDataMap.put(2, money2);
        Money money3 = new Money(1000, "RUR"); //нельзя внести отрицательное значение или 0
        expectedDataMap.put(3, money3);
        Money money4 = new Money(1000 + (getTestDataValue(4) * getTestDataCourse(4)), "RUR");
        expectedDataMap.put(4, money4);
        Money money5 = new Money(1000 + (getTestDataValue(2) * getTestDataCourse(2)), "RUR");
        expectedDataMap.put(5, money5);
        Money money6 = new Money(1000, "RUR"); //нельзя внести отрицательное значение
        expectedDataMap.put(6, money6);
        Money money7 = new Money(1000 + (getTestDataValue(7) * getTestDataCourse(7)), "RUR");
        expectedDataMap.put(7, money7);
        Money money8 = new Money(1000 + (getTestDataValue(8) * getTestDataCourse(8)), "RUR");
        expectedDataMap.put(8, money8);
        Money money9 = new Money(1000 + (getTestDataValue(9) * getTestDataCourse(9)), "RUR");
        expectedDataMap.put(9, money9);
        Money money10 = new Money(1000 + (getTestDataValue(10) * getTestDataCourse(10)), "RUR");
        expectedDataMap.put(10, money10);
        Money money11 = new Money(1000 + (getTestDataValue(11) * getTestDataCourse(11)), "RUR");
        expectedDataMap.put(11, money11);

        return expectedDataMap;
    }

    public Map<Integer, Money> getExpectedDataForGetMoney(ATM atm) {
        Map<Integer, Money> expectedDataMap = new TreeMap<>();

        Money money1 = new Money(900, "RUR");
        expectedDataMap.put(1, money1);
        Money money2 = new Money(1000, "RUR");
        expectedDataMap.put(2, money2);
        Money money3 = new Money(1000, "RUR");
        expectedDataMap.put(3, money3);
        Money money4 = new Money((1000 - 100 *getTestDataCourse(4)), "RUR");
        expectedDataMap.put(4, money4);
        Money money5 = new Money(1000, "RUR");
        expectedDataMap.put(5, money5);
        Money money6 = new Money(1000, "RUR");
        expectedDataMap.put(6, money6);
        Money money7 = new Money(1000, "RUR");
        expectedDataMap.put(7, money7);
        Money money8 = new Money(1000, "RUR");
        expectedDataMap.put(8, money8);
        Money money9 = new Money(1000, "RUR");
        expectedDataMap.put(9, money9);
        Money money10 = new Money(1000, "RUR");
        expectedDataMap.put(10, money10);
        Money money11 = new Money(1000, "RUR");
        expectedDataMap.put(11, money11);

        return expectedDataMap;
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


}
