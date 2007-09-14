package pl.aislib.test.lang;

import pl.aislib.lang.TaskListener;
import pl.aislib.lang.TaskProcessingException;

/**
 * Test listener class.
 */
public class SimpleListener implements TaskListener {

  private boolean started               = false;
  private boolean startedCalledOnce     = true;
  private boolean finished              = false;
  private boolean finishedCalledOnce    = true;
  private boolean startedBeforeFinished = true;
  private boolean finishedAfterStarted  = true;
  private boolean failed                = false;
  private Object result;

  /**
   * @see pl.aislib.lang.TaskListener@taskStarted()
   */
  public void taskStarted() {
    if (finished) {
      startedBeforeFinished = false;
    }
    if (started) {
      startedCalledOnce = false;
    }
    started = true;
  }
  
  /**
   * @see pl.aislib.lang.TaskListener#taskFinished(Object)
   */
  public void taskFinished(Object result) {
    if (!started) {
      finishedAfterStarted = false;
    }
    if (finished) {
      finishedCalledOnce = false;
    }
    finished = true;
    this.result = result;
  }

  /**
   * @see pl.aislib.lang.TaskListener#taskFailed(TaskProcessingException)
   */
  public void taskFailed(TaskProcessingException tpe) {
    failed = true;
  }


  /**
   * @return boolean.
   */
  public boolean getStarted() {
    return started;
  }

  /**
   * @return boolean.
   */
  public boolean getStartedCalledOnce() {
    return startedCalledOnce;
  }

  /**
   * @return boolean.
   */
  public boolean getFinished() {
    return finished;
  }

  /**
   * @return boolean.
   */
  public boolean getFinishedCalledOnce() {
    return finishedCalledOnce;
  }

  /**
   * @return boolean.
   */
  public boolean getStartedBeforeFinished() {
    return startedBeforeFinished;
  }

  /**
   * @return boolean.
   */
  public boolean getFinishedAfterStarted() {
    return finishedAfterStarted;
  }

  /**
   * @return boolean.
   */
  public boolean getFailed() {
    return failed;
  }

  /**
   * @return object.
   */
  public Object getResult() {
    return result;
  }


  /**
   * @param started boolean.
   */
  public void setStarted(boolean started) {
    this.started = started;
  }
  
  /**
   * @param startedCalledOnce boolean.
   */
  public void setStartedCalledOnce(boolean startedCalledOnce) {
    this.startedCalledOnce = startedCalledOnce;
  }
  
  /**
   * @param finished boolean.
   */
  public void setFinished(boolean finished) {
    this.finished = finished;
  }
  
  /**
   * @param finishedCalledOnce boolean.
   */
  public void setFinishedCalledOnce(boolean finishedCalledOnce) {
    this.finishedCalledOnce = finishedCalledOnce;
  }
  
  /**
   * @param startedBeforeFinished boolean.
   */
  public void setStartedBeforeFinished(boolean startedBeforeFinished) {
    this.startedBeforeFinished = startedBeforeFinished;
  }
  
  /**
   * @param finishedAfterStarted boolean.
   */
  public void setFinishedAfterStarted(boolean finishedAfterStarted) {
    this.finishedAfterStarted = finishedAfterStarted;
  }
  
  /**
   * @param failed boolean.
   */
  public void setFailed(boolean failed) {
    this.failed = failed;
  }
  
  /**
   * @param result object.
   */
  public void setResult(Object result) {
    this.result = result;
  }

}

