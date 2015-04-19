package pl.aislib.text.html.attrs;

/**
 * Represents <code>CoreAttrs</code> Attributes Set.
 * Contains following attributes:
 * <ul>
 *   <li>id</li><li>class</li><li>style</li><li>title</li>
 * </ul>
 *
 * @author Michal Jastak
 * @since AISLIB 0.1
 * @see <a href='http://www.w3.org/TR/html4/sgml/dtd.html#coreattrs'>CoreAttrs</a>
 */
public class CoreAttributesSet extends AttributesSet {

  /**
   *
   */
  public CoreAttributesSet() {
    super();
    try {
      add(new CDataAttribute("id"));
      add(new CDataAttribute("class"));
      add(new CDataAttribute("style"));
      add(new CDataAttribute("title"));
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe.getMessage());
    }
  }

} // class
