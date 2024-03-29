package meli.challenge.test.model;

import meli.challenge.test.model.country.CountryCurrency;
import meli.challenge.test.model.country.Language;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class CountryInfoComplete implements Serializable {
    private static final long serialVersionUID = -1725625507979295639L;

    private String ipInserted;
    private String name;
    private String code;
    private String isoCode;
    private List<Language> officialLanguages;
    private LocalDateTime currentTimeInUTC;
    private List<LocalDateTime> currentTimes;
    private double distanceBetweenVillavoToThisCountryInKm;
    private List<CountryCurrency> countryCurrencies;


    public CountryInfoComplete() {
    }


    public CountryInfoComplete(String ipInserted, String name, String code, String isoCode, List<Language> officialLanguages, LocalDateTime currentTimeInUTC, List<LocalDateTime> currentTimes, double distanceBetweenVillavoToThisCountryInKm, List<CountryCurrency> countryCurrencies) {
        this.ipInserted = ipInserted;
        this.name = name;
        this.code = code;
        this.isoCode = isoCode;
        this.officialLanguages = officialLanguages;
        this.currentTimeInUTC = currentTimeInUTC;
        this.currentTimes = currentTimes;
        this.distanceBetweenVillavoToThisCountryInKm = distanceBetweenVillavoToThisCountryInKm;
        this.countryCurrencies = countryCurrencies;
    }

    public String getIpInserted() {
        return ipInserted;
    }

    public void setIpInserted(String ipInserted) {
        this.ipInserted = ipInserted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public List<Language> getOfficialLanguages() {
        return officialLanguages;
    }

    public void setOfficialLanguages(List<Language> officialLanguages) {
        this.officialLanguages = officialLanguages;
    }

    public LocalDateTime getCurrentTimeInUTC() {
        return currentTimeInUTC;
    }

    public void setCurrentTimeInUTC(LocalDateTime currentTimeInUTC) {
        this.currentTimeInUTC = currentTimeInUTC;
    }

    public List<LocalDateTime> getCurrentTimes() {
        return currentTimes;
    }

    public void setCurrentTimes(List<LocalDateTime> currentTimes) {
        this.currentTimes = currentTimes;
    }

    public double getDistanceBetweenVillavoToThisCountryInKm() {
        return distanceBetweenVillavoToThisCountryInKm;
    }

    public void setDistanceBetweenVillavoToThisCountryInKm(double distanceBetweenVillavoToThisCountryInKm) {
        this.distanceBetweenVillavoToThisCountryInKm = distanceBetweenVillavoToThisCountryInKm;
    }

    public List<CountryCurrency> getCountryCurrencies() {
        return countryCurrencies;
    }

    public void setCountryCurrencies(List<CountryCurrency> countryCurrencies) {
        this.countryCurrencies = countryCurrencies;
    }
}
