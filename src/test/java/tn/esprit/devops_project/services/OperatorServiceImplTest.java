/*package tn.esprit.devops_project.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OperatorServiceImplTest {

@Autowired
private OperatorServiceImpl operatorService;

@Autowired
private OperatorRepository operatorRepository;

@Test
public void testAddOperator() {
        Operator operatorToAdd = new Operator();
        operatorToAdd.setFname("Test");
        operatorToAdd.setLname("Operator");
        operatorToAdd.setPassword("Operator1");
        Operator savedOperator = operatorService.addOperator(operatorToAdd);
        assertNotNull(savedOperator);
        assertNotNull(savedOperator.getIdOperateur());
        assertEquals("Test", savedOperator.getFname());
        Operator foundOperator = operatorRepository.findById(savedOperator.getIdOperateur()).orElse(null);
        assertNotNull(foundOperator);
        assertEquals(savedOperator.getFname(), foundOperator.getFname());
        }
        }

 */