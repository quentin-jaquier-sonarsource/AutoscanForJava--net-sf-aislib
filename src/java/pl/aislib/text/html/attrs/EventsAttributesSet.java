package pl.aislib.text.html.attrs;

/**
 * Represents <code>Events</code> Attributes Set.
 * Contains following attributes:
 * <ul>
 *   <li>onclick</li><li>ondblclick</li><li>onmousedown</li>
 *   <li>onmouseup</li><li>onmouseover</li><li>onmousemove</li>
 *   <li>onmouseout</li><li>onkeypress</li><li>onkeydown</li>
 *   <li>onkeyup</li>
 * </ul>
 * 
 * @author Michal Jastak 
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.1
 * @see <a href='http://www.w3.org/TR/html4/sgml/dtd.html#events'>Events</a>
 */
public class EventsAttributesSet extends AttributesSet {

  /**
   *
   */
  public EventsAttributesSet() {
    super();
    try {
      add(new CDataAttribute("onclick"));
      add(new CDataAttribute("ondblclick"));
      add(new CDataAttribute("onmousedown"));
      add(new CDataAttribute("onmouseup"));
      add(new CDataAttribute("onmouseover"));
      add(new CDataAttribute("onmousemove"));
      add(new CDataAttribute("onmouseout"));
      add(new CDataAttribute("onkeypress"));
      add(new CDataAttribute("onkeydown"));
      add(new CDataAttribute("onkeyup"));
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe.getMessage());
    }
  }

} // class
