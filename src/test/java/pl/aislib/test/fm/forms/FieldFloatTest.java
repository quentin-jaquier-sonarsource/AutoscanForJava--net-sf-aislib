package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing float number fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
 */
public class FieldFloatTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldFloatTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_float";
  }


  // Test methods

  /**
   * field_001: Zero float.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("0");
  }

  /**
   * field_001: Float with fraction.
   * @throws ValidateException if test failed.
   */
  public void test002Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("123.45");
  }

  /**
   * field_001: Negative float.
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("-10");
  }

  /**
   * field_001: Float without thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test004Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("1234.56");
  }

  /**
   * field_001: Float with thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("1,234.56");
  }

  /**
   * field_001: Float as empty string.
   */
  public void test006Bad() {
    field = form.getField("field_001");

    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(1);
    }
  }

  /**
   * field_001: Invalid float.
   */
  public void test007Bad() {
    field = form.getField("field_001");

    try {
      validateField("1a");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(2);
    }
  }

  /**
   * field_002: Float within the range.
   * @throws ValidateException if test failed.
   */
  public void test008Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("0");
  }

  /**
   * field_002: Start range float.
   * @throws ValidateException if test failed.
   */
  public void test009Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("-2");
  }

  /**
   * field_002: End range float.
   * @throws ValidateException if test failed.
   */
  public void test010Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("3.5");
  }

  /**
   * field_002: Float less than start range.
   */
  public void test011Bad() {
    field = form.getField("field_002");

    try {
      validateField("-2.5");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(3);
    }
  }

  /**
   * field_002: Float greater than end range.
   */
  public void test012Bad() {
    field = form.getField("field_002");

    try {
      validateField("4");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(4);
    }
  }

} // FieldFloatTest class
