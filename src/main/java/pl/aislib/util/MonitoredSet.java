package pl.aislib.util;

import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

import org.apache.commons.logging.Log;

/**
 * Deprecated.
 *
 * @deprecated This class is deprecated and will be removed.
 */
public class MonitoredSet implements Set {

  protected Set monitoredSet;
  protected Log log;

  public MonitoredSet(Set set, Log log) {
    if (set == null) {
      throw new NullPointerException("set cannot be null");
    }
    if (log == null) {
      throw new NullPointerException("log cannot be null");
    }
    monitoredSet = set;
    this.log     = log;
  }

  public boolean add(Object o) {
    if (log.isDebugEnabled()) {
      if (o != null) {
        log.debug("add: " + o);
      } else {
        log.debug("add null");
      }
    }
    return monitoredSet.add(o);
  }

  public boolean remove(Object o) {
    if (log.isDebugEnabled()) {
      if (o != null) {
        log.debug("remove: " + o);
      } else {
        log.debug("remove null");
      }
    }
    return monitoredSet.remove(o);
  }

  public boolean containsAll(Collection collection) {
    if (log.isDebugEnabled()) {
      if (collection != null) {
        log.debug("containsAll: " + collection);
      } else {
        log.debug("containsAll null");
      }
    }
    return monitoredSet.containsAll(collection);
  }

  public boolean addAll(Collection collection) {
    if (log.isDebugEnabled()) {
      if (collection != null) {
        log.debug("addAll: " + collection);
      } else {
        log.debug("addAll null");
      }
    }
    return monitoredSet.addAll(collection);
  }

  public boolean retainAll(Collection collection) {
    if (log.isDebugEnabled()) {
      if (collection != null) {
        log.debug("retainAll: " + collection);
      } else {
        log.debug("retainAll null");
      } 
    } 
    return monitoredSet.retainAll(collection);
  }

  public boolean removeAll(Collection collection) {
    if (log.isDebugEnabled()) {
      if (collection != null) {
        log.debug("removeAll: " + collection);
      } else {
        log.debug("removeAll null");
      } 
    } 
    return monitoredSet.removeAll(collection);
  }

  public void clear() {
    if (log.isDebugEnabled()) {
      log.debug("clear");
    } 
    monitoredSet.clear();
  }
  
  public int size() {
    if (log.isDebugEnabled()) {
      log.debug("size");
    }
    return monitoredSet.size();
  }

  public boolean isEmpty() {
    if (log.isDebugEnabled()) {
      log.debug("isEmpty");
    }
    return monitoredSet.isEmpty();
  }

  public boolean contains(Object o) {
    if (log.isDebugEnabled()) {
      if (o != null) {
        log.debug("contains: " + o);
      } else {
        log.debug("contains null");
      }
    }
    return monitoredSet.contains(o);
  }

  public Iterator iterator() {
    if (log.isDebugEnabled()) {
      log.debug("iterator");
    }
    return monitoredSet.iterator();
  }

  public Object[] toArray() {
    if (log.isDebugEnabled()) {
      log.debug("toArray");
    }
    return monitoredSet.toArray();
  }

  public Object[] toArray(Object[] a) {
    if (log.isDebugEnabled()) {
      if (a != null) {
        log.debug("toArray[]");
      } else {
        log.debug("toArray null");
      }
    }
    return monitoredSet.toArray(a);
  }
}
