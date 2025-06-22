package com.serenitydojo.playwright.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.*;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(
        key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME,
        value = "true"
)
@ConfigurationParameter(
        key = "allure.results.directory",  // This is important!
        value = "target/allure-results"
)
public class CucumberTestSuiteTests {
}
