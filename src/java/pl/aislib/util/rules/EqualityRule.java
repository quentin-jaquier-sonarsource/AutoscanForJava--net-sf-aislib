package pl.aislib.util.rules;

import java.util.Map;

import pl.aislib.fm.forms.ConditionalRule;

/**
 * <p>Describes rules for checking if objects are equal.</p>
 *
 * Expected parameters:
 * <ul>
 *   <li><code>object1</code> - first object to compare.</li>
 *   <li><code>object2</code> - second object to compare.</li>
 *   <li><code>check</code> - should be set to true if equality should be validated at all (<i>optional</i>)</li>
 * </ul>
 *
 * @author Tomasz Pik, AIS.PL
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.1 $
 */
public class EqualityRule extends ConditionalRule {

  /**
   * Constructor for EqualityRule.
   */
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
    return values.get("object1").equals(values.get("object2"));
  }

} // pl.aislib.util.rules.EqualityRule class
