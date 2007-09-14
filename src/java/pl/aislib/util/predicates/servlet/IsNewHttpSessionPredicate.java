package pl.aislib.util.predicates.servlet;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.Predicate;

/**
 * Predicate checking if HttpSession is new.
 *
 * @author Tomasz Pik
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.4
 * @see org.apache.commons.collections.Predicate
 */
public class IsNewHttpSessionPredicate implements Predicate {

  /**
   * Checks if given parametr represents new HttpSession.
   *
   * @param httpSession HttpSession object to check
   * @return <code>true</code> if given <code>httpSession</code> parameter is new Session
   * (see {@link HttpSession#isNew}), <code>false</code> otherwise
   */
  public boolean evaluate(Object httpSession) {
    if (httpSession == null) {
      return true;
    }
    try {
      HttpSession session = (HttpSession) httpSession;
      return session.isNew();
    } catch (ClassCastException ccex) {
      throw new IllegalArgumentException("IsNewPredicate accepts only HttpSession objects");
    }
  }

} // class

