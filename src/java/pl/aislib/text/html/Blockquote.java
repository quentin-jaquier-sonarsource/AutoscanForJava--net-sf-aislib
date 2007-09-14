package pl.aislib.text.html;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EventsAttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;

/**
 * Represents HTML 'blockquote' element.
 * Right now blockquote may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs (see: {@link CoreAttributesSet})</li>
 *     <li>Events (see: {@link EventsAttributesSet})</li>
 *     <li>cite</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * See {@link CDataAttribute} for <code>cite</code>.
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.1
 */
public class Blockquote extends AbstractHTMLObject {
  
  /**
   *
   */
  public Blockquote() {
    super("blockquote");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
    AttributesSet privateAttrs = new AttributesSet();
    try {
      privateAttrs.add(new CDataAttribute("cite"));
    } catch (ClassNotFoundException cnfe) { ; }
    addAttributesSet(privateAttrs);
  }

} // class
