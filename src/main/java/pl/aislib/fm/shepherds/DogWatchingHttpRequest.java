package pl.aislib.fm.shepherds;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class for using while testing whole {@link HttpServletRequest}.
 * 
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 * @since AISLIB 0.5
 */
public class DogWatchingHttpRequest extends AbstractShepherdsDog {

  /**
   * @return String identyfying this Dog ({@link ShepherdsDog#REQUEST})
   */
  public String getBreed() {
    return ShepherdsDog.REQUEST;
  }

  /**
   * Check if given sheep is a {@link HttpServletRequest}.
   * 
   * Returns <code>sheep</code> if it's a {@link HttpServletRequest},
   * otherwise throws {@link IllegalArgumentException}.
   * 
   * @param pasture Universe in which everything happens.
   * @param sheep HttpServletRequest object which is observed by this one.
   * @return <code>sheep</code>.
   */
  public Object mindTheSheep(Pasture pasture, Object sheep) {
    if (sheep instanceof HttpServletRequest) {
      return sheep;
    } else {
      throw new IllegalArgumentException("Invalid argument '" + sheep + "'");
    }
  }

  /**
   * Describe Shepherd Dog.
   * 
   * @return String contains description of Dog.
   */
  public String toString() {
    return "DogWatchingHttpRequest";
  }
}
