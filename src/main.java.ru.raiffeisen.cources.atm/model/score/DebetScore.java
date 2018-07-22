package main.java.ru.raiffeisen.cources.atm.model.score;

import main.java.ru.raiffeisen.cources.atm.model.account.Account;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;

public class DebetScore extends Score {
    private CreditScore creditScore;

    public DebetScore(Money balance, Account owner, Integer number, CreditScore creditScore) {
        super(balance, owner, number);
        this.creditScore = creditScore;
    }

    @Override
    public void addMoney(Money money){
        if(creditScore.getMoneyWithoutLess().getValue() < -20000) {
            System.out.println("No money on Credit Score!");
            return;
        }
        super.addMoney(money);
    }

    @Override
    public Money getMoney(double balanceLess){
        if(creditScore.getMoneyWithoutLess().getValue() < 20000) {
            System.out.println("No money on Credit Score!");
            return null;
        }

        if(this.getBalance().getValue() <= 0) {
            System.out.println("No money!");
            return null;
        }

        return super.getMoney(balanceLess);
    }

    @Override
    public Money getMoneyWithoutLess(){
        /*if(creditScore.getMoneyWithoutLess().getValue() < 20000) {
            System.out.println("No money on Credit Score!");
            return null;
        }*/

        return super.getMoneyWithoutLess();
    }

    public void setCreditScore(CreditScore creditScore) {
        this.creditScore = creditScore;
    }

    @Override
    boolean checkBefore() {
        return true;
    }
}
