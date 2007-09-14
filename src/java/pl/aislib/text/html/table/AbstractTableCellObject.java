package pl.aislib.text.html.table;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;
import pl.aislib.text.html.attrs.EnumeratedAttribute;

/**
 * Base class for all table cell objects.
 * Defines
 * <ul>
 * <li>attributes inherited from {@link AbstractTableObject}</li>
 * <li>and following attributes:
 *   <ul>
 *     <li>abbr</li>
 *     <li>align</li>
 *     <li>axis</li>
 *     <li>bgcolor</li>
 *     <li>char</li>
 *     <li>charoff</li>
 *     <li>colspan</li>
 *     <li>headers</li>
 *     <li>height</li>
 *     <li>nowrap</li>
 *     <li>rowspan</li>
 *     <li>scope</li>
 *     <li>width</li>
 *     <li>valign</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * </p>
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.2
 */
class AbstractTableCellObject extends AbstractTableObject {

  /**
   * Constructor.
   */
  protected AbstractTableCellObject() {
    super("__table_cell_object");
    AttributesSet attrs = new AttributesSet();
    try {
      attrs.add(new CDataAttribute("abbr"));

      EnumeratedAttribute enAttr = new EnumeratedAttribute("align");
      enAttr.addAllowedValue("center");
      enAttr.addAllowedValue("char");
      enAttr.addAllowedValue("justify");
      enAttr.addAllowedValue("left");
      enAttr.addAllowedValue("right");
      attrs.add(enAttr);

      attrs.add(new CDataAttribute("axis"));
      attrs.add(new CDataAttribute("bgcolor"));
      attrs.add(new CDataAttribute("char"));
      attrs.add(new CDataAttribute("charoff"));
      attrs.add(new CDataAttribute("colspan"));
      attrs.add(new CDataAttribute("headers"));
      attrs.add(new CDataAttribute("height"));

      enAttr = new EnumeratedAttribute("nowrap");
      enAttr.addAllowedValue("nowrap");
      attrs.add(enAttr);

      attrs.add(new CDataAttribute("rowspan"));

      enAttr = new EnumeratedAttribute("scope");
      enAttr.addAllowedValue("col");
      enAttr.addAllowedValue("colgroup");
      enAttr.addAllowedValue("row");
      enAttr.addAllowedValue("rowgroup");
      attrs.add(enAttr);

      attrs.add(new CDataAttribute("width"));

      enAttr = new EnumeratedAttribute("valign");
      enAttr.addAllowedValue("baseline");
      enAttr.addAllowedValue("bottom");
      enAttr.addAllowedValue("middle");
      enAttr.addAllowedValue("top");
      attrs.add(enAttr);

    } catch (ClassNotFoundException cnfe) { }
    addAttributesSet(attrs);
  }

  /**
   * Constructor.
   */
  protected AbstractTableCellObject(String objectName) {
    this();
    htmlName = objectName;
  }

} // class
