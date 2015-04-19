package pl.aislib.text.html.table;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;
import pl.aislib.text.html.attrs.EnumeratedAttribute;

/**
 * Represents HTML <tt>tr</tt> element.
 * Defines
 * <ul>
 * <li>attributes inherited from {@link AbstractTableObject}</li>
 * <li>and following attributes:
 *   <ul>
 *     <li>align</li>
 *     <li>bgcolor</li>
 *     <li>char</li>
 *     <li>charoff</li>
 *     <li>valign</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * </p>
 * @author Michal Jastak
 * @since AISLIB 0.2
 */
public class TableRow extends AbstractTableObject {

  /**
   * Constructor.
   */
  public TableRow() {
    super("tr");
    AttributesSet attrs = new AttributesSet();
    try {
      EnumeratedAttribute enAttr = new EnumeratedAttribute("align");
      enAttr.addAllowedValue("center");
      enAttr.addAllowedValue("char");
      enAttr.addAllowedValue("justify");
      enAttr.addAllowedValue("left");
      enAttr.addAllowedValue("right");
      attrs.add(enAttr);

      attrs.add(new CDataAttribute("bgcolor"));
      attrs.add(new CDataAttribute("char"));
      attrs.add(new CDataAttribute("charoff"));

      enAttr = new EnumeratedAttribute("valign");
      enAttr.addAllowedValue("baseline");
      enAttr.addAllowedValue("bottom");
      enAttr.addAllowedValue("middle");
      enAttr.addAllowedValue("top");
      attrs.add(enAttr);

    } catch (ClassNotFoundException cnfe) { }
    addAttributesSet(attrs);
  }

} // class
