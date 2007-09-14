package pl.aislib.util;

import java.io.Serializable;

/**
 * Small but useful class of pairs of objects.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public final class Pair implements Serializable {

  /** First object in the pair. */
  private Object first;

  /** Second object in the pair. */
  private Object second;

  /**
   * @param first an object.
   * @param second an object.
   */
  public Pair(Object first, Object second) {
    this.first = first;
    this.second = second;
  }

  /**
   * @return first object in the pair.
   */
  public Object getFirst() {
    return first;
  }

  /**
   * @return second object in the pair.
   */
  public Object getSecond() {
    return second;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "(" + String.valueOf(first) + "," + String.valueOf(second) + ")";
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return ("(" + String.valueOf(first) + "," + String.valueOf(second) + ")").hashCode();
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    try {
      Pair pair = (Pair) object;
      return (equals(first, pair.getFirst()) && equals(second, pair.getSecond()));
    } catch (ClassCastException cce) {
      return false;
    }
  }

  /**
   * Checks if two objects are equals.
   *
   * Checks if two objects are equals. Assume, that <code>null == null</code>.
   *
   * @param o1 object to compare.
   * @param o2 object to compare.
   * @return <code>true</code> if both arguments are null or equal, <code>false</code> otherwise.
   */
  private boolean equals(Object o1, Object o2) {
    if ((o1 == null) && (o2 == null)) {
      return true;
    }
    if (o1 != null) {
      if (o2 == null) {
        return false;
      } else {
        return o1.equals(o2);
      }
    } else {
      return false;
    }
  }
}
