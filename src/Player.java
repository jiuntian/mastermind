/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jiuntian
 */
public class Player implements Comparable<Player>{
    private final String name;
    private double score;
    
    public Player(String name){
        this.name = name;
    }
    public Player(String name, double score){
        this.name = name;
        this.score = score;
    }
    public void updateScore(double score){
        this.score = score;
    }
    @Override
    public int compareTo(Player o) {
        return (int)((o.score-this.score)*100); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getName(){
        return name;
    }
    public double getScore(){
        return score;
    }
    
}
