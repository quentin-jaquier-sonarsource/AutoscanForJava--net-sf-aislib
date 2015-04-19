package pl.aislib.text.html;

import org.jdom.CDATA;
import org.jdom.Element;

/**
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class CDataNode extends AbstractHTMLObject {

  /**
   *
   */
  protected String value;

  /**
   *
   */
  public CDataNode(String value) {
    super("_CDataNode");
    this.value = value;
  }

  /**
   *
   */
  protected Element toXML(Element parent) {
    if (value != null) {
      parent.addContent(new CDATA(value));
    }
    return null;
  }

} // class
