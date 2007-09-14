package pl.aislib.fm;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.EnumerationIterator;
import org.apache.commons.collections.FilterIterator;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.TransformIterator;
import org.apache.commons.collections.Transformer;


/**
 * Wrapper for attribute names.
 *
 * @author Pawel Chmielewski, AIS.PL
 * @version $Revision: 1.3 $
 * @since AISLIB 0.4
 */
public class AttributeNameWrapper {

  /**
   * Predicate.
   */
  private Predicate predicate;

  /**
   * Cutter.
   */
  private Transformer cutter;

  /**
   * Adder.
   */
  private Transformer adder;


  // Constructors

  /**
   * Creates a new <code>AttributeNameWrapper</code> instance.
   *
   * @param prefix <code>String</code> object.
   */
  protected AttributeNameWrapper(String prefix) {
    predicate = new AttributeNamePredicate(prefix);
    cutter    = new AttributeNameCutter(predicate);
    adder     = new AttributeNameAdder();
  }


  // Public methods

  /**
   * @param name <code>String</code> object.
   * @return <code>String</code> object.
   */
  public String wrap(String name) {
    return (String) adder.transform((Object) name);
  }

  /**
   * @param name <code>String</code> object.
   * @return <code>String</code> object.
   */
  public String unwrap(String name) {
    return (String) cutter.transform((Object) name);
  }

  /**
   * @param namesEnumeration <code>Enumeration</code> object.
   * @return <code>Iterator</code> object.
   */
  public Iterator unwrap(Enumeration namesEnumeration) {
    Iterator namesIterator = new EnumerationIterator(namesEnumeration);
    Iterator appNamesIterator = new FilterIterator(namesIterator, predicate);
    return new TransformIterator(appNamesIterator, cutter);
  }

  /**
   * @param name <code>String</code> object.
   * @return true if predicate is true.
   */
  public boolean match(String name) {
    return predicate.evaluate(name);
  }

  /**
   * @param namesEnumeration <code>Enumeration</code> object.
   * @return <code>Iterator</code> object.
   */
  public Iterator getMatched(Enumeration namesEnumeration) {
    List result = new LinkedList();
    while (namesEnumeration.hasMoreElements()) {
      String attrName = (String) namesEnumeration.nextElement();
      if (match(attrName)) {
        result.add(attrName);
      }
    }
    return result.iterator();
  }


  // Protected classes

  /**
   * Class of predicates starting with a given prefix.
   *
   * @author Pawel Chmielewski, AIS.PL
   */
  protected class AttributeNamePredicate implements Predicate {

    /**
     * Prefix string.
     */
    private String prefix;


    // Constructors

    /**
     * @param prefix <code>String</code> object.
     */
    AttributeNamePredicate(String prefix) {
      this.prefix = prefix + ".";
    }


    // Public methods

    /**
     * @see Predicate#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object obj) {
      return obj.toString().startsWith(prefix);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      return prefix;
    }

  } // AttributeNamePredicate class


  /**
   * Class of transformers cutting predicate evaluation object to a given length.
   *
   * @author Pawel Chmielewski, AIS.PL
   */
  class AttributeNameCutter implements Transformer {

    /**
     * Length to which predicate evaluation is cut.
     */
    private int predicateLength;


    // Constructors

    /**
     * @param predicate <code>Predicate</code> object.
     */
    public AttributeNameCutter(Predicate predicate) {
      predicateLength = predicate.toString().length();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object input) {
      if (predicate.evaluate(input)) {
        return input.toString().substring(predicateLength);
      }
      return input;
    }

  } // AttributeNameCutter class


  /**
   * Inner class of transformers adding string to predicate evaluation object.
   *
   * @author Pawel Chmielewski, AIS.PL
   */
  class AttributeNameAdder implements Transformer {

    // Public methods

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object input) {
      return predicate.toString() + input.toString();
    }

  } // AttributeNameAdder class

} // AttributeNameWrapper class
