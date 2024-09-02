package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.HomePage;


public class Verifications {

    /**
     * Verify element is displayed
     * @param element
     * @return boolean
     */
    public static boolean verifyElementIsDisplayed(WebElement element){
        boolean isElementVisible = element.isDisplayed();
        if (isElementVisible) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compare and verify displayed user balance is the same as the Api balance
     * @param driver
     * @throws Exception
     */
    public static void verifyBalance(WebDriver driver) throws Exception {

        HomePage homePage = new HomePage(driver);

        String displayedBalance = homePage.getDisplayedBalance();
        String currencyDisplayedBalance = displayedBalance.replaceAll("[^\\p{Sc}]", "");
        String displayedUserBalance = displayedBalance.replaceAll("[^\\d.]", "");

        String apiBalance = homePage.getMemberBalanceFromAPI(driver);
        String currencyApiBalance = apiBalance.replaceAll("[^\\p{Sc}]", "");
        String apiUserBalance = apiBalance.replaceAll("[^\\d.]", "");

        Assert.assertEquals(currencyDisplayedBalance, currencyApiBalance, "Currency mismatch between UI and API");
        Assert.assertEquals(displayedUserBalance, apiUserBalance, "Balance mismatch between UI and API!");
        System.out.println("User balance is correct: " + currencyApiBalance + apiUserBalance);
    }


}
