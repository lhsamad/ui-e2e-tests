# UI E2E Tests

This project contains End-to-End (E2E) UI tests for the Spring Boot Pets application using a modern testing stack in Java.

## Libraries and Components Used

*   **Java 17**: The core programming language.
*   **Selenium WebDriver**: Used for browser automation and interacting with the DOM.
*   **Cucumber**: A Behavior-Driven Development (BDD) framework for writing tests in plain text (Gherkin syntax).
*   **Serenity BDD**: An open-source library that wraps Selenium and Cucumber to provide rich reporting and living documentation.
*   **JUnit 4**: The underlying test runner used by Serenity and Cucumber.
*   **Maven**: Dependency management and build tool.

## How to Run the Tests

To execute the test suite, ensure you have Maven installed and run the following command from the root of the project:

```bash
mvn clean verify
```

Serenity will execute the Cucumber tests and generate a comprehensive HTML report in the `target/site/serenity` directory. You can open `target/site/serenity/index.html` in your browser to view the results.

## Test Execution Order and Lifecycle

The order of test execution is strictly determined by the `.feature` files (e.g., `src/test/resources/features/pets.feature`). 

1.  **Top-to-Bottom Execution**: Cucumber parses the feature file and executes scenarios from top to bottom.
2.  **Sequential Steps**: Inside each `Scenario`, the steps (`Given`, `When`, `Then`, `And`) are executed sequentially exactly as written.
3.  **State Management**: In this setup, the browser (`WebDriver`) is initialized when needed (e.g., visiting a page) and explicitly quit at the end of each `Then` step. This ensures each Scenario runs in a clean, isolated browser session, preventing state leakage between tests.

## Why Cucumber, Serenity, and Selenium?

### Why not just Selenium?
While Selenium is excellent for driving a browser, it is purely an automation tool. If you write tests with just Selenium and JUnit, your tests are code-heavy, hard for non-developers to read, and the reporting is usually basic (pass/fail traces).

### Why Cucumber?
Cucumber introduces **Behavior-Driven Development (BDD)**. It allows you to write test scenarios in plain English (Gherkin syntax: Given/When/Then) that act as both specification and automated tests.
*   **Collaboration**: Business analysts, PMs, and QAs can read and contribute to the test scenarios without knowing Java.
*   **Living Documentation**: The feature files serve as up-to-date documentation of how the system is supposed to behave.

### Why Serenity BDD?
Serenity BDD acts as an orchestrator that brings Cucumber and Selenium together, adding significant value:
*   **Rich Reporting**: Serenity generates beautiful, detailed reports containing step-by-step execution details, screenshots for UI tests, and aggregated test results.
*   **Screenplay Pattern**: Serenity strongly supports the Screenplay Pattern, leading to more maintainable and reusable test code compared to traditional Page Objects.
*   **Built-in WebDriver Management**: Serenity can automatically manage the WebDriver lifecycle, reducing boilerplate code for driver setup and teardown.

---

## Alternative Approach: Java and Playwright

As modern web applications evolve, **Playwright** has emerged as a powerful alternative to Selenium WebDriver. Playwright is developed by Microsoft and offers several advantages for modern web testing.

### Playwright vs Selenium Setup

Instead of Selenium, you can integrate Playwright with Cucumber and JUnit.

**Maven Dependencies:**
```xml
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.41.0</version>
</dependency>
```

**Step Definition Example (Playwright + Java):**
```java
import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertTrue;

public class PlaywrightPetSteps {
    private Playwright playwright;
    private Browser browser;
    private Page page;

    @Before
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @After
    public void teardown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Given("the user is on the login page")
    public void userIsOnLoginPage() {
        page.navigate("http://localhost:4200/login");
    }

    @When("the user logs in with valid credentials")
    public void userLogsIn() {
        page.fill("#username", "luqman");
        page.fill("#password", "password");
        page.click("button[type='submit']");
    }

    @Then("the user should see the pet list")
    public void userShouldSeePetList() {
        page.waitForURL("**/pets");
        assertTrue(page.url().contains("/pets"));
    }
}
```

### Why Choose Playwright over Selenium?
1.  **Auto-waiting**: Playwright automatically waits for elements to be actionable (visible, enabled, etc.) before performing actions, drastically reducing the need for explicit waits (`WebDriverWait`) and reducing test flakiness.
2.  **Speed**: Playwright communicates directly with the browser over the DevTools Protocol, which is generally faster than Selenium's HTTP-based WebDriver protocol.
3.  **Network Interception**: Playwright allows you to easily mock or modify network requests and responses, which is great for UI testing without relying on the actual backend.
4.  **Multi-tab/Multi-user**: Playwright's Browser Contexts make it extremely easy to simulate multiple users in the same test without launching multiple browser instances.
