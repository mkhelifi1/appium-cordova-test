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
		setOptionalCapability("TESTOBJECT_DEVICE");
		setOptionalCapability("TESTOBJECT_APPIUM_VERSION");
		setOptionalCapability("TESTOBJECT_SESSION_CREATION_TIMEOUT");
		setOptionalCapability("TESTOBJECT_SESSION_CREATION_RETRY");

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
				System.out.println("Found webview. Switching context");
				driver.context(context);
				driver.getPageSource();
				foundWebview = true;
			}
		}
		Assert.assertTrue("Looking for context " + webviewContext + " in " + contexts, foundWebview);
	}

	private void setOptionalCapability(String var) {
		Optional.ofNullable(System.getenv(var))
				.filter(env -> !env.isEmpty())
				.ifPresent(data -> capabilities.setCapability(var, data));
	}

}
