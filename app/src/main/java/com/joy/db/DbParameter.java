package com.joy.db;

import com.joy.struct.ParameterStruct;
import java.io.UnsupportedEncodingException;

public class DbParameter extends DbUtil {
    ParameterStruct parameter;

    public DbParameter() {
        this.parameter = null;
        this.parameter = new ParameterStruct();
    }

    private void init() {
        this.parameter.type = 0;
        this.parameter.dir = 0;
        this.parameter.size = 0;
    }

    public long setParameter(int type, int direction, String value, int size) {
        init();
        try {
            this.parameter.type = type;
            this.parameter.dir = direction;
            this.parameter.size = (long) size;
            int i;
            if (value == null || value.equals("null")) {
                this.parameter.data = new byte[size];
                for (i = 0; i < size; i++) {
                    this.parameter.data[i] = (byte) 0;
                }
                return this.parameter.size;
            }
            if (type == DbInterface.STRING_TYPE) {
                this.parameter.data = new byte[(size + 1)];
                byte[] byteVal = value.getBytes("8859_1");
                for (i = 0; i < size; i++) {
                    this.parameter.data[i] = byteVal[i];
                }
                this.parameter.data[size] = (byte) 0;
                this.parameter.size++;
            } else if (type == 2) {
                int intVal = Integer.valueOf(value).intValue();
                this.parameter.data = parseByte(intVal);
            } else if (type == 3) {
                long longVal = Long.valueOf(value).longValue();
                this.parameter.data = parseByte(longVal);
            } else if (value != null) {
                this.parameter.data = value.getBytes();
            } else {
                this.parameter.data = new byte[size];
                for (i = 0; i < size; i++) {
                    this.parameter.data[i] = (byte) 0;
                }
            }
//            return this.parameter.size;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.parameter.size;
    }

    public long setParameter(int type, int direction, byte[] value, int size) {
        init();
        try {
            this.parameter.type = type;
            this.parameter.dir = direction;
            this.parameter.size = (long) size;
            int i;
            if (value == null || value.equals("null")) {
                this.parameter.data = new byte[size];
                for (i = 0; i < size; i++) {
                    this.parameter.data[i] = (byte) 0;
                }
                return this.parameter.size;
            }
            if (type == DbInterface.STRING_TYPE) {
                this.parameter.data = new byte[(size + 1)];
                for (i = 0; i < size; i++) {
                    this.parameter.data[i] = value[i];
                }
                this.parameter.data[size] = (byte) 0;
                this.parameter.size++;
            } else if (type == 2) {
                this.parameter.data = value;
            } else if (type == 3) {
                this.parameter.data = value;
            } else if (value != null) {
                this.parameter.data = value;
            } else {
                this.parameter.data = new byte[size];
                for (i = 0; i < size; i++) {
                    this.parameter.data[i] = (byte) 0;
                }
            }
//            return this.parameter.size;
        } catch (Exception e) {
        }
        return this.parameter.size;
    }

    public ParameterStruct getParameter() {
        return this.parameter;
    }

    public int getParameterType() {
        return this.parameter.type;
    }

    public int getParameterDir() {
        return this.parameter.dir;
    }

    public long getParameterSize() {
        return this.parameter.size;
    }

    public void setValue(ParameterStruct ps, byte[] buffer, int cursor) {
        if (ps.type == DbInterface.STRING_TYPE) {
            ps.data = new byte[(((int) ps.size) + 1)];
            for (int i = 0; ((long) i) < ps.size; i++) {
                ps.data[i] = buffer[cursor + i];
            }
            ps.data[(int) ps.size] = (byte) 0;
        } else {
            ps.data = new byte[((int) ps.size)];
            ps.data[0] = buffer[cursor + 0];
            ps.data[1] = buffer[cursor + 1];
            ps.data[2] = buffer[cursor + 2];
            ps.data[3] = buffer[cursor + 3];
        }
        this.parameter.data = ps.data;
    }

    public void setValue(int value) {
        this.parameter.data = parseByte(value);
    }

    public void setValue(long value) {
        this.parameter.data = parseByte(value);
    }

    public String getStringValue() throws UnsupportedEncodingException {
        return new String(this.parameter.data, "EUC_KR");
    }

    public int getIntValue() {
        return parseInt(this.parameter.data, 0);
    }

    public long getLongValue() {
        return parseLong(this.parameter.data, 0);
    }
}
