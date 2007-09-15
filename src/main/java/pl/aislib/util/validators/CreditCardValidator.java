package pl.aislib.util.validators;

/**
 * Credit card validation class.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public class CreditCardValidator extends StringValidator {

  // TODO: Implement much more complex validation, e.g. allowing specific types of credit card numbers only.

  // Constructors

  /**
   * Base constructor.
   */
  public CreditCardValidator() {
    super();

    minimumLength.setValue(13);
    maximumLength.setValue(17);
    allowedChars.setValue("0123456789");
  }
    
} // CreditCardValidator class
