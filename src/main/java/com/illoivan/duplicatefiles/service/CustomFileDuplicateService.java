package com.illoivan.duplicatefiles.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.model.FilterCustomFile;
import com.illoivan.duplicatefiles.model.MP3Metadata;
import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;
import com.illoivan.duplicatefiles.repository.search.IgnoreOptions;
import com.illoivan.duplicatefiles.service.predicate.PredicateCustomMatch;
import com.illoivan.duplicatefiles.service.predicate.PredicateMP3Metadata;
import com.illoivan.duplicatefiles.utils.LoggerUtils;

public class CustomFileDuplicateService {
	
	private CustomFileService fileService;

	public CustomFileDuplicateService() {
		this.fileService = new CustomFileService();
	}
	
	public Map<CustomFile, List<CustomFile>> search(String path, FilterCustomFile filter) {

		LoggerUtils.info("ALGORITHM= " + filter.getCheckSumAlgorithm());
		LoggerUtils.info("PATH= " + path);
		
		IgnoreOptions ignoreOptions = filter.getIgnoreOptions();
		LoggerUtils.info(ignoreOptions.toString());
		
		LoggerUtils.info(filter.getMatchBy().toString());
		LoggerUtils.info(filter.getCustomReadBuffer().toString());
		List<String> excludePaths = filter.getPathSearch().getExcludePaths();
		LoggerUtils.info("EXCLUDE_PATHS=[ " + String.join(",", excludePaths) + "]");			
		fileService.filterByIgnoreOptions(ignoreOptions);
		fileService.filterByExcludePath(excludePaths);
		
		List<CustomFile> fileList = fileService.execute(path, filter.getCheckSumAlgorithm(),filter.getCustomReadBuffer());

		Map<CustomFile, List<CustomFile>> mapCustomFiles = buildCustomFiles(fileList,filter);

		LoggerUtils.info("START build duplicates");
		//takes the duplicates, only those that are more than once in the list
		Map<CustomFile, List<CustomFile>> duplicates = new LinkedHashMap<>(); 
		for (Map.Entry<CustomFile, List<CustomFile>> cfEntry : mapCustomFiles.entrySet()) {
			if(cfEntry.getValue().size() > 1) {
				duplicates.put(cfEntry.getKey(), cfEntry.getValue());
			}
		}
		LoggerUtils.info("END build duplicates");
		
		return duplicates;
	}
       
    private Map<CustomFile, List<CustomFile>> buildCustomFiles(List<CustomFile> fileList, FilterCustomFile filter) {
        LoggerUtils.info("START buildCustomFiles");
        Map<CustomFile, List<CustomFile>> mapCustomFiles = new HashMap<>();

        if(fileList.isEmpty()) {
        	return mapCustomFiles;
        }
        
		if(filter.getMatchBy().isShowSimilarity()) {
			calculateSimilarity(fileList);
		}
        

        for (CustomFile customFile : fileList) {
            Predicate<CustomFile> searchPredicate = getSearchPredicates(customFile, filter);
            if (searchPredicate != null) {
                CustomFile key = mapCustomFiles.keySet().stream()
                        .filter(searchPredicate)
                        .findFirst()
                        .orElse(customFile);
                mapCustomFiles.computeIfAbsent(key, k -> new ArrayList<>()).add(customFile);
            } else {
                mapCustomFiles.computeIfAbsent(customFile, k -> new ArrayList<>(List.of(customFile)));
            }
        }

        LoggerUtils.info("END buildCustomFiles");
        return mapCustomFiles;
    }
    
	private Predicate<CustomFile> getSearchPredicates(CustomFile customFile, FilterCustomFile filter) {
		Predicate<CustomFile> predicate = null;

		Predicate<CustomFile> predicateMatchBy = PredicateCustomMatch.getFilter(filter.getMatchBy(), customFile);
		if (predicateMatchBy != null) {
			predicate = predicateMatchBy;
		}
		Predicate<CustomFile> predicateMP3 = null;
		if(customFile.getMetadata() instanceof MP3Metadata) {
			predicateMP3 = PredicateMP3Metadata.getFilter(filter.getMetadataSearch().getMp3Search(), customFile);	
		}
		
		if(predicateMP3 != null) {
			predicate = (predicate == null) ? predicateMP3 : predicate.and(predicateMP3);
		}

		return predicate;
	}

	public List<CustomFile> deleteCustomFile(List<CustomFile> list) {
		
		List<CustomFile> errorFiles = new ArrayList<>();
		errorFiles.clear();
		// check if can delete files (write permission)
		for (CustomFile customFile : list) {
			
			if(!customFile.getFile().exists()) {
				customFile.setError("The file does not exist");
				errorFiles.add(customFile);
			}else if (!customFile.getFile().canWrite()) {
				customFile.setError("Cannot delete file");
				errorFiles.add(customFile);
			}
		}

		for (CustomFile customFile : list) {
			
			if(customFile.hasError()) {
				continue;
			}

			try {
				Files.delete(customFile.getFile().toPath());
			} catch (NoSuchFileException e) {
				customFile.setError("File not exist");
				errorFiles.add(customFile);
			} catch (IOException e) {
				customFile.setError("Invalid permissions");
				errorFiles.add(customFile);
			}
		}
		return errorFiles;
	}
	
	private void calculateSimilarity(List<CustomFile> fileList) {
		LoggerUtils.info("START calculateSimilarity");
	    for (CustomFile cf1 : fileList) {
	    	int maxScore = 0;
	    	for (CustomFile cf2 : fileList) {
		        if (cf1.equals(cf2)) {
		        	continue;
		        }
	            int similarityScore = getScoreSimilarity(cf1, cf2);
	            if (similarityScore > maxScore) {
	                maxScore = similarityScore;
	                cf1.setSimilarity(maxScore);
	                cf1.setSimilar(cf2);
	            }
	    	}
	    }
	    LoggerUtils.info("END calculateSimilarity");
	}

    private int getScoreSimilarity(CustomFile cf1, CustomFile cf2) {
        byte[] data1 = cf1.getBytes();
		byte[] data2 = cf2.getBytes();
		int score = 0;
		for (int i = 0; i < Math.min(data1.length, data2.length); i++) {
		    if (data1[i] == data2[i]) {
		        score++;
		    }
		}

		//calculate similarity percentage
		int max = Math.max(data1.length, data2.length);
		return  (score * 100 ) / max;
    }
    
    
    public List<CustomFile> getAllFiles(List<File> list,String checkSumAlgorithm, CustomReadBuffer readBuffer){
    	List<CustomFile> customFiles = new ArrayList<>();
    	
    	list.forEach(file -> {
        	
    		if (file.isDirectory()) {
    			for(File f : file.listFiles()) {
    				customFiles.add(new CustomFile(f,checkSumAlgorithm,readBuffer));
    			}
    		}else {
    			customFiles.add(new CustomFile(file,checkSumAlgorithm,readBuffer));
    		}
    	});

    	return customFiles;
    	
    }

}
