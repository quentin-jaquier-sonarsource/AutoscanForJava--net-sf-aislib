package pl.aislib.test.lang;

import pl.aislib.lang.Task;
import pl.aislib.lang.TaskProcessingException;

/**
 * Task test class.
 */
public class SimpleTask extends Task {

  private Object  result;

  private boolean doFail = false;

  /**
   * Constructor.
   *
   * @param result object.
   */
  public SimpleTask(Object result) {
    this.result = result;
  }
 
  /**
   * @see pl.aislib.lang.Task#process()
   */
  public Object process() throws TaskProcessingException {
    if (doFail) {
      throw new TaskProcessingException("I have failed !!!");
    }
    return result;
  }

  /**
   * @return boolean.
   */
  public boolean getDoFail() {
    return doFail;
  }

  /**
   * @param doFail boolean.
   */
  public void setDoFail(boolean doFail) {
    this.doFail = doFail;
  }
}

