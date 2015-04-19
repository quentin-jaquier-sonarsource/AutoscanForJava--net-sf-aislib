package pl.aislib.fm;

/**
 * Abstract class for encapsulating the environment specific parameters
 * for application.
 *
 * @author Tomasz Pik
 * @since AISLIB 0.1
 */
public abstract class ConfigAdapter {

  /**
   * return the environment-specific config parameter for application
   * @param key config parameter name
   * @return parameter value or <code>null</code>
   */
  public abstract String getConfigParameter(String key);

} // class
