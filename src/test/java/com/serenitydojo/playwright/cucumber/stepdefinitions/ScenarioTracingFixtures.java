package com.serenitydojo.playwright.cucumber.stepdefinitions;

import com.microsoft.playwright.Tracing;
import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.nio.file.Paths;

public class ScenarioTracingFixtures {

    @Before
    public void setUpTracing(){
        PlaywrightCucumberFixtures.getContext().tracing().start(
                new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true)
        );
    }

    @After
    public void recordTraces(Scenario scenario) {
        String traceName = scenario.getName().replace(" ", "-").toLowerCase();

        PlaywrightCucumberFixtures.getContext().tracing().stop(
                new Tracing.StopOptions().setPath(
                        Paths.get("target/traces/trace-" + traceName + ".zip")
                )
        );
    }
}
