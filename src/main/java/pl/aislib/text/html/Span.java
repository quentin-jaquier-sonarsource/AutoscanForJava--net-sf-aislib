package pl.aislib.text.html;

import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EventsAttributesSet;

/**
 * Represents HTML 'span' element.
 * Right now span may have:
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
public class Span extends AbstractHTMLObject {
  
  /**
   *
   */
  public Span() {
    super("span");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
  }

} // class
