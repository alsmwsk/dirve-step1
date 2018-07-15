package com.joy.db;

import com.joy.security.AESCipher;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class DbSocket extends DbUtil {
    public static final String COL_DELIMITER = "\u001b";
    public static final int headerSize = 8;
    protected boolean DES_STATE;
    private String SERVER_IP;
    private int SERVER_PORT;
    protected boolean bInitDes = false;
    private int connectTimeOut = 7;
    private boolean connected = false;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    public byte[] fixedKey = new byte[]{(byte) 85, (byte) 73, (byte) -31, (byte) 18, (byte) -66, (byte) 3, (byte) -46, (byte) 55};
    private InputStream is = null;
    private OutputStream os = null;
    private byte[] retBytes;
    private Socket socket = null;

    public DbSocket(String ip, int port, boolean desState) {
        this.SERVER_IP = ip;
        this.SERVER_PORT = port;
        this.DES_STATE = desState;
    }

    public boolean open() {
        try {
            this.socket = new Socket(InetAddress.getByName(this.SERVER_IP), this.SERVER_PORT);
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();
            this.dis = new DataInputStream(this.is);
            this.dos = new DataOutputStream(this.os);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void close() {
        try {
            if (this.dis != null) {
                this.dis.close();
            }
            this.dis = null;
            if (this.dos != null) {
                this.dos.close();
            }
            this.dos = null;
            if (this.socket != null) {
                this.socket.close();
            }
            this.socket = null;
        } catch (IOException e) {
        }
    }

    protected void setTimeOut(long time) {
    }

    public boolean sendData(int nType, int nSubType, byte[] data, int dataLen) {
        byte[] sData;
        int k;
        if (nType == DbInterface.PT_DATABASE) {
            sData = new byte[dataLen];
            for (k = 0; k < dataLen; k++) {
                sData[k] = data[k];
            }
        } else {
            try {
                sData = AESCipher.AES_Encode(data, dataLen);
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
        dataLen = sData.length;
        byte[] sPacket = new byte[(dataLen + 8)];
        sPacket[0] = (byte) 72;
        sPacket[1] = (byte) 0;
        sPacket[2] = (byte) ((dataLen + 8) >> 8);
        sPacket[3] = (byte) ((dataLen + 8) & 255);
        sPacket[4] = (byte) (nType >> 8);
        sPacket[5] = (byte) (nType & 255);
        sPacket[6] = (byte) (nSubType >> 8);
        sPacket[7] = (byte) (nSubType & 255);
        for (k = 0; k < dataLen; k++) {
            sPacket[k + 8] = sData[k];
        }
        try {
            this.dos.write(sPacket, 0, sData.length + 8);
            this.dos.flush();
            return true;
        } catch (Exception exception2) {
            System.out.println("write Exception...");
            exception2.printStackTrace();
            return false;
        }
    }

    public byte[] ReadStream() throws Exception {
        int i;
        int j = 0;
        int k = 0;
        byte[] header = new byte[8];
        while (j < 8) {
            int l = this.dis.read(header, j, 8 - j);
            if (l == -1) {
                throw new Exception("End of stream retured.");
            } else if (l >= 0) {
                j += l;
            }
        }
        if (parseShort(header, 4) == DbInterface.PT_END) {
            i = parseShort(header, 2);
        } else {
            i = AESCipher.headerSizeToPacketSize(parseShort(header, 2) - 8) + 8;
        }
        if (i <= 8) {
            return header;
        }
        byte[] rcv = new byte[i];
        for (int j1 = 0; j1 < 8; j1++) {
            rcv[j1] = header[j1];
        }
        while (k < i - 8) {
            int i1 = this.dis.read(rcv, k + 8, (i - 8) - k);
            if (i1 == -1) {
                throw new Exception("End of stream retured.");
            } else if (i1 >= 0) {
                k += i1;
            }
        }
        if (i != 16) {
            return rcv;
        }
        this.retBytes = new byte[8];
        for (int k1 = 0; k1 < 8; k1++) {
            this.retBytes[k1] = rcv[k1];
        }
        return rcv;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized java.lang.String K2E(java.lang.String r7) {
        /*
        r4 = com.joy.db.DbSocket.class;
        monitor-enter(r4);
        r1 = 0;
        if (r7 != 0) goto L_0x0009;
    L_0x0006:
        r3 = 0;
    L_0x0007:
        monitor-exit(r4);
        return r3;
    L_0x0009:
        r2 = new java.lang.String;	 Catch:{ all -> 0x002b }
        r2.<init>(r7);	 Catch:{ all -> 0x002b }
        r1 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r3 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r5 = "KSC5601";
        r5 = r7.getBytes(r5);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r6 = "8859_1";
        r3.<init>(r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r1.<init>(r3);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
    L_0x0022:
        r3 = r1;
        goto L_0x0007;
    L_0x0024:
        r0 = move-exception;
        r1 = new java.lang.String;	 Catch:{ all -> 0x002e }
        r1.<init>(r7);	 Catch:{ all -> 0x002e }
        goto L_0x0022;
    L_0x002b:
        r3 = move-exception;
    L_0x002c:
        monitor-exit(r4);
        throw r3;
    L_0x002e:
        r3 = move-exception;
        r1 = r2;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.joy.db.DbSocket.K2E(java.lang.String):java.lang.String");
    }

    public boolean sendCommand(int nType, int nSubType) {
        try {
            this.dos.write(new byte[]{(byte) 72, (byte) 0, (byte) 0, (byte) 8, (byte) (nType >> 8), (byte) (nType & 255), (byte) (nSubType >> 8), (byte) (nSubType & 255)}, 0, 8);
            this.dos.flush();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
