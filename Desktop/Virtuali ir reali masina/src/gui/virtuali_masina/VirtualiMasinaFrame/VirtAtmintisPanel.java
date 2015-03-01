/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.virtuali_masina.VirtualiMasinaFrame;

import gui.reali_masina.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout.Constraints;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import virtuali.ir.reali.masina.reali_masina.RealiMasina;
import zodis.DvigubasZodis;

/**
 *
 * @author unknown
 */
public class VirtAtmintisPanel extends JPanel{
    JTable table;
    DefaultTableModel model;    
    String columns[] = {"  ", "0", "1", "2", "3", "4", "5", "6", "7",
                            "8", "9", "A", "B", "C", "D", "E", "F"};
    String[][] data = new String[16][17];
    
    
    RealiMasina reali_masina;
    public VirtAtmintisPanel(RealiMasina reali_masina){
        this.reali_masina = reali_masina;
        sukurtiLentele();
    }
    
    public void sukurtiLentele(){
        TitledBorder border = BorderFactory.createTitledBorder("Atmintis");
        border.setTitleColor(Color.blue); 
        border.setTitleFont(new Font("", 4, 10));
        setBorder(border);

        uzpildytiData();
        
        table = new JTable(new DefaultTableModel(data, columns));
        table.setPreferredScrollableViewportSize(new Dimension(600,256));
//table.setFillsViewportHeight(true);
        model = (DefaultTableModel) table.getModel();        
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
    }
    
    public void uzpildytiData(){
        DvigubasZodis[] atmintis = reali_masina.getAtmintis();
        
        int k = 0;
        int k2 = 0;
        for (int i = 0; i < 16; i++){
            for(int j = 1; j <= 16; j++)
            {
                data[i][j] = String.copyValueOf(atmintis[k2].getDvigubasZodis());
                k2++;
            }
            data[i][0] = Integer.toHexString(k);
            k++;
        }        
    }
    
    public void atnaujintiLentele(){
        uzpildytiData();
        model.setDataVector(data, columns);
    }
    
}