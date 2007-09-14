package pl.aislib.util.validators;

import pl.aislib.fm.forms.ValidateException;

/**
 * Phone validation class.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public class PhoneValidator extends StringValidator {

  // Constructors

  /**
   * Base constructor.
   */
  public PhoneValidator() {
    super();

    format.setValue("(000) 000-0000");
    configFormat();
  }


  // Public property methods

  /**
   * @param value a format string.
   */
  public void setFormat(String value) {
    super.setFormat(value);
    configFormat();
  }


  // Protected methods

  /**
   * Phone configuration.
   */
  protected void configFormat() {
    String strFormat    = (String) format.getValue();
    int    formatLength = strFormat.length();

    minimumLength.setValue(formatLength);
    maximumLength.setValue(formatLength);

    String sAllowedChars = "";
    boolean digitsInserted = false;
    for (int i = 0; i < formatLength; i++) {
      char c = strFormat.charAt(i);
      if (c == '0' && !digitsInserted) {
        sAllowedChars += "0123456789";
        digitsInserted = true;
      } else if (c != '0') {
        sAllowedChars += c;
      }
    }
    allowedChars.setValue(sAllowedChars);
  }

  /**
   * @see pl.aislib.util.validators.StringValidator#formatString
   */
  protected Object formatString(String value) throws Exception {
    String strFormat    = (String) format.getValue();
    int    formatLength = strFormat.length();

    for (int i = 0; i < formatLength; i++) {
      char fc = strFormat.charAt(i);
      char vc = value.charAt(i);
      if (fc == '0') {
        if (vc < '0' || vc > '9') {
          throw new ValidateException("Invalid phone number format.");
        }
      } else if (fc != vc) {
        throw new ValidateException("Invalid phone number format.");
      }
    }

    return value;
  }

} // PhoneValidator class
