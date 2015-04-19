package pl.aislib.util.xml;

import java.io.InputStream;

import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;

import pl.aislib.lang.Loader;

/**
 * Implements {@link URIResolver} using Jar archive as repository.
 * @author Michal Jastak
 * @since AISLIB 0.1
 * @see <a href='http://jakarta.apache.org/commons/logging.html'>Jakarta Commons Logging Component</a>
 */
public class JarURIResolver implements URIResolver {

  /**
   * Jakarta Commons Logging Component used to store information about
   * URI resolving process.
   */
  protected Log     log;

  /**
   * Contains Jar resource name prefix.
   */
  protected String  jarPrefix;

  /**
   *
   */
  public JarURIResolver(String jarPrefix) {
    this.jarPrefix  = jarPrefix;
    log             = null;
  }

  /**
   *
   */
  public JarURIResolver(String jarPrefix, Log log) {
    this(jarPrefix);
    this.log = log;
  }

  /**
   * Turns an URI used in document(), xsl:import, or xsl:include into a Source object.
   * URI is mapped as follows: <code>jarPrefix</code> + base + href
   */
  public Source resolve (String href, String base) throws TransformerException {
    String resource = null;

    if (log != null) {
      log.debug("resolving: base: " + base + ", href: " + href);
    }

    if (base != null) {
      resource = base.substring (0, base.lastIndexOf ("/"));
    } else {
      resource = jarPrefix;
    }

    if (resource == null) { resource = ""; }

    if (!resource.endsWith ("/")) { resource = resource.concat ("/"); }

    if (href != null) { resource = resource.concat (href); }

    if (log != null) {
      log.debug("resolved to: " + resource);
    }

    InputStream is       = null;
    Source      result   = null;
    String      systemId = null;

    URL url = null;
    try {
      if (base == null) {
        url = Loader.findResource(resource);
      } else {
        url = new URL(resource);
      }
      is = url.openStream();
      systemId = url.toString();
    } catch (Exception ex) {
      if (log != null) {
        log.error("Exception caught during URL.openStream", ex);
      }
      throw new TransformerException(ex);
    }

    if (is == null) {
      throw new TransformerException("Cannot resolve '" + base + href + "'");
    }

    result = new StreamSource(is);
    if (systemId != null) {
      result.setSystemId(systemId);
    }
    return result;
  }

} // class
