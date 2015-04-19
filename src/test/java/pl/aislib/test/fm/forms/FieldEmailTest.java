package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing email fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldEmailTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldEmailTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_email";
  }


  // Test methods

  /**
   * field_001: Valid email.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("john@ais.pl");
  }

  /**
   * field_001: Valid email.
   * @throws ValidateException if test failed.
   */
  public void test002Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("a.b@c.d");
  }

  /**
   * field_001: Valid email.
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("a@a.a");
  }

  /**
   * field_001: Empty string, required.
   */
  public void test004Bad() {
    field = form.getField("field_001");

    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(1);
    }
  }

  /**
   * field_001: String too short for email.
   */
  public void test005Bad() {
    field = form.getField("field_001");

    try {
      validateField("a");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_001: String starting with '@' character.
   */
  public void test006Bad() {
    field = form.getField("field_001");

    try {
      validateField("@a.b");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

} // FieldEmailTest class
