package pl.aislib.fm.forms.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import pl.aislib.fm.forms.BaseValidator;
import pl.aislib.fm.forms.FieldBuilder;
import pl.aislib.lang.Loader;
import pl.aislib.lang.ReflectionException;

/**
 * Base field handler class.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public abstract class FieldHandler extends PartialHandler {

  /**
   * name of validation class currently parsed.
   */
  protected String currentClassName;

  /**
   * Field being used.
   */
  protected IField currentField;

  /**
   * Current properties.
   */
  protected Map currentProperties;

  /**
   * Current mapping.
   */
  protected Map currentMapping;

  /**
   * Validator being used.
   */
  protected BaseValidator currentValidator;

  /**
   * Builder being used.
   */
  protected FieldBuilder currentBuilder;

  /**
   * Properties for current builder.
   */
  protected Map currentBuilderProperties;

  /**
   * <code>true</code> if builder is being parsed.
   */
  private boolean insideBuilder;


  // Constructors

  /**
   * @see pl.aislib.fm.forms.config.PartialHandler#PartialHandler(Handler)
   */
  public FieldHandler(Handler parentHandler) {
    super(parentHandler);
  }


  // Public methods

  /**
   * @see org.xml.sax.ContentHandler#endElement(String,String,String)
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if ("field".equals(localName)) {
      addField(currentField);
      currentBuilder = null;
      return;
    }
    if ("validation".equals(localName)) {
      Map props = (Map) currentValidator.applyObject("properties", currentProperties);
      populate(currentValidator, props);
      addValidator(currentValidator, currentAttributes);
      return;
    }
    if ("builder".equals(localName)) {
      insideBuilder = false;
      return;
    }
  }

  /**
   * @see org.xml.sax.ContentHandler#startElement(String,String,String,Attributes)
   */
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if ("field".equals(localName)) {
      currentObject = currentField = processField(atts);
      return;
    }
    if ("validation".equals(localName)) {
      currentObject = currentValidator = processValidator(atts);
      return;
    }
    if ("builder".equals(localName)) {
      currentObject = currentBuilder = processBuilder(atts);
      insideBuilder = true;
      return;
    }
    if ("property".equals(localName)) {
      if (insideBuilder) {
        currentBuilderProperties.put(atts.getValue("name"), atts.getValue("value"));
      } else {
        currentProperties.put(atts.getValue("name"), atts.getValue("value"));
      }
      currentObject = "property";
      return;
    }
    if ("builder-mapping".equals(localName)) {
      currentMapping.put(atts.getValue("field-param"), atts.getValue("field-name"));
      currentObject = "builder-mapping";
      return;
    }
    currentObject = localName;
  }


  // Protected methods

  /**
   * @param atts <code>Attributes</code> object.
   * @return <code>IField</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected IField processField(Attributes atts) throws SAXException {
    currentClassName = atts.getValue("class");
    String fieldName = atts.getValue("name");

    try {
      createObject(currentClassName);
    } catch (SAXException e) {
      throw new SAXException("Class " + currentClassName + " could not be created: " + e.getMessage());
    }

    IField field = createNewField(fieldName);

    processFieldBehavior(field, atts);

    currentProperties = new LinkedHashMap();
    currentMapping = new LinkedHashMap();

    return field;
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return <code>FieldBuilder</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected FieldBuilder processBuilder(Attributes atts) throws SAXException {
    String builderClassName = atts.getValue("class");
    try {
      currentBuilder = (FieldBuilder) Loader.instantiate(builderClassName, FieldBuilder.class);
    } catch (ReflectionException re) {
      throw new SAXException(re.getMessage(), re);
    }
    currentBuilderProperties = new LinkedHashMap();
    currentMapping = new LinkedHashMap();

    return currentBuilder;
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return <code>BaseValidator</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected BaseValidator processValidator(Attributes atts) throws SAXException {
    try {
      currentValidator = (BaseValidator) Loader.instantiate(currentClassName, BaseValidator.class);
    } catch (ReflectionException re) {
      throw new SAXException(re.getMessage(), re);
    }
    currentAttributes = attributesToMap(atts);

    return currentValidator;
  }


  // Abstract methods

  /**
   * @param field <code>IField</code> object.
   */
  protected abstract void addField(IField field) throws SAXException;

  /**
   * @param fieldName name of a field.
   * @return <code>IField</code> object.
   */
  protected abstract IField createNewField(String fieldName);

  /**
   * @param field <code>IField</code> object.
   * @param atts <code>Attributes</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected abstract void processFieldBehavior(IField field, Attributes atts) throws SAXException;

  /**
   * @param validator <code>BaseValidator</code> object.
   * @param atts <code>Map</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected abstract void addValidator(BaseValidator validator, Map atts) throws SAXException;

} // FieldHandler class
