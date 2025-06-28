package com.serenitydojo.playwright.toolshop.fixtures;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;

public interface TakesFinalScreenshot {

//    @AfterEach
//    default void takeScreenshot(Page page){
//        System.out.println("Taking final screenshot");
//        ScreenshotManager.takeScreenshot(page, "Final Screenshot");
//    }

    void takeScreenshot();
}
