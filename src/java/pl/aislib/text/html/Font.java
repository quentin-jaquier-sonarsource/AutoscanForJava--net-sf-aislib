package pl.aislib.text.html;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;

/**
 * Represents HTML 'font' element.
 * Right now font may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs (see: {@link CoreAttributesSet})</li>
 *     <li>color</li>
 *     <li>face</li>
 *     <li>size</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * See {@link CDataAttribute} for <code>cite</code>.
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.1
 */
public class Font extends AbstractHTMLObject {
  
  /**
   *
   */
  public Font() {
    super("font");
    addAttributesSet(new CoreAttributesSet());
    AttributesSet privateAttrs = new AttributesSet();
    try {
      privateAttrs.add(new CDataAttribute("color"));
      privateAttrs.add(new CDataAttribute("face"));
      privateAttrs.add(new CDataAttribute("size"));
    } catch (ClassNotFoundException cnfe) { ; }
    addAttributesSet(privateAttrs);
  }

} // class
