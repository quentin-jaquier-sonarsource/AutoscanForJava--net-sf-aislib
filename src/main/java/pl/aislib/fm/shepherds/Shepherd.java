package pl.aislib.fm.shepherds;

import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;

/**
 * Minds the sheeps (usually {@link javax.servlet.http.HttpServletRequest} objects)
 * on the Pasture ({@link pl.aislib.fm.Application}).
 * <p>Shepherd uses given <code>Predicate</code> object for controlling
 * <code>Application</code> state and flow between pages. <br />
 * In other words: Shepherds are used by <code>Application</code> during user request dispatching between pages. <br />
 * During this process, <code>Application</code> asks all Shepherds registered (in <code>PageInfo</code> object)
 * for specific <code>Page</code> if Predicates assigned to them evaluates to
 * <code>true</code>. If there will be any Shepherd which will match this condition,
 * <code>Application</code> will dispatch user request to <code>Page</code> having
 * action key equal to <code>pageRef</code> remembered by this Shepherd.</p>
 *
 * @author Michal Jastak
 * @see pl.aislib.fm.Application
 * @see pl.aislib.fm.Page
 * @see pl.aislib.fm.PageInfo
 */
public class Shepherd extends Object {

  private Predicate    predicate;
  private String       pageRef;
  private ShepherdsDog shepherdsDog;
  private Log          log;

  private String description;

  /**
   * Class Constructor.
   * @param predicate <code>Predicate</code> which will be used by this Shepherd
   * @param shepherdsDog Helper class which is responsible for getting values of some properties
   * @param pageRef action key of <code>Page</code> to which we should dispatch user's request
   *        if <code>Predicate</code> will be matched against value of some property
   * @param log workflow log.
   */
  public Shepherd(Predicate predicate, ShepherdsDog shepherdsDog, String pageRef, Log log) {
    this.predicate    = predicate;
    this.shepherdsDog = shepherdsDog;
    this.pageRef      = pageRef;
    this.log          = log;

    description = "Shepherd [Dog: " + shepherdsDog + ", Predicate: " + predicate + ", Page: " + pageRef + "]";
  }

  /**
   * Used by <code>Application</code> for quering <code>Shepherd</code> about <code>Predicate</code> matching.
   * @param pasture universe in which everything happens (<code>Application</code> object)
   * @param sheep current user request (usually <code>HttpServletRequest</code> object)
   * @return action key for <code>Page</code> to which user's request should be dispatched or <code>null</code>
   *         if this Shepherd's Predicate was not matched
   */
  public String mindTheSheep(Pasture pasture, Object sheep) {
    boolean isDebugEnabled = log.isDebugEnabled();
    if (isDebugEnabled) {
      log.debug("minding sheep: " + sheep + " with " + description);
    }
    if (predicate.evaluate(shepherdsDog.mindTheSheep(pasture, sheep))) {
      if (isDebugEnabled) {
        log.debug("caught, returning: " + pageRef);
      }
      return pageRef;
    }
    if (isDebugEnabled) {
      log.debug("not caught");
    }
    return null;
  }

  /**
   * Describe Shepherd
   *
   * @return String description of Shephed
   */
  public String toString() {
    return description;
  }
} // class
