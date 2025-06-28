package com.serenitydojo.playwright.toolshop.catalog;

import com.microsoft.playwright.*;
import com.serenitydojo.playwright.toolshop.catalog.pageObjects.*;
import com.serenitydojo.playwright.toolshop.fixtures.*;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit5.AllureJunit5;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Paths;
import java.util.List;

@ExtendWith(AllureJunit5.class)
@Feature("Shopping Cart")
public class AddToCartTest extends PlaywrightTestCase{


    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;

    @BeforeEach
    void setUpAddToCartTest(){
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
    }

    @BeforeEach
    void openHomePage() {
        navBar.openHomePage();
    }

    @BeforeEach
    void setupTrace() {
        browserContext.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @AfterEach
    void recordTrace(TestInfo testInfo){
        String traceName = testInfo.getDisplayName().replace(' ', '-').toLowerCase();
        browserContext.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/traces/trace" + traceName +  ".zip"))
        );
    }


    @Test
    @Story("Checking out a product")
    @DisplayName("Checking out a single item")
    void whenCheckingOutASingleItem(){
        searchComponent.searchBy("pliers");
        productList.viewProductDetails("Combination Pliers");

        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    Assertions.assertThat(item.title()).contains("Combination Pliers");
                    Assertions.assertThat(item.quantity()).isEqualTo(3);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());

                });
    }

    @Test
    @DisplayName("Checking out multiple items")
    void whenCheckingOutMultipleItems(){
        navBar.openHomePage();
        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();

        navBar.openHomePage();
        productList.viewProductDetails("Slip Joint Pliers");
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();
        Assertions.assertThat(lineItems).hasSize(2);

        List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
        Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

        Assertions.assertThat(lineItems)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.price()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });

    }


}
