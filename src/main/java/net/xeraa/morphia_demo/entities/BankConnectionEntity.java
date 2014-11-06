package net.xeraa.morphia_demo.entities;

import org.mongodb.morphia.annotations.Embedded;

/**
 * An embedded BankConnectionEntity, which should not use @Id. Also creationDate and lastChange are
 * not required, as they are already in the container entity.
 */
@Embedded
public class BankConnectionEntity {

  private String accountNumber;
  private String bankCode;
  private String bic;
  private String iban;
  private String country;

  public BankConnectionEntity() {
    super();
  }

  public BankConnectionEntity(String name, String accountNumber, String bankCode, String bic,
			      String iban, String country) {
    this();
    this.accountNumber = accountNumber;
    this.bankCode = bankCode;
    this.bic = bic;
    this.iban = iban;
    this.country = country;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return "BankConnectionEntity [accountNumber=" + accountNumber + ", bankCode=" + bankCode
	   + ", bic=" + bic + ", iban=" + iban + ", country=" + country + "]";
  }

}
