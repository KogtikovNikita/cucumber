package com.serenitydojo.playwright.toolshop.catalog;

import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import com.serenitydojo.playwright.toolshop.catalog.pageObjects.ProductList;
import com.serenitydojo.playwright.toolshop.catalog.pageObjects.SearchComponent;
import io.qameta.allure.Feature;
import io.qameta.allure.junit5.AllureJunit5;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AllureJunit5.class)
//@UsePlaywright(HeadlessChromeOptions.class)
@DisplayName("Search for products")
@Feature("Product Catalog")
public class SearchForProductsTest extends PlaywrightTestCase {


    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }


    @Test
    void whenSearchingByKeyword() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("tape");

        var matchingProducts = productList.getProductNames();
        org.assertj.core.api.Assertions.assertThat(matchingProducts)
                .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
    }

    @Test
    void whenThereIsNoMatchingProducts(){
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("unknown");

        var matchingProducts = productList.getProductNames();

        Assertions.assertThat(matchingProducts).isEmpty();
        Assertions.assertThat(productList.getSearchCompletedMessage()).isEqualTo("There are no products found.");
    }

    @Test
    void clearingTheSearchResults(){
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("saw");

        searchComponent.clearSearch();
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).hasSize(9);
    }
}
