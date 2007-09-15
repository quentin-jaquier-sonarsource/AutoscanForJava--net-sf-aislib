package pl.aislib.util.template.image;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.io.InputStream;
import java.io.IOException;

import org.xml.sax.SAXException;

import pl.aislib.util.template.Field;
import pl.aislib.util.template.TemplateFont;

/**
 * Master object for image templates representation.
 * It acts like List of Templates and TemplateFonts
 * (as in the DTD) with added functionality of
 * loading itself from XML InputStream.
 *
 * @author Daniel Rychcik, AIS.PL
 * @version $Revision: 1.2 $
 */
public class ImageTemplatesBean {

  protected Map templateFonts;
  protected Map templates;

  /**
   * Empty contstructor. Creates empty templateFonts and templates
   * Lists.
   */
  public ImageTemplatesBean() {
    templateFonts = new HashMap();
    templates = new HashMap();
  }

  public Map getTemplateFonts() {
    return templateFonts;
  }

  public Map getTemplates() {
    return templates;
  }

  public TemplateFont getTemplateFont(String name) {
    return (TemplateFont) templateFonts.get(name);
  }

  public ImageTemplate getTemplate(String name) {
    return (ImageTemplate) templates.get(name);
  }

  /**
   * Initialize this object from XML InputStream
   *
   * @param in
   * @throws IOException if something is wrong with the file (i.e. there is no file :)
   * @throws SAXException if something is wrong with the XML (i.e. DTD incompatibility)
   */
  public void load(InputStream in) throws IOException, SAXException {

    ImageTemplatesDigester digester = new ImageTemplatesDigester();
    ImageTemplatesBean result = (ImageTemplatesBean) digester.parse(in);
    templates = result.getTemplates();
    templateFonts = result.getTemplateFonts();
    updateTemplateFonts();
  }

  /**
   * Update templateFont objects in all the fields in all the templates.
   * Called internally after XML processing.
   */
  protected void updateTemplateFonts() {

    for (Iterator iter = templates.values().iterator(); iter.hasNext(); ) {
      ImageTemplate imageTemplate = (ImageTemplate)iter.next();
      for (Iterator fieldIter = imageTemplate.getFields().values().iterator(); fieldIter.hasNext(); ) {
        Field field = (Field) fieldIter.next();
        if (field.getFontName() != null) {
          field.setTemplateFont(getTemplateFont(field.getFontName()));
        }
      }
    }
  }

  /**
   * Add templateFont to the font List. Called internally
   * by the Digester.
   *
   * @param templateFont
   */
  public void addFont(TemplateFont templateFont) {

    if (templateFonts == null) {
      templateFonts = new HashMap();
    }
    templateFonts.put(templateFont.getName(), templateFont);
  }

  /**
   * Add template to the templates List. Called internally
   * by the Digester.
   *
   * @param template
   */
  public void addTemplate(ImageTemplate template) {

    if (templates == null) {
      templates = new HashMap();
    }
    templates.put(template.getName(), template);
  }



  public String toString() {
    StringBuffer sb = new StringBuffer("IMAGE_TEMPLATES: (");
    for (Iterator iter = templates.values().iterator(); iter.hasNext(); ) {
      ImageTemplate template = (ImageTemplate) iter.next();
      sb.append(template.toString());
      if (iter.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append(")");

    return sb.toString();
  }
}
