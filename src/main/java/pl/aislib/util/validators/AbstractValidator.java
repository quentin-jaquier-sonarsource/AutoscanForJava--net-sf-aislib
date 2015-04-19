package pl.aislib.util.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import pl.aislib.fm.forms.BaseValidator;
import pl.aislib.fm.forms.ValidateException;

/**
 * Core implementation of validators' class.
 *
 * <p>
 * Implemented properties:
 * <ul>
 *   <li><code>required</code> - requirement property</li>
 *   <li><code>locale</code> - localization property</li>
 * </ul>
 *
 * Notes:
 * Property <code>locale</code> should have values coming from Locale.toString() method.
 * This allows for assigning e.g. "en_US", "en_UK" in a single property.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public abstract class AbstractValidator implements BaseValidator {

  /**
   * Requirement property.
   */
  protected BooleanProperty required;

  /**
   * Locale property.
   */
  protected Property localeProperty;

  /**
   * <code>Locale</code> object.
   */
  protected Locale locale;

  /**
   * Object storing additional data.
   */
  protected Object data;

  /**
   * Map of (prioritized) properties.
   */
  protected List properties = new ArrayList();

  /**
   * Map of property names and property values.
   */
  protected Map propertyValues = new HashMap();

  /**
   * Array of all available locales for the system server resides in.
   * This is called only once, in the creation of the application.
   */
  private static Locale[] availableLocales = Locale.getAvailableLocales();


  // Constructors

  /**
   * Initializes validator and sets its properties.
   */
  public AbstractValidator() {
    required = new BooleanProperty("required", 0, Property.DEFAULT, false);
    localeProperty = new Property("locale", 0, Property.DEFAULT, Locale.US.toString());
    setLocaleObject(localeProperty.getValue().toString());
  }


  // Public validation methods

  /**
   * Main validation function for strings.
   *
   * @param value a string to be validated.
   * @return a converted object.
   * @throws ValidateException if value could not be validated.
   */
  public Object validate(String value) throws ValidateException {
    // Throw exception if value is empty but should be required
    if (required.isTrue() && checkEmpty(value)) {
      throw new ValidateException("Value is required.");
    }

    return value;
  }

  /**
   * Main validation function for string arrays.
   *
   * @param values an array of string to be validated.
   * @return Object[] a converted array.
   * @throws ValidateException if value could not be validated.
   */
  public Object[] validate(String[] values) throws ValidateException {
    // Throw exception if array is empty but should not be.
    if (required.isTrue() && checkEmpty(values)) {
      throw new ValidateException("Values are required.");
    }

    return values;
  }


  // Public property methods

  /**
   * Sets the requirement property.
   *
   * @param value true when the property is set.
   */
  public void setRequired(boolean value) {
    required.set(required.getType() & ~Property.DEFAULT, value);
  }

  /**
   * Sets the localization property.
   *
   * @param value string value for the <code>Locale</code> object.
   */
  public void setLocale(String value) {
    localeProperty.set(value);
    setLocaleObject(value);
  }


  // Other public methods

  /**
   * @see pl.aislib.fm.forms.BaseValidator#applyObject(java.lang.String, java.lang.Object)
   */
  public Object applyObject(String objName, Object obj) {
    if ("properties".equals(objName)) {
      return prioritize((Map) obj);
    }

    return null;
  }

  /**
   * Gets map of property names and property values.
   *
   * @return map of property names and property values.
   */
  public Map getPropertyValues() {
    return propertyValues;
  }

  /**
   * Gets <code>String</code> value of a property with given name.
   *
   * @param propertyName name of the property.
   * @return <code>String</code> value of the property.
   */
  public String getPropertyValue(String propertyName) {
    Object result = propertyValues.get(propertyName);
    return result != null ? (String) result : null;
  }

  /**
   * @return object storing additional data.
   */
  public Object getData() {
    return data;
  }

  /**
   * @param data object storing additional data.
   */
  public void setData(Object data) {
    this.data = data;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    String className = getClass().getName();
    int dotIndex = className.lastIndexOf('.');
    String classPrefix = className.substring(0, dotIndex);
    String shortName = className.substring(dotIndex + 1);

    String str = data == null ? "" : ", data = " + data;

    if ("pl.aislib.util.validators".equals(classPrefix)) {
      return shortName + " (AISlib)" + str;
    }
    return className + str;
  }


  // Protected check methods

  /**
   * Checks whether an object is empty.
   *
   * @param value an object to be checked.
   * @return true if object is empty.
   */
  protected boolean checkEmpty(Object value) {
    return value == null;
  }

  /**
   * Checks whether an array of objects is empty.
   *
   * @param values an array to be checked.
   * @return true if array is empty.
   */
  protected boolean checkEmpty(Object[] values) {
    return values == null || values.length == 0;
  }

  /**
   * Specifies whether a property can be checked against the validation.
   *
   * @param property a property to be checked.
   * @return true if the property can be checked.
   */
  protected boolean checkProperty(Property property) {
    return property.isSet() && (required.isFalse() || !property.isDefault());
  }

  /**
   * Specifies whether a property map can be checked against the validation.
   *
   * @param propertyMap properties to be checked.
   * @return true if properties can be checked.
   */
  protected boolean checkProperty(PropertyMap propertyMap) {
    return propertyMap.isSet() && (required.isFalse() || !propertyMap.isDefault());
  }


  // Private methods

  /**
   * @param currentProperties map of properties to be prioritized.
   * @return map of prioritized properties.
   */
  private Map prioritize(Map currentProperties) {
    Collections.sort(
      properties,
      new Comparator() {
        public int compare(Object o1, Object o2) {
          AbstractValidator.Property p1 = (AbstractValidator.Property) o1;
          AbstractValidator.Property p2 = (AbstractValidator.Property) o2;
          if (p1.getPriority() < p2.getPriority()) {
            return -1;
          } else if (p1.getPriority() > p2.getPriority()) {
            return 1;
          }
          // If priorities are the same, first property is 'less' than the second one
          return -1;
        }
      }
    );

    Map result = new LinkedHashMap();

    // Check all named properties
    for (Iterator i = properties.iterator(); i.hasNext();) {
      Property property = (Property) i.next();
      String propertyName = property.getName();
      Object propertyValue = currentProperties.get(propertyName);
      if (propertyValue != null) {
        result.put(propertyName, propertyValue);
      }
    }

    // Check unnamed properties (compliant with 'old' validators)
    for (Iterator i = currentProperties.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Object key = me.getKey();
      Object value = me.getValue();
      if (!result.containsKey(key)) {
        result.put(key, value);
      }
    }

    propertyValues = result;

    return result;
  }

  /**
   * Sets the <code>Locale</code> object.
   * By default, it will have <code>Locale.US</code> value.
   *
   * @param value string value for the <code>Locale</code> object.
   */
  private void setLocaleObject(String value) {
    for (int i = 0; i < availableLocales.length; i++) {
      if (value.equals(availableLocales[i].toString())) {
        locale = availableLocales[i];
        return;
      }
    }
    locale = Locale.US;
  }


  // Protected classes

  /**
   * Property class.
   * Its purpose is to make implementation of various properties easier.
   * A property can be default, i.e. it can be set inside superclass.
   * A property can be final meaning it is not changed after being set in the configuration file.
   * Each property has the priority. The order of property evaluation during validation depends on it.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected class Property {

    /**
     * Constant describing normal property.
     */
    public static final int NORMAL = 0;

    /**
     * Constant describing default property.
     */
    public static final int DEFAULT = 1;

    /**
     * Constant describing final property.
     */
    public static final int FINAL = 2;

    /**
     * Name of the property.
     */
    protected String name;

    /**
     * Priority of the property.
     */
    protected int priority;

    /**
     * Type of the property (combination of normal, default and final).
     */
    protected int type;

    /**
     * Value of the property.
     */
    protected Object value;

    /**
     * @param priority priority of the property.
     * @param type type of the property.
     * @param value value of the property.
     * @deprecated
     */
    public Property(int priority, int type, Object value) {
      set(priority, type, value);
    }

    /**
     * @param name name of the property.
     * @param priority priority of the property.
     * @param type type of the property.
     * @param value value of the property.
     */
    public Property(String name, int priority, int type, Object value) {
      this.name = name;
      set(priority, type, value);
      properties.add(this);
    }

    /**
     * @param type type of the property to be added.
     */
    public void addType(int type) {
      this.type |= type;
    }

    /**
     * @return name of the property.
     */
    public final String getName() {
      return name;
    }

    /**
     * @return priority of the property.
     */
    public int getPriority() {
      return priority;
    }

    /**
     * @return type of the property.
     */
    public int getType() {
      return type;
    }

    /**
     * @return value of the property.
     */
    public Object getValue() {
      return value;
    }

    /**
     * @return true if the property is default.
     */
    public boolean isDefault() {
      return (type & DEFAULT) == DEFAULT;
    }

    /**
     * @return true if the property is final.
     */
    public boolean isFinal() {
      return (type & FINAL) == FINAL;
    }

    /**
     * @return boolean if value of the property has been set.
     */
    public boolean isSet() {
      return value != null;
    }

    /**
     * Sets value of the property and makes it non-default.
     * This method should be called only when the property is set in the configuration file.
     *
     * @param value an <code>Object</code>.
     */
    public void set(Object value) {
      type = type & ~DEFAULT;
      this.value = value;
    }

    /**
     * @param type type of the property.
     * @param value an <code>Object</code>.
     */
    public void set(int type, Object value) {
      this.type = type;
      this.value = value;
    }

    /**
     * @param priority priority of the property.
     * @param type type of the property.
     * @param value an <code>Object</code>.
     */
    public void set(int priority, int type, Object value) {
      this.priority = priority;
      this.type = type;
      this.value = value;
    }

    /**
     * Sets value of the property leaving its type unchanged.
     * This method should be called in superclasses, for example for default properties.
     *
     * @param value an <code>Object</code>.
     */
    public void setValue(Object value) {
      this.value = value;
    }

  } // Property inner class

  /**
   * A class of properties of boolean value.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected class BooleanProperty extends Property {

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(int, int, Object)
     * @deprecated
     */
    public BooleanProperty(int priority, int type, boolean value) {
      super(priority, type, new Boolean(value));
    }

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(String, int, int, Object)
     */
    public BooleanProperty(String name, int priority, int type, boolean value) {
      super(name, priority, type, new Boolean(value));
    }

    /**
     * @return true if boolean value of the property is <code>false</code>.
     */
    public boolean isFalse() {
      return value.equals(Boolean.FALSE);
    }

    /**
     * @return true if boolean value of the property is <code>true</code>.
     */
    public boolean isTrue() {
      return value.equals(Boolean.TRUE);
    }

    /**
     * @see AbstractValidator.Property#set(Object)
     */
    public void set(boolean value) {
      super.set(new Boolean(value));
    }

    /**
     * @see AbstractValidator.Property#set(int, Object)
     */
    public void set(int type, boolean value) {
      super.set(type, new Boolean(value));
    }

    /**
     * @see AbstractValidator.Property#set(int, int, Object)
     */
    public void set(int priority, int type, boolean value) {
      super.set(priority, type, new Boolean(value));
    }

    /**
     * @see AbstractValidator.Property#setValue(Object)
     */
    public void setValue(boolean value) {
      super.setValue(new Boolean(value));
    }

  } // BooleanProperty inner class

  /**
   * A class of properties of integer value.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected class IntegerProperty extends Property {

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(int, int, Object)
     * @deprecated
     */
    public IntegerProperty(int priority, int type, int value) {
      super(priority, type, new Integer(value));
    }

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(String, int, int, Object)
     */
    public IntegerProperty(String name, int priority, int type, int value) {
      super(name, priority, type, new Integer(value));
    }

    /**
     * @return integer value of the property.
     */
    public int intValue() {
      return ((Integer) value).intValue();
    }

    /**
     * @return true if integer value of the property is negative.
     */
    public boolean isNegative() {
      return ((Integer) value).intValue() < 0;
    }

    /**
     * @return true if integer value of the property is positive.
     */
    public boolean isPositive() {
      return ((Integer) value).intValue() > 0;
    }

    /**
     * @return true if integere value of the property is zero.
     */
    public boolean isZero() {
      return ((Integer) value).intValue() == 0;
    }

    /**
     * @see AbstractValidator.Property#set(Object)
     */
    public void set(int value) {
      super.set(new Integer(value));
    }

    /**
     * @see AbstractValidator.Property#set(int, Object)
     */
    public void set(int type, int value) {
      super.set(type, new Integer(value));
    }

    /**
     * @see AbstractValidator.Property#set(int, int, Object)
     */
    public void set(int priority, int type, int value) {
      super.set(priority, type, new Integer(value));
    }

    /**
     * @see AbstractValidator.Property#setValue(Object)
     */
    public void setValue(int value) {
      super.setValue(new Integer(value));
    }

  } // IntegerProperty inner class

  /**
   * A class of properties of an array of values.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected class ArrayProperty extends Property {

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(int, int, Object)
     * @deprecated
     */
    public ArrayProperty(int priority, int type, Object[] value) {
      super(priority, type, value);
    }

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(String, int, int, Object)
     */
    public ArrayProperty(String name, int priority, int type, Object[] value) {
      super(name, priority, type, value);
    }

    /**
     * @param index an <code>int</code>.
     * @return appropriate object in the array of values.
     */
    public Object getValue(int index) {
      return ((Object[]) value)[index];
    }

    /**
     * @return length of the array of values.
     */
    public int length() {
      return isSet() ? ((Object[]) value).length : -1;
    }

  } // ArrayProperty inner class

  /**
   * A class of properties of string value together with its converted counterpart.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected class ConvertedProperty extends Property {

    /**
     * Converted value.
     */
    protected Object convertedValue;

    /**
     * @param priority priority of the property.
     * @param type type of the property.
     * @param value value of the property.
     * @param convertedValue converted value of the property.
     * @deprecated
     */
    public ConvertedProperty(int priority, int type, Object value, Object convertedValue) {
      super(priority, type, value);
      setConvertedValue(convertedValue);
    }

    /**
     * @param name name of the property.
     * @param priority priority of the property.
     * @param type type of the property.
     * @param value value of the property.
     * @param convertedValue converted value of the property.
     */
    public ConvertedProperty(String name, int priority, int type, Object value, Object convertedValue) {
      super(name, priority, type, value);
      setConvertedValue(convertedValue);
    }

    /**
     * @return converted value of the property.
     */
    public Object getConvertedValue() {
      return convertedValue;
    }

    /**
     * @param value value of the property.
     * @param convertedValue converted value of the property.
     */
    public void set(Object value, Object convertedValue) {
      super.set(value);
      setConvertedValue(convertedValue);
    }

    /**
     * @param type type of the property.
     * @param value value of the property.
     * @param convertedValue converted value of the property.
     */
    public void set(int type, Object value, Object convertedValue) {
      super.set(type, value);
      setConvertedValue(convertedValue);
    }

    /**
     * @param priority priority of the property.
     * @param type type of the property.
     * @param value value of the property.
     * @param convertedValue converted value of the property.
     */
    public void set(int priority, int type, Object value, Object convertedValue) {
      super.set(priority, type, value);
      setConvertedValue(convertedValue);
    }

    /**
     * @param convertedValue converted value of the property.
     */
    public void setConvertedValue(Object convertedValue) {
      this.convertedValue = convertedValue;
    }

  } // ConvertedProperty inner class

  /**
   * A class of properties of string value together with its parts (tokens) divided according to given delimiters.
   * Tokens have their converted counterparts as well.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected class TokenizedProperty extends Property {

    /**
     * Array of tokens.
     */
    protected String[] tokens;

    /**
     * Array of converted tokens.
     */
    protected Object[] convertedTokens;

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(int, int, Object)
     * @deprecated
     */
    public TokenizedProperty(int priority, int type, String value) {
      super(priority, type, value);
      setTokens(null, null);
    }

    /**
     * @see AbstractValidator.Property#AbstractValidator.Property(int, int, Object)
     */
    public TokenizedProperty(String name, int priority, int type, String value) {
      super(name, priority, type, value);
      setTokens(null, null);
    }

    /**
     * @return array of tokens.
     */
    public String[] getTokens() {
      return tokens;
    }

    /**
     * @return array of converted tokens.
     */
    public Object[] getConvertedTokens() {
      return convertedTokens;
    }

    /**
     * @param type type of the property.
     * @param value value of the property.
     */
    public void set(int type, String value) {
      super.set(type, value);
      setTokens(null, null);
    }

    /**
     * @param priority priority of the property.
     * @param type type of the property.
     * @param value value of the property.
     */
    public void set(int priority, int type, String value) {
      super.set(priority, type, value);
      setTokens(null, null);
    }

    /**
     * @param tokens array of tokens.
     */
    public void setTokens(String[] tokens) {
      this.tokens = tokens;
    }

    /**
     * @param tokens array of tokens.
     * @param convertedTokens array of converted tokens.
     */
    public void setTokens(String[] tokens, Object[] convertedTokens) {
      this.tokens = tokens;
      this.convertedTokens = convertedTokens;
    }

    /**
     * @return value of the property, as a <code>String</code> object.
     */
    public String stringValue() {
      return (String) value;
    }

    /**
     * Splits value into tokens.
     *
     * @param delimiters string of delimiting characters.
     */
    public void tokenize(String delimiters) {
      if (!isSet()) {
        return;
      }

      StringTokenizer st = new StringTokenizer((String) value, delimiters);
      tokens = new String[st.countTokens()];

      for (int i = 0; st.hasMoreTokens(); i++) {
        tokens[i] = st.nextToken();
      }
    }

    /**
     * Splits string into tokens.
     *
     * @param tokenString <code>String</code> object.
     * @param delimiters string of delimiting characters.
     */
    public void tokenize(String tokenString, String delimiters) {
      value = tokenString;
      setTokens(null, null);
      tokenize(delimiters);
    }

  } // TokenizedProperty inner class

  /**
   * Class of property maps.
   * It is a way of grouping similar properties.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected class PropertyMap extends HashMap {

    /**
     * @param key a key.
     * @return property given by the key.
     */
    public Property getProperty(String key) {
      return (Property) get(key);
    }

    /**
     * @return true if at least one property in the map is default.
     */
    public boolean isDefault() {
      return countDefault(true) > 0;
    }

    /**
     * @return true if at least one property in the map has been set.
     */
    public boolean isSet() {
      return countSet(true) > 0;
    }

    // Protected methods

    /**
     * Counts (not) default properties in the map.
     *
     * @param bDefault true if counting default properties.
     * @return number of found properties.
     */
    protected int countDefault(boolean bDefault) {
      int counter = 0;
      for (Iterator i = entrySet().iterator(); i.hasNext();) {
        Map.Entry me = (Map.Entry) i.next();
        Property property = (Property) me.getValue();
        if (bDefault == property.isDefault()) {
          counter++;
        }
      }

      return counter;
    }

    /**
     * Counts properties in the map that have (not) been set.
     *
     * @param bSet true if counting properites that have been set.
     * @return number of found properties.
     */
    protected int countSet(boolean bSet) {
      int counter = 0;
      for (Iterator i = entrySet().iterator(); i.hasNext();) {
        Map.Entry me = (Map.Entry) i.next();
        Property property = (Property) me.getValue();
        if (bSet == property.isSet()) {
          counter++;
        }
      }

      return counter;
    }

  } // PropertyMap inner class

} // pl.aislib.util.validators.AbstractValidator class
