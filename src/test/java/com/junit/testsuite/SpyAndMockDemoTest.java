package com.junit.testsuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(TestResultClass.class)
public class SpyAndMockDemoTest {
	@Mock
	private List<String> mockList;

	@Spy
	private List<String> spyList = new ArrayList();

	@Test
	public void testMockList() {
		mockList.add("test");
		assertNull(mockList.get(0));
	}

	@Test
	public void testSpyList() {
		spyList.add("test");
		assertEquals("test", spyList.get(0));
		assertEquals(spyList.size(),1);
	}

	@Test
	public void testMockWithStub() {
		// try stubbing a method
		String expected = "Mock 100";
		when(mockList.get(100)).thenReturn(expected);

		assertEquals(expected, mockList.get(100));
	}

	@Test
	public void testSpyWithStub() {
		String expected = "Spy 100";
		doReturn(expected).when(spyList).get(100);
		assertEquals(expected, spyList.get(100));
	}

}
