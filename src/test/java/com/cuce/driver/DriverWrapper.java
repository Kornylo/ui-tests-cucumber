package com.cuce.driver;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.cuce.driver.DriverFactory.setImplicitlyWait;
import static com.cuce.driver.SharedDriver.TIMEOUT_VALUE;

public class DriverWrapper {

    private final Logger logger = Logger.getLogger(DriverWrapper.class);
    private WebDriver driver;
    private WebDriverWait driverWait;

    public DriverWrapper(WebDriver driver) {
        this.driver = driver;
        driverWait = new WebDriverWait(driver, TIMEOUT_VALUE);
    }

    public WebElement findElement(String locator) {
        String[] locatorData = locator.split(":>");
        String locatorType = locatorData[0];
        String locatorValue = locatorData[1];
        By by = null;
        switch (locatorType) {
            case "xpath":
                by = By.xpath(locatorValue);
                break;
            case "css":
                by = By.cssSelector(locatorValue);
                break;
            case "id":
                by = By.id(locatorValue);
                break;
            case "name":
                by = By.name(locatorValue);
                break;
            case "link":
                by = By.linkText(locatorValue);
                break;
            default:
                logger.info("Something went wrong: there is no such " + locatorType + " method!");
        }
        logger.info("Use locator: " + locatorValue);
        return driver.findElement(by);
    }

    public List<WebElement> findElements(String locator) {
        String[] locatorData = locator.split(":>");
        String locatorType = locatorData[0];
        String locatorValue = locatorData[1];
        By by = null;
        switch (locatorType) {
            case "xpath":
                by = By.xpath(locatorValue);
                break;
            case "css":
                by = By.cssSelector(locatorValue);
                break;
            case "id":
                by = By.id(locatorValue);
                break;
            case "name":
                by = By.name(locatorValue);
                break;
            case "link":
                by = By.linkText(locatorValue);
                break;
            default:
                logger.info("Something went wrong: there is no such " + locatorType + " method!");
        }
        logger.info("Use locator: " + locatorValue);
        return driver.findElements(by);
    }

    public List<WebElement> findElements(String locator, String value) {
        return findElements(String.format(locator, value));
    }

    public WebElement findElement(String locator, String value) {
        return findElement(String.format(locator, value));
    }

    private WebElement findElement(String locator, String valueOne, String valueTwo) {
        return findElement(String.format(locator, valueOne, valueTwo));
    }

    public WebElement findElementByUntilWait(String locator) {
        return driverWait.until(ExpectedConditions.visibilityOf(findElement(locator)));
    }

    public WebElement findElementByUntilWait(String locator, String value) {
        return driverWait.until(ExpectedConditions.visibilityOf(findElement(locator, value)));
    }

    public WebElement findElementByUntilWait(String locator, String value, String valueTwo) {
        return driverWait.until(ExpectedConditions.visibilityOf(findElement(locator, value, valueTwo)));
    }

    public WebElement findElementByImplicitWait(String locator, long waitSec) {
        setImplicitlyWait(driver, waitSec);
        WebElement webElement;
        try {
            webElement = findElement(locator);
        } catch (NoSuchElementException e) {
            setImplicitlyWait(driver, TIMEOUT_VALUE);
            return null;
        }
        setImplicitlyWait(driver, TIMEOUT_VALUE);
        return webElement;
    }

    public WebElement findElementByImplicitWait(String locator, String value, long waitSec) {
        setImplicitlyWait(driver, waitSec);
        WebElement webElement;
        try {
            webElement = findElement(locator, value);
        } catch (NoSuchElementException e) {
            setImplicitlyWait(driver, TIMEOUT_VALUE);
            return null;
        }
        setImplicitlyWait(driver, TIMEOUT_VALUE);
        return webElement;
    }
}