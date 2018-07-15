package com.joy.security;

import com.facebook.appevents.AppEventsConstants;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {
    public static int BUFFER_SIZE = 16;
    public static byte[] ivBytes = new byte[16];
    public static String key = "1234567890123456";

    public static byte[] AES_Encode(byte[] data, int len) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        int diff = BUFFER_SIZE - (len % BUFFER_SIZE);
        byte[] textBytes = new byte[(len + diff)];
        for (int k = 0; k < len; k++) {
            textBytes[k] = data[k];
        }
        for (int i = 0; i < diff; i++) {
            textBytes[len + i] = (byte) 0;
        }
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(1, newKey, ivSpec);
        return byteArrayToHex(cipher.doFinal(textBytes));
    }

    public static byte[] AES_Decode(byte[] data, int len) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] textBytes = hexToByteArray(new String(data, "EUC-KR"));
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("EUC-KR"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(2, newKey, ivSpec);
        return cipher.doFinal(textBytes);
    }

    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }
        byte[] ba = new byte[(hex.length() / 2)];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(i * 2, (i * 2) + 2), 16);
        }
        return ba;
    }

    public static byte[] byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(ba.length * 2);
        for (byte b : ba) {
            String hexNumber = new StringBuilder(AppEventsConstants.EVENT_PARAM_VALUE_NO).append(Integer.toHexString(b & 255)).toString();
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString().toUpperCase().getBytes();
    }

    public static int headerSizeToPacketSize(int headerSize) {
        int nDiffSize = BUFFER_SIZE - (headerSize % BUFFER_SIZE);
        int nPacketSize = headerSize;
        if (nDiffSize > 0) {
            nPacketSize += nDiffSize;
        }
        return nPacketSize * 2;
    }
}
