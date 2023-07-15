package com.illoivan.duplicatefiles.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.List;

import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;
import com.illoivan.duplicatefiles.service.MetadataService;
import com.illoivan.duplicatefiles.utils.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomFile{

    private File file;
    private CustomFile parent;
    private String fileName;
    private String folder;
    private String pathFileName;
    private String extension; 
    private long sizeBytes;
    private double sizeMegabytes;
    private LocalDateTime creation;
    private LocalDateTime lastModified;
    private String checksum;
    private boolean hidden;
    private CustomMetadata metadata;
    private byte[] bytes;
    private int similarity;
    private String pathSimilarFile;
    private CustomFile similar;
    private String error = "";
    
    public CustomFile() {}
    
    public CustomFile(File file, String checkSumAlgorithm, CustomReadBuffer readBuffer) {
        Path path = file.toPath();
        BasicFileAttributes attr;
        creation = null;
        lastModified = null;
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
            creation = Utils.toLocalDateTime(attr.creationTime().toInstant());
            lastModified = Utils.toLocalDateTime(attr.lastModifiedTime().toInstant());
        } catch (IOException e1) {
            error = "Cannot read file attributes"+System.lineSeparator();
            e1.printStackTrace();
        }
        this.file = file;
        fileName = file.getName();
        extension = Utils.getFileExtension(file);
        bytes = Utils.readBytesFile(path.toString(),readBuffer, this);
        checksum = Utils.createChecksum(bytes,checkSumAlgorithm);
        parent = null;
        pathFileName = file.getAbsolutePath();
        folder = file.getParent();
        hidden = file.isHidden();
        sizeBytes = file.length();
        this.metadata = MetadataService.createMetadata(this);
        sizeMegabytes =  BigDecimal.valueOf((double) sizeBytes / (1024 * 1024)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    public static List<CustomFile> ToCustomFileList(List<File> files, String checkSumAlgorithm, CustomReadBuffer readBuffer){
        return files.stream().map(file -> new CustomFile(file,checkSumAlgorithm,readBuffer)).toList();
    }
    
    public InputStream readStream() {
        try {
             return new FileInputStream(file);
        } catch (FileNotFoundException e) {
        	this.error = ("Cannot read file");
        }
        return null;
    }
    
    public boolean hasError() {
    	return !this.error.isEmpty();
    }

    @Override
    public String toString() {
        return "CustomFile [checksum=" + checksum 
        		+ ", fileName=" + fileName 
        		+ ", folder=" + folder 
        		+ ", sizeBytes=" + sizeBytes 
        		+ ", sizeMegabytes=" + sizeMegabytes + "]";
    }

}
