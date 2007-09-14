package pl.aislib.jakarta.velocity;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogSystem;

/**
 * Helper class to simplfy the process of configuration
 * <code>VelocityTemplateEngine</code>.
 *
 * The <code>configuration<code> Map, that is used as first argument
 * in this class methods should be used to initialize
 * {@link pl.aislib.jakarta.velocity.VelocityTemplateEngine}.
 *
 * @author Tomasz Pik
 * @version $Revision: 1.9 $
 */
public class VelocityConfigHelper {

  /**
   * Put proper keys for {@link ServletContextResourceLoader} into given <code>Map</code>.
   *
   * Use <code>/WEB-INF/etc</code> as a default directory.
   *
   * @param configuration Map to initialize <code>Velocity</code>
   * @param context used to load templates
   */
  public static void configureServletContextLoader(Map configuration, ServletContext context) {
    configureServletContextLoader(configuration, context, "/WEB-INF/etc");
  }

  /**
   * Put proper entries for <code>ServletContextResourceLoader</code> into given <code>Map</code>.
   *
   * Use non-cached version of loader.
   *
   * @param configuration Map to initialize <code>Velocity</code>
   * @param context used to load templates
   * @param prefix directory to load templates
   */
  public static void configureServletContextLoader(Map configuration, ServletContext context, String prefix) {
    configureServletContextLoader(configuration, context, prefix, false);
  }

  /**
   * Put proper entries for <code>ServletContextResourceLoader</code> into given <code>Map</code>.
   *
   * @param configuration Map to initialize <code>Velocity</code>
   * @param context used to load templates
   * @param prefix directory to load templates
   * @param cacheControl define Velocity cache behavior.
   */
  public static void configureServletContextLoader(Map configuration,
          ServletContext context, String prefix, boolean cacheControl) {
    if (context == null) {
      throw new NullPointerException("servlet context cannot be null");
    }
    if (prefix == null) {
      throw new NullPointerException("prefix cannot be null");
    }
    String resourceLoader;
    if (configuration.get(RuntimeConstants.RESOURCE_LOADER) != null) {
      resourceLoader = (String) configuration.get(RuntimeConstants.RESOURCE_LOADER);
      resourceLoader += ", servletContext";
    } else {
      resourceLoader = "servletContext";
    }
    configuration.put(RuntimeConstants.RESOURCE_LOADER, resourceLoader);

    String baseKey = "servletContext." + RuntimeConstants.RESOURCE_LOADER + ".";
    configuration.put(baseKey + "class", ServletContextResourceLoader.class.getName());
    configuration.put(baseKey + ServletContextResourceLoader.CONTEXT_KEY, context);
    configuration.put(baseKey + ServletContextResourceLoader.DIRECTORY_KEY, prefix);
    configuration.put(baseKey + "description", "Servlet context resource loader");
    configuration.put(baseKey + "cache", new Boolean(cacheControl));
  }

  /**
   * Put proper entries for {@link URLResourceLoader} into given <code>Map</code>.
   *
   * Register <code>URLResourceLoader</code> with <code>URL</code> name.
   *
   * @param configuration where <code>URLResourceLoader</code> should be added.
   * @param modificationCheckInterval which will be used by Velocity to reload
   *         already loaded templates.
   */
  public static void configureURLResourceLoader(Map configuration, long modificationCheckInterval) {
    String resourceLoader;
    if (configuration.get(RuntimeConstants.RESOURCE_LOADER) != null) {
      resourceLoader = (String) configuration.get(RuntimeConstants.RESOURCE_LOADER);
      resourceLoader += ", URL";
    } else {
      resourceLoader = "URL";
    }
    configuration.put(RuntimeConstants.RESOURCE_LOADER, resourceLoader);
    String baseKey = "URL." + RuntimeConstants.RESOURCE_LOADER + ".";
    configuration.put(baseKey + "class", URLResourceLoader.class.getName());
    configuration.put(baseKey + "modificationCheckInterval", new Long(modificationCheckInterval));
    configuration.put(baseKey + "cache", new Boolean(modificationCheckInterval != 0));
    configuration.put(baseKey + "description", "URL resource loader");
  }


  /**
   * For backwards compatibility: configure URL resource loader without caching
   * (modificationCheckInterval = 0, no caching - might be quite slow)
   *
   * @param configuration where <code>URLResourceLoader</code> should be added.
   */
  public static void configureURLResourceLoader(Map configuration) {
    configureURLResourceLoader(configuration, 0);
  }

  /**
   * Put proper entries for {@link CommonsLogSystem} into given <code>Map</code>.
   *
   * @param configuration where <code>CommonsLogSystem</code> should be configured.
   * @param logKey log category name which should be used to log.
   */
  public static void configureLog(Map configuration, String logKey) {
    configuration.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM + ".class", CommonsLogSystem.class.getName());
    configuration.put(CommonsLogSystem.LOGSYSTEM_AISLIB_NAME, logKey);
  }

  /**
   * Disable Velocity internal logging.
   *
   * Sets <code>org.apache.velocity.runtime.log.NullLogSystem</code>
   * as logging system.
   *
   * @param configuration Map to initialize <em>Velocity</em>.
   */
  public static void disableLog(Map configuration) {
    configuration.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM + ".class", NullLogSystem.class.getName());
  }

  /**
   * Disable loading of default macro library.
   *
   * Disable loading of default macro library (<code>VM_global_library.vm</code>)
   * by setting value for {@link RuntimeConstants#VM_LIBRARY} to empty string.
   *
   * @since AISLIB 0.5
   * @param configuration Map to initialize <code>Velocity</code>
   */
  public static void disableDefaultLibraryLoading(Map configuration) {
    configuration.put(RuntimeConstants.VM_LIBRARY, "");
  }
}
