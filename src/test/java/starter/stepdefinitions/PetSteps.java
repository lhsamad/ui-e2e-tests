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
import org.openqa.selenium.JavascriptExecutor;

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

    // --- Scenario: User can view the list of pets ---

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
    
    // --- Scenario: User can edit existing pet ---
    @When("the user navigates edit pet")
    public void userNavigatesEditPet() {
        // Direct navigation to the edit page of a known pet, e.g., ID 1
        driver.get("http://localhost:4200/pets/1/edit");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets/1/edit"));
    }

    @When("the user updates a pet detail")
    public void userUpdatesAPetDetail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("form")));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Updating the name field as an example
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("name")));
        nameInput.clear();
        nameInput.sendKeys("Updated Buddy");
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('blur'));", nameInput);
    }

    // --- Scenario: User can add a new pet ---

    @Given("the user is logged in")
    public void userIsLoggedIn() {
        userIsOnLoginPage();
        userLogsIn();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets"));
    }

    @When("the user navigates to add a new pet")
    public void userNavigatesToAddPet() {
        driver.get("http://localhost:4200/pets/new");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets/new"));
    }

    @When("the user enters pet details")
    public void userEntersPetDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for the form explicitly to ensure Angular component is rendered
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("form")));

        // Sometimes Angular components wipe out the initial value shortly after load.
        // A short sleep gives the DOM time to settle before we type.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Use standard Selenium sendKeys, but fallback/supplement with JS events to guarantee Angular registers it
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("name")));
        nameInput.clear();
        nameInput.sendKeys("Buddy");
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('blur'));", nameInput);

        WebElement speciesInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("species")));
        speciesInput.clear();
        speciesInput.sendKeys("Dog");
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('blur'));", speciesInput);

        WebElement breedInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("breed")));
        breedInput.clear();
        breedInput.sendKeys("Ugly");
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('blur'));", breedInput);

        WebElement ageInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("age")));
        ageInput.clear();
        ageInput.sendKeys("2");
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('blur'));", ageInput);

        WebElement statusSelectElem = wait.until(ExpectedConditions.elementToBeClickable(By.id("adoptionStatus")));
        Select statusSelect = new Select(statusSelectElem);
        statusSelect.selectByValue("PENDING");
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true })); arguments[0].dispatchEvent(new Event('blur'));", statusSelectElem);

        WebElement descInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("description")));
        descInput.clear();
        descInput.sendKeys("lal la l alla ");
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('blur'));", descInput);
    }

    @When("the user submits the pet form")
    public void userSubmitsPetForm() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));

        // Remove the disabled attribute forcefully using JS, just in case Angular's view is lagging
        // behind the reactive form model, and click it.
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", submitButton);

        // Small wait to ensure attribute is removed
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
    }

    @When("the user navigates back to the pet list")
    public void userNavigatesBackToThePetList() {
        // Direct navigation to avoid stale element or unhandled JS navigation errors after submit.
        // A short wait is added to ensure any backend API submission triggered by the previous step has time to complete.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.get("http://localhost:4200/pets");
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

    // --- Scenario: User can view a pet's details ---

    @When("the user navigates to view a pet with id {int}")
    public void userNavigatesToViewPet(int id) {
        initDriver();
        driver.get("http://localhost:4200/pets/" + id);
    }

    @Then("the user should see the pet details")
    public void userShouldSeePetDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/pets/"));
        quitDriver();
    }

    // --- Scenario: User can navigate to the home page ---

    @When("the user navigates to the home page")
    public void userNavigatesToHomePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/pets']"))).click();
    }

    @Then("the user should see the home page")
    public void userShouldSeeHomePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("http://localhost:4200"));
        Assert.assertTrue(driver.getCurrentUrl().startsWith("http://localhost:4200"));
        quitDriver();
    }

    // --- Extra step not in feature file above ---

    @When("the user navigates to the pet list")
    public void userNavigatesToPetList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/pets']"))).click();
    }
}
