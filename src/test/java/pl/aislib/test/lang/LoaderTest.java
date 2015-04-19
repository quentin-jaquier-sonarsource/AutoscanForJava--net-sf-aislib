package pl.aislib.test.lang;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Properties;

import pl.aislib.lang.Loader;
import pl.aislib.lang.ReflectionException;
import junit.framework.TestCase;

/**
 * TestCase for {@link Loader} class.
 *
 * @author Tomasz Pik, AIS.PL
 */
public class LoaderTest extends TestCase {

  private static final String NON_EXISTING_CLASS = "pl.aislib.lang.LoaderTest";
  /**
   * Constructor for LoaderTest.
   *
   * @param arg0
   */
  public LoaderTest(String arg0) {
    super(arg0);
  }

  /**
   * Tests {@link Loader#findClass(String)}.
   *
   * @throws Exception in case if incorrect behavior.
   */
  public void testFindLoaderTestClass() throws Exception {
    String clazzName = "pl.aislib.test.lang.LoaderTest";

    Class clazz = Loader.findClass(clazzName);
    assertNotNull("LoaderTest", clazz);
    assertEquals("LoaderTest", clazz.getName(), clazzName);
    assertEquals("LoaderTest", clazz, getClass());

    try {
      Loader.findClass(NON_EXISTING_CLASS);
      fail("pl.aislib.lang.LoaderTest should not exists");
    } catch (Exception e) {
      ; // OK
    }
  }

  /**
   * Tests {@link Loader#findResource(String)}.
   */
  public void testFindResource() {
    String clazzResource = "pl/aislib/test/lang/LoaderTest.class";

    URL url = Loader.findResource(clazzResource);
    assertNotNull("LoaderTest", url);
    assertTrue(url.toString().endsWith(clazzResource));

    assertNull(Loader.findResource("pl/aislib/test/lang/LoaderTest.clas"));
  }

  /**
   * Tests successfull {@link Loader#instantiate(String)}.
   *
   * @throws Exception exception thrown in <code>Loader</code> call.
   */
  public void testInstantiateOK() throws Exception {
    Object res = Loader.instantiate(ClassOK.class.getName());
  }

  /**
   * Tests {@link Loader#instantiate(String)} failed due to <code>InstantiationException</code>.
   */
  public void testInstantiateIE1() {
    try {
      Object res = Loader.instantiate(ClassArgCstr.class.getName());
    } catch (ReflectionException re) {
      assertNotNull(re.getCause());
      assertTrue(InstantiationException.class.isAssignableFrom(re.getCause().getClass()));
    }
  }

  /**
   * Tests {@link Loader#instantiate(String, Class)} with success.
   * @throws Exception
   */
  public void testInstantiationWithSpecificationOK() throws Exception {
    Loader.instantiate(DecimalFormat.class.getName(), NumberFormat.class);
  }

  /**
   * Tests {@link Loader#instantiate(String, Class)} with failure.
   * @throws Exception
   */
  public void testInstantiationWithSpecificationFailure() throws Exception {
    try {
      Loader.instantiate(Hashtable.class.getName(), Properties.class);
      fail("Hastable do not extends Properties");
    } catch (ReflectionException re) {
      assertTrue(ClassCastException.class.isAssignableFrom(re.getCause().getClass()));
    }
  }

  /**
   * Tests {@link Loader#instantiate(String)} failed due to <code>InstantiationException</code>.
   */
  public void testInstantiateIE2() {
    try {
      Object res = Loader.instantiate(ClassAbstr.class.getName());
    } catch (ReflectionException re) {
      assertNotNull(re.getCause());
      assertTrue(InstantiationException.class.isAssignableFrom(re.getCause().getClass()));
    }
  }

  /**
   * Tests {@link Loader#instantiate(String)} failed due to <code>IllegalAccessException</code>.
   */
  public void testInstantiateIAE() {
    try {
      Object res = Loader.instantiate(ClassPrivCstr.class.getName());
    } catch (ReflectionException re) {
      assertNotNull(re.getCause());
      assertTrue(IllegalAccessException.class.isAssignableFrom(re.getCause().getClass()));
    }
  }

  /**
   * Tests {@link Loader#instantiate(String)} failed due to <code>ClassNotFoundException</code>.
   */
  public void testInstantiateCNFE() {
    try {
      Object res = Loader.instantiate(NON_EXISTING_CLASS);
    } catch (ReflectionException re) {
      assertNotNull(re.getCause());
      assertTrue(ClassNotFoundException.class.isAssignableFrom(re.getCause().getClass()));
    }
  }
}
