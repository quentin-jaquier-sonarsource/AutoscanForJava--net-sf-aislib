package pl.aislib.test.fm.forms;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import pl.aislib.util.messages.MessageFormatConverter;

/**
 * Abstract class for testing rules.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.6 $
 */
public abstract class RuleTestAbstract extends FormTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public RuleTestAbstract(String name) throws Exception {
    super(name);

    form = forms.getForm(getFormName(), null, null, null);
  }


  // Protected methods

  /**
   * @see pl.aislib.test.fm.forms.FormTestAbstract#getFormsConfigurationFileName()
   */
  protected String getFormsConfigurationFileName() {
    return "forms_rules.xml";
  }

  /**
   * @see pl.aislib.test.fm.forms.FormTestAbstract#getMessagesConfigurationFileName()
   */
  protected String getMessagesConfigurationFileName() {
    return "messages_rules.xml";
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * @return name of the form.
   */
  protected abstract String getFormName();

  /**
   *
   */
  protected void printMessages() {
    if (verbose) {
      Map messages = form.getErrorMessages(new MessageFormatConverter());
      MapUtils.verbosePrint(System.out, "Messages", messages);
    }
  }

} // RuleTestAbstract class
