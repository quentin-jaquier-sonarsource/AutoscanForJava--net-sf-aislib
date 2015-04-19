package pl.aislib.util;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @author Tomasz Pik, AIS.PL
 * @author Michal Jastak, AIS.PL
 */
public abstract class AbstractTransactionalBean implements Serializable {

  private Stack history;
  private Set   stateChanges;

  private static final Null NULL = new Null();

  /**
   * Constructor for AbstractTransactionalBean.
   */
  protected AbstractTransactionalBean() {
    history      = new Stack();
    stateChanges = new HashSet();
  }

  /**
   *
   */
  protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    if (oldValue == null) {
      oldValue = NULL;
    }
    if (newValue == null) {
      newValue = NULL;
    }
    if (!newValue.equals(oldValue)) {
      stateChanges.add(new PropertyChange(propertyName, newValue));
    }
  }

  /**
   *
   */
  public final int newSavepoint() {
    history.push(stateChanges);
    stateChanges = new HashSet();
    return history.size();
  }

  /**
   *
   */
  public final void rollback(int savepoint) {
    int size = history.size();
    while ((size > 0) && (size > savepoint)) {
      history.pop();
      size--;
    }
    Set changes = (Set) history.peek();
    Iterator iter = changes.iterator();
    while (iter.hasNext()) {
      PropertyChange change = (PropertyChange) iter.next();
      Object oldValue = change.getValue();
      if (oldValue.equals(NULL)) {
        oldValue = null;
      }
      try {
        BeanUtils.setProperty(this, change.propertyName(), oldValue);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }

  /**
   *
   */
  private class PropertyChange {
    private String propertyName;
    private Object propertyValue;

    /**
     *
     */
    PropertyChange(String propertyName, Object propertyValue) {
      this.propertyName  = propertyName;
      this.propertyValue = propertyValue;
    }

    /**
     *
     */
    Object getValue() {
      return propertyValue;
    }

    /**
     *
     */
    String propertyName() {
      return propertyName;
    }

    /**
     *
     */
    public int hashCode() {
      return ("PropertyChange " + propertyName).hashCode();
    }

    /**
     *
     */
    public boolean equals(Object o) {
      try {
        PropertyChange propertyChange = (PropertyChange) o;
        return propertyChange.propertyName().equals(propertyName);
      } catch (ClassCastException cce) {
        return false;
      }
    }

    /**
     *
     */
    public String toString() {
      return "PropertyChange: " + propertyName + " - " + propertyValue;
    }

  } // class PropertyChange

  /**
   *
   */
  private static final class Null {

    /**
     *
     */
    public String toString() {
      return "NULL";
    }
  } // class Null

} // class
