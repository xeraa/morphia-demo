package net.xeraa.morphia_demo.entities;

import net.xeraa.morphia_demo.types.AddressType;

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

  public String getStreet() {
    return street;
  }

  public AddressEntity setStreet(String street) {
    this.street = street;
    return this;
  }

  public String getZip() {
    return zip;
  }

  public AddressEntity setZip(String zip) {
    this.zip = zip;
    return this;
  }

  public String getCity() {
    return city;
  }

  public AddressEntity setCity(String city) {
    this.city = city;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public AddressEntity setCountry(String country) {
    this.country = country;
    return this;
  }

  public AddressType getAddressType() {
    return addressType;
  }

  public AddressEntity setAddressType(AddressType addressType) {
    this.addressType = addressType;
    return this;
  }

  @Override
  public String toString() {
    return "AddressEntity [street=" + street + ", zip=" + zip + ", city=" + city + ", country="
	   + country + ", addressType=" + addressType + "]";
  }

}