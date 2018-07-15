package com.joy.db;

import com.joy.security.AESCipher;
import com.joy.security.DES;
import com.joy.struct.CommandStruct;

public class DbDatabase extends DbUtil {
    private int bodySize;
    private int bodyType;
    public DbSocket dbSocket;
    public DES des;
    private boolean desState;
    private long fileSize;
    private String ip;
    public int mainType;
    private int port;
    public long size;

    public DbDatabase() {
        this.dbSocket = null;
        this.des = null;
        this.ip = null;
        this.port = 0;
        this.desState = false;
        this.bodyType = 0;
        this.bodySize = 0;
        this.fileSize = 0;
        this.des = new DES();
    }

    public DbDatabase(String ip, int port) {
        this.dbSocket = null;
        this.des = null;
        this.ip = null;
        this.port = 0;
        this.desState = false;
        this.bodyType = 0;
        this.bodySize = 0;
        this.fileSize = 0;
        this.des = new DES();
        this.ip = ip;
        this.port = port;
    }

    public DbDatabase(String ip, int port, boolean desState) {
        this.dbSocket = null;
        this.des = null;
        this.ip = null;
        this.port = 0;
        this.desState = false;
        this.bodyType = 0;
        this.bodySize = 0;
        this.fileSize = 0;
        this.ip = ip;
        this.port = port;
        this.desState = desState;
    }

    public boolean open() {
        this.dbSocket = new DbSocket(this.ip, this.port, this.desState);
        if (connectServer()) {
            return true;
        }
        return false;
    }

    public void close() {
        this.dbSocket.close();
    }

    protected int getMainType() {
        return this.mainType;
    }

    protected void setMainType(int mainType) {
        this.mainType = mainType;
    }

    protected long getSize() {
        return this.size;
    }

    protected void setSize(int size) {
        this.size = (long) size;
    }

    protected boolean connectServer() {
        close();
        if (!this.dbSocket.bInitDes) {
            this.des.init(this.dbSocket.fixedKey, 0, false);
            this.dbSocket.bInitDes = true;
        }
        if (this.dbSocket.open()) {
            return true;
        }
        return false;
    }

    protected int connectTestProc() {
        return 0;
    }

    protected int getBodyType() {
        return this.bodyType;
    }

    protected void setBodyType(int bodyType) {
        this.bodyType = bodyType;
    }

    protected int getBodySize() {
        return this.bodySize;
    }

    protected void setBodySize(int bodySize) {
        this.bodySize = bodySize;
    }

    protected byte[] SendUserCommandWithResponse(int nSubType, String data) {
        byte[] returnVal = null;
        try {
            if (this.dbSocket.sendData(150, nSubType, data.getBytes("8859_1"), data.length())) {
                while (true) {
                    byte[] recv = this.dbSocket.ReadStream();
                    if (recv != null) {
                        int i;
                        switch (parseShort(recv, 4)) {
                            case 150:
                                this.bodyType = parseShort(recv, 6);
                                this.bodySize = parseShort(recv, 2) - 8;
                                returnVal = new byte[(this.bodySize + 1)];
                                for (i = 0; i < this.bodySize; i++) {
                                    returnVal[i] = recv[i + 8];
                                }
                                try {
                                    return AESCipher.AES_Decode(returnVal, returnVal.length);
                                } catch (Exception e) {
                                    return null;
                                }
                            case DbInterface.PT_ERROR /*180*/:
                                System.out.println("개체 : " + recv + " 오류 입니다.");
                                return null;
                            case 190:
                                if (parseShort(recv, 6) == 1020) {
                                    this.fileSize = Long.valueOf(new String(recv, 8, parseShort(recv, 2) - 8, "EUC-KR")).longValue();
                                    this.bodySize = 0;
                                    returnVal = new byte[(((int) this.fileSize) + 1)];
                                } else {
                                    int nParmeSize = parseShort(recv, 2) - 8;
                                    i = 0;
                                    while (i < nParmeSize) {
                                        try {
                                            returnVal[this.bodySize + i] = recv[i + 8];
                                            i++;
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                            break;
                                        }
                                    }
                                    this.bodySize += nParmeSize;
                                }
                                if (this.dbSocket.sendCommand(150, 0)) {
                                    break;
                                }
                                return null;
                            case DbInterface.PT_END /*191*/:
                                this.bodyType = parseShort(recv, 6);
                                this.bodySize = (int) this.fileSize;
                                return returnVal;
                            default:
                                break;
                        }
                    }
                    return null;
                }
            }
            return null;
        } catch (Exception e22) {
            e22.printStackTrace();
            return null;
        }
    }

    protected synchronized byte[] execute(CommandStruct pCS, long size, String commonText) {
        byte[] result;
        int i;
        byte[] data = new byte[((int) size)];
        this.bodySize = 0;
        data[0] = (byte) (pCS.type & 255);
        data[1] = (byte) ((pCS.type >> 8) & 255);
        for (i = 0; i < pCS.sotredProc.length; i++) {
            data[i + 2] = pCS.sotredProc[i];
        }
        data[(pCS.sotredProc.length + 2) + 0] = (byte) ((pCS.varCount >> 8) & 255);
        data[(pCS.sotredProc.length + 2) + 1] = (byte) (pCS.varCount & 255);
        for (i = 0; i < pCS.data.length; i++) {
            data[i + 59] = pCS.data[i];
        }
        if (this.dbSocket.sendData(150, DbInterface.PST_EXECUTE_DB, data, (int) size)) {
            result = result(commonText);
        } else {
            close();
            if (open() && this.dbSocket.sendData(150, DbInterface.PST_EXECUTE_DB, data, (int) size)) {
                result = result(commonText);
            } else {
                System.out.println("sendData 실패");
                result = null;
            }
        }
        return result;
    }

    private byte[] result(String commonText) {
        byte[] returnVal = null;
        this.bodySize = 0;
        while (true) {
            try {
                byte[] recv = this.dbSocket.ReadStream();
                if (recv != null) {
                    int i;
                    switch (parseShort(recv, 4)) {
                        case 150:
                            this.bodyType = parseShort(recv, 6);
                            this.bodySize = AESCipher.headerSizeToPacketSize(parseShort(recv, 2) - 8);
                            returnVal = new byte[(this.bodySize + 1)];
                            for (i = 0; i < this.bodySize; i++) {
                                returnVal[i] = recv[i + 8];
                            }
                            byte[] decodedata = AESCipher.AES_Decode(returnVal, returnVal.length);
                            if (this.bodySize == decodedata.length) {
                                return decodedata;
                            }
                            this.bodySize = decodedata.length;
                            return decodedata;
                        case DbInterface.PT_ERROR /*180*/:
                            System.out.println("개체 : " + commonText + " 오류 입니다.");
                            return null;
                        case 190:
                            if (parseShort(recv, 6) == 1020) {
                                byte[] byteSize = new String(recv, 8, AESCipher.headerSizeToPacketSize(parseShort(recv, 2) - 8), "EUC-KR").getBytes();
                                this.fileSize = Long.valueOf(new String(AESCipher.AES_Decode(byteSize, byteSize.length), "EUC-KR").trim()).longValue();
                                this.bodySize = 0;
                                returnVal = new byte[(((int) this.fileSize) + 1)];
                            } else {
                                int nParmeSize = parseShort(recv, 2) - 8;
                                int nPacketSize = AESCipher.headerSizeToPacketSize(nParmeSize);
                                byte[] recvData = new byte[nPacketSize];
                                for (i = 0; i < nPacketSize; i++) {
                                    recvData[i] = recv[i + 8];
                                }
                                byte[] decodeByte = AESCipher.AES_Decode(recvData, nPacketSize);
                                int nLen = decodeByte.length;
                                for (i = 0; i < nParmeSize; i++) {
                                    returnVal[this.bodySize + i] = decodeByte[i];
                                }
                                this.bodySize += nParmeSize;
                            }
                            if (this.dbSocket.sendCommand(150, 0)) {
                                break;
                            }
                            return null;
                        case DbInterface.PT_END /*191*/:
                            this.bodyType = parseShort(recv, 6);
                            this.bodySize = (int) this.fileSize;
                            return returnVal;
                        default:
                            break;
                    }
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
