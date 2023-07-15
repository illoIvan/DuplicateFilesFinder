
package com.illoivan.duplicatefiles.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.CRC32;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;

public class Utils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private Utils() {}
    
    public static Path createPath(String path, String... more) {
        return Paths.get(path, more);
    }

    public static boolean isDirectory(String path) {
        return Files.isDirectory(createPath(path), LinkOption.NOFOLLOW_LINKS);
    }

    public static String isValidPath(String strPath) {
        if (isNullOrEmpty(strPath)) {
            return "The path is empty";
        }
        File file = new File(strPath);

        if (!file.exists()) {
            return "The specified path does not exist";
        }

        if (!file.isDirectory()) {
            return "The path is not a directory";
        }

        if (!file.canRead()) {
            return "Cannot read the file";
        }

        return null;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty() || value.isBlank();
    }

    public static String substringFileNameBySeparator(String name, String separator) {
        return separator.isEmpty() ? name : name.substring(name.indexOf(separator) + 1);
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant,ZoneId.systemDefault());
    }

    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : null;
    }    
    
    public static String getFormatedDate(long duration) {
        int time = (int) duration;
        int hours = time / 3600;
        int minutes = (time / 60) % 60;
        int seconds = time % 60;

        return hours > 0 
                ? String.format("%02d:%02d:%02d", hours,  minutes, seconds)
                : String.format("%02d:%02d", minutes, seconds);
    }

    
    public static String createChecksum(byte[] bytes, String algorithm){
        if (algorithm.equalsIgnoreCase("CRC32")) {
            CRC32 crc = new CRC32();
            crc.update(bytes);
            return Long.toHexString(crc.getValue());
        } else {
        	byte[] digest = new byte[0];
			try {
				MessageDigest md = MessageDigest.getInstance(algorithm.toUpperCase());
				md.update(bytes);
				digest = md.digest();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
            
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xFF & digest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
    }
    
    public static byte[] readBytesFile(String path, CustomReadBuffer readBuffer, CustomFile customFile) {
    	try (FileInputStream fileInputStream = new FileInputStream(path)){
        	long skipped = 0;
        	
        	if(readBuffer.getStart() > 0) {
        		skipped = fileInputStream.skip(readBuffer.getStart());
        	}
        	
            if (skipped < readBuffer.getStart()) {
            	customFile.setError("Could not skip to byte " + readBuffer.getStart());
                throw new IOException("Could not skip to byte " + readBuffer.getStart());
            }
            
            int numBytesCanReadFile = fileInputStream.available(); 
            if(readBuffer.getMax() > numBytesCanReadFile) {
            	customFile.setError("Maximum byte size exceeded" + readBuffer.getMax());
            	throw new IOException("Maximum byte size exceeded" + readBuffer.getMax());
            }
            int numBytesToRead = numBytesCanReadFile;
            if(readBuffer.getMax() > 0) {
            	numBytesToRead = readBuffer.getMax() - readBuffer.getStart();
            }
            
            byte[] buffer = new byte[numBytesToRead];
            int bytesRead = fileInputStream.read(buffer, 0, numBytesToRead);

            if (bytesRead != numBytesToRead) {
            	customFile.setError("Could not be read " + numBytesToRead + " bytes");
            	throw new IOException("Could not be read " + numBytesToRead + " bytes");
            }
            return buffer;
    	}catch (Exception e) {
    		customFile.setError(e.getMessage());
			e.printStackTrace();
		}
    	return new byte[0];
    }
    
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return null;
        }
        return name.substring(lastIndexOf + 1);
    }

}
