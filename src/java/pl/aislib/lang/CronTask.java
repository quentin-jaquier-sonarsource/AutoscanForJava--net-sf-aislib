package pl.aislib.lang;

import java.util.TimerTask;

/**
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.1 $
 */
public abstract class CronTask extends TimerTask {

  private TaskListenerSupport listenerSupport = new TaskListenerSupport();

  /**
   * Performs task operation.
   * 
   * @return result of given task.
   * @throws TaskProcessingException if something goes wrong.
   */
  public abstract Object process() throws TaskProcessingException;

  /**
   * @see java.lang.Runnable#run()
   */
  public void run() {
    try {
      listenerSupport.fireTaskStart();
      Object result = process();
      listenerSupport.fireTaskFinish(result);
    } catch (TaskProcessingException tpe) {
      listenerSupport.fireTaskFailed(tpe);
    }
  }

  /**
   * Adds the specified task listener to receive events from this task.
   *
   * @param listener listener to be added. 
   */
  public void addTaskListener(TaskListener listener) {
    listenerSupport.addTaskListener(listener);
  }

  /**
   * Removes the specified task listener so that it no longer receives events from this task.
   *
   * @param listener listener to remove.
   */
  public void removeTaskListener(TaskListener listener) {
    listenerSupport.removeTaskListener(listener);
  }

  /**
   * Notify all registerd extended task listeners with {@link ExtendedTaskListener#taskNotify} 
   *
   * @param object object to propagate
   */
  protected void fireTaskEvent(Object object) {
    listenerSupport.fireTaskEvent(object);
  }

  /**
   * Notify all registerd extended task listeners with {@link ExtendedTaskListener#taskProgress}
   *
   * @param progress progress of task (should be between 0 and 1)
   */
  protected void updateProgress(float progress)  {
    listenerSupport.updateProgress(progress);
  }
} // class
