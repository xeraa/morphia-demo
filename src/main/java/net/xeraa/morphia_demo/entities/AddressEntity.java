package net.xeraa.morphia_demo.entities;

import org.mongodb.morphia.annotations.Embedded;

/**
 * An embedded AddressEntity, which should not use @Id. Also creationDate and lastChange are not
 * required, as they are already in the container entity.
 */
@Embedded
public class AddressEntity {

  private String street;
  private String zip;
  private String city;
  private String country;
  private AddressType addressType;
  public AddressEntity() {
    super();
  }

  public AddressEntity(String street, String zip, String city, String country,
		       AddressType addressType) {
    this();
    this.street = street;
    this.zip = zip;
    this.city = city;
    this.country = country;
    this.addressType = addressType;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public AddressType getAddressType() {
    return addressType;
  }

  public void setAddressType(AddressType addressType) {
    this.addressType = addressType;
  }

  @Override
  public String toString() {
    return "AddressEntity [street=" + street + ", zip=" + zip + ", city=" + city + ", country="
	   + country + ", addressType=" + addressType + "]";
  }

  public static enum AddressType {
    HOME, WORK, HOMEANDWORK,
  }

}