package com.junit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.dto.WeatherResponse;
import com.junit.entity.Demo;
import com.junit.service.IDemoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DemoController.class)
public class DemoControllerTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	IDemoService demoService;

	@Test
	public void getAllTest() throws Exception {

		mockMvc.perform(get("/demo").contentType("application/json")).andDo(print()).andExpect(status().isOk());

	}

	@Test
	public void getByIdTest() throws Exception {
		mockMvc.perform(get("/demo/1").contentType("application/json")).andDo(print()).andExpect(status().isOk());
//					.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

	}

	@Test
	public void saveTest() throws JsonProcessingException, Exception {
		Demo demo = new Demo(1L, "Ram");
		ObjectMapper mapper = new ObjectMapper();

		when(demoService.save(any())).thenReturn("Data saved successfully");
		mockMvc.perform(post("/demo").contentType("application/json").content(mapper.writeValueAsString(demo)))
				.andDo(print()).andExpect(status().isOk());

	}

	@Test
	public void saveTest_fail() throws Exception {
		Demo demo = new Demo(1L, "Ram");
		ObjectMapper mapper = new ObjectMapper();
		when(demoService.save(any())).thenReturn(null);
		mockMvc.perform(post("/demo").contentType("application/json").content(mapper.writeValueAsBytes(demo)))
				.andDo(print()).andExpect(status().isBadRequest());

	}

	@Test
	public void getWeatherTest() throws Exception {
		String testString = "Hyderabad";
		WeatherResponse resp = new WeatherResponse(12, "64", "1015", "clear sky");
		when(demoService.weatherforecast(any())).thenReturn(resp);
		mockMvc.perform(get("/getWeather").contentType("application/json").content(testString)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.temperature").value(12));

	}

	@Test
	public void getWeatherTest_fail() throws Exception {
		String testString = "Hyderabad";
//		WeatherResponse resp = new WeatherResponse(12, "64", "1015", "clear sky");
		when(demoService.weatherforecast(any())).thenReturn(null);
		mockMvc.perform(get("/getWeather").contentType("application/json").content(testString)).andDo(print())
				.andExpect(status().isBadRequest());

	}

	@Test
	public void updateDetailsTest_demo() throws Exception {
		long demoId = 1L;
		Demo demo = new Demo(1L, "kishore", 100);
		ObjectMapper mapper = new ObjectMapper();
		when(demoService.updateDemo(any(), any())).thenReturn("data updated successfully");

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/demo/" + demoId)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(demo));
		this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("data updated successfully"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void updateDetailsNullTest() throws Exception {
		long demoId = 1L;
		Demo demo = new Demo(1L, "sai");
		ObjectMapper mapper = new ObjectMapper();
		when(demoService.updateDemo(demoId, demo)).thenReturn(null);
		mockMvc.perform(
				put("/demo/{id}", demoId).contentType("application/json").content(mapper.writeValueAsString(demo)))
				.andDo(print()).andExpect(status().isInternalServerError());

	}

	@Test
	public void updateDetailsTest_fail() throws Exception {
		long demoId = 1L;
		Demo demo = new Demo(1L, "sai");
		ObjectMapper mapper = new ObjectMapper();
		when(demoService.updateDemo(1L, demo)).thenReturn(null);
		mockMvc.perform(
				put("/demo/{id}", demoId).contentType("application/json").content(mapper.writeValueAsString(demo)))
				.andDo(print()).andExpect(status().isInternalServerError());

	}

	@Test
	public void updateUsingPatchTest() throws Exception {
		long demoId = 1L;
		Map<String, Object> val = new HashMap<String, Object>();
		val.put("name", "kishore");
		String reqst = "{\r\n" + "    \"score\":100\r\n" + "}";
		ObjectMapper mapper = new ObjectMapper();
		when(demoService.updatePartialDetails(demoId, mapper.readValue(reqst, Map.class)))
				.thenReturn("updated successfully");
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/demo/" + demoId)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(reqst);
		this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("updated successfully"))
				.andDo(MockMvcResultHandlers.print());

	}

}
