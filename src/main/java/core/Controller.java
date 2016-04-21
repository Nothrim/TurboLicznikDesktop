package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.deploy.util.WinRegistry;
import gui.Record;
import gui.Time;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pdf.OutputPdfDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {
    public static String BAT_FILE_PATH;
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
    boolean start=false;
    long sum=0;
    private Map<String,List<Long>>map;
    public static Controller pointer;
    public void initialize(URL location, ResourceBundle resources) {

        pointer=this;
        path.setText(System.getProperty("user.home")+ File.separator+"data.json");
        load.setOnMouseClicked(e->{
            output.getChildren().clear();
            try {
                map=new Gson().fromJson(new FileReader(path.getText()),new TypeToken<Map<String, List<Long>>>() {}.getType());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            for(Map.Entry<String,List<Long>>entry : map.entrySet()) {
                sum+=entry.getValue().stream().mapToLong(Long::longValue).map(TimeUnit.MILLISECONDS::toMinutes).sum();
                output.getChildren().add(new Record(entry.getKey(),entry.getValue().stream().map(TimeUnit.MILLISECONDS::toMinutes)
                        .toArray()));
            }
            label=new Label(new Time(sum).toString());
            output.getChildren().add(label);
        });
        save.setOnMouseClicked(e->{
            if(map!=null)
            new OutputPdfDocument(output,"godziny "+new SimpleDateFormat("MMMM").format(new Date())+".pdf");
        });
        install.setOnMouseClicked(e->{
            String path=System.getProperty("user.dir") + "\\TurboLicznik.jar";
            WinRegistry.setStringValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "TurboLicznik autorun key", path);
        });
        stop.setOnMouseClicked(e->{
            if(start){
                stop.setText("Stop");

            }else {
                try {
                    new Socket(InetAddress.getByName(null), 8331);
                    stop.setText("Start");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            start=!start;
        });
    }

    public long getSum() {
        return sum;
    }

    public void calculateSum(){
        sum=0;
        output.getChildren().forEach(e->{
            if(e instanceof Record){
                try {
                    ((Record) e).fields.forEach(f -> sum += Long.parseLong(f.getText()));
                }catch (Exception ignored){}
            }
        });
        if(label!=null)label.setText(new Time(sum).toString());


    }

}