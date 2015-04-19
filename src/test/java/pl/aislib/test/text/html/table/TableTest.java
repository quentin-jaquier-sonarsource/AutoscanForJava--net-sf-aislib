package pl.aislib.test.text.html.table;

import junit.framework.TestCase;

import pl.aislib.text.html.table.Table;

import pl.aislib.text.html.attrs.AttributeException;

/**
 * @author Michal Jastak
 */
public class TableTest extends TestCase {

  private Table table;

  /**
   *
   */
  public TableTest(String name) {
    super(name);
  }

  /**
   *
   */
  public void setUp() {
    table = new Table();
  }

  /**
   *
   */
  public void testBgColor() throws Exception {
    try {
      table.setAttributeValue("bgcolor", "blue");
    } catch (AttributeException ae) {
      fail("'bgcolor' attribute with 'blue' value");
    }
  }

} // class

/**
 * $Log: TableTest.java,v $
 * Revision 1.1.1.1  2003/02/27 12:25:06  pikus
 * initial import
 *
 * Revision 1.1.1.1  2003/02/27 12:07:30  pikus
 * initial import
 *
 * Revision 1.1.1.1  2003/02/24 07:43:13  pikus
 *
 *
 * Revision 1.3  2002/09/23 14:34:14  wswiatek
 * Correction of package name.
 *
 * Revision 1.2  2002/06/19 09:40:18  warlock
 * at least version which can be compiled
 *
 * Revision 1.1  2002/06/19 09:38:28  warlock
 * Table.java moved to TableTest.java
 *
 * Revision 1.1  2002/06/19 09:36:34  warlock
 * initial commit
 *
 */
