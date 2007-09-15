package pl.aislib.test.lang;

import junit.framework.TestCase;

import pl.aislib.lang.Task;

/**
 * Testing tasks JUnit class.
 */
public class TaskTest extends TestCase {

  private Object         result;
  private Task           task;
  private SimpleListener taskListener;

  /**
   * @see junit.framework.TestCase#TestCase(String)
   */
  public TaskTest(String testName) {
    super(testName);
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() {
    result       = new Object();
    task         = new SimpleTask(result);
    taskListener = new SimpleListener();

    task.addTaskListener(taskListener);
  }
  
  
    /**
   * @throws Exception if an error has occurred.
   */
  public void testTaskStarted() throws Exception {
    task.start();
    task.join();
    assertTrue("task started", taskListener.getStarted());
  }

    /**
   * @throws Exception if an error has occurred.
   */
  public void testStartedCalledOnce() throws Exception {
    task.start();
    task.join();
    assertTrue("start called once", taskListener.getStartedCalledOnce());
  }

    /**
   * @throws Exception if an error has occurred.
   */
  public void testTaskFinished() throws Exception {
    task.start();
    task.join();
    assertTrue("task finished", taskListener.getFinished());
  }

    /**
   * @throws Exception if an error has occurred.
   */
  public void testFinishedCalledOnce() throws Exception {
    task.start();
    task.join();
    assertTrue("finished called once", taskListener.getFinishedCalledOnce());
  }

    /**
   * @throws Exception if an error has occurred.
   */
  public void testStartedBeforeFinished() throws Exception {
    task.start();
    task.join();
    assertTrue("started before finished", taskListener.getStartedBeforeFinished());
  }

    /**
   * @throws Exception if an error has occurred.
   */
  public void testFinishedAfterStarted() throws Exception {
    task.start();
    task.join();
    assertTrue("finished after started", taskListener.getFinishedAfterStarted());
  }

    /**
   * @throws Exception if an error has occurred.
   */
  public void testPassingResult() throws Exception {
    task.start();
    task.join();
    assertSame("passing result", result, taskListener.getResult());
  }

    /**
   * @throws Exception if an error has occurred.
   */
  public void testTaskFailed() throws Exception {
    ((SimpleTask) task).setDoFail(true);
    task.start();
    task.join();
    assertTrue("task failed", taskListener.getFailed());
  }

}

