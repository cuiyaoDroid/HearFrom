package com.tingshuo.web.http;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpStringMD5 {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9',

			'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) {

		// String to byte

		StringBuilder sb = new StringBuilder(b.length * 2);

		for (int i = 0; i < b.length; i++) {

			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);

			sb.append(HEX_DIGITS[b[i] & 0x0f]);

		}

		return sb.toString();

	}

	public static String md5(String string) {

	    byte[] hash;

	    try {

	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));

	    } catch (NoSuchAlgorithmException e) {

	        throw new RuntimeException("Huh, MD5 should be supported?", e);

	    } catch (UnsupportedEncodingException e) {

	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);

	    }


	    StringBuilder hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {

	        if ((b & 0xFF) < 0x10) hex.append("0");

	        hex.append(Integer.toHexString(b & 0xFF));

	    }

	    return hex.toString();

	}
}
