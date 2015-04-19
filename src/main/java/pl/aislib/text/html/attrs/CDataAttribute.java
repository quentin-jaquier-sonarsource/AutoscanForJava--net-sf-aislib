package pl.aislib.text.html.attrs;

/**
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class CDataAttribute extends AbstractHTMLAttribute {

  /**
   *
   */
  public CDataAttribute(String name) throws ClassNotFoundException {
    super(name, "java.lang.String");
  }

} // class
