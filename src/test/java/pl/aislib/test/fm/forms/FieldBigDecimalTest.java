package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing <code>BigDecimal</code> fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldBigDecimalTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldBigDecimalTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_big_decimal";
  }


  // Test methods

  /**
   * field_001: Zero big decimal.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("0");
  }

  /**
   * field_001: Big decimal with fraction.
   * @throws ValidateException if test failed.
   */
  public void test002Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("123.45");
  }

  /**
   * field_001: Negative big decimal.
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("-10");
  }

  /**
   * field_001: Big decimal without thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test004Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("1234.56");
  }

  /**
   * field_001: Big decimal with thousand separator.
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("1,234.56");
  }

  /**
   * field_001: Big decimal as empty string.
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
   * field_001: Invalid big decimal.
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
   * field_002: Big decimal within the range.
   * @throws ValidateException if test failed.
   */
  public void test008Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("0");
  }

  /**
   * field_002: Start range big decimal.
   * @throws ValidateException if test failed.
   */
  public void test009Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("-2");
  }

  /**
   * field_002: End range big decimal.
   * @throws ValidateException if test failed.
   */
  public void test010Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("3.5");
  }

  /**
   * field_002: Big decimal less than start range.
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
   * field_002: Big decimal greater than end range.
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
   * Using ICU4J.
   *
   * field_003: Big decimal with fraction.
   * @throws ValidateException if test failed.
   */
  public void test013Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("1234354353432432432434242454365567890.12");
  }

} // pl.aislib.test.fm.forms.FieldBigDecimalTest class
