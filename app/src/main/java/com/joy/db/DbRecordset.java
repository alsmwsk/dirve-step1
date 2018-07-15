package com.joy.db;

import com.joy.struct.FieldStruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DbRecordset extends DbUtil {
    private boolean bSortAscending;
    private DbRecordset dbChunk;
    private DbCommand dbCommand;
    private FieldStruct dbField;
    private DbRecordset dbIndex;
    private DbRecordset dbRecordset;
    private DbDatabase dbdatabase;
    private boolean displayError;
    private List fieldList;
    private FieldStruct fieldStruct;
    private List fieldStructList;
    private List fieldValueList;
    private int nCurRecord;
    private int nFieldCount;
    private int nRecordCount;
    private int nSize;
    private int nSortedColumn;
    byte[] pChunk;
    byte[] pRecordset;

    public DbRecordset() {
        this.dbdatabase = null;
        this.dbCommand = null;
        this.dbRecordset = null;
        this.dbChunk = null;
        this.dbIndex = null;
        this.dbField = null;
        this.fieldStruct = null;
        this.fieldStructList = null;
        this.fieldValueList = null;
        this.fieldList = null;
        this.displayError = false;
        this.dbdatabase = null;
        this.dbCommand = null;
        this.dbRecordset = null;
        this.dbChunk = null;
        this.dbIndex = null;
        this.fieldStruct = null;
        this.nSize = 0;
        this.nFieldCount = 0;
        this.nRecordCount = 0;
        this.fieldStructList = new ArrayList();
        this.fieldList = new ArrayList();
        this.displayError = false;
    }

    public DbRecordset(DbCommand dbCommand) {
        this.dbdatabase = null;
        this.dbCommand = null;
        this.dbRecordset = null;
        this.dbChunk = null;
        this.dbIndex = null;
        this.dbField = null;
        this.fieldStruct = null;
        this.fieldStructList = null;
        this.fieldValueList = null;
        this.fieldList = null;
        this.displayError = false;
        this.dbdatabase = null;
        this.dbCommand = dbCommand;
        this.dbRecordset = null;
        this.dbChunk = null;
        this.dbIndex = null;
        this.fieldStruct = null;
        this.nSize = 0;
        this.nFieldCount = 0;
        this.nRecordCount = 0;
        this.fieldStructList = new ArrayList();
        this.fieldList = new ArrayList();
        this.displayError = false;
    }

    public DbRecordset(DbDatabase dbDatabase) {
        this.dbdatabase = null;
        this.dbCommand = null;
        this.dbRecordset = null;
        this.dbChunk = null;
        this.dbIndex = null;
        this.dbField = null;
        this.fieldStruct = null;
        this.fieldStructList = null;
        this.fieldValueList = null;
        this.fieldList = null;
        this.displayError = false;
        this.dbdatabase = dbDatabase;
        this.dbCommand = null;
        this.dbRecordset = null;
        this.dbChunk = null;
        this.dbIndex = null;
        this.fieldStruct = null;
        this.nSize = 0;
        this.nFieldCount = 0;
        this.nRecordCount = 0;
        this.fieldStructList = new ArrayList();
        this.fieldList = new ArrayList();
        this.displayError = false;
    }

    public void setChunk(byte[] pChunk, int size) {
        this.dbIndex = null;
        this.pChunk = null;
        this.pChunk = pChunk;
    }

    public boolean execute(DbCommand pCmd) {
        this.displayError = false;
        this.dbCommand = pCmd;
        pCmd.setDbRecordset(this);
        if (pCmd.execute()) {
            return parser();
        }
        return false;
    }

    private boolean parser() {
        int cursor = 0;
        this.nCurRecord = 0;
        this.nFieldCount = parseInt(this.pChunk, 0);
        this.nRecordCount = parseInt(this.pChunk, 4);
        if (this.nRecordCount == 0) {
            return true;
        }
        int i;
        for (i = 0; i < this.nFieldCount; i++) {
            try {
                this.fieldStruct = new FieldStruct();
                cursor = (i * 40) + 8;
                this.fieldStruct.type = parseInt(this.pChunk, cursor + 0);
                this.fieldStruct.size = parseLong(this.pChunk, cursor + 4);
                this.fieldStruct.fieldName = new String(this.pChunk, cursor + 8, 31, "EUC-KR").trim();
                this.fieldStructList.add(this.fieldStruct);
            } catch (Exception e) {
            }
        }
        if (!(this.nFieldCount == 0 || this.nRecordCount == 0)) {
            cursor += 40;
        }
        this.pRecordset = new byte[(this.pChunk.length - cursor)];
        for (i = 0; i < this.pChunk.length - cursor; i++) {
            this.pRecordset[i] = this.pChunk[cursor + i];
        }
        cursor = 0;
        for (int row = 0; row < this.nRecordCount; row++) {
            this.fieldValueList = new ArrayList();
            for (int col = 0; col < this.nFieldCount; col++) {
                FieldStruct fs = (FieldStruct) this.fieldStructList.get(col);
                if (fs.type == 2) {
                    this.fieldValueList.add(Integer.valueOf(parseInt(this.pRecordset, cursor)));
                    cursor += (int) fs.size;
                } else if (fs.type == 3) {
                    this.fieldValueList.add(Long.valueOf(parseLong(this.pRecordset, cursor)));
                    cursor += (int) fs.size;
                } else if (fs.type == 202 || fs.type == 203) {
                    int strLen = (int) parseLong(this.pRecordset, cursor);
                    cursor += 4;
                    String temp = "";
                    if (strLen > 0) {
                        try {
                            temp = new String(this.pRecordset, cursor, strLen - 1, "EUC-KR");
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        strLen = 0;
                        temp = null;
                    }
                    this.fieldValueList.add(temp);
                    cursor += strLen;
                } else if (fs.type == 7) {
                    if (((byte) parseInt(this.pRecordset, cursor)) != (byte) -1) {
                        int year = parseShort1(this.pRecordset, cursor);
                        int month = parseShort1(this.pRecordset, cursor + 2);
                        int day = parseShort1(this.pRecordset, cursor + 4);
                        int hour = parseShort1(this.pRecordset, cursor + 6);
                        int minute = parseShort1(this.pRecordset, cursor + 8);
                        int second = parseShort1(this.pRecordset, cursor + 10);
                        int fiaction = (int) parseLong(this.pRecordset, cursor + 12);
                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set(year, month - 1, day, hour, minute, second);
                        this.fieldValueList.add(cal.getTime());
                        cursor += (int) fs.size;
                    } else {
                        cursor += 4;
                        this.fieldValueList.add(null);
                    }
                } else {
                    System.out.println("알 수 없는 타입 입니다.");
                }
            }
            this.fieldList.add(this.fieldValueList);
        }
        return true;
    }

    public String getFieldName(int index) {
        if (index < 0 || index >= this.nFieldCount) {
            return "";
        }
        return ((FieldStruct) this.fieldStructList.get(index)).fieldName;
    }

    public int getFieldType(int index) {
        if (index < 0 || index >= this.nFieldCount) {
            return 0;
        }
        return ((FieldStruct) this.fieldStructList.get(index)).type;
    }

    public long getFieldLength(int index) {
        if (index < 0 || index >= this.nFieldCount) {
            return 0;
        }
        return ((FieldStruct) this.fieldStructList.get(index)).size;
    }

    public boolean isFirst() {
        if (this.nCurRecord == 0) {
            return true;
        }
        return false;
    }

    public boolean isLast() {
        if (this.nCurRecord == this.nRecordCount) {
            return true;
        }
        return false;
    }

    public void moveNext() {
        this.nCurRecord++;
    }

    public int getRowCount() {
        return this.nRecordCount;
    }

    public String getFieldStringValue(int index) {
        int type = getFieldType(index);
        if ((type != DbInterface.STRING_TYPE && type != DbInterface.WSTRING_TYPE) || index < 0 || index >= this.nFieldCount) {
            return null;
        }
        this.fieldValueList = (ArrayList) this.fieldList.get(this.nCurRecord);
        String value = (String) this.fieldValueList.get(index);
        if (this.fieldValueList.get(index) == null) {
            return null;
        }
        return value;
    }

    public int getFieldIntValue(int index) {
        if (getFieldType(index) == 2 && index >= 0 && index < this.nFieldCount) {
            String temp = String.valueOf(((ArrayList) this.fieldList.get(this.nCurRecord)).get(index));
            if (!(temp.equals("") && temp == null)) {
                return Integer.valueOf(temp).intValue();
            }
        }
        return 0;
    }

    public long getFieldLongValue(int index) {
        if (getFieldType(index) == 3 && index >= 0 && index < this.nFieldCount) {
            String temp = String.valueOf(((ArrayList) this.fieldList.get(this.nCurRecord)).get(index));
            if (temp.equals("") || temp != null) {
                return Long.valueOf(temp).longValue();
            }
        }
        return 0;
    }

    public String getFieldDateValue(int index) {
        return getFieldDateValue(index, "yyyy-MM-dd HH:mm:ss");
    }

    public String getFieldDateValue(int index, String format) {
        if (getFieldType(index) != 7) {
            return null;
        }
        this.fieldValueList = (ArrayList) this.fieldList.get(this.nCurRecord);
        Date value = (Date) this.fieldValueList.get(index);
        String temp = null;
        if (this.fieldValueList.get(index) == null) {
            return temp;
        }
        try {
            return new SimpleDateFormat(format, Locale.KOREA).format(value);
        } catch (Exception e) {
            return temp;
        }
    }

    public String getFieldStringValue(String fieldName) {
        for (int i = 0; i < this.nFieldCount; i++) {
            if (fieldName.equals(((FieldStruct) this.fieldStructList.get(i)).fieldName)) {
                return getFieldStringValue(i);
            }
        }
        System.out.println(new StringBuilder(String.valueOf(fieldName)).append(" 필드가 존재 하지 않습니다.").toString());
        return null;
    }

    public int getFieldIntValue(String fieldName) {
        for (int i = 0; i < this.nFieldCount; i++) {
            if (fieldName.equals(((FieldStruct) this.fieldStructList.get(i)).fieldName)) {
                return getFieldIntValue(i);
            }
        }
        System.out.println(new StringBuilder(String.valueOf(fieldName)).append(" 필드가 존재 하지 않습니다.").toString());
        return 0;
    }

    public long getFieldLongValue(String fieldName) {
        for (int i = 0; i < this.nFieldCount; i++) {
            if (fieldName.equals(((FieldStruct) this.fieldStructList.get(i)).fieldName)) {
                return getFieldLongValue(i);
            }
        }
        System.out.println(new StringBuilder(String.valueOf(fieldName)).append(" 필드가 존재 하지 않습니다.").toString());
        return 0;
    }

    public String getFieldDateValue(String fieldName) {
        for (int i = 0; i < this.nFieldCount; i++) {
            if (fieldName.equals(((FieldStruct) this.fieldStructList.get(i)).fieldName)) {
                return getFieldDateValue(i, "yyyy-MM-dd HH:mm:ss");
            }
        }
        System.out.println(new StringBuilder(String.valueOf(fieldName)).append(" 필드가 존재 하지 않습니다.").toString());
        return null;
    }

    public String getFieldDateValue(String fieldName, String format) {
        for (int i = 0; i < this.nFieldCount; i++) {
            if (fieldName.equals(((FieldStruct) this.fieldStructList.get(i)).fieldName)) {
                return getFieldDateValue(i, format);
            }
        }
        System.out.println(new StringBuilder(String.valueOf(fieldName)).append(" 필드가 존재 하지 않습니다.").toString());
        return null;
    }

    public String toString() {
        String data = "";
        for (int i = 0; i < this.nFieldCount; i++) {
            data = new StringBuilder(String.valueOf(data)).append("[").append(((FieldStruct) this.fieldStructList.get(i)).fieldName).append("-").append(((FieldStruct) this.fieldStructList.get(i)).type).append("]:").append(this.fieldValueList.get(i)).append(", ").toString();
        }
        return data.substring(0, data.length() - 2);
    }
}
