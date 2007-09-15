package pl.aislib.util.validators;

import pl.aislib.fm.forms.ValidateException;

/**
 * Zip code validation class.
 * 
 * <p>
 * Implemented additional properties:
 * <ul>
 *   <li><code>allowExtended</code></li>
 *   <li><code>allowNormal</code></li>
 * </ul>
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
 */
public class ZipValidator
  extends StringValidator {

  /**
   * Constant describing extended (9-digit) zip code type.
   */
  protected static final String KEY_ZIP_EXTENDED = "extended";

  /**
   * Constant describing normal (5-digit) zip code type.
   */
  protected static final String KEY_ZIP_NORMAL = "normal";


  /**
   * Zip code type property map.
   */
  protected PropertyMap zipTypes;


  // Constructors
  
  /**
   * Base constructor.
   */
  public ZipValidator() {
    super();

    // By default, zip code is of extended type
    zipTypes = new PropertyMap();
    zipTypes.put(KEY_ZIP_NORMAL, new BooleanProperty("allowExtended", 4, Property.DEFAULT, true));
    zipTypes.put(KEY_ZIP_EXTENDED, new BooleanProperty("allowNormal", 4, Property.DEFAULT, true));
    
    configZipTypes();
    
    allowedChars.addType(Property.FINAL);
    minimumLength.addType(Property.FINAL);
    maximumLength.addType(Property.FINAL);
  }


  // Public validation methods
  
  /**
   * @see pl.aislib.util.validators.Validator#validateObject
   */
  public void validateObject(Object value) throws ValidateException {
    super.validateObject(value);

    if (checkProperty(zipTypes) && !checkZip((String) value)) {
      throw new ValidateException("Invalid zip.");
    }
  }


  // Public property methods

  /**
   * @param value flag for allowing extended zip code.
   */  
  public void setAllowExtended(boolean value) {
    setAllowZipType(KEY_ZIP_EXTENDED, value);
  }
  
  /**
   * @param value flag for allowing 5-digit zip code.
   */
  public void setAllowNormal(boolean value) {
    setAllowZipType(KEY_ZIP_NORMAL, value);
  }
  

  // Protected check methods
  
  /**
   * @param value a string to be checked.
   * @return true if the string is a zip code.
   */
  protected boolean checkZip(String value) {
    if (value == null) {
      return false;
    }

    boolean bExtended = ((BooleanProperty) zipTypes.getProperty(KEY_ZIP_EXTENDED)).isTrue();
    boolean bNormal   = ((BooleanProperty) zipTypes.getProperty(KEY_ZIP_NORMAL)).isTrue();
    boolean bBoth     = bNormal && bExtended;

    int valueLength = value.length();

    if (bExtended || bBoth) {
      // Zip must be 5 or 10 characters long, if validating both types
      if (bBoth && valueLength != 5 && valueLength != 10) {
        return false;
      }

      int indDash = value.indexOf("-");
      if (valueLength == 5) {
        // No dashes should be allowed
        if (indDash != -1) {
          return false;
        }
      } else {
        // Length is 10
        
        // There should be a dash
        if (indDash == -1) {
          return false;
        }

        // There should be only one dash
        if (indDash != value.lastIndexOf("-")) {
          return false;
        }

        // Dash should be in an appropriate place
        if (indDash != 5) {
          return false;
        }
      }
    }

    return true;
  }


  // Protected methods
  
  /**
   * Configure zip code types.
   */
  protected void configZipTypes() {
    boolean bExtended = ((BooleanProperty) zipTypes.getProperty(KEY_ZIP_EXTENDED)).isTrue();
    boolean bNormal   = ((BooleanProperty) zipTypes.getProperty(KEY_ZIP_NORMAL)).isTrue();

    if (bNormal && bExtended) {
      allowedChars.setValue("0123456789-");
      minimumLength.setValue(5);
      maximumLength.setValue(10);
    } else if (bNormal) {
      allowedChars.setValue("0123456789-");
      minimumLength.setValue(5);
      maximumLength.setValue(5);
    } else if (bExtended) {
      allowedChars.setValue("0123456789-");
      minimumLength.setValue(10);
      maximumLength.setValue(10);
    } else {
      // At least one property must have a truth value
    }
  }

  /**
   * @param zipType a type of zip code.
   * @param allow flag for allowing entering the type of zip code.
   */
  protected void setAllowZipType(String zipType, boolean allow) {
    BooleanProperty property = (BooleanProperty) zipTypes.getProperty(zipType);
    property.set(allow);
    configZipTypes();
  }

} // ZipValidator class
