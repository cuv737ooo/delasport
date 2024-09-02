package utils;

import org.openqa.selenium.WebElement;

public class InputFields {

    /**
     * Populate string to an input field
     */
    public static void populateText(String input, WebElement inputField) throws Exception {
        try{
            inputField.sendKeys(input);

        }catch (Exception e){
            throw new Exception("Input could not be populated: " + e.getStackTrace());
        }

    }
}
