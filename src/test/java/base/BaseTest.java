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
        
        // Chrome Options - minimal set to avoid incompatibilities
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        
        // Initialize WebDriver
        driver = new ChromeDriver(options);
        
        // Give Chrome time to fully initialize (important on resource-constrained EC2)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Set timeouts - generous for EC2 resource constraints
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(20));
        driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(60));
    }
    
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
