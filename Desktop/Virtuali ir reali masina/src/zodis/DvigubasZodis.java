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
public class DvigubasZodis {
    final int size = 4;
    private char[] dvigubas_zodis;
    
    public DvigubasZodis(){
        dvigubas_zodis = new char[size];
    }
    
    public void setDvigubasZodis(char[] dvigubas_zodis){
        if(dvigubas_zodis.length > 4){
            System.out.println("ERROR priskiriamas per ilgas string i dviguba zodi" );
        }
        else{
            this.dvigubas_zodis = dvigubas_zodis;
        }
        
    }
    public void setDvigubasZodis(int skaicius){
        char[] chars = ("" + skaicius).toCharArray();
        if(chars.length > 4){
            System.out.println("ERROR priskiriamas per ilgas int i dviguba zodi" );
        }
        else{
            this.dvigubas_zodis = chars;
        }
    }
    
    public char[] getDvigubasZodis(){
        return dvigubas_zodis;
    }
    
    public int getInt(){
        int x = -1;
        try{
             x = Integer.parseInt(new String(dvigubas_zodis));
        }
        catch(NumberFormatException e){
            System.out.println("ERROR bando atlikti aritmetini veiksma ne su skaiciumi");
        }
        return x;
    }
}
