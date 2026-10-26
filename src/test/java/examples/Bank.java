package examples;
import java.util.*;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
public class Bank {
    final WebDriver driver;
    final WebDriverWait wait;
    final String username="qa"+UUID.randomUUID().toString().replace("-", "").substring(0,18);
    final String password="DemoPass42!";
    Bank(WebDriver driver) { this.driver=driver; this.wait=new WebDriverWait(driver,Duration.ofSeconds(20)); }
    WebElement element(String id) { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id))); }
    void home() { driver.get(System.getenv().getOrDefault("PARABANK_URL","https://parabank.parasoft.com/parabank")+"/index.htm"); }
    void link(String name) { wait.until(ExpectedConditions.elementToBeClickable(By.linkText(name))).click(); }
    void text(String selector,String expected) {
        wait.until(ExpectedConditions.textToBe(By.cssSelector(selector),expected));
        if (!driver.findElement(By.cssSelector(selector)).getText().equals(expected)) throw new AssertionError(expected);
    }
    void form(boolean mismatch) {
        home(); link("Register");
        Map<String,String> fields=Map.ofEntries(Map.entry("customer.firstName","Automation"),Map.entry("customer.lastName","Example"),Map.entry("customer.address.street","123 Fictional Street"),Map.entry("customer.address.city","Demo City"),Map.entry("customer.address.state","CA"),Map.entry("customer.address.zipCode","90210"),Map.entry("customer.phoneNumber","5551234567"),Map.entry("customer.ssn","123456789"));
        fields.forEach((id,value)->element(id).sendKeys(value));
        element("customer.username").sendKeys(username); element("customer.password").sendKeys(password);
        element("repeatedPassword").sendKeys(mismatch?"Different42!":password);
    }
    void submit() { driver.findElement(By.cssSelector("#customerForm input[type=submit]")).click(); }
    void register() { form(false); submit(); text("#rightPanel h1","Welcome "+username); }
    void login() {
        driver.findElement(By.name("username")).sendKeys(username); driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("#loginPanel input[type=submit]")).click();
    }
    void savings() {
        link("Open New Account"); wait.until(d-> !d.findElements(By.cssSelector("#fromAccountId option")).isEmpty());
        new Select(element("type")).selectByValue("1"); driver.findElement(By.cssSelector("#openAccountForm input.button")).click();
        wait.until(ExpectedConditions.textMatches(By.id("newAccountId"),java.util.regex.Pattern.compile("^[0-9]+$")));
        String id=element("newAccountId").getText(); element("newAccountId").click(); text("#accountId",id); text("#accountType","SAVINGS");
    }
}
