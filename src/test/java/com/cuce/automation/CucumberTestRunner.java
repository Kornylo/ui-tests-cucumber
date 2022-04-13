package com.cuce.automation;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.log4j.BasicConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static com.cuce.common.AllureReportConfig.prepareAllureResultsFolder;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty",
        "html:target/cucumber/cucumber-html-report",
        "junit:target/cucumber/cucumber-junit-report.xml",
        "json:target/cucumber/cucumber.json",
        "io.qameta.allure.cucumberjvm.AllureCucumberJvm"},
        glue = "com/cuce/step_definitions",
        features = "src/test/resources")

public class CucumberTestRunner {
    static {
        BasicConfigurator.configure();
    }
    @BeforeClass
    static public void beforeSuite() {
        prepareAllureResultsFolder();
    }

    @AfterClass
    public static void afterSuite() throws Exception {
    }
}


