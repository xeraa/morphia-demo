package net.xeraa.morphia_demo.entities;

import net.xeraa.morphia_demo.utilities.AESEncryptor;

import org.mongodb.morphia.annotations.Embedded;

import java.io.Serializable;

@Embedded
public class EncryptionEntity implements Serializable {

  protected String salt;

  protected String encryptedAttribute;

  public EncryptionEntity() {
    super();
    setSalt(AESEncryptor.getSalt());
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getEncryptedAttribute() {
    return encryptedAttribute;
  }

  public void setEncryptedAttribute(String encryptedAttribute) {
    this.encryptedAttribute = encryptedAttribute;
  }

}

