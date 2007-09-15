package pl.aislib.fm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.aislib.fm.forms.Field;
import pl.aislib.fm.forms.IEntity;
import pl.aislib.fm.forms.Rule;
import pl.aislib.fm.messages.IMessageConverter;
import pl.aislib.util.Pair;

/**
 * Class of forms.
 *
 * A form is a set of fields together with a set of rules.
 * Form is said to be validated if all fields and rules are appropriately validated.
 *
 * @author
 * <table>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.8 $
 * @since AISLIB 0.1
 */
public class Form extends FieldContainer {

  /** */
  public static final String MC_ENTITY_RULE = "rule";


  /**
   * Map of rules.
   */
  protected Map rules;

  /**
   * Map of rule names and booleans.
   */
  protected Map validatedRules;


  // Constructors

  /**
   * @see pl.aislib.fm.FieldContainer#FieldContainer(String)
   */
  public Form(String name) {
    super(name);

    messageGroups = new LinkedHashMap();
    rules = new LinkedHashMap();
  }


  // Public methods

  /**
   * @param rule <code>Rule</code> object.
   */
  public void addRule(Rule rule) {
    rules.put(rule.getName(), rule);
  }


  // Protected methods

  /**
   * @param rule <code>Rule</code> object.
   * @return true if the rule can be validated.
   */
  protected boolean checkRuleForValidation(Rule rule) {
    for (Iterator i = rule.getMapping().entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String value = (String) me.getValue();
      if (!isFieldValidated(value)) {
        return false;
      }
    }
    return true;
  }

  /**
   * @see java.lang.Object#clone()
   */
  protected Object clone() throws CloneNotSupportedException {
    Form form = new Form(name);

    form.messages = messages;
    form.messageGroups = messageGroups;
    form.fields = fields;
    form.rules = rules;
    form.log = log;
    form.bTreatEmptyAsNull = bTreatEmptyAsNull;

    return form;
  }

  /**
   * Form validates all non-complex fields first, then all complex fields.
   * Then it tries to validate all rules.
   *
   * @see pl.aislib.fm.FieldContainer#doValidate(Map, Object)
   */
  protected boolean doValidate(Map fieldValues, Object data) {
    boolean result = true;

    result &= validateNonComplexFields(fieldValues, data);
    result &= validateComplexFields(fieldValues, data);
    result &= validateRules(fieldValues);

    return result;
  }

  /**
   * @param rule a Rule object
   * @param fieldValues map of string field values.
   * @param values map of values.
   * @return true if rule has been successfully validated.
   */
  protected boolean validateRule(Rule rule, Map fieldValues, Map values) {
    String ruleName = rule.getName();
    boolean result = true;

    List l = orderFieldValues(values, rule.getMapping(), fieldValues);
    for (Iterator i = l.iterator(); i.hasNext();) {
      try {
        Pair pair = (Pair) i.next();
        int j = ((Integer) pair.getFirst()).intValue();
        ruleName = rule.getName() + (rule.isDynamic() ? "" + j : "");
        Map map = (Map) pair.getSecond();

        stamp("Validating rule " + rule.getName() + " ...");

        boolean ruleValidated = rule.doValidate(this, fieldValues, map, j);

        validatedRules.put(ruleName, new Boolean(ruleValidated));

        if (!ruleValidated) {
          if (rule.areAllFieldsValidated()) {
            messagesMap.put(ruleName, new Integer(rule.getMsgCode()));
            orderedMessagesMap.put(ruleName, new Integer(rule.getMsgCode()));
            stamp("Rule '" + ruleName + "' has not been successfully validated.");
          }
        }

        result &= ruleValidated;
      } catch (Exception e) {
        messagesMap.put(ruleName, new Integer(rule.getMsgCode()));
        orderedMessagesMap.put(ruleName, new Integer(rule.getMsgCode()));
        validatedRules.put(ruleName, Boolean.FALSE);
        stamp("Rule '" + ruleName + "' has not been successfully validated. Exception thrown: " + e.getMessage());
        return false;
      }
    }

    return result;
  }

  /**
   * @param fieldValues map of string field values.
   * @return true if all rules have been successfully validated.
   */
  protected boolean validateRules(Map fieldValues) {
    boolean result = true;

    for (Iterator i = rules.entrySet().iterator(); i.hasNext();) {
      Rule rule = (Rule) ((Map.Entry) i.next()).getValue();
      result &= validateRule(rule, fieldValues, values);
    }

    return result;
  }

  /**
   * Automatically generates all conditional fields.
   * <p>
   * The algorithm is as follows:
   * <ul>
   *   <li>take all rules for which condition should be checked</li>
   *   <li>for each such rule, store all fields that are to be validated only when the condition has been met</li>
   *   <li>remove from those fields all such that must be used in 'unconditional' rules, therefore
   *       such that must be validated before rules</li>
   *   <li>mark fields that left as 'conditional'</li>
   * </ul>
   */
  public void setConditionalFields() {
    Map conditionalFields = new HashMap();

    for (Iterator i = rules.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Rule rule = (Rule) me.getValue();
      if (!rule.isCheckCondition()) {
        continue;
      }
      Map ruleConditionalFields = rule.getFieldsValidatedConditionally();
      if (ruleConditionalFields != null) {
        for (Iterator j = ruleConditionalFields.entrySet().iterator(); j.hasNext();) {
          Map.Entry me2 = (Map.Entry) j.next();
          String fieldName = (String) me2.getValue();
          conditionalFields.put(fieldName, Boolean.TRUE);
          Field field = getField(fieldName);
          if (field.isComplex()) {
            for (Iterator k = field.getBuilder().getMapping().entrySet().iterator(); k.hasNext();) {
              Map.Entry me3 = (Map.Entry) k.next();
              String fieldPartName = (String) me3.getValue();
              conditionalFields.put(fieldPartName, Boolean.TRUE);
            }
          }
        }
      }
    }

    for (Iterator i = rules.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Rule rule = (Rule) me.getValue();
      if (rule.isCheckCondition()) {
        continue;
      }
      Map mapping = rule.getMapping();
      if (mapping != null) {
        for (Iterator j = mapping.entrySet().iterator(); j.hasNext();) {
          Map.Entry me2 = (Map.Entry) j.next();
          String fieldName = (String) me2.getValue();
          conditionalFields.remove(fieldName);
          Field field = getField(fieldName);
          if (field.isComplex()) {
            for (Iterator k = field.getBuilder().getMapping().entrySet().iterator(); k.hasNext();) {
              Map.Entry me3 = (Map.Entry) k.next();
              String fieldPartName = (String) me3.getValue();
              conditionalFields.remove(fieldPartName);
            }
          }
        }
      }
    }

    for (Iterator i = conditionalFields.keySet().iterator(); i.hasNext();) {
      Field field = getField((String) i.next());
      field.addType(Field.FT_CONDITIONAL);
    }
  }

  /**
   * @return description of the form.
   */
  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("Form '" + name + "':");

    result.append("\n  defined fields: ");
    for (Iterator i = fields.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Field field = (Field) me.getValue();
      result.append(field.toString());
    }

    result.append("\n  defined rules: ");
    for (Iterator i = rules.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Rule rule = (Rule) me.getValue();
      result.append(rule.toString());
    }

    result.append("\n  parsed values: ");
    for (Iterator i = values.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String fieldName = (String) me.getKey();
      Object value = me.getValue();
      result.append("\n\t" + fieldName + " = " + value);
    }

    return result.toString();
  }

  /**
   * This method will not work for dynamic fields.
   * Therefore I would like to make it deprecated - wswiatek.
   *
   * @see pl.aislib.fm.FieldContainer#getErrorCodes()
   */
  public Iterator getErrorCodes() {
    List list = new ArrayList();

    for (Iterator i = orderedMessagesMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      Integer msgCode = (Integer) me.getValue();
      if (!list.contains(msgCode)) {
        if (messages.containsKey(msgCode)) {
          list.add(msgCode);
        } else if (messageGroups.containsKey(msgCode)) {
          List messageGroup = (List) messageGroups.get(msgCode);
          for (Iterator j = messageGroup.iterator(); j.hasNext();) {
            Integer refMsgCode = (Integer) j.next();
            if (!list.contains(refMsgCode)) {
              list.add(refMsgCode);
            }
          }
        }
      }
    }

    return list.iterator();
  }

  /**
   * @return iterator of rule names.
   */
  public Iterator getRuleNames() {
    return rules.keySet().iterator();
  }

  /**
   * @param ruleName name of a rule.
   * @return the rule.
   */
  public Rule getRule(String ruleName) {
    return (Rule) rules.get(ruleName);
  }

  /**
   * @return map of validated rules.
   */
  public Map getValidatedRules() {
    return validatedRules;
  }

  /**
   * @param ruleName name of a rule.
   * @return true if the rule has been validated successfully.
   */
  public boolean isRuleValidated(String ruleName) {
    Boolean b = (Boolean) validatedRules.get(ruleName);
    return (b != null ? b.booleanValue() : false);
  }


  // Protected methods

  /**
   * @see pl.aislib.fm.FieldContainer#preValidate(Map, Object)
   */
  protected boolean preValidate(Map fieldValues, Object data) {
    super.preValidate(fieldValues, data);

    validatedRules = new LinkedHashMap();

    return true;
  }

  /**
   * @see pl.aislib.fm.FieldContainer#getEntity(String)
   */
  protected IEntity getEntity(String entityName) {
    IEntity entity = super.getEntity(entityName);
    return entity != null ? entity : (IEntity) rules.get(entityName);
  }

  /**
   *
   * @see pl.aislib.fm.FieldContainer#getDynamicEntityNamesList(String)
   */
  protected List getDynamicEntityNamesList(String entityName) {
    List result = super.getDynamicEntityNamesList(entityName);
    if (result.size() != 0) {
      return result;
    }

    for (Iterator i = validatedRules.keySet().iterator(); i.hasNext();) {
      String ruleName = (String) i.next();
      if (ruleName.startsWith(entityName)) {
        result.add(ruleName);
      }
    }

    return result;
  }

  /**
   * @see pl.aislib.fm.FieldContainer#getErrorMessages(IMessageConverter)
   */
  public Map getErrorMessages(IMessageConverter messageConverter) {
    Map formEntityProperties = getEntitiesProperties();

    Map result = super.getErrorMessages(messageConverter);

    for (Iterator i = rules.keySet().iterator(); i.hasNext();) {
      String ruleName = (String) i.next();
      result.putAll(getEntityMessages(ruleName, messageConverter, formEntityProperties));
    }

    return result;
  }

  /**
   * @see pl.aislib.fm.FieldContainer#getEntitiesProperties()
   */
  protected Map getEntitiesProperties() {
    Map result = super.getEntitiesProperties();

    for (Iterator i = rules.keySet().iterator(); i.hasNext(); ) {
      String ruleName = (String) i.next();
      getEntityProperties(ruleName, result);
    }

    return result;
  }


  /**
   * @see pl.aislib.fm.FieldContainer#addSpecificEntityProperties(pl.aislib.fm.forms.IEntity, java.lang.String, java.util.Map)
   */
  protected void addSpecificEntityProperties(IEntity entity, String entityName, Map entityProperties) {
    super.addSpecificEntityProperties(entity, entityName, entityProperties);

    if (!(entity instanceof Rule)) {
      return;
    }

    Rule rule = (Rule) entity;
    Map fieldNameMapping = null;
    if (entity.isDynamic()) {
      try {
        int entityNumber = Integer.parseInt(entityName.substring(entityName.lastIndexOf("_") + 1));
        fieldNameMapping = rule.getDynamicFieldNameMapping(this, entityNumber);
      } catch (Exception ex) {
        ;
      }
    } else {
      fieldNameMapping = rule.getMapping();
    }

    Map ruleFieldProperties = new HashMap();

    if (fieldNameMapping != null) {
      for (Iterator iter = fieldNameMapping.entrySet().iterator(); iter.hasNext();) {
        Map.Entry me = (Map.Entry) iter.next();
        String key = (String) me.getKey();
        String value = (String) me.getValue();
        Field field = getField((String) rule.getMapping().get(key));
        Map fieldProperties = new HashMap();
        addSpecificEntityProperties(field, value, fieldProperties);
        ruleFieldProperties.put(key, fieldProperties);
      }
    }

    entityProperties.put(MC_ENTITY_RULE, ruleFieldProperties);
  }

} // pl.aislib.fm.Form class
