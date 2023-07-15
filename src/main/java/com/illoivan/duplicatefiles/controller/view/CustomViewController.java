package com.illoivan.duplicatefiles.controller.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.illoivan.duplicatefiles.model.CustomTableColumn;
import com.illoivan.duplicatefiles.model.CustomTableView;
import com.illoivan.duplicatefiles.viewmodel.CustomFileViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class CustomViewController implements Initializable{

    private ObservableList<CustomFileViewModel> files;

    @FXML
    private CustomTableView tableView;
    private List<CustomTableColumn<CustomFileViewModel,Object>> columns;
    
    
    public CustomViewController(List<CustomTableColumn<CustomFileViewModel,Object>> columns,
    		List<CustomFileViewModel> list) {
        this.files = FXCollections.observableArrayList(list);
        this.columns = columns;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	columns.forEach(column -> tableView.addColumn(column));
        tableView.setItems(files);
        tableView.autosize();
    }

}
