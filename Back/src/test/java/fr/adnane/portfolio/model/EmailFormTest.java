package fr.adnane.portfolio.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailFormTest {

    @Test
    void testEmailFormCreation() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String message = "Test message";
        
        // When
        EmailForm emailForm = new EmailForm(firstName, lastName, email, message);
        
        // Then
        assertEquals(firstName, emailForm.getFirstName());
        assertEquals(lastName, emailForm.getLastName());
        assertEquals(email, emailForm.getEmail());
        assertEquals(message, emailForm.getMessage());
        assertNull(emailForm.getPhoneNumber());
        assertNull(emailForm.getCompanyName());
    }
    
    @Test
    void testEmailFormWithAllFields() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phoneNumber = "1234567890";
        String companyName = "ACME Inc";
        String message = "Test message";
        
        // When
        EmailForm emailForm = new EmailForm(firstName, lastName, email, message);
        emailForm.setPhoneNumber(phoneNumber);
        emailForm.setCompanyName(companyName);
        
        // Then
        assertEquals(firstName, emailForm.getFirstName());
        assertEquals(lastName, emailForm.getLastName());
        assertEquals(email, emailForm.getEmail());
        assertEquals(phoneNumber, emailForm.getPhoneNumber());
        assertEquals(companyName, emailForm.getCompanyName());
        assertEquals(message, emailForm.getMessage());
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Given
        EmailForm emailForm1 = new EmailForm("John", "Doe", "john.doe@example.com", "Test message");
        EmailForm emailForm2 = new EmailForm("John", "Doe", "john.doe@example.com", "Test message");
        EmailForm emailForm3 = new EmailForm("Jane", "Doe", "jane.doe@example.com", "Another message");
        
        // Then
        assertEquals(emailForm1, emailForm2);
        assertEquals(emailForm1.hashCode(), emailForm2.hashCode());
        assertNotEquals(emailForm1, emailForm3);
        assertNotEquals(emailForm1.hashCode(), emailForm3.hashCode());
    }
    
    @Test
    void testToString() {
        // Given
        EmailForm emailForm = new EmailForm("John", "Doe", "john.doe@example.com", "Test message");
        
        // When
        String toString = emailForm.toString();
        
        // Then
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("john.doe@example.com"));
        assertTrue(toString.contains("Test message"));
    }
}