package net.xeraa.morphia_demo.entities;

import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Serialized;

import java.math.BigDecimal;

/**
 * A concrete EmployeeEntity, showing the difference between object and primitive attributes. Using
 * @Transient.
 */
public class ManagerEntity extends EmployeeEntity<ManagerEntity> {

  protected Boolean approveFunds;

  @Property("hire")
  protected boolean managerCanApproveHires;

  /**
   * You shouldn't use Double for money values, but BigDecimal instead. However, MongoDB doesn't
   * natively support that (yet), so we'll serialize the value in Java - done transparently.
   */
  @Serialized
  protected BigDecimal bonus;

  public ManagerEntity() {
    super();
  }

  public BigDecimal getBonus() {
    return bonus;
  }

  public ManagerEntity setBonus(BigDecimal bonus) {
    if (bonus != null) {
      this.bonus = bonus.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    return this;
  }

  public Boolean getApproveFunds() {
    return approveFunds;
  }

  public ManagerEntity setApproveFunds(Boolean approveFunds) {
    this.approveFunds = approveFunds;
    return this;
  }

  public boolean isManagerCanApproveHires() {
    return managerCanApproveHires;
  }

  public ManagerEntity setManagerCanApproveHires(boolean managerCanApproveHires) {
    this.managerCanApproveHires = managerCanApproveHires;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    // Doing the same thing as the base method, just for added clarity
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
