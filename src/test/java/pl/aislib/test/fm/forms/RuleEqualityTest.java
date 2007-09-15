package pl.aislib.test.fm.forms;


/**
 * @author Wojciech Swiatek, AIS.PL
 */
public class RuleEqualityTest extends RuleTestAbstract {

  /**
   * @see FormTestAbstract#FormTestAbstract(String)
   */
  public RuleEqualityTest(String name) throws Exception {
    super(name);
  }

  /**
   * @see pl.aislib.test.fm.forms.RuleTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_equality_001";
  }

  /**
   * field_001: non-empty string.
   * field_002: same as field_001.
   * rule_001:  unconditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 validated.
   */
  public void test001Ok() {
    form = getForm("form_equality_001");

    values.put("field_001", "a");
    values.put("field_002", "a");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001: non-empty string.
   * field_002: non-empty string, not equal to field_001.
   * rule_001:  unconditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 not validated.
   */
  public void test002Bad() {
    form = getForm("form_equality_001");

    values.put("field_001", "a");
    values.put("field_002", "b");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

  /**
   * field_001: empty string.
   * field_002: empty string.
   * rule_001:  unconditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 validated.
   */
  public void test003Ok() {
    form = getForm("form_equality_002");

    values.put("field_001", "");
    values.put("field_002", "");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001: empty string.
   * field_002: non-empty string.
   * rule_001:  unconditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 not validated.
   */
  public void test004Bad() {
    form = getForm("form_equality_002");

    values.put("field_001", "");
    values.put("field_002", "b");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

  /**
   * Test for conditionality of the rule.
   */
  public void test005Ok() {
    form = getForm("form_equality_003");

    values.put("field_001", "a");
    values.put("field_002", "b");
    values.put("check_fields", "n");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001: non-empty string.
   * field_002: same as field_001.
   * rule_001:  conditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 validated.
   */
  public void test006Ok() {
    form = getForm("form_equality_003");

    values.put("field_001", "a");
    values.put("field_002", "a");
    values.put("check_fields", "y");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001: non-empty string.
   * field_002: non-empty string, not equal to field_001.
   * rule_001:  conditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 not validated.
   */
  public void test007Bad() {
    form = getForm("form_equality_003");

    values.put("field_001", "a");
    values.put("field_002", "b");
    values.put("check_fields", "y");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

  /**
   * field_001: empty string.
   * field_002: empty string.
   * rule_001:  conditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 validated.
   */
  public void test008Ok() {
    form = getForm("form_equality_004");

    values.put("field_001", "");
    values.put("field_002", "");
    values.put("check_fields", "y");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001: empty string.
   * field_002: non-empty string.
   * rule_001:  conditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 not validated.
   */
  public void test009Bad() {
    form = getForm("form_equality_004");

    values.put("field_001", "");
    values.put("field_002", "b");
    values.put("check_fields", "y");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

  /**
   * field_001: non-empty integer.
   * field_002: same as field_001.
   * rule_001:  unconditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 validated.
   */
  public void test010Ok() {
    form = getForm("form_equality_001");

    values.put("field_001", "1");
    values.put("field_002", "1");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001: non-empty integer.
   * field_002: non-empty integer, not equal to field_001.
   * rule_001:  unconditional check of equality between field_001 and field_002.
   *
   * Expected result:
   *   rule_001 not validated.
   */
  public void test011Bad() {
    form = getForm("form_equality_001");

    values.put("field_001", "1");
    values.put("field_002", "2");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

  /**
   * field_001_1: non-empty string.
   * field_002_1: same as field_001_1.
   * field_001_2: non-empty string.
   * field_002_2: same as field_001_2.
   * rule_001_*:  unconditional check of equality between field_001_* and field_002_*.
   *
   * Expected result:
   *   rule_001_1 validated,
   *   rule_001_2 validated.
   */
  public void test011Ok() {
    form = getForm("form_equality_006");

    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_002_1", "a");
    values.put("field_002_2", "b");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001_1: non-empty string.
   * field_002_1: non-empty string, not equal to field_001_1.
   * field_001_2: non-empty string.
   * field_002_2: non-empty string, not equal to field_001_2.
   * rule_001_*:  unconditional check of equality between field_001_* and field_002_*.
   *
   * Expected result:
   *   rule_001_1 not validated,
   *   rule_001_2 not validated.
   */
  public void test012Bad() {
    form = getForm("form_equality_006");

    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_002_1", "c");
    values.put("field_002_2", "d");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

  /**
   * field_001_1: non-empty string.
   * field_002_1: same as field_001_1.
   * field_001_2: non-empty string.
   * field_002_2: same as field_001_2.
   * rule_001_*:  conditional check of equality between field_001_* and field_002_*.
   *
   * Expected result:
   *   rule_001_1 validated,
   *   rule_001_2 validated.
   */
  public void test013Ok() {
    form = getForm("form_equality_007");

    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_002_1", "a");
    values.put("field_002_2", "b");

    values.put("check_fields_1", "y");
    values.put("check_fields_2", "y");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001_1: non-empty string.
   * field_002_1: non-empty string, not equal to field_001_1.
   * field_001_2: non-empty string.
   * field_002_2: non-empty string, not equal to field_001_2.
   * rule_001_*:  conditional check of equality between field_001_* and field_002_*.
   *
   * Expected result:
   *   rule_001_1 not validated,
   *   rule_001_2 not validated.
   */
  public void test014Bad() {
    form = getForm("form_equality_007");

    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_002_1", "c");
    values.put("field_002_2", "d");

    values.put("check_fields_1", "y");
    values.put("check_fields_2", "y");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

  /**
   * field_001_1: non-empty string.
   * field_002_1: same as field_001_1.
   * field_001_2: non-empty string.
   * field_002_2: same as field_001_2.
   * field_001_3: non-empty string.
   * field_002_3: same as field_001_3.
   * rule_001_*:  conditional check of equality between field_001_* and field_002_*.
   *
   * Expected result:
   *   rule_001_1 validated,
   *   rule_001_2 not checked,
   *   rule_001_3 validated.
   */
  public void test015Ok() {
    form = getForm("form_equality_007");

    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_001_3", "c");
    values.put("field_002_1", "a");
    values.put("field_002_2", "b");
    values.put("field_002_3", "c");

    values.put("check_fields_1", "y");
    values.put("check_fields_2", "n");
    values.put("check_fields_3", "y");

    assertTrue(validateForm(values));

    printValues();
  }

  /**
   * field_001_1: non-empty string.
   * field_002_1: non-empty string, not equal to field_001_1.
   * field_001_2: non-empty string.
   * field_002_2: non-empty string, not equal to field_001_2.
   * field_001_3: non-empty string.
   * field_002_3: non-empty string, not equal to field_001_2.
   * rule_001_*:  conditional check of equality between field_001_* and field_002_*.
   *
   * Expected result:
   *   rule_001_1 not validated,
   *   rule_001_2 not checked,
   *   rule_001_3 not validated.
   */
  public void test016Bad() {
    form = getForm("form_equality_007");

    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_001_3", "c");
    values.put("field_002_1", "d");
    values.put("field_002_2", "e");
    values.put("field_002_3", "f");

    values.put("check_fields_1", "y");
    values.put("check_fields_2", "n");
    values.put("check_fields_3", "y");

    assertFalse(validateForm(values));

    printValues();
    printMessages();
  }

} // pl.aislib.test.fm.forms.RuleEqualityTest class.
