package pl.aislib.jakarta.velocity;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.collections.ExtendedProperties;

import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * ResourceLoader for loading templates from ServletContext.
 *
 * @author Tomasz Pik
 */
public class ServletContextResourceLoader extends ResourceLoader {

  /**
   * Properties key identifies the given ServletContext instance.
   */
  public static final String CONTEXT_KEY = "context";

  /**
   * Properties key specifies directory inside the <tt>WEB-INF</tt> directory,
   * where the templates are located.
   */
  public static final String DIRECTORY_KEY = "directory";

  private ServletContext servletContext;

  /**
   * Sets servlet context which is used for loading templates.
   *
   * @param context servlet context used to load templates.
   */
  public void setServletContext(ServletContext context) {
    servletContext = context;
  }

  private String templateDir;

  /**
   * Sets dir within servlet context used as a root for templates.
   *
   * @param dir dir within servlet contex used as a root for templates.
   */
  public void setTemplateDir(String dir) {
    templateDir = dir;
  }

  /**
   *
   * @see ResourceLoader#init(ExtendedProperties)
   */
  public void init(ExtendedProperties properties) {
    setServletContext((ServletContext) properties.getProperty(CONTEXT_KEY));
    setTemplateDir((String) properties.getProperty(DIRECTORY_KEY));
  }

  /**
   * Open a stream within servlet context.
   *
   * @param source path within servlet context (prefixed by <code>dir</code>, see {@link #setTemplateDir(String)}.
   * @return stream with template.
   * @throws ResourceNotFoundException if stream don't exists.
   */
  public InputStream getResourceStream(String source) throws ResourceNotFoundException {
    InputStream iStream = servletContext.getResourceAsStream(templateDir + source);
    if (iStream == null) {
      throw new ResourceNotFoundException("Not found: " + source);
    } else {
      return iStream;
    }
  }

  /**
   * @param resource ignored argument
   * @return <code>true</code>.
   */
  public boolean isSourceModified(Resource resource) {
    return true;
  }

  /**
   * @param resource ignored argument
   * @return <code>0</code>.
   */
  public long getLastModified(Resource resource) {
    return 0;
  }
}
