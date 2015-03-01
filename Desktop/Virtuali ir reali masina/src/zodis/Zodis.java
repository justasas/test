/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zodis;

/**
 *
 * @author Dell
 */
public class Zodis {
    final int size = 2;
    private char[] zodis;
    
    Zodis(){
        zodis = new char[size];
    }
    
    public void setZodis(char[] zodis){
        if(zodis.length > 2){
            System.out.println("ERROR priskiriamas per ilgas string i zodi" );
        }
        else{
            this.zodis = zodis;
        }
    }
    
    public Zodis getZodis(){
        return this;
    }
    
}
