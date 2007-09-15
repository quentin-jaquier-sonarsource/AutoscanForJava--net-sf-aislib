package pl.aislib.util.predicates;

import org.apache.commons.collections.Predicate;

/**
 * Predicate which is always true. 
 *
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.4
 */
public class TruePredicate implements Predicate {

  /**
   * @param input anything you want
   * @return always <code>true</code>
   */
  public boolean evaluate(Object input) {
    return true;
  }

} // class

