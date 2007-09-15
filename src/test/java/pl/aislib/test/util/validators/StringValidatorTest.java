package pl.aislib.test.util.validators;

import junit.framework.TestCase;

import pl.aislib.fm.forms.ValidateException;
import pl.aislib.util.validators.StringValidator;

import pl.aislib.test.fm.forms.IConstants;

/**
 * @author Wojciech Swiatek, AIS.PL
 */
public class StringValidatorTest extends TestCase
  implements IConstants {

  private StringValidator validator;

  /**
   * @see TestCase#TestCase
   */
  public StringValidatorTest(String name) {
    super(name);
  }

  /**
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();

    validator = new StringValidator();
  }


  /**
   * @throws ValidateException if test failed.
   */  
  public void test001Ok() throws ValidateException {
    validator.setRequired(true);
    validator.validate("a");
  }
  
  /**
   * 
   */
  public void test002Bad() {
    validator.setRequired(true);
    try {
      validator.validate("");
      fail(MSG_001);
    } catch (ValidateException e) {
      ;
    }
  }
  
  /**
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    validator.setRequired(false);
    validator.setMaximumLength(12);
    validator.validate("");
    validator.validate("a");
    validator.validate("abcdefghijkl");
  }
  
  /**
   * 
   */
  public void test004Bad() {
    validator.setRequired(false);
    validator.setMaximumLength(12);
    try {
      validator.validate("abcdefghijklm");
      fail(MSG_001);
    } catch (ValidateException e) {
      ;
    }
  }

  /**
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    validator.setRequired(false);
    validator.setMaximumLength(12);
    validator.setMinimumLength(5);
    validator.validate("abcdef");
    validator.validate("abcde");
    validator.validate("abcdefghijkl");
  }

  /**
   * 
   */  
  public void test006Bad() {
    validator.setRequired(false);
    validator.setMaximumLength(12);
    validator.setMinimumLength(5);
    String[] values = { "", "abcd", "abcdefghijklm" };

    boolean passed = true;
    for (int i = 0; i < values.length; i++) {
      try {
        validator.validate(values[i]);
        passed = false;
      } catch (ValidateException e) {
        ;
      }
    }

    if (passed) {
      fail(MSG_001);
    }
  }

}
