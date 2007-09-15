package pl.aislib.test.fm.forms;

import java.util.HashMap;
import java.util.Map;

import pl.aislib.fm.forms.FieldBuilder;

public class SimpleBuilder extends FieldBuilder {

  private String property;

  public SimpleBuilder() {
    super();
  }

  public String join(Map values) {
    return property;
  }

  public Map split(Object value) {
    return new HashMap();
  }

  public void setProperty(String property) {
    this.property = property;
  }
}
