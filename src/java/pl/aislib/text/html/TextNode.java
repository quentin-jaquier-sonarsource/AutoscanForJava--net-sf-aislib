package pl.aislib.text.html;

import org.jdom.Element;

/**
 * Class representing node containing text only.
 * @author Michal Jastak
 * @since AISLIB 0.1
 * @version $Revision: 1.1.1.1 $
 */
public class TextNode extends AbstractHTMLObject {
  
  /**
   *
   */
  protected String value;

  /**
   *
   */
  public TextNode(String value) {
    super("_TextNode");
    this.value = value;
  }

  /**
   *
   */
  protected Element toXML(Element parent) {
    if (value != null) {
      parent.addContent(value);
    }
    return null;
  }

} // class
