package pl.aislib.test.util.validators;

import junit.framework.TestCase;

import pl.aislib.fm.forms.ValidateException;

import pl.aislib.util.validators.URLValidator;

/**
 * Testing url validation.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class URLValidatorTest extends TestCase {

  /** */
  private URLValidator validator;

  /** */
  private static final String WWW_AIS_PL = "http://www.ais.pl";

  /** */
  private static final String FTP_AIS_PL = "ftp://ftp.ais.pl";

  /**
   * @see junit.framework.TestCase#TestCase(String)
   */
  public URLValidatorTest(String message) {
    super(message);
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() {
    validator = new URLValidator();
  }

  /**
   * Method testAllowHttp.
   * @throws Exception if an exception has occured.
   */
  public void testAllowHttp() throws Exception {
    validator.setAllowHttp(true);
    validator.validate(WWW_AIS_PL);
  }

  /**
   * Method testNonAllowMalformedHttp.
   * @throws Exception if an exception has occured.
   */
  public void testNonAllowMalformedHttp() throws Exception {
    validator.setAllowHttp(true);
    try {
      validator.validate("http www.ais.pl");
      fail("malformed URL: http www.ais.pl");
    } catch (ValidateException ve) {
      ;
    }
  }

  /**
   * Method testAnyProtocolAllowed.
   * @throws Exception if an exception has occured.
   */
  public void testAnyProtocolAllowed() throws Exception {
    try {
      validator.validate(WWW_AIS_PL);
      validator.validate(FTP_AIS_PL);
      fail("all prococols are not allowed");
    } catch (ValidateException ve) {
      ;
    }
  }
}

