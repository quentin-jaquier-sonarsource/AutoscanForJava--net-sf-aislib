package pl.aislib.fm.forms;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import pl.aislib.fm.forms.config.IField;
import pl.aislib.util.validators.AbstractValidator;

/**
 * Field class used in framework.
 *
 * @author
 * <table>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.4 $
 * @since AISLIB 0.1
 */
public class Field implements IField, IEntity {

  // Field type constants

  /**
   * Field's normal property value.
   */
  public static final int FT_NORMAL = 0;

  /**
   * Field's dynamic property value.
   */
  public static final int FT_DYNAMIC = 1;

  /**
   * Field's complex property value.
   */
  public static final int FT_COMPLEX = 2;

  /**
   * Field's conditional property value.
   */
  public static final int FT_CONDITIONAL = 4;


  // Value type constants

  /**
   * No values accepted.
   */
  public static final int VT_NONE = 0;

  /**
   * Single (string) values accepted.
   */
  public static final int VT_SINGLE = 1;

  /**
   * Array (of strings) values accepted.
   */
  public static final int VT_ARRAY = 2;

  /**
   * Name of the field.
   */
  protected String name;

  /**
   * Type of the field.
   */
  protected int type;

  /**
   * Builder for the field, for complex fields only.
   */
  protected FieldBuilder builder;

  /**
   * Type of values this field can have.
   */
  protected int valuesType;

  /**
   * Map of the field's validators.
   */
  protected Map validators;


  // Constructors

  /**
   * Constructor.
   *
   * @param name name of the field.
   */
  public Field(String name) {
    this.name = name;
    validators = new SequencedHashMap();
  }


  // Public methods

  /**
   * @param type type of the field.
   */
  public void addType(int type) {
    this.type |= type;
  }

  /**
   * Adds validator to the field.
   *
   * @param validator validator object.
   * @param messageCode message code.
   */
  public void addValidator(BaseValidator validator, int messageCode) {
    if (valuesType == VT_NONE) {
      if (!(validator instanceof ArrayValidator)) {
        valuesType = VT_SINGLE;
      } else {
        valuesType = VT_ARRAY;
      }
    } else {
      if ((valuesType == VT_ARRAY) && (validator instanceof Validator) && !(validator instanceof ArrayValidator)) {
        throw new RuntimeException("cannot mix validators for one field");
      }
      if ((valuesType == VT_SINGLE) && (validator instanceof ArrayValidator)) {
        throw new RuntimeException("cannot mix validators for one field");
      }
    }
    validators.put(validator, new Integer(messageCode));
  }

  /**
   * @return builder for the field.
   */
  public FieldBuilder getBuilder() {
    return builder;
  }

  /**
   * @return name of the field.
   */
  public String getName() {
    return name;
  }

  /**
   * @return type of the field.
   */
  public int getType() {
    return type;
  }

  /**
   * @return a map of the field's validators.
   */
  public Map getValidators() {
    return validators;
  }

  /**
   * @return type of values this field can have.
   */
  public int getValuesType() {
    return valuesType;
  }

  /**
   * @return true if the field is complex.
   */
  public boolean isComplex() {
    return (type & FT_COMPLEX) == FT_COMPLEX;
  }

  /**
   * @return true if the field is conditional.
   */
  public boolean isConditional() {
    return (type & FT_CONDITIONAL) == FT_CONDITIONAL;
  }

  /**
   * @return true if the field is dynamic.
   */
  public boolean isDynamic() {
    return (type & FT_DYNAMIC) == FT_DYNAMIC;
  }

  /**
   * @return true if the field is normal.
   */
  public boolean isNormal() {
    return (type & FT_NORMAL) == FT_NORMAL;
  }

  /**
   * @param builder Builder for the field.
   */
  public void setBuilder(FieldBuilder builder) {
    this.builder = builder;
  }

  /**
   * Sets mapping for the builder for the field.
   * @param mapping a map.
   */
  public void setBuilderMapping(Map mapping) {
    if (builder != null) {
      builder.setMapping(mapping);
    }
  }

  /**
   * @param type type of the field.
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * @param valuesType type of values this field can have.
   */
  public void setValuesType(int valuesType) {
    this.valuesType = valuesType;
  }

  /**
   * @param value value of the complex field.
   * @return map of base values of the complex field.
   */
  public Map split(Object value) {
    return split(value, true);
  }

  /**
   * @param value value of the complex field.
   * @param useMapping true if use mapping from template names to real names.
   * @return map of base values of the complex field.
   */
  public Map split(Object value, boolean useMapping) {
    if (!(isComplex() && builder != null)) {
      return null;
    }
    return useMapping ? builder.useMapping(builder.split(value)) : builder.split(value);
  }

  /**
   * Validates value of the field.
   *
   * @param value value of the field.
   * @param messagesMap a map the message will be put into, in case of errors.
   * @param fieldName name of the field (used for dynamic fields).
   * @param data an object specific for validators.
   * @return converted value of the field.
   * @throws ValidateException if an error has occurred while validating the field.
   */
  public Object validate(String value, Map messagesMap, String fieldName, Object data) throws ValidateException {
    Object result = value;
    Validator validator = null;

    boolean validatorDataChecked = false;

    for (Iterator i = validators.entrySet().iterator(); i.hasNext();) {
      validator = (Validator) ((Map.Entry) i.next()).getKey();
      if (data != null && checkValidatorData(validator, data)) {
        validatorDataChecked = true;
        continue;
      } else {
        validatorDataChecked = false;
      }
      try {
        result = validator.validate(value);
      } catch (ValidateException e) {
        messagesMap.put(fieldName, validators.get(validator));
        throw e;
      }
    }
    if (result == null && validator != null) {
      if (!validatorDataChecked) {
        messagesMap.put(fieldName, validators.get(validator));
        throw new ValidateException("Probably invalid conversion.");
      } else {
        return value;
      }
    }
    return result;
  }

  /**
   * Validates an array of values of the field.
   *
   * @param values an array of values of the field.
   * @param messagesMap a map the message will be put into, in case of errors.
   * @param fieldName name of the field (used for dynamic fields).
   * @param data an object specific for validators.
   * @return array of converted values of the field.
   * @throws ValidateException if an error has occurred while validating the field.
   */
  public Object validate(String[] values, Map messagesMap, String fieldName, Object data) throws ValidateException {
    Object[] result = values;
    ArrayValidator validator = null;

    boolean validatorDataChecked = false;

    for (Iterator i = validators.entrySet().iterator(); i.hasNext();) {
      validator = (ArrayValidator) ((Map.Entry) i.next()).getKey();
      if (data != null && checkValidatorData(validator, data)) {
        validatorDataChecked = true;
        continue;
      } else {
        validatorDataChecked = false;
      }
      try {
        result = validator.validate(values);
      } catch (ValidateException e) {
        messagesMap.put(fieldName, validators.get(validator));
        throw e;
      }
    }
    if (result == null && validator != null) {
      if (!validatorDataChecked) {
        messagesMap.put(fieldName, validators.get(validator));
        throw new ValidateException("Probably invalid conversion.");
      } else {
        return values;
      }
    }
    return result;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("\n\t" + name + " (type: " + valuesType + ")");
    result.append("\n\t  defined validators: ");
    for (Iterator it = validators.entrySet().iterator(); it.hasNext();) {
      Object object = ((Map.Entry) it.next()).getKey();
      result.append("\n\t\t" + object);
    }
    return new String(result);
  }


  // Protected methods

  /**
   * @param validator <code>BaseValidator</code> object.
   * @param data an <code>Object</code>.
   * @return true if additional data allow to proceed with validation in case of errors.
   */
  protected boolean checkValidatorData(BaseValidator validator, Object data) {
    Object validatorData = ((AbstractValidator) validator).getData();
    return (validatorData == null || !(data.equals(validatorData)));
  }

} // Field class
