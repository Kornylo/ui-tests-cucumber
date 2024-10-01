package com.cuce.steps;

import com.cuce.driver.DriverWrapper;
import com.cuce.driver.SharedDriver;
import com.cuce.pages.*;
import com.cuce.runs.CucumberTestRunner;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.cuce.util.PropertiesUtils.*;


public class GlobalSteps {

    private final Logger logger = Logger.getLogger(GlobalSteps.class);
    @Getter
    private GlobalPage globalPage;
    @Getter
    private WebDriver driver;
    @Getter
    private String emailXpath = "//input[@type='email']";



    public GlobalSteps(SharedDriver driver) {
        globalPage = new GlobalPage(driver);
        this.driver = driver;
    }

    /**
     * The method accepts direct links or fragments thereof.
     * If only the route is specified, the method will determine the domain by the environment.
     *
     * @param page = direct URL or URI (e.g. /my-account/login)
     */
    @When("^Go to '(.*)' page.*$")
    public void goToPage(String page) {
        String host = getTargetTestHost();
        boolean isUrl = Pattern.compile("(http|www)").matcher(page).find();
        if (isUrl) {
            if (!page.startsWith("http")) {
                page = "http://" + page;
            }
            logger.info("Go to " + page);
            globalPage.getUrl(page);
        } else {
            logger.info("Go to " + host + page);
            globalPage.getUrl(host + page);
        }
    }

    @And("^Wait (\\d+) Second$")
    public void implicitWait(int wait) {
        logger.info("Waiting " + wait + " Second");
        try {
            TimeUnit.SECONDS.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @And("^Refresh Current Page$")
    public void refreshCurrentPage() {
        globalPage.refreshBrowserPage();
    }

    @And("^Switch to '(.*)' iFrame$")
    public void switchToIframe(String frame) {
        waitForPageComplete();
        globalPage.waitSpinnerCheckoutLoad(15000);
        globalPage.switchToIframe(frame);
    }

    @And("^Press on '(.*)' Button By ID$")
    public void pressOnButtonById(String buttonId) {
        globalPage.waitSpinnerLoad(3000);
        globalPage.checkElemById(buttonId);
        globalPage.pressOnIdButton(buttonId);
    }


    @When("^Enter '(.*)' text by id '(.*)' Field$")
    public void enterTextById(String txt, String id) {
        globalPage.waitSpinnerLoad(100);
        globalPage.waitElementOnCurrentPage(id);
        globalPage.setDataInFormWithIds(id, txt);
        globalPage.waitSpinnerLoad(10);
    }

    @When("^Enter '(.*)' text in field form by id '(.*)' with replace space$")
    public void enterTextInCrCdFormById(String txt, String id) {
        globalPage.waitSpinnerLoad(100);
        globalPage.waitElementOnCurrentPage(id);
        globalPage.setDataInFormByIdsWithReplace(id, txt);
    }


    @When("^Fill out Form on Current page:$")
    public void fillOutForm(DataTable dataTable) {
        Map<String, String> map = dataTable.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            globalPage.setDataInFormWithIds(entry.getKey(), entry.getValue());
        }
    }

    @And("^Select '(.*)' by Visible Text in '(.*)' Drop Down List$")
    public void selectIdByVisibleTextInDropDownList(String value, String fieldId) {
        globalPage.waitSpinnerLoad(100);
        globalPage.checkElemById(fieldId);
        globalPage.selectVisibleTextInDropDownListById(value, fieldId);
        globalPage.waitZCustomSelectorLoading(3000);
    }

    @Then("^Check '(.*)' Text on Current page$")
    public void checkTextOnCurrentPage(String text) {
        globalPage.searchText(text);
        logger.info(text + ": OK");
    }

    @And("^Press on '(.*)' Button By Xpath.*$")
    public void pressOnButtonByXpath(String xpath) throws InterruptedException {
        globalPage.pressOnButtonByXpath(xpath);
        this.implicitWait(1);
        globalPage.waitSpinnerLoadProductOptions(10000);
    }

    public void waitForPageComplete() {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(webDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"));
    }

    @Then("^ICBC")
    public void getКandomProductFromList() throws InterruptedException {
        driver.get("https://onlinebusiness.icbc.com/webdeas-ui/login;type=driver");
        driver.findElement(By.xpath("//input[@formcontrolname='drvrLastName']")).sendKeys("Levchuk");
        driver.findElement(By.xpath("//input[@formcontrolname='licenceNumber']")).sendKeys("09657275");
        driver.findElement(By.xpath("//input[@formcontrolname='keyword']")).sendKeys("Shushunova");
        driver.findElement(By.xpath("//label[@class='mat-checkbox-layout']//span")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        System.out.println("Page title is: " + driver.getTitle());
        Thread.sleep(5000);

        WebElement textField = driver.findElement(By.xpath("//input[@data-placeholder='Start typing...']"));

        // The text you want to type
        String text = "port ";
        // Enter text one letter at a time
        for (char ch : text.toCharArray()) {
            textField.sendKeys(Character.toString(ch));
            // Optional: Add a delay between keystrokes to simulate natural typing
            try {
                Thread.sleep(100); // 100 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(3000);
        driver.findElement(By.xpath("//span[text()[normalize-space()='Port Coquitlam, BC']]")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//span[text()[normalize-space()='Search']]")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//div[text()[normalize-space()='Port Coquitlam BC V3C 0A4']]")).click();
        Thread.sleep(5000);
        List<WebElement> element = driver.findElements(By.xpath("//span[text()='No appointments available']/following-sibling::span"));
        if (element.size() > 0 && element.get(0).isDisplayed()) {
            sendToTelegramPhotos("7660493471:AAGUsSIUVjjYD3KS0-Z65EkAS2HvgXz1gPg", "449324889", "no slot");
        } else {
            String slot = driver.findElement(By.xpath("//div[@class='dialog container']")).getText();
            sendToTelegramPhotos("7660493471:AAGUsSIUVjjYD3KS0-Z65EkAS2HvgXz1gPg", "449324889", slot);
        }
    }

    public static void sendToTelegramPhotos(String apiToken, String chatId, String text) {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        urlString = String.format(urlString, apiToken, chatId, text);
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

    }



