package pl.aislib.text.html.table;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;
import pl.aislib.text.html.attrs.EnumeratedAttribute;

/**
 * Represents HTML <tt>table</tt> element.
 * Defines
 * <ul>
 * <li>attributes inherited from {@link AbstractTableObject}</li>
 * <li>and following attributes:
 *   <ul>
 *     <li>bgcolor</li>
 *     <li>border</li>
 *     <li>cellpadding</li>
 *     <li>cellspacing</li>
 *     <li>frame</li>
 *     <li>rules</li>
 *     <li>summary</li>
 *     <li>width</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * </p>
 * @author Michal Jastak
 * @version $Revision: 1.2 $
 * @since AISLIB 0.2
 */
public class Table extends AbstractTableObject {

  /**
   * Constructor.
   */
  public Table() {
    super("table");
    AttributesSet attrs = new AttributesSet();
    try {
      attrs.add(new CDataAttribute("bgcolor"));
      attrs.add(new CDataAttribute("border"));
      attrs.add(new CDataAttribute("cellpadding"));
      attrs.add(new CDataAttribute("cellspacing"));

      EnumeratedAttribute enAttr = new EnumeratedAttribute("frame");
      enAttr.addAllowedValue("above");
      enAttr.addAllowedValue("below");
      enAttr.addAllowedValue("border");
      enAttr.addAllowedValue("box");
      enAttr.addAllowedValue("hsides");
      enAttr.addAllowedValue("lhs");
      enAttr.addAllowedValue("rhs");
      enAttr.addAllowedValue("void");
      enAttr.addAllowedValue("vsides");
      attrs.add(enAttr);

      enAttr = new EnumeratedAttribute("rules");
      enAttr.addAllowedValue("all");
      enAttr.addAllowedValue("cols");
      enAttr.addAllowedValue("groups");
      enAttr.addAllowedValue("none");
      enAttr.addAllowedValue("rows");
      attrs.add(enAttr);

      attrs.add(new CDataAttribute("summary"));
      attrs.add(new CDataAttribute("width"));

    } catch (ClassNotFoundException cnfe) {
      ; // nop
    }
    addAttributesSet(attrs);
  }

} // class
