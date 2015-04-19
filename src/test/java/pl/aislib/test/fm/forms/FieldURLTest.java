package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing url fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldURLTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldURLTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_url";
  }


  // Test methods

  /**
   * field_001: Empty string, no URL allowed.
   */
  public void test001Bad() {
    field = form.getField("field_001");

    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(1);
    }
  }

  /**
   * field_001: Invalid URL, no URL allowed.
   */
  public void test002Bad() {
    field = form.getField("field_001");

    try {
      validateField("1a");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_001: Valid URL, no URL allowed.
   */
  public void test003Bad() {
    field = form.getField("field_001");

    try {
      validateField("http://www.ais.pl");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_002: Valid http URL, http URLs allowed.
   * @throws ValidateException if test failed.
   */
  public void test004Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("http://www.ais.pl");
  }

  /**
   * field_002: Valid non-http URL, http URLs allowed.
   */
  public void test005Bad() {
    field = form.getField("field_002");

    try {
      validateField("https://secure.ais.pl");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_003: Valid https URL, http, https, ftp, file URLs allowed.
   * @throws ValidateException if test failed.
   */
  public void test006Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField("https://secure.ais.pl");
  }

  /**
   * field_003: Valid ftp URL, http, https, ftp, file URLs allowed.
   * @throws ValidateException if test failed.
   */
  public void test007Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField("ftp://ftp.ais.pl");
  }

  /**
   * field_003: Valid file URL, http, https, ftp, file URLs allowed.
   * @throws ValidateException if test failed.
   */
  public void test008Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField("file:///ais/index.html");
  }

} // FieldURLTest class
