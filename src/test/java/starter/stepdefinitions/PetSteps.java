package starter.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class PetSteps {

    private WebDriver driver;

    private void initDriver() {
        if (driver == null) {
            driver = new ChromeDriver();
        }
    }

    private void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Given("the user is on the login page")
    public void userIsOnLoginPage() {
        initDriver();
        driver.get("http://localhost:4200/login");
    }

    @When("the user logs in with valid credentials")
    public void userLogsIn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("luqman");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("the user should see the pet list")
    public void userShouldSeePetList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/pets"));
        quitDriver();
    }

    @Given("the user is logged in")
    public void userIsLoggedIn() {
        userIsOnLoginPage();
        userLogsIn();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets"));
    }

    @When("the user navigates to add a new pet")
    public void userNavigatesToAddPet() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Use Javascript execution directly to ensure the click happens on the correct element
        // There may be multiple links (like navbar and main content)
        WebElement addPetLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[routerlink='/pets/new'], a.btn-primary[href='/pets/new']")));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addPetLink);
        
        wait.until(ExpectedConditions.urlContains("/pets/new"));
    }

    @When("the user navigates to view a pet with id {int}")
    public void userNavigatesToViewPet(int id) {
        initDriver();
        driver.get("http://localhost:4200/pets/" + id);
    }

    @When("the user navigates to the pet list")
    public void userNavigatesToPetList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/pets']"))).click();
    }

    @When("the user navigates to the home page")
    public void userNavigatesToHomePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/pets']"))).click();
    }

    @When("the user enters pet details")
    public void userEntersPetDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Wait for the form name input to appear, confirming the page rendered
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        
        // Fill out form with the exact payload from the cURL request
        nameInput.sendKeys("Buddy");
        driver.findElement(By.id("species")).sendKeys("Dog");
        driver.findElement(By.id("breed")).sendKeys("Ugly");
        driver.findElement(By.id("age")).sendKeys("2");
        
        Select statusSelect = new Select(driver.findElement(By.id("adoptionStatus")));
        statusSelect.selectByValue("PENDING"); 
        
        driver.findElement(By.id("description")).sendKeys("lal la l alla ");
    }

    @When("the user submits the pet form")
    public void userSubmitsPetForm() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
        
        // Wait until angular removes the disabled attribute
        wait.until(d -> submitButton.isEnabled());
        
        // Click the button
        submitButton.click();
    }

    @When("the user navigates back to the pet list")
    public void userNavigatesBackToThePetList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // After submitting the form, it makes an API call. 
        // Then it routes to the pet detail page (/pets/{id}).
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("http://localhost:4200/pets/new")));
        
        WebElement backLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Back to List")));
        backLink.click();
    }

    @Then("the newly added pet should appear in the list")
    public void newPetShouldAppearInList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets"));
        // Make sure it's the exact pets listing page and not a details page
        wait.until(ExpectedConditions.urlMatches(".*localhost:4200/pets/?$"));
        
        // Verify the new pet is visible on the page
        wait.until(driver -> driver.getPageSource().contains("Buddy"));
        quitDriver();
    }

    @Then("the user should see the pet details")
    public void userShouldSeePetDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/pets/"));
        quitDriver();
    }

    @Then("the user should see the home page")
    public void userShouldSeeHomePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("http://localhost:4200"));
        Assert.assertTrue(driver.getCurrentUrl().startsWith("http://localhost:4200"));
        quitDriver();
    }
}
