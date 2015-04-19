package pl.aislib.test.fm.forms;

/**
 * Class for testing dynamic string fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldStringDynamicTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldStringDynamicTest(String name) throws Exception {
    super(name);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_string_dynamic_001";
  }


  // Test methods

  /**
   * field_001_: Dynamic field with all non-empty strings.
   *
   * Expected result:
   *   field_001_1 validated,
   *   field_001_2 validated,
   *   field_001_3 validated.
   */
  public void test001Ok() {
    form = getForm("form_string_dynamic_001");
    field = form.getField("field_001_");

    values.put("field_001_1", "a");
    values.put("field_001_3", "c");
    values.put("field_001_2", "b");

    assertTrue(MSG_003, validateForm(values));
    assertEquals(form.getValue("field_001_1"), "a");
    assertEquals(form.getValue("field_001_2"), "b");
    assertEquals(form.getValue("field_001_3"), "c");

    printValues();
  }

  /**
   * field_001_: Dynamic field with all empty strings.
   *
   * Expected result:
   *   field_001_1 not validated,
   *   field_001_2 not validated,
   *   field_001_3 not validated.
   */
  public void test002Bad() {
    form = getForm("form_string_dynamic_001");
    field = form.getField("field_001_");

    values.put("field_001_1", "");
    values.put("field_001_3", "");
    values.put("field_001_2", "");

    assertFalse(MSG_001, validateForm(values));

    printValues();
  }

  /**
   * field_001_: Dynamic field with non-empty and empty strings.
   *
   * Expected result:
   *   field_001_1 validated,
   *   field_001_2 not validated,
   *   field_001_3 validated.
   */
  public void test003Bad() {
    form = getForm("form_string_dynamic_001");
    field = form.getField("field_001_");

    values.put("field_001_1", "a");
    values.put("field_001_3", "c");
    values.put("field_001_2", "");

    assertFalse(MSG_001, validateForm(values));
    assertEquals(form.getValue("field_001_1"), "a");
    assertEquals(form.getValue("field_001_2"), null);
    assertEquals(form.getValue("field_001_3"), "c");

    printValues();
  }

  /**
   * field_002_: Dynamic field with all strings of length >= 3.
   *
   * Expected result:
   *   field_002_1 validated,
   *   field_002_2 validated,
   *   field_002_3 validated.
   */
  public void test004Ok() {
    form = getForm("form_string_dynamic_002");
    field = form.getField("field_002_");

    values.put("field_002_1", "abc");
    values.put("field_002_3", "abcde");
    values.put("field_002_2", "abcd");

    assertTrue(MSG_003, validateForm(values));
    assertEquals(form.getValue("field_002_1"), "abc");
    assertEquals(form.getValue("field_002_2"), "abcd");
    assertEquals(form.getValue("field_002_3"), "abcde");

    printValues();
  }

  /**
   * field_002_: Dynamic field with some strings of length less than 3.
   *
   * Expected result:
   *   field_002_1 validated,
   *   field_002_2 not validated,
   *   field_002_3 not validated.
   */
  public void test005Bad() {
    form = getForm("form_string_dynamic_002");
    field = form.getField("field_002_");

    values.put("field_002_1", "abc");
    values.put("field_002_3", "a");
    values.put("field_002_2", "ab");

    assertFalse(MSG_001, validateForm(values));
    assertEquals(form.getValue("field_002_1"), "abc");
    assertEquals(form.getValue("field_002_2"), null);
    assertEquals(form.getValue("field_002_3"), null);

    printValues();
  }

} // pl.aislib.test.fm.forms.FieldStringDynamicTest class
