package com.serenitydojo.playwright.toolshop.contact;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.fixtures.*;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit5.AllureJunit5;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@ExtendWith(AllureJunit5.class)
@UsePlaywright(HeadlessChromeOptions.class)
@DisplayName("Contact Form Tests")
@Feature("Contacts")
public class ContactFormTest extends PlaywrightTestCase {

    ContactForm contactForm;

    @DisplayName("interacting with the text fields")
    @Nested
    class WhenInteractingWithTheTextFields{

        @BeforeEach
        void openContactPage() {
            contactForm = new ContactForm(page);
            page.navigate("https://practicesoftwaretesting.com/contact");
        }



        @DisplayName("Complete the form")
        @Story("Contact Form")
        @Test
        void completeForm() throws Exception{

            contactForm.setFirstName("Sarah-Jane");
            contactForm.setLastName("Smith");
            contactForm.setEmail("sarah-jane@example.com");
            contactForm.setMessage("I hope this message finds you well. I purchased Combination Pliers (Order #123456) on 24 April 2025, and unfortunately, I've encountered an issue that I believe is covered under the warranty.");
            contactForm.selectSubject("Warranty");

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
            contactForm.setAttachment(fileToUpload);
            contactForm.submitForm();



            Assertions.assertThat(contactForm.getAlertMessage()).contains(" Thanks for your message! We will contact you shortly. ");


        }

        @DisplayName("Mandatory fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
        @Story("Contact Form")
        void mandatoryFields(String fieldName){
            contactForm.setFirstName("Sarah-Jane");
            contactForm.setLastName("Smith");
            contactForm.setEmail("sarah@example.com");
            contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
            contactForm.selectSubject("Warranty");


            contactForm.clearField(fieldName);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Test interrupted during sleep", e);
            }
            contactForm.submitForm(fieldName);


            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
            errorMessage.waitFor(new Locator.WaitForOptions().setTimeout(20000));
            assertThat(errorMessage).isVisible();
        }

        @DisplayName("The message must be at least 50 characters long")
        @Story("Contact Form")
        @Test
        void messageField() throws URISyntaxException{
            contactForm.setFirstName("Sarah-Jane");
            contactForm.setLastName("Smith");
            contactForm.setEmail("sarah-jane@example.com");
            contactForm.setMessage("A short message");
            contactForm.selectSubject("Warranty");

            contactForm.submitForm();


            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Message must be minimal 50 characters");
        }
    }
}
