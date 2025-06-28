package com.serenitydojo.playwright.toolshop.fixtures;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Map;

public abstract class PlaywrightTestCase implements TakesFinalScreenshot {


    protected static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(() -> {
        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        return playwright;
    });
    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );
    protected BrowserContext browserContext;


    protected  Page page;


    @BeforeEach
    public void setUp(){
        browserContext = browser.get().newContext(new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .setExtraHTTPHeaders(Map.of(
                        "Accept-Language", "en-US,en;q=0.9"
                )));
        page = browserContext.newPage();
        page.setDefaultTimeout(150000);

    }

    @AfterEach
    public void closeContext() {
        takeScreenshot("End of test");
        browserContext.close();
    }


    protected void takeScreenshot(String name) {
        var screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );
        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }


    @AfterAll
    public static void tearDown() {
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }

    public void takeScreenshot() {
        System.out.println("Taking final screenshot");
        ScreenshotManager.takeScreenshot(page, "Final Screenshot");
    }

}
