package pl.aislib.test.fm.forms;

/**
 * Class for testing complex fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
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

} // FieldComplexTest class
