package pl.aislib.util.rules;

import java.util.Map;

import pl.aislib.fm.forms.ConditionalRule;

/**
 * Describes rules for checking if two objects are equal.
 *
 * Expected parameters:
 * <ul>
 *   <li><code>object1</code> - first object to compare;</li>
 *   <li><code>object2</code> - second object to compare;</li>
 *   <li><code>check</code> - should be set to <code>true</code> if equality should be validated at all (<i>optional</i>).</li>
 * </ul>
 *
 * @author Tomasz Pik, AIS.PL
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public class EqualityRule extends ConditionalRule {

  /** */
  public EqualityRule() {
    super();
  }

  /**
   * Constructor for EqualityRule.
   *
   * @see pl.aislib.fm.forms.Rule#Rule(String)
   */
  public EqualityRule(String name) {
    super(name);
  }

  /**
   * @see pl.aislib.fm.forms.Rule#validate(Map)
   */
  public boolean validate(Map values) {
    Object obj1 = values.get("object1");
    Object obj2 = values.get("object2");
    if (obj1 == null && obj2 == null) {
      return true;
    }
    if (obj1 != null && obj2 != null) {
      return obj1.equals(obj2);
    }
    return false;
  }

} // pl.aislib.util.rules.EqualityRule class
