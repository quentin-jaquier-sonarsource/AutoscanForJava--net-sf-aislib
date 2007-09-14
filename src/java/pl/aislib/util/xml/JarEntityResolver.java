package pl.aislib.util.xml;

import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import java.util.Map;
import java.util.HashMap;

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
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.1
 * @see <a href='http://jakarta.apache.org/commons/logging.html'>Jakarta Commons Logging Component</a>
 */
public class JarEntityResolver implements EntityResolver {

 /**
  * Jakarta Commons Logging Component used to store information about 
  * entity resolving process.
  */
  protected Log     log = LogFactory.getLog("pl.aislib.util.xml");

 /**
  * Contains mapping between public ID of entity and Jar resource name.
  */
  protected Map idMappings;
 
 /**
  * Contains Jar resource name prefix.
  */
  protected String  jarPrefix;

  private   String  lastPublicId;

 /**
  * @param jarPrefix path inside classpath to look for DTDs
  * @param idMappings mapping between public IDs of DTDs and files in classpath
  */
  public JarEntityResolver(String jarPrefix, Map idMappings) {
    this.idMappings = cloneMap(idMappings);
    this.jarPrefix  = jarPrefix;
    lastPublicId    = null;
  }

 /**
  * @param jarPrefix path inside classpath to look for DTDs
  * @param idMappings mapping between public IDs of DTDs and files in classpath
  * @param log log component to use during lookup
  */
  public JarEntityResolver(String jarPrefix, Map idMappings, Log log) {
    this(jarPrefix, idMappings);
    this.log = log;
  }

 /**
  * Allow the application to resolve external entities.
  *
  * @param publicId public ID to resolve
  * @param systemId system ID (ignored)
  */
  public InputSource resolveEntity(String publicId, String systemId) {
  
    if ((log != null) && (log.isDebugEnabled())) {
      log.debug("resolving: publicId: " + publicId + ", systemId: " + systemId);
    }
    if ((idMappings != null) && (publicId != null)) {
      if (idMappings.containsKey(publicId)) {
  
        String      resource = jarPrefix;
  
        if (resource == null) {
          resource = "";
        }
        if (!resource.endsWith ("/")) {
          resource = resource.concat("/");
        }
        resource = resource.concat((String) idMappings.get(publicId));
        if ((log != null) && (log.isDebugEnabled())) {
          log.debug("resolved to: " + resource);
        }

        URL resourceURL = Loader.findResource(resource);
        if (resourceURL != null) {
          try {
            InputStream is = resourceURL.openStream();
            if (is != null) {
              InputSource result = new InputSource(is);
              if ((log != null) && (log.isDebugEnabled())) {
                log.debug("found");
              }
              if (resourceURL != null) {
                result.setSystemId(resourceURL.toExternalForm());
            }
              
              return result;
            }
          } catch (IOException ioe) {
            // nop
          }
        } else {
          if ((log != null) && (log.isDebugEnabled())) {
            log.debug("not found");
          }
        }
      }
    }
    return null; 
  }

 /**
  *
  */
  private Map cloneMap (Map source) {
    if (source == null) {
      return null;
    }
    HashMap result = new HashMap(source.size());
    result.putAll(source);
    return result;
  }

} // class
