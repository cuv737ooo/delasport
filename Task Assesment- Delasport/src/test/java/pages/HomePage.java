package pages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v106.network.Network;
import org.openqa.selenium.devtools.v106.network.model.RequestId;
import org.openqa.selenium.devtools.v106.network.model.Response;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class HomePage {

    public final WebDriver driver;

    //WebElements
    @FindBy(css = "[class='btn-lg btn cl-login-button d-flex align-items-center justify-content-center']")
    private WebElement loginButton;

    @FindBy(xpath = "//ul[@class='user-balance-container d-flex']/*[1]/span[@class='user-balance-item-amount']")
    private WebElement userBalance;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Login user from login button in header
     * @param username
     * @param password
     * @throws Exception
     */
    final public void login(String username, String password) throws Exception {
        Modals modals = new Modals(driver);
        loginButton.click();
        modals.login(username, password);
        modals.closeModalIfDisplayed();
    }

    public String getDisplayedBalance() {
        return userBalance.getText();
    }

    /**
     * Get balance from the response of getMemberBalance request
     * @param driver
     * @return
     * @throws Exception
     */
    public String getMemberBalanceFromAPI(WebDriver driver) throws Exception {

        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);

        // Enable the network domain to capture network traffic
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Atomic reference to store the response body
        AtomicReference<String> xhrResponse = new AtomicReference<>();

        // Add a listener to capture responses
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();
            if (response.getUrl().contains("getMemberBalance")) {
                RequestId requestId = responseReceived.getRequestId();
                // Get the response body for the specific request
                String responseBody = devTools.send(Network.getResponseBody(requestId)).getBody();
                xhrResponse.set(responseBody);
            }
        });

        driver.navigate().refresh();

        // Wait for some time to allow the XHR request to be made and captured
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = "";
        if (xhrResponse.get() != null) {
            jsonResponse = xhrResponse.get();
        } else {
            System.out.println("No XHR response captured.");
        }

        // Parse JSON response
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");

        // Navigate to the balance key
        JsonObject balanceInfo = dataObject.getAsJsonObject("1").getAsJsonObject("info");
        String balanceAmount = balanceInfo.get("amount").getAsString();

        return balanceAmount;
    }

}
