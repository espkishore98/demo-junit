package com.junit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.dto.WeatherResponse;
import com.junit.entity.Demo;
import com.junit.exception.DemoItemNotFoundException;
import com.junit.repository.DemoRepository;
import com.junit.testsuite.TestResultClass;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ExtendWith(TestResultClass.class)
public class DemoServiceTests {
	@InjectMocks
	DemoService demoService;

	@Mock
	DemoRepository demoRepository;

	@Mock
	Environment env;
	@Captor
	ArgumentCaptor<Demo> argumentCaptor;

	@Mock
	ModelMapper mapper;

	@Value("${weatherurl}")
	String weatherUrl;
	@Mock
	private RestTemplate restTemplate;
	private MockRestServiceServer mockServer;
	private Validator validator;

	private String url;

	@BeforeEach
	public void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		url = env.getProperty("weatherurl");

	}

	@Test
	public void testDemoSuccess() {
		Demo demo = new Demo();
		demo.setId(1L);
		demo.setName("");
		Set<ConstraintViolation<Demo>> violations = validator.validate(demo);
		assertFalse(violations.isEmpty());
	}

	@Test
	void testDelete() {

		String result = demoService.delete(1L);

		assertThat(result).isEqualTo("item deleted successfully");
		verify(demoRepository).deleteById(1L);
	}

	@Test
	public void testFindDemoById() {
		Demo demo = new Demo(1L, "Kishore");

		when(demoRepository.findById(1L)).thenReturn(Optional.of(demo));
		Demo actualResponse = demoService.findById(1L);
		assertThat(demo.getId()).isEqualTo(actualResponse.getId());
		assertThat(demo.getName()).isEqualTo(actualResponse.getName());
	}

	@Test
	void testFindById_DemoRepositoryReturnsAbsent() {
//		when(demoService.findById(1L)).thenThrow(DemoItemNotFoundException.class);

		assertThrows(DemoItemNotFoundException.class, () -> demoService.findById(1L));
	}

	@Test
	public void getAllDemoDetailsTests() {
		List<Demo> demo = List.of(new Demo(1L, "kishore"), new Demo(2L, "sai"), new Demo(3L, "balram"));
		when(demoRepository.findAll()).thenReturn(demo);
		List<Demo> actualResp = demoService.getAll();
		assertEquals(demo.size(), actualResp.size());

	}

	@Test
	void testGetAll_DemoRepositoryReturnsNoItems() {
		// Setup
		when(demoRepository.findAll()).thenReturn(Collections.emptyList());
		// Run the test
		List<Demo> result = demoService.getAll();
		// Verify the results
		assertThat(result).isEqualTo(Collections.emptyList());
	}

	@Test
	void test_getAll_Exception() {
//		when(demoService.findById(1L)).thenThrow(DemoItemNotFoundException.class);

//		assertThrows(DemoItemNotFoundException.class, () -> demoService.getAll());
		when(demoRepository.findAll()).thenThrow(DemoItemNotFoundException.class);
		assertThrows(DemoItemNotFoundException.class, () -> demoService.getAll());
	}

	@Test
	void testSave() {
		Demo demo = new Demo(1L, "Kishore");
		when(demoRepository.save(any(Demo.class))).thenReturn(demo);
		final String result = demoService.save(demo);
		assertThat(result).isEqualTo("Data saved successfully");
		Mockito.verify(demoRepository, times(1)).save(ArgumentMatchers.any(Demo.class));

	}

//	@Test
//	void demoConvertAndSaveTest() {
//		DemoDto dto = new DemoDto("Esp", 111L);
//		DemoDto dtoResp = new DemoDto("Esp", 111L);
//
//		Demo demoResp = new Demo(1L, "Esp", 111);
//		Demo demoReqst = new Demo(null, "Esp", 111);
//		when(mapper.map(dto, Demo.class)).thenReturn(demoReqst);
//		when(demoRepository.save(demoReqst)).thenReturn(demoResp);
//		when(mapper.map(demoResp, DemoDto.class)).thenReturn(dtoResp);
//		DemoDto demoDto = demoService.demoConvertAndSave(dto);
//		assertEquals(dtoResp.getName(), demoDto.getName());
//
//	}

	@Test
	void testSave_DemoRepositoryReturnsNull() {
		// Setup
		final Demo demo = new Demo(1L, "Kishore");
		when(demoRepository.save(any(Demo.class))).thenReturn(null);

		String result = demoService.save(demo);

		assertThat(result).isNull();
	}

	@Test
	void testSave_exception() {
		// Setup
		Demo demo = new Demo(1L, "Kishore");
		when(demoRepository.save(any(Demo.class))).thenThrow(RuntimeException.class);
		String result = demoService.save(demo);
		assertEquals("something went wrong", result);
		verify(demoRepository, times(1)).save(any());
	}

	@Test
	void captorTest() {
		List<Demo> demo = mock(List.class);
		demo.add(new Demo(1L, "kishore"));
		demo.add(new Demo(2L, "sai"));
		verify(demo, times(2)).add(argumentCaptor.capture());
		List<Demo> demoList = argumentCaptor.getAllValues();
		assertEquals("kishore", demoList.get(0).getName());
	}

	@Test
	public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject()
			throws URISyntaxException, JsonProcessingException {

		WeatherResponse dummyResp = new WeatherResponse(28.23f, "17", "1020", "clear sky");
		Mockito.when(env.getProperty("weatherurl")).thenReturn("http://localhost:9002/weatherForecast");
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(env.getProperty("weatherurl"))
				.queryParam("cityName", "Hyderabad");
		URI uri = uriBuilder.build().toUri();
		ObjectMapper mapper = new ObjectMapper();
		when(restTemplate.getForEntity(any(), eq(WeatherResponse.class)))
				.thenReturn(new ResponseEntity<>(dummyResp, HttpStatus.OK));

		WeatherResponse actualWeatherResponse = demoService.weatherforecast("Hyderabad");

		assertEquals(dummyResp, actualWeatherResponse);
		assertEquals(dummyResp.getHumidity(), actualWeatherResponse.getHumidity());
	}

	@Test
	public void testWeatherForecastFailure() throws Exception {
		String cityName = "New York";
		Mockito.when(env.getProperty("weatherurl")).thenReturn("http://localhost:9002/weatherForecast");
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(env.getProperty("weatherurl"))
				.queryParam("cityName", "Hyderabad");
		URI uri = uriBuilder.build().toUri();
		when(restTemplate.getForEntity(uri, WeatherResponse.class)).thenThrow(new RuntimeException());
		WeatherResponse actualWeatherResponse = demoService.weatherforecast(cityName);
		assertEquals(null, actualWeatherResponse);
	}

	@Test
	void testUpdate_ForElseCondition() {
		// Setup
		Long demoId = 1L;
		Demo demoReqst = new Demo(1L, "sai");
		when(demoRepository.findById(demoId)).thenReturn(Optional.empty());
		String result = demoService.updateDemo(1L, demoReqst);
		assertEquals("no item found with id", result);

	}

	@Test
	void testUpdate() {
		Long demoId = 1L;
		Optional<Demo> demo = Optional.of(new Demo(1L, "Kishore"));
		Demo demoReqst = new Demo(1L, "sai");
		when(demoRepository.findById(demoId)).thenReturn(demo);
		String result = demoService.updateDemo(demoId, demoReqst);
		assertThat(result).isEqualTo("data updated successfully");
//		Mockito.verify(demoRepository, times(1)).save(ArgumentMatchers.any(Demo.class));
	}

	@Test
	void testUpdate_ForTransactionSystemException() {
		// Setup
		Long demoId = 1L;
		Demo demoReqst = new Demo(1L, "sai");

		when(demoRepository.findById(demoId)).thenThrow(TransactionSystemException.class);
		String result = demoService.updateDemo(demoId, demoReqst);
		assertEquals("Please check the mandatory fields", result);
	}

	@Test
	void testUpdate_ForException() {
		// Setup
		Long demoId = 1L;
		Demo demoReqst = new Demo(1L, "sai");

		when(demoRepository.findById(demoId)).thenThrow(RuntimeException.class);
		String result = demoService.updateDemo(demoId, demoReqst);
		assertEquals(null, result);
	}

	@Test
	void updatePartialDetailsTest() {
		Long demoId = 1L;
		Optional<Demo> demo = Optional.of(new Demo(1L, "sai"));
		Demo demoResp = new Demo(1L, "Kishore");
		when(demoRepository.findById(1L)).thenReturn(demo);
		Map<String, Object> testMap = new HashMap<String, Object>();
		testMap.put("name", "Kishore");
//		testMap.forEach((k, v) -> {
//			Field field = ReflectionUtils.findField(Demo.class, k);
//			field.setAccessible(true);
//			ReflectionUtils.setField(field, demo.get(), v);
//
//		});
		demoRepository.saveAndFlush(demoResp);
		String result = demoService.updatePartialDetails(demoId, testMap);
		assertEquals("updated successfully", result);
		verify(demoRepository, times(1)).saveAndFlush(demoResp);

	}

	@Test
	void updatePartialDetailsTest_ElseCondition() {
		Long demoId = 1L;
		Demo demoResp = new Demo(1L, "Kishore");
		when(demoRepository.findById(1L)).thenReturn(Optional.empty());
		Map<String, Object> testMap = new HashMap<String, Object>();
		testMap.put("name", "Kishore");
		String result = demoService.updatePartialDetails(demoId, testMap);
		assertEquals("details not found", result);
		verify(demoRepository, times(0)).saveAndFlush(demoResp);

	}

	@Test
	@Disabled
	void dummyTest_disable() {

	}

	@Test
	void dummyTest_abort() {
		Assumptions.assumeTrue(false);

	}

}
