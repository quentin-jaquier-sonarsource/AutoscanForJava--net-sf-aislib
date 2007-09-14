package pl.aislib.fm.xml;

import pl.aislib.fm.Application;
import pl.aislib.fm.TemplateEngine;
import pl.aislib.fm.TemplateEngineException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

import org.apache.commons.logging.Log;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;
import org.jdom.transform.JDOMResult;

/**
 * Template Engine class transforming XML input using XSL.
 *
 * @deprecated this class will be reimplemented in future versions of AISLIB.
 * @author Michal Jastak
 * @version $Revision: 1.3 $
 * @since AISLIB 0.1
 */
public class XSLTemplateEngine extends TemplateEngine {

  /**
   * Jakarta Commons Logging Component used to store information about related to this Template.
   */
  protected Log     log;

  /**
   * URIResolver used to turn a URI used in document(), xsl:import, or xsl:include into a {@link Source} object.
   */
  protected URIResolver resolver;

  /**
   * Describes if we should omit XML declaration in result produced during XSL transformation.
   * Defaults to <code>false</code>.
   */
  protected boolean omitDeclaration;

  /**
   * Describes if we should omit encoding declaration in result produced during XSL transformation.
   * Defaults to <code>false</code>.
   */
  protected boolean omitEncoding;

  /**
   * Describes result document encoding. Defaults to <code>iso-8859-1</code>.
   */
  protected String  encoding;

  /**
   * Transformer Factory used for preparing XSL Templates.
   */
  protected TransformerFactory transformerFactory;

  /**
   *
   */
  public XSLTemplateEngine (URIResolver resolver) {
    this.resolver   = resolver;
    encoding        = "iso-8859-1";
    omitDeclaration = false;
    omitEncoding    = false;
    transformerFactory = TransformerFactory.newInstance ();
    if (resolver != null) {
      transformerFactory.setURIResolver (resolver);
    }
  }

  /**
   *
   */
  public void setEncoding (String encoding) {
    this.encoding = encoding;
  }

  /**
   *
   */
  public String getEncoding () {
    return encoding;
  }

  /**
   *
   */
  public void setOmitDeclaration (boolean omitDeclaration) {
    this.omitDeclaration = omitDeclaration;
  }

  /**
   *
   */
  public boolean getOmitDeclaration () {
    return omitDeclaration;
  }

  /**
   *
   */
  public void setOmitEncoding (boolean omitEncoding) {
    this.omitEncoding = omitEncoding;
  }

  /**
   *
   */
  public boolean getOmitEncoding () {
    return omitEncoding;
  }

  /**
   * Evaluates template using given <code>parameters</code>.
   * <p>Evaluation mentioned above means:
   * <ul>
   *   <li>get XML given as {@link org.jdom.Document} in <font style="color: navy">jdom.document</font>
   *       <code>parameters</code> element,</li>
   *   <li>get precompiled XSL stylesheet contained by <code>template</code> parameter or XSL stylesheet
   *       associated with XML file by processing instruction <code>xml-stylesheet</code>,
   *       if <code>template</code> parameter is null.
   *   <li>transform XML using XSL style and return result formatted using {@link XMLOutputter}</li>
   * </ul></p>
   * @param template {@link Templates} object containing precompiled XSL stylesheet or null
   * @param parameters evaluation parameters
   * @return Trasformed XML document written as String
   */
  public String evaluate(Object template, Map parameters) throws TemplateEngineException {

    Templates templates = null;
    try {
      templates = (Templates) template;
    } catch (ClassCastException ccex) {
      if (log != null) {
        log.error ("[XSLT]:", ccex);
      }
      throw new TemplateEngineException (ccex);
    }
    Document doc = (Document) parameters.get ("jdom.document");

    if (doc == null) {
      throw new TemplateEngineException("JDOM Document cannot be null, yet");
    }

    JDOMSource source = new JDOMSource (doc);
    JDOMResult result = new JDOMResult ();

    Transformer transformer = null;
    Source style = null;

    if (templates == null) {
      try {
        style = transformerFactory.getAssociatedStylesheet (source, null, null, null);
        if (style != null) {
          templates = transformerFactory.newTemplates (style);
        }
      } catch (TransformerConfigurationException tcex) {
        if (log != null) {
          log.error ("[XSLT]: ", tcex);
        }
        throw new TemplateEngineException(tcex);
      }
    }

    try {
      if (templates != null) {
        transformer = templates.newTransformer ();
      }
    } catch (TransformerConfigurationException tcex) {
      if (log != null) {
        log.error ("[XSLT]: ", tcex);
      }
      throw new TemplateEngineException (tcex);
    }

    if (transformer != null) {
      try {
        transformer.transform (source, result);
      } catch (TransformerException tex) {
        if (log != null) {
          log.error ("[XSLT]: ", tex);
        }
        throw new TemplateEngineException (tex);
      }
    }

    XMLOutputter xo = new XMLOutputter ("  ", true);
    xo.setOmitDeclaration (omitDeclaration);
    xo.setOmitEncoding (omitEncoding);
    if (!omitEncoding) {
      xo.setEncoding (encoding);
    }
    String xoDoc = xo.outputString (result.getDocument ());
    return xoDoc;
  }

  /**
   *
   */
  public boolean isTemplate(Object object) {
    Templates templates = null;
    try {
      templates = (Templates) object;
      return true;
    } catch (ClassCastException ccex) {
      if (log != null) {
        log.error ("[XSLT]:", ccex);
      }
    }
    return false;
  }

  /**
   *
   */
  public Object load(Application application, HttpServletRequest request, HttpServletResponse response,
                    String templateName) throws TemplateEngineException {

    Templates templates = null;
    try {
      Source style = resolver.resolve (templateName, null);
      templates = transformerFactory.newTemplates (style);
    } catch (TransformerConfigurationException tcex) {
      if (log != null) {
        log.error ("[XSLT]: ", tcex);
      }
      throw new TemplateEngineException (tcex);
    } catch (TransformerException tex) {
      if (log != null) {
        log.error ("[XSLT]: ", tex);
      }
      throw new TemplateEngineException (tex);
    }

    if (templates == null) {
      throw new TemplateEngineException ("Cannot prepare template for '" + templateName + "'");
    }

    return templates;
  }

} // class
