/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package virtuali.ir.reali.masina;

import gui.reali_masina.Frame;
import gui.virtuali_masina.VirtualiMasinaFrame.FrameVirt;
import javax.swing.JFrame;
import virtuali.ir.reali.masina.reali_masina.RealiMasina;

/**
 *
 * @author Dell
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        //po kolkas tik testavimo kodas
        //test1();
        RealiMasina r = new RealiMasina();  
//        r.LPRG("programa.txt");       
        //JFrame reali_masina_gui = new Frame(r);
        FrameVirt virtuali_masina_gui = new FrameVirt(r);
        //r.LPRG("programa.txt");  
        
        r.SR(1,2);
        r.SR(1,3);        
        virtuali_masina_gui.atnaujintiLentele();
    }
    /*
    public static void test1(){ //LPRG
        RealiMasina r = new RealiMasina();
        r.LPRG("programa.txt");
        for(int i = 0; i < 10; i++){
            System.out.println(r.getAtmintis()[r.getPTR()*10 + i].getDvigubasZodis());
        }
    }
    
    public static void test2(){ //CG
        RealiMasina r = new RealiMasina();
        r.CG(1,2);
        System.out.println(r.getMODE());
        System.out.println(r.getIC());
    }
    public static void test3() throws InterruptedException{ //LR , SR
        RealiMasina r = new RealiMasina();
        r.LPRG("programa.txt");
        r.LR(1,2);
        System.out.print("LR rezultatas : ");
        System.out.println(r.getR().getDvigubasZodis());
        System.out.print("pries SR : ");
        System.out.println(r.getAtmintis()[13].getDvigubasZodis());
        r.SR(1,3);
        System.out.print("po SR : ");
        System.out.println(r.getAtmintis()[13].getDvigubasZodis());
        
    }
    public static void test4(){ //AD
        RealiMasina r = new RealiMasina();
        r.LPRG("programa.txt");
        r.LR(0,0);
        System.out.print("R reiksme: ");
        System.out.println(r.getR().getDvigubasZodis());
        System.out.print("atminties reiksme: ");
        System.out.println(r.getAtmintis()[1].getDvigubasZodis());
        r.AD(0, 1);
        System.out.print("AD : ");
        System.out.println(r.getR().getDvigubasZodis());
    }
    public static void test5(){ //CR
        RealiMasina r = new RealiMasina();
        r.LPRG("programa.txt");
        r.LR(0, 1);
        System.out.print("R reiksme: ");
        System.out.println(r.getR().getDvigubasZodis());
        System.out.print("atminties reiksme: ");
        System.out.println(r.getAtmintis()[2].getDvigubasZodis());
        r.CR(0,2);
        System.out.println("C = " + r.getC());
    }
    public static void test6() throws InterruptedException{//atpazintiInstrukcijas
        //programa.txt
        //0011 / 0032 / 0001 / AD00 / BR10 / CG12
        RealiMasina r = new RealiMasina();
        r.LPRG("programa.txt");
        r.LR(1, 2);
        r.AtpazintiInstrukcija(r.getAtmintis()[13].getDvigubasZodis());
        System.out.print("R reiksme: ");
        System.out.println(r.getR().getDvigubasZodis());
        System.out.println("IC pries BR :" + r.getIC());
        r.AtpazintiInstrukcija(r.getAtmintis()[14].getDvigubasZodis());
        System.out.println("IC po BR :" + r.getIC());
        r.AtpazintiInstrukcija(r.getAtmintis()[15].getDvigubasZodis());
        System.out.println("po CG MODE: " + r.getMODE());
        System.out.println("IC: " + r.getIC());
    }*/
}
