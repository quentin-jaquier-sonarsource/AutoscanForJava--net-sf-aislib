package pl.aislib.text.html.table;

/**
 * Represents HTML <tt>td</tt> element.
 * Defines
 * <ul>
 * <li>attributes inherited from {@link AbstractTableCellObject}</li>
 * <li>any content</li>
 * </ul>
 * </p>
 * @author Michal Jastak
 * @since AISLIB 0.2
 */
public class TableDataCell extends AbstractTableCellObject {

  /**
   * Constructor.
   */
  public TableDataCell() {
    super("td");
  }

} // class
