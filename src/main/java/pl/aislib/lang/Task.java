package pl.aislib.lang;

/**
 * Task extends {@link Thread} with for notifications.
 *
 * <p>Usage scenario:
 * <pre>
 * public class SimpleTask extends Task {
 *   public Object process() {
 *     return new Integer(0);
 *   }
 * }
 *
 * // piece of code:
 * Task task = new SimpleTask();
 * task.start();
 * </pre>
 * </p>
 * <p>There's a possibility to register listeners with Task:
 * <ul>
 *   <li>
 *     Listeners, which implements only <code>TaskListener</code> are notified
 *     directly by {@link #process} method.
 *   </li>
 *   <li>
 *     Listeners, which implements <code>ExtendedTaskListerers</code> are
 *     notified only from those tasks, that calls {@link #fireTaskEvent}
 *     (this is propagated to {@link ExtendedTaskListener#taskNotify}) or
 *     {@link #updateProgress} (this is propagated to {@link ExtendedTaskListener#taskProgress}).
 *   </li>
 * </ul>
 * </p>
 *
 * @author Tomasz Sandecki, AIS.PL
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 * @since AISLIB 0.3
 */
public abstract class Task extends Thread {

  private TaskListenerSupport listenerSupport = new TaskListenerSupport();

  /**
   * @see Thread#Thread
   */
  public Task() {
    super();
  }

  /**
   * @see Thread#Thread(String)
   */
  public Task(String taskName) {
    super(taskName);
  }

  /**
   * @see Thread#Thread(ThreadGroup,String)
   */
  public Task(ThreadGroup threadGroup, String taskName) {
    super(threadGroup, taskName);
  }

  /**
   * Perform task operation.
   *
   * @return result of given task.
   * @throws TaskProcessingException if something goes wrong.
   */
  public abstract Object process() throws TaskProcessingException;
  
  /**
   * Execute task operation.
   *
   * Perform following steps:
   * <ul>
   *   <li>notify all registered listeners with {@link TaskListener#taskStarted}</li>
   *   <li>call {@link #process}</li>
   *   <li>if <code>process</code> return with a result, notify all registered listeners 
   *       with {@link TaskListener#taskFinished}</li>
   *   <li>if <code>process</code> throws an exception, notify all registered listeners 
   *       with {@link TaskListener#taskFailed}
   * </ul>
   * <p><b>NOTE:</b> don't call this method directly, use {@link Thread#start} instead.</p>
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
