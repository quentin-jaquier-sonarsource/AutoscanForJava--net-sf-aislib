package pl.aislib.util.messages;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.aislib.fm.FieldContainer;
import pl.aislib.fm.Form;
import pl.aislib.fm.Message;
import pl.aislib.fm.messages.IMessage;
import pl.aislib.fm.messages.IMessageConverter;
import pl.aislib.util.validators.Validator;

/**
 * <code>MessageFormat</code> converter class.
 *
 * When keys (contents) provided for the message is an array of objects,
 * standard <code>MessageFormat</code> class is used for converting message key (message content).
 *
 * <p>
 * Example: Value of the field {1} should be up to 30 dollars.
 *
 * <p>
 * When keys (contents) provided for the message is a map of pattern names and objects,
 * first message key (message content) is converted, using the map, to string containing fields
 * for <code>MessageFormat</code> class, and then such string is used by <code>MessageFormat</code> object.
 *
 * <p>
 * Example: Value of the field {entity:name} should be up to {validator:maximumValue} {custom:currencyName}.
 *
 * <p>
 * This way, messages can be more dynamic.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class MessageFormatConverter implements IMessageConverter {

  // Static fields

  /** Used for every message. */
  private static final String PATTERN_TYPE_ENTITY = "entity";

  /** Used for field messages. */
  private static final String PATTERN_TYPE_VALIDATOR = "validator";

  /** Used for rule messages. */
  private static final String PATTERN_TYPE_RULE = "rule";

  /** Used for all fields in the form. */
  private static final String PATTERN_TYPE_FORM = "form";

  /** Custom properties in messages. */
  private static final String PATTERN_TYPE_CUSTOM = "custom";

  /** Pattern against which message format should be matched. */
  private static final Pattern pattern = Pattern.compile("\\{.*?[,\\}]");

  /** Map used for converting from old message format to the new one. */
  private static final Map backwardCompatibleEntityPatterns;

  static {
    // Conversion from old message format to the new one
    // The order of putting entries is important because,
    // for example <em>nameSuffix</em> should be caught in a pattern "nameSuffix" instead of <em>name</em>
    backwardCompatibleEntityPatterns = new LinkedHashMap();
    backwardCompatibleEntityPatterns.put("\\{5", "{" + PATTERN_TYPE_ENTITY + ":" + FieldContainer.MC_ENTITY_NAME_SUFFIX);
    backwardCompatibleEntityPatterns.put("\\{2", "{" + PATTERN_TYPE_ENTITY + ":" + FieldContainer.MC_ENTITY_NAME);
    backwardCompatibleEntityPatterns.put("\\{1", "{" + PATTERN_TYPE_ENTITY + ":" + FieldContainer.MC_ENTITY_GENERIC_NAME);
    backwardCompatibleEntityPatterns.put("\\{4", "{" + PATTERN_TYPE_ENTITY + ":" + FieldContainer.MC_ENTITY_DYNAMIC_NUMBER_FROM_1);
    backwardCompatibleEntityPatterns.put("\\{3", "{" + PATTERN_TYPE_ENTITY + ":" + FieldContainer.MC_ENTITY_DYNAMIC_NUMBER_FROM_0);
    backwardCompatibleEntityPatterns.put("\\{6", "{" + PATTERN_TYPE_ENTITY + ":" + FieldContainer.MC_ENTITY_DYNAMIC_COUNT);
  }


  // Fields

  /**
   * Map of custom property names and values.
   */
  private Map customProperties = new LinkedHashMap();


  // Public methods

  /**
   * Adds custom property to the converter.
   *
   * @param propertyName name of the custom property.
   * @param propertyValue value of the custom property.
   */
  public void addCustomProperty(String propertyName, Object propertyValue) {
    customProperties.put(propertyName, propertyValue);
  }

  /**
   * @see pl.aislib.util.messages.MessageFormatConverter#convert(pl.aislib.fm.messages.IMessage, java.lang.String, java.lang.Object, java.lang.Object)
   */
  public IMessage convert(IMessage message, String language, Object keys, Object contents) {
    if (keys == null && contents == null) {
      return message;
    }

    String messageKey = message.getKey();
    String messageContent = message.getContent(language);

    String convertedMessageKey = null;
    if (keys instanceof Object[]) {
      convertedMessageKey = formatPatterns(messageKey, (Object[]) keys);
    } else if (keys instanceof Map) {
      convertedMessageKey = convertPatterns(messageKey, preparePatternsForKeys((Map) keys));
    }

    String convertedMessageContent = null;
    if (contents instanceof Object[]) {
      convertedMessageContent = formatPatterns(messageContent, (Object[]) contents);
    } else if (contents instanceof Map) {
      convertedMessageContent = convertPatterns(messageContent, preparePatternsForContents((Map) contents));
    }

    return new Message(message.getCode(), convertedMessageKey, convertedMessageContent, language);
  }


  // Protected methods

  /**
   * Prepares patterns for given map of objects used in message keys.
   *
   * @param mapKeys <code>Map</code> of keys.
   * @return <code>Map</code> of pattern types as keys and values being maps of values possibly to be put in message keys.
   */
  protected Map preparePatternsForKeys(Map mapKeys) {
    Map mapPatternsForKeys = new HashMap();

    mapPatternsForKeys.put(PATTERN_TYPE_ENTITY, mapKeys);
    if (mapKeys.get(FieldContainer.MC_ENTITY_VALIDATOR) != null) {
      mapPatternsForKeys.put(PATTERN_TYPE_VALIDATOR, ((Validator) mapKeys.get(FieldContainer.MC_ENTITY_VALIDATOR)).getPropertyValues());
    }
    if (mapKeys.get(Form.MC_ENTITY_RULE) != null) {
      mapPatternsForKeys.put(PATTERN_TYPE_RULE, mapKeys.get(Form.MC_ENTITY_RULE));
    }
    mapPatternsForKeys.put(PATTERN_TYPE_CUSTOM, customProperties);

    return mapPatternsForKeys;
  }

  /**
   * Prepares patterns for given map of objects used in message contents.
   *
   * @param mapContents <code>Map</code> of contents.
   * @return <code>Map</code> of pattern types as keys and values being maps of values possibly to be put in message contents.
   */
  protected Map preparePatternsForContents(Map mapContents) {
    Map mapPatternsForContents = new HashMap();

    mapPatternsForContents.put(PATTERN_TYPE_ENTITY, mapContents);
    if (mapContents.get(FieldContainer.MC_ENTITY_VALIDATOR) != null) {
      mapPatternsForContents.put(PATTERN_TYPE_VALIDATOR, ((Validator) mapContents.get(FieldContainer.MC_ENTITY_VALIDATOR)).getPropertyValues());
    }
    if (mapContents.get(Form.MC_ENTITY_RULE) != null) {
      mapPatternsForContents.put(PATTERN_TYPE_RULE, mapContents.get(Form.MC_ENTITY_RULE));
    }
    if (mapContents.get(Form.MC_ENTITY_FORM) != null) {
      mapPatternsForContents.put(PATTERN_TYPE_FORM, mapContents.get(Form.MC_ENTITY_FORM));
    }
    mapPatternsForContents.put(PATTERN_TYPE_CUSTOM, customProperties);

    return mapPatternsForContents;
  }

  /**
   * Changes some characters in a string, if needed.
   *
   * @param str <code>String</code> to prefilter.
   * @return prefiltered <code>String</code>.
   */
  protected String prefilterString(String str) {
    return replaceBackwardCompatibleEntityPatterns(str);
  }

  /**
   * Gets object from given map of pattern types and objects, using given pattern type.
   *
   * <p>
   * Example:
   *   <code>patternType</code> is <em>rule</em>
   *   <code>fullPatternName</code> is <em>rule:object1:value</em>
   *
   * <code>patternValue</code> is value of <code>mapPatterns.get(<em>rule</em>).getField(<em>object1</em>).getValue()</code>.
   *
   * @param patternType pattern type.
   * @param fullPatternName full name of the pattern.
   * @param mapPatterns map of pattern types and objects.
   * @return object from map of pattern types or <code>null</code> if such object does not exist.
   */
  protected Object getPatternValue(String patternType, String fullPatternName, Map mapPatterns) {
    Object patternValue = null;

    if (PATTERN_TYPE_RULE.equals(patternType) || PATTERN_TYPE_FORM.equals(patternType)) {
      String patternFieldName = fullPatternName.substring(patternType.length() + 1);
      int indPatternFieldName = patternFieldName.indexOf(":");
      if (indPatternFieldName != -1) {
        String patternName = patternFieldName.substring(indPatternFieldName + 1);
        patternFieldName = patternFieldName.substring(0, indPatternFieldName);

        Map pattern = (Map) mapPatterns.get(patternType);
        if (pattern != null) {
          Map patternField = (Map) pattern.get(patternFieldName);
          if (patternField != null) {
            patternValue = patternField.get(patternName);
          }
        }
      }
    }

    return patternValue;
  }


  // Private methods

  /**
   * Replaces <code>MessageFormat</code> parameter numbers in given string with their name equivalents.
   *
   * Used for backward compatibility.
   *
   * <p>
   * Example:
   *   {2} goes to {entity:name}
   *
   * @param str <code>String</code> containing <code>MessageFormat</code> parameter numbers.
   * @return <code>String</code> with replaced parameters.
   */
  private String replaceBackwardCompatibleEntityPatterns(String str) {
    String result = str;

    for (Iterator iter = backwardCompatibleEntityPatterns.entrySet().iterator(); iter.hasNext();) {
      Map.Entry me = (Map.Entry) iter.next();
      result = result.replaceAll((String) me.getKey(), (String) me.getValue());
    }

    return result;
  }

  /**
   * Replaces full pattern names in a given string with consecutive <code>MessageFormat</code> parameter numbers.
   *
   * @param str <code>String</code> containing full pattern names.
   * @param map <code>Map</code> of full pattern names and values to replace - only keys are used.
   * @return <code>String</code> with replaced full pattern names.
   */
  private String replacePatterns(String str, Map map) {
    String result = str;

    int counter = 0;
    for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
      Map.Entry me = (Map.Entry) iter.next();
      String key = (String) me.getKey();
      result = result.replaceAll(key, "" + counter);
      counter++;
    }

    return result;
  }

  /**
   * Formats <code>MessageFormat</code>-compatible pattern with given values.
   *
   * @param strPattern <code>String</code> with <code>MessageFormat</code>-compatible pattern.
   * @param values array of objects to replace.
   * @return formatted <code>String</code>.
   */
  private String formatPatterns(String strPattern, Object[] values) {
    return MessageFormat.format(strPattern, values);
  }

  /**
   * Replaces given string containing extended message format patterns with values given in a map.
   *
   * @param str <code>String</code> to be replaced.
   * @param mapPatterns <code>Map</code> of pattern types.
   * @return <code>String</code> with replaced values.
   */
  private String convertPatterns(String str, Map mapPatterns) {
    // Prefilter string
    String strPrefiltered = prefilterString(str);

    // Match string against pattern
    Matcher matcher = pattern.matcher(strPrefiltered);

    // Initialize map of pattern values to replace in the string
    Map patternValues = new TreeMap(new PatternKeysComparator());

    while (matcher.find()) {
      String group = matcher.group();
      // Retrieve next match, e.g. entity:name or rule:object1:value
      String fullPatternName = group.substring(1, group.length() - 1);

      // Retrieve patternType, e.g. entity or rule
      int indPatternType = fullPatternName.indexOf(":");
      String patternType = indPatternType != -1 ? fullPatternName.substring(0, indPatternType) : fullPatternName;

      // Try to get value for the match found, e.g. value of rule:object1:value
      Object patternValue = getPatternValue(patternType, fullPatternName, mapPatterns);

      // If the value is not found, find the value explicitly from the map of patterns, e.g. value of entity:name
      if (patternValue == null) {
        String patternName = fullPatternName.substring(indPatternType + 1);
        Object patternTypeObject = mapPatterns.get(patternType);
        if (patternTypeObject != null && patternTypeObject instanceof Map && ((Map) patternTypeObject).get(patternName) != null) {
          patternValue = ((Map) patternTypeObject).get(patternName);
        }
      }

      // If the value is found, put it to the map of pattern values
      if (patternValue != null) {
        if (patternValues.get(fullPatternName) == null) {
          patternValues.put(fullPatternName, patternValue);
        }
      } else {
        patternValues.put(fullPatternName, "");
      }
    }

    // Replace all e.g. entity:name or rule:object1:value with consecutive numbers needed for <code>MessageFormat</code> class.
    String strWithNumbers = replacePatterns(strPrefiltered, patternValues);

    // Return string formatted with values, using <code>MessageFormat</code> class.
    return formatPatterns(strWithNumbers, patternValues.values().toArray());
  }


  // Inner classes

  /**
   * Class for comparing pattern keys.
   *
   * It sorts keys in descending order for the longest key to be caught.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  class PatternKeysComparator implements Comparator, Serializable {

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object obj1, Object obj2) {
      return -((String) obj1).compareTo((String) obj2);
    }

  }

} // pl.aislib.util.messages.MessageFormatConverter class
