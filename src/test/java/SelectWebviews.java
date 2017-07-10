import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testobject.appium.junit.TestObjectTestResultWatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;


public class SelectWebviews {
    private AppiumDriver driver;

    @Rule
    public TestName testName = new TestName();

    @Rule
    public TestObjectTestResultWatcher resultWatcher = new TestObjectTestResultWatcher();

    @Before
    public void setup() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();


        capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY"));
        capabilities.setCapability("testobject_device", System.getenv("TESTOBJECT_DEVICE_ID"));
        capabilities.setCapability("testobject_appium_version", System.getenv("TESTOBJECT_APPIUM_VERSION"));

		String TESTOBJECT_SESSION_CREATION_TIMEOUT = System.getenv("TESTOBJECT_SESSION_CREATION_TIMEOUT");
		if (TESTOBJECT_SESSION_CREATION_TIMEOUT != null) {
			capabilities.setCapability("testobject_session_creation_timeout", TESTOBJECT_SESSION_CREATION_TIMEOUT);
		}

		String TESTOBJECT_SESSION_CREATION_RETRY = System.getenv("TESTOBJECT_SESSION_CREATION_RETRY");
		if (TESTOBJECT_SESSION_CREATION_RETRY != null) {
			capabilities.setCapability("testobject_session_creation_retry", TESTOBJECT_SESSION_CREATION_RETRY);
		}

        String testUUID = UUID.randomUUID().toString();
        System.out.println("TestUUID: " + testUUID);
        capabilities.setCapability("testobject_testuuid", testUUID);

        String testobjectAppiumEndpoint = Optional.ofNullable(System.getenv("APPIUM_URL"))
                .orElse("http://appium.testobject.com/wd/hub");

        driver = new AndroidDriver(new URL(testobjectAppiumEndpoint), capabilities);

        System.out.println(driver.getCapabilities().getCapability("testobject_test_report_url"));
        System.out.println(driver.getCapabilities().getCapability("testobject_test_live_view_url"));

        resultWatcher.setAppiumDriver(driver);
    }

    @Test
    public void reproTest() throws Exception {

        final String webviewContext = "WEBVIEW";

        // Wait for webview to be available
        Thread.sleep(1000 * 25);

        Iterable<String> contexts =  driver.getContextHandles();

        boolean foundWebview = false;
        for (String context : contexts) {
            System.out.println("Found context: " + context);
            if (context.contains(webviewContext)) {
                System.out.println("Found webview. Switching context");
                driver.context(context);
                driver.getPageSource();
                foundWebview = true;
            }
        }
        Assert.assertTrue("Looking for context " + webviewContext + " in " + contexts, foundWebview);
    }
}
