package pl.aislib.jakarta.velocity;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.OutputStreamWriter;

import java.util.Map;
import java.util.Iterator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import pl.aislib.util.template.StringTemplate;

public class VelocityTemplate implements StringTemplate {

  private Template        template;
  private VelocityContext context;

  VelocityTemplate(Template _template) {
    template = _template;
    context = new VelocityContext();
  }

  public void setValues(Map map) {
    if (map != null) {
      Iterator keys = map.keySet().iterator();
      while (keys.hasNext()) {
        String key = keys.next().toString();
        context.put(key, map.get(key));
      }
    }
  }

  public void setValue(String key, Object value) {
    context.put(key, value);
  }

  public void writeTo(Writer writer) throws IOException {
    try {
      template.merge(context, writer);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    } 
  }

  public void writeTo(OutputStream stream) throws IOException {
    Writer writer = new OutputStreamWriter(stream);
    writeTo(writer);
  }

  public byte[] toByteArray() {
    return toString().getBytes();
  }

  public String toString() {
    StringWriter writer = new StringWriter();
    try {
      writeTo(writer);
      return writer.toString();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe.getMessage());
    }
  }
}
