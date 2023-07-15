package com.illoivan.duplicatefiles.service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.repository.CustomFileRepository;
import com.illoivan.duplicatefiles.repository.search.CustomMatch;
import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;
import com.illoivan.duplicatefiles.repository.search.IgnoreOptions;
import com.illoivan.duplicatefiles.utils.CustomRegex;
import com.illoivan.duplicatefiles.utils.LoggerUtils;

public class CustomFileService {
    
    private CustomFileRepository repository;
    private Predicate<CustomFile> predicate;
    
    public Predicate<CustomFile> getPredicate() {
		return predicate;
	}

	public CustomFileService() {
        predicate = null;
        repository = new CustomFileRepository();
    }
    
    public List<CustomFile> execute(String path, String algorithm, CustomReadBuffer readBuffer){
    	LoggerUtils.info("START execute");
        List<CustomFile> list = repository.searchFiles(path,algorithm,readBuffer);
        if(!list.isEmpty() && predicate != null) {
            list = list.stream().filter(predicate).collect(Collectors.toList());
        }
        predicate = null;
        LoggerUtils.info("END execute");
        return list;
    }
    
    public void filterByFileName(String fileName){
        this.addFilter(getPredicateFileName(fileName));
    }

    public void filterByExtension(String... extensions){
        Predicate<CustomFile> filter  = (CustomFile cf) -> cf.getExtension().matches(CustomRegex.stringContains(extensions));
        this.addFilter(filter);
    }

    public void filterByExactMBSize(long size){
        Predicate<CustomFile> filter = (CustomFile cf) -> cf.getSizeMegabytes() == size;
        this.addFilter(filter);
    }

    public void filterByIgnoreOptions(IgnoreOptions ignore) {
        
        if(ignore.isSizeOver() && ignore.getSizeOver() > 0) {
            this.addFilter((CustomFile cf) -> cf.getSizeMegabytes() >= ignore.getSizeOver());
        }
        
        if(ignore.isSizeUnder() && ignore.getSizeUnder() > 0) {
            this.addFilter((CustomFile cf) -> cf.getSizeMegabytes() <= ignore.getSizeUnder());
        }
        
        if(ignore.isZeroBytes()) {
            this.addFilter((CustomFile cf ) -> cf.getSizeBytes() > 0);
        }
        
        if(ignore.isSystemFiles()) {
            this.addFilter((CustomFile cf) -> !cf.getFile().isHidden());
        }
    }

    public void filterByExcludePath(List<String> excludePaths) {
    	if(!excludePaths.isEmpty()) {
    		Predicate<CustomFile> filter = (CustomFile cf) -> !excludePaths.contains(cf.getFolder());
    		this.addFilter(filter);    		
    	}
    }
    
    public void filterByFileNamePattern(String pattern) {
        Predicate<CustomFile> filter = (CustomFile cf) -> cf.getFileName().matches(pattern);
        this.addFilter(filter);
    }
    
    public Predicate<CustomFile> getPredicateChecksum(String checkSum) {
        return (CustomFile cf) -> cf.getChecksum().equals(checkSum);
    }
    
    public Predicate<CustomFile> getPredicateExactSize(long size) {
        return (CustomFile cf) -> cf.getSizeBytes() == size;
    }
    
    public Predicate<CustomFile> getPredicateFileName(String fileName) {
        return (CustomFile cf) -> cf.getFileName().matches(fileName);
    }

    public Predicate<CustomFile> filterByCustomMatch(CustomMatch matchBy,CustomFile cfMain) {
        Predicate<CustomFile> matchPredicate = null;
        if(matchBy.isFileChecksum()) {
            Predicate<CustomFile> checksum = (CustomFile cf) -> cf.getChecksum().equals(cfMain.getChecksum());
            matchPredicate = checksum;
        }
        
        if(matchBy.isFileName()) {
            Predicate<CustomFile> fileName = (CustomFile cf) -> cf.getFileName().equals(cfMain.getFileName());
            matchPredicate = (matchPredicate == null) ? fileName : matchPredicate.and(fileName);
        }
        
        if(matchBy.isFileSize()) {
            Predicate<CustomFile> exactSize = (CustomFile cf) -> cf.getSizeBytes() == (cfMain.getSizeBytes());
            matchPredicate = (matchPredicate == null) ? exactSize : matchPredicate.and(exactSize);
        }
        return matchPredicate;
    }


    private void addFilter(Predicate<CustomFile> filter) {
        predicate = (predicate == null) ? filter : predicate.and(filter);
    }


}
