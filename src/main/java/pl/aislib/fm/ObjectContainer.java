package pl.aislib.fm;

/**
 *
 * @author Pawel Chmielewski, AIS.PL
 */
public interface ObjectContainer {

   public Object getObject(String name, Class clazz) throws Exception;

}
