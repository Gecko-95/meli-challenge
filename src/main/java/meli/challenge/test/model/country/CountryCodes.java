package meli.challenge.test.model.country;

import java.io.Serializable;
import java.util.Map;

public class CountryCodes implements Serializable {
    private static final long serialVersionUID = 5655422392454107892L;

    private Boolean success;
    private Map<String, String> symbols;

    public CountryCodes() {
    }

    public CountryCodes(Boolean success, Map<String, String> symbols) {
        this.success = success;
        this.symbols = symbols;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Map<String, String> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, String> symbols) {
        this.symbols = symbols;
    }
}


