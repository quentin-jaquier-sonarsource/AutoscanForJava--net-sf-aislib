package pl.aislib.util.predicates;

import org.apache.commons.collections.Predicate;

/**
 * Predicate which is always false.
 *
 * @author Michal Jastak
 * @author Tomasz Pik
 * @since AISLIB 0.4
 */
public class FalsePredicate implements Predicate {

  /**
   *
   * @param input anything you want
   * @return always <code>false</code>
   */
  public boolean evaluate(Object input) {
    return false;
  }

} // class

