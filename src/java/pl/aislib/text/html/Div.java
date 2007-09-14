package pl.aislib.text.html;

import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EventsAttributesSet;

/**
 * Represents HTML 'div' element.
 * Right now div may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs (see: {@link CoreAttributesSet})</li>
 *     <li>Events (see: {@link EventsAttributesSet})</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.1
 */
public class Div extends AbstractHTMLObject {
  
  /**
   *
   */
  public Div() {
    super("div");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
  }

} // class
