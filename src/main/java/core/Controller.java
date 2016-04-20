package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gui.Record;
import gui.Time;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Button install;
    @FXML
    Button load;
    @FXML
    Button save;
    @FXML
    Button stop;
    @FXML
    TextField path;
    @FXML
    VBox output;
    Label label;
    private Map<String,List<Long>>map;
    public void initialize(URL location, ResourceBundle resources) {
        path.setText(System.getProperty("user.home")+ File.separator+"data.json");
        load.setOnMouseClicked(e->{
            output.getChildren().clear();
            try {
                map=new Gson().fromJson(new FileReader(path.getText()),new TypeToken<Map<String, List<Long>>>() {}.getType());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            long sum=0;
            for(Map.Entry<String,List<Long>>entry : map.entrySet()) {
                sum+=entry.getValue().stream().mapToLong(Long::longValue).sum();
                output.getChildren().add(new Record(entry.getKey(), entry.getValue().toArray(new Long[entry.getValue().size()])));
            }
            label=new Label(new Time(sum).toString());
            output.getChildren().add(label);
        });
    }

}