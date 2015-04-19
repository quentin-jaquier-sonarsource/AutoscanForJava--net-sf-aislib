package pl.aislib.lang;

import java.util.Vector;

/**
 * @author Tomasz Pik, AIS.PL
 */
class TaskListenerSupport {

  private Vector taskListeners;

  TaskListenerSupport() {
    taskListeners = new Vector();
  }

  void addTaskListener(TaskListener listener) {
    taskListeners.addElement(listener);
  }

  void removeTaskListener(TaskListener listener) {
    taskListeners.removeElement(listener);
  }

  void fireTaskEvent(Object object) {
    Vector l;
    synchronized (this) {
      l = (Vector) taskListeners.clone();
    }

    for (int i = 0; i < l.size(); ++i) {
      TaskListener task = (TaskListener) l.elementAt(i);
      if (task instanceof ExtendedTaskListener) {
        ((ExtendedTaskListener) task).taskNotify(object);
      }
    }
  }

  void updateProgress(float progress)  {
    Vector l;
    synchronized (this) {
      l = (Vector) taskListeners.clone();
    }

    for (int i = 0; i < l.size(); ++i) {
      TaskListener task = (TaskListener) l.elementAt(i);
      if (task instanceof ExtendedTaskListener) {
        ((ExtendedTaskListener) task).taskProgress(progress);
      }
    }
  }

  /**
   * Notify all registerd task listeners with {@link TaskListener#taskFailed}
   *
   * @param TaskProcessingException exception to propagate.
   */
  void fireTaskFailed(TaskProcessingException e) {
    Vector l;
    synchronized (this) {
      l = (Vector) taskListeners.clone();
    }

    for (int i = 0; i < l.size(); ++i) {
      TaskListener task = (TaskListener) l.elementAt(i);
      task.taskFailed(e);
    }
  }

  /**
   * Notify all registered task listeners with {@link TaskListener#taskFinished}
   *
   * @param result returned from {@link #process}
   */
  void fireTaskFinish(Object result) {
    Vector l;
    synchronized (this) {
      l = (Vector) taskListeners.clone();
    }

    for (int i = 0; i < l.size(); ++i) {
      TaskListener task = (TaskListener) l.elementAt(i);
      task.taskFinished(result);
    }
  }

  /**
   * Notify all registered task listeners with {@link TaskListener#taskStarted}
   */
  void fireTaskStart() {
    Vector l;
    synchronized (this) {
      l = (Vector) taskListeners.clone();
    }

    for (int i = 0; i < l.size(); ++i) {
      TaskListener task = (TaskListener) l.elementAt(i);
      task.taskStarted();
    }
  }
}
