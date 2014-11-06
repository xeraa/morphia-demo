package net.xeraa.morphia_demo.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * The CompanyEntity, using @Indexed(unique=true), @Reference (list), and @Embedded.
 */
@Entity(value = "company", noClassnameStored = true)
public class CompanyEntity extends BaseEntity {

  @Indexed(unique = true)
  private String name;

  private List<String> telephone = new ArrayList<>();
  private List<String> fax = new ArrayList<>();
  private List<String> mobile = new ArrayList<>();
  private String email;
  private String web;

  @Reference
  private List<EmployeeEntity> employees = new ArrayList<>();

  @Embedded
  private BankConnectionEntity bankConnection;
  @Embedded
  private AddressEntity address;

  public CompanyEntity() {
    super();
  }

  public CompanyEntity(String name, List<String> telephone, List<String> fax,
		       List<String> mobile, String email, String web) {
    this();
    this.name = name;
    this.telephone = telephone;
    this.fax = fax;
    this.mobile = mobile;
    this.email = email;
    this.web = web;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getTelephone() {
    return telephone;
  }

  public void setTelephone(List<String> telephone) {
    this.telephone = telephone;
  }

  public List<String> getFax() {
    return fax;
  }

  public void setFax(List<String> fax) {
    this.fax = fax;
  }

  public List<String> getMobile() {
    return mobile;
  }

  public void setMobile(List<String> mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWeb() {
    return web;
  }

  public void setWeb(String web) {
    this.web = web;
  }

  public List<EmployeeEntity> getEmployees() {
    return employees;
  }

  public void setEmployees(List<EmployeeEntity> employees) {
    this.employees = employees;
  }

  public void addEmployee(EmployeeEntity employee) {
    if (!this.employees.contains(employee)) {
      this.employees.add(employee);
    }
  }

  public void removeEmployee(EmployeeEntity employee) {
    if (this.employees.contains(employee)) {
      this.employees.remove(employee);
    }
  }

  public BankConnectionEntity getBankConnection() {
    return bankConnection;
  }

  public void setBankConnection(BankConnectionEntity bankConnection) {
    this.bankConnection = bankConnection;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "CompanyEntity [id=" + id + ", name=" + name + ", telephone=" + telephone + ", fax="
	   + fax + ", mobile=" + mobile + ", email=" + email + ", web=" + web
	   + ", creationDate=" + creationDate + ", lastChange=" + lastChange + ", employees="
	   + employees + ", bankConnection=" + bankConnection + ", address=" + address + "]";
  }

}
