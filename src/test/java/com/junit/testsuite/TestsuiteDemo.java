package com.junit.testsuite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
//@SelectPackages({ "com.junit.repo", "com.junit.service" })
@SelectClasses({ com.junit.service.DemoServiceTests.class, com.junit.repository.DemoRepoTests.class })
//@ExcludeClassNamePatterns({ "^.*Controller?$" })
//@ExcludePackages("com.junit.controller")
public class TestsuiteDemo {

}
