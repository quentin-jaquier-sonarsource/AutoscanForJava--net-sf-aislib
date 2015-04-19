package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing string fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldStringTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldStringTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_string";
  }


  // Test methods

  /**
   * field_001: Non-empty string, required.
   *
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");

    validateField("abcde");
  }

  /**
   * field_001: Empty string, required.
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
   * field_002: Empty string, not required.
   *
   * @throws ValidateException if test failed.
   */
  public void test003Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("");
  }

  /**
   * field_002: Non-empty string, not required.
   *
   * @throws ValidateException if test failed.
   */
  public void test004Ok() throws ValidateException {
    field = form.getField("field_002");

    validateField("abcde");
  }

  /**
   * field_003: Non-empty string.
   *
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    field = form.getField("field_003");

    validateField("abcde");
  }

  /**
   * field_003: Empty string.
   */
  public void test006Bad() {
    field = form.getField("field_003");

    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(1);
    }
  }

  /**
   * field_004: String of length 3.
   *
   * @throws ValidateException if test failed.
   */
  public void test007Ok() throws ValidateException {
    field = form.getField("field_004");

    validateField("abc");
  }

  /**
   * field_004: String of length greater than 3.
   *
   * @throws ValidateException if test failed.
   */
  public void test008Ok() throws ValidateException {
    field = form.getField("field_004");

    validateField("abcd");
  }

  /**
   * field_004: Non-empty string of length less than 3.
   */
  public void test009Bad() {
    field = form.getField("field_004");

    try {
      validateField("ab");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_005: String of length 3, containing allowed chars 'abc'.
   *
   * @throws ValidateException if test failed.
   */
  public void test010Ok() throws ValidateException {
    field = form.getField("field_005");

    validateField("abc");
  }

  /**
   * field_005: String of length greater than 3, containing allowed chars 'abc'.
   *
   * @throws ValidateException if test failed.
   */
  public void test011Ok() throws ValidateException {
    field = form.getField("field_005");

    validateField("abcbcba");
  }

  /**
   * field_005: Non-empty string of length greater than 3, containing unallowed character (different from 'abc').
   */
  public void test012Bad() {
    field = form.getField("field_005");

    try {
      validateField("abcd");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(3);
    }
  }

  /**
   * field_006: Allowed string.
   *
   * @throws ValidateException if test failed.
   */
  public void test013Ok() throws ValidateException {
    field = form.getField("field_006");

    validateField("aaa");
  }

  /**
   * field_006: Non-empty string of length >= 3, containing allowed chars 'abc', disallowed.
   */
  public void test014Bad() {
    field = form.getField("field_006");

    try {
      validateField("abcbcba");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(4);
    }
  }

  /**
   * field_007: String of length 3.
   *
   * @throws ValidateException if test failed.
   */
  public void test015Ok() throws ValidateException {
    field = form.getField("field_007");

    validateField("abc");
  }

  /**
   * field_007: Non-empty string of length less than 3.
   *
   * @throws ValidateException if test failed.
   */
  public void test016Ok() throws ValidateException {
    field = form.getField("field_007");

    validateField("ab");
  }

  /**
   * field_007: Non-empty string of length greater than 3.
   */
  public void test017Bad() {
    field = form.getField("field_007");

    try {
      validateField("abcd");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_008: String not containing disallowed chars 'def'.
   *
   * @throws ValidateException if test failed.
   */
  public void test018Ok() throws ValidateException {
    field = form.getField("field_008");

    validateField("abc");
  }

  /**
   * field_008: String containing unallowed chars 'def'.
   */
  public void test019Bad() {
    field = form.getField("field_008");

    try {
      validateField("abcd");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_009: Allowed string.
   *
   * @throws ValidateException if test failed.
   */
  public void test020Ok() throws ValidateException {
    field = form.getField("field_009");

    validateField("abc");
  }

  /**
   * field_009: Disallowed string.
   */
  public void test021Bad() {
    field = form.getField("field_009");

    try {
      validateField("def");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_010: Allowed string.
   *
   * @throws ValidateException if test failed.
   */
  public void test022Ok() throws ValidateException {
    field = form.getField("field_010");

    validateField("abc");
  }

  /**
   * field_010: Disallowed string.
   */
  public void test023Bad() {
    field = form.getField("field_010");

    try {
      validateField("abcde");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_011: Allowed string, ignore case.
   *
   * @throws ValidateException if test failed.
   */
  public void test024Ok() throws ValidateException {
    field = form.getField("field_011");

    validateField("aBcD");
  }

  /**
   * field_012: Strings matching given pattern.
   *
   * @throws ValidateException if test failed.
   */
  public void test025Ok() throws ValidateException {
    field = form.getField("field_012");

    validateField("ab");
    validateField("acb");
    validateField("acccb");
  }

  /**
   * field_012: String not matching given pattern.
   */
  public void test026Bad() {
    field = form.getField("field_012");

    try {
      validateField("abc");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

} // pl.aislib.test.fm.forms.FieldStringTest class
