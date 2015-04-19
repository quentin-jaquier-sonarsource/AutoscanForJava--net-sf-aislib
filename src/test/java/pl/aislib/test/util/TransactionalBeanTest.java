package pl.aislib.test.util;

import junit.framework.TestCase;

/**
 * @author Tomasz Pik, AIS.PL
 */
public class TransactionalBeanTest extends TestCase {

  private TransactionalBean bean;

  /**
   * Constructor for TransactionalBeanTest.
   * @param arg0
   */
  public TransactionalBeanTest(String arg0) {
    super(arg0);
  }

  public void setUp() {
    bean = new TransactionalBean();
  }

  public void testBeanFuncionality() {
    assertNull(bean.getName());
    assertNull(bean.getAge());

    bean.setName("ala");
    assertNotNull(bean.getName());
    assertEquals("ala", bean.getName());

    bean.setName(null);
    assertNull(bean.getName());

    bean.setAge(new Integer(5));
    assertNotNull(bean.getAge());
    assertEquals(new Integer(5), bean.getAge());

    bean.setAge(null);
    assertNull(bean.getAge());
  }

  public void testGettingSavepoint() {
    int savepoint1 = bean.newSavepoint();
    assertEquals(1, savepoint1);

    int savepoint2 = bean.newSavepoint();
    assertEquals(2, savepoint2);

    int savepoint3 = bean.newSavepoint();
    assertEquals(3, savepoint3);
  }

  public void testOneSavepoint() {
    assertNull(bean.getName());
    assertNull(bean.getAge());

    bean.setName("ala");
    assertNotNull(bean.getName());
    assertEquals("ala", bean.getName());

    bean.setAge(new Integer(5));
    assertNotNull(bean.getAge());
    assertEquals(new Integer(5), bean.getAge());

    int savepoint = bean.newSavepoint();
    assertEquals(1, savepoint);

    assertEquals("ala", bean.getName());
    assertEquals(new Integer(5), bean.getAge());

    bean.setName("zosia");
    assertEquals("zosia", bean.getName());

    bean.rollback(savepoint);
    assertEquals(new Integer(5), bean.getAge());
    assertEquals("ala", bean.getName());
  }

  public void testSavepoints() {
    assertNull(bean.getName());
    assertNull(bean.getAge());

    bean.setName("ala");
    assertNotNull(bean.getName());
    assertEquals("ala", bean.getName());

    bean.setAge(new Integer(5));
    assertNotNull(bean.getAge());
    assertEquals(new Integer(5), bean.getAge());

    int savepoint1 = bean.newSavepoint();
    assertEquals(1, savepoint1);

    bean.setAge(new Integer(6));
    assertNotNull(bean.getAge());
    assertEquals(new Integer(6), bean.getAge());

    int savepoint2 = bean.newSavepoint();
    assertEquals(2, savepoint2);

    bean.setAge(new Integer(7));
    assertNotNull(bean.getAge());
    assertEquals(new Integer(7), bean.getAge());

    int savepoint3 = bean.newSavepoint();
    assertEquals(3, savepoint3);

    bean.rollback(savepoint1);

    assertEquals(new Integer(5), bean.getAge());
    assertEquals("ala", bean.getName());
  }

}
