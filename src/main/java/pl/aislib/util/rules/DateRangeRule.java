package pl.aislib.util.rules;

import java.util.Date;
import java.util.Map;

import pl.aislib.fm.forms.ConditionalRule;

/**
 * Describes rules for checking if two dates form a proper range.
 *
 * Expected parameters:
 * <ul>
 *   <li><code>from_date</code> - start date in range;</li>
 *   <li><code>to_date</code> - end date in range;</li>
 *   <li><code>check</code> - should be set to <code>true</code> if range should be validated at all (<i>optional</i>).</li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class DateRangeRule extends ConditionalRule {

  private boolean fromDatePresent = true;
  private boolean toDatePresent = true;

  /** */
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
   * @see pl.aislib.fm.forms.Rule#validate(Map)
   */
  public boolean validate(Map values) {
    Date dateFrom = (Date) values.get("from_date");
    Date dateTo   = (Date) values.get("to_date");
    if (dateFrom != null && dateTo != null) {
      return dateTo.after(dateFrom) || dateTo.equals(dateFrom);
    }
    if (dateFrom == null && dateTo == null) {
      return !fromDatePresent && !toDatePresent;
    }
    if (dateFrom == null) {
      return !fromDatePresent;
    }
    return !toDatePresent;
  }

  /**
   * Sets whether start range should be given.
   *
   * @param value <code>true</code> if start range should be given.
   */
  public void setFromDatePresent(boolean value) {
    fromDatePresent = value;
  }

  /**
   * Sets whether end range should be given.
   *
   * @param value <code>true</code> if end range should be given.
   */
  public void setToDatePresent(boolean value) {
    toDatePresent = value;
  }

} // package pl.aislib.util.rules.DateRangeRule class
