package pl.aislib.util;

import org.apache.commons.logging.Log;

/**
 * Interface, which should be implemented by log-enabled classes.
 *
 * @author Tomasz Pik, AIS.PL
 * @since 0.3
 */
public interface Loggable {

  /**
   * Sets Log instance to given class.
   *
   * @param log to be used within class.
   */
  public void setLog(Log log);

}

