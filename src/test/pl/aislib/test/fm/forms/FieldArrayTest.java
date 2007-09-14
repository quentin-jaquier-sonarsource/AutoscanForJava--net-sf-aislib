package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing array fields.
 *
 * @author Wojciech Swiatek<, AIS.PL
 * @version $Revision: 1.5 $
 */
public class FieldArrayTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldArrayTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_array";
  }


  // Test methods

  /**
   * field_001: Non-empty array, required.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField(new String[] { "a" });
  }

  /**
   * field_001: Empty array, required.
   */
  public void test002Bad() {
    field = form.getField("field_001");

    try {
      validateField(new String[0]);
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(1);
    }
  }

  /**
   * field_002: Empty array, not required.
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField(new String[0]);
  }

  /**
   * field_002: Non-empty array, not required.
   * @throws ValidateException if test failed.
   */
  public void test004Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField(new String[] { "a" });
  }

  /**
   * field_003: Array of length 3.
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField(new String[] { "a", "b", "c" });
  }

  /**
   * field_003: Array of length 5.
   * @throws ValidateException if test failed.
   */
  public void test006Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField(new String[] { "a", "b", "c", "d", "e" });
  }

  /**
   * field_003: Array of length between 3 and 5.
   * @throws ValidateException if test failed.
   */
  public void test007Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField(new String[] { "a", "b", "c", "d" });
  }

  /**
   * field_003: Array of length less than 3.
   */
  public void test008Bad() {
    field = form.getField("field_003");

    try {
      validateField(new String[] { "a", "b" });
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_003: Array of length greater than 5.
   */
  public void test009Bad() {
    field = form.getField("field_003");

    try {
      validateField(new String[] { "a", "b", "c", "d", "e", "f" });
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

} // FieldArrayTest class
