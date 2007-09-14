package pl.aislib.util.xml;

import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

/**
 * Simple Class creating XHTML from XML representation in JDOM.
 * <dl>
 *   <dt><b>Note:</b></dt>
 *   <dd><code>XHtmlOutputter</code> uses <i>ais-xhtml-fo</i> namespace
 *     attributes for formatting XHTML output.</dd>
 * </dl>
 * @author Michal Jastak
 * @since AISLIB 0.2
 * @version $Revision: 1.1.1.1 $
 */
public class XHtmlOutputter {

  /**
   *
   */
  protected String lineSeparator;

  /**
   *
   */
  protected String indentString;

  /**
   * Character used to enclose HTML attribute value.
   */
  protected char quotationMark;

  /**
   *
   */
  public XHtmlOutputter() {
    lineSeparator = System.getProperty("line.separator");
    quotationMark = '\"';
    indentString  = "  ";
  }

  /**
   *
   */
  protected String outputString(Element element, String tabulator, boolean keepWithPrevious) {
    boolean      keepWithNext = false;
    String       elementText  = element.getText();
    StringBuffer result       = new StringBuffer();

    if (!keepWithPrevious) {
      result.append(lineSeparator + tabulator);
    }

    result.append("<" + element.getQualifiedName());

    List attrs = element.getAttributes();
    for (Iterator it = attrs.iterator(); it.hasNext();) {
      Attribute attr = (Attribute) it.next();
      if (!attr.getNamespacePrefix().equals("ais-xhtml-fo")) {
        result.append(" " + attr.getQualifiedName() + "=" + quotationMark + attr.getValue() + quotationMark);
      }
    }
  
    List content = element.getContent();

    boolean hasContent = element.hasChildren() | (elementText.length() > 0) | !content.isEmpty();

    if (hasContent) {
      result.append(">");
      for (Iterator it = content.iterator(); it.hasNext();) {
        Object object = it.next();
        if (object instanceof String) {
          result.append((String) object);
          continue;
        }
        if (object instanceof Text) {
          result.append(((Text) object).getText());
          continue;
        }
        if (object instanceof Element) {
          Element child = (Element) object;
          result.append(outputString(child, tabulator + indentString, shallIKeepWith(child, "keep-with-previous")));
          keepWithNext = shallIKeepWith(child, "keep-with-next");
          continue;
        }
        if (object instanceof Comment) {
          result.append("<!-- " + ((Comment) object).getText() + " -->");
          continue;
        }
        if (object instanceof EntityRef) {
          result.append("&" + ((EntityRef) object).getName() + ";");
          continue;
        }
        if (object instanceof ProcessingInstruction) {
          // FIXME
          result.append(((ProcessingInstruction) object).toString());
          continue;
        }
        if (object instanceof CDATA) {
          result.append("<![CDATA[" + ((CDATA) object).getText() + "]]>");
          continue;
        }
      }

      if (!keepWithNext && element.hasChildren()) {
        result.append(lineSeparator + tabulator);
      }
      result.append("</" + element.getQualifiedName() + ">");
    } else {
      result.append(" />");
    }

    return result.toString();
  }

  /**
   *
   */
  public String outputString(Element element) {
    return outputString(element, "", false);
  }

  /**
   *
   */
  protected boolean shallIKeepWith(Element element, String attrName) {
    boolean   result = false;
    Attribute attr   = element.getAttribute(attrName, Namespace.getNamespace("ais-xhtml-fo", "ais-xhtml-fo"));
    if (attr != null) {
      try {
        result = attr.getBooleanValue();
      } catch (DataConversionException dcex) { ; }
    }
    return result;
  }

  /**
   *
   */
  public String getLineSeparator() {
    return lineSeparator;
  }

  /**
   *
   */
  public void setLineSeparator(String lineSeparator) {
    this.lineSeparator = lineSeparator;
  }

  /**
   *
   */
  public char getQuotationMark() {
    return quotationMark;
  }

  /**
   *
   */
  public void setQuotationMark(char quotationMark) {
    this.quotationMark = quotationMark;
  }

} // class
