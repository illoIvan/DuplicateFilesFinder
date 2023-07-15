package com.illoivan.duplicatefiles.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.illoivan.duplicatefiles.model.CustomFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomFileDuplicateViewModel{
    private List<CustomFileViewModel> duplicate;
    private CustomFileViewModel root;
    
    
    public static CustomFileDuplicateViewModel ToView(CustomFile customFile, List<CustomFile> duplicate) {
        CustomFileDuplicateViewModel view = new CustomFileDuplicateViewModel();

        view.setRoot(CustomFileViewModel.ToView(customFile));
        view.setDuplicate(CustomFileViewModel.ToViewList(duplicate));
        return view;
    }   
    
    public static List<CustomFileDuplicateViewModel> ToViewList(Map<CustomFile, List<CustomFile>> map){
    	List<CustomFileDuplicateViewModel> list = new ArrayList<>();
    	for (Map.Entry<CustomFile, List<CustomFile>> cfEntry : map.entrySet()) {
    		CustomFileDuplicateViewModel view = ToView(cfEntry.getKey(),cfEntry.getValue());
			list.add(view);
    	}
    	return list;
    }
}
