package meli.challenge.test.service;

import meli.challenge.test.model.BlackList;
import meli.challenge.test.model.CountryInfoComplete;
import meli.challenge.test.model.Statistics;
import meli.challenge.test.model.StatisticsDTO;
import meli.challenge.test.model.country.CountryCurrency;
import meli.challenge.test.model.country.Currency;
import meli.challenge.test.repository.BlackListRepository;
import meli.challenge.test.repository.StatisticsRepository;
import meli.challenge.test.rest.CountryInfoRestClient;
import meli.challenge.test.rest.CurrencyInfoRestClient;
import meli.challenge.test.rest.IpInfoRestClient;
import meli.challenge.test.utils.DistanceCalculator;
import meli.challenge.test.utils.ValidateIpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static meli.challenge.test.utils.Constants.*;

@Service
public class IpService {

    @Autowired
    CountryInfoRestClient countryInfoRestClient;

    @Autowired
    IpInfoRestClient ipInfoRestClient;

    @Autowired
    CurrencyInfoRestClient currencyInfoRestClient;

    @Autowired
    RedisTemplate<String, StatisticsDTO> redisTemplate;

    @Autowired
    StatisticsRepository statisticsRepository;

    @Autowired
    BlackListRepository blackListRepository;

    @Autowired
    RedisTemplate<String, BlackList> redisBlackTemplate;

    public CountryInfoComplete countryInfoComplete(String ip) {

        if(!ValidateIpAddress.validateIPAddress(ip)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "La ip ingresada no posee el patron 'XXX.XXX.XXX.XXX' ");
        }

        var blackListToRedis = this.getBlackListObject(ip);

        if(!blackListToRedis.isEmpty()){
            this.addBlackListObjectToRedis(blackListToRedis.get());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "La ip ingresada se encuentra en la lista negra.");
        }

        var ipInfo = ipInfoRestClient.ipInfo(ip);
        var countriesInfoMap = countryInfoRestClient.getAllCountriesInfoMap();

        var countryInfo = countriesInfoMap.get(ipInfo.getCountryCode3());

        LocalDateTime localDateTimeInUTC = LocalDateTime.now();

        var currentTimes = this.setCurrentTimes(countryInfo.getTimezones(), localDateTimeInUTC);

        var distanceBetweenVillavoToThisCountryInKm = DistanceCalculator.distance(countryInfo.getLatlng().get(0), countryInfo.getLatlng().get(1));

        var countryCurrencies = this.getCountryCurrencies(countryInfo.getCurrencies());

        CountryInfoComplete countryInfoComplete = new CountryInfoComplete(
                ip,
                countryInfo.getName(),
                countryInfo.getAlpha2Code(),
                countryInfo.getAlpha3Code(),
                countryInfo.getLanguages(),
                localDateTimeInUTC,
                currentTimes,
                distanceBetweenVillavoToThisCountryInKm,
                countryCurrencies);

        // armamos el objeto a persistir
        var statistics = new Statistics(ip, distanceBetweenVillavoToThisCountryInKm);

        //persistimos el objeto
        statisticsRepository.save(statistics);

        // obtengo el objeto de redis y si no existe lo trae de sql
        var statisticsToRedis = this.getStatisticsObject(countryInfoComplete.getDistanceBetweenVillavoToThisCountryInKm());

        //guardo el objeto en redis
        this.addStatisticsObjectToRedis(statisticsToRedis.get());

        return countryInfoComplete;
    }

    public CountryInfoComplete blackList(String ip) {

        if(!ValidateIpAddress.validateIPAddress(ip)){
            throw new RuntimeException("La ip ingresada no posee el patron 'XXX.XXX.XXX.XXX' ");
        }

        var ipInfo = ipInfoRestClient.ipInfo(ip);
        var countriesInfoMap = countryInfoRestClient.getAllCountriesInfoMap();

        var countryInfo = countriesInfoMap.get(ipInfo.getCountryCode3());

        LocalDateTime localDateTimeInUTC = LocalDateTime.now();

        var currentTimes = this.setCurrentTimes(countryInfo.getTimezones(), localDateTimeInUTC);

        var distanceBetweenVillavoToThisCountryInKm = DistanceCalculator.distance(countryInfo.getLatlng().get(0), countryInfo.getLatlng().get(1));

        var countryCurrencies = this.getCountryCurrencies(countryInfo.getCurrencies());

        CountryInfoComplete countryInfoComplete = new CountryInfoComplete(
                ip,
                countryInfo.getName(),
                countryInfo.getAlpha2Code(),
                countryInfo.getAlpha3Code(),
                countryInfo.getLanguages(),
                localDateTimeInUTC,
                currentTimes,
                distanceBetweenVillavoToThisCountryInKm,
                countryCurrencies);

        if(this.getBlackListObject(ip).isEmpty()){

            // armamos el objeto a persistir
            var blackList = new BlackList(ip);

            //persistimos el objeto
            blackListRepository.save(blackList);

        }

        // obtengo el objeto de redis y si no existe lo trae de sql
        var blackListToRedis = this.getBlackListObject(ip);

        //guardo el objeto en redis
        this.addBlackListObjectToRedis(blackListToRedis.get());

        return countryInfoComplete;
    }

    public List<CountryCurrency> getCountryCurrencies(List<Currency> countryInfoCurrency) {

        var exchangesRateInEuroComplete = currencyInfoRestClient.getCountryCodeExchangeRates().getRates();

        var countryCurrencies = countryInfoCurrency.stream().map(currency -> new CountryCurrency(
                currency.getCode(),
                currency.getName(),
                exchangesRateInEuroComplete.getOrDefault(currency.getCode(), -1D))).collect(Collectors.toList());

        return countryCurrencies;
    }

    public List<LocalDateTime> setCurrentTimes(List<String> timeZones, LocalDateTime localDateTimeInUTC) {
        List<LocalDateTime> currentTimes = new ArrayList<>();

        var timeZonesWithouthUTC = timeZones.stream().map(s -> s.replace("UTC", "").replace(":00", "")).collect(Collectors.toList());

        var timeZonesParsedToLong = timeZonesWithouthUTC.stream().map(Long::parseLong).collect(Collectors.toList());

        timeZonesParsedToLong.forEach(timeZone -> currentTimes.add(localDateTimeInUTC.plusHours(timeZone)));
        return currentTimes;
    }

    private void addBlackListObjectToRedis(BlackList params) {
        try {
            ValueOperations<String, BlackList> opsForValue = redisBlackTemplate.opsForValue();
            opsForValue.set("BLACK_REDIS"+params.getIp(), params, 900, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }

    private void addStatisticsObjectToRedis(StatisticsDTO params) {
        try {
            ValueOperations<String, StatisticsDTO> opsForValue = redisTemplate.opsForValue();
            opsForValue.set("STATISTICS_REDIS", params, 900, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }

    private Optional<BlackList> getBlackListObject(String ip) {
        try {
            ValueOperations<String, BlackList> opsForValue = redisBlackTemplate.opsForValue();
            Optional<BlackList> params = Optional.ofNullable(opsForValue.get("BLACK_REDIS"+ip));
            if (params.isEmpty() || !params.get().getIp().equals(ip)) {
                params = Optional.ofNullable(blackListRepository.findByIp(ip).stream()
                        .collect(Collectors.toList()).get(0));
            }

            return params;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<StatisticsDTO> getStatisticsObject(Double distanceBetweenVillavoToThisCountryInKm) {
        try {
            ValueOperations<String, StatisticsDTO> opsForValue = redisTemplate.opsForValue();
            Optional<StatisticsDTO> params = Optional.ofNullable(opsForValue.get("STATISTICS_REDIS"));
            if (params.isEmpty() || params.get().getMaxDistanceToVillavo() == null) {

                params = Optional.ofNullable(statisticsRepository.averageDistanceToVillavo().stream()
                        .map(this::convertToItem)
                        .collect(Collectors.toList()).get(0));

            }
            else{

                // aca actualizo los valores del objeto de redis. esto lo hago en el caso de que ya exista en redis
                // porque los valores van a ser "viejos" por estar cacheados y no "nuevos" por ser traidos de sql
                var statisticsUpdated = this.updateStatistics(params.get(), distanceBetweenVillavoToThisCountryInKm);
                params = Optional.of(statisticsUpdated);

            }

            return params;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private StatisticsDTO updateStatistics(StatisticsDTO statisticsToRedis, Double distanceBetweenVillavoToThisCountryInKm) {

        var maxDistance = statisticsToRedis.getMaxDistanceToVillavo() > distanceBetweenVillavoToThisCountryInKm ? statisticsToRedis.getMaxDistanceToVillavo() : distanceBetweenVillavoToThisCountryInKm;
        var minDistance = statisticsToRedis.getMinDistanceToVillavo() < distanceBetweenVillavoToThisCountryInKm ? statisticsToRedis.getMaxDistanceToVillavo() : distanceBetweenVillavoToThisCountryInKm;
        var sumDistances = statisticsToRedis.getAverage() * statisticsToRedis.getQuantity() + distanceBetweenVillavoToThisCountryInKm;
        var quantity = statisticsToRedis.getQuantity() + 1;

        var average = sumDistances / quantity;
        return new StatisticsDTO(average, maxDistance, minDistance, quantity);

    }

    private StatisticsDTO convertToItem(Map<String, ?> item) {

        var average = (Double) item.get("average");
        var min = (Double) item.get("min");
        var max = (Double) item.get("max");
        var quantity = (BigInteger) item.get("quantity");

        return new StatisticsDTO(average, max, min, quantity.intValue());
    }

    public Optional<StatisticsDTO> getStatisticsObjectByEndpoint() {
        try {
            ValueOperations<String, StatisticsDTO> opsForValue = redisTemplate.opsForValue();
            Optional<StatisticsDTO> params = Optional.ofNullable(opsForValue.get("STATISTICS_REDIS"));
            if (params.isEmpty()) {

                params = Optional.ofNullable(statisticsRepository.averageDistanceToVillavo().stream()
                        .map(this::convertToItem)
                        .collect(Collectors.toList()).get(0));

                this.addStatisticsObjectToRedis(params.get());

            }

            return params;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo statistics /", e);
        }
    }

    public List<Statistics> getStatisticsssObjectByEndpoint() {
        List<Statistics> resultList = new ArrayList<>();
        statisticsRepository.findAll().forEach(resultList::add);

        return resultList;
    }

    /**
     * Metodo para limpiar todas las caches
     */
    @CacheEvict(allEntries = true, value = {COUNTRY_CODES_INFO_CACHE, COUNTRY_EXCHANGE_RATE_CACHE, COUNTRIES_INFO_MAP_CACHE})
    public void clearCache() {
        System.out.println("Flush Memory Cache by endpoint " + new Date());
    }

}
