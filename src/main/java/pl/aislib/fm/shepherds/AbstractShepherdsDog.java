package pl.aislib.fm.shepherds;

import org.apache.commons.logging.Log;

/**
 * Provide base implementation of {@link ShepherdsDog}.
 *
 * @author Tomasz Pik, AIS.PL
 */
public abstract class AbstractShepherdsDog implements ShepherdsDog {

  protected String propertyName;

  /**
   * @see ShepherdsDog#setWatchedProperty(String)
   */
  public final void setWatchedProperty(String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   * @see pl.aislib.fm.shepherds.ShepherdsDog#getWatchedProperty()
   */
  public final String getWatchedProperty() {
    return propertyName;
  }

  protected Log log;

  /**
   * @see ShepherdsDog#setLog(Log)
   */
  public final void setLog(Log log) {
    this.log = log;
  }

}
