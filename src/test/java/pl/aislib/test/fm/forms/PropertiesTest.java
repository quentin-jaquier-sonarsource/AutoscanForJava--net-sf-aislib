package pl.aislib.test.fm.forms;

import java.util.HashMap;
import java.util.Map;

import pl.aislib.fm.Form;


public class PropertiesTest extends FormTestAbstract {

  private static final String FORM1 = "form1";
  private static final String V1 = "v1";
  private static final String RESULT = "result";

  public PropertiesTest(String name) throws Exception {
    super(name);
  }

  public void testTest() {
    Form formM = getForm(FORM1);
    assertNotNull(FORM1, formM);
    Map valuesS = new HashMap();
    valuesS.put(V1, V1);
    formM.validate(valuesS);
    String result = (String) formM.getValue(RESULT);
    assertNotNull(RESULT, result);
    assertEquals("BuilderValidator", result);
  }

  protected String getFormsConfigurationFileName() {
    return "properties_forms.xml";
  }

  protected String getMessagesConfigurationFileName() {
    return "messages_messages.xml";
  }

}
