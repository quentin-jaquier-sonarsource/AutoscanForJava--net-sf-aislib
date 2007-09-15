package pl.aislib.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomasz Sandecki, AIS.PL
 * @version $Revision: 1.3 $
 */
public class ClassConstUtils {
  /**
   * Returns all final static from given class as a map. Keys are fields' names
   * and values are fields' values
   *
   * @param clazz
   *          class to map
   *
   * @return final static fields map or null (if <code>clazz<code> is null)
   */
  public static Map constsFromClass(Class clazz) throws ReflectionException {
    if (clazz == null) {
      return null;
    }
    Map map = new HashMap();
    String className = clazz.getName();
    Field[] fields = clazz.getFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      try {
        if ((field.getModifiers() & (Modifier.STATIC | Modifier.FINAL | Modifier.PUBLIC)) == (Modifier.STATIC
            | Modifier.FINAL | Modifier.PUBLIC)) {
          map.put(field.getName(), field.get(null));
        }
      } catch (IllegalAccessException e) {
        throw new ReflectionException(className, e);
      }
    }
    return map;
  }
}
