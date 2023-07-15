package com.illoivan.duplicatefiles.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.model.FilterCustomFile;
import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;
import com.illoivan.duplicatefiles.service.CustomFileDuplicateService;
import com.illoivan.duplicatefiles.viewmodel.CustomFileDuplicateViewModel;
import com.illoivan.duplicatefiles.viewmodel.CustomFileViewModel;

public class CustomFileController {

    private CustomFileDuplicateService duplicateService;

    public CustomFileController() {
        duplicateService = new CustomFileDuplicateService();
    }

    public List<CustomFileDuplicateViewModel> search(String path, FilterCustomFile filter) {
        Map<CustomFile, List<CustomFile>> map = duplicateService.search(path,filter);
        return CustomFileDuplicateViewModel.ToViewList(map);
    }

    public List<CustomFileViewModel> getCustomFiles(List<File> list, String checkSumAlgorithm, CustomReadBuffer readBuffer) {
    	return CustomFileViewModel.ToViewList(duplicateService.getAllFiles(list,checkSumAlgorithm, readBuffer));
    }

    public List<CustomFileViewModel> deleteFiles(List<CustomFileViewModel> viewModel){
        List<CustomFile> list = CustomFileViewModel.ToModel(viewModel);
        return CustomFileViewModel.ToViewList(duplicateService.deleteCustomFile(list));
    }
}
