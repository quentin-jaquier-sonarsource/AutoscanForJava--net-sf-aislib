package pl.aislib.fm.forms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import pl.aislib.fm.Form;
import pl.aislib.fm.forms.config.IRule;

/**
 * Describes rule which should be checked for {@link pl.aislib.fm.Form}.
 *
 * <p>
 * A rule has:
 * <ul>
 *   <li>name - should be unique for every rule</li>
 *   <li>mapping of parameters</li>
 * </ul>
 *
 * A rule has {@link Rule#validate(Map)} method which checks if values of fields
 * that are mentioned in mapping fall under specified criteria.
 *
 * @author
 * <table>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.8 $
 * @since AISLIB 0.1
 */
public abstract class Rule implements IRule, IEntity {

  // Rule types

  /**
   * Rule's normal property value.
   */
  public static final int RT_NORMAL = 0;

  /**
   * Rule's dynamic property value.
   */
  public static final int RT_DYNAMIC = 1;

  /**
   * Holds name of the rule.
   */
  protected String name;

  /**
   * Type of the rule.
   */
  protected int type;

  /**
   * Holds mappings between rule parameters and form fields.
   */
  protected Map mapping;

  /**
   * True if rule is conditional.
   */
  protected boolean checkCondition = false;

  /**
   * Map of fields that are to be validated only if other fields allow them to.
   */
  protected Map fieldsValidatedConditionally = null;

  /**
   * Message code.
   */
  private int msgCode;

  /**
   * True, if all fields have been validated.
   */
  protected boolean allFieldsValidated;


  // Constructors

  /**
   * Contructor used internally.
   *
   * This constructor is used only when creating Rules during XML file parsing.
   */
  protected Rule() {
    mapping = new HashMap();

    type = RT_NORMAL;
  }

  /**
   * @param name name of the rule.
   */
  public Rule(String name) {
    this();

    this.name = name;
  }


  // Public methods

  /**
   * @return true if all fields have been validated.
   */
  public boolean areAllFieldsValidated() {
    return allFieldsValidated;
  }

  /**
   * Puts fields that are to be validated conditionally into fieldsValidatedConditionally map.
   */
  public void createFieldsValidatedConditionally() {
    if (mapping == null) {
      return;
    }

    fieldsValidatedConditionally = new HashMap();
    fieldsValidatedConditionally.putAll(mapping);

    List fields = getFieldsValidatedUnconditionally();

    if (fields != null) {
      for (Iterator i = fields.iterator(); i.hasNext();) {
        String ruleParameter = (String) i.next();
        fieldsValidatedConditionally.remove(ruleParameter);
      }
    }
  }

  /**
   * @return map of fields that are to be validated conditionally.
   */
  public Map getFieldsValidatedConditionally() {
    return fieldsValidatedConditionally;
  }

  /**
   * @return true if the rule is dynamic.
   */
  public boolean isDynamic() {
    return (type & RT_DYNAMIC) == RT_DYNAMIC;
  }

  /**
   * Changes type of the rule to dynamic, if necessary.
   *
   * @param form <code>Form</code> object.
   * @throws SAXException if there is a parsing problem.
   */
  public void setDynamic(Form form) throws SAXException {
    if (mapping == null) {
      return;
    }

    boolean bDynamic = false;
    for (Iterator i = mapping.values().iterator(); i.hasNext();) {
      String fieldName = (String) i.next();
      Field field = form.getField(fieldName);

      if (field == null) {
        throw new SAXException("Field " + fieldName + " does not exist, check rule mapping");
      }
      if (field.isDynamic()) {
        bDynamic = true;
        break;
      }
    }

    if (bDynamic) {
      type |= RT_DYNAMIC;
    }
  }

  /**
   * @return message code.
   */
  public int getMsgCode() {
    return msgCode;
  }

  /**
   * @param msgCode message code.
   */
  public void setMsgCode(int msgCode) {
    this.msgCode = msgCode;
  }

  /**
   * @return name of the rule.
   */
  public String getName() {
    return name;
  }

  /**
   * @param name name of the rule.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return type of the rule.
   */
  public int getType() {
    return type;
  }

  /**
   * @param type type of the rule.
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * @param ruleParameter common key.
   * @param fieldName name of the field that must be converted.
   */
  public void addMapping(String ruleParameter, String fieldName) {
    mapping.put(ruleParameter, fieldName);
  }

  /**
   * @return rule's mapping.
   */
  public Map getMapping() {
    return mapping;
  }

  /**
   * @param mapping rule's mapping.
   */
  public void setMapping(Map mapping) {
    this.mapping.putAll(mapping);
  }

  /**
   * @return true if the rule is to be validated only under certain condition.
   */
  public boolean isCheckCondition() {
    return checkCondition;
  }

  /**
   * @param checkCondition true if the rule is to be validated only under certain condition.
   */
  public void setCheckCondition(boolean checkCondition) {
    this.checkCondition = checkCondition;
  }

  /**
   * Validates the rule.
   *
   * @param values values of fields. Note that in order for this method to be called, all fields must be validated.
   * @return true if the rule has been successfully validated.
   */
  public abstract boolean validate(Map values);

  /**
   * Validates the rule, called from {@link pl.aislib.fm.Form#validateRule(Rule, Map, Map)}.
   *
   * This is actually the main validation method for the rule.
   * At first, all not conditional fields have to be validated.<br>
   * Then the method checks whether the rule is 'conditional'.
   * If not, it just calls {@link #validate(Map)}.
   * <br>
   * Otherwise, if the condition has not been met, the rule is not validated.
   * <br>
   * Otherwise, the rule must be sure all fields have been validated before being validated itself.
   *
   * @param form form the rule is child of.
   * @param fieldValues original values of fields.
   * @param values converted values of fields, i.e. after validation.
   * @param number consecutive number of the dynamic rule, this field is used for dynamic rules only.
   * @return true if the rule has been successfully validated.
   */
  public final boolean doValidate(Form form, Map fieldValues, Map values, int number) {
    boolean bDoValidateRule = true;
    for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Field field = form.getField((String) me.getValue());
      if (!field.isConditional()) {
        String fieldName = field.getName();
        if (field.isDynamic()) {
          fieldName += number;
        }
        Boolean bFieldValidated = (Boolean) form.getValidatedFields().get(fieldName);
        if (bFieldValidated != null && !bFieldValidated.booleanValue()) {
          bDoValidateRule = false;
        }
      }
    }

    allFieldsValidated = true;

    if (!bDoValidateRule) {
      // Rule has not been tried to be validated
      return true;
    }

    if (!isCheckCondition()) {
      return validate(values);
    }

    if (!validateConditional(values)) {
      // Rule has not been tried to be validated
      return true;
    }

    for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Field field = form.getField((String) me.getValue());
      if (field.isConditional()) {
        if (field.isComplex() && field.isDynamic()) {
          Map builderMapping = field.getBuilder().getMapping();
          Map builderValues = new HashMap();
          for (Iterator j = builderMapping.entrySet().iterator(); j.hasNext();) {
            Map.Entry me2 = (Map.Entry) j.next();
            Field partField = form.getField((String) me2.getValue());
            if (partField.isDynamic()) {
              builderValues.put(me2.getKey(), fieldValues.get((String) me2.getValue() + number));
            } else {
              builderValues.put(me2.getKey(), fieldValues.get(me2.getValue()));
            }
          }
          allFieldsValidated
            &= form.validateField(
                 field,
                 field.getName() + number,
                 null,
                 field.getBuilder().join(builderValues)
               );
        } else if (field.isComplex()) {
          Map builderMapping = field.getBuilder().getMapping();
          Map builderValues = new HashMap();
          for (Iterator j = builderMapping.entrySet().iterator(); j.hasNext(); ) {
            Map.Entry me2 = (Map.Entry) j.next();
            String partFieldName = (String) me2.getValue();
            if (form.isFieldValidated(partFieldName)) {
              builderValues.put(me2.getKey(), form.getValue(partFieldName, true));
            } else {
              Field partField = form.getField(partFieldName);
              allFieldsValidated &= form.validateField(partField, null, fieldValues);
              if (allFieldsValidated) {
                builderValues.put(me2.getKey(), form.getValue(partFieldName, true));
              }
            }
          }
          if (allFieldsValidated) {
            allFieldsValidated &= form.validateField(field, null, field.getBuilder().join(builderValues));
          }
        } else {
          allFieldsValidated
            &= form.validateField(
                 field,
                 field.isDynamic() ? field.getName() + number : field.getName(),
                 null,
                 fieldValues.get(field.isDynamic() ? field.getName() + number : field.getName())
               );
        }
      }
    }

    return allFieldsValidated ? validate(useMapping(form, form.getValues(), number)) : false;
  }


  // Protected methods

  /**
   * @return a list of fields that are always validated in the rule.
   */
  protected List getFieldsValidatedUnconditionally() {
    return null;
  }

  /**
   * Gets proper values of the rule's fields from the mapping before the rule is being validated.
   *
   * @param form a form.
   * @param values a map.
   * @param number an int.
   * @return a map.
   */
  protected Map useMapping(Form form, Map values, int number) {
    Map result = new HashMap();
    for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Field field = form.getField((String) me.getValue());
      if (field.isDynamic()) {
        result.put(me.getKey(), values.get((String) me.getValue() + number));
      } else {
        result.put(me.getKey(), values.get(me.getValue()));
      }
    }
    return result;
  }

  public Map getDynamicFieldNameMapping(Form form, int number) {
    Map result = new HashMap();
    for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Field field = form.getField((String) me.getValue());
      if (field.isDynamic()) {
        result.put(me.getKey(), (String) me.getValue() + number);
      } else {
        result.put(me.getKey(), me.getValue());
      }
    }
    return result;
  }

  /**
   * @param values a map of values of fields.
   * @return true if the condition under which the rule can be validated, has been fulfilled.
   */
  protected boolean validateConditional(Map values) {
    return true;
  }

} // Rule class
