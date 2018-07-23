package tests.java.ru.raifeisen.cources.atm;

import main.java.ru.raiffeisen.cources.atm.ATM;
import main.java.ru.raiffeisen.cources.atm.ScoreTypeEnum;
import main.java.ru.raiffeisen.cources.atm.model.money.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import tests.java.ru.raifeisen.cources.atm.data.AtmDataSupplierCredit;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ATMTestWithParams {
    private static ATM atm;
    private static final AtmDataSupplierCredit atmDataSupplier = new AtmDataSupplierCredit();

    @BeforeAll
    static void init(){
        atm = atmDataSupplier.getStartDataATM();
    }

    @BeforeEach
    void fillData(){
        atmDataSupplier.fillATMcreditScore(atm);
    }

    @ParameterizedTest
    @ValueSource(doubles = {100, 0, 1000})
    void addMoneyToScore(double value) {
        Money money = new Money(value, "RUR");

        atm.addMoneyToScore(money, ScoreTypeEnum.CREDIT);
        Money newMoney = atmDataSupplier.getMoneyFromCredit(atm);
        Money expectedMoney = new Money(1000 + value, "RUR");

        assertEquals(expectedMoney, newMoney);
    }

    @ParameterizedTest
    @EnumSource(ScoreTypeEnum.class)
    void addMoneyToScore(ScoreTypeEnum scoreTypeEnum){
        Money money = new Money(100, "RUR");

        atm.addMoneyToScore(money, scoreTypeEnum);
    }

    @ParameterizedTest(name = "{index} => value={0}, valut={1}")
    @CsvSource({
                "100, RUR",
                "1000, USD"
                })
    void addMoneyToScore(String value, String valut){
        Money money = new Money(Double.parseDouble(value), valut);

        atm.addMoneyToScore(money, ScoreTypeEnum.CREDIT);
    }

    @ParameterizedTest(name = "{index} => value={0}, valut={1}")
    @MethodSource("addMoneyToScoreSecondPr")
    void addMoneyToScoreSecond(String value, String valut){
        Money money = new Money(Double.parseDouble(value), valut);

        atm.addMoneyToScore(money, ScoreTypeEnum.CREDIT);
    }

    private static Stream<Arguments> addMoneyToScoreSecondPr(){
        return Stream.of(
                Arguments.of("1000", "RUR"),
                Arguments.of("100", "USD")
        );
    }

    @ParameterizedTest(name = "{index} => value={0}, valut={1}")
    @ArgumentsSource(AddMoneyToScoreClassProvider.class)
    void addMoneyToScoreClass(String value, String valut){
        Money money = new Money(Double.parseDouble(value), valut);

        atm.addMoneyToScore(money, ScoreTypeEnum.CREDIT);
    }

    /*static class AddMoneyToScoreClassProvider implements ArgumentsProvider{

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("1000", "RUR"),
                    Arguments.of("100", "USD")
            );
        }
    }*/
}
