package pl.aislib.util.rules;

import java.util.Map;

import pl.aislib.fm.forms.ConditionalRule;

/**
 * Describes rules for checking if given set of fields should be validated when another field is <em>checked</em>.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class CheckSingleFieldRule extends ConditionalRule {

  /** */
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
   * @see pl.aislib.fm.forms.Rule#validate(Map)
   */
  public boolean validate(Map values) {
    return true;
  }

} // pl.aislib.util.rules.CheckSingleFieldRule class
