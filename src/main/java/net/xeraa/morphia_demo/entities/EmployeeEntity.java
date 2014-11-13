package net.xeraa.morphia_demo.entities;


import org.mongodb.morphia.annotations.AlsoLoad;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The (base) EmployeeEntity, so we don't have to duplicate code for full blown entities. It's using
 * @Indexed, @Indexed(unique=true), @Reference, @Transient, and @Embedded. Additionally making use
 * of @PrePersist and @PostLoad.
 * The EmployeeType is only required to have chainable setters in subclasses.
 */
@Entity(value = "employee", noClassnameStored = false)
@Indexes(@Index(name = "name", value = "surname, firstname"))
public class EmployeeEntity <EmployeeType extends EmployeeEntity> extends BaseEntity {

  protected String firstname;

  @AlsoLoad("lastname")
  protected String surname;

  @Indexed(unique = true)
  protected String email;

  @Transient
  protected String secret;
  @Embedded
  protected EncryptionEntity secretEncrypted;

  protected List<String> telephone = new ArrayList<>();

  /**
   * You shouldn't use Double for money values, but BigDecimal instead. However, MongoDB doesn't
   * natively support that (yet), so we'll use Strings in MongoDB. Be careful with the conversions,
   * both here and in the persistence.
   */
  @Transient
  protected BigDecimal salary;
  protected String salaryString;

  @Reference
  protected CompanyEntity company;

  @Embedded
  protected BankConnectionEntity bankConnection;
  @Embedded
  protected AddressEntity address;

  public EmployeeEntity() {
    super();
  }

  public String getFirstname() {
    return firstname;
  }

  public EmployeeType setFirstname(String firstname) {
    this.firstname = firstname;
    return (EmployeeType) this;
  }

  public String getSurname() {
    return surname;
  }

  public EmployeeType setSurname(String surname) {
    this.surname = surname;
    return (EmployeeType) this;
  }

  public List<String> getTelephone() {
    return telephone;
  }

  public EmployeeType setTelephone(List<String> telephone) {
    this.telephone = telephone;
    return (EmployeeType) this;
  }

  public String getEmail() {
    return email;
  }

  public EmployeeType setEmail(String email) {
    this.email = email;
    return (EmployeeType) this;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public EmployeeType setSalary(BigDecimal salary) {
    if (salary != null) {
      this.salary = salary.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    return (EmployeeType) this;
  }

  public CompanyEntity getCompany() {
    return company;
  }

  public EmployeeType setCompany(CompanyEntity company) {
    this.company = company;
    return (EmployeeType) this;
  }

  public BankConnectionEntity getBankConnection() {
    return bankConnection;
  }

  public EmployeeType setBankConnection(BankConnectionEntity bankConnection) {
    this.bankConnection = bankConnection;
    return (EmployeeType) this;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public EmployeeType setAddress(AddressEntity address) {
    this.address = address;
    return (EmployeeType) this;
  }

  @PrePersist
  public void prePersist() {
    super.prePersist();
    if (salary != null) {
      this.salaryString = this.salary.toString();
    }
  }

  @PostLoad
  public void postLoad() {
    if (salaryString != null) {
      this.salary = new BigDecimal(salaryString);
    } else {
      this.salary = null;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EmployeeEntity that = (EmployeeEntity) o;

    return !(email != null ? !email.equals(that.email) : that.email != null);
  }

  @Override
  public int hashCode() {
    return email != null ? email.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "EmployeeEntity [id=" + id + ", firstname=" + firstname + ", surname=" + surname
	   + ", telephone=" + telephone + ", email=" + email + ", salary=" + salary +
           ", creationDate=" + creationDate + ", lastChange=" + lastChange + ", company=" +
           company.getId() + ", bankConnection=" + bankConnection + ", address=" + address + "]";
  }

}
