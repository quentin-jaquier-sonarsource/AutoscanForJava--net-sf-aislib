package pl.aislib.util.template;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Map;

/**
 * Interface represents dynamic output.
 *
 * @author Tomasz Pik, AIS.PL
 */
public interface Template {

  /**
   * Adds value with given key to template.
   *
   * @param key key in template.
   * @param value value for key.
   */
  public void setValue(String key, Object value);

  /**
   * Adds map of values to template.
   *
   * @param map of keys and values.
   */
  public void setValues(Map map);

  /**
   * Process template content and write result to stream.
   *
   * @param stream for write result of template processing.
   * @throws IOException in case of problems.
   */
  public void writeTo(OutputStream stream) throws IOException;

  /**
   * Process template content and return result as array of bytes.
   *
   * @return array of bytes contains result of template processng.
   */
  public byte[] toByteArray();

}
