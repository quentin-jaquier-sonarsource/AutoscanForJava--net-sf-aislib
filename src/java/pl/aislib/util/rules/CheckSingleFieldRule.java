package pl.aislib.util.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.aislib.fm.forms.Rule;

/**
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.1.1.1 $
 */
public class CheckSingleFieldRule extends Rule {

  protected String checkConditionValue = "true";

  /**
   *
   */
  public CheckSingleFieldRule() {
    super();
  }

  /**
   * @see pl.aislib.fm.forms.Rule#Rule(String)
   */
  public CheckSingleFieldRule(String name) {
    super(name);
  }

  /**
   * @param value value of the check field that must be satisfied.
   */
  public void setCheckConditionValue(String value) {
    checkConditionValue = value;
  }

  /**
   * @see pl.aislib.fm.forms.Rule#validate(Map)
   */
  public boolean validate(Map values) {
    return true;
  }

  /**
   * @see pl.aislib.fm.forms.Rule#validateConditional(Map)
   */
  protected boolean validateConditional(Map values) {
    if (values.get("check") == null) {
      return false;
    }

    Object oValue = values.get("check");
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
    result.add("check");
    return result;
  }

}
