import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testobject.appium.junit.TestObjectTestResultWatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

public class SelectWebviews {

	DesiredCapabilities capabilities;
	private AppiumDriver driver;

	@Rule
	public TestName testName = new TestName();

	@Rule
	public TestObjectTestResultWatcher resultWatcher = new TestObjectTestResultWatcher();

	@Before
	public void setup() throws MalformedURLException {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY"));
		setOptionalCapability("testobject_app_id", "TESTOBJECT_APP_ID");
		setOptionalCapability("testobject_device", "TESTOBJECT_DEVICE");
		setOptionalCapability("deviceName", "DEVICE_NAME");
		setOptionalCapability("platformVersion", "PLATFORM_VERSION");
		setOptionalCapability("automationName", "AUTOMATION_NAME");
		setOptionalCapability("testobject_appium_version", "TESTOBJECT_APPIUM_VERSION");
		setOptionalCapability("testobject_cache_device", "TESTOBJECT_CACHE_DEVICE");
		setOptionalCapability("testobject_session_creation_timeout", "TESTOBJECT_SESSION_CREATION_TIMEOUT");
		setOptionalCapability("testobject_session_creation_retry", "TESTOBJECT_SESSION_CREATION_RETRY");


		String testUUID = UUID.randomUUID().toString();
		System.out.println("TestUUID: " + testUUID);
		capabilities.setCapability("testobject_testuuid", testUUID);

		String testobjectAppiumEndpoint = Optional.ofNullable(System.getenv("APPIUM_URL"))
				.orElse("http://appium.testobject.com/wd/hub");

		System.out.println(capabilities.toString());

		driver = new AndroidDriver(new URL(testobjectAppiumEndpoint), capabilities);

		System.out.println(driver.getCapabilities().getCapability("testobject_test_report_url"));
		System.out.println(driver.getCapabilities().getCapability("testobject_test_live_view_url"));


		resultWatcher.setRemoteWebDriver(driver);
	}

	@Test
	public void reproTest() throws Exception {

		final String webviewContext = "WEBVIEW";
		// Wait for webview to be available
		Thread.sleep(1000 * 25);

		Iterable<String> contexts = driver.getContextHandles();

		boolean foundWebview = false;
		for (String context : contexts) {
			System.out.println("Found context: " + context);
			if (context.contains(webviewContext)) {
				System.out.println("Found webview. Switching context..");
				driver.context(context);
				driver.getPageSource();
				foundWebview = true;
			}
		}
		Assert.assertTrue("Looking for context " + webviewContext + " in " + contexts, foundWebview);
	}

	private void setOptionalCapability(String desiredCapabilityName, String environmentVariableName) {
		Optional.ofNullable(System.getenv(environmentVariableName))
				.filter(env -> !env.isEmpty())
				.ifPresent(value -> capabilities.setCapability(desiredCapabilityName, value));
	}

}
