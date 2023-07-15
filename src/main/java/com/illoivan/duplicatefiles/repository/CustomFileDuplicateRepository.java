package com.illoivan.duplicatefiles.repository;

import java.util.List;

import com.illoivan.duplicatefiles.model.CustomFile;

public class CustomFileDuplicateRepository {
    
    public List<CustomFile> findByFileName(List<CustomFile> list, String value){
        return list.stream().filter(cf -> cf.getFileName().equals(value)).toList();
    }
    
    public List<CustomFile> findByChecksum(List<CustomFile> list, String value){
        return list.stream().filter(cf -> cf.getChecksum().equals(value)).toList();
    }
    
    public List<CustomFile> findByMBSize(List<CustomFile> list, long value){
        return list.stream().filter(cf -> cf.getSizeMegabytes() == value).toList();
    }   
 
}
