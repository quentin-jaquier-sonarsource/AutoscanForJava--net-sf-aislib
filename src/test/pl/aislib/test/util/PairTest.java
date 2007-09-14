package pl.aislib.test.util;

import pl.aislib.util.Pair;
import junit.framework.TestCase;

/**
 * Tests {@link Pair} class implementation.
 * 
 * Tests {@link Pair} class implementation, especially {@link Pair#equals(Object)} method.
 * 
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 */
public class PairTest extends TestCase {

  private Pair nullAndNull1;
  private Pair nullAndNull2;

  private Pair nullAndNotNull1;
  private Pair nullAndNotNull2;
  private Pair nullAndNotNull3;


  private Pair notNullAndNull1;
  private Pair notNullAndNull2;
  private Pair notNullAndNull3;

  private Pair notNullAndNotNull1;
  private Pair notNullAndNotNull2;
  private Pair notNullAndNotNull3;

  /**
   * Create test data.
   */
  public void setUp() {
    nullAndNull1 = new Pair(null, null);
    nullAndNull2 = new Pair(null, null);

    nullAndNotNull1 = new Pair(null, "right");
    nullAndNotNull2 = new Pair(null, "right");
    nullAndNotNull3 = new Pair(null, "RIGHT");

    notNullAndNull1 = new Pair("left", null);
    notNullAndNull2 = new Pair("left", null);
    notNullAndNull3 = new Pair("LEFT", null);
    
    notNullAndNotNull1 = new Pair("left", "right");
    notNullAndNotNull2 = new Pair("left", "right");
    notNullAndNotNull3 = new Pair("LEFT", "RIGHT");
  }

  /**
   * Tests equality of pairs having two nulls.
   */
  public void testTwoNullsEquality() {
    assertTrue(nullAndNull1.equals(nullAndNull1));
    assertTrue(nullAndNull1.equals(nullAndNull2));
  }

  /**
   * Tests equality of two pairs having null as first counterpart.
   */
  public void testNullAndNotNullEquality() {
    assertTrue(nullAndNotNull1.equals(nullAndNotNull1));
    assertTrue(nullAndNotNull1.equals(nullAndNotNull2));
    assertFalse(nullAndNotNull1.equals(nullAndNotNull3));
  }

  /**
   * Tests equality of two pairs having null as second counterpart.
   */
  public void testNotNullAndNullEquality() {
    assertTrue(notNullAndNull1.equals(notNullAndNull1));
    assertTrue(notNullAndNull1.equals(notNullAndNull2));
    assertFalse(notNullAndNull1.equals(notNullAndNotNull3));
  }

  /**
   * Tests equality of two pairs having not null as both counterparts.
   */
  public void testNotNullAndNotNullEquality() {
    assertTrue(notNullAndNotNull1.equals(notNullAndNotNull1));
    assertTrue(notNullAndNotNull1.equals(notNullAndNotNull2));
    assertFalse(notNullAndNotNull1.equals(notNullAndNotNull3));
  }

  /**
   * Tests inequalities of two pairs.
   */
  public void testInequalities() {
    assertFalse(nullAndNull1.equals(nullAndNotNull1));
    assertFalse(nullAndNull1.equals(notNullAndNull1));
    assertFalse(nullAndNull1.equals(notNullAndNotNull1));
    assertFalse(nullAndNotNull1.equals(notNullAndNull1));
    assertFalse(nullAndNotNull1.equals(notNullAndNotNull1));
  } 
}
