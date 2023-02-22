package com.junit.service;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.junit.dto.DemoDto;
import com.junit.dto.WeatherResponse;
import com.junit.entity.Demo;
import com.junit.exception.DemoItemNotFoundException;
import com.junit.exception.DemoNameNullException;
import com.junit.repository.DemoRepository;

@Service
public class DemoService implements IDemoService {
	@Autowired
	DemoRepository demoRepository;
	@Autowired
	Environment env;

	@Autowired
	private RestTemplate weatherTemplate;

	@Autowired
	@Qualifier("test")
	private RestTemplate testTemplate;
	@Autowired
	UserService userService;

	@Override
	public List<Demo> getAll() {
//		String authToken = bearerToken.substring(7);
//		String externalId = userService.getUserExternalId(authToken);
		List<Demo> allDemoDtls = null;
		try {
			allDemoDtls = demoRepository.findAll();
		} catch (Exception e) {
			throw new DemoItemNotFoundException();
		}
		return allDemoDtls;
	}

	@Override
	public Demo findById(Long id) {
		Optional<Demo> demoDtls = demoRepository.findById(id);
		if (!demoDtls.isEmpty()) {
			return demoDtls.get();
		} else {
			throw new DemoItemNotFoundException();
		}
	}

	@Override
	public String save(Demo demo) {
		try {
			Demo demoResp = demoRepository.save(demo);
			if (demoResp != null && !ObjectUtils.isEmpty(demoResp)) {
				return "Data saved successfully";
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return "something went wrong";
		}

	}

	@Override
	public String delete(Long id) {
		demoRepository.deleteById(id);
		return "item deleted successfully";
	}

	@Override
	public WeatherResponse weatherforecast(String cityName) {
		try {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromUriString(env.getProperty("url") + "/weatherForecast").queryParam("cityName", cityName);
			URI uri = uriBuilder.build().toUri();
			ResponseEntity<WeatherResponse> respString = weatherTemplate.getForEntity(uri, WeatherResponse.class);

			return respString.getBody();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String updateDemo(Long id, Demo demo) {
		try {
			Optional<Demo> demoResp = demoRepository.findById(id);
			if (!demoResp.isEmpty()) {
				demo.setId(demoResp.get().getId());
				BeanUtils.copyProperties(demo, demoResp.get());
				demoRepository.save(demo);
				return "data updated successfully";
			} else {
				return "no item found with id";

			}
		} catch (TransactionSystemException e) {
			System.out.println(e.getMessage());
			return "Please check the mandatory fields";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public String updatePartialDetails(Long id, Map<String, Object> demo) {
		Optional<Demo> demoResp = demoRepository.findById(id);
		if (!demoResp.isEmpty()) {
			demo.forEach((k, v) -> {
				if (!k.equals("id")) {
					Field field = ReflectionUtils.findField(Demo.class, k);
					field.setAccessible(true);
					ReflectionUtils.setField(field, demoResp.get(), v);
				}

			});
			demoRepository.saveAndFlush(demoResp.get());
			return "updated successfully";
		} else {
			return "details not found";
		}

	}

	@Override
	public String test1(String message) {
		try {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(env.getProperty("url") + "/test")
					.queryParam("message", message);
			URI uri = uriBuilder.build().toUri();
			ResponseEntity<String> respString = testTemplate.getForEntity(uri, String.class);

			return respString.getBody();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String testForPost(DemoDto message) {
		try {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromUriString(env.getProperty("url") + "/testForPost");

			URI uri = uriBuilder.build().toUri();
			ResponseEntity<String> respString = testTemplate.postForEntity(uri, message, String.class);

			return respString.getBody();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String testForPut(DemoDto message, Long id) {
		try {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromUriString(env.getProperty("url") + "/testForPut/" + id);
//					.queryParam("id", id);
			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("id", id);
			String uri = uriBuilder.build().toUri().toString();
			testTemplate.put(uri, message, params);

			return "data updated";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@Override
	@Transactional
	public List<DemoDto> saveBulkDemo(List<DemoDto> demoBulk) {

		try {
			List<DemoDto> demoVals = demoBulk.stream()
					.filter(d -> (d.getName() == null || d.getName().equalsIgnoreCase("")))
					.collect(Collectors.toList());

			if (demoVals.size() != 0 || !demoVals.isEmpty()) {
				throw new DemoNameNullException();
			} else {
				List<Demo> demos = demoBulk.stream().map(d -> demoMap(d)).collect(Collectors.toList());

				List<Demo> demoResp = demoRepository.saveAll(demos);
				List<DemoDto> demoDtoResp = demoResp.stream().map(d -> demoDtoMap(d)).collect(Collectors.toList());
				return demoDtoResp;
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

	private Demo demoMap(DemoDto dto) {
		Demo demo = new Demo();
//		BeanUtils.copyProperties(dto, demo);
		demo.setName(dto.getName());
		demo.setScore(Integer.valueOf(dto.getScore()));
		return demo;
	}

	private DemoDto demoDtoMap(Demo demo) {
		DemoDto demoDto = new DemoDto();
		BeanUtils.copyProperties(demo, demoDto);
//		throw new RuntimeException();
		return demoDto;
	}
}
