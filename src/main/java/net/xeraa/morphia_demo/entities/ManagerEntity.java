package net.xeraa.morphia_demo.entities;

import org.mongodb.morphia.annotations.Serialized;

import java.math.BigDecimal;
import java.util.List;

/**
 * A concrete EmployeeEntity, showing the difference between object and primitive attributes. Using
 * @Transient.
 */
public class ManagerEntity extends EmployeeEntity {

  private Boolean approveFunds;
  private boolean approveHires;

  /**
   * You shouldn't use Double for money values, but BigDecimal instead. However, MongoDB doesn't
   * natively support that (yet), so we'll serialize the value in Java - done transparently.
   */
  @Serialized
  private BigDecimal bonus;

  public ManagerEntity() {
    super();
  }

  public ManagerEntity(String firstname, String surname, List<String> telephone,
		       List<String> fax, List<String> mobile, String email, BigDecimal salary,
		       BigDecimal bonus) {
    super(firstname, surname, telephone, fax, mobile, email, salary);
    this.setBonus(bonus);
  }

  public BigDecimal getBonus() {
    return bonus;
  }

  public void setBonus(BigDecimal bonus) {
    if (bonus != null) {
      this.bonus = bonus.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
  }

  public Boolean getApproveFunds() {
    return approveFunds;
  }

  public void setApproveFunds(Boolean approveFunds) {
    this.approveFunds = approveFunds;
  }

  public boolean isApproveHires() {
    return approveHires;
  }

  public void setApproveHires(boolean approveHires) {
    this.approveHires = approveHires;
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
