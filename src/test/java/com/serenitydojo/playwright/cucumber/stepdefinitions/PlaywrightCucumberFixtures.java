package com.serenitydojo.playwright.cucumber.stepdefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.util.Arrays;
import java.util.Map;

public class PlaywrightCucumberFixtures {

    private static final ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(() -> {
        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        return playwright;
    });
    private static final ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );
    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();


    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    @Before(order = 100)
    public void setUpBrowserContext(){
        browserContext.set(browser.get().newContext(new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .setExtraHTTPHeaders(Map.of(
                        "Accept-Language", "en-US,en;q=0.9"
                ))));
        page.set(browserContext.get().newPage());

    }

    @After
    public void closeContext() {
        browserContext.get().close();
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }


    public static Page getPage(){
        return page.get();
    }

    public static BrowserContext getContext(){
        return browserContext.get();
    }
}
