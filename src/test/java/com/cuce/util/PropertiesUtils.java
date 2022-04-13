package com.cuce.util;

import com.google.common.base.Strings;

public class PropertiesUtils {

    public static String getTargetTestHost() {

        String hostProperty = System.getProperty("host");
        if (Strings.isNullOrEmpty(hostProperty)) {
            System.out.println("Testing host is not provided. Please set variable 'host'.");
        }
        return hostProperty;
    }

    public static boolean isProdEnvironment() {
        return getTargetTestHost().contains("carid.com");
    }

    public static boolean isDevEnvironment() {
        return getTargetTestHost().contains("devzone.net");
    }

    public static boolean isStgEnvironment() {
        return getTargetTestHost().contains("carid2.com");
    }

    public static String getSelenoidUrl() {
        return System.getProperty("selenoid");
    }

    public static String getBrowserVersion() {
        String version = System.getProperty("version");
        return version.matches("^\\d\\d\\.0$") ? version : "";
    }
}
