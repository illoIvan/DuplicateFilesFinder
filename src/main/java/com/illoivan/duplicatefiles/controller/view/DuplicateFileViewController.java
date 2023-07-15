
package com.illoivan.duplicatefiles.controller.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.illoivan.duplicatefiles.MainApplicationJavaFx;
import com.illoivan.duplicatefiles.controller.CustomFileController;
import com.illoivan.duplicatefiles.controller.view.commons.CustomColumn;
import com.illoivan.duplicatefiles.model.CustomTableColumn;
import com.illoivan.duplicatefiles.model.CustomTableView;
import com.illoivan.duplicatefiles.model.UtilsAlert;
import com.illoivan.duplicatefiles.viewmodel.CustomFileDuplicateViewModel;
import com.illoivan.duplicatefiles.viewmodel.CustomFileViewModel;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class DuplicateFileViewController implements Initializable {

    @FXML
    private CustomTableView tableView;
    
    @FXML
    private Button btnDelete;

    private ObservableList<CustomFileDuplicateViewModel> listDuplicate;

    private CustomFileController controller;
    private int similarityValue;
    private boolean showSimilarity;
    
    public DuplicateFileViewController(List<CustomFileDuplicateViewModel> listDuplicate, int similarity, boolean showSimilarity) {
        this.listDuplicate = FXCollections.observableArrayList(listDuplicate);
        controller = new CustomFileController();
        this.similarityValue = similarity;
        this.showSimilarity = showSimilarity;
    }

	@Override
    public void initialize(URL location, ResourceBundle resources) {
		loadColumns();
		tableView.setTableItems(listDuplicate);
		tableView.assingsColorByList("","");
        tableView.setEditable(true); //need this for checkbox
        tableView.autosize();
        
        TableViewSelectionModel<CustomFileViewModel> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel.setCellSelectionEnabled(true);
        
        //contextMenu
        MenuItem menuItem = new MenuItem("Open Folder File");
        menuItem.setOnAction((ActionEvent event) -> {
            CustomFileViewModel item = selectionModel.getSelectedItem();
            try {
                Desktop.getDesktop().open(new File(item.getFolder()));
            } catch (IOException e) {
            	UtilsAlert.error("Cannot open folder file "+item.getPathFileName());
                e.printStackTrace();
            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItem);
        tableView.setContextMenu(menu);

        tableView.setRowFactory(row -> new TableRow<CustomFileViewModel>() {
            @Override
            protected void updateItem(CustomFileViewModel item, boolean empty) {
                super.updateItem(item, empty);
                
                if(item != null) {
                    if(!item.getCss().isEmpty()) {
                        setStyle(item.getCss());
                    }
                    if(item.getMetadata() != null) {
                    	Tooltip tooltip = new Tooltip(item.getMetadata());
                    	tooltip.setHideDelay(new Duration(2000));
                    	tooltip.setAutoHide(true);
                        setTooltip(tooltip);    
                    }else if(item.hasError()) {
                    	item.setFileName(item.getError());
                    }
                }else {
                	setStyle("");
                }
            }
        });
    }
    
    private void loadColumns() {
    	CustomTableColumn<CustomFileViewModel, Number> index = new CustomTableColumn<>("Index", "index");
        index.setStyle("-fx-alignment: CENTER;");
    	index.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(tableView.getItems().indexOf(column.getValue())+1));
        index.setSortable(false);
        tableView.addColumn(index);
        
        CustomTableColumn<CustomFileViewModel, Boolean> delete = new CustomTableColumn<>("Delete","selected");
        delete.addCheckbox();
        tableView.addColumn(delete); 
        tableView.addColumn(new CustomTableColumn<>("FileName", "fileName"));
        tableView.addColumn(new CustomTableColumn<>("Folder", "folder"));
        tableView.addColumn(new CustomTableColumn<>("Size", "size"));
        tableView.addColumn(new CustomTableColumn<>("Checksum", "checksum"));
        
        CustomTableColumn<CustomFileViewModel, Integer> similarity = new CustomTableColumn<>("Similarity", "similarity");
        tableView.addColumn(similarity);

        similarity.setCellFactory(param -> new TableCell<CustomFileViewModel, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(showSimilarity ? (Integer.toString(item) + "%") : "");
                    if (item > similarityValue) {
                        setStyle("-fx-background-color: green;");
                    } else if(item > 0 && item == similarityValue) {
                        setStyle("-fx-background-color: yellow;");
                    }else if(showSimilarity){
                        setStyle("-fx-background-color: red;");
                    }
                }
            }
            
        });
        
        tableView.addColumn(new CustomTableColumn<>("CreationDate", "creationDate"));
        tableView.addColumn(new CustomTableColumn<>("LastModifiedDate", "lastModifiedDate"));
    }  
    
    
    public void btnDelete() {
        ObservableList<CustomFileViewModel> list = tableView.getItems().filtered(cf -> cf.getSelected().get());

        if (list.isEmpty()) {
        	return;
        }

        boolean acceptButton = UtilsAlert.showAndWaitConfirmation("DELETE FILES?","Are you sure you want to delete the files?");
        
        if(!acceptButton) return;

        List<CustomFileViewModel> errorList = controller.deleteFiles(list);
        if(errorList.isEmpty()) {
        	UtilsAlert.info("All files have been deleted");
        	this.tableView.removeCustomFiles(list);        	
        }else {
        	showErrorFileList(errorList);        	
        }
    }

    public void showErrorFileList(List<CustomFileViewModel> list){
    	UtilsAlert.showErrorAndWait("Errors were found and some files could not be deleted");
        FXMLLoader loader = MainApplicationJavaFx.getFXML("CustomView");
        loader.setControllerFactory(controllerClass -> new CustomViewController(CustomColumn.getErrorColumns(),list));
        MainApplicationJavaFx.openNewWindow(loader);
    }

}
