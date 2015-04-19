package pl.aislib.test.util;

import pl.aislib.util.AbstractTransactionalBean;

/**
 * @author Tomasz Pik, AIS.PL
 */
public class TransactionalBean extends AbstractTransactionalBean {

  private String name;
  private Integer age;

  public void setName(String name) {
    firePropertyChange("name", getName(), name);
    this.name = name;
  }

  public void setAge(Integer age) {
    firePropertyChange("age", getAge(), age);
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }
}
