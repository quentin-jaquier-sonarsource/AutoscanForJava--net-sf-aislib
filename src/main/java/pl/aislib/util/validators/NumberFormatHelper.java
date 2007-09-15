package pl.aislib.util.validators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 */
public class NumberFormatHelper {

  protected static final Log LOG = LogFactory.getLog("pl.aislib.util.validators");

  /**
   * Calls {@link DecimalFormat#applyLocalizedPattern(String)}.
   *
   * @param format
   * @param value
   */
  public static void applyLocalizedPattern(Format format, String value) {
    try {
      Method method = format.getClass().getDeclaredMethod("applyLocalizedPattern", new Class[] { String.class });
      method.invoke(format, new Object[] { value });
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", e);
      }
    }
  }

  /**
   * Calls {@link DecimalFormat#toLocalizedPattern()}.
   *
   * @param format
   * @return
   */
  public static String toLocalizedPattern(Format format) {
    try {
      Method method = format.getClass().getDeclaredMethod("toLocalizedPattern", new Class[] {});
      return (String) method.invoke(format, new Object[] {});
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", e);
      }
    }
    return null;
  }

  /**
   * Calls {@link DecimalFormatSymbols#getGroupingSeparator()}.
   *
   * @param format
   * @return
   */
  public static char getGroupingSeparator(Format format) {
    try {
      Method method1 = format.getClass().getDeclaredMethod("getDecimalFormatSymbols", new Class[] {});
      Object decimalFormatSymbols = method1.invoke(format, new Object[] {});
      if (decimalFormatSymbols != null) {
        Method method2 = decimalFormatSymbols.getClass().getDeclaredMethod("getGroupingSeparator", new Class[] {});
        Character charResult = (Character) method2.invoke(decimalFormatSymbols, new Object[] {});
        return charResult.charValue();
      }
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", e);
      }
    }
    return ' ';
  }

  /**
   * Calls {@link DecimalFormatSymbols#setGroupingSeparator(char)}.
   *
   * @param format
   * @param separator
   * @return previous separator
   */
  public static char setGroupingSeparator(Format format, char separator) {
    try {
      Method method1 = format.getClass().getDeclaredMethod("getDecimalFormatSymbols", new Class[] {});
      Object decimalFormatSymbols = method1.invoke(format, new Object[] {});

      Method method2 = decimalFormatSymbols.getClass().getDeclaredMethod("getGroupingSeparator", new Class[] {});
      Character result = (Character) method2.invoke(decimalFormatSymbols, new Object[] {});

      Method method3 = decimalFormatSymbols.getClass().getDeclaredMethod("setGroupingSeparator",
          new Class[] { char.class });
      method3.invoke(decimalFormatSymbols, new Object[] { new Character(separator) });

      Method method4 = format.getClass().getDeclaredMethod("setDecimalFormatSymbols",
          new Class[] { decimalFormatSymbols.getClass() });
      method4.invoke(format, new Object[] { decimalFormatSymbols });

      return result.charValue();
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", e);
      }
    }
    return ' ';
  }

  /**
   * Calls {@link NumberFormat#parse(String, ParsePosition)}.
   *
   * @param format
   * @param str
   * @param pos
   * @return
   * @throws ParseException
   */
  public static Number parse(Format format, String str, ParsePosition pos) throws ParseException {
    try {
      Method m = format.getClass().getDeclaredMethod("parse", new Class[] { String.class, ParsePosition.class });
      return (Number) m.invoke(format, new Object[] { str, pos });
    } catch (IllegalAccessException iae) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", iae);
      }
    } catch (NoSuchMethodException nsme) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", nsme);
      }
    } catch (InvocationTargetException ite) {
      if (ite.getTargetException() != null && ite.getTargetException() instanceof ParseException) {
        throw (ParseException) ite.getTargetException();
      } else {
        if (LOG.isWarnEnabled()) {
          LOG.warn("exeption caught", ite);
        }
      }
    }
    return null;
  }

  /**
   * Calls {@link NumberFormat#setParseIntegerOnly(boolean)}.
   *
   * @param format
   * @param value
   */
  public static void setParseIntegerOnly(Format format, boolean value) {
    try {
      Method m = format.getClass().getMethod("setParseIntegerOnly", new Class[] { boolean.class });
      m.invoke(format, new Object[] { new Boolean(value) });
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", e);
      }
    }
  }

  /**
   * Calls {@link DecimalFormat#setMaximumFractionDigits(int)}.
   *
   * @param format
   * @param value
   */
  public static void setMaximumFractionDigits(Format format, int value) {
    try {
      Method m = format.getClass().getDeclaredMethod("setMaximumFractionDigits", new Class[] { int.class });
      m.invoke(format, new Object[] { new Integer(value) });
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("exeption caught", e);
      }
    }
  }

  /**
   * Creates new instance of <code>NumberFormat</code>. If
   * <code>useICU4J</code> is <code>true</code>,
   * <code>com.ibm.icu.text.DecimalFormat</code> class is being instantied.
   * Otherwise, standard <code>java.text.DecimalFormat</code> is used.
   *
   * @param useICU4J
   * @param pattern
   * @param locale
   * @return
   */
  public static Format createInstance(boolean useICU4J, String pattern, Locale locale) {
    if (useICU4J) {
      try {
        Class dfsclass = Class.forName("com.ibm.icu.text.DecimalFormatSymbols");
        Constructor cnscnstr = dfsclass.getConstructor(new Class[] { Locale.class });
        Object dfs = cnscnstr.newInstance(new Object[] { locale });

        Class dfclass = Class.forName("com.ibm.icu.text.DecimalFormat");
        Constructor dfcsnstr = dfclass.getConstructor(new Class[] { String.class, dfsclass });
        Object result = dfcsnstr.newInstance(new Object[] { pattern, dfs });
        return (Format) result;
      } catch (Exception e) {
        LOG.error("Cannot instantiate ICU4J classes, use default implementation", e);
      }
    }
    DecimalFormat result = new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
    return result;
  }

  public static BigDecimal convert(boolean useICU4J, Number number) {
    if (number instanceof BigDecimal) {
      return (BigDecimal) number;
    }
    if (number instanceof Double) {
      BigDecimal result = new BigDecimal(number.doubleValue());
      result = result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      return result;
    }
    if (number instanceof Long) {
      return new BigDecimal(number.longValue());
    }
    try {
      Class nClass = number.getClass();
      Class[] args = new Class[0];
      Method unscaledValM = nClass.getMethod("unscaledValue", args);
      BigInteger unscaledVal = (BigInteger) unscaledValM.invoke(number, args);
      Method scaleM = nClass.getMethod("scale", args);
      Integer scale = (Integer) scaleM.invoke(number, args);
      BigDecimal result = new BigDecimal(unscaledVal, scale.intValue());
      return result;
    } catch (Exception e) {
      ;
    }
    return null;
  }

}
