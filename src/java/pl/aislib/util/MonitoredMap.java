package pl.aislib.util;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

import org.apache.commons.logging.Log;

/**
 * Deprecated.
 *
 * @deprecated This class is deprecated and will be removed.
 */
public class MonitoredMap implements Map {

  protected Map monitoredMap;
  protected Log log;

  public MonitoredMap(Map map, Log log) {
    if (map == null) {
      throw new NullPointerException("map cannot be null");
    }
    if (log == null) {
      throw new NullPointerException("log cannot be null");
    }
    monitoredMap = map;
    this.log     = log;
  }

  public void clear() {
    if (log.isDebugEnabled()) {
      log.debug("clear");
    } 
    monitoredMap.clear();
  }
  
  public int size() {
    if (log.isDebugEnabled()) {
      log.debug("size");
    }
    return monitoredMap.size();
  }

  public boolean isEmpty() {
    if (log.isDebugEnabled()) {
      log.debug("isEmpty");
    }
    return monitoredMap.isEmpty();
  }

  public boolean containsKey(Object key) {
    if (log.isDebugEnabled()) {
      if (key != null) {
        log.debug("containsKey: " + key);
      } else {
        log.debug("containsKey null");
      }
    }
    return monitoredMap.containsKey(key);
  }

  public boolean containsValue(Object value) {
     if (log.isDebugEnabled()) {
       if (value != null) {
         log.debug("containsValue: " + value);
       } else {
         log.debug("containsValue null");
       }
     }
    return monitoredMap.containsValue(value);
  }

  public Object get(Object key) {
    if (log.isDebugEnabled()) {
      if (key != null) {
        log.debug("get: " + key);
      } else {
        log.debug("get null");
      }
    }
    return monitoredMap.get(key);
  }

  public Object put(Object key, Object value) {
    if (log.isDebugEnabled()) {
      if ((key != null) && (value != null)) {
        log.debug("put: (" + key + ", " + value + ")");
      } else if (key == null) {
        log.debug("put (null, " + value + ")");
      } else {
        log.debug("put (" + key + ", null)");  
      }
    }
    return monitoredMap.put(key, value);
  }

  public Object remove(Object key) {
    if (log.isDebugEnabled()) {
      if (key != null) {
        log.debug("remove: " + key);
      } else {
        log.debug("remove null");
      }
    }
    return monitoredMap.remove(key);
  }

  public void putAll(Map map) {
    if (log.isDebugEnabled()) {
      if (map != null) {
        log.debug("putAll: " + map);
      } else {
        log.debug("putAll null");
      }
    }
    monitoredMap.putAll(map);
  }

  public Set keySet() {
    if (log.isDebugEnabled()) {
      log.debug("keySet");
    }
    return new MonitoredSet(monitoredMap.keySet(), log);
  }

  public Collection values() {
    if (log.isDebugEnabled()) {
      log.debug("values");
    }
    return monitoredMap.values();
  }

  public Set entrySet() {
    if (log.isDebugEnabled()) {
      log.debug("entrySet");
    }
    return new MonitoredSet(monitoredMap.entrySet(), log);
  }
}
