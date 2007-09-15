package pl.aislib.util.validators;

import java.util.regex.Pattern;

import pl.aislib.fm.forms.ValidateException;

/**
 * Email validation class.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
 */
public class EmailValidator extends StringValidator {

  // Static fields

  /**
   * Constant describing default pattern for validating email address.
   */
  private static final String DEFAULT_EMAIL_PATTERN = "^(([\\w-&]+[.])*[\\w-&]+[@][\\w-&]+([.][\\w-&]+)*)?$";

  /**
   * Default email pattern object.
   */
  private static final Pattern defaultEmailPatternObject = Pattern.compile(DEFAULT_EMAIL_PATTERN);


  // Constructors

  /**
   * Base constructor.
   */
  public EmailValidator() {
    super();

    minimumLength.setValue(5);
    maximumLength.setValue(255);

    setPattern(PATTERN_DEFAULT);
  }


  // Public property methods

  /**
   * @see pl.aislib.util.validators.AbstractValidator#setRequired(boolean)
   */
  public void setRequired(boolean value) {
    super.setRequired(value);
    setPattern(value ? PATTERN_REQUIRED : PATTERN_DEFAULT);
  }


  // Public validation methods

  /**
   * @see pl.aislib.util.validators.Validator#toObject(String)
   */
  public Object toObject(String value) throws ValidateException {
    return value;
  }


  // Protected methods

  /**
   * @see pl.aislib.util.validators.StringValidator#getDefaultPatternObject()
   */
  protected Pattern getDefaultPatternObject() {
    return defaultEmailPatternObject;
  }

} // pl.aislib.util.validators.EmailValidator class
