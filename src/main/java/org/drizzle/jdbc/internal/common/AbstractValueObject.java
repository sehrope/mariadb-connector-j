/*
 * Drizzle JDBC
 *
 *  Copyright (C) 2009 Marcus Eriksson (krummas@gmail.com)
 *  All rights reserved.
 *
 *  Use and distribution licensed under the BSD license.
 */

package org.drizzle.jdbc.internal.common;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: marcuse
 * Date: Jun 10, 2009
 * Time: 4:13:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractValueObject implements ValueObject {
    private final byte[] rawBytes;
    protected final DataType dataType;

    protected AbstractValueObject(final byte[] rawBytes,final DataType dataType) {
        this.dataType = dataType;
        this.rawBytes = rawBytes;
    }

    public String getString() {
        if (rawBytes == null) {
            return null;
        }
        return new String(rawBytes);
    }

    public long getLong() {
        if (rawBytes == null) {
            return 0;
        }
        return Long.valueOf(getString());
    }

    public int getInt() {
        if (rawBytes == null) {
            return 0;
        }
        return Integer.valueOf(getString());
    }

    public short getShort() {
        if (rawBytes == null) {
            return 0;
        }
        return Short.valueOf(getString());
    }

    public byte getByte() {
        if (rawBytes == null) {
            return 0;
        }
        return Byte.valueOf(getString());
    }

    public byte[] getBytes() {
        return rawBytes;
    }

    public float getFloat() {
        if (rawBytes == null) {
            return 0;
        }
        return Float.valueOf(getString());
    }

    public double getDouble() {
        if (rawBytes == null) {
            return 0;
        }
        return Double.valueOf(getString());
    }

    public BigDecimal getBigDecimal() {
        if (rawBytes == null) {
            return null;
        }
        return new BigDecimal(getString());
    }

    public Date getDate() throws ParseException {
        if (rawBytes == null) {
            return null;
        }
        final String rawValue = getString();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final java.util.Date utilDate = sdf.parse(rawValue);
        return new Date(utilDate.getTime());
    }

    /**
     * Since drizzle has no TIME datatype, JDBC Time is stored in a packed integer
     *
     * @return the time
     * @throws java.text.ParseException
     * @see Utils#packTime(long)
     * @see Utils#unpackTime(int)
     */
    public Time getTime() {
        if (rawBytes == null) {
            return null;
        }
        final int packedValue = getInt();
        final long timestamp = Utils.unpackTime(packedValue);
        return new Time(timestamp);
    }

    public Timestamp getTimestamp() throws ParseException {
        if (rawBytes == null) {
            return null;
        }
        final String rawValue = getString();
        SimpleDateFormat sdf;
        if(rawValue.length() > 11) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        final java.util.Date utilTime = sdf.parse(rawValue);
        return new Timestamp(utilTime.getTime());
    }

    public InputStream getInputStream() {
        if (rawBytes == null) {
            return null;
        }
        return new ByteArrayInputStream(getString().getBytes());
    }

    public InputStream getBinaryInputStream() {
        if (rawBytes == null) {
            return null;
        }
        return new ByteArrayInputStream(rawBytes);
    }

    public abstract Object getObject() throws ParseException;

    public Date getDate(final Calendar cal) throws ParseException {
        if (rawBytes == null) {
            return null;
        }
        final String rawValue = getString();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setCalendar(cal);
        final java.util.Date utilDate = sdf.parse(rawValue);
        return new Date(utilDate.getTime());
    }

    public Time getTime(final Calendar cal) {
        // TODO: FIX! USE CAL!

        if (rawBytes == null) {
            return null;
        }
        final int packedTime = getInt();
        final long millis = Utils.unpackTime(packedTime);
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setCalendar(cal);
        return new Time(millis);
    }

    public Timestamp getTimestamp(final Calendar cal) throws ParseException {
        if (rawBytes == null) {
            return null;
        }
        final String rawValue = getString();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setCalendar(cal);
        final java.util.Date utilTime = sdf.parse(rawValue);
        return new Timestamp(utilTime.getTime());
    }

    public boolean getBoolean() {
        if (rawBytes == null) {
            return false;
        }

        final String rawVal = getString();
        return rawVal.equalsIgnoreCase("true") || rawVal.equalsIgnoreCase("1");
    }

    public boolean isNull() {
        return rawBytes == null;
    }



    public int getDisplayLength() {
        if (rawBytes != null) {
            return rawBytes.length;
        }
        return 4; //NULL
    }

}