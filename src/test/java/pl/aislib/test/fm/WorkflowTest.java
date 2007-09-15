package pl.aislib.test.fm;

import java.io.InputStream;

import org.apache.commons.logging.impl.SimpleLog;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import pl.aislib.fm.FmEntityResolver;
import pl.aislib.fm.Workflow;
import pl.aislib.util.xml.XMLUtils;

import junit.framework.TestCase;

/**
 * Class for testing issues regarding file configuration parsing and loading.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 */
public class WorkflowTest extends TestCase {

  public void testTwoPageKeys() throws Exception {
    final String xmlFileName = "twopages.xml";
    InputStream stream = this.getClass().getResourceAsStream(xmlFileName);
    assertNotNull("config file: " + xmlFileName + " is null", stream);

    InputSource source = new InputSource(stream);
    source.setSystemId(this.getClass().getResource(xmlFileName).toString());

    XMLReader xmlReader = XMLUtils.newXMLReader(true, true);
    Workflow workflow = new Workflow(new SimpleLog(xmlFileName));
    xmlReader.setContentHandler(workflow);
    xmlReader.setEntityResolver(FmEntityResolver.getResolverInstance());
    try {
      xmlReader.parse(source);
      fail("two pages with the same key cannot be registered");
    } catch (Exception e) {
      ; // it's OK in this test
    }
  }

}
