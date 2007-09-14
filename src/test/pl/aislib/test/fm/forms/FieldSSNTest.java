package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing Social Security Number (SSN) fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.6 $
 */
public class FieldSSNTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldSSNTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_ssn";
  }


  // Test methods

  /**
   * field_001: Valid SSN, normal format allowed.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("123-45-6789");
  }

  /**
   * field_001: Empty string, normal format allowed.
   */
  public void test002Bad() {
    field = form.getField("field_001");

    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(1);
    }
  }

  /**
   * field_001: Invalid SSN, normal format allowed.
   */
  public void test003Bad() {
    field = form.getField("field_001");

    try {
      validateField("1a");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_001: 000-00-0000, normal format allowed.
   */
  public void test004Bad() {
    field = form.getField("field_001");

    try {
      validateField("000-00-0000");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_002: Valid SSN, short format allowed.
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("123456789");
  }

  /**
   * field_002: Invalid SSN, short format allowed.
   */
  public void test006Bad() {
    field = form.getField("field_002");

    try {
      validateField("123-45-6789");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_002: 000000000, short format allowed.
   */
  public void test007Bad() {
    field = form.getField("field_002");

    try {
      validateField("000000000");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_003: Valid SSN in normal format, normal and short formats allowed.
   * @throws ValidateException if test failed.
   */
  public void test008Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField("123-45-6789");
  }

  /**
   * field_003: Valid SSN in short format, normal and short formats allowed.
   * @throws ValidateException if test failed.
   */
  public void test009Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField("123456789");
  }

  /**
   * field_003: Invalid SSN, normal and short formats allowed.
   */
  public void test010Bad() {
    field = form.getField("field_003");

    try {
      validateField("1a");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_003: 000-00-0000, normal and short formats allowed.
   */
  public void test011Bad() {
    field = form.getField("field_003");

    try {
      validateField("000-00-0000");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_003: 000000000, normal and short formats allowed.
   */
  public void test012Bad() {
    field = form.getField("field_003");

    try {
      validateField("000000000");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_001: valid normal SSN.
   * field_002: valid short SSN.
   * field_003: invalid short SSN, short formats allowed.
   *
   * Expected result:
   *   field_001: validated,
   *   field_002: validated,
   *   field_003: not validated.
   */
  public void test013Bad() {
    field = form.getField("field_003");

    values.put("field_001", "123-45-6789");
    values.put("field_002", "123456789");
    values.put("field_003", "12345678901");

    assertFalse(MSG_001, validateForm(values));

    printValues();
  }

} // FieldSSNTest class
