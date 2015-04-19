package pl.aislib.text.html;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EnumeratedAttribute;
import pl.aislib.text.html.attrs.EventsAttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;

/**
 * Represents HTML 'img' element.
 * Right now img may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs (see: {@link CoreAttributesSet})</li>
 *     <li>Events (see: {@link EventsAttributesSet})</li>
 *     <li>align (deprecated)</li>
 *     <li>border (deprecated)</li>
 *     <li>alt</li>
 *     <li>height</li>
 *     <li>hspace (deprecated)</li>
 *     <li>ismap</li>
 *     <li>longdesc</li>
 *     <li>name</li>
 *     <li>src</li>
 *     <li>usemap</li>
 *     <li>width</li>
 *     <li>vspace (deprecated)</li>
 *   </ul>
 * </li>
 * <li>no content</li>
 * </ul>
 * <dl>
 *   <dt><b>Note:</b></dt>
 *   <dd><code>align</code>, <code>border</code>, <code>hspace</code>, <code>ismap</code>, <code>longdesc</code>,
 *       <code>name</code>, <code>usemap</code>, <code>vspace</code> attributes are available since
 *       <tt>AISLIB 0.2</tt></dd>
 * </dl>
 * @author Michal Jastak, AIS.PL
 * @since AISLIB 0.1
 */
public class Img extends AbstractHTMLObject {

  /**
   *
   */
  public Img() {
    super("img");

    setKeepWithNext(true);
    setKeepWithPrevious(true);

    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
    AttributesSet privateAttrs = new AttributesSet();
    try {
      EnumeratedAttribute enumAttr = new EnumeratedAttribute("align");
      enumAttr.addAllowedValue("bottom");
      enumAttr.addAllowedValue("left");
      enumAttr.addAllowedValue("middle");
      enumAttr.addAllowedValue("right");
      enumAttr.addAllowedValue("top");
      privateAttrs.add(enumAttr);

      privateAttrs.add(new CDataAttribute("alt"));
      privateAttrs.add(new CDataAttribute("border"));
      privateAttrs.add(new CDataAttribute("height"));
      privateAttrs.add(new CDataAttribute("hspace"));
      privateAttrs.add(new CDataAttribute("ismap"));
      privateAttrs.add(new CDataAttribute("longdesc"));
      privateAttrs.add(new CDataAttribute("name"));
      privateAttrs.add(new CDataAttribute("src"));
      privateAttrs.add(new CDataAttribute("usemap"));
      privateAttrs.add(new CDataAttribute("width"));
      privateAttrs.add(new CDataAttribute("vspace"));
    } catch (ClassNotFoundException cnfe) { ; }
    addAttributesSet(privateAttrs);
  }

} // class
