package pl.aislib.fm.shepherds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.lang.reflect.Constructor;

import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;

import pl.aislib.lang.Loader;

/**
 * Defines how Shepherds are trained.
 * @author Michal Jastak
 * @version $Revision: 1.6 $
 * @since AISLIB 0.4
 * @see Shepherd
 * @see ShepherdsDog
 */
public class ShepherdsSchool {

  private static final String DEFAULT_PREDICATE_CLASS = "pl.aislib.util.predicates.IsNotNullPredicate";

  private Map knownDogBreeds;
  private Map  knownShepherds;

  /**
   * Constructs new ShepherdsSchool object.
   */
  public ShepherdsSchool() {
    knownShepherds = new HashMap();
    knownShepherds.put("isNull",    "pl.aislib.util.predicates.IsNullPredicate");
    knownShepherds.put("isNotNull", "pl.aislib.util.predicates.IsNotNullPredicate");

    knownDogBreeds = new HashMap(4);
    registerDogBreed(new DogWatchingHttpRequestParameters());
    registerDogBreed(new DogWatchingHttpRequestAttributes());
    registerDogBreed(new DogWatchingHttpSessionAttributes());
    registerDogBreed(new DogWatchingHttpRequest());
  }

  private void registerDogBreed(ShepherdsDog dog) {
    knownDogBreeds.put(dog.getBreed(), dog);
  }

  /**
   * Creates new Shepherd. 
   * <p>New <code>Shepherd</code> is created using <code>Predicate</code>, <code>dogsBreed</code> 
   * and <code>pageRef</code> (see {@link Shepherd} class description). <br />
   * <code>Predicate</code> mentioned above is created by instatiating class found in following way:
   * <ul>
   *   <li>given <code>predicateName</code> is checked against known predicate names, if found, 
   *   Class corresponding to it is used for constructing new <code>Predicate</code> object,</li>
   *   <li>unknown <code>predicateName</code> is treated as some Class name and used for 
   *   constructing new <code>Predicate</code> object</li>
   * </ul></p>
   * <p>Well known predicates are listed below, their names may be used as <code>predicateName</code>.
   * <table border='0' cellspacing='4' cellpadding='4'>
   *   <tr><th style='background-color: Navy; color: White; font-size: small;'>Predicate Name</th>
   *       <th style='background-color: Navy; color: White; font-size: small;'>Implementing class</th></tr>
   *   <tr style='background-color: #f0f0f0; font-size: small;'><td><code>isNull</code></td>
   *                                         <td>{@link pl.aislib.util.predicates.IsNullPredicate}</td></tr>
   *   <tr style='background-color: #d0d0d0; font-size: small;'><td><code>isNotNull</code></td>
   *                                         <td>{@link pl.aislib.util.predicates.IsNotNullPredicate}</td></tr>
   * </table>
   * </p>
   * <p>Every <code>Shepherd</code> has its own <code>ShepherdsDog</code> associated, helping him to mind the sheeps.
   * Such a <it>Dog</it> object is used by <code>Shepherd</code> for reading values of watched properties
   * for specific <code>sheep</code> object. <br /> <it>Dogs</it> available now are listed below, their breeds
   * may be used as <code>dogsBreed</code> parameter.
   * <table border='0' cellspacing='4' cellpadding='4'>
   *   <tr><th style='background-color: Navy; color: White; font-size: small;'>Breed</th>
   *       <th style='background-color: Navy; color: White; font-size: small;'>Implementing class</th></tr>
   *   <tr style='background-color: #f0f0f0; font-size: small;'><td><code>request-attr</code></td>
   *                                         <td>{@link DogWatchingHttpRequestAttributes}</td></tr>
   *   <tr style='background-color: #d0d0d0; font-size: small;'><td><code>request-param</code></td>
   *                                         <td>{@link DogWatchingHttpRequestParameters}</td></tr>
   *   <tr style='background-color: #f0f0f0; font-size: small;'><td><code>session-attr</code></td>
   *                                         <td>{@link DogWatchingHttpSessionAttributes}</td></tr>
   * </table>
   * </p>
   * @param propertyName name of property which will be watched by Shepherd and its Dog
   * @param predicateName well known <code>Predicate</code> name or <code>Predicate</code> class name (defines how
   *        properties will be checked)
   * @param dogsBreed breed of the dog which will help Shepherd (defines what kind of properties will be watched)
   * @param pageRef action key for {@link pl.aislib.fm.Page} which will be used as target for all operations controlled
   *                by <code>Shepherd</code> when <code>Predicate</code> will be matched
   * @return trained Shepherd or <code>null</code>
   * @see Predicate
   * @see Shepherd
   * @see ShepherdsDog
   */
  public static Shepherd newShepherd(String propertyName, String pageRef,
      String predicateName, String dogsBreed, Log log) throws Exception {
    Predicate       predicate          = null;
    ShepherdsSchool school             = new ShepherdsSchool();

    ShepherdsDog    shepherdsDog       = school.newShepherdsDog(dogsBreed);
    String          predicateClassName = school.getPredicateClassName(predicateName);

    Class clazz = Loader.findClass(predicateClassName); 
    Constructor constructor = clazz.getConstructor(new Class[0]);
    predicate = (Predicate) constructor.newInstance(new Object[0]);
    shepherdsDog.setWatchedProperty(propertyName);
    shepherdsDog.setLog(log);
    return new Shepherd(predicate, shepherdsDog, pageRef, log);
  }

  /**
   * @deprecated, use {@link ShepherdsSchool#egzamine(List, Pasture, Object, String)
   */
  public static String egzamine(List shepherds, Pasture pasture, Object sheep, String defaultResult) {
    return examine(shepherds, pasture, sheep, defaultResult);
  }

  public static String examine(List shepherds, Pasture pasture, Object sheep, String defaultResult) {
    for (Iterator it = shepherds.iterator(); it.hasNext();) {
      Shepherd shepherd = (Shepherd) it.next();
      String result = shepherd.mindTheSheep(pasture, sheep);
      if (result != null) {
        return result;
      }
    }
    return defaultResult;
  }


  /**
   *
   */
  private ShepherdsDog newShepherdsDog(String dogsBreed) {
    ShepherdsDog result = null;
    if (dogsBreed != null) {
      result = (ShepherdsDog) knownDogBreeds.get(dogsBreed);
    }
    if (result == null) {
      result = new DogWatchingHttpRequestParameters();
    } 
    return result;
  }

  /**
   *
   */
  private String getPredicateClassName(String predicateName) {
    String result = (String) knownShepherds.get(predicateName);
    if (result == null) {
      result = predicateName;
    }
    if (result == null) {
      result = DEFAULT_PREDICATE_CLASS;
    }
    return result;
  }

} // class
