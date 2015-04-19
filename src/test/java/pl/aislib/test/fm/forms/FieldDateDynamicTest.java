package pl.aislib.test.fm.forms;

/**
 * Class for testing dynamic date fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldDateDynamicTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldDateDynamicTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_date";
  }


  // Test methods

  /**
   * field_001_: Dynamic field with non-empty proper dates.
   */
  public void test001Ok() {
    form = getForm("form_date_dynamic_001");
    field = form.getField("field_001_");

    values.put("field_001_1", "2003-06-30");
    values.put("field_001_3", "2003-07-02");
    values.put("field_001_2", "2003-07-01");

    if (!validateForm(values)) {
      fail(MSG_003);
    }

    printValues();
  }

} // FieldDateDynamicTest class
