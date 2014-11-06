package net.xeraa.morphia_demo.entities;

import net.xeraa.morphia_demo.persistence.BigDecimalConverter;

import org.mongodb.morphia.annotations.Converters;

import java.math.BigDecimal;

/**
 * A concrete EmployeeEntity.
 */
@Converters({BigDecimalConverter.class})
public class WorkerEntity extends EmployeeEntity<WorkerEntity> {

  private Integer yearsExperience;
  private BigDecimal dailyAllowance;

  public WorkerEntity() {
    super();
  }

  public Integer getYearsExperience() {
    return yearsExperience;
  }

  public WorkerEntity setYearsExperience(Integer yearsExperience) {
    this.yearsExperience = yearsExperience;
    return this;
  }

  public BigDecimal getDailyAllowance() {
    return dailyAllowance;
  }

  public WorkerEntity setDailyAllowance(BigDecimal dailyAllowance) {
    if (dailyAllowance != null) {
      this.dailyAllowance = dailyAllowance.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
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
