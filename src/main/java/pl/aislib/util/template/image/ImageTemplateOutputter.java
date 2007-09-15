package pl.aislib.util.template.image;

import java.awt.Image;

import java.io.IOException;
import java.io.OutputStream;

public interface ImageTemplateOutputter {

  public void writeImage(Image image, OutputStream stream) throws IOException;
}
