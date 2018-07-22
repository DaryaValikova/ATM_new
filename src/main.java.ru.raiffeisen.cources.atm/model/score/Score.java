package main.java.ru.raiffeisen.cources.atm.model.score;

import main.java.ru.raiffeisen.cources.atm.model.account.Account;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import main.java.ru.raiffeisen.cources.atm.model.money.MoneyInterface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class Score implements MoneyInterface {
    private Money balance;
    private Account owner;
    private Integer number;
    private Map<String, Integer> methodConstraintMap;
    private Map<String, Integer> methodCallMap;
    private boolean methodConstraintToggl;

    public Score(Money balance, Account owner, Integer number) {
        this.balance = balance;
        this.owner = owner;
        this.number = number;

        this.methodConstraintMap = new HashMap<String, Integer>();
        this.methodCallMap = new HashMap<String, Integer>();

        Class thisClass = this.getClass();
        for (Method method :
                thisClass.getDeclaredMethods()) {
            for (Annotation annotation :
                    method.getDeclaredAnnotations()) {
                if (annotation instanceof MethodLimit) {
                    int limit = ((MethodLimit) annotation).limit();
                    methodConstraintMap.put(method.getName(), limit);
                    methodCallMap.put(method.getName(), 0);
                    methodConstraintToggl = true;
                }
            }
        }
    }

    protected void logIfneeded(String methodName) {
        Class thisClass = this.getClass();
        for (Annotation annotation :
                thisClass.getAnnotations()) {
            if (annotation instanceof Loggable) {
                System.out.println("We call method " + methodName);
            }
        }
    }

    protected boolean isMethodAvailableByOperLimit(String methodName) {
        if (methodConstraintMap.containsKey(methodName)) {
            int currentCalls = methodCallMap.get(methodName);
            int limitCalls = methodConstraintMap.get(methodName);

            if (currentCalls >= limitCalls) {
                return false;
            }

            currentCalls++;
            methodCallMap.put(methodName, currentCalls);
        }
        return true;
    }

    public Money getBalance() {
        logIfneeded("getBalance");
        if (!isMethodAvailableByOperLimit("getBalance")) {
            System.out.println("Method limit is over!");
            return null;
        }
        return balance;
    }

    public void setBalance(Money balance) {
        logIfneeded("setBalance");
        if (!isMethodAvailableByOperLimit("setBalance")) {
            System.out.println("Method limit is over!");
            return;
        }
        this.balance = balance;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

        if (balance != null ? !balance.equals(score.balance) : score.balance != null) return false;
        if (owner != null ? !owner.equals(score.owner) : score.owner != null) return false;
        return number != null ? number.equals(score.number) : score.number == null;
    }

    @Override
    public int hashCode() {
        int result = balance != null ? balance.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Score{" +
                "balance=" + balance +
                ", owner=" + owner +
                ", number=" + number +
                '}';
    }

    public Integer getNumber() {
        logIfneeded("getNumber");
        if (!isMethodAvailableByOperLimit("getNumber")) {
            System.out.println("Method limit is over!");
            return number;
        }
        return number;
    }

    public void setNumber(Integer number) {
        logIfneeded("setNumber");
        if (!isMethodAvailableByOperLimit("setNumber")) {
            System.out.println("Method limit is over!");
            return;
        }
        this.number = number;
    }

    @Override
    public void addMoney(Money money) {
        logIfneeded("addMoney");
        if (!isMethodAvailableByOperLimit("addMoney")) {
            System.out.println("Method limit is over!");
            return;
        }
        double usdValueIn = money.getValue() * money.getCurrency().getUsdCource(); //"деньги" в рублях
        double usdValueThis = this.balance.getValue() * this.balance.getCurrency().getUsdCource(); //"баланс" в рублях

//        if(usdValueThis < usdValueIn) {     //зачем добавили эту проверку во внесение средств?
//            System.out.println("You have no so much!");
//            return;
//        }

        if(money.getValue()>0) {
            this.balance.setValue((usdValueThis + usdValueIn)/this.balance.getCurrency().getUsdCource()); //делим на курс, чтобы получить значение "исходной" валюты
        } else {
            System.out.println("Нельзя внести на счет отрицательную сумму");
            return;
        }
    }

    @Override
    public Money getMoney(double balanceLess) {
        logIfneeded("getMoney");
        if (!isMethodAvailableByOperLimit("getMoney")) {
            System.out.println("Method limit is over!");
            return null;
        }
        if (balanceLess > 30000) {
            throw new IllegalArgumentException("Wrong balance less!");
        }

        this.balance.setValue(this.balance.getValue() - balanceLess);

        return this.balance;
    }

    @Override
    public Money getMoneyWithoutLess() {
        logIfneeded("getMoneyWithoutLess");
        if (!isMethodAvailableByOperLimit("getMoneyWithoutLess")) {
            System.out.println("Method limit is over!");
            return null;
        }

        return this.balance;
    }

    abstract boolean checkBefore();
}
