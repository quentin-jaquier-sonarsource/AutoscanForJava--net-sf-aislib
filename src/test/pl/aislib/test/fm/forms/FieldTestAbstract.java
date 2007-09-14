package pl.aislib.test.fm.forms;

import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import pl.aislib.fm.forms.Field;
import pl.aislib.fm.forms.ValidateException;

/**
 * Abstract class for testing fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.5 $
 */
public abstract class FieldTestAbstract extends FormTestAbstract {

  /**
   * Field being used.
   */
  protected Field field;

  /**
   * Map of messages from validation of the field.
   */
  protected Map messagesMap;


  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldTestAbstract(String name) throws Exception {
    super(name);

    form = forms.getForm(getFormName(), null, null, null);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FormTestAbstract#getFormsConfigurationFileName()
   */
  protected String getFormsConfigurationFileName() {
    return "forms_fields.xml";
  }

  /**
   * @see pl.aislib.test.fm.forms.FormTestAbstract#getMessagesConfigurationFileName()
   */
  protected String getMessagesConfigurationFileName() {
    return "messages_fields.xml";
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();

    messagesMap = new SequencedHashMap();
  }

  /**
   * @return name of the form.
   */
  protected abstract String getFormName();

  /**
   * @param value <code>String</code> object.
   * @return validated object.
   * @throws ValidateException if an error occurs during validation.
   */
  protected Object validateField(String value) throws ValidateException {
    return field.validate(value, messagesMap, field.getName(), null);
  }

  /**
   * @param values array of <code>String</code> objects.
   * @return validated objects.
   * @throws ValidateException if an error occurs during validation.
   */
  protected Object validateField(String[] values) throws ValidateException {
    return field.validate(values, messagesMap, field.getName(), null);
  }

  /**
   * @return identification code of the message from validation.
   */
  protected int errorCode() {
    int errCode = -1;
    if (messagesMap == null || messagesMap.size() == 0) {
      return errCode;
    }
    return ((Integer) ((Map.Entry) messagesMap.entrySet().iterator().next()).getValue()).intValue();
  }

  /**
   * @param expected identification code of expected message from validation.
   */
  protected void checkErrorCode(int expected) {
    int errCode = errorCode();
    if (errCode != expected) {
      fail(MSG_002 + errCode);
    }
  }

} // FieldTestAbstract class
