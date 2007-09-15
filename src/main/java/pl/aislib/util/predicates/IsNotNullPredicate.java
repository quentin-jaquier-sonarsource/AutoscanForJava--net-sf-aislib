package pl.aislib.util.predicates;

import org.apache.commons.collections.Predicate;

/**
 * Predicate which is true only if evaluated object is not <code>null</code>. 
 *
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.4
 */
public class IsNotNullPredicate implements Predicate {

  /**
   * @param object object to check
   * @return <code>true</code> if <code>object</code> is not <code>null</code>, <code>false</code> otherwise
   */
  public boolean evaluate(Object object) {
    return (object != null);
  }

} // class

