package gui;

import core.Controller;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;

public class Record extends HBox{
    public TextField tDate;
    public ArrayList<TextField> fields=new ArrayList<>();
    public Record(String date, Object[] time){
        tDate=new TextField(date);
        tDate.setMinWidth(60);
        tDate.setPrefWidth(60);
        tDate.setMaxWidth(60);
        tDate.getStyleClass().add("text-field-generated");
        this.getChildren().add(tDate);
        Arrays.stream(time).forEach(e->{
            TextField field=new TextField(Long.toString((Long)e));
            field.setOnKeyReleased(key->{
                Controller.pointer.calculateSum();
            });
            fields.add(field);
            field.getStyleClass().add("text-field-generated2");
            field.setMaxWidth(115);
            field.setMinWidth(85);
            field.setPrefWidth(85);
            this.getChildren().add(field);
        });
    }
}
