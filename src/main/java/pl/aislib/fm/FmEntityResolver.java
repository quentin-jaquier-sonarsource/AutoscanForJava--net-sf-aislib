package pl.aislib.fm;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.xml.sax.EntityResolver;

import pl.aislib.util.xml.JarEntityResolver;

/**
 * Provides {@link EntityResolver} containing DTDs for all
 * supported by aislib configuration formats.
 *
 * @author Tomasz Pik, AIS.PL
 */
public class FmEntityResolver {

  /**
   * Path to all framework DTD files inside of Jar archive.
   * Don't use leading '/'. Use '/' instead of '.' in package name.
   * Thanks for your cooperation :)
   */
  private static final String DTD_JAR_PREFIX = "pl/aislib/fm/dtds";

  private static Map publicIdMap = buildPublicIdMap();
  private static Map systemIdMap = buildSystemIdMap();

  /**
   * Instance of {@link EntityResolver} provides DTDs for all
   * supported aislib configuration formats.
   *
   * @return instance of {@link EntityResolver}.
   */
  public static EntityResolver getResolverInstance() {
    return new JarEntityResolver(DTD_JAR_PREFIX, publicIdMap, systemIdMap);
  }

  /**
   * Instance of {@link EntityResolver} provides DTDs for all
   * supported aislib configuration formats and with given <code>log</code>
   * as callback listener.
   *
   * @param log logging entity resolving.
   * @return instance of {@link EntityResolver}.
   */
  public static EntityResolver getResolverInstance(Log log) {
    return new JarEntityResolver(DTD_JAR_PREFIX, publicIdMap, systemIdMap, log);
  }

  private static Map buildPublicIdMap() {
    Map registeredIds  = new HashMap(4);
    registeredIds.put("-//AIS.PL//DTD Forms Description 0.2//EN",    "forms_0_2.dtd");
    registeredIds.put("-//AIS.PL//DTD Forms Description 0.4//EN",    "forms_0_4.dtd");
    registeredIds.put("-//AIS.PL//DTD Messages Description 0.2//EN", "messages_0_2.dtd");
    registeredIds.put("-//AIS.PL//DTD Workflow Description 0.2//EN", "workflow_0_2.dtd");
    return registeredIds;
  }

  private static Map buildSystemIdMap() {
    Map registeredIds  = new HashMap(4);
    registeredIds.put("http://www.ais.pl/dtds/forms_0_2.dtd",    "forms_0_2.dtd");
    registeredIds.put("http://www.ais.pl/dtds/forms_0_4.dtd",    "forms_0_4.dtd");
    registeredIds.put("http://www.ais.pl/dtds/messages_0_2.dtd", "messages_0_2.dtd");
    registeredIds.put("http://www.ais.pl/dtds/workflow_0_2.dtd", "workflow_0_2.dtd");
    return registeredIds;
  }

}
