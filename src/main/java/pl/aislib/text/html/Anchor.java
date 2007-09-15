package pl.aislib.text.html;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EventsAttributesSet;

/**
 * Represents HTML 'a' element.
 * Right now it may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs</li>
 *     <li>EventsAttrs</li>
 *     <li>accesskey</li>
 *     <li>charset</li>
 *     <li>coords</li>
 *     <li>href</li>
 *     <li>hreflang</li>
 *     <li>name</li>
 *     <li>onblur</li>
 *     <li>onfocus</li>
 *     <li>rel</li>
 *     <li>rev</li>
 *     <li>shape</li>
 *     <li>tabindex</li>
 *     <li>type</li>
 *   </ul>
 * </li>
 * <li>any content</li> 
 * </ul>
 * @author Michal Jastak, AIS.PL
 * @see CoreAttributesSet
 * @see EventsAttributesSet
 * @since AISLIB 0.2
 * @version $Revision: 1.1.1.1 $
 */
public class Anchor extends AbstractHTMLObject {

  /**
   * Constructor.
   */
  public Anchor() {
    super("a");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
    AttributesSet selectAttrs = new AttributesSet();
    try {
      selectAttrs.add(new CDataAttribute("accesskey"));
      selectAttrs.add(new CDataAttribute("charset"));
      selectAttrs.add(new CDataAttribute("coords"));
      selectAttrs.add(new CDataAttribute("href"));
      selectAttrs.add(new CDataAttribute("hreflang"));
      selectAttrs.add(new CDataAttribute("name"));
      selectAttrs.add(new CDataAttribute("onblur"));
      selectAttrs.add(new CDataAttribute("onfocus"));
      selectAttrs.add(new CDataAttribute("rel"));
      selectAttrs.add(new CDataAttribute("rev"));
      selectAttrs.add(new CDataAttribute("shape"));
      selectAttrs.add(new CDataAttribute("tabindex"));
      selectAttrs.add(new CDataAttribute("type"));
    } catch (ClassNotFoundException cnfe) {
    }
    addAttributesSet(selectAttrs);
  }
  
} // class
