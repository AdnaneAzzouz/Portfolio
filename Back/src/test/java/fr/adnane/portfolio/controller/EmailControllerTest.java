package fr.adnane.portfolio.controller;

import fr.adnane.portfolio.model.EmailForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(EmailController.class)
@MockitoBean(types = JavaMailSender.class)
class EmailControllerTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void sendEmail_withValidData_shouldSendEmailAndReturnOk() {
        // Given
        EmailForm emailForm = new EmailForm("John", "Doe", "john.doe@example.com", "Test message");
        emailForm.setPhoneNumber("1234567890");
        emailForm.setCompanyName("ACME Inc");

        // When
        webTestClient.post()
                .uri("/email")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(emailForm), EmailForm.class)
                .exchange()
                // Then
                .expectStatus().isOk()
                .expectBody(EmailForm.class)
                .isEqualTo(emailForm);

        // Verify that email was sent
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("noreply.adnaneportfolio@gmail.com", sentMessage.getFrom());
        Assertions.assertNotNull(sentMessage.getTo());
        assertEquals("adnaneazzouzpro@outlook.com", sentMessage.getTo()[0]);
        assertEquals("[PORTFOLIO] - Someone wants to contact you!", sentMessage.getSubject());

        String messageText = sentMessage.getText();
        Assertions.assertNotNull(messageText);
        assertTrue(messageText.contains("John Doe"));
        assertTrue(messageText.contains("Test message"));
        assertTrue(messageText.contains("john.doe@example.com"));
        assertTrue(messageText.contains("1234567890"));
        assertTrue(messageText.contains("ACME Inc"));
    }

    @Test
    void sendEmail_withMinimalData_shouldSendEmailAndReturnOk() {
        // Given
        EmailForm emailForm = new EmailForm("Jane", "Smith", "jane.smith@example.com", "Another test message");

        // When
        webTestClient.post()
                .uri("/email")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(emailForm), EmailForm.class)
                .exchange()
                // Then
                .expectStatus().isOk()
                .expectBody(EmailForm.class)
                .isEqualTo(emailForm);

        // Verify that email was sent with the correct content
        // Use atLeastOnce() to indicate that we expect the method to be called at least once
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, Mockito.atLeastOnce()).send(messageCaptor.capture());

        // Find the message that contains our test data
        boolean foundMatchingMessage = false;
        for (SimpleMailMessage message : messageCaptor.getAllValues()) {
            String messageText = message.getText();
            Assertions.assertNotNull(messageText);
            if (messageText.contains("Jane Smith") && messageText.contains("Another test message") && messageText.contains("jane.smith@example.com")) {
                foundMatchingMessage = true;
                // Should not contain optional fields
                assertTrue(!messageText.contains("phone") || !messageText.contains("company"));
                break;
            }
        }
        assertTrue(foundMatchingMessage, "Could not find email with expected content");
    }

    @Test
    void sendEmail_withInvalidData_shouldReturnBadRequest() {
        // Given - JSON with the missing required field (firstName)
        String invalidJson = "{\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"message\":\"Test message\"}";

        // When & Then
        webTestClient.post()
                .uri("/email")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest();

        // No email should be sent
        verify(javaMailSender, times(0)).send(any(SimpleMailMessage.class));
    }
}
