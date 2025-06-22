package com.serenitydojo.playwright.toolshop.catalog.pageObjects;

import com.microsoft.playwright.Page;
import com.serenitydojo.playwright.toolshop.fixtures.ScreenshotManager;
import io.qameta.allure.Step;

public class NavBar {
    private final Page page;


    public NavBar(Page page) {
        this.page = page;
    }

    @Step("Open the shopping cart")
    public void openCart() {

        page.getByTestId("nav-cart").click();
        ScreenshotManager.takeScreenshot(page, "Shopping Cart");
    }

    @Step("Open home page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
        ScreenshotManager.takeScreenshot(page, "Home Page");
    }


    @Step("Open contact page")
    public void openContactPage(){
        page.navigate("https://practicesoftwaretesting.com/contact");
        ScreenshotManager.takeScreenshot(page, "Contact Page");
    }
}
