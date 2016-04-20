package gui;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Arrays;

public class Record extends HBox{
    public Record(String date,Long[] time){
        TextField tDate=new TextField(date);
        tDate.setMinWidth(60);
        tDate.setPrefWidth(60);
        tDate.setMaxWidth(60);
        tDate.getStyleClass().add("text-field-generated");
        this.getChildren().add(tDate);
        Arrays.stream(time).forEach(e->{
            TextField field=new TextField(Long.toString(e));
            field.getStyleClass().add("text-field-generated2");
            field.setMaxWidth(115);
            field.setMinWidth(85);
            field.setPrefWidth(85);
            this.getChildren().add(field);
        });
    }
}
