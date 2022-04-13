package com.cuce.runs;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.BasicConfigurator;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.cuce.steps", "com.cuce.driver"},
        tags = {"@test"},
        plugin = {"pretty",
                "html:test-outout",
                "html:target/site/cucumber-pretty",
                "json:target/cucumber.json"})
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    static {
        BasicConfigurator.configure();
    }

    @Getter
    @Setter
    private static String hash;

    @Getter
    @Setter
    private static String user;
}