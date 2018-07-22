package main.java.ru.raiffeisen.cources.atm.model.db.DAO;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.model.db.IConnectionManager;
import main.java.ru.raiffeisen.cources.atm.model.db.SingleConnectionManager;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import main.java.ru.raiffeisen.cources.atm.model.score.CreditScore;
import main.java.ru.raiffeisen.cources.atm.model.score.CurrentScore;
import main.java.ru.raiffeisen.cources.atm.model.score.DebetScore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AtmDAO extends AbstractDAO {
    public AtmDAO(IConnectionManager connectionManager) {
        super(connectionManager);
    }

    public ATM getAtm(){
        ATM newATM = null;
        Statement statement = null;

        try{
            statement = super.connectionManager.getConnection()
                    .createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM public.credit cr \n" +
                    "\t JOIN public.currency cur ON cr.currency_id = cur.id");

            rs.next();

            Money money = new Money(rs.getDouble("course"),
                    rs.getString("name"));
            CreditScore creditScore = new CreditScore(money, null, null);

            rs = statement.executeQuery("SELECT * FROM public.debet cr \n" +
                    "\t JOIN public.currency cur ON cr.currency_id = cur.id");

            rs.next();

            money = new Money(rs.getDouble("course"),
                    rs.getString("name"));
            DebetScore debetScore = new DebetScore(money, null, null, creditScore);

            rs = statement.executeQuery("SELECT * FROM public.current cr \n" +
                    "\t JOIN public.currency cur ON cr.currency_id = cur.id");

            rs.next();

            money = new Money(rs.getDouble("value"),
                    rs.getString("name"));
            CurrentScore currentScore = new CurrentScore(money, null, null, debetScore);

            newATM = new ATM(currentScore,debetScore,creditScore);
        } catch (SQLException sqlEX) {
            sqlEX.printStackTrace();
        }

        return newATM;
    }

    public static void main(String[] args) {
        AtmDAO atmDAO = new AtmDAO(new SingleConnectionManager());
        atmDAO.getAtm();
    }
}
