package pl.aislib.fm;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Wrapper for attribute names.
 * 
 * @author Pawel Chmielewski, AIS.PL
 * @version $Revision: 1.4 $
 * @since AISLIB 0.4
 */
public class AttributeNameWrapper {

  /**
   * Predicate.
   */
  private AttributeNamePredicate predicate;

  /**
   * Cutter.
   */
  private AttributeNameCutter cutter;

  /**
   * Adder.
   */
  private AttributeNameAdder adder;

  // Constructors

  /**
   * Creates a new <code>AttributeNameWrapper</code> instance.
   *
   * @param prefix <code>String</code> object.
   */
  protected AttributeNameWrapper(String prefix) {
    predicate = new AttributeNamePredicate(prefix);
    cutter = new AttributeNameCutter(predicate);
    adder = new AttributeNameAdder();
  }

  // Public methods

  /**
   * @param name <code>String</code> object.
   * @return <code>String</code> object.
   */
  public String wrap(String name) {
    return (String) adder.transform(name);
  }

  /**
   * @param name <code>String</code> object.
   * @return <code>String</code> object.
   */
  public String unwrap(String name) {
    return (String) cutter.transform(name);
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
  protected class AttributeNamePredicate {

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
     * 
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
  class AttributeNameCutter {

    /**
     * Length to which predicate evaluation is cut.
     */
    private int predicateLength;

    // Constructors

    /**
     * @param predicate <code>Predicate</code> object.
     */
    public AttributeNameCutter(AttributeNamePredicate predicate) {
      predicateLength = predicate.toString().length();
    }

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
  class AttributeNameAdder {

    // Public methods

    /**
     * 
     */
    public Object transform(Object input) {
      return predicate.toString() + input.toString();
    }

  } // AttributeNameAdder class

  class EnumerationIterator implements Iterator {

    private Enumeration enumeration;

    EnumerationIterator(Enumeration enumeration) {
      this.enumeration = enumeration;
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return enumeration.hasMoreElements();
    }

    /**
     * @see java.util.Iterator#next()
     */
    public Object next() {
      return enumeration.nextElement();
    }
  }

  class FilterIterator implements Iterator {

    /** The iterator being used */
    private Iterator iterator;
    /** The predicate being used */
    private AttributeNamePredicate predicate;
    /** The next object in the iteration */
    private Object nextObject;
    /** Whether the next object has been calculated yet */
    private boolean nextObjectSet = false;

    /**
     * Constructs a new <code>FilterIterator</code> that will not function
     * until {@link #setPredicate(Predicate) setPredicate} is invoked.
     *
     * @param iterator  the iterator to use
     */
    /**
     * Constructs a new <code>FilterIterator</code> that will use the
     * given iterator and predicate.
     *
     * @param iterator  the iterator to use
     * @param predicate  the predicate to use
     */
    public FilterIterator(Iterator iterator, AttributeNamePredicate predicate) {
      this.iterator = iterator;
      this.predicate = predicate;
    }

    //-----------------------------------------------------------------------
    /** 
     * Returns true if the underlying iterator contains an object that 
     * matches the predicate.
     *
     * @return true if there is another object that matches the predicate 
     */
    public boolean hasNext() {
      if (nextObjectSet) {
        return true;
      } else {
        return setNextObject();
      }
    }

    /** 
     * Returns the next object that matches the predicate.
     * 
     * @return the next object which matches the given predicate
     * @throws NoSuchElementException if there are no more elements that
     *  match the predicate 
     */
    public Object next() {
      if (!nextObjectSet) {
        if (!setNextObject()) {
          throw new NoSuchElementException();
        }
      }
      nextObjectSet = false;
      return nextObject;
    }

    /**
     * Removes from the underlying collection of the base iterator the last
     * element returned by this iterator.
     * This method can only be called
     * if <code>next()</code> was called, but not after
     * <code>hasNext()</code>, because the <code>hasNext()</code> call
     * changes the base iterator.
     * 
     * @throws IllegalStateException if <code>hasNext()</code> has already
     *  been called.
     */
    public void remove() {
      if (nextObjectSet) {
        throw new IllegalStateException("remove() cannot be called");
      }
      iterator.remove();
    }

    //-----------------------------------------------------------------------
    /**
     * Set nextObject to the next object. If there are no more 
     * objects then return false. Otherwise, return true.
     */
    private boolean setNextObject() {
      while (iterator.hasNext()) {
        Object object = iterator.next();
        if (predicate.evaluate(object)) {
          nextObject = object;
          nextObjectSet = true;
          return true;
        }
      }
      return false;
    }
  }

  class TransformIterator implements Iterator {

    /** The iterator being used */
    private Iterator iterator;
    /** The transformer being used */
    private AttributeNameCutter transformer;

    /**
     * Constructs a new <code>TransformIterator</code> that will use the
     * given iterator and transformer.  If the given transformer is null,
     * then objects will not be transformed.
     *
     * @param iterator  the iterator to use
     * @param transformer  the transformer to use
     */
    public TransformIterator(Iterator iterator, AttributeNameCutter transformer) {
      this.iterator = iterator;
      this.transformer = transformer;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return iterator.hasNext();
    }

    /**
     * Gets the next object from the iteration, transforming it using the
     * current transformer. If the transformer is null, no transformation
     * occurs and the object from the iterator is returned directly.
     * 
     * @return the next object
     * @throws java.util.NoSuchElementException if there are no more elements
     */
    public Object next() {
      return transform(iterator.next());
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      iterator.remove();
    }

    //-----------------------------------------------------------------------
    /**
     * Transforms the given object using the transformer.
     * If the transformer is null, the original object is returned as-is.
     *
     * @param source  the object to transform
     * @return the transformed object
     */
    protected Object transform(Object source) {
      if (transformer != null) {
        return transformer.transform(source);
      }
      return source;
    }
  }
} // AttributeNameWrapper class
