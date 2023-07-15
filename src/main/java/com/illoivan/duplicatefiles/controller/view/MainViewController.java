
package com.illoivan.duplicatefiles.controller.view;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileSystemView;

import com.illoivan.duplicatefiles.MainApplicationJavaFx;
import com.illoivan.duplicatefiles.controller.CustomFileController;
import com.illoivan.duplicatefiles.controller.view.commons.CustomChangeListener;
import com.illoivan.duplicatefiles.controller.view.commons.CustomColumn;
import com.illoivan.duplicatefiles.model.CustomPathSearch;
import com.illoivan.duplicatefiles.model.FilterCustomFile;
import com.illoivan.duplicatefiles.repository.search.CustomMatch;
import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;
import com.illoivan.duplicatefiles.repository.search.IgnoreOptions;
import com.illoivan.duplicatefiles.repository.search.MP3MetadataSearch;
import com.illoivan.duplicatefiles.repository.search.MetadataSearch;
import com.illoivan.duplicatefiles.viewmodel.CustomFileDuplicateViewModel;
import com.illoivan.duplicatefiles.viewmodel.CustomFileViewModel;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewController implements Initializable {

    @FXML
    private Button btnAddMainFolder;
    @FXML
    private TextField txtMainFolder;
    @FXML
    private Button btnSearch;
    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private Tab tabExcludePath;
    @FXML
    private Label lblInfo;
    @FXML
    private CheckBox chkMatchName;
    @FXML
    private CheckBox chkMatchSize;
    @FXML
    private CheckBox chkMatchChecksum;
    @FXML
    private CheckBox chkIgnoreSystemFiles;
    @FXML
    private CheckBox chkIgnoreZeroBytes;
    @FXML
    private CheckBox chkIgnoreSizeOver;
    @FXML
    private CheckBox chkIgnoreSizeUnder;
    @FXML
    private Spinner<Integer> spinnerSizeOver;
    @FXML
    private Spinner<Integer> spinnerSizeUnder;
    @FXML
    private ListView<String> lstViewExcludePath;
    @FXML
    private Button btnAddToViewExcludePath;
    @FXML
    private VBox vBoxMp3Metadata;
    @FXML
    private CheckBox chkMp3Metadata;
    @FXML
    private CheckBox chkGenre;
    @FXML
    private CheckBox chkAlbum;
    @FXML
    private CheckBox chkArtist;
    @FXML
    private CheckBox chkTitle;
    @FXML
    private CheckBox chkDuration;
    @FXML
    private Button btnViewChecksum;
    @FXML
    ToggleGroup radioButtonAlgorithmGroup;
    @FXML
    private Spinner<Integer> spinnerBufferStart;
    @FXML
    private Spinner<Integer> spinnerBufferMax;
    @FXML
    private Spinner<Integer> spinnerSimilarity;
    @FXML
    private CheckBox chkShowSimilarity;
    
    private CustomFileController customFileController;
    private File previusPathSelected = null;
    private File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customFileController = new CustomFileController();
        btnAddMainFolder.setOnMouseClicked(e -> this.txtMainFolder.setText(selectDirectory()));
        txtMainFolder.textProperty().addListener(new CustomChangeListener(btnSearch));
        spinnerSizeOver.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spinnerSizeUnder.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spinnerBufferStart.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spinnerBufferMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spinnerSimilarity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

        chkIgnoreSizeOver.selectedProperty().addListener(new CustomChangeListener(spinnerSizeOver));
        chkIgnoreSizeUnder.selectedProperty().addListener(new CustomChangeListener(spinnerSizeUnder));

        spinnerSimilarity.valueProperty().addListener(new CustomChangeListener(chkShowSimilarity));
        
        btnViewChecksum.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(homeDirectory);
            showViewChecksumFileList(fileChooser.showOpenMultipleDialog(anchorPaneMain.getScene().getWindow()));
        });
    }

    public void btnSearch(){
        String pathMainFolder = txtMainFolder.getText();
        if(pathMainFolder.isBlank()) return;

        String checkSumAlgorithm = getSelectedAlgorithm();

        boolean isZeroBytes = chkIgnoreZeroBytes.isSelected();
        boolean isSystemFiles = chkIgnoreSystemFiles.isSelected();
        boolean isSizeUnder = chkIgnoreSizeUnder.isSelected();
        boolean isSizeOver = chkIgnoreSizeOver.isSelected();
        Integer valueSizeOver = spinnerSizeOver.getValue();
        int sizeOver = valueSizeOver != null && valueSizeOver > 0 ? valueSizeOver : 0;
        Integer valueSizeUnder = spinnerSizeUnder.getValue();
        int sizeUnder = valueSizeUnder != null && valueSizeUnder > 0 ? valueSizeUnder : 0;
        IgnoreOptions ignoreOptions = new IgnoreOptions(isZeroBytes,isSystemFiles,isSizeUnder,isSizeOver,sizeUnder,sizeOver );
        
        List<String> excludePaths = lstViewExcludePath.getItems();
        CustomPathSearch pathSearch = new CustomPathSearch(excludePaths);

        int bufferStart = spinnerBufferStart.getValue();
        int bufferMax =  spinnerBufferMax.getValue();
        CustomReadBuffer readBuffer = new CustomReadBuffer(bufferStart,bufferMax);
        
        MetadataSearch metadata = new MetadataSearch();
        metadata.setMp3Search(new MP3MetadataSearch(chkGenre.isSelected(),chkAlbum.isSelected(),chkArtist.isSelected(),chkTitle.isSelected(),chkDuration.isSelected()));
        
        boolean showSimilarity = chkShowSimilarity.isSelected();
        Integer valueSimilarity = spinnerSimilarity.getValue();
        int similarity =  valueSimilarity != null  && valueSimilarity > 0 ? valueSimilarity : 0;
        
        boolean isMatchName = chkMatchName.isSelected();
        boolean isMatchSize = chkMatchSize.isSelected();
        boolean isMatchChecksum = chkMatchChecksum.isSelected();
        CustomMatch matchBy = new CustomMatch(isMatchName,isMatchSize,isMatchChecksum ,showSimilarity,similarity);

        FilterCustomFile filter = new FilterCustomFile(matchBy,ignoreOptions,pathSearch,readBuffer,metadata,checkSumAlgorithm);
        List<CustomFileDuplicateViewModel> listDuplicate = customFileController.search(pathMainFolder,filter);
        
        FXMLLoader loader = MainApplicationJavaFx.getFXML("DuplicateFile");
        loader.setControllerFactory(controllerClass -> new DuplicateFileViewController(listDuplicate, similarity,showSimilarity));
        MainApplicationJavaFx.openNewWindow(loader);
    }

    public void addPath() {
        String path = selectDirectory();
        boolean isAdded = lstViewExcludePath.getItems().contains(path);
        if (path == null || isAdded) return;
        previusPathSelected = new File(path);
        lstViewExcludePath.getItems().addAll(path);
    }

    public void removePath() {
        ObservableList<Integer> indices = lstViewExcludePath.getSelectionModel().getSelectedIndices();
        indices.forEach(v -> lstViewExcludePath.getItems().remove((int) v));
    }

    public String selectDirectory() {
        DirectoryChooser dir = new DirectoryChooser();
        File folder = null;

        if (previusPathSelected != null) {
            folder = previusPathSelected;
        } else {
            folder = homeDirectory;
        }

        dir.setInitialDirectory(folder);
        Stage stage = (Stage) anchorPaneMain.getScene().getWindow();
        File file = dir.showDialog(stage);

        if (file != null) {
            folder = file;
            previusPathSelected = file;
        } else if (previusPathSelected != null) {
            folder = previusPathSelected;
        } else {
            return null;
        }

        return folder.getAbsolutePath();
    }

    
    public void showViewChecksumFileList(List<File> list){
        if(list == null) return;
        
        String checkSumAlgorithm = getSelectedAlgorithm();
        CustomReadBuffer readBuffer = new CustomReadBuffer(spinnerBufferStart.getValue(), spinnerBufferMax.getValue());

        List<CustomFileViewModel> files = this.customFileController.getCustomFiles(list,checkSumAlgorithm,readBuffer);
        FXMLLoader loader = MainApplicationJavaFx.getFXML("CustomView");
        loader.setControllerFactory(controllerClass -> new CustomViewController(CustomColumn.getCheckSumColumns(),files));
        MainApplicationJavaFx.openNewWindow(loader);
    }
    
    private String getSelectedAlgorithm() {
        RadioButton selectedRadioButton = (RadioButton) radioButtonAlgorithmGroup.getSelectedToggle();
        return selectedRadioButton.getText();
    }
    
}
