package pl.aislib.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import pl.aislib.lang.Loader;

/**
 * Implements {@link EntityResolver} using Jar archive as DTD repository.
 *
 * By default, <code>pl.aislib.util.xml</code> log category is used.
 *
 * @author Michal Jastak
 * @since AISLIB 0.1
 * @see <a href='http://jakarta.apache.org/commons/logging.html'>Jakarta Commons Logging Component</a>
 */
public class JarEntityResolver implements EntityResolver {

  /**
   * Jakarta Commons Logging Component used to store information about
   * entity resolving process.
   */
  protected Log log = LogFactory.getLog("pl.aislib.util.xml");

  /**
   * Contains mapping between public ID of entity and Jar resource name.
   */
  protected Map publicIdMappings;

  /**
   * Contains mapping between system ID of entity and Jar resource name.
   */
  protected Map systemIdMappings;

  /**
   * Contains Jar resource name prefix.
   */
  protected String jarPrefix;

  /**
   * @param jarPrefix path inside classpath to look for DTDs
   * @param idMappings mapping between public IDs of DTDs and files in classpath
   */
  public JarEntityResolver(String jarPrefix, Map idMappings) {
    this.publicIdMappings = cloneMap(idMappings);
    this.jarPrefix = jarPrefix;
  }

  /**
   * @param jarPrefix path inside classpath to look for DTDs
   * @param publicIdMappings mapping between public IDs of DTDs and files in classpath
   * @param log log component to use during lookup
   */
  public JarEntityResolver(String jarPrefix, Map publicIdMappings, Log log) {
    this(jarPrefix, publicIdMappings);
    this.log = log;
  }

  /**
   * @param jarPrefix path inside classpath to look for DTDs
   * @param publicIdMappings mapping between public IDs of DTDs and files in classpath
   * @param systemIdMappings mapping between system IDs of DTDs and files in classpath
   */
  public JarEntityResolver(String jarPrefix, Map publicIdMappings, Map systemIdMappings) {
    this.publicIdMappings = cloneMap(publicIdMappings);
    this.systemIdMappings = cloneMap(systemIdMappings);
    this.jarPrefix = jarPrefix;
  }

  /**
   * @param jarPrefix path inside classpath to look for DTDs
   * @param publicIdMappings mapping between public IDs of DTDs and files in classpath
   * @param systemIdMappings mapping between system IDs of DTDs and files in classpath
   * @param log log component to use during lookup
   */
  public JarEntityResolver(String jarPrefix, Map publicIdMappings, Map systemIdMappings, Log log) {
    this(jarPrefix, publicIdMappings, systemIdMappings);
    this.log = log;
  }

  /**
   * Allow the application to resolve external entities.
   *
   * @param publicId public ID to resolve
   * @param systemId system ID (ignored)
   */
  public InputSource resolveEntity(String publicId, String systemId) {
    InputSource result = resolve(publicId, publicIdMappings);
    if (result != null) {
      return result;
    }
    result = resolve(systemId, systemIdMappings);
    if (result != null) {
      InputStream istr = result.getByteStream();
      ByteArrayOutputStream cache = new ByteArrayOutputStream();
      byte[] bcache = new byte[1024];
      int count = 0;
      try {
        while ((count = istr.read(bcache)) > 0) {
          cache.write(bcache, 0, count);
        }
        istr.close();
        result.setByteStream(new ByteArrayInputStream(cache.toByteArray()));
      } catch (IOException ioe) {
        if (log != null) {
          log.fatal("exception caught during resource loading " + ioe.getMessage(), ioe);
        }
      }
    }
    return result;
  }

  private InputSource resolve(String id, Map mapping) {
    if ((mapping != null) && (id != null)) {
      if (mapping.containsKey(id)) {
        String resource = jarPrefix;
        if (resource == null) {
          resource = "";
        }
        if (!resource.endsWith("/")) {
          resource = resource.concat("/");
        }
        resource = resource.concat((String) mapping.get(id));
        if ((log != null) && (log.isDebugEnabled())) {
          log.debug("resolved to: " + resource);
        }

        URL resourceURL = Loader.findResource(resource);
        if (resourceURL != null) {
          try {
            InputStream is = resourceURL.openStream();
            if (is != null) {
              InputSource result = new InputSource(is);
              if (resourceURL != null) {
                result.setSystemId(resourceURL.toExternalForm());
              }
              return result;
            }
          } catch (IOException ioe) {
            // nop
          }
        }
      }
    }
    return null;
  }

  /**
   *
   */
  private Map cloneMap(Map source) {
    if (source == null) {
      return null;
    }
    HashMap result = new HashMap(source.size());
    result.putAll(source);
    return result;
  }

} // class
