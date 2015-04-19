package pl.aislib.test.fm.forms;

/**
 * Class for testing complex fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldComplexTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldComplexTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_complex_date_001";
  }


  // Test methods

  /**
   * field_001: Valid day integer.
   * field_002: Valid month integer.
   * field_003: Valid day integer.
   * field_004 should be valid date.
   */
  public void test001Ok() {
    form = getForm("form_complex_date_001");

    values.put("field_001", "30");
    values.put("field_002", "6");
    values.put("field_003", "2003");

    if (!validateForm(values)) {
      fail(MSG_003);
    }

    printValues();
  }

  /**
   * field_001: Empty string.
   * field_002: Valid month integer.
   * field_003: Valid day integer.
   * field_004 should not be validated.
   */
  public void test002Ok() {
    form = getForm("form_complex_date_001");

    values.put("field_001", "");
    values.put("field_002", "6");
    values.put("field_003", "2003");

    if (validateForm(values)) {
      fail(MSG_001);
    }

    printValues();
  }

  /**
   * field_001, field_002, field_003: valid parts of SSN number.
   * field_004 should be SSN number.
   */
  public void test003Ok() {
    form = getForm("form_complex_string_001");

    values.put("field_001", "111");
    values.put("field_002", "11");
    values.put("field_003", "1111");

    if (!validateForm(values)) {
      fail(MSG_003);
    }

    printValues();
  }

  /**
   * field_001, field_002, field_003: do not form proper SSN number.
   * field_004 should not be validated.
   */
  public void test004Bad() {
    form = getForm("form_complex_string_001");

    values.put("field_001", "000");
    values.put("field_002", "00");
    values.put("field_003", "0000");

    if (validateForm(values)) {
      fail(MSG_001);
    }

    printValues();
  }

} // FieldComplexTest class
