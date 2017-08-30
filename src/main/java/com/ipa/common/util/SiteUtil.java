package com.ipa.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SiteUtil {
	
	protected static Logger log = Logger.getLogger(SiteUtil.class.getName());

	public static void mergeJson(Object o1, Object o2) {}
	
	public static boolean deleteDir(File f, boolean deleteSelf) throws IOException{
		if (f == null){
			return false;
		}
		boolean deleteOk = true;
		if (f.isDirectory()){
			File [] files = f.listFiles();
			if(files != null) {
				for (int i = 0; i < files.length; i++) {
					
					boolean deleted = deleteDir(files[i],true);
					if (!deleted){
						deleteOk = false;
					}
				}
			}
		}
		boolean exists = f.exists();
		if (deleteSelf){
			for (int i=0; exists && i<3; i++){
				if (i > 0){
					try{
						Thread.sleep(10);
					}catch(Exception e){}
				}
				exists = !f.delete();
			}
			if (exists){
				log.log(Level.SEVERE,"Failed to delete file " + f);
			}
		}else{
			
		}
		return deleteOk && (!exists || deleteSelf);
	}
	
	public static String readFileAsString(File f) throws Exception{
		FileInputStream in = null;
		
		try {
			StringBuffer buffer = new StringBuffer();
			in = new FileInputStream(f);
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in));
	        String s;
	        while((s = bufferedreader.readLine()) != null) {
	        	buffer.append(s);
	        	buffer.append("\n");
	        }
	        return buffer.toString();
	            
	    }finally {
	    	if (in != null){
	    		try {
	    			in.close();
	    		}catch (Exception e){}
	    	}
	    }
	}

	public static byte hexToByte( char[] hex ) throws NumberFormatException {

		int  i = 0;
		byte nibble;

		if((hex[ i ] >= '0') && (hex[ i ] <= '9')) {
			nibble = (byte)((hex[ i ] - '0') << 4);
		}
		else if((hex[ i ] >= 'A') && (hex[ i ] <= 'F')) {
			nibble = (byte)((hex[ i ] - ('A' - 0x0A)) << 4);
		}
		else if((hex[ i ] >= 'a') && (hex[ i ] <= 'f')) {
			nibble = (byte)((hex[ i ] - ('a' - 0x0A)) << 4);
		}
		else {
			throw new NumberFormatException( hex[ i ] + " is not a hexadecimal string." );
		}

		if(i == hex.length) {
			throw new NumberFormatException( "Invalid number of digits in " + new String( hex ) );
		}

		i++;

		if((hex[ i ] >= '0') && (hex[ i ] <= '9')) {
			nibble = (byte)(nibble | (hex[ i ] - '0'));
		}
		else if((hex[ i ] >= 'A') && (hex[ i ] <= 'F')) {
			nibble = (byte)(nibble | (hex[ i ] - ('A' - 0x0A)));
		}
		else if((hex[ i ] >= 'a') && (hex[ i ] <= 'f')) {
			nibble = (byte)(nibble | (hex[ i ] - ('a' - 0x0A)));
		}
		else {
			throw new NumberFormatException( hex[ i ] + " is not a hexadecimal string." );
		}

		return nibble;
	}
	
	public static String bytesToHex( byte[] ba ) {

		int          len = ba.length;
		int          j;
		int          k;
		StringBuffer sb = new StringBuffer( (len * 3) );

		for(int i = 0; i < len; i++) {
			j = (ba[ i ] & 0xf0) >> 4;
			k = ba[ i ] & 0xf;

			sb.append( HEX_CHARS[ j ] );
			sb.append( HEX_CHARS[ k ] );
		}

		return sb.toString();
	}


	private static final char[] HEX_CHARS = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};

}
