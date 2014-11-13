package net.xeraa.morphia_demo.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * The CompanyEntity, using @Indexed, @Reference (list), and @Embedded.
 */
@Entity(value = "company", noClassnameStored = true)
public class CompanyEntity extends BaseEntity {

  @Indexed
  protected String name;

  protected List<String> telephone = new ArrayList<>();

  protected String email;

  protected String web;

  @Reference
  protected List<EmployeeEntity> employees = new ArrayList<>();

  @Embedded
  protected BankConnectionEntity bankConnection;
  @Embedded
  protected AddressEntity address;

  public CompanyEntity() {
    super();
  }

  public String getName() {
    return name;
  }

  public CompanyEntity setName(String name) {
    this.name = name;
    return this;
  }

  public List<String> getTelephone() {
    return telephone;
  }

  public CompanyEntity setTelephone(List<String> telephone) {
    this.telephone = telephone;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public CompanyEntity setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getWeb() {
    return web;
  }

  public CompanyEntity setWeb(String web) {
    this.web = web;
    return this;
  }

  public List<EmployeeEntity> getEmployees() {
    return employees;
  }

  public CompanyEntity setEmployees(List<EmployeeEntity> employees) {
    this.employees = employees;
    return this;
  }

  public CompanyEntity addEmployee(EmployeeEntity employee) {
    if (!this.employees.contains(employee)) {
      this.employees.add(employee);
    }
    return this;
  }

  public CompanyEntity removeEmployee(EmployeeEntity employee) {
    if (this.employees.contains(employee)) {
      this.employees.remove(employee);
    }
    return this;
  }

  public BankConnectionEntity getBankConnection() {
    return bankConnection;
  }

  public CompanyEntity setBankConnection(BankConnectionEntity bankConnection) {
    this.bankConnection = bankConnection;
    return this;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public CompanyEntity setAddress(AddressEntity address) {
    this.address = address;
    return this;
  }

  @Override
  public String toString() {
    return "CompanyEntity [id=" + id + ", name=" + name + ", telephone=" + telephone + ", email=" +
           email + ", web=" + web + ", creationDate=" + creationDate + ", lastChange=" +
           lastChange + ", employees=" + employees + ", bankConnection=" + bankConnection +
           ", address=" + address + "]";
  }

}
