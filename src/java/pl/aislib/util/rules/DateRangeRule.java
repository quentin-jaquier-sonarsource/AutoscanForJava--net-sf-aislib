package pl.aislib.util.rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.aislib.fm.forms.Rule;

/**
 * <p>Describes rules for checking if date is in a given range.</p>
 *
 * Expected parameters:
 * <ul>
 *   <li><code>from_date</code> - start date in range</li>
 *   <li><code>to_date</code> - end date in range</li>
 *   <li><code>check</code> - should be set to true if range should be validated at all (<i>optional</i>)</li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public class DateRangeRule extends Rule {

  protected String checkConditionValue = "true";

  /**
   * Constructor for DateRangeRule.
   */
  public DateRangeRule() {
    super();
  }

  /**
   * Constructor for DateRangeRule.
   * @see pl.aislib.fm.forms.Rule#Rule(String)
   */
  public DateRangeRule(String name) {
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
    Date dateFrom = (Date) values.get("from_date");
    Date dateTo   = (Date) values.get("to_date");
    return dateTo.after(dateFrom) || dateTo.equals(dateFrom);
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

