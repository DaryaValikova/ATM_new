package tests.java.ru.raifeisen.cources.atm.data;

public class TestPair<T> {
    private T testValue;
    private T expectedValue;

    public TestPair(T testValue, T expectedValue) {
        this.testValue = testValue;
        this.expectedValue = expectedValue;
    }

    public T getTestValue() {
        return testValue;
    }

    public void setTestValue(T testValue) {
        this.testValue = testValue;
    }

    public T getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(T expectedValue) {
        this.expectedValue = expectedValue;
    }
}
