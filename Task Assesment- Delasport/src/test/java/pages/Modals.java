package pages;

import utils.Verifications;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.testng.Assert.*;

public class Modals {
    private final WebDriver driver;

    //WebElements
    @FindBy(css = "[class='login-title']")
    private WebElement loginModal;

    @FindBy(css = "[id='login_form[username]']")
    private WebElement loginModalUsernameInput;

    @FindBy(css = "[id='login-modal-password-input']")
    private WebElement loginModalPasswordInput;

    @FindBy(css = "div[class='modal-action-bar'] > [class='btn btn-primary btn-block modal-submit-button']")
    private WebElement loginModalSubmitButton;

    @FindBy(css = "[class='btn-dropdown-icon ds-icon-material']")
    private WebElement loggedInIcon;

    @FindBy(css = "[id='sportsbookModal']")
    private WebElement sportsbookModal;

    @FindBy(css = "[id='sportsbookModal'] > * [class='close']")
    private WebElement sportsbookModalCloseBtn;

    public Modals(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Login user from the login modal
     * @param username
     * @param password
     * @throws Exception
     */
    public void login(String username, String password) throws Exception {
        try {
            if (Verifications.verifyElementIsDisplayed(loginModal)) {
                enterUserCredentianls(username, password);
                submitLogin();
                assertSuccessfulLogin();
            } else {
                throw new NoSuchElementException("Login modal is not displayed");
            }
        } catch (NoSuchFieldException e){
            throw new NoSuchFieldException("Username or Password fields are not displayed");
        }
    }

    private void assertSuccessfulLogin() {
        assertTrue(Verifications.verifyElementIsDisplayed(loggedInIcon));
        System.out.println("User is successfully logged in");
    }

    private void submitLogin() {
        loginModalSubmitButton.click();
    }

    private void enterUserCredentianls(String username, String password) throws Exception {
        utils.InputFields.populateText(username, loginModalUsernameInput);
        utils.InputFields.populateText(password, loginModalPasswordInput);
    }

    public void closeModalIfDisplayed(){
        if(Verifications.verifyElementIsDisplayed(sportsbookModal)){
            sportsbookModalCloseBtn.click();
        }
    }
}
