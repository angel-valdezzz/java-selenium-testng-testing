package examples;
        import org.testng.annotations.*;
        import org.openqa.selenium.*;
        import org.openqa.selenium.chrome.*;
        import java.nio.file.*;
        public class ParaBankUiTest {
            WebDriver driver; Bank bank;
            @BeforeMethod public void setup() { driver=new ChromeDriver(new ChromeOptions().addArguments("--headless=new","--no-sandbox","--disable-dev-shm-usage","--window-size=1440,900")); bank=new Bank(driver); }
            @AfterMethod(alwaysRun=true) public void teardown() throws Exception { if(driver!=null) { try { Files.createDirectories(Path.of("reports")); Files.write(Path.of("reports",java.util.UUID.randomUUID()+".png"),((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)); } finally { driver.quit(); } } }

    @Test public void registration() { bank.register(); }
    @Test public void logoutLogin() { bank.register(); bank.link("Log Out"); bank.login(); bank.text("#rightPanel h1","Accounts Overview"); }
    @Test public void requiredFields() { bank.home(); bank.link("Register"); bank.submit(); bank.text("[id='customer.firstName.errors']","First name is required."); }
    @Test public void passwordMismatch() { bank.form(true); bank.submit(); bank.text("[id='repeatedPassword.errors']","Passwords did not match."); }
    @Test public void invalidLogin() { bank.home(); bank.login(); bank.text("#rightPanel .error","The username and password could not be verified."); }
    @Test public void savingsAccount() { bank.register(); bank.savings(); }

        }