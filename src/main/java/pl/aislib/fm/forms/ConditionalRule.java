package pl.aislib.fm.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class of conditional rules.
 *
 * Describes rules that are validated only under certain condition
 * described by value of <code>check</code> parameter.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.1 $
 */
public abstract class ConditionalRule extends Rule {

  /**
   * Name of checking condition parameter.
   */
  protected Object checkConditionName;

  /**
   * Value of checking condition parameter.
   */
  protected Object checkConditionValue;


  /**
   *
   */
  public ConditionalRule() {
    super();

    setCheckConditionName("check");
    setCheckConditionValue("true");
  }

  /**
   * @see pl.aislib.fm.forms.Rule#Rule(String)
   */
  public ConditionalRule(String name) {
    super(name);

    setCheckConditionName("check");
    setCheckConditionValue("true");
  }


  /**
   * Sets name of checking condition parameter.
   *
   * By default it is set to <em>"check"</em>.
   *
   * @param checkConditionName name of the parameter.
   */
  public void setCheckConditionName(Object checkConditionName) {
    this.checkConditionName = checkConditionName;
  }

  /**
   * Sets value of checking condition parameter.
   *
   * By default it is set to <em>"true"</em>.
   * @param checkConditionValue
   */
  public void setCheckConditionValue(Object checkConditionValue) {
    this.checkConditionValue = checkConditionValue;
  }


  /**
   * @see pl.aislib.fm.forms.Rule#validateConditional(Map)
   */
  protected boolean validateConditional(Map values) {
    Object oValue = values.get(checkConditionName);

    if (oValue == null) {
      return false;
    }

    if (oValue instanceof Object[]) {
      Object[] oValues = (Object[]) oValue;
      for (int i = 0; i < oValues.length; i++) {
        if (checkConditionValue.equals(oValues[i])) {
          return true;
        }
      }
      return false;
    }

    return checkConditionValue.equals(oValue);
  }

  /**
   * @see pl.aislib.fm.forms.Rule#getFieldsValidatedUnconditionally()
   */
  protected List getFieldsValidatedUnconditionally() {
    List result = new ArrayList();
    result.add(checkConditionName);
    return result;
  }

} // pl.aislib.fm.forms.ConditionalRule class
