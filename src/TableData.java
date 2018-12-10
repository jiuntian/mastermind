
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jiuntian
 */
public class TableData implements Comparable<TableData>{
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty score;
    
    public TableData(String name, int score){
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleIntegerProperty(score);
    }
    
    public String getName(){
        return name.get();
    }
    public void setName(String name){
        this.name.set(name);
    }
    public int getScore(){
        return score.get();
    }
    public void setScore(int score){
        this.score.set(score);
    }

    @Override
    public int compareTo(TableData o) {
        Integer v1 = this.score.get();
        Integer v2 = o.getScore();
        return v2.compareTo(v1); //To change body of generated methods, choose Tools | Templates.
    }

}
