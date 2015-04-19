package pl.aislib.fm;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import pl.aislib.fm.forms.BaseValidator;
import pl.aislib.fm.forms.Field;
import pl.aislib.fm.forms.config.Handler;
import pl.aislib.fm.forms.config.IField;
import pl.aislib.lang.Loader;
import pl.aislib.util.validators.AbstractValidator;
import pl.aislib.util.validators.StringValidator;

/**
 * XML handler for handling framework's fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FieldHandler extends pl.aislib.fm.forms.config.FieldHandler {

  /**
   * True if field is required.
   */
  protected boolean allRequired;


  // Constructors

  /**
   * @see pl.aislib.fm.forms.config.PartialHandler#PartialHandler(Handler)
   */
  public FieldHandler(Handler parentHandler) {
    super(parentHandler);
  }


  // Public methods

  /**
   * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
   */
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if ("field".equals(localName)) {
      allRequired = true;
    } else if (
      currentBuilder == null
        && "property".equals(localName)
        && "required".equals(atts.getValue("name"))
        && "false".equals(atts.getValue("value"))) {
      allRequired = false;
    }
    super.startElement(namespaceURI, localName, qName, atts);
  }


  // Protected methods

  /**
   * @see pl.aislib.fm.forms.config.FieldHandler#addField(IField)
   */
  protected void addField(IField field) throws SAXException {
    Field formField = (Field) field;

    if (currentBuilder != null) {
      formField.addType(Field.FT_COMPLEX);
      formField.setBuilder(currentBuilder);
      formField.setBuilderMapping(currentMapping);

      populate(currentBuilder, currentBuilderProperties);
    }

    if (allRequired) {
      addDefaultValidator(field);
    }

    ((FormsHandler) parentHandler).currentForm.addField(formField);
  }

  /**
   * @see pl.aislib.fm.forms.config.FieldHandler#createNewField(String)
   */
  protected IField createNewField(String fieldName) {
    return new Field(fieldName);
  }

  /**
   * @see pl.aislib.fm.forms.config.FieldHandler#processFieldBehavior(IField, Attributes)
   */
  protected void processFieldBehavior(IField field, Attributes atts) throws SAXException {
    Field formField = (Field) field;

    boolean bDynamic = atts.getValue("dynamic") != null && atts.getValue("dynamic").equals("true");

    int fieldType = Field.FT_NORMAL;
    if (bDynamic) {
      fieldType |= Field.FT_DYNAMIC;
    }

    formField.setType(fieldType);
  }

  /**
   * @see pl.aislib.fm.forms.config.FieldHandler#addValidator(BaseValidator, Map)
   */
  protected void addValidator(BaseValidator validator, Map atts) throws SAXException {
    Field formField = (Field) currentField;

    int msgCode = 0;
    try {
      msgCode = Integer.parseInt((String) atts.get("msg-code"));
    } catch (NumberFormatException nfe) {
      throw new SAXException(nfe.getMessage());
    }

    formField.addValidator(validator, msgCode);
  }

  /**
   * Adds validator in case when no validators are strictly given in a form definition.
   *
   * @param field <code>IField</code> object.
   */
  protected void addDefaultValidator(IField field) throws SAXException {
    Field formField = (Field) field;
    BaseValidator validator = null;
    try {
      if (currentValidator != null) {
        validator = (BaseValidator) currentValidator.getClass().newInstance();
      } else {
        throw new SAXException("No validators found. Add default validators with property 'required' set to 'false'.");
      }
    } catch (SAXException saxe) {
      throw saxe;
    } catch (Exception e) {
      throw new SAXException(e.getMessage(), e);
    }
    populate(validator, currentProperties);
    ((AbstractValidator) validator).setRequired(false);
    formField.addValidator(validator, ((Integer) formField.getValidators().get(currentValidator)).intValue());
  }

} // FieldHandler class
