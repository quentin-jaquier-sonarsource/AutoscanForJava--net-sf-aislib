package pl.aislib.test.text.html;

import junit.framework.TestCase;

import pl.aislib.text.html.Select;

import pl.aislib.text.html.attrs.AttributeException;

public class SelectTest extends TestCase {

  private Select select;

  public SelectTest(String name) {
    super(name);
  }

  public void setUp() {
    select = new Select();
  }

  public void testSetSelected() throws Exception {
    try {
      select.setAttributeValue("selected", "selected");
      fail("'selected' attribute with 'selected' value");
    } catch (AttributeException ae) {
    }
  }

  public void testSetName() throws Exception {
    String name = "ala ma kota";
    select.setAttributeValue("name", name);
    assertEquals("'name'", select.getAttributeValue("name"), name);
  }
}
