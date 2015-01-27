package net.xeraa.morphia_demo.persistence;

import net.xeraa.morphia_demo.config.MongoDB;
import net.xeraa.morphia_demo.entities.CompanyEntity;
import net.xeraa.morphia_demo.entities.EmployeeEntity;
import net.xeraa.morphia_demo.entities.ManagerEntity;
import net.xeraa.morphia_demo.entities.WorkerEntity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The Persistence implementation, showing how to do an upsert, various queries, clearing the
 * database,...
 */
public class MongodbPersistence {

  private final Datastore mongoDatastore;

  public MongodbPersistence() {
    mongoDatastore = MongoDB.instance().getDatabase();
  }

  public ObjectId persistCompanyEntity(CompanyEntity company) {
    mongoDatastore.save(company);
    return company.getId();
  }

  public ObjectId persistManagerEntity(ManagerEntity manager) {
    mongoDatastore.save(manager);
    return manager.getId();
  }

  public ObjectId persistWorkerEntity(WorkerEntity worker) {
    mongoDatastore.save(worker);
    return worker.getId();
  }

  public void clearData() {
    mongoDatastore.delete(mongoDatastore.createQuery(CompanyEntity.class));
    mongoDatastore.delete(mongoDatastore.createQuery(ManagerEntity.class));
    mongoDatastore.delete(mongoDatastore.createQuery(WorkerEntity.class));
  }

  public List<CompanyEntity> getAllCompanies() {
    return mongoDatastore.createQuery(CompanyEntity.class).asList();
  }

  public List<EmployeeEntity> getAllEmployees() {
    return mongoDatastore.createQuery(EmployeeEntity.class).asList();
  }

  public List<ManagerEntity> getAllManagers() {
    return mongoDatastore.createQuery(ManagerEntity.class)
	// Disable validation when querying an implicit attribute to avoid warnings
	.disableValidation()
	.field("className").equal(ManagerEntity.class.getName()).asList();
  }

  public List<WorkerEntity> getAllWorkers() {
    return mongoDatastore.createQuery(WorkerEntity.class)
	// Disable validation when querying an implicit attribute to avoid warnings
	.disableValidation()
	.field("className").equal(WorkerEntity.class.getName()).asList();
  }

  public EmployeeEntity findByEmail(final String email) {
    if ((email == null) || email.isEmpty()) {
      return null;
    }
    // Don't do this in production, this is slow
    Pattern regexp = Pattern.compile("^" + email + "$", Pattern.CASE_INSENSITIVE);
    return mongoDatastore.find(EmployeeEntity.class).filter("email", regexp).get();
  }

  public List<CompanyEntity> findCompanyByCountry(final String country) {
    if ((country == null) || country.isEmpty()) {
      return new ArrayList<>();
    }
    return mongoDatastore.createQuery(CompanyEntity.class).filter("address.country =", country)
	.asList();
  }

  public List<EmployeeEntity> findBySalary(final BigDecimal minimum, final BigDecimal maximum) {

    /**
     * Check that the minimum is not negative and that the maximum is not
     * smaller than the minimum.
     */
    if (((minimum != null) && (minimum.compareTo(BigDecimal.ZERO) < 0))
	|| ((minimum != null) && (maximum != null) && (maximum.compareTo(minimum) < 0))) {
      return new ArrayList<>();
    }
    Query<EmployeeEntity> query = mongoDatastore.find(EmployeeEntity.class);
    if (minimum != null) {
      BigDecimal minimumScale = minimum.setScale(2, BigDecimal.ROUND_HALF_UP);
      query.filter("salaryString >=", minimumScale.toPlainString());
    }
    if (maximum != null) {
      BigDecimal maximumScale = maximum.setScale(2, BigDecimal.ROUND_HALF_UP);
      query.filter("salaryString <=", maximumScale.toPlainString());
    }
    return query.asList();
  }

  public List<EmployeeEntity> findBySalaryFluent(final BigDecimal minimum,
						 final BigDecimal maximum) {
    if (((minimum != null) && (minimum.compareTo(BigDecimal.ZERO) < 0))
	|| ((minimum != null) && (maximum != null) && (maximum.compareTo(minimum) < 0))) {
      return new ArrayList<>();
    }
    Query<EmployeeEntity> query = mongoDatastore.find(EmployeeEntity.class);
    if (minimum != null) {
      BigDecimal minimumScale = minimum.setScale(2, BigDecimal.ROUND_HALF_UP);
      query.field("salary").greaterThanOrEq(minimumScale.toPlainString());
    }
    if (maximum != null) {
      BigDecimal maximumScale = maximum.setScale(2, BigDecimal.ROUND_HALF_UP);
      query.field("salary").lessThanOrEq(maximumScale.toPlainString());
    }
    return query.asList();
  }

  public List<EmployeeEntity> findCompanyEmployees(final String companyName) {
    if ((companyName == null) || companyName.isEmpty()) {
      return new ArrayList<>();
    }
    CompanyEntity company = mongoDatastore.find(CompanyEntity.class).field("name")
	.equal(companyName).get();
    return mongoDatastore.find(EmployeeEntity.class).filter("company exists", company).asList();
  }

  public void workingFor(EmployeeEntity employee, CompanyEntity company) {
    if (employee.getId() == null) {
      mongoDatastore.save(employee);
    }
    if (company.getId() == null) {
      persistCompanyEntity(company);
    }
    employee.setCompany(company);
    mongoDatastore.save(employee);
    company.addEmployee(employee);
    persistCompanyEntity(company);
  }

}
