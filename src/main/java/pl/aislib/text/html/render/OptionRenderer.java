package pl.aislib.text.html.render;

/**
 * Defines minimal set of methods which is required for rendering some <em>Object</em> to HTML Option element.
 *
 * @author Michal Jastak
 * @since AISLIB 0.5
 */
public interface OptionRenderer {

  /**
   * Retrieves HTML Option Value attribute.
   * @param object rendered <em>Object</em>
   * @return <em>HTML Option</em> value attribute
   */
  public String getValue(Object object);

  /**
   * Retrieves HTML Option content.
   * @param object rendered <em>Object</em>
   * @return <em>HTML Option</em> content
   */
  public String getContent(Object object);

} // interface
