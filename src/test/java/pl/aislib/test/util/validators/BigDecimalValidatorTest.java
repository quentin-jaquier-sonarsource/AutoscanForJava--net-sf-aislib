package pl.aislib.test.util.validators;

import java.math.BigDecimal;

import junit.framework.TestCase;

import pl.aislib.util.validators.BigDecimalValidator;

/**
 * Test for BigDecimal validator.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.3 $
 */
public class BigDecimalValidatorTest extends TestCase {

  private BigDecimalValidator validator;
  private final String bigNumber = "1234354353432432432434242454365567890.12";
  private final String smallNumber = "890.12";

  /**
   * Creates validator instance with <code>US</code> locales.
   */
  public void setUp() {
    validator = new BigDecimalValidator();
    validator.setLocale("en_US");
    validator.setUseICU4J(false);
  }

  /**
   * Tests if <code>BigValidator</code> correctly handles big numbers with ICU4J.
   *
   * @throws Exception if it occurs.
   */
  public void testBigBigDecimalWithICU4J() throws Exception {
    validator.setUseICU4J(true);
    BigDecimal value = (BigDecimal) validator.validate(bigNumber);
    assertNotNull(value);
    System.err.println("V: " + value);
    assertEquals(bigNumber, value.toString());
  }

  /**
   * Tests if <code>BigValidator</code> correctly handles small numbers with ICU4J.
   *
   * @throws Exception if it occurs.
   */
  public void testSmallBigDecimalWithICU4J() throws Exception {
    validator.setUseICU4J(true);
    BigDecimal value = (BigDecimal) validator.validate(smallNumber);
    assertNotNull(value);
    assertEquals(smallNumber, value.toString());
  }

  /**
   * Tests if <code>BigValidator</code> correctly handles small numbers without ICU4J.
   *
   * @throws Exception if it occurs.
   */
  public void testSmallBigDecimalWithoutICU4J() throws Exception {
    validator.setUseICU4J(false);
    BigDecimal value = (BigDecimal) validator.validate(smallNumber);
    assertNotNull(value);
    assertEquals(smallNumber, value.toString());
  }

  /**
   * Tests if <code>BigValidator</code> incorrectly handles big numbers without ICU4J.
   *
   * @throws Exception if it occurs.
   */
  public void testBigBigDecimalWithoutICU4J() throws Exception {
    validator.setUseICU4J(false);
    BigDecimal value = (BigDecimal) validator.validate(bigNumber);
    assertNotNull(value);
    assertFalse(bigNumber.equals(value.toString()));
  }

} // pl.aislib.test.util.validators.BigDecimalValidatorTest class
