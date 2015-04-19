package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing long integer number fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldLongTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldLongTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_long";
  }


  // Test methods

  /**
   * field_001: Zero long.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("0");
  }

  /**
   * field_001: Positive long.
   * @throws ValidateException if test failed.
   */
  public void test002Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("123");
  }

  /**
   * field_001: Negative long.
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("-10");
  }

  /**
   * field_001: Long as empty string.
   */
  public void test004Bad() {
    field = form.getField("field_001");

    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(1);
    }
  }

  /**
   * field_001: Invalid long.
   */
  public void test005Bad() {
    field = form.getField("field_001");

    try {
      validateField("1.5");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(2);
    }
  }

  /**
   * field_002: Long within the range.
   * @throws ValidateException if test failed.
   */
  public void test006Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("0");
  }

  /**
   * field_002: Start range long.
   * @throws ValidateException if test failed.
   */
  public void test007Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("-2");
  }

  /**
   * field_002: End range long.
   * @throws ValidateException if test failed.
   */
  public void test008Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("3");
  }

  /**
   * field_002: Long less than start range.
   */
  public void test009Bad() {
    field = form.getField("field_002");

    try {
      validateField("-3");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(3);
    }
  }

  /**
   * field_002: Long greater than end range.
   */
  public void test010Bad() {
    field = form.getField("field_002");

    try {
      validateField("4");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(4);
    }
  }

} // FieldLongTest class
