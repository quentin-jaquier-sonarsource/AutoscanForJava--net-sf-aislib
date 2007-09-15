package pl.aislib.util.template.image;

import java.awt.Graphics;

import pl.aislib.util.template.Field;

/**
 * Interface for rendering field values.
 *
 * @author Tomasz Pik, AIS.PL
 * @since 0.3
 */
public interface FieldRenderer {

  /**
   * Render value of template.
   * 
   * Render <code>value</code> within <code>field</code> on
   * given <code>graphics</code>.
   * 
   * @param graphics on which value should be render.
   * @param field definition of template field.
   * @param value value for given field.
   */
  public void render(Graphics graphics, Field field, Object value);
}
