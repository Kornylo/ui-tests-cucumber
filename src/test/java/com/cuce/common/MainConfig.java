package com.cuce.common;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({ "file:src/test/resources/MainConfig.properties" })
public interface MainConfig extends Config {


    String allure_results_folder();

    String allure_screenshots_folder();


}
