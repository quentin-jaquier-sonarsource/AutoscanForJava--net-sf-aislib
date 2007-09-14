package pl.aislib.test.fm.forms;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import pl.aislib.util.messages.MessageFormatConverter;

/**
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public class MessageConverterTest extends FormTestAbstract {

  /**
   * @see FormTestAbstract#FormTestAbstract(String)
   */
  public MessageConverterTest(String name) throws Exception {
    super(name);

    form = forms.getForm(getFormName(), null, null, null);
    verbose = true;
  }

  /**
   * @see pl.aislib.test.fm.forms.FormTestAbstract#getFormsConfigurationFileName()
   */
  protected String getFormsConfigurationFileName() {
    return "forms_messages.xml";
  }

  /**
   * @see pl.aislib.test.fm.forms.FormTestAbstract#getMessagesConfigurationFileName()
   */
  protected String getMessagesConfigurationFileName() {
    return "messages_messages.xml";
  }

  /**
   * @return <code>String</code> object containing name of the form.
   */
  protected String getFormName() {
    return "form_messages_001";
  }

  /**
   *
   */
  protected void printMessages() {
    if (verbose) {
      Map messages = form.getErrorMessages(new MessageFormatConverter());
      MapUtils.verbosePrint(System.out, "Messages", messages);
    }
  }

  /**
   * field_001_1: empty.
   * field_001_2: not empty.
   * field_001_3: empty.
   *
   * Expected result:
   *   field_001_1 not validated,
   *   field_001_2 validated,
   *   field_001_3 not validated.
   */
  public void test001Bad() {
    form = getForm(getFormName(), "en-us");

    values.put("field_001_1", "");
    values.put("field_001_2", "b");
    values.put("field_001_3", "");

    assertFalse(validateForm(values));
    assertEquals(form.getValue("field_001_1"), null);
    assertEquals(form.getValue("field_001_2"), "b");
    assertEquals(form.getValue("field_001_3"), null);

    printValues();
    printMessages();
  }

  /**
   * field_002_1: invalid <code>BigDecimal</code> object.
   * field_002_2: invalid <code>BigDecimal</code> object.
   *
   * Expected result:
   *   field_002_1 not validated,
   *   field_002_2 not validated.
   */
  public void test002Bad() {
    form = getForm("form_messages_002", "en-us");

    values.put("field_002_1", "aaa");
    values.put("field_002_2", "bbb");

    assertFalse(validateForm(values));
    assertEquals(form.getValue("field_002_1"), null);
    assertEquals(form.getValue("field_002_2"), null);

    printValues();
    printMessages();
  }

  /**
   * field_001_1: invalid <code>String</code>.
   * field_001_2: invalid <code>String</code>.
   * field_002_1: valid <code>Double</code> object.
   * field_002_2: valid <code>Double</code> object.
   *
   * Expected result:
   *   field_001_1 not validated,
   *   field_001_2 not validated.
   *   field_002_1 validated,
   *   field_002_2 validated.
   */
  public void test003Bad() {
    form = getForm("form_messages_003", null);

    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_002_1", "1.0");
    values.put("field_002_2", "2.0");

    assertFalse(validateForm(values));
    assertEquals(form.getValue("field_001_1"), null);
    assertEquals(form.getValue("field_001_2"), null);
    assertEquals(form.getValue("field_002_1"), new Double(1.0));
    assertEquals(form.getValue("field_002_2"), new Double(2.0));

    printValues();
    printMessages();
  }

  /**
   * check_001_1: field_001_1 will be validated.
   * check_001_2: field_001_2 will not be validated.
   * check_001_3: field_001_3 will be validated.
   * field_001_1: invalid <code>Integer</code>.
   * field_001_2: invalid <code>Integer</code>.
   * field_001_3: invalid <code>Integer</code>.
   *
   * Expected result:
   *   field_001_1 not validated,
   *   field_001_3 not validated.
   *
   * Message with {5} applied written.
   */
  public void test004Bad() {
    form = getForm("form_messages_004", null);

    values.put("check_001_1", "1");
    values.put("check_001_2", "0");
    values.put("check_001_3", "1");
    values.put("field_001_1", "a");
    values.put("field_001_2", "b");
    values.put("field_001_3", "c");

    assertFalse(validateForm(values));
    assertEquals(form.getValue("field_001_1"), null);
    assertEquals(form.getValue("field_001_3"), null);

    printValues();
    printMessages();
  }

} // pl.aislib.test.fm.forms.MessageConverterTest class
