package net.xeraa.morphia_demo.persistence;

import net.xeraa.morphia_demo.entities.CompanyEntity;
import net.xeraa.morphia_demo.entities.EmployeeEntity;
import net.xeraa.morphia_demo.entities.ManagerEntity;
import net.xeraa.morphia_demo.entities.WorkerEntity;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;

/**
 * The general Persistence interface - in our specific case this will be implemented by
 * MongodbPersistence.
 */
public interface Persistence {

  ObjectId persistCompanyEntity(CompanyEntity company);

  ObjectId persistWorkerEntity(WorkerEntity worker);

  ObjectId persistManagerEntity(ManagerEntity manager);

  void clearData();

  List<CompanyEntity> getAllCompanies();

  List<EmployeeEntity> getAllEmployees();

  List<ManagerEntity> getAllManagers();

  List<WorkerEntity> getAllWorkers();

  EmployeeEntity findByEmail(String email);

  List<CompanyEntity> findCompanyByCountry(String country);

  List<EmployeeEntity> findBySalary(BigDecimal minimum, BigDecimal maximum);

  List<EmployeeEntity> findBySalaryFluent(BigDecimal minimum, BigDecimal maximum);

  List<EmployeeEntity> findCompanyEmployees(String companyName);

  void workingFor(EmployeeEntity employee, CompanyEntity company);

}
