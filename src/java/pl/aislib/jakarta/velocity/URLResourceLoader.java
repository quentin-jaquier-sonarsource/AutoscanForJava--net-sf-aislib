package pl.aislib.jakarta.velocity;

import java.io.InputStream;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import org.apache.commons.collections.ExtendedProperties; 

import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * ResourceLoader for loading templates from URLs.
 *
 * @author Daniel Rychcik
 * @version 0.1
 * @since 0.2
 */
public class URLResourceLoader extends ResourceLoader {

  /**
   * There's nothing to init in this class.
   *
   * @param properties which are ignored.
   */
  public void init(ExtendedProperties properties) {
  }

  /**
   * Create <code>URL</code> and load content from this url.
   *
   * @param source used to create URL
   * @return InputStream created using <code>source</code> as URL.
   * @throws ResourceNotFoundException if given argument is not a valid URL
   *         or given URL cannot be opened
   */
  public InputStream getResourceStream(String source) throws ResourceNotFoundException {
    InputStream iStream = null;
    try {
      URL url = new URL(source);
      iStream = url.openStream();
    } catch (MalformedURLException e) {
      throw new ResourceNotFoundException("Not found: " + source);
    } catch (IOException e) {
      throw new ResourceNotFoundException("Not found: " + source);
    }

    if (iStream == null) {
      throw new ResourceNotFoundException("Not found: " + source);
    } else {
      return iStream;
    }
  }

  /**
   * Checks if given resource has been modified.
   *
   * Always return <code>true</code>.
   *
   * @param resource to be checked.
   * @return <code>true</code>.
   */
  public boolean isSourceModified(Resource resource) {
    return true;
  }

  /**
   * Checks when given resource has been modified.
   *
   * Always return <code>0</code>.
   *
   * @param resource to be checked
   * @return <code>0</code>
   */
  public long getLastModified(Resource resource) {
    return 0;
  }
}
