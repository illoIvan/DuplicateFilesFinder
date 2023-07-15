package com.illoivan.duplicatefiles.viewmodel;

import java.io.File;
import java.util.List;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.model.CustomMetadata;
import com.illoivan.duplicatefiles.utils.Utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomFileViewModel extends ViewModelBase{

    private CustomFileViewModel parent;
    private String fileName;
    private String folder;
    private String pathFileName;
    private String extension; 
    private double size;
    private String creationDate;
    private String lastModifiedDate;
    private String checksum;
    private boolean delete;
    private String metadata;
    private File file;
    private int similarity;
    private String pathSimilarFile;
    private String error = "";
    
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    public static CustomFileViewModel ToView(CustomFile customFile) {
        CustomFileViewModel view = new CustomFileViewModel();
        if(customFile.getParent() != null) {
            view.setParent(ToView(customFile));
        }
        view.setPathFileName(customFile.getPathFileName());
        view.setFolder(customFile.getFolder());
        view.setCreationDate(Utils.localDateTimeToString(customFile.getCreation()));
        view.setLastModifiedDate(Utils.localDateTimeToString(customFile.getLastModified()));
        view.setFileName(customFile.getFileName());
        view.setExtension(customFile.getExtension());
        view.setChecksum(customFile.getChecksum());
        view.setSize(customFile.getSizeMegabytes());
        CustomMetadata metadata = customFile.getMetadata();
        view.setMetadata(metadata != null ? metadata.getDescription():"");
        view.setDelete(false);
        view.setFile(customFile.getFile());
        view.setSimilarity(customFile.getSimilarity());
        view.setPathSimilarFile(customFile.getPathSimilarFile());
        view.setError(!customFile.getError().isEmpty() ? customFile.getError() : "");
        return view;
    }
    
    public static List<CustomFileViewModel> ToViewList(List<CustomFile> customFileList){
        return customFileList.parallelStream().map(CustomFileViewModel::ToView).toList();
    }    
    
    public static CustomFile ToModel(CustomFileViewModel customFile) {
        //only for delete file
        CustomFile model = new CustomFile();
        model.setPathFileName(customFile.getPathFileName());
        model.setFolder(customFile.getFolder());
        model.setFile(customFile.getFile());
        model.setFileName(customFile.getFileName());
        return model;
    }   

    public static List<CustomFile> ToModel(List<CustomFileViewModel> viewModel){
        return viewModel.parallelStream().map(CustomFileViewModel::ToModel).toList();
    }
    
    public boolean hasError() {
    	return !this.getError().isEmpty();
    }
    
    
}
