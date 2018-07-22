package main.java.ru.raiffeisen.cources.atm.model.money;

public class Currency {
    private String name;
    private float usdCource;

    public Currency(String name, float usdCource) {
        this.name = name;
        this.usdCource = usdCource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUsdCource() {
        return usdCource;
    }

    public void setUsdCource(float usdCource) {
        this.usdCource = usdCource;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", usdCource=" + usdCource +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (Float.compare(currency.usdCource, usdCource) != 0) return false;
        return name != null ? name.equals(currency.name) : currency.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (usdCource != +0.0f ? Float.floatToIntBits(usdCource) : 0);
        return result;
    }
}
