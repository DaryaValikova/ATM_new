package main.java.ru.raiffeisen.cources.atm;

import com.google.gson.Gson;
import main.java.ru.raiffeisen.cources.atm.model.constants.CurrencyHolder;
import main.java.ru.raiffeisen.cources.atm.model.db.DAO.AtmDAO;
import main.java.ru.raiffeisen.cources.atm.model.db.SingleConnectionManager;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import main.java.ru.raiffeisen.cources.atm.model.score.CreditScore;
import main.java.ru.raiffeisen.cources.atm.model.score.CurrentScore;
import main.java.ru.raiffeisen.cources.atm.model.score.DebetScore;
import main.java.ru.raiffeisen.cources.atm.model.score.OperationLimitATM;
import main.java.ru.raiffeisen.cources.atm.model.score.DumpType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Scanner;

public class ATM {
    private CurrentScore currentScore;
    private DebetScore debetScore;
    private CreditScore creditScore;
    private int operLimit;
    private int currentOpers;
    private boolean operLimitToggl;

    public ATM() {
        Class thisClass = this.getClass();
        for (Annotation annotation:
                thisClass.getAnnotations()) {
            if(annotation instanceof OperationLimitATM){
                this.operLimit = ((OperationLimitATM)annotation).limit();
                this.operLimitToggl = true;
            }
        }

        this.creditScore =
                new CreditScore(
                        new Money(1000.0d,
                                  CurrencyHolder.getCurrencyByName("RUR").getName()
                                 ), null, 1);
        this.debetScore =
                new DebetScore(
                        new Money(1000.0d,
                                CurrencyHolder.getCurrencyByName("RUR").getName()
                        ), null, 1, this.creditScore);
        this.currentScore =
                new CurrentScore(
                        new Money(1000.0d,
                                CurrencyHolder.getCurrencyByName("RUR").getName()
                        ), null, 1, this.debetScore);
    }

    public ATM(CurrentScore currentScore, DebetScore debetScore, CreditScore creditScore) {
        this.currentScore = currentScore;
        this.debetScore = debetScore;
        this.creditScore = creditScore;

        Class thisClass = this.getClass();
        for (Annotation annotation:
                thisClass.getAnnotations()) {
            if(annotation instanceof OperationLimitATM){
                this.operLimit = ((OperationLimitATM)annotation).limit();
                this.operLimitToggl = true;
            }
        }
    }

    public void addMoneyToScore(Money money, ScoreTypeEnum choice){
        if(operLimitToggl && currentOpers >= operLimit){
            System.out.println("Oper limit ends!");
            return;
        }

        switch (choice){
            case CREDIT:
                this.creditScore.addMoney(money);
                break;
            case DEBET:
                this.debetScore.addMoney(money);
                break;
            case CURRENT:
                this.currentScore.addMoney(money);
                break;
        }
        currentOpers++;
    }

    public Money getMoneyFromScore(Money money, ScoreTypeEnum choice){
        if(operLimitToggl && currentOpers >= operLimit){
            System.out.println("Oper limit ends!");
            return null;
        }
        currentOpers++;
        switch (choice){
            case CREDIT:
                return this.creditScore.getMoney(money.getValue());
            case DEBET:
                return this.debetScore.getMoney(money.getValue());
            case CURRENT:
                return this.currentScore.getMoney(money.getValue());
        }

        return null;
    }

    public void restore(DumpType dumpType){
        switch (dumpType){
            case DB:
                System.out.println("Not implemented yet!");
                break;
            case XML:
                System.out.println("Not implemented yet!");
                break;
            case JSON:
                restoreFromJSON();
                break;
        }
    }

    public void dump(DumpType dumpType){
        switch (dumpType){
            case DB:
                System.out.println("Not implemented yet!");
                break;
            case XML:
                System.out.println("Not implemented yet!");
                break;
            case JSON:
                dumpToJSON();
                break;
        }
    }

    public void restoreFromJSON(){
        File dampFile = new File("dumpJSON.txt");
        if(dampFile.exists()){
            try (FileReader fileReader = new FileReader(dampFile)){
                Scanner fileScaner = new Scanner(fileReader);
                StringBuilder atmJSONBuilder = new StringBuilder();

                String line = null;
                while (fileScaner.hasNext()) {
                    line = fileScaner.next();
                    atmJSONBuilder.append(line);
                }

                ATM newAtm = getATMFromJSONString(atmJSONBuilder);
                copy(newAtm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ATM getATMFromJSONString(StringBuilder jsonBuilder){
        return new Gson().fromJson(jsonBuilder.toString(), ATM.class);
    }

    private AtmDAO atmDAO = new AtmDAO(new SingleConnectionManager());

    public AtmDAO getAtmDAO() {
        return atmDAO;
    }

    public void setAtmDAO(AtmDAO atmDAO) {
        this.atmDAO = atmDAO;
    }

    public double getAllScoresBalanceFromDB(){
        ATM atm = atmDAO.getAtm();

        double sum = 0;

        sum += atm.creditScore.getMoneyWithoutLess().getValue();
        sum += atm.getDebetScore().getMoneyWithoutLess().getValue();
        sum += atm.currentScore.getMoneyWithoutLess().getValue();

        return sum;
    }

    public CurrentScore getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(CurrentScore currentScore) {
        this.currentScore = currentScore;
    }

    public DebetScore getDebetScore() {
        return debetScore;
    }

    public void setDebetScore(DebetScore debetScore) {
        this.debetScore = debetScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ATM atm = (ATM) o;

        if (operLimit != atm.operLimit) return false;
        if (currentOpers != atm.currentOpers) return false;
        if (operLimitToggl != atm.operLimitToggl) return false;
        if (currentScore != null ? !currentScore.equals(atm.currentScore) : atm.currentScore != null) return false;
        if (debetScore != null ? !debetScore.equals(atm.debetScore) : atm.debetScore != null) return false;
        return creditScore != null ? creditScore.equals(atm.creditScore) : atm.creditScore == null;
    }

    @Override
    public int hashCode() {
        int result = currentScore != null ? currentScore.hashCode() : 0;
        result = 31 * result + (debetScore != null ? debetScore.hashCode() : 0);
        result = 31 * result + (creditScore != null ? creditScore.hashCode() : 0);
        result = 31 * result + operLimit;
        result = 31 * result + currentOpers;
        result = 31 * result + (operLimitToggl ? 1 : 0);
        return result;
    }

    private void copy(ATM atm){
        this.currentScore = atm.currentScore;
        this.debetScore = atm.debetScore;
        this.creditScore = atm.creditScore;
        this.operLimit =  atm.operLimit;
        this.currentOpers = atm.currentOpers;
        this.operLimitToggl = atm.operLimitToggl;
    }

    private void dumpToJSON(){
        String atmJSON = new Gson().toJson(this);
        File dampFile = new File("dumpJSON.txt");
        try (FileWriter fileWriter = new FileWriter(dampFile)){
            dampFile.createNewFile();
            fileWriter.write(atmJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CreditScore getCreditScore() {
        return creditScore;
    }
}
