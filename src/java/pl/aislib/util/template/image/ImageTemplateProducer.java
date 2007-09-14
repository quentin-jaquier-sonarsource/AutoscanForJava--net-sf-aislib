package pl.aislib.util.template.image;

import java.awt.Image;

import java.io.InputStream;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xml.sax.SAXException;

import pl.aislib.util.Loggable;

import pl.aislib.util.template.Template;

/**
 * Class for creating image templates.
 *
 * <p>Abstract {@link #loadImage} method may be implemented using
 * <code>java.awt.Toolikt.createImage</code> methods or using
 * different libraries like JIMI or Java Image I/O package.</p>
 *
 * <p><b>Renderers</b> can be registered for class or field name.
 * During processing, system looks for registered renderer for
 * a given field name. If this looking failed, then for renderer
 * for a class of given field value. And, at least
 * {@link SimpleFieldRenderer} is instantied.</p>
 *
 * <p><b>Logging</b> by default, <code>ImageTemplateProducer</code>
 * use category named <code>pl.aislib.util.template.image</code>
 * for logging. This may be changed using {@link #setLog} method.
 * All renderers registering is logged at <code>debug</code> level.
 *
 * @author Tomasz Pik, AIS.PL
 * @since 0.3
 */
public abstract class ImageTemplateProducer implements Loggable {

  private ImageTemplateOutputter outputter;
  private Map clazzRenderers = new HashMap();
  private Map fieldRenderers = new HashMap();
  private Log log = LogFactory.getLog("pl.aislib.util.template.image");
  private ImageTemplatesBean config = new ImageTemplatesBean();

  /**
   * @throws RuntimeException if <code>outputter</code> is <em>null</em>.
   */
  public Template loadTemplate(InputStream stream, Map fields) throws IOException {
    if (outputter == null) {
      throw new RuntimeException("call setOutputter before loading templates");
    }
    Image image = loadImage(stream);
    return new ImageTemplate(image, outputter, fields,
                             clazzRenderers, fieldRenderers, log);
  }

  /**
   * Create {@link Image} from {@link InputStream}.
   *
   * This method is abstract and should be implemented in applications,
   * that wants to use <em>Image Templates</em>.
   *
   * @return created image.
   * @param stream containg image.
   * @throws IOException if there's a problem with image loading.
   */
  public abstract Image loadImage(InputStream stream) throws IOException;

  /**
   * Configure this ImageTemplateProducer using XML description from {@link InputStream}
   *
   * @param stream
   * @throws IOException if there's a problem with XML loading
   * @throws SAXException if there's something wrong with XML format
   */
  public void configure(InputStream stream) throws IOException, SAXException {

    config.load(stream);
  }

  /**
   *
   *
   * @param stream
   * @param name
   * @return created template
   * @throws IOException
   */
  public Template loadTemplate(InputStream stream, String name) throws IOException {

    ImageTemplate template = config.getTemplate(name);
    return loadTemplate(stream, template.getFields());
  }

  public void setOutputter(ImageTemplateOutputter _outputter) {
    outputter = _outputter;
  }

  /**
   * Register {@link FieldRenderer} for values of given <code>Class</code>
   *
   * @param clazz class of values
   * @param renderer renderer for the given class
   * @throws NullPointerException if one of arguments is <code>null</code>
   */
  public void addClassRenderer(Class clazz, FieldRenderer renderer) {
    if (clazz == null) {
      throw new NullPointerException("clazz argument cannot be null");
    }
    if (renderer == null) {
      throw new NullPointerException("renderer argument cannot be null");
    }
    if (log.isDebugEnabled()) {
      log.debug("register renderer: " + renderer.toString() + " for class: " + clazz.getName());
    }
    clazzRenderers.put(clazz, renderer);
  }

  /**
   * Register {@link FieldRenderer} for field with a given name.
   *
   * @param fieldName name of field
   * @param renderer renderer for fields with the given name
   * @throws NullPointerException if one of argument is <code>null</code>
   */
  public void addFieldRenderer(String fieldName, FieldRenderer renderer) {
    if (fieldName == null) {
      throw new NullPointerException("fieldName argument cannot be null");
    }
    if (renderer == null) {
      throw new NullPointerException("renderer cannot be null");
    }
    if (log.isDebugEnabled()) {
      log.debug("setting renderer: " + renderer.toString() + " for field: " + fieldName);
    }
    fieldRenderers.put(fieldName, renderer);
  }

  /**
   * Set Log for using during processing images.
   *
   * Log instance may be accessed using {@link #getLog} method.
   *
   * @param _log instance which will be used during loading and rendering images.
   */
  public final void setLog(Log _log) {
    if (_log == null) {
      throw new NullPointerException("Log cannot be null");
    }
    log = _log;
  }

  /**
   * Return Log instance used by this Loggable.
   */
  public Log getLog() {
    return log;
  }
}

