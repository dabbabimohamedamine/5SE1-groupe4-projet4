package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;

import javax.validation.ValidationException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperatorServiceImplTest {

    @Mock
    private OperatorRepository operatorRepository;

    @InjectMocks
    private OperatorServiceImpl operatorService;

    private Operator validOperator;

    @BeforeEach
    void setUp() {
        // Initialize a valid operator for the test
        validOperator = new Operator();
        validOperator.setFname("John");
        validOperator.setLname("Doe");
        validOperator.setEmail("john.doe@company.com");
    }

    @Test
    void testAddOperator_Success() {
        // Arrange: Mock the repository to return a saved operator
        when(operatorRepository.existsByEmail(validOperator.getEmail())).thenReturn(false);  // No operator exists with this email
        when(operatorRepository.save(validOperator)).thenReturn(validOperator);

        // Act: Call the method
        Operator savedOperator = operatorService.addOperator(validOperator);

        // Assert: Verify that the operator is saved and returned
        assertNotNull(savedOperator);
        assertEquals("John", savedOperator.getFname());
        assertEquals("Doe", savedOperator.getLname());
        assertEquals("john.doe@company.com", savedOperator.getEmail());

        // Verify interactions with the repository
        verify(operatorRepository, times(1)).existsByEmail(validOperator.getEmail());
        verify(operatorRepository, times(1)).save(validOperator);
    }

    @Test
    void testAddOperator_EmailAlreadyExists() {
        // Arrange: Mock the repository to simulate that the email already exists
        when(operatorRepository.existsByEmail(validOperator.getEmail())).thenReturn(true);

        // Act & Assert: Check that a ValidationException is thrown
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            operatorService.addOperator(validOperator);
        });

        assertEquals("An operator with this email already exists.", exception.getMessage());

        // Verify that the save method was never called
        verify(operatorRepository, times(1)).existsByEmail(validOperator.getEmail());
        verify(operatorRepository, times(0)).save(validOperator);
    }

    @Test
    void testAddOperator_InvalidEmailFormat() {
        // Arrange: Set an invalid email format
        validOperator.setEmail("invalid-email-format");

        // Act & Assert: Check that a ValidationException is thrown due to invalid email format
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            operatorService.addOperator(validOperator);
        });

        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    void testAddOperator_EmptyFirstName() {
        // Arrange: Set the first name to an empty value
        validOperator.setFname("");

        // Act & Assert: Check that a ValidationException is thrown due to empty first name
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            operatorService.addOperator(validOperator);
        });

        assertEquals("First name cannot be empty.", exception.getMessage());
    }

    @Test
    void testAddOperator_EmptyLastName() {
        // Arrange: Set the last name to an empty value
        validOperator.setLname("");

        // Act & Assert: Check that a ValidationException is thrown due to empty last name
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            operatorService.addOperator(validOperator);
        });

        assertEquals("Last name cannot be empty.", exception.getMessage());
    }

    @Test
    void testAddOperator_EmptyEmail() {
        // Arrange: Set the email to an empty value
        validOperator.setEmail("");

        // Act & Assert: Check that a ValidationException is thrown due to empty email
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            operatorService.addOperator(validOperator);
        });

        assertEquals("Email cannot be empty.", exception.getMessage());
    }
}
