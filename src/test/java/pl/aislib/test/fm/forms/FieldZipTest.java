package pl.aislib.test.fm.forms;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class for testing zip code fields.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.5 $
 */
public class FieldZipTest extends FieldTestAbstract {

  // Constructors
  
  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FieldZipTest(String name) throws Exception {
    super(name);
  }


  // Protected methods
  
  /**
   * @see pl.aislib.test.fm.forms.FieldTestAbstract#getFormName()
   */
  protected String getFormName() {
    return "form_zip";
  }


  // Test methods
  
  /**
   * field_001: Valid zip code in normal format, normal and extended formats allowed.
   * @throws ValidateException if test failed.
   */
  public void test001Ok() throws ValidateException {
    field = form.getField("field_001");
    
    validateField("12345");
  }

  /**
   * field_001: Valid zip code in extended format, normal and extended formats allowed.
   * @throws ValidateException if test failed.
   */
  public void test002Ok() throws ValidateException {
    field = form.getField("field_001");
    
    validateField("12345-6789");
  }

  /**
   * field_001: Empty string, normal and extended formats allowed.
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
   * field_001: Invalid zip, normal and extended formats allowed.
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
   * field_002: Valid zip code in extended format, extended format allowed.
   * @throws ValidateException if test failed.
   */
  public void test005Ok() throws ValidateException {
    field = form.getField("field_002");
    
    validateField("12345-6789");
  }

  /**
   * field_002: Valid zip code in normal format, extended format allowed.
   */
  public void test006Bad() {
    field = form.getField("field_002");
    
    try {
      validateField("12345");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

  /**
   * field_003: Valid zip code in normal format, normal format allowed.
   * @throws ValidateException if test failed.
   */
  public void test007Ok() throws ValidateException {
    field = form.getField("field_003");
    
    validateField("12345");
  }

  /**
   * field_003: Valid zip code in extended format, normal format allowed.
   */
  public void test008Bad() {
    field = form.getField("field_003");
    
    try {
      validateField("12345-6789");
      fail(MSG_001);
    } catch (ValidateException ve) {
      checkErrorCode(2);
    }
  }

} // FieldZipTest class
