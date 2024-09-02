package tests;

import utils.Verifications;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.HomePage;

import java.time.Duration;


public class BalanceCheck {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeTest
    public void setUp() {

        // Setup WebDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

    }

    @Test(description = "Assure displayed user balance and api balance are equal")
    public void verifyUserBalance() throws Exception {

        homePage = new HomePage(driver);
        String username = "tu_tsvetina";
        String password = "Pass112#";


        // 1. Load Webpage
        driver.get("https://luckybandit.club.test-delasport.com/en/sports");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);

        // 2. Login user
        homePage.login(username, password);

        //3. Verify user's balance
        Verifications.verifyBalance(driver);

    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
