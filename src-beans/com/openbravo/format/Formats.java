//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.format;

import java.text.*;
import java.util.Date;
import com.openbravo.basic.BasicException;

public abstract class Formats {
    
    public final static Formats NULL = new FormatsNULL();
    public final static Formats INT = new FormatsINT();
    public final static Formats STRING = new FormatsSTRING();
    public final static Formats DOUBLE = new FormatsDOUBLE();
    public final static Formats CURRENCY = new FormatsCURRENCY();
    public final static Formats PERCENT = new FormatsPERCENT();
    public final static Formats BOOLEAN = new FormatsBOOLEAN();
    public final static Formats TIMESTAMP = new FormatsTIMESTAMP();
    public final static Formats TIMESTAMPPos = new FormatsTIMESTAMPPos();
    public final static Formats TIMESTAMPRec = new FormatsTIMESTAMPPosRec();
    public final static Formats DATE = new FormatsDATE();
    public final static Formats DATEPos = new FormatsDATEPos();
    public final static Formats DATERec = new FormatsDATEPosRec();
    public final static Formats TIME = new FormatsTIME();
    public final static Formats BYTEA = new FormatsBYTEA();
    
    private static NumberFormat m_integerformat = NumberFormat.getIntegerInstance();
    private static NumberFormat m_doubleformat = NumberFormat.getNumberInstance();
    private static NumberFormat m_currencyformat = NumberFormat.getCurrencyInstance();
    private static NumberFormat m_percentformat = new DecimalFormat("#,##0.##%");
    
    private static DateFormat m_dateformat = DateFormat.getDateInstance();
    private static DateFormat m_dateformatPos = DateFormat.getDateInstance();
    private static DateFormat m_dateformatPosRec = DateFormat.getDateInstance();
    private static DateFormat m_timeformat = DateFormat.getTimeInstance();
    private static DateFormat m_datetimeformat = DateFormat.getDateTimeInstance();
    private static DateFormat m_datetimeformatPos = DateFormat.getDateTimeInstance();
    private static DateFormat m_datetimeformatPosRec = DateFormat.getDateTimeInstance();
   
    
    /** Creates a new instance of Formats */
    protected Formats() {
    }
    
    public static int getCurrencyDecimals() {

        return m_currencyformat.getMaximumFractionDigits();
    }
    
    public String formatValue(Object value) {
        if (value == null) {
            return "";
        } else {
            return formatValueInt(value);
        }
    }
    
    public Object parseValue(String value, Object defvalue) throws BasicException {
        if (value == null || "".equals(value)) {
            return defvalue;
        } else {
            try {
                return parseValueInt(value);
            } catch (ParseException e) {
                throw new BasicException(e.getMessage(), e);
            }
        }  
    }
    
    public Object parseValue(String value) throws BasicException {
        return parseValue(value, null);
    }

    public static void setIntegerPattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_integerformat = NumberFormat.getIntegerInstance();
        } else {
            m_integerformat = new DecimalFormat(pattern);
        }
    }

    public static void setDoublePattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_doubleformat = NumberFormat.getNumberInstance();
        } else {
            m_doubleformat = new DecimalFormat(pattern);
        }
    }

    public static void setCurrencyPattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_currencyformat = NumberFormat.getCurrencyInstance();
        } else {
            m_currencyformat = new DecimalFormat(pattern);
        }
    }    

    public static void setPercentPattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_percentformat = new DecimalFormat("#,##0.##%");
        } else {
            m_percentformat = new DecimalFormat(pattern);
        }
    }   
    
    public static void setDatePattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_dateformat = DateFormat.getDateInstance();
        } else {
            m_dateformat = new SimpleDateFormat(pattern);
        }
    }
    
    public static void setTimePattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_timeformat = DateFormat.getTimeInstance();
        } else {
            m_timeformat = new SimpleDateFormat(pattern);
        }
    }
    
    public static void setDateTimePattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_datetimeformat = DateFormat.getDateTimeInstance();
        } else {
            m_datetimeformat = new SimpleDateFormat(pattern);
        }
    }
    
    public static void setDateTimePatternProdeco() {
    	String pattern = "dd-MM-yyyy";
        if (pattern == null || pattern.equals("")) {
            m_datetimeformatPos = DateFormat.getDateTimeInstance();
        } else {
            m_datetimeformatPos = new SimpleDateFormat(pattern);
        }
    }
    
    public static void setDateTimePatternRecarga() {
    	String pattern = "dd/MM/yyyy";
        if (pattern == null || pattern.equals("")) {
            m_datetimeformatPosRec = DateFormat.getDateTimeInstance();
        } else {
            m_datetimeformatPosRec = new SimpleDateFormat(pattern);
        }
    }
    
    protected abstract String formatValueInt(Object value);
    protected abstract Object parseValueInt(String value) throws ParseException;
    public abstract int getAlignment();

    private static final class FormatsNULL extends Formats {       
        protected String formatValueInt(Object value) {
            return null;
        }       
        protected Object parseValueInt(String value) throws ParseException {
            return null;
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }
    }
    private static final class FormatsINT extends Formats {       
        protected String formatValueInt(Object value) {
            return m_integerformat.format(((Number) value).longValue());
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return new Integer(m_integerformat.parse(value).intValue());
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.RIGHT;
        }
    }
    private static final class FormatsSTRING extends Formats {       
        protected String formatValueInt(Object value) {
            return (String) value;
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return value;
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }
    }    
    private static final class FormatsDOUBLE extends Formats {       
        protected String formatValueInt(Object value) {
            return m_doubleformat.format(((Number) value).doubleValue());
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return new Double(m_doubleformat.parse(value).doubleValue());
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.RIGHT;
        }
    }    
    private static final class FormatsPERCENT extends Formats {       
        protected String formatValueInt(Object value) {
            //return NumberFormat.getPercentInstance().format(((Number) value).doubleValue());
            return m_percentformat.format(((Number) value).doubleValue());
        }   
        protected Object parseValueInt(String value) throws ParseException {
            try {
                //return new Double(NumberFormat.getPercentInstance().parse(value).doubleValue());
                return new Double(m_percentformat.parse(value).doubleValue());
            } catch (ParseException e) {
                // Segunda oportunidad como numero normalito
                return new Double(m_doubleformat.parse(value).doubleValue() / 100);
            }
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.RIGHT;
        }
    }  
    private static final class FormatsCURRENCY extends Formats {       
        protected String formatValueInt(Object value) {
            return m_currencyformat.format(((Number) value).doubleValue());
        }   
        protected Object parseValueInt(String value) throws ParseException {
            try {
                return new Double(m_currencyformat.parse(value).doubleValue());
            } catch (ParseException e) {
                // Segunda oportunidad como numero normalito
                return new Double(m_doubleformat.parse(value).doubleValue());
            }
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.RIGHT;
        }
    }  
    private static final class FormatsBOOLEAN extends Formats {       
        protected String formatValueInt(Object value) {
            return ((Boolean) value).toString();
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return Boolean.valueOf(value);
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }    
    private static final class FormatsTIMESTAMP extends Formats {       
        protected String formatValueInt(Object value) {
            return m_datetimeformat.format((Date) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            try {
                return m_datetimeformat.parse(value);
            } catch (ParseException e) {
                // segunda oportunidad como fecha normalita
                return m_dateformat.parse(value);
            }
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }
    private static final class FormatsTIMESTAMPPos extends Formats {       
        protected String formatValueInt(Object value) {
            return m_datetimeformatPos.format((Date) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            try {
                return m_datetimeformatPos.parse(value);
            } catch (ParseException e) {
                // segunda oportunidad como fecha normalita
                return m_dateformatPos.parse(value);
            }
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }
    
    private static final class FormatsTIMESTAMPPosRec extends Formats {       
        protected String formatValueInt(Object value) {
            return m_datetimeformatPosRec.format((Date) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            try {
                return m_datetimeformatPosRec.parse(value);
            } catch (ParseException e) {
                // segunda oportunidad como fecha normalita
                return m_dateformatPosRec.parse(value);
            }
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }
    private static final class FormatsDATE extends Formats {       
        protected String formatValueInt(Object value) {
            return m_dateformat.format((Date) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return m_dateformat.parse(value);
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }  
    private static final class FormatsDATEPos extends Formats {       
        protected String formatValueInt(Object value) {
            return m_dateformatPos.format((Date) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return m_dateformatPos.parse(value);
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }  
    private static final class FormatsDATEPosRec extends Formats {  
    	java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
        protected String formatValueInt(Object value) {
            return df.format((Date) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return df.parse(value);
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }  
    private static final class FormatsTIME extends Formats {       
        protected String formatValueInt(Object value) {
            return m_timeformat.format((Date) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return m_timeformat.parse(value);
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }    
    private static final class FormatsBYTEA extends Formats {       
        protected String formatValueInt(Object value) {
            try {
                return new String((byte[]) value, "UTF-8");
            } catch (java.io.UnsupportedEncodingException eu) {
                return "";
            }
        }   
        protected Object parseValueInt(String value) throws ParseException {
            try {
               return value.getBytes("UTF-8");
            } catch (java.io.UnsupportedEncodingException eu) {
               return new byte[0];
            }
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.LEADING;
        }
    }     
}
