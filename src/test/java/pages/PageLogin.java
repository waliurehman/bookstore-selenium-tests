package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageLogin {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Locators
    private By loginButton = By.xpath("//button[contains(text(), 'Login')]");
    private By logoutButton = By.xpath("//button[contains(text(), 'Logout')]");
    private By signUpButton = By.xpath("//button[normalize-space()='Sign Up']");
    private By emailField = By.xpath("//input[@placeholder='email']");
    private By usernameField = By.xpath("//input[@placeholder='username']");
    private By passwordField = By.xpath("//input[@placeholder='password']");
    private By confirmPasswordField = By.xpath("//input[@placeholder='confirm password']");
    private By loginSubmitButton = By.xpath("//input[@value='Log In']");
    private By signUpSubmitButton = By.xpath("//input[@value='Sign Up']");
    private By backToUserLoginLink = By.xpath("//button[contains(text(), 'Here')]");
    private By adminLoginLink = By.xpath("//button[contains(text(), 'Here')]");
    private By adminToggleHere = By.xpath("//button[normalize-space()='Here']");
    private By adminSecretKeyField = By.xpath("//input[@placeholder='Secret-key']");
    private By booksLink = By.xpath("//a[contains(text(), 'Books')]");
    private By contactLink = By.xpath("//a[contains(text(), 'Contact')]");
    private By aboutLink = By.xpath("//a[contains(text(), 'About')]");
    private By homeLink = By.xpath("//a[contains(text(), 'Home')]");
    private By darkModeToggle = By.cssSelector("svg.swap-on, svg.swap-off");
    private By freeBookFilter = By.xpath("//div[contains(@class, 'filter-dropdown')]//li[contains(normalize-space(.), 'Free')]");
    private By paidBookFilter = By.xpath("//div[contains(@class, 'filter-dropdown')]//li[contains(normalize-space(.), 'Paid')]");
    private By filterIcon = By.cssSelector(".filter-icon");
    private By filterDropdown = By.cssSelector(".filter-dropdown");
    private By bookCard = By.cssSelector(".grid > div");
    private By addBookButton = By.xpath("//button[contains(text(), 'Add Book')]");
    private By bookTitleInput = By.id("title");
    private By bookDescriptionInput = By.id("description");
    private By bookCategorySelect = By.xpath("//select[contains(@class, 'select')]");
    private By bookPriceSelect = By.xpath("//select[position()=2]");
    private By successMessage = By.xpath("//*[contains(@class, 'Toastify__toast') and contains(., 'Book added successfully')]");
    private By errorMessage = By.xpath("//div[contains(text(), 'Error') or contains(text(), 'error')]");
    
    // Constructor
    public PageLogin(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Helper to safely send keys or fallback to JS when element is not interactable
    private void safeSendKeys(By locator, String value) {
        WebElement el = null;
        try {
            el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            el.sendKeys(value);
            return;
        } catch (Exception e) {
            // fallback to JS
            try {
                if (el == null) {
                    el = driver.findElement(locator);
                }
                String script = "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));";
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script, el, value);
                return;
            } catch (Exception ignored) {}
        }
    }
    
    // Navigation Methods
    public void navigateToHome() {
        driver.navigate().to("http://localhost:5173");
    }
    
    public void navigateToBooks() {
        driver.navigate().to("http://localhost:5173/books");
    }
    
    public void navigateToAdminDashboard() {
        driver.navigate().to("http://localhost:5173/admin-dashboard");
    }
    
    public void navigateToAddBook() {
        driver.navigate().to("http://localhost:5173/add-book");
    }
    
    public void navigateToContact() {
        driver.navigate().to("http://localhost:5173/contact");
    }
    
    public void navigateToAbout() {
        driver.navigate().to("http://localhost:5173/about");
    }
    
    // Login Methods
    public void clickLoginButton() {
        // First wait for button to be present, then clickable
        wait.until(ExpectedConditions.presenceOfElementLocated(loginButton));
        // Small delay for React rendering
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }
    
    public void enterEmail(String email) {
        // Ensure we're in user mode (not admin mode)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(emailField));
        } catch (Exception e) {
            // Email field not found, toggle to user mode
            try {
                WebElement hereButton = wait.until(ExpectedConditions.elementToBeClickable(adminToggleHere));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", hereButton);
                Thread.sleep(500);
            } catch (Exception ignored) {}
        }
        safeSendKeys(emailField, email);
    }
    
    public void enterPassword(String password) {
        // Ensure we're in user mode (not admin mode)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(passwordField));
        } catch (Exception e) {
            // Password field not found, toggle to user mode
            try {
                WebElement hereButton = wait.until(ExpectedConditions.elementToBeClickable(adminToggleHere));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", hereButton);
                Thread.sleep(500);
            } catch (Exception ignored) {}
        }
        safeSendKeys(passwordField, password);
    }
    
    public void clickLoginSubmit() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(loginSubmitButton));
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));",
                el
            );
        } catch (Exception e) {
            wait.until(ExpectedConditions.elementToBeClickable(loginSubmitButton)).click();
        }
    }
    
    public void login(String email, String password) {
        clickLoginButton();
        enterEmail(email);
        enterPassword(password);
        clickLoginSubmit();
    }
    
    // Sign Up Methods
    public void clickSignUpButton() {
        openUserAuthModal();
        try {
            WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(signUpButton));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
            wait.until(ExpectedConditions.presenceOfElementLocated(signUpSubmitButton));
        } catch (Exception e) {
            try {
                WebElement hereButton = wait.until(ExpectedConditions.elementToBeClickable(adminToggleHere));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", hereButton);
                WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(signUpButton));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
                wait.until(ExpectedConditions.presenceOfElementLocated(signUpSubmitButton));
            } catch (Exception ignored) {
                // Leave the failure to the calling test with the current state intact.
            }
        }
    }
    
    public void enterUsername(String username) {
        // Ensure we're in user mode (not admin mode)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
        } catch (Exception e) {
            // Username field not found, toggle to user mode
            try {
                WebElement hereButton = wait.until(ExpectedConditions.elementToBeClickable(adminToggleHere));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", hereButton);
                Thread.sleep(500);
            } catch (Exception ignored) {}
        }
        safeSendKeys(usernameField, username);
    }
    
    public void enterConfirmPassword(String password) {
        // Ensure we're in user mode (not admin mode) - needed for signup
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(confirmPasswordField));
        } catch (Exception e) {
            // Confirm password field not found, toggle to user mode
            try {
                WebElement hereButton = wait.until(ExpectedConditions.elementToBeClickable(adminToggleHere));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", hereButton);
                Thread.sleep(500);
            } catch (Exception ignored) {}
        }
        safeSendKeys(confirmPasswordField, password);
    }

    // Admin secret-key entry (for AdminLoginForm)
    public void enterAdminSecretKey(String secret) {
        // Try presence then send keys with safe fallback
        safeSendKeys(adminSecretKeyField, secret);
    }
    
    public void clickSignUpSubmit() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(signUpSubmitButton));
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception e) {
            wait.until(ExpectedConditions.elementToBeClickable(signUpSubmitButton)).click();
        }
    }
    
    public void signup(String email, String username, String password) {
        clickLoginButton();
        clickSignUpButton();
        enterEmail(email);
        enterUsername(username);
        enterPassword(password);
        enterConfirmPassword(password);
        clickSignUpSubmit();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(signUpSubmitButton));
    }
    
    // Logout Methods
    public void clickLogoutButton() {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception e) { el.click(); }
        By confirmLogoutButton = By.xpath("//div[contains(@class, 'fixed') and contains(@class, 'inset-0')]//button[normalize-space()='Logout']");
        WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(confirmLogoutButton));
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", confirm);
        } catch (Exception e) {
            confirm.click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
    }
    
    public void logout() {
        clickLogoutButton();
    }
    
    // Navigation Methods
    
    
    public void clickContactLink() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(contactLink));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);arguments[0].click();", el);
        wait.until(driver -> driver.getCurrentUrl().contains("/contact"));
    }
    
    public void clickAboutLink() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(aboutLink));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);arguments[0].click();", el);
        wait.until(driver -> driver.getCurrentUrl().contains("/about"));
    }
    
    public void clickHomeLink() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(homeLink));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);arguments[0].click();", el);
        wait.until(driver -> driver.getCurrentUrl().contains("/") || driver.getCurrentUrl().endsWith("/"));
    }

    public void clickBooksLink() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(booksLink));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);arguments[0].click();", el);
        wait.until(driver -> driver.getCurrentUrl().contains("/books"));
    }
    
    // Dark Mode Methods
    public void toggleDarkMode() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(darkModeToggle));
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));",
                el
            );
        } catch (Exception e) {
            el.click();
        }
    }
    
    public boolean isDarkModeEnabled() {
        try {
            String htmlClass = driver.findElement(By.tagName("html")).getAttribute("class");
            if (htmlClass != null && htmlClass.contains("dark")) return true;
        } catch (Exception ignored) {}
        try {
            Object ls = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return window.localStorage.getItem('theme');");
            if (ls != null && ls.toString().toLowerCase().contains("dark")) return true;
        } catch (Exception ignored) {}
        return false;
    }
    
    // Book Filter Methods
    public void clickFilterIcon() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(filterIcon));
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));",
                el
            );
        } catch (Exception e) { el.click(); }
        wait.until(ExpectedConditions.visibilityOfElementLocated(filterDropdown));
    }

    // Ensure the login modal is opened and showing the user login/register (not admin)
    public void openUserAuthModal() {
        try {
            clickLoginButton();
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            
            // Check if username field exists (user mode active)
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
                return; // Already in user mode
            } catch (Exception ignored) {}
            
            // Not in user mode, try to toggle via "Here" button
            try {
                WebElement hereButton = wait.until(ExpectedConditions.elementToBeClickable(adminToggleHere));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", hereButton);
                Thread.sleep(500);
                
                // Now wait for username field to appear
                wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
            } catch (Exception e) {
                // Silent fail - modal may not be in expected state but continue
            }
        } catch (Exception ignored) {
            // Silently ignore any issues opening modal
        }
    }

    public void clearUsernameField() {
        WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
        username.clear();
    }

    public String getUsernameFieldValue() {
        return driver.findElement(usernameField).getAttribute("value");
    }
    
    public void filterFreeBooks() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(filterDropdown));
        } catch (Exception e) {
            clickFilterIcon();
        }
        WebElement item = wait.until(ExpectedConditions.presenceOfElementLocated(freeBookFilter));
        try { ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", item); } catch (Exception e) { item.click(); }
    }
    
    public void filterPaidBooks() {
        clickFilterIcon();
        WebElement item = wait.until(ExpectedConditions.presenceOfElementLocated(paidBookFilter));
        try { ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", item); } catch (Exception e) { item.click(); }
    }
    
    // Book Methods
    public void addBook(String title, String description, String category, String price) {
        navigateToAddBook();
        wait.until(ExpectedConditions.presenceOfElementLocated(bookTitleInput)).sendKeys(title);
        driver.findElement(bookDescriptionInput).sendKeys(description);
        wait.until(ExpectedConditions.elementToBeClickable(addBookButton)).click();
    }
    
    public int getVisibleBooksCount() {
        return driver.findElements(bookCard).size();
    }
    
    // Assertion Methods
    public boolean isLoginButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isLogoutButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isSuccessMessageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isErrorMessageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public WebElement getLoginButton() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(loginButton));
    }
    
    public WebElement getLogoutButton() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(logoutButton));
    }
    
    public void clearEmailField() {
        WebElement email = wait.until(ExpectedConditions.presenceOfElementLocated(emailField));
        email.clear();
    }
    
    public void clearPasswordField() {
        WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated(passwordField));
        password.clear();
    }
    
    public String getEmailFieldValue() {
        return driver.findElement(emailField).getAttribute("value");
    }
    
    public String getPasswordFieldValue() {
        return driver.findElement(passwordField).getAttribute("value");
    }
}
