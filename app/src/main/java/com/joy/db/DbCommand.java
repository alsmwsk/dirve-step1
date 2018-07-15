package com.joy.db;

import com.joy.struct.CommandStruct;
import com.joy.struct.ParameterStruct;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbCommand extends DbUtil {
    private String commonText;
    private DbDatabase dbDatabase;
    private DbRecordset dbRecordset;
    private long paramDateSize;
    private List paramList;

    public DbCommand() {
        this.paramDateSize = 0;
        this.paramList = new ArrayList();
        this.dbDatabase = null;
        this.dbRecordset = null;
        this.paramDateSize = 0;
        this.paramList.clear();
    }

    public DbCommand(DbDatabase dbDatabase) {
        this.paramDateSize = 0;
        this.paramList = new ArrayList();
        this.dbDatabase = dbDatabase;
        this.dbRecordset = null;
        this.paramDateSize = 0;
        this.paramList.clear();
    }

    public DbCommand(DbDatabase dbDatabase, String commonText) {
        this.paramDateSize = 0;
        this.paramList = new ArrayList();
        this.dbDatabase = dbDatabase;
        this.dbRecordset = null;
        this.paramDateSize = 0;
        this.commonText = commonText;
        this.paramList.clear();
    }

    public DbRecordset getDbRecordset() {
        return this.dbRecordset;
    }

    public void setDbRecordset(DbRecordset dbRecordset) {
        this.dbRecordset = dbRecordset;
    }

    public DbParameter addParameter(int type, int direction, String value) {
        DbParameter dbParameter = new DbParameter();
        try {
            long size = dbParameter.setParameter(type, direction, value, value.getBytes("8859_1").length);
            this.paramList.add(dbParameter);
            this.paramDateSize += size;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbParameter;
    }

    public DbParameter addParameter(int type, int direction, int value) {
        DbParameter dbParameter = new DbParameter();
        long size = dbParameter.setParameter(type, direction, String.valueOf(value), 4);
        this.paramList.add(dbParameter);
        this.paramDateSize += size;
        return dbParameter;
    }

    public DbParameter addParameter(int type, int direction, long value) {
        DbParameter dbParameter = new DbParameter();
        long size = dbParameter.setParameter(type, direction, String.valueOf(value), 4);
        this.paramList.add(dbParameter);
        this.paramDateSize += size;
        return dbParameter;
    }

    public DbParameter addParameter(int type, int direction, Calendar cal) {
        DbParameter dbParameter = new DbParameter();
        byte[] date = new byte[12];
        if (cal == null) {
            date = null;
        } else {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hore = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            date[0] = (byte) (year & 255);
            date[1] = (byte) (year >> 8);
            date[2] = (byte) (month & 255);
            date[3] = (byte) (month >> 8);
            date[4] = (byte) (day & 255);
            date[5] = (byte) (day >> 8);
            date[6] = (byte) (hore & 255);
            date[7] = (byte) (hore >> 8);
            date[8] = (byte) (minute & 255);
            date[9] = (byte) (minute >> 8);
            date[10] = (byte) (second & 255);
            date[11] = (byte) (second >> 8);
        }
        long size = dbParameter.setParameter(type, direction, date, 12);
        this.paramList.add(dbParameter);
        this.paramDateSize += size;
        return dbParameter;
    }

    public String getOutParamter(int paramConut) {
        DbParameter dbParameter = (DbParameter) this.paramList.get(paramConut);
        String data = "";
        try {
            byte[] temp = new byte[(dbParameter.getParameter().data.length - 1)];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = dbParameter.getParameter().data[i];
            }
            return new String(temp, "EUC_KR");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return data;
        }
    }

    public boolean execute() {
        int i;
        StringBuffer sb = new StringBuffer();
        int cursor = 1;
        int size = (((this.paramList.size() * 12) + 59) + ((int) this.paramDateSize)) + 1;
        CommandStruct pCS = new CommandStruct();
        pCS.type = 4;
        if (!(getDbRecordset() == null && getDbRecordset().equals("null"))) {
            pCS.type += 2;
        }
        pCS.varCount = this.paramList.size();
        byte[] commonTemp = this.commonText.getBytes();
        int commonSize = commonTemp.length;
        for (i = 0; i < 55; i++) {
            if (i < commonSize) {
                pCS.sotredProc[i] = commonTemp[i];
            } else {
                pCS.sotredProc[i] = (byte) 0;
            }
        }
        byte[] commonData = new byte[(((this.paramList.size() * 12) + ((int) this.paramDateSize)) + 1)];
        for (i = 0; i < this.paramList.size(); i++) {
            int j;
            DbParameter parameter = (DbParameter) this.paramList.get(i);
            int paramType = parameter.getParameterType();
            int paramDir = parameter.getParameterDir();
            int paramSize = (int) parameter.getParameterSize();
            int paramStuctSize = paramSize + 12;
            byte[] paramTmp = new byte[paramStuctSize];
            byte[] temp = parseByte(paramType);
            paramTmp[0] = temp[0];
            paramTmp[1] = temp[1];
            paramTmp[2] = temp[2];
            paramTmp[3] = temp[3];
            temp = parseByte(paramDir);
            paramTmp[4] = temp[0];
            paramTmp[5] = temp[1];
            paramTmp[6] = temp[2];
            paramTmp[7] = temp[3];
            temp = parseByte(paramSize);
            paramTmp[8] = temp[0];
            paramTmp[9] = temp[1];
            paramTmp[10] = temp[2];
            paramTmp[11] = temp[3];
            byte[] data = parameter.getParameter().data;
            for (j = 0; j < paramSize; j++) {
                paramTmp[j + 12] = data[j];
            }
            for (j = 0; j < paramStuctSize; j++) {
                commonData[cursor + j] = paramTmp[j];
            }
            cursor += paramStuctSize;
        }
        commonData[0] = (byte) 0;
        pCS.data = commonData;
        byte[] buffer = this.dbDatabase.execute(pCS, (long) size, this.commonText);
        if (buffer == null) {
            return false;
        }
        int type = this.dbDatabase.getBodyType();
        if ((type & 1) > 0) {
            cursor = 59;
            CommandStruct cs = new CommandStruct();
            int dataSize = this.dbDatabase.getBodySize() - 59;
            cs.data = new byte[dataSize];
            for (i = 0; i < dataSize; i++) {
                cs.data[i] = buffer[59 + i];
            }
            for (i = 0; i < this.paramList.size(); i++) {
                DbParameter param = (DbParameter) this.paramList.get(i);
                if (param.getParameterDir() >= 2) {
                    ParameterStruct ps = new ParameterStruct();
                    ps.type = parseInt(buffer, cursor + 1);
                    ps.dir = param.getParameterDir();
                    ps.size = parseLong(buffer, cursor + 9);
                    cursor = (cursor + 12) + 1;
                    param.setValue(ps, buffer, cursor);
                    cursor = (((int) ps.size) + cursor) - 1;
                }
            }
            if ((type & 2) > 0 && this.dbRecordset != null) {
                dataSize = (dataSize - cursor) + 59;
                byte[] pChunk = new byte[(dataSize + 1)];
                for (i = 0; i < dataSize; i++) {
                    pChunk[i] = cs.data[(cursor - 59) + i];
                }
                this.dbRecordset.setChunk(pChunk, dataSize);
            }
        } else if ((type & 2) > 0 && this.dbRecordset != null) {
            this.dbRecordset.setChunk(buffer, this.dbDatabase.getBodySize());
        }
        return true;
    }
}
