package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing date fields.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.5 $
 */
public class FieldDateTest extends FieldTestAbstract {

  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldDateTest(String name) throws Exception {
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
   * field_001: Proper date after start range.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");
    
    validateField("2003-07-01");
  }

  /**
   * field_001: Start range date.
   * @throws ValidateException if test failed.
   */
  public void test002Ok() throws ValidateException {
    field = form.getField("field_001");
    
    validateField("2003-06-30");
  }
  
  /**
   * field_001: Empty string.
   */
  public void test003Bad() {
    field = form.getField("field_001");
    
    try {
      validateField("");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(1);
    }
  }

  /**
   * field_001: Invalid date.
   */
  public void test004Bad() {
    field = form.getField("field_001");
    
    try {
      validateField("1a");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }
  
  /**
   * field_001: Proper date before start range.
   */
  public void test005Bad() {
    field = form.getField("field_001");
    
    try {
      validateField("2003-06-29");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(3);
    }
  }

  /**
   * field_002: Proper date before end range.
   * @throws ValidateException if test failed.
   */
  public void test006Ok() throws ValidateException {
    field = form.getField("field_002");
    
    validateField("2003-07-01");
  }

  /**
   * field_002: End range date.
   * @throws ValidateException if test failed.
   */
  public void test007Ok() throws ValidateException {
    field = form.getField("field_002");
    
    validateField("2003-07-31");
  }

  /**
   * field_002: Proper date after end range.
   */
  public void test008Bad() {
    field = form.getField("field_002");
    
    try {
      validateField("2003-08-01");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(3);
    }
  }

} //FieldDateTest class
