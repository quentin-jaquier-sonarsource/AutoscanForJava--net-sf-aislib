package pl.aislib.text.html.table;

import pl.aislib.text.html.AbstractHTMLObject;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EventsAttributesSet;

/**
 * Base class for all table objects.
 * Defines
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs</li>
 *     <li>EventsAttrs</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * </p>
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @see CoreAttributesSet
 * @see EventsAttributesSet
 * @since AISLIB 0.2
 */
class AbstractTableObject extends AbstractHTMLObject {

  /**
   * Constructor.
   */
  protected AbstractTableObject() {
    super("__table_object");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
  }

  /**
   * Constructor.
   */
  protected AbstractTableObject(String objectName) {
    this();
    htmlName = objectName;
  }

} // class
