package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;
import pl.aislib.util.validators.Validator;


public class SimpleValidator extends Validator {

  private String property;


  public void setProperty(String property) {
    this.property = property;
  }


  protected void validateString(String value) throws ValidateException {
  }


  protected Object toObject(String value) throws ValidateException {
    return property + value;
  }

  protected void validateObject(Object value) throws ValidateException {

  }


}
