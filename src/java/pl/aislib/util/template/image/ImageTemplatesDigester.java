package pl.aislib.util.template.image;

import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * XML Digester for parsing and creating ImageTemplatesBean objects.
 *
 * @author Daniel Rychcik, AIS.PL
 * @version $Revision: 1.2 $
 */
public class ImageTemplatesDigester extends Digester {

  protected boolean configured = false;

  /**
   * Parse the XML stream and retur ImageTemplatesBean object if
   * everything is OK
   *
   * @param in XML InputStream
   * @return ImageTemplatesBean
   * @throws IOException if something is wrong with the file (i.e. there is no file :)
   * @throws SAXException if something is wrong with the XML (i.e. DTD incompatibility)
   */
  public Object parse(InputStream in) throws IOException, SAXException {

    configure();
    return super.parse(in);
  }

  protected void configure() {

    if (configured) {
      return;
    }

    addObjectCreate("image-templates", "pl.aislib.util.template.image.ImageTemplatesBean");

    addObjectCreate("image-templates/font", "pl.aislib.util.template.TemplateFont");
    addSetProperties("image-templates/font", "font-name", "fontName");
    addSetProperties("image-templates/font", "name", "name");
    addSetProperties("image-templates/font", "name", "name");
    addSetProperties("image-templates/font", "size", "size");
    addSetProperties("image-templates/font", "style", "style");
    addSetProperties("image-templates/font", "weight", "weight");
    addSetProperties("image-templates/font", "red", "red");
    addSetProperties("image-templates/font", "green", "green");
    addSetProperties("image-templates/font", "blue", "blue");
    addSetNext(      "image-templates/font", "addFont", "pl.aislib.util.template.TemplateFont");

    addObjectCreate( "image-templates/image-template", "pl.aislib.util.template.image.ImageTemplate");
    addSetProperties("image-templates/image-template", "name", "name");
    addSetNext(      "image-templates/image-template", "addTemplate", "pl.aislib.util.template.image.ImageTemplate");

    addObjectCreate( "image-templates/image-template/field", "pl.aislib.util.template.Field");
    addSetProperties("image-templates/image-template/field", "name", "name");
    addSetProperties("image-templates/image-template/field", "position-x", "positionX");
    addSetProperties("image-templates/image-template/field", "position-y", "positionY");
    addSetProperties("image-templates/image-template/field", "align-x", "alignX");
    addSetProperties("image-templates/image-template/field", "align-y", "alignY");
    addSetProperties("image-templates/image-template/field", "page-number", "pageNumber");
    addSetProperties("image-templates/image-template/field", "font", "fontName");
    addSetNext(      "image-templates/image-template/field", "addField", "pl.aislib.util.template.Field");

    configured = true;
  }
}
