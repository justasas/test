/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package virtuali.ir.reali.masina.reali_masina;

import gui.virtuali_masina.VirtualiMasinaFrame.FrameVirt;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import zodis.DvigubasZodis;
import zodis.Zodis;

/**
 *
 * @author Dell
 */

public class RealiMasina implements Runnable{
    //atmintis
    private DvigubasZodis[] atmintis;
    private int[] laisvi_blokai = new int[100];
    private DvigubasZodis[] bendra_atmintis;
    
    //registrai
    private DvigubasZodis R;
    private DvigubasZodis R2;    
    private int IC;
    private int C;
    private int PTR;
    private boolean MODE;
    private char PI;
    private char SI;
    private char TI;
    private int[] SEM = new int[16];
    private int bendra;
    private int L; // loop
    
    public boolean zingsnis = false;
    public boolean nuolatinis = false;
    
    public RealiMasina(){
        atmintis = new DvigubasZodis[1800];
        for(int i = 0; i < 1800; i++){
            atmintis[i] = new DvigubasZodis();
        }
        R = new DvigubasZodis();
        R.setDvigubasZodis('a');
        R2 = new DvigubasZodis();
        L = 7;
    }
    
    //instrukcijos
    public void CG(int x, int y)
    {
        MODE = true;
        IC = 16 * x + y;
    }
    
    public void LPRG(String failo_vardas) {
        
        int bloko_nr = 0;
        int temp = 0;
        File file = new File(failo_vardas);
        String blokas ;
        int PTRx = 0; // igis reiksmes nuo 0 iki 9, rasant i atminti PTR adresu
        
        if(-1 == patikrinti_vieitos_uztektinuma()){
            System.out.println("neuztenka vietos");
            return; // iseina is metodo
        }
        
        PTR = gauti_laisva_bloka();
        
        try {

            Scanner sc = new Scanner(file);
            
            while (sc.hasNextLine()) {
                bloko_nr = gauti_laisva_bloka();
                atmintis[PTR*16 + PTRx].setDvigubasZodis(bloko_nr);
                PTRx++;
                blokas = sc.nextLine();
                System.out.println(blokas);
                Scanner pagalbinis = new Scanner(blokas);
                pagalbinis.useDelimiter(" / ");
                
                while (pagalbinis.hasNext()) {
                    if(temp > 9){
                        System.out.println("ERROR bando irasyt per daug zodziu i bloka");
                        return; //iseina is metodo
                    }
                    String s = pagalbinis.next();
                    System.out.println(s);
                    atmintis[bloko_nr*16 + temp].setDvigubasZodis( s.toCharArray());
                    System.out.println(atmintis[bloko_nr*10 + temp].getDvigubasZodis());
                    temp++; 
                }  
                pagalbinis.close();
                temp = 0;
            }
            sc.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void LR(int x, int y) throws InterruptedException{
        R.setDvigubasZodis(atmintis[x*16 + y].getDvigubasZodis());
        
        while(zingsnis == false && nuolatinis == false) Thread.sleep(500);
        zingsnis = false;        
    }
    
    public void SR(int x, int y) throws InterruptedException{
        atmintis[x*16 + y].setDvigubasZodis(R.getDvigubasZodis());

        while(zingsnis == false && nuolatinis == false) Thread.sleep(500);
        zingsnis = false;
    }
    
    public void AD(int x, int y) throws InterruptedException{
        R.setDvigubasZodis( R.getInt() + atmintis[x*16 + y].getInt());

        while(zingsnis == false && nuolatinis == false) Thread.sleep(500);
        zingsnis = false;    
    }
    
    public void CR(int x, int y){
        if(R.getInt() == atmintis[x*16 + y].getInt()){
            C = 0;
        }
        else if(R.getInt() < atmintis[x*16 + y].getInt()){
            C = -1;
        }
        else{
            C = 1;
        }
    }
    
    public void BR(int x, int y){
        if(C == 0){
            IC = x*16 + y;
        }
    }
    
    public void GO(int x, int y){
        IC = x*16 + y;
    }
    
    public void GOR2(){
        zingsnis = false;
        while (zingsnis == false){};        
        IC = R.getInt();
    }
    
    public int gauti_laisva_bloka(){
        for(int i = 0; i < 16; i++){
            if(laisvi_blokai[i] == 0){
                laisvi_blokai[i] = 1;
                return i; 
            }
        }
        return -1;
    }
    
    //ar yra 1 laisvu bloku kad tilptu programa ir 1 laisvo bloko pulsapiavimo lentelei
    public int patikrinti_vieitos_uztektinuma(){
        int vieta = 0;
        for(int i = 0; i < 16; i++){
            if(laisvi_blokai[i] == 0){
                vieta++;
            }
        }
        if(vieta >= 11){
            return 1;
        }
        else return -1;
    }
    
    public void AtpazintiInstrukcija(char[] instr) throws InterruptedException{
        if(instr[0]=='A' && instr[1] == 'D'){
            AD(instr[2] - 48, instr[3] - 48);
        }
        else if(instr[0]=='B' && instr[1] == 'R'){
            BR(instr[2] - 48, instr[3] - 48);
        }
        else if(instr[0]=='C' && instr[1] == 'G'){
            CG(instr[2] - 48, instr[3] - 48);
        }
        else if(instr[0]=='C' && instr[1] == 'R'){
            CR(instr[2] - 48, instr[3] - 48);
        }
        else if(instr[0]=='G' && instr[1] == 'O'){
            GO(instr[2] - 48, instr[3] - 48);
        }
        else if(instr[0]=='G' && instr[1] == 'O' && instr[2] == 'R' && instr[3] == '2'){
            GOR2();
        }
        else if(instr[0]=='L' && instr[1] == 'R'){
            LR(instr[2] - 48, instr[3] - 48);
        }
        else if(instr[0]=='S' && instr[1] == 'R'){
            SR(instr[2] - 48, instr[3] - 48);
        }
        zingsnis = false;
        while (zingsnis == false)
        {
            System.out.println("asdsa");
        }        
    }
    
    public DvigubasZodis getR(){
        return R;
    }

    public DvigubasZodis getR2(){
        return R2;
    }
    
    public boolean getMODE(){
        return MODE;
    }
    
    public int getIC(){
        return IC;
    }
    
    public DvigubasZodis[] getAtmintis(){
        return atmintis;
    }
    
    public int getPTR(){
        return PTR;
    }
    
    public int getC(){
        return C;
    }

    public int getSI(){
        return SI;
    }
    
    public int getTI(){
        return TI;
    }   
    
    public int getPI(){
        return PI;
    }    
    
    public int getBendra()
    {
        return bendra;
    }
    
    public int getL()
    {
        return L;
    }
    
    public int getSEM()[]
    {
        return SEM;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}