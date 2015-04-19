package pl.aislib.test.fm.forms;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for testing rules.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class RuleTestSuite {

  // Public methods

  /**
   * @return <code>Test</code> object.
   */
  public static Test suite() {
    TestSuite suite = new TestSuite("Rule tests");
    //$JUnit-BEGIN$
    suite.addTest(new TestSuite(RuleDateRangeTest.class));
    //$JUnit-END$
    return suite;
  }

} // RuleTestSuite class
