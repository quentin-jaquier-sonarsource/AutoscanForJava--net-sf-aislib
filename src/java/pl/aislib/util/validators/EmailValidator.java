package pl.aislib.util.validators;

import pl.aislib.fm.forms.ValidateException;

/**
 * Email validation class.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public class EmailValidator extends StringValidator {

  // Constructors

  /**
   * Base constructor.
   */
  public EmailValidator() {
    super();

    minimumLength.setValue(5);
    maximumLength.setValue(255);

    setRegExp(REG_EXP_DEFAULT);
  }


  // Public property methods

  /**
   * @see pl.aislib.util.validators.AbstractValidator#setRequired(boolean)
   */
  public void setRequired(boolean value) {
    super.setRequired(value);
    setRegExp(value ? REG_EXP_REQUIRED : REG_EXP_DEFAULT);
  }


  // Public validation methods

  /**
   * @see pl.aislib.util.validators.Validator#toObject
   */
  public Object toObject(String value) throws ValidateException {
    return value;
  }


  // Protected check methods

  /**
   * @param value a string to be checked.
   * @return true if the string is an email.
   * @deprecated
   */
  protected boolean checkEmail(String value) {
    int indAt = value.indexOf("@");
    int indDot = value.indexOf(".");

    // There must be @ character and . character
    if (indAt == -1 || indDot == -1) {
      return false;
    }

    // There must be only one @ character
    if (indAt != value.lastIndexOf("@")) {
      return false;
    }

    // Email can not start from @ character
    if (indAt == 1) {
      return false;
    }

    // Email can not start with .@ characters
    if (indDot == 1 && indAt == 2) {
      return false;
    }

    // . character can not be straight after @ character
    if (indDot == indAt + 1) {
      return false;
    }

    // There must be at least one . character after @ character
    if (indAt > value.lastIndexOf(".")) {
      return false;
    }

    return true;
  }


  // Protected methods

  /**
   * @see pl.aislib.util.validators.StringValidator#getDefaultRegExp()
   */
  protected String getDefaultRegExp() {
    // TODO: Full email address specification as per RFC822
    return "^(([\\w-&]+[.])*[\\w-&]+[@][\\w-&]+([.][\\w-&]+)*)?$";
  }

} // EmailValidator class
