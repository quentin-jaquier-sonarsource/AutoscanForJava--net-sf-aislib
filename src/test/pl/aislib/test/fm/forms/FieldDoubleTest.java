package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing double number fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.5 $
 */
public class FieldDoubleTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldDoubleTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_double";
  }


  // Test methods

  /**
   * field_001: Zero double.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("0");
  }

  /**
   * field_001_locale: Zero double.
   * @throws ValidateException if test failed.
   */
  public void test001LocaleOk() throws ValidateException {
    field = form.getField("field_001_locale");

    validateField("0");
  }

  /**
   * field_001: Double with fraction.
   * @throws ValidateException if test failed.
   */
  public void test002Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("123.45");
  }

  /**
   * field_001_locale: Double with fraction.
   * @throws ValidateException if test failed.
   */
  public void test002LocaleOk() throws ValidateException {
    field = form.getField("field_001_locale");

    validateField("123,45");
  }

  /**
   * field_001: Negative double.
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("-10");
  }

  /**
   * field_001_locale: Negative double.
   * @throws ValidateException if test failed.
   */
  public void test003LocaleOk() throws ValidateException {
    field = form.getField("field_001_locale");

    validateField("-10");
  }

  /**
   * field_001: Double without thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test004Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("1234.56");
  }

  /**
   * field_001_locale: Double without thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test004LocaleOk() throws ValidateException {
    field = form.getField("field_001_locale");

    validateField("1234,56");
  }

  /**
   * field_001: Double with thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("1,234.56");
  }

  /**
   * field_001_locale: Double with thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test005LocaleOk() throws ValidateException {
    field = form.getField("field_001_locale");

    validateField("1 234,56");
  }

  /**
   * field_001: Double as empty string.
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
   * field_001_locale: Double as empty string.
   */
  public void test006LocaleBad() {
    field = form.getField("field_001_locale");

    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(1);
    }
  }

  /**
   * field_001: Invalid double.
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
   * field_001_locale: Invalid double.
   */
  public void test007LocaleBad() {
    field = form.getField("field_001_locale");

    try {
      validateField("1a");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(2);
    }
  }

  /**
   * field_002: Double within the range.
   * @throws ValidateException if test failed.
   */
  public void test008Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("0");
  }

  /**
   * field_002_locale: Double within the range.
   * @throws ValidateException if test failed.
   */
  public void test008LocaleOk() throws ValidateException {
    field = form.getField("field_002_locale");

    validateField("0");
  }

  /**
   * field_002: Start range double.
   * @throws ValidateException if test failed.
   */
  public void test009Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("-2");
  }

  /**
   * field_002_locale: Start range double.
   * @throws ValidateException if test failed.
   */
  public void test009LocaleOk() throws ValidateException {
    field = form.getField("field_002_locale");

    validateField("-2");
  }

  /**
   * field_002: End range double.
   * @throws ValidateException if test failed.
   */
  public void test010Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("3.5");
  }

  /**
   * field_002_locale: End range double.
   * @throws ValidateException if test failed.
   */
  public void test010LocaleOk() throws ValidateException {
    field = form.getField("field_002_locale");

    validateField("3,5");
  }

  /**
   * field_002: Double less than start range.
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
   * field_002_locale: Double less than start range.
   */
  public void test011LocaleBad() {
    field = form.getField("field_002_locale");

    try {
      validateField("-2,5");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(3);
    }
  }

  /**
   * field_002: Double greater than end range.
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

  /**
   * field_002_locale: Double greater than end range.
   */
  public void test012LocaleBad() {
    field = form.getField("field_002_locale");

    try {
      validateField("4");
      fail(MSG_001);
    } catch (ValidateException e) {
      checkErrorCode(4);
    }
  }

} // FieldDoubleTest class
