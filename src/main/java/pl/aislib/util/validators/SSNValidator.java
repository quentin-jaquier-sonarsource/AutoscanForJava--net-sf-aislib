package pl.aislib.util.validators;

import pl.aislib.fm.forms.ValidateException;

/**
 * Social Security Number (SSN) validation class.
 * 
 * <p>
 * Implemented additional properties:
 * <ul>
 *   <li><code>allowNormal</code></li>
 *   <li><code>allowShort</code></li>
 * </ul>
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.5 $
 */
public class SSNValidator extends StringValidator {

  /**
   * Constant describing normal SSN type (the one with dashes).
   */
  protected static final String KEY_SSN_DASHES = "normal";
  
  /**
   * Constant describing short SSN type (the one without dashes).
   */
  protected static final String KEY_SSN_NO_DASHES = "short";


  /**
   * SSN type property map.
   */
  protected PropertyMap ssnTypes;


  // Constructors
      
  /**
   * Base constructor.
   */
  public SSNValidator() {
    super();

    ssnTypes = new PropertyMap();
    ssnTypes.put(KEY_SSN_DASHES, new BooleanProperty("allowNormal", 4, Property.DEFAULT, true));
    ssnTypes.put(KEY_SSN_NO_DASHES, new BooleanProperty("allowShort", 4, Property.DEFAULT, false));

    configSsnTypes();

    allowedChars.addType(Property.FINAL);
    minimumLength.addType(Property.FINAL);
    maximumLength.addType(Property.FINAL);
    disallowedValues.addType(Property.FINAL);
  }


  // Public validation methods
  
  /**
   * @see pl.aislib.util.validators.Validator#validateObject
   */
  public void validateObject(Object value) throws ValidateException {
    super.validateObject(value);

    if (checkProperty(ssnTypes) && !checkSSN((String) value)) {
      throw new ValidateException("Invalid SSN number.");
    }
  }


  // Public property methods

  /**
   * @param value flag for allowing dashes in SSN.
   */  
  public void setAllowNormal(boolean value) {
    setAllowSsnType(KEY_SSN_DASHES, value);
  }
  
  /**
   * @param value flag for allowing no dashes in SSN.
   */
  public void setAllowShort(boolean value) {
    setAllowSsnType(KEY_SSN_NO_DASHES, value);
  }


  // Protected check methods

  /**
   * @param value a string to be checked.
   * @return true if the string is SSN.
   */
  protected boolean checkSSN(String value) {
    if (value == null) {
      return false;
    }
    
    boolean bNormal = ((BooleanProperty) ssnTypes.getProperty(KEY_SSN_DASHES)).isTrue();
    boolean bShort  = ((BooleanProperty) ssnTypes.getProperty(KEY_SSN_NO_DASHES)).isTrue();
    boolean bBoth   = bNormal && bShort;
    
    int valueLength = value.length();
    
    if (bNormal || bBoth) {
      // SSN must be 9 or 11 characters long, if validating both types
      if (bBoth && valueLength != 9 && valueLength != 11) {
        return false;
      }

      if (valueLength == 9) {
        int indDash = value.indexOf("-");
        // No dashes should be allowed
        if (indDash != -1) {
          return false;
        }
      } else {
        // Length is 11
        int     indDash1  = value.indexOf("-");
        int     indDash2  = value.lastIndexOf("-");
        boolean bDashes   = indDash1 != -1 && indDash2 != -1;
        boolean bNoDashes = indDash1 == -1 && indDash2 == -1;

        if (bDashes) {
          // If there are dashes, there must be two of them
          if (indDash1 == indDash2) {
            return false;
          }
        
          // Dashes should be in appropriate places
          if (indDash1 != 3 || indDash2 != 6) {
            return false;
          }

          // There must be at most two dashes
          if (value.charAt(4) == '-' || value.charAt(5) == '-') {
            return false;
          }
        } else {
          // There must not be one dash
          if (bNoDashes) {
            return false;
          }
        }
      }
    }
      
    return true;
  }


  // Protected methods  

  /**
   * Configure SSN types.
   */
  protected void configSsnTypes() {
    boolean bNormal = ((BooleanProperty) ssnTypes.getProperty(KEY_SSN_DASHES)).isTrue();
    boolean bShort  = ((BooleanProperty) ssnTypes.getProperty(KEY_SSN_NO_DASHES)).isTrue();

    if (bNormal && bShort) {
      allowedChars.setValue("0123456789-");
      minimumLength.setValue(9);
      maximumLength.setValue(11);
      setDisallowedValuesOnly("000-00-0000, 000000000", true);
    } else if (bNormal) {
      allowedChars.setValue("0123456789-");
      minimumLength.setValue(11);
      maximumLength.setValue(11);
      setDisallowedValuesOnly("000-00-0000", true);
    } else if (bShort) {
      allowedChars.setValue("0123456789");
      minimumLength.setValue(9);
      maximumLength.setValue(9);
      setDisallowedValuesOnly("000000000", true);
    } else {
      // At least one property must have a truth value
    }
  }
  
  /**
   * @param ssnType a type of SSN: with or without dashes.
   * @param allow flag for allowing entering the type of SSN.
   */
  protected void setAllowSsnType(String ssnType, boolean allow) {
    BooleanProperty property = (BooleanProperty) ssnTypes.getProperty(ssnType);
    property.set(allow);
    configSsnTypes();
  }

} // SSNValidator class
