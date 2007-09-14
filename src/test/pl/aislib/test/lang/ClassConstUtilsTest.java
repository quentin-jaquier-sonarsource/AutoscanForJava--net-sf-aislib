/*
 * Created on 2004-05-05
 *
 */

package pl.aislib.test.lang;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import pl.aislib.lang.ClassConstUtils;
import pl.aislib.lang.ReflectionException;
import junit.framework.TestCase;

/**
 * @author Tomasz Sandecki, AIS.PL
 * @version $Revision: 1.1 $
 */
public class ClassConstUtilsTest extends TestCase {

  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * test result if argument is null result should be null
   *
   */
  public void testNullAsClass() {
    try {
      Map map = ClassConstUtils.constsFromClass(null);
      assertNull(map);
    } catch (ReflectionException e) {
      fail();
    }
  }

  public void testJavaUtilCalendarConsts() {
    try {
      Map map = ClassConstUtils.constsFromClass(Calendar.class);
      assertNotNull(map); // result coulnd not be null!

      // test if we have some const
      assertEquals(map.get("MONDAY"), new Integer(Calendar.MONDAY));
      assertNull(map.get("NOT_IN_CALENDAR"));
      assertEquals(map.get("MONDAY"), new Integer(Calendar.MONDAY));
    } catch (ReflectionException e) {
      fail();
    }
  }

  /**
   *
   *
   */
  public void testJavaUtilGrgorianCalendarConsts() {
    try {
      Map map = ClassConstUtils.constsFromClass(Calendar.class);
      assertNotNull(map); // result coulnd not be null!

      // test if we have some const
      assertEquals(map.get("MONDAY"), new Integer(GregorianCalendar.MONDAY));
      assertNull(map.get("NOT_IN_CALENDAR"));
      assertEquals(map.get("MONDAY"), new Integer(GregorianCalendar.MONDAY));
    } catch (ReflectionException e) {
      fail();
    }
  }

  /**
   *
   *
   */
  public void testConstsFromCalendaInGregorianClaenda() {
    try {
      Map map = ClassConstUtils.constsFromClass(GregorianCalendar.class);
      assertNotNull(map); // result coulnd not be null!

      // test if we have some const
      assertEquals(map.get("MONDAY"), new Integer(Calendar.MONDAY));
      assertNull(map.get("NOT_IN_CALENDAR"));
      assertEquals(map.get("MONDAY"), new Integer(Calendar.MONDAY));
    } catch (ReflectionException e) {
      fail();
    }
  }

  /**
   * Access to fields of non public class/interface
   *
   */
  public void testIllegalAccess() {
    try {
      Map map = ClassConstUtils.constsFromClass(InternalConstsClass.class);
      assertTrue(false);
    } catch (ReflectionException e) {
      assertTrue(true);
    }
  }


  /**
   * we should have no access to non static, and not public fields
   *
   */
  public void testCustomNonStaticConsts() {
    try {
      Map map = ClassConstUtils.constsFromClass(MyConstsClass.class);
      assertNotNull(map); // result coulnd not be null!

      MyConstsClass testConstsClass = new MyConstsClass();
      assertNull(map.get("STRING_PUBLIC_FINAL"));
      assertNull(map.get("STRING_PROTECTED_FINAL"));
      assertNull(map.get("STRING_PRIVATE_FINAL"));
      assertNull(map.get("STRING_PROTECTED_STATIC_FINAL"));
      assertNull(map.get("STRING_PUBLIC_STATIC"));
      assertNull(map.get("STRING_PROTECTED_STATIC"));
      assertNull(map.get("STRING_PRIVATE_STATIC"));
      assertNull(map.get("method"));
    } catch (ReflectionException e) {
      e.printStackTrace();
      fail();
    }
  }


  /**
   * access only to public static final fields
   *
   */
  public void testCustomPublicStaticConsts() {
    try {
      Map map = ClassConstUtils.constsFromClass(MyConstsClass.class);
      assertNotNull(map); // result coulnd not be null!

      MyConstsClass testConstsClass = new MyConstsClass();
      assertEquals(map.get("STRING_PUBLIC_STATIC_FINAL"), MyConstsClass.STRING_PUBLIC_STATIC_FINAL);
    } catch (ReflectionException e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testCustomPublicStaticConstsFromInterface() {
    try {
      Map map = ClassConstUtils.constsFromClass(MyConstsInterface.class);
      assertNotNull(map); // result coulnd not be null!

      MyConstsClass testConstsClass = new MyConstsClass();
      assertEquals(map.get("STRING_PUBLIC_STATIC_FINAL"), MyConstsInterface.STRING_PUBLIC_STATIC_FINAL);
    } catch (ReflectionException e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testCustomPublicConstsFromInterface() {
    try {
      Map map = ClassConstUtils.constsFromClass(MyConstsInterface.class);
      assertNotNull(map); // result coulnd not be null!

      MyConstsClass testConstsClass = new MyConstsClass();
      assertEquals(map.get("STRING_PUBLIC_FINAL"), MyConstsInterface.STRING_PUBLIC_FINAL);
    } catch (ReflectionException e) {
      e.printStackTrace();
      fail();
    }
  }
}

class InternalConstsClass {
  public final String STRING_PUBLIC_FINAL = "ala";

  protected final String STRING_PROTECTED_FINAL = "something";

  private final String STRING_PRIVATE_FINAL = "private";

  public static final String STRING_PUBLIC_STATIC_FINAL = "STRING_PUBLIC_STATIC_FINAL";

  protected static final String STRING_PROTECTED_STATIC_FINAL = "STRING_PROTECTED_STATIC_FINAL";

  private static final String STRING_PRIVATE_STATIC_FINAL = "STRING_PRIVATE_STATIC_FINAL";

  public final void method() {
    // empty method
  }
}
