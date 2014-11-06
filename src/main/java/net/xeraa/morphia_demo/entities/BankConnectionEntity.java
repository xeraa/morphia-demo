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

  public String getAccountNumber() {
    return accountNumber;
  }

  public BankConnectionEntity setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

  public String getBankCode() {
    return bankCode;
  }

  public BankConnectionEntity setBankCode(String bankCode) {
    this.bankCode = bankCode;
    return this;
  }

  public String getBic() {
    return bic;
  }

  public BankConnectionEntity setBic(String bic) {
    this.bic = bic;
    return this;
  }

  public String getIban() {
    return iban;
  }

  public BankConnectionEntity setIban(String iban) {
    this.iban = iban;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public BankConnectionEntity setCountry(String country) {
    this.country = country;
    return this;
  }

  @Override
  public String toString() {
    return "BankConnectionEntity [accountNumber=" + accountNumber + ", bankCode=" + bankCode
	   + ", bic=" + bic + ", iban=" + iban + ", country=" + country + "]";
  }

}
