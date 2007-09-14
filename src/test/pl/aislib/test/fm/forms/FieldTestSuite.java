package pl.aislib.test.fm.forms;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for testing fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
 */
public class FieldTestSuite {

  // Public methods

  /**
   * @return <code>Test</code> object.
   */
  public static Test suite() {
    TestSuite suite = new TestSuite("Field tests");
    //$JUnit-BEGIN$
    suite.addTest(new TestSuite(FieldStringTest.class));
    suite.addTest(new TestSuite(FieldStringDynamicTest.class));
    suite.addTest(new TestSuite(FieldIntegerTest.class));
    suite.addTest(new TestSuite(FieldLongTest.class));
    suite.addTest(new TestSuite(FieldFloatTest.class));
    suite.addTest(new TestSuite(FieldDoubleTest.class));
    suite.addTest(new TestSuite(FieldBigDecimalTest.class));
    suite.addTest(new TestSuite(FieldDateTest.class));
    suite.addTest(new TestSuite(FieldDateDynamicTest.class));
    suite.addTest(new TestSuite(FieldURLTest.class));
    suite.addTest(new TestSuite(FieldEmailTest.class));
    suite.addTest(new TestSuite(FieldZipTest.class));
    suite.addTest(new TestSuite(FieldSSNTest.class));
    suite.addTest(new TestSuite(FieldArrayTest.class));
    //$JUnit-END$
    return suite;
  }

} // FieldTestSuite class
