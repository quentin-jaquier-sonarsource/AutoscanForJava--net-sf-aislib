package pl.aislib.text.html.render;

/**
 * Dummy OptionRenderer implementation.
 * @author Michal Jastak
 * @since AISLIB 0.5
 */
public class DummyOptionRenderer implements OptionRenderer {

  /**
   * 
   * @param object rendered <em>Object</em> 
   * @return result of <code>object.toString()</code> method
   */
  public String getContent(Object object) {
    return toString(object);
  }

  /**
   *
   * @param object rendered <em>Object</em>
   * @return result of <code>object.toString()</code> method
   */
  public String getValue(Object object) {
    return toString(object);
  }

  private String toString(Object object) {
    if (object != null) {
      return object.toString();
    }
    return "";
  }

} // class DummyOptionRenderer
