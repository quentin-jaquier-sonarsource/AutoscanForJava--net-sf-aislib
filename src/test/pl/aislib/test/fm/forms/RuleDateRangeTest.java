package pl.aislib.test.fm.forms;


/**
 * @author Wojciech Swiatek, AIS.PL
 */
public class RuleDateRangeTest extends RuleTestAbstract {

  /**
   * @see FormTestAbstract#FormTestAbstract(String)
   */
  public RuleDateRangeTest(String name) throws Exception {
    super(name);
  }

  /**
   * @see pl.aislib.test.fm.forms.RuleTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_date_range_001";
  }

  /**
   * field_001: May 20, 2002.
   * field_002: June 10, 2003.
   */
  public void test001Ok() {
    form = getForm("form_date_range_001");

    values.put("field_001", "2002/05/20");
    values.put("field_002", "2003/06/10");

    if (!validateForm(values)) {
      fail(MSG_003);
    }

    printValues();
  }

  /**
   * field_001: Empty date.
   * field_002: June 10, 2003.
   */
  public void test002Bad() {
    form = getForm("form_date_range_001");

    values.put("field_001", "");
    values.put("field_002", "2003/06/10");

    if (validateForm(values)) {
      fail(MSG_001);
    }

    printValues();
  }

  /**
   * field_001_1: May 20, 2002.
   * field_002_1: June 10, 2003.
   * field_001_2: May 30, 2002.
   * field_002_2: June 20, 2003.
   */
  public void test003Ok() {
    form = getForm("form_date_range_002");

    values.put("field_001_1", "2002/05/20");
    values.put("field_002_1", "2003/06/10");
    values.put("field_001_2", "2002/05/30");
    values.put("field_002_2", "2003/06/20");

    if (!validateForm(values)) {
      fail(MSG_003);
    }

    printValues();
  }

} // RuleDateRangeTest class
