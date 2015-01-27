package net.xeraa.morphia_demo.test;

import com.mongodb.DuplicateKeyException;

import net.xeraa.morphia_demo.entities.AddressEntity;
import net.xeraa.morphia_demo.entities.BankConnectionEntity;
import net.xeraa.morphia_demo.entities.CompanyEntity;
import net.xeraa.morphia_demo.entities.EmployeeEntity;
import net.xeraa.morphia_demo.entities.ManagerEntity;
import net.xeraa.morphia_demo.entities.WorkerEntity;
import net.xeraa.morphia_demo.types.AddressType;

import org.bson.types.ObjectId;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Testing our entities and the MongodbPersistence.
 */
public class PersistenceTest extends BaseTest {

  /**
   * Ensure a company is saved and the ObjectId is generated correctly.
   */
  @Test
  public void persistCompanyEntity() {
    CompanyEntity company = new CompanyEntity().setName("Test company");
    ObjectId id = persistence.persistCompanyEntity(company);
    assertNotNull("An ObjectId should have been generated when saving the entity", id);
    assertEquals("The return value and actual value of the ObjectId should match",
		 company.getId(), id);
  }

  /**
   * Check salary conversion.
   */
  @Test
  public void persistEmployeeEntity() {
    WorkerEntity worker = new WorkerEntity().setFirstname("Steve").setSurname("Jobs")
        .setEmail("steve@apple.com");
    persistence.persistWorkerEntity(worker);
    EmployeeEntity resultEmployee = persistence.findByEmail("steve@apple.com");
    assertEquals("Steve", resultEmployee.getFirstname());

    // Since there is only 1 manager this is fine, but you cannot rely on the order of inserted
    // entities in general.
    WorkerEntity resultWorker = persistence.getAllWorkers().get(0);
    assertEquals(new BigDecimal("10.00"), resultWorker.getDailyAllowance());
    ManagerEntity manager = new ManagerEntity().setFirstname("Mr").setSurname("Big")
    .setEmail("big@test.com").setSalary(new BigDecimal("5000")).setBonus(new BigDecimal("100000"));
    persistence.persistManagerEntity(manager);
    List<ManagerEntity> resultManagers = persistence.getAllManagers();
    assertEquals(1, resultManagers.size());
    assertEquals(new BigDecimal("100000.00"), resultManagers.get(0).getBonus());
  }

  /**
   * Check that upserts are working correctly.
   */
  @Test
  public void updateCompanyEntity() {
    CompanyEntity company = new CompanyEntity().setName("Test company");
    persistence.persistCompanyEntity(company);
    assertEquals("The value of an entity should match after persisting it", "Test company",
		 persistence.getAllCompanies().get(0).getName());
    assertFalse("The value of an entity should not match anything",
		"foobar".equals(persistence.getAllCompanies().get(0).getName()));
    company.setName("New name");
    persistence.persistCompanyEntity(company);
    assertEquals("The value of an entity should match after updateing it", "New name",
		 persistence.getAllCompanies().get(0).getName());
    assertFalse("The value of an entity should not match the old name",
		"Test company".equals(persistence.getAllCompanies().get(0).getName()));
  }

  /**
   * Testing queries returning a list of objects.
   */
  @Test
  public void getAllCompanies() {
    assertEquals("Right at the start no companies should be found", 0, persistence
	.getAllCompanies().size());
    CompanyEntity company1 = new CompanyEntity().setName("First company");
    persistence.persistCompanyEntity(company1);
    CompanyEntity company2 = new CompanyEntity().setName("Second company");
    persistence.persistCompanyEntity(company2);
    assertEquals("After persisting two companies they should be found", 2, persistence
	.getAllCompanies().size());
    persistence.clearData();
    assertEquals("After clearing no companies should be found again", 0, persistence
	.getAllCompanies().size());
  }

  /**
   * Checking the regular expression search, including upper- and lower-case versions.
   */
  @Test
  public void findByEmail() {
    WorkerEntity worker1 = new WorkerEntity().setFirstname("Philipp").setSurname("Krenn")
        .setEmail("pk@test.com");
    persistence.persistWorkerEntity(worker1);
    WorkerEntity worker2 = new WorkerEntity().setFirstname("Jane").setSurname("Doe")
        .setEmail("jane@test.com");
    persistence.persistWorkerEntity(worker2);
    assertNull("Null parameter shouldn't find anything", persistence.findByEmail(null));
    assertNull("Empty parameter shouldn't find anything", persistence.findByEmail(""));
    assertNull("Wrong address shouldn't find anything", persistence.findByEmail("foo@test.com"));
    assertEquals("The right address should find the correct user", "Philipp", persistence
	.findByEmail("pk@test.com").getFirstname());
    assertEquals("Upper and lowercase are ignored", "Philipp",
		 persistence.findByEmail("PK@test.com").getFirstname());
  }

  /**
   * Check the built in query functions, which also matches lower- and upper-case.
   */
  @Test
  public void findCompanyByCountry() {
    AddressEntity address = new AddressEntity().setStreet("Street").setZip("1234").setCity("London")
        .setCountry("UK").setAddressType(AddressType.WORK);
    BankConnectionEntity bankConnection = new BankConnectionEntity().setAccountNumber("1234567890")
        .setBankCode("11111");
    CompanyEntity company = new CompanyEntity().setName("Test company");
    company.setAddress(address);
    company.setBankConnection(bankConnection);
    persistence.persistCompanyEntity(company);
    assertEquals("Find the UK companies", 1, persistence.findCompanyByCountry("UK").size());
    assertEquals("Equals is case sensitive, use regular expression when needed", 0, persistence
	.findCompanyByCountry("uk").size());
    assertEquals("Find the US companies", 0, persistence.findCompanyByCountry("US").size());
  }

  /**
   * Do a query on multiple criteria, both for filter and fluent interface.
   */
  @Test
  public void findBySalary() {
    WorkerEntity worker1 = new WorkerEntity().setFirstname("Philipp").setEmail("pk@test.com")
        .setSalary(new BigDecimal("2500"));
    persistence.persistWorkerEntity(worker1);
    WorkerEntity worker2 = new WorkerEntity().setFirstname("Jane").setEmail("jd@test.com")
        .setSalary(new BigDecimal("2800.50"));
    persistence.persistWorkerEntity(worker2);
    assertEquals("Invalid lower boundary should return an empty list", 0, persistence
	.findBySalary(new BigDecimal("-1"), new BigDecimal("100")).size());
    assertEquals("Mixed up boundaries should return an empty list", 0, persistence
	.findBySalary(new BigDecimal("500"), new BigDecimal("100")).size());
    assertEquals("Range outside the available values", 0,
		 persistence.findBySalary(new BigDecimal("100"), new BigDecimal("2499.99")).size());
    assertEquals("No lower boundary and right inside the upper one", 1, persistence
	.findBySalary(null, new BigDecimal("2500")).size());
    assertEquals("No upper boundary and right outside the lower one", 1, persistence
	.findBySalary(new BigDecimal("2500.1"), null).size());
    assertEquals("No boundaries at all should find every entry", 2,
		 persistence.findBySalary(null, null).size());
    assertEquals("Boundaries with both entries inside", 2,
		 persistence.findBySalary(new BigDecimal("2000"), new BigDecimal("3000")).size());
    assertEquals("Testing against the fluent interface",
		 persistence.findBySalary(new BigDecimal("-1"), new BigDecimal("2600")),
		 persistence.findBySalaryFluent(new BigDecimal("-1"), new BigDecimal("2600")));
  }

  /**
   * Test a query which relies on @Reference.
   */
  @Test
  public void findCompanyEmployees() {
    WorkerEntity worker1 = new WorkerEntity().setFirstname("Philipp").setEmail("pk@test.com");
    WorkerEntity worker2 = new WorkerEntity().setFirstname("Jane").setEmail("jd@test.com");
    ManagerEntity manager = new ManagerEntity().setFirstname("Mr").setSurname("Big");
    CompanyEntity company = new CompanyEntity().setName("The company");
    persistence.workingFor(worker1, company);
    persistence.persistWorkerEntity(worker2);
    assertEquals("One employee should be working for the company", 1, persistence
	.findCompanyEmployees(company.getName()).size());
    assertEquals("The worker's name should be Philipp", "Philipp", persistence
	.findCompanyEmployees(company.getName()).get(0).getFirstname());
    persistence.workingFor(worker2, company);
    persistence.workingFor(manager, company);
    assertEquals("Three employees should be working for the company", 3, persistence
	.findCompanyEmployees(company.getName()).size());
  }

  /**
   * Test if getAllManagers() only returns managers.
   */
  @Test
  public void getAllManagers() {
    WorkerEntity worker = new WorkerEntity();
    worker.setEmail("worker@test.com");
    persistence.persistWorkerEntity(worker);
    ManagerEntity manager = new ManagerEntity();
    manager.setEmail("manager@test.com");
    persistence.persistManagerEntity(manager);
    assertEquals(1, persistence.getAllManagers().size());
  }

  /**
   * Check the uniqueness constraint.
   */
  @Test
  public void uniqueness() {
    WorkerEntity worker1 = new WorkerEntity().setFirstname("Philipp").setSurname("Krenn")
        .setEmail("pk@test.com");
    persistence.persistWorkerEntity(worker1);
    try {
      WorkerEntity worker2 = new WorkerEntity().setFirstname("Paul").setSurname("Kaufmann")
          .setEmail("pk@test.com");
      persistence.persistWorkerEntity(worker2);
      fail("A duplicate key exception should be thrown");
    } catch (DuplicateKeyException e) {
    }
    assertEquals(
        "When adding a second user with the same email address, the first one should be preserved",
        "Philipp", persistence.getAllEmployees().get(0).getFirstname());
  }

  /**
   * Ensure optimistic locking is working.
   */
  @Test
  public void concurrency() {
    WorkerEntity worker1 = new WorkerEntity().setFirstname("Philipp").setSurname("Krenn");
    persistence.persistWorkerEntity(worker1);
    WorkerEntity worker2 = persistence.getAllWorkers().get(0);
    worker1.setFirstname("Paul");
    persistence.persistWorkerEntity(worker1);
    boolean collision = false;
    try {
      worker2.setSurname("Kaufmann");
      persistence.persistWorkerEntity(worker2);
    } catch (ConcurrentModificationException e) {
      collision = true;
    }
    assertEquals("When doing two concurrent edits, only the first should go through", "Paul",
		 persistence.getAllEmployees().get(0).getFirstname());
    assertTrue("A collision exception should have been thrown", collision);
  }

  /**
   * Test the difference between primitive and object variables.
   */
  @Test
  public void objectsPrimitives() {
    ManagerEntity manager1 = new ManagerEntity().setEmail("foo@test.com");
    persistence.persistManagerEntity(manager1);
    ManagerEntity manager2 = new ManagerEntity().setApproveFunds(true).setManagerCanApproveHires(
        true)
        .setEmail("bar@test.com");
    persistence.persistManagerEntity(manager2);
    assertNull("Objects can have null values", persistence.getAllManagers().get(0)
	.getApproveFunds());
    assertNotNull("Primitives can't have null values (always added to the database)",
		  persistence.getAllManagers().get(0).isManagerCanApproveHires());
    assertFalse("Primitives can't have null values (initialized to false)", persistence
	.getAllManagers().get(0).isManagerCanApproveHires());
  }

}
