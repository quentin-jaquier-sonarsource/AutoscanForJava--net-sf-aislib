package pl.aislib.util.template.image;

import java.awt.Image;
import java.awt.Frame;
import java.awt.Graphics;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;

import pl.aislib.util.template.Field;
import pl.aislib.util.template.Template;

/**
 * Template for generating images.
 *
 *
 * @since 0.2
 * @author Tomasz Pik
 */
public class ImageTemplate implements Template {

  private String name;

  private Image image;
  private ImageTemplateOutputter outputter;
  private Log log;

  private Map fields;
  private Map values;

  private Map clazzRenderers;
  private Map fieldRenderers;
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map getFields() {
    return fields;
  }

  public Field getField(String name) {
    return (Field) fields.get(name);
  }

  public Image getImage() {
    return image;
  }

  /** 
   * Dummy constructor for JavaBean creation. Initializes fields and fonts
   * as empty HashMaps.
   */
  public ImageTemplate() {
    fields = new HashMap();
  }

  /** 
   * Add Field to fields List. Used by the Digester for automatic
   * object creation.
   * 
   * @param field 
   */
  public void addField(Field field) {
    fields.put(field.getName(), field);
  }

  ImageTemplate(Image _image, ImageTemplateOutputter _outputter, Map _fields,
  Map _clazzRenderers, Map _fieldRenderers, Log _log) {
    image = _image;
    outputter = _outputter;
    fields = _fields;
    clazzRenderers = _clazzRenderers;
    fieldRenderers = _fieldRenderers;
    log = _log;

    values = new HashMap();
  }

  public void setValue(String key, Object value) {
    values.put(key, value);
  }

  public void setValues(Map map) {
    values.putAll(map);
  }

  /**
   * @throws UnsupportedOperationException if given graphics toolkit doesn't support requested alignment.
   * @throws IOException if there's a problem with writing to given stream.
   */
  public void writeTo(OutputStream stream) throws IOException {
    Frame frame = new Frame();
    frame.addNotify();
    Image image2 = frame.createImage(image.getWidth(frame), image.getHeight(frame));
    Graphics graphics = image2.getGraphics();
    graphics.drawImage(image, 0, 0, frame);
    Iterator keys = values.keySet().iterator();
    while (keys.hasNext()) {
      String fieldName = (String) keys.next();
      Field field = (Field) fields.get(fieldName);
      if (field != null) {
        Object value = values.get(fieldName);
        if (value != null) {
          FieldRenderer fieldRenderer = findFieldRenderer(fieldName, value.getClass());
          fieldRenderer.render(graphics, field, value);
        }
      }
    }
    outputter.writeImage(image2, stream);
  }

  public byte[] toByteArray() {
    try {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      writeTo(bOut);
      return bOut.toByteArray();
    } catch (IOException ioe) {
      log.fatal("exception catched during processing", ioe);
      throw new RuntimeException("exception catched during processing: " + ioe.getMessage());
    }
  }

  private FieldRenderer findFieldRenderer(String fieldName, Class valueClazz) {
    FieldRenderer result = (FieldRenderer) fieldRenderers.get(fieldName);
    if (result != null) {
      return result;
    }
    while (valueClazz != null) {
      result = (FieldRenderer) clazzRenderers.get(valueClazz);
      if (result != null) {
        return result;
      }
      valueClazz = valueClazz.getSuperclass();
    }
    return new SimpleFieldRenderer();
  }
}
