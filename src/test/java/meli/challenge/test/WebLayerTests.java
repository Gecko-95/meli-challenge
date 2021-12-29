package meli.challenge.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import meli.challenge.test.model.Statistics;
import meli.challenge.test.rest.CountryInfoRestClient;
import meli.challenge.test.rest.IpInfoRestClient;
import meli.challenge.test.utils.DistanceCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import meli.challenge.test.controller.IpController;
import meli.challenge.test.model.CountryInfoComplete;
import meli.challenge.test.model.Statistics;
import meli.challenge.test.service.IpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

/*
 * @WebMvcTest - for testing the controller layer exclusively
 * Includes @ExtendWith(SpringExtension.class) for Spring TestContext Framework into JUnit 5's Jupiter programming model.
 * @AutoConfigureWebMvc and the @AutoConfigureMockMvc are also included among other functionality.
 */
@WebMvcTest({IpController.class, IpInfoRestClient.class, CountryInfoRestClient.class})
@ActiveProfiles("test")
public class WebLayerTests {

	/*
	 * We can @Autowire MockMvc because the WebApplicationContext provides an
	 * instance/bean for us
	 */
	@Autowired
	MockMvc mockMvc;

	/*
	 * Jackson mapper for Object -> JSON conversion
	 */
	@Autowired
	ObjectMapper mapper;

	/*
	 * We use @MockBean because the WebApplicationContext does not provide
	 * any @Component, @Service or @Repository beans instance/bean of this service
	 * in its context. It only loads the beans solely required for testing the
	 * controller.
	 */
	@MockBean
	IpService ipService;

	@Autowired
	IpInfoRestClient ipInfoRestClient;

	@Autowired
	CountryInfoRestClient countryInfoRestClient;

	@Autowired
	IpController ipController;

	@Test
	//Prueba Unitaria
	public void post_ipsSaved_andReturnsObjWith200() {

		var statistics = ipService.getStatisticsssObjectByEndpoint();

		Mockito.when(ipService.getStatisticsssObjectByEndpoint()).thenReturn(statistics);

		List<Statistics> httpResponse = ipController.getipsSaved();

		Assert.assertEquals(statistics, httpResponse);
	}

	@Test
	//Prueba Integracion
	public void post_countryInfo_andReturnsObjWith200() throws Exception {
		String ip = "191.107.172.124";

		var ipInfo = ipInfoRestClient.ipInfo(ip);
		var countriesInfoMap = countryInfoRestClient.getAllCountriesInfoMap();

		var countryInfo = countriesInfoMap.get(ipInfo.getCountryCode3());

		LocalDateTime localDateTimeInUTC = LocalDateTime.now();

		var currentTimes = ipService.setCurrentTimes(countryInfo.getTimezones(), localDateTimeInUTC);

		var distanceBetweenVillavoToThisCountryInKm = DistanceCalculator.distance(countryInfo.getLatlng().get(0), countryInfo.getLatlng().get(1));

		var countryCurrencies = ipService.getCountryCurrencies(countryInfo.getCurrencies());

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

		Mockito.when(ipService.countryInfoComplete(ip)).thenReturn(countryInfoComplete);

		// Build post request with vehicle object payload
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/country/info?ip="+ip)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");

		mockMvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(this.mapper.writeValueAsString(countryInfoComplete)))
				.andReturn();
	}


}