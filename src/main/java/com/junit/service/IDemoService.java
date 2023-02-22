package com.junit.service;

import java.util.List;
import java.util.Map;

import com.junit.dto.DemoDto;
import com.junit.dto.WeatherResponse;
import com.junit.entity.Demo;

public interface IDemoService {
//	List<Demo> getAll();

	Demo findById(Long id);

	String save(Demo demo);

	String delete(Long id);

	WeatherResponse weatherforecast(String cityName);

	String updateDemo(Long id, Demo demo);

	String updatePartialDetails(Long id, Map<String, Object> demo);

	String test1(String message);

	List<Demo> getAll();

	List<DemoDto> saveBulkDemo(List<DemoDto> demoBulk);

	String testForPost(DemoDto message);

	String testForPut(DemoDto message, Long id);

//	DemoDto demoConvertAndSave(DemoDto dto);

}
