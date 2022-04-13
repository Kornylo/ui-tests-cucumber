package com.cuce.pages;

import com.cuce.driver.DriverWrapper;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.sql.*;
import java.util.concurrent.TimeUnit;

import static com.cuce.util.PropertiesUtils.*;
import static java.lang.System.currentTimeMillis;

public class GlobalPage {

    private final Logger logger = Logger.getLogger(GlobalPage.class);
    @Getter
    private Actions action;
    @Getter
    private WebDriver driver;
    @Getter
    private DriverWrapper driverWrapper;
    private String textBoolCheck = "xpath:>//*[contains(text(),\"%s\")]";
    private String elementById = "id:>%s";
    private String elementByCss = "css:>%s";
    private String elementByXpath = "xpath:>%s";
    private String elementByText = "xpath:>//body//*[contains(text(),\"%s\")]";
    private String elementLoaderAbsent = "xpath:>//*[@class=\"gbox_loader\"][contains(@style,\"block\")]";
    private String elementZCustomSelectorLoader = "xpath:>//*[@class=\"chosen enabled\"]";
    private String elementCheckoutLoaderAbsent = "css:>div.checkout-iframe-holder>div";
    private String elementFooter = "css:>div.f-other";
    private String elementHeader = "css:>div.header-top";
    private String elementLoaderProductOptions = "css:>div.po-child-products-loading";
    private String userName1 = "system.qa-ro";
    private String password1 = "nfTSBgsisU";
    private String connectionUrl1 = "jdbc:mysql://db2.devzone.net:3306/carid_weekly";
    private String connectionUrlProd1 = "jdbc:mysql://db22.devzone.net:3306/carid";
    private String userNameProd1 = "maksym.go";
    private String passwordProd1 = "dQN5ln6rHB";
    private long productid;
    private String gottenYear;
    private String gottenModel;
    private String gottenMake;
    private String gottenYMM;


    public GlobalPage(WebDriver driver) {
        this.driver = driver;
        action = new Actions(driver);
        driverWrapper = new DriverWrapper(driver);
    }

    /**
     * Follow the direct link
     *
     * @param host = direct link
     */
    public void getUrl(String host) {
        driver.get(host);
    }

    /**
     * Native browser button with page refresh
     */
    public void refreshBrowserPage() {
        driver.navigate().refresh();
    }

    /**
     * Search for text on the current page
     *
     * @param text = search text
     */
    public void searchText(String text) {
        if (driverWrapper.findElementByUntilWait(elementByText, text).isDisplayed()) {
            driverWrapper.findElementByUntilWait(elementByText, text).isDisplayed();
        } else {
            refreshBrowserPage();
            waitElementOnCurrentPage(elementByText);
            logger.info("Caught exception: ");
            driverWrapper.findElementByUntilWait(elementByText, text).isDisplayed();
        }

    }

    /**
     * Entering data by id field
     *
     * @param id         = id field
     * @param fieldValue = value
     */
    public void setDataInFormWithIds(String id, String fieldValue) {
        driverWrapper.findElementByUntilWait(elementById, id).clear();
        driverWrapper.findElementByUntilWait(elementById, id).sendKeys(fieldValue);
    }

    public void setDataInFormByIdsWithReplace(String id, String fieldValue) {
        driverWrapper.findElementByUntilWait(elementById, id).clear();
        driverWrapper.findElementByUntilWait(elementById, id).sendKeys(fieldValue);
        String getEnteredValue = getDriver().findElement(By.id(id)).getAttribute("value").replace(" ", "");
        logger.info("Gotten number of card is: " + getEnteredValue);
        if (!getEnteredValue.equals(fieldValue)) {
            driverWrapper.findElementByUntilWait(elementById, id).clear();
            driverWrapper.findElementByUntilWait(elementById, id).sendKeys(fieldValue);
        }
    }

    /**
     * Parameter selection by text in dropdown by id
     *
     * @param value = visible text
     * @param id    = dropdown id
     */
    public void selectVisibleTextInDropDownListById(String value, String id) {
        new Select(driverWrapper.findElement(elementById, id)).selectByVisibleText(value);
    }

    /**
     * Pressing on an element by id
     *
     * @param idBtn = id
     */
    public void pressOnIdButton(String idBtn) {
        action.moveToElement(driverWrapper.findElementByUntilWait(elementById, idBtn)).perform();
        driverWrapper.findElementByUntilWait(elementById, idBtn).click();
    }

    /**
     * Check Element By Id
     *
     * @param id = Element id
     */
    public void checkElemById(String id) {
        waitElementOnCurrentPage(id);
        driverWrapper.findElementByImplicitWait(elementById, id, 10);
        driverWrapper.findElementByUntilWait(elementById, id).isEnabled();
    }


    /**
     * Switch to Iframe zone
     *
     * @param frameName = iframe name
     */
    public void switchToIframe(String frameName) {
        logger.info("Switch to iFrame: " + frameName);
        try {
            driver.switchTo().frame(frameName);
        } catch (StaleElementReferenceException e) {
            logger.info("Retry...switch to iFrame: " + frameName);
            driver.switchTo().frame(frameName);
        }
    }


    /**
     * Wait for an web element for all types of locator (This is a bad method, if possible it should be eliminated)
     *
     * @param valueElem = web element
     */
    public void waitElementOnCurrentPage(String valueElem) {
        if (driverWrapper.findElementByImplicitWait(elementById, valueElem, 20) != null) {
            driverWrapper.findElementByUntilWait(elementById, valueElem).isDisplayed();
            logger.info("elementById is Displayed");
        } else if (driverWrapper.findElementByImplicitWait(elementByCss, valueElem, 5) != null) {
            driverWrapper.findElementByUntilWait(elementByCss, valueElem).isDisplayed();
            logger.info("elementByCss is Displayed");
        } else if (driverWrapper.findElementByImplicitWait(elementByText, valueElem, 5) != null) {
            driverWrapper.findElementByUntilWait(elementByText, valueElem).isDisplayed();
            logger.info("elementByText is Displayed");
        } else if (driverWrapper.findElementByImplicitWait(elementByXpath, valueElem, 5) != null) {
            driverWrapper.findElementByUntilWait(elementByXpath, valueElem).isDisplayed();
            logger.info("elementByXpath is Displayed");
        } else {
            Assert.fail("Element " + valueElem + " NOT Displayed");
        }
    }


    /**
     * Make a scroll of the screen to searched element
     *
     * @param element = element
     */
    public void autoScrollToWebElement(String element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                driverWrapper.findElement(element));
    }



    /**
     * Scroll up or down
     *
     * @param scroll = direction value
     */
    public void scrollWindow(String scroll) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(" + scroll + ")");
    }

    /**
     * Scroll to the top or bottom of the page
     *
     * @param sectionPage = footer or header value
     */
    public void scrollToSectionPage(String sectionPage) {
        switch (sectionPage.toUpperCase()) {
            case "FOOTER":
                autoScrollToWebElement(elementFooter);
                break;
            case "HEADER":
                autoScrollToWebElement(elementHeader);
                break;
            default:
                logger.info("Invalid entered section!");
        }
    }

    /**
     * Wait while spinner is available
     *
     * @param waitMilliS = milliseconds value
     */
    public void waitSpinnerLoad(int waitMilliS) {
        boolean isAvailable;
        long endTime = currentTimeMillis() + waitMilliS;
        do {
            isAvailable = driverWrapper.findElementByImplicitWait(elementLoaderAbsent, 1) != null;
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info(currentTimeMillis() + "/" + endTime);
            logger.info("Loader Spinner is Available: " + isAvailable);
        }
        while (currentTimeMillis() < endTime && isAvailable);
    }

    public void waitZCustomSelectorLoading(int waitMilliS) {
        boolean isAvailable;
        long endTime = currentTimeMillis() + waitMilliS;
        do {
            isAvailable = driverWrapper.findElementByImplicitWait(elementZCustomSelectorLoader, 1) == null;
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info(currentTimeMillis() + "/" + endTime);
            logger.info("Loader ZCustom is Available: " + isAvailable);
        }
        while (currentTimeMillis() < endTime && isAvailable);
    }


    //TODO - this method needs to be combined with waitSpinnerLoad()
    public void waitSpinnerCheckoutLoad(int waitMilliS) {
        boolean isAvailable;
        long endTime = currentTimeMillis() + waitMilliS;
        do {
            isAvailable = driverWrapper.findElementByImplicitWait(elementCheckoutLoaderAbsent, 1) != null;
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info(currentTimeMillis() + "/" + endTime);
            logger.info("Loader Spinner is Available: " + isAvailable);
        }
        while (currentTimeMillis() < endTime && isAvailable);
    }

    /**
     * Press on xpath element
     *
     * @param xpath = element
     */
    public void pressOnButtonByXpath(String xpath) {
        scrollToSectionPage(elementByXpath);
        this.waitUntilElemDispl(40, xpath);
        WebElement btn = driverWrapper.findElement(elementByXpath, xpath);
        btn.click();
    }


    public void waitSpinnerLoadProductOptions(int waitMilliS) throws InterruptedException {
        boolean isAvailable;
        long endTime = currentTimeMillis() + waitMilliS;
        do {
            isAvailable = driverWrapper.findElementByImplicitWait(elementLoaderProductOptions, 1) != null;
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info(currentTimeMillis() + "/" + endTime);
            logger.info("Loader Spinner is Available: " + isAvailable);
            TimeUnit.MILLISECONDS.sleep(100);
        }
        while (currentTimeMillis() < endTime && isAvailable);
    }



    private Statement connectToDB() throws SQLException {
        if (isProdEnvironment()) {
            Connection conn = DriverManager.getConnection(connectionUrlProd1, userNameProd1, passwordProd1);
            logger.info("Connect to prod DB!");
            return conn.createStatement();
        } else {
            Connection conn = DriverManager.getConnection(connectionUrl1, userName1, password1);
            logger.info("Connect to DEV DB!");
            return conn.createStatement();
        }
    }


    public void waitUntilElemDispl(int sec, String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, sec);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        logger.info("Element ByXpath: " + xpath + " is Displayed");
    }

    public void waitUntilElemPresent(int sec, String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, sec);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        logger.info("Element ByXpath: " + xpath + " is Present");
    }

    public void implicitWait(int wait) {
        logger.info("Waiting " + wait + " Second");
        try {
            TimeUnit.SECONDS.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeIframe() {
        driver.switchTo().defaultContent();
    }

    private String domain;

    public void getYMMFromDBByProductID() throws Throwable {
        Class.forName("com.mysql.jdbc.Driver");
        logger.info("We're connected to DB");
        Statement statement = connectToDB();
        ResultSet resusltSet = statement.executeQuery("SELECT \n" +
                "mk.name as Make, md.name as Model,mm.year as Year,cvs.name\n" +
                "FROM catalog_mmy mm\n" +
                "left join catalog_model md on md.id=mm.model_id\n" +
                "left join catalog_make mk on mk.id=md.make_id\n" +
                "left join catalog_vehicle_to_mmy mmvt on mmvt.mmy_id=mm.id\n" +
                "left join catalog_vehicle cv on mmvt.vehicle_id=cv.id\n" +
                "left join catalog_vehicle cvs on cvs.id=cv.parent_id \n" +
                "left join xcart_product_mmy mf on mf.year_id=mm.id\n" +
                "left join xcart_products xs on xs.productid=mf.product_id\n" +
                "left join mmy_selector_hidden_model mshd on mshd.model_id=mm.model_id\n" +
                "where product_id=" + productid + "\n" +
                "and cvs.name like '%" + domain + "%'\n" +
                "and md.id not in (select model_id from mmy_selector_hidden_model)\n" +
                "limit 1;");
        while (resusltSet.next()) {
            gottenYear = resusltSet.getString("Year");
            gottenMake = resusltSet.getString("Make");
            gottenModel = resusltSet.getString("Model");
        }
        logger.info("YMM from DB for MPN product: " + gottenYear + " " + gottenMake + " " + gottenModel);
        gottenYMM = (gottenYear + " " + gottenMake + " " + gottenModel);
    }

}





