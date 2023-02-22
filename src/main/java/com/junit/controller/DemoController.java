package com.junit.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.junit.dto.DemoDto;
import com.junit.dto.WeatherResponse;
import com.junit.entity.Demo;
import com.junit.service.IDemoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class DemoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	IDemoService demoService;

	@Operation(summary = "GetAll demo items")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = " Fetched demo items", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Demo.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	@GetMapping("/demo")
	public ResponseEntity<?> getAll(Authentication authentication) {
		LOGGER.info(authentication.getName());
		List<Demo> allDemoDlts = demoService.getAll();
		return new ResponseEntity<List<Demo>>(allDemoDlts, HttpStatus.OK);

	}

	@GetMapping("/demo/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		Demo allDemoDlts = demoService.findById(id);
		return new ResponseEntity<Demo>(allDemoDlts, HttpStatus.OK);

	}

	@PostMapping("/demo")
	public ResponseEntity<?> save(@RequestBody Demo demo) {
		String resp = demoService.save(demo);
		if (resp != null) {
			return new ResponseEntity<String>(resp, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getWeather")
	public ResponseEntity<?> getWeather(@RequestParam(name = "city", required = false) String city) {
		WeatherResponse resp = demoService.weatherforecast(city);
		if (resp != null) {
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
		return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/demo/{id}")
	public ResponseEntity<?> updateDetails(@PathVariable(name = "id") Long id, @RequestBody Demo demo) {
		String resp = demoService.updateDemo(id, demo);
		if (resp != null) {
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/demo/{id}")
	public ResponseEntity<?> updateDetailsUsingPatch(@PathVariable(name = "id") Long id,
			@RequestBody Map<String, Object> demo) {

		String resp = demoService.updatePartialDetails(id, demo);

		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@GetMapping("/demo/testRest")
	public ResponseEntity<?> getTestResp(@RequestParam("message") String message) {
		return new ResponseEntity<String>(demoService.test1(message), HttpStatus.OK);
	}

	@PostMapping("/bulkSave")
	public ResponseEntity<?> saveDemo(@RequestBody List<DemoDto> demoList) {
		List<DemoDto> resp = demoService.saveBulkDemo(demoList);
		if (resp != null) {
			return new ResponseEntity<List<DemoDto>>(resp, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/demo/testPost")
	public ResponseEntity<?> getTestForPost(@RequestBody DemoDto message) {
		return new ResponseEntity<String>(demoService.testForPost(message), HttpStatus.OK);
	}

	@PutMapping("/demo/testPut")
	public ResponseEntity<?> getTestForPost(@RequestBody DemoDto message, @RequestParam Long id) {
		return new ResponseEntity<String>(demoService.testForPut(message, id), HttpStatus.OK);
	}
}
