package pl.aislib.text.html;

import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EventsAttributesSet;

/**
 * Represents HTML 'p' element.
 * Right now paragraph may have:
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
 * @since AISLIB 0.1
 */
public class Paragraph extends AbstractHTMLObject {

  /**
   *
   */
  public Paragraph() {
    super("p");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
  }

} // class
