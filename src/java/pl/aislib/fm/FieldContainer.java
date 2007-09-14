package pl.aislib.fm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.logging.Log;

import pl.aislib.fm.forms.Field;
import pl.aislib.fm.forms.IEntity;
import pl.aislib.fm.forms.ValidateException;
import pl.aislib.fm.messages.IMessage;
import pl.aislib.fm.messages.IMessageConverter;
import pl.aislib.util.Pair;
import pl.aislib.util.messages.StandardMessageConverter;

/**
 * Field container class.
 *
 * Please note the following:
 * Messages from forms' validation can be converted using implementation of
 * <code>IMessageConverter</code> interface. <br>
 *
 * One can use <code>MessageFormatConverter</code> to do that. In this case,
 * message format arguments should be used for each message in the following way:
 * <ul>
 *   <li>{1} - name of the entity (field or rule for which the message is generated)</li>
 *   <li>{2} - name of the dynamic entity (equals to name of the entity for non-dynamic fields or rules)</li>
 *   <li>{3} - number of the dynamic entity in collection of dynamic fields and rules (starting with 0)</li>
 *   <li>{4} - number of the dynamic entity in collection of dynamic fields and rules (starting with 1)</li>
 *   <li>{5} - suffix number of the dynamic entity</li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.8 $
 */
public abstract class FieldContainer {

  // String constants describing things to be put to message converter

  /** */
  public static final String MC_ENTITY = "mc_entity";

  /** */
  public static final String MC_ENTITY_NAME = "mc_entity_name";

  /** */
  public static final String MC_DYNAMIC_ENTITY_NAME = "mc_dynamic_entity_name";

  /** */
  public static final String MC_DYNAMIC_ENTITY_NUMBER = "mc_dynamic_entity_number";

  /** */
  public static final String MC_DYNAMIC_ENTITY_NUMBER_1 = "mc_dynamic_entity_number_1";

  /** */
  public static final String MC_DYNAMIC_ENTITY_NAME_SUFFIX = "mc_dynamic_entity_name_suffix";


  // Protected fields

  /**
   * Name of the container.
   */
  protected String name;

  /**
   * Map of fields.
   */
  protected Map fields;

  /**
   * Map of messages (codes and objects).
   */
  protected Map messages;

  /**
   * Groups of messages (codes and list of messages).
   */
  protected Map messageGroups;

  /**
   * Map of field values, converted after successful validation.
   */
  protected Map values;

  /**
   * Map of original field values.
   */
  protected Map originalValues;

  /**
   * Map of field names and booleans.
   * Values of the map state whether fields (i.e. keys) have been validated.
   */
  protected Map validatedFields;

  /**
   * Map of messages from non-validated entities.
   * Keys are entity names.
   * Values are messages.
   */
  protected Map messagesMap;

  /**
   * Map of messages from non-validated entities.
   * Same as messages map but messages are added in an ordered manner.
   */
  protected Map orderedMessagesMap;

  /**
   * Language of messages.
   */
  protected String lang;

  /**
   * Logging object.
   */
  protected Log log;

  /**
   * True if empty fields should be given null value.
   */
  protected boolean bTreatEmptyAsNull = false;


  // Constructors

  /**
   * @param name name of the field.
   */
  public FieldContainer(String name) {
    this.name = name;

    fields = new SequencedHashMap();
    messages = new SequencedHashMap();
    messageGroups = new SequencedHashMap();
  }


  // Public methods

  /**
   * @param field field object.
   */
  public void addField(Field field) {
    fields.put(field.getName(), field);
  }

  /**
   * @param fieldName name of a field.
   * @return the field.
   */
  public Field getField(String fieldName) {
    return (Field) fields.get(fieldName);
  }

  /**
   * @return name of the container.
   */
  public String getName() {
    return name;
  }

  /**
   * @param fieldName name of a field.
   * @return original value of the field. It makes sense only when the field has been tried to be validated.
   */
  public String getOriginalValue(String fieldName) {
    return originalValues != null ? (String) originalValues.get(fieldName) : null;
  }

  /**
   * @return original field values.
   */
  public Map getOriginalValues() {
    return originalValues;
  }

  /**
   * @return map of validated fields.
   */
  public Map getValidatedFields() {
    return validatedFields;
  }

  /**
   * @param fieldName name of a field.
   * @return value of the field. It makes sense only when the field has been validated.
   */
  public Object getValue(String fieldName) {
    return getValue(fieldName, bTreatEmptyAsNull);
  }

  /**
   *
   * @param fieldName name of a field.
   * @param treatEmptyAsNull true if empty field should be given null value.
   * @return Object
   */
  public Object getValue(String fieldName, boolean treatEmptyAsNull) {
    if (values != null) {
      Object value = values.get(fieldName);
      return treatEmptyAsNull && isValueEmpty(value) ? null : value;
    }

    return null;
  }

  /**
   * @return map of field values, converted after successful validation.
   */
  public Map getValues() {
    return getValues(bTreatEmptyAsNull);
  }

  /**
   * @param treatEmptyAsNull true if empty fields should be given null value.
   * @return map of field values.
   */
  public Map getValues(boolean treatEmptyAsNull) {
    if (!treatEmptyAsNull || values == null) {
      return values;
    }

    Map result = new SequencedHashMap(values);

    for (Iterator i = result.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String fieldName = (String) me.getKey();
      boolean fieldValidated = isFieldValidated(fieldName);
      if (!fieldValidated || (fieldValidated && isValueEmpty(me.getValue()))) {
        result.put(fieldName, null);
      }
    }

    return result;
  }

  /**
   *
   * @param bTreatEmptyAsNull true if empty fields should be given null value.
   */
  public void treatEmptyAsNull(boolean bTreatEmptyAsNull) {
    this.bTreatEmptyAsNull = bTreatEmptyAsNull;
  }

  /**
   *
   * @param value object to be checked against emptiness
   * @return true if the object is empty.
   */
  protected boolean isValueEmpty(Object value) {
    if (value == null) {
      return true;
    }

    if (value instanceof String && ((String) value).trim().length() == 0) {
      return true;
    }

    if (value instanceof String[] && ((String[]) value).length == 0) {
      return true;
    }

    return false;
  }

  /**
   * @param fieldName name of a field.
   * @return true if the field has been validated successfully.
   */
  public boolean isFieldValidated(String fieldName) {
    Boolean b = (Boolean) validatedFields.get(fieldName);
    return (b != null ? b.booleanValue() : false);
  }

  /**
   * @param log logging object.
   */
  public void setLog(Log log) {
    this.log = log;
  }

  /**
   * Validates fields in the container.
   *
   * @param fieldValues map of original string values.
   * @return true if all fields have been successfully validated.
   */
  public boolean validate(Map fieldValues) {
    return validate(fieldValues, null);
  }

  /**
   * Validates fields in the container.
   *
   * @param fieldValues map of original string values.
   * @param data an object specific for validators.
   * @return true if all fields have been successfully validated.
   */
  public boolean validate(Map fieldValues, Object data) {
    return preValidate(fieldValues, data)
         && doValidate(fieldValues, data)
         && postValidate(fieldValues, data);
  }

  /**
   * Validates fields in the container.
   *
   * @param field a field.
   * @param data an object specific for validators.
   * @param fieldValues map of original string values.
   * @return true if the field has been successfully validated.
   */
  public boolean validateField(Field field, Object data, Map fieldValues) {
    String fieldName = field.getName();
    Object fieldValue = null;

    if (field.isComplex() && field.isDynamic()) {
      Map fValues = new SequencedHashMap(fieldValues);
      Map builderMapping = field.getBuilder().getMapping();

      //int maxIndex = -1;

      for (Iterator i = builderMapping.entrySet().iterator(); i.hasNext();) {
        Map.Entry me = (Map.Entry) i.next();
        String partFieldName = (String) me.getValue();
        constrainValues(fValues, partFieldName);
      }

      boolean result = true;

      List l = orderFieldValues(values, builderMapping, fValues);
      for (Iterator i = l.iterator(); i.hasNext();) {
        Pair pair = (Pair) i.next();
        int j = ((Integer) pair.getFirst()).intValue();
        Map builderValues = (Map) pair.getSecond();
        if (builderValues != null) {
          fieldValue = field.getBuilder().join(builderValues);
          result &= validateField(field, field.getName() + j, data, fieldValue);
        }
      }

      return result;
    } else if (field.isComplex()) {
      Map builderValues = constrainValues(values, field.getBuilder().getMapping(), true);
      if (builderValues == null) {
        // No validation is performed, because null is returned when
        // at least one of base fields has not been validated
        return true;
      }
      fieldValue = field.getBuilder().join(builderValues);
    } else if (field.isDynamic()) {
      Map fValues = new SequencedHashMap(fieldValues);
      Map constrainedValues = constrainValues(fValues, field.getName());
      boolean result = true;
      for (Iterator i = constrainedValues.entrySet().iterator(); i.hasNext();) {
        Map.Entry me = (Map.Entry) i.next();
        result &= validateField(field, (String) me.getKey(), data, me.getValue());
      }
      return result;
    } else {
      fieldValue = fieldValues.get(fieldName);
    }

    return validateField(field, data, fieldValue);
  }

  /**
   * Validates a single field in the container.
   *
   * @param field a field.
   * @param data an object specific for validators.
   * @param fieldValue value of the field.
   * @return true if the field has been successfully validated.
   */
  public boolean validateField(Field field, Object data, Object fieldValue) {
    return validateField(field, field.getName(), data, fieldValue);
  }

  /**
   * Validates a single field in the container.
   *
   * @param field a field.
   * @param fieldName name of the field (used primarily for dynamic fields).
   * @param data an object specific for validators.
   * @param fieldValue value of the field.
   * @return true if the field has been successfully validated.
   */
  public boolean validateField(Field field, String fieldName, Object data, Object fieldValue) {
    stamp("Validating field: " + fieldName + " ...");

    int valuesType = field.getValuesType();

    if (fieldValue == null) {
      if (valuesType == Field.VT_SINGLE) {
        fieldValue = new String();
      } else {
        fieldValue = new String[0];
      }
    }

    try {
      originalValues.put(fieldName, fieldValue);
      validatedFields.put(fieldName, Boolean.TRUE);
      switch (valuesType) {
        case Field.VT_SINGLE:
          if (fieldValue instanceof String) {
            values.put(fieldName, field.validate((String) fieldValue, messagesMap, fieldName, data));
            return true;
          } else if (fieldValue instanceof String[]) {
            String[] arr = (String[]) fieldValue;
            originalValues.put(fieldName, arr[0]);
            values.put(fieldName, field.validate(arr[0], messagesMap, fieldName, data));
            return true;
          } else {
            validatedFields.put(fieldName, Boolean.FALSE);
            return false;
          }
        case Field.VT_ARRAY:
          if (fieldValue instanceof String) {
            String[] arr = new String[1];
            arr[0] = (String) fieldValue;
            originalValues.put(fieldName, arr);
            values.put(fieldName, field.validate(arr, messagesMap, fieldName, data));
            return true;
          } else if (fieldValue instanceof String[]) {
            values.put(fieldName, field.validate((String[]) fieldValue, messagesMap, fieldName, data));
            return true;
          } else {
            validatedFields.put(fieldName, Boolean.FALSE);
            return false;
          }
        default :
          if (fieldValue instanceof String) {
            values.put(fieldName, fieldValue);
            return true;
          }
          if (fieldValue instanceof String[]) {
            String[] aValue = (String[]) fieldValue;
            if (aValue.length == 1) {
              values.put(fieldName, aValue[0]);
            } else {
              values.put(fieldName, aValue);
            }
            return true;
          }
          validatedFields.put(fieldName, Boolean.FALSE);
          return false;
      }
    } catch (ValidateException ve) {
      orderedMessagesMap.put(fieldName, ((SequencedHashMap) messagesMap).getLastValue());
      validatedFields.put(fieldName, Boolean.FALSE);
      stamp("Field '" + fieldName + "' has not been successfully validated. Returned message: " + ve.getMessage());
      return false;
    }
  }


  // Protected methods

  /**
   * Contains all necessary steps taken before the proper validation of fields in the container.
   *
   * @param fieldValues map of field values.
   * @param data an object specific for validators.
   * @return true if all fields have been successfully pre-validated.
   */
  protected boolean preValidate(Map fieldValues, Object data) {
    values = new SequencedHashMap();
    originalValues = new SequencedHashMap();
    validatedFields = new SequencedHashMap();
    messagesMap = new SequencedHashMap();
    orderedMessagesMap = new SequencedHashMap();
    return true;
  }

  /**
   * Main step of validation of fields in the container.
   *
   * @param fieldValues map of field values.
   * @param data an object specific for validators.
   * @return true if all fields have been successfully validated.
   */
  protected abstract boolean doValidate(Map fieldValues, Object data);

  /**
   * Contains all necessary steps taken after the proper validation of fields in the container.
   * @param fieldValues map of field values.
   * @param data an object specific for validators.
   * @return true if all fields have been successfully post-validated.
   */
  protected boolean postValidate(Map fieldValues, Object data) {
    return true;
  }

  /**
   * @param fieldValues map of field values, as a reference.
   * @param fieldName name of a field.
   * @return map of values of fields that start with the given name.
   */
  protected Map constrainValues(Map fieldValues, String fieldName) {
    Map result = new TreeMap();

    int maxIndex = -1;
    try {
      maxIndex = Integer.parseInt((String) fieldValues.get(fieldName + "_count"));
    } catch (Exception e) {
      ;
    }

    for (Iterator i = fieldValues.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String key = (String) me.getKey();
      if (key.startsWith(fieldName)) {
        try {
          int index = Integer.parseInt(key.substring(fieldName.length()));
          if (index > maxIndex) {
            maxIndex = index;
          }
          result.put(key, me.getValue());
        } catch (Exception e) {
          ;
        }
      }
    }

    for (int i = 1; i <= maxIndex; i++) {
      if (!fieldValues.containsKey(fieldName + i)) {
        result.put(fieldName + i, null);
      }
    }

    return new SequencedHashMap(result);
  }

  /**
   * Method constrainValues.
   * @param values map of values.
   * @param mapping a map.
   * @param checkValidation true if values should be checked against having been validated.
   * @return map of constrained values.
   */
  protected Map constrainValues(Map values, Map mapping, boolean checkValidation) {
    Map result = new SequencedHashMap();
    for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String value = (String) me.getValue();
      if (checkValidation && !isFieldValidated(value)) {
        return null;
      }
      result.put(me.getKey(), values.get(value));
    }
    return result;
  }

  /**
   * @param fieldValues map of field values.
   * @param data an object specific for validators.
   * @return true if all complex fields have been successfully validated.
   */
  protected boolean validateComplexFields(Map fieldValues, Object data) {
    boolean result = true;

    for (Iterator i = fields.entrySet().iterator(); i.hasNext();) {
      Field field = (Field) ((Map.Entry) i.next()).getValue();
      if (field.isConditional() || !field.isComplex()) {
        continue;
      }

      boolean bFieldValidated = validateField(field, data, fieldValues);

      validatedFields.put(field.getName(), new Boolean(bFieldValidated));
      result &= bFieldValidated;
    }

    return result;
  }

  /**
   * @param fieldValues map of field values.
   * @param data an object specific for validators.
   * @return true if all non-complex fields have been successfully validated.
   */
  protected boolean validateNonComplexFields(Map fieldValues, Object data) {
    boolean result = true;

    for (Iterator i = fields.entrySet().iterator(); i.hasNext();) {
      Field field = (Field) ((Map.Entry) i.next()).getValue();
      if (field.isConditional() || field.isComplex()) {
        continue;
      }

      boolean bFieldValidated = validateField(field, data, fieldValues);

      validatedFields.put(field.getName(), new Boolean(bFieldValidated));
      result &= bFieldValidated;
    }

    return result;
  }

  /**
   * @param str log message.
   */
  protected void stamp(String str) {
    if (log != null && log.isDebugEnabled()) {
      log.debug(str);
    }
  }


  // wswiatek: I would like to make those methods deprecated

  /**
   * @return iterator of message codes.
   */
  public Iterator getErrorCodes() {
    return getMessageCodes();
  }

  /**
   * @param fieldName name of a field.
   * @return message for the field.
   */
  public IMessage getErrorMessage(String fieldName) {
    return (IMessage) messages.get(messagesMap.get(fieldName));
  }

  /**
   * @return map of messages.
   */
  public Map getErrorMessages() {
    return getErrorMessages(null);
  }

  /**
   * This method should be called when one wants to use dynamic fields or rules.
   *
   * Two items are put into message converter values: the whole entity and a real entity name.
   * Same objects go as message converter keys.
   *
   * @param messageConverter message converter object.
   * @return map of messages with converted key and content.
   */
  public Map getErrorMessages(IMessageConverter messageConverter) {
    Map result = new SequencedHashMap();

    for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
      String fieldName = (String) i.next();
      result.putAll(getEntityMessages(fieldName, messageConverter));
    }

    return result;
  }

  /**
   * @param entityName name of a field or a rule.
   * @param messageConverter message converter object.
   * @return map of messages for given entity (the entity may be dynamic).
   */
  protected Map getEntityMessages(String entityName, IMessageConverter messageConverter) {
    if (messageConverter == null) {
      messageConverter = new StandardMessageConverter();
    }

    Map result = new SequencedHashMap();

    IEntity entity = getEntity(entityName);

    if (entity == null) {
      return result;
    }

    Map msgKeys = new SequencedHashMap();
    Map msgValues = new SequencedHashMap();

    msgKeys.put(MC_ENTITY, entity);
    msgKeys.put(MC_ENTITY_NAME, entityName);

    msgValues.put(MC_ENTITY, entity);
    msgValues.put(MC_ENTITY_NAME, entityName);

    Iterator entitiesIterator = null;
    if (entity.isDynamic()) {
      entitiesIterator = getDynamicEntityNames(entityName);
    } else {
      List entities = new ArrayList();
      entities.add(entityName);
      entitiesIterator = entities.iterator();
    }

    int counter = 0;
    for (Iterator i = entitiesIterator; i.hasNext(); counter++) {
      String entName = (String) i.next();
      String entNameSuffix = entName.substring(entName.lastIndexOf("_") + 1);
      Integer errorCode = (Integer) orderedMessagesMap.get(entName);
      Message message = (Message) messages.get(errorCode);

      msgKeys.put(MC_DYNAMIC_ENTITY_NAME, entName);
      msgKeys.put(MC_DYNAMIC_ENTITY_NUMBER, new Integer(counter));
      msgKeys.put(MC_DYNAMIC_ENTITY_NUMBER_1, new Integer(counter + 1));
      msgKeys.put(MC_DYNAMIC_ENTITY_NAME_SUFFIX, entNameSuffix);

      msgValues.put(MC_DYNAMIC_ENTITY_NAME, entName);
      msgValues.put(MC_DYNAMIC_ENTITY_NUMBER, new Integer(counter));
      msgValues.put(MC_DYNAMIC_ENTITY_NUMBER_1, new Integer(counter + 1));
      msgValues.put(MC_DYNAMIC_ENTITY_NAME_SUFFIX, entNameSuffix);

      // First, check not grouped messages
      if (message != null) {
        IMessage cMessage = messageConverter.convert(message, lang, msgKeys, msgValues);
        result.put(cMessage.getKey(), cMessage.getContent());
      } else {
        // Then, check grouped messages
        List messageGroup = (List) messageGroups.get(errorCode);
        if (messageGroup != null) {
          for (Iterator j = messageGroup.iterator(); j.hasNext();) {
            Message gMessage = (Message) j.next();
            if (gMessage != null) {
              IMessage cMessage = messageConverter.convert(gMessage, lang, msgKeys, msgValues);
              result.put(cMessage.getKey(), cMessage.getContent());
            }
          }
        }
      }
    }

    return result;
  }

  /**
   * @param entityName name of an entity.
   * @return the entity.
   */
  protected IEntity getEntity(String entityName) {
    return (IEntity) fields.get(entityName);
  }

  /**
   * @param entityName name of a dynamic entity.
   * @return list of all names of entity's instances.
   */
  protected List getDynamicEntityNamesList(String entityName) {
    List result = new ArrayList();

    for (Iterator i = validatedFields.keySet().iterator(); i.hasNext();) {
      String fieldName = (String) i.next();
      if (fieldName.startsWith(entityName)) {
        result.add(fieldName);
      }
    }

    return result;
  }

  /**
   * @param entityName name of a dynamic entity.
   * @return iterator for all names of entity's instances.
   */
  protected Iterator getDynamicEntityNames(String entityName) {
    return getDynamicEntityNamesList(entityName).iterator();
  }

  /**
   * @return iterator of field names.
   */
  public Iterator getFieldNames() {
    return fields.keySet().iterator();
  }

  /**
   * @param fieldName of a field.
   * @return message code for the field.
   */
  public int getMessageCode(String fieldName) {
    return ((Integer) messagesMap.get(fieldName)).intValue();
  }

  /**
   * @return iterator of message codes.
   */
  public Iterator getMessageCodes() {
    return orderedMessagesMap.values().iterator();
  }

  /**
   * @return map of original values.
   */
  public Map getStringValues() {
    return getOriginalValues();
  }

  /**
   * @param fieldValues string values of fields.
   * @param fieldName name of a field.
   * @return list of consecutive numbers of entity's instances.
   */
  protected List getDynamicNumbers(Map fieldValues, String fieldName) {
    List result = new ArrayList();
    for (Iterator i = fieldValues.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String key = (String) me.getKey();
      if (key.startsWith(fieldName)) {
        try {
          Integer j = Integer.valueOf(key.substring(fieldName.length()));
          result.add(j);
        } catch (Exception e) {
          ;
        }
      }
    }
    Collections.sort(result);
    return result;
  }

  /**
   * @param values values of fields.
   * @param mapping mapping.
   * @param fieldValues string values of fields.
   * @return list of pairs: number and map of values of fields for entity's instance.
   */
  protected List orderFieldValues(Map values, Map mapping, Map fieldValues) {
    List result = new ArrayList();

    List l = new ArrayList();
    boolean bDynamicFound = false;
    boolean bNoDynamicFound = true;
    for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String fieldName = (String) me.getValue();
      Field field = (Field) fields.get(fieldName);
      if (field.isDynamic()) {
        bNoDynamicFound = false;
        if (field.isConditional() && field.isComplex()) {
          for (Iterator j = field.getBuilder().getMapping().entrySet().iterator(); j.hasNext();) {
            Map.Entry me2 = (Map.Entry) j.next();
            String partFieldName = (String) me2.getValue();
            //Field partField = (Field) fields.get(partFieldName);
            if (!bDynamicFound) {
              l = getDynamicNumbers(fieldValues, partFieldName);
              bDynamicFound = true;
            } else {
              l.retainAll(getDynamicNumbers(fieldValues, partFieldName));
            }
          }
        } else {
          if (!bDynamicFound) {
            l = getDynamicNumbers(
                  (isFieldValidated(fieldName) || fieldValues == null) ? values : fieldValues,
                  fieldName
                );
            bDynamicFound = true;
          } else {
            l.retainAll(
              getDynamicNumbers(
                (isFieldValidated(fieldName) || fieldValues == null) ? values : fieldValues,
                fieldName
              )
            );
          }
        }
      }
    }

    if (bNoDynamicFound) {
      l.add(new Integer(-1));
    }

    for (Iterator i = l.iterator(); i.hasNext();) {
      int j = ((Integer) i.next()).intValue();
      Map map = new HashMap();
      for (Iterator k = mapping.entrySet().iterator(); k.hasNext();) {
        Map.Entry me = (Map.Entry) k.next();
        String fieldName = (String) me.getValue();
        Field field = (Field) fields.get(fieldName);
        if (isFieldValidated(fieldName) || fieldValues == null) {
          if (field.isDynamic()) {
            map.put(me.getKey(), values.get(fieldName + j));
          } else {
            map.put(me.getKey(), values.get(fieldName));
          }
        } else {
          if (field.isDynamic()) {
            map.put(me.getKey(), fieldValues.get(fieldName + j));
          } else {
            map.put(me.getKey(), fieldValues.get(fieldName));
          }
        }
      }
      result.add(new Pair(new Integer(j), map));
    }

    return result;
  }

} // FieldContainer class
