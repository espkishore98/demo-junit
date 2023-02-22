package com.junit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.junit.entity.Demo;
import com.junit.testsuite.TestResultClass;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ExtendWith(TestResultClass.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class DemoRepoTests {

	@Autowired
	DemoRepository demoRepository;

	@BeforeEach
	void initUseCase() {
		List<Demo> demos = Arrays.asList(new Demo(1L, "sai"));
		demoRepository.saveAll(demos);
	}

	@AfterEach
	public void destroyAll() {
		demoRepository.deleteAll();
	}

	@Test
	@Order(3)
	public void should_find_all_items() {

		List<Demo> items = demoRepository.findAll();
		assertThat(items.size()).isGreaterThanOrEqualTo(1);

	}

	@Test
	@Order(2)
	public void should_find_by_name() {

		Demo items = demoRepository.findByName("sai");
		assertThat(items.getName()).isEqualTo("sai");

	}

	@Test
//	@Rollback(false)
	@Order(1)
	public void createDemo() throws InterruptedException {
		Demo demo = new Demo(null, "sai");
		Demo resp = demoRepository.save(demo);
		Iterable<Demo> demos = demoRepository.findAll();

		Assertions.assertThat(demos).extracting(Demo::getName).contains("sai");
		assertThat(resp.getId()).isGreaterThan(0);

//    employeeRepository.deleteAll();
//		Assertions.assertThat(employeeRepository.findAll()).isEmpty();
	}

}
