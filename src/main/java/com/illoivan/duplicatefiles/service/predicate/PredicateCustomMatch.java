package com.illoivan.duplicatefiles.service.predicate;

import java.util.function.Predicate;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.repository.search.CustomMatch;

public class PredicateCustomMatch {

    private PredicateCustomMatch() {}
    
    public static Predicate<CustomFile> getFilter(CustomMatch customMatch, CustomFile customFile){
        Predicate<CustomFile> predicate = null;
        
        if(customMatch.isFileName()) {
            predicate = fileName(customFile);
        }
        
        if(customMatch.isFileChecksum()) {
            predicate = (predicate == null) ? checksum(customFile) : predicate.and(checksum(customFile));
        }
        
        if(customMatch.isFileSize()) {
            predicate = (predicate == null) ? syzeBytes(customFile) : predicate.and(syzeBytes(customFile));
        }
        
        if(customMatch.getSimilarity() > 0 && customFile.getSimilarity() > customMatch.getSimilarity()) {
        	int similarity = customMatch.getSimilarity();
        	predicate = (predicate == null) ? similarity(similarity) : predicate.and(similarity(similarity));
        }
        return predicate;
    }
    
    private static Predicate<CustomFile> fileName(CustomFile customFile){
        return cf -> cf.getFileName().equals(customFile.getFileName());
    }
    private static Predicate<CustomFile> checksum(CustomFile customFile){
        return cf -> cf.getChecksum().equals(customFile.getChecksum());
    }
    private static Predicate<CustomFile> syzeBytes(CustomFile customFile){
        return cf -> cf.getSizeBytes()==customFile.getSizeBytes();
    }
    private static Predicate<CustomFile> similarity(int similarity){
        return cf -> cf.getSimilarity() >= similarity;
    }
    
}
