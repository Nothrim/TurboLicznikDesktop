package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gui.Record;
import gui.Time;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pdf.OutputPdfDocument;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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
    @FXML
    HBox buttonBox;
    Label label;
    boolean start=false;
    long sum=0;
    private Map<String,List<Long>>map;
    public static Controller pointer;
    public void initialize(URL location, ResourceBundle resources) {
        buttonBox.getChildren().stream().filter(e->e instanceof Button).forEach(e->{
            e.getStyleClass().add("button-normal");
            e.setOnMousePressed(ev->e.getStyleClass().add("button-clicked"));
            e.setOnMouseReleased(ev->e.getStyleClass().removeAll("button-clicked"));
        });
        String runPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        runPath = clearPathString(runPath);
        try {
            runPath= URLDecoder.decode(runPath, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        final String runPathFinal=runPath;
        pointer=this;
        path.setText(System.getProperty("user.home")+ File.separator+"data.json");
        load.setOnMouseClicked(e->{
            output.getChildren().clear();
            try {
                map=new Gson().fromJson(new FileReader(path.getText()),new TypeToken<Map<String, List<Long>>>() {}.getType());
            } catch (FileNotFoundException e1) {
              Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("File is missing");
                alert.setHeaderText(e1.getMessage());
                alert.setContentText("There is no file named data.json in "+path.getText());
                alert.showAndWait();
            }
            sum=0;
            for(Map.Entry<String,List<Long>>entry : map.entrySet()) {
                sum+=entry.getValue().stream().mapToLong(Long::longValue).map(TimeUnit.MILLISECONDS::toMinutes).sum();
                output.getChildren().add(new Record(entry.getKey(),entry.getValue().stream().map(TimeUnit.MILLISECONDS::toMinutes)
                        .toArray()));
            }
            label=new Label(new Time(sum).toString());
            output.getChildren().add(label);
            label.setStyle("-fx-text-fill:#F0F0F0");
        });
        save.setOnMouseClicked(e->{
            if(map!=null)try {
                new OutputPdfDocument(output, "godziny " + new SimpleDateFormat("MMMM").format(new Date()) + ".pdf");
            }catch (Exception e1){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("File is used");
                alert.setHeaderText(e1.getMessage());
                alert.setContentText("The PDF file is used by another program");
                alert.showAndWait();
            }
        });
        install.setOnMouseClicked(e->{
            try {
                Files.write(Paths.get(System.getProperty("user.home")+File.separator+"data.json"),"{}".getBytes());
            } catch (IOException e1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Can't create file");
                alert.setHeaderText(e1.getMessage());
                alert.showAndWait();
            }
            try {
                WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER,
                        "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "TurboLicznik autorun key", runPathFinal+"TurboLicznik.jar",1);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Can't access register");
                alert.setHeaderText(e1.getMessage());
                alert.setContentText("Try running this program as administrator to make changes in register");
                alert.showAndWait();
            }
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Done");
            alert.setHeaderText("Installation completed successfully");
            alert.showAndWait();
        });
        stop.setOnMouseClicked(e->{
            if(start){
                stop.setText("Stop");
                try {

                    //String command="java -jar " +path.substring(0,getSlashIndex(path)) +"TurboLicznik.jar";
                    Runtime.getRuntime().exec("java -jar \""+runPathFinal+"TurboLicznik.jar\"");
                } catch (IOException e1) {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Tray application was already running");
                    alert.setHeaderText(e1.getMessage());
                    alert.showAndWait();
                }
            }else {
                try {
                    stop.setText("Start");
                    new Socket(InetAddress.getByName(null), 8331);
                } catch (IOException e1) {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Can't find tray application");
                    alert.setHeaderText(e1.getMessage());
                    alert.setContentText("Probably tray application hasn't been loaded properly, try to re run it");
                    alert.showAndWait();

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
    private int getSlashIndex(String s){
        int index=0;
        for(int i=s.length()-1;i>0;i--){
            if(s.charAt(i)=='/')return i+1;
        }
        return index;
    }
    public static String clearPathString(String in)
    {
        StringBuilder sb=new StringBuilder(in);
        if(System.getProperty("os.name").contains("Windows") && sb.charAt(0)=='/')sb.deleteCharAt(0);
        for(int i=sb.length()-1;i>0;i--)
        {
            if(sb.charAt(i)!='/')sb.deleteCharAt(i);
            else
                return sb.toString();
        }
        return sb.toString();
    }
}