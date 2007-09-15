package pl.aislib.test.util.validators;

import junit.framework.TestCase;

import pl.aislib.fm.forms.ValidateException;
import pl.aislib.test.fm.forms.IConstants;
import pl.aislib.util.validators.EmailValidator;

/**
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.3 $
 */
public class EmailValidatorTest extends TestCase implements IConstants {

  private EmailValidator validator;

  /**
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() {
    validator = new EmailValidator();
    validator.setRequired(false);
  }

  /**
   * Tests as many valid emails as possible.
   *
   * @throws ValidateException if test failed.
   */
  public void testEmailsValid() throws ValidateException {
    validator.validate("abc@def.com");
    validator.validate("abc-def@ghijk.lmn.opq.com");
    validator.validate("abcdef@ghij-klm-nup.com");
  }

  /**
   * Tests against invalid user.
   *
   */
  public void testEmailInvalidUser() {
    try {
      validator.validate("@abc.com");
      fail(MSG_001);
    } catch (ValidateException ve) {
      ;
    }
  }

} // pl.aislib.test.util.validators.EmailValidatorTest class
