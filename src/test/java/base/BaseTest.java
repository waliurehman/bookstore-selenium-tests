package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    
    protected WebDriver driver;
    
    @BeforeClass
    public void setup() {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        
        // Chrome Options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        
        // Initialize WebDriver
        driver = new ChromeDriver(options);
        
        // Set implicit wait and page load timeout
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(30));
    }
    
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
