package pl.aislib.fm;

/**
 *
 * @author Pawel Chmielewski, AIS.PL
 * @version $Revision: 1.1 $
 */
public interface ObjectContainer {
  
   public Object getObject(String name, Class clazz) throws Exception;
  
}
