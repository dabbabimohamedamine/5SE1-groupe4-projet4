package tn.esprit.devops_project.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.services.Iservices.IOperatorService;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j // Fixing the logging issue
public class OperatorServiceImpl implements IOperatorService {

	private final OperatorRepository operatorRepository;
	private final Random random;

	@Autowired
	public OperatorServiceImpl(OperatorRepository operatorRepository) {
		this.operatorRepository = operatorRepository;
		this.random = new Random();
	}

	@Override
	public List<Operator> retrieveAllOperators() {
		return (List<Operator>) operatorRepository.findAll();
	}

	@Override
	public Operator addOperator(Operator operator) {
		// Input Validation: Ensure fname and lname are not empty
		if (operator.getFname() == null || operator.getFname().isEmpty()) {
			throw new ValidationException("First name cannot be empty.");
		}
		if (operator.getLname() == null || operator.getLname().isEmpty()) {
			throw new ValidationException("Last name cannot be empty.");
		}

		// Normalize Input: Capitalize the first letter of fname and lname
		operator.setFname(capitalizeFirstLetter(operator.getFname()));
		operator.setLname(capitalizeFirstLetter(operator.getLname()));

		// Email Validation: Ensure email is not empty and matches basic email pattern
		if (operator.getEmail() == null || operator.getEmail().isEmpty()) {
			throw new ValidationException("Email cannot be empty.");
		}
		if (!isValidEmail(operator.getEmail())) {
			throw new ValidationException("Invalid email format.");
		}

		// Check if operator already exists in the system based on email (unique constraint)
		if (operatorRepository.existsByEmail(operator.getEmail())) {
			throw new ValidationException("An operator with this email already exists.");
		}

		// Log the addition process
		log.info("Preparing to add new operator: {} {} with email: {}", operator.getFname(), operator.getLname(), operator.getEmail());

		// Save the operator
		Operator savedOperator = operatorRepository.save(operator);

		// Log success
		log.info("Successfully added operator with ID: {} and email: {}", savedOperator.getIdOperateur(), savedOperator.getEmail());

		return savedOperator;
	}

	// Helper function to capitalize the first letter of a string
	private String capitalizeFirstLetter(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

	// Helper function to validate email format using a simple regex
	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}


	@Override
	public void deleteOperator(Long id) {
		operatorRepository.deleteById(id);
	}

	@Override
	public Operator updateOperator(Operator operator) {
		return operatorRepository.save(operator);
	}

	@Override
	public Operator retrieveOperator(Long id) {
		return operatorRepository.findById(id)
				.orElseThrow(() -> new NullPointerException("Operator not found"));
	}
}
