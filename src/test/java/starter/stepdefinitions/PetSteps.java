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
        driver.findElement(By.id("username")).sendKeys("luqman");
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
        initDriver();
        driver.get("http://localhost:4200/pets/new");
    }

    @When("the user navigates to view a pet with id {int}")
    public void userNavigatesToViewPet(int id) {
        initDriver();
        driver.get("http://localhost:4200/pets/" + id);
    }

    @When("the user navigates to the pet list")
    public void userNavigatesToPetList() {
        initDriver();
        driver.get("http://localhost:4200/pets");
    }

    @When("the user navigates to the home page")
    public void userNavigatesToHomePage() {
        initDriver();
        driver.get("http://localhost:4200");
    }

    @When("the user enters pet details")
    public void userEntersPetDetails() {
        driver.findElement(By.id("name")).sendKeys("Jonny Cash");
        driver.findElement(By.id("species")).sendKeys("Cat");
        driver.findElement(By.id("breed")).sendKeys("Peterbald");
        driver.findElement(By.id("age")).sendKeys("3");
        // Often 'select' elements or material elements require different interaction
        driver.findElement(By.id("adoptionStatus")).sendKeys("Available");
        driver.findElement(By.id("description")).sendKeys("A cute cat");
    }

    @When("the user submits the pet form")
    public void userSubmitsPetForm() {
        // Normally forms use type="submit" so we can assume finding a button of type submit or id="submit" works.
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("the newly added pet should appear in the list")
    public void newPetShouldAppearInList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets"));
        
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h2")));
        Assert.assertEquals("Jonny Cash", heading.getText());
        
        quitDriver();
    }

    @Then("the user should see the pet details")
    public void userShouldSeePetDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/pets/"));
        Assert.assertTrue(driver.getCurrentUrl().matches(".*localhost:4200/pets/\\d+.*"));
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
