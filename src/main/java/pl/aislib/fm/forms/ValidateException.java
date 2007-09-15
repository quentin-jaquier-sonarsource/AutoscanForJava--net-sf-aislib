package pl.aislib.fm.forms;

/**
 * <code>Exception</code> subclass used in validation.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.4 $
 * @since AISLIB 0.1
 */
public class ValidateException extends Exception {

  // Fields

  /**
   * Validator object, if any, in which exception occurred.
   */
  BaseValidator validator = null;


  // Constructors

  /**
   * @param reason exception's message.
   */
  public ValidateException(String reason) {
    super(reason);
  }

  /**
   * @param reason exception's message.
   * @param validator object in which exception occurred.
   */
  public ValidateException(String reason, BaseValidator validator) {
    this(reason);
    this.validator = validator;
  }


  // Public methods

  /**
   * Gets possible validator object in which exception occurred.
   *
   * @return validator object or <code>null</code> if it is not set.
   */
  public BaseValidator getValidator() {
    return validator;
  }

  /**
   * Sets validator object in which exception occurred.
   *
   * @param validator validator object.
   */
  public void setValidator(BaseValidator validator) {
    this.validator = validator;
  }

} // pl.aislib.fm.forms.ValidateException class
