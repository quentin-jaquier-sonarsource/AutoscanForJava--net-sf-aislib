package pl.aislib.jakarta.velocity;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Interface between Velocity and Commons Logging.
 *
 * @author Daniel Rychcik
 */
public class CommonsLogSystem implements LogSystem {

  /**
   * Configuration key, value of this key is used to create {@link Log} instance.
   */
  public static final String LOGSYSTEM_AISLIB_NAME = "runtime.log.logsystem.aislib";

  private Log log;

  /**
   * Initialize logging system.
   *
   * Looks for property with {@link #LOGSYSTEM_AISLIB_NAME} and create {@link Log} instance
   * using {@link LogFactory#getLog(String)} method.
   *
   * @param runtimeServices represents initializing Velocity.
   */
  public void init (RuntimeServices runtimeServices) {
    String logName = runtimeServices.getString(LOGSYSTEM_AISLIB_NAME);
    log = LogFactory.getLog(logName);
  }

  /**
   * Dispatch given log message to Commons Logging system.
   *
   * @param level one of constants defined in {@link LogSystem}
   * @param message to be logged
   */
  public void logVelocityMessage(int level, String message) {

    switch (level) {

      case LogSystem.DEBUG_ID: {
                log.debug(message);
                break;
              }

      case LogSystem.INFO_ID: {
                log.info(message);
                break;
              }

      case LogSystem.WARN_ID: {
                log.warn(message);
                break;
              }

      case LogSystem.ERROR_ID: {
                log.error(message);
                break;
              }

      default: {
                 log.fatal(message);
                 break;
               }

    }
  }
}

