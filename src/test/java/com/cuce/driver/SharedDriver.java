package com.cuce.driver;

import com.cuce.runs.CucumberTestRunner;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;
import static java.lang.System.*;
import static org.testng.Assert.assertEquals;

public class SharedDriver extends EventFiringWebDriver {
    public static final long TIMEOUT_VALUE = 20;//30
    private static WebDriver DRIVER;
    private WebDriver driver;
    String imagesUrl;
    static String imegesCURL;
    static List<String> list = new ArrayList<String>();
    static String imegesCURLTransfer;
    @Getter
    private DriverWrapper driverWrapper;

    static {
        try {
            DRIVER = DriverFactory.create((getProperty("browser")).toUpperCase());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            DRIVER.quit();
        }
    };

    static String allureReportResultsFolder = "allure-results";

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    private Logger logger = Logger.getLogger(SharedDriver.class);

    public SharedDriver() {
        super(DRIVER);
    }

    @Override
    public void close() {
        if (Thread.currentThread() != CLOSE_THREAD) {
            throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
        }
        super.close();
    }

    @Before
    public void deleteAllCookies(Scenario scenario) throws Exception {
        System.out.println(System.getenv("JOB_URL") + System.getenv("BUILD_NUMBER") + "/allure/");
        logger.info("\n----------------------------------------------------------------------------------");
        logger.info("Scenario: '" + scenario.getName() + "'");
        int cookiesCount = 1;
        long endTime = currentTimeMillis() + 10000;
        while (currentTimeMillis() < endTime && !(cookiesCount == 0)) {
            manage().deleteAllCookies();
            Set<Cookie> cookies = manage().getCookies();
            cookiesCount = cookies.size();
            logger.info("Time left: " + (currentTimeMillis() < endTime));
            logger.info("Cookies: " + !(cookiesCount == 0));
        }
        get("about:blank");

        /*  MyScreenRecorder.startRecording(scenario.getName());*/
    }


    public static void sendToTelegramPhotos(String apiToken, String chatId, String photo, String caption) {
        String urlString = "https://api.telegram.org/bot%s/sendPhoto?chat_id=%s&photo=%s&caption=%s";
        urlString = String.format(urlString, apiToken, chatId, photo, caption);
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            StringBuilder sb = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            String response = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @After
    public void tearDown(Scenario scenario) throws Exception {
        String scenarioName = scenario.getName();
        String scenarioStatus = scenario.getStatus();
        logger.info("Scenario: '" + scenario.getName() + "' is: " + scenario.getStatus());
        logger.info("\n----------------------------------------------------------------------------------");
        closeExtraTabs();
    }

    private void makeScreenshot(Scenario scenario) {
        try {
            scenario.write("Current URL is " + DRIVER.getCurrentUrl());
            byte[] screenshot = ((TakesScreenshot) DRIVER).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        } catch (WebDriverException somePlatformsDontSupportScreenshots) {
            err.println(somePlatformsDontSupportScreenshots.getMessage());
        }
    }


    public static String uploadErrorImagesToTransfer() throws IOException, JSONException {
        String url = "https://transfer.sh/";
        //  String[] command = ("curl -i -F filedata=@"+System.getProperty("user.dir")+"javanullpointer.png" ,url)
        String shellcmd = "curl -i -F filedata=@" + System.getProperty("user.dir") + "/javanullpointer.png https://transfer.sh/";
        System.out.println(shellcmd);
        Process process = Runtime.getRuntime().exec(shellcmd);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
            out.println(line);
        }
        imegesCURLTransfer = list.get(18);
        return url;
    }


    private void makeScreenshotForTelegrambot() throws IOException {
        TakesScreenshot scrShot = ((TakesScreenshot) DRIVER);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(System.getProperty("user.dir") + "/javanullpointer.png");
        FileUtils.copyFile(SrcFile, DestFile);

    }


    private void closeExtraTabs() {
        List<String> tabs = new ArrayList<>(DRIVER.getWindowHandles());

        if (tabs.size() > 1) {
            for (int i = 1; i < tabs.size(); i++) {
                DRIVER.switchTo().window(tabs.get(i));
                DRIVER.close();
            }
            DRIVER.switchTo().window(tabs.get(0));
            DRIVER.switchTo().defaultContent();
        }
    }


}