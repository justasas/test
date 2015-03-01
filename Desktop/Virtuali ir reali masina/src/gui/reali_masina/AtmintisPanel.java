/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.reali_masina;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import virtuali.ir.reali.masina.reali_masina.RealiMasina;
import zodis.DvigubasZodis;

/**
 *
 * @author unknown
 */
public class AtmintisPanel extends JPanel{
    public AtmintisPanel(RealiMasina reali_masina){
        TitledBorder border = BorderFactory.createTitledBorder("Atmintis");
        border.setTitleColor(Color.blue); 
        border.setTitleFont(new Font("", 4, 10));
        setBorder(border);
        
        DvigubasZodis[] atmintis = reali_masina.getAtmintis();
        
        String[][] data = new String[atmintis.length/16][17];
        int k = 0;
        int k2 = 0;
        for (int i = 0; i < atmintis.length/16; i++){
            for(int j = 1; j <= 16; j++)
            {
                data[i][j] = String.copyValueOf(atmintis[k2].getDvigubasZodis());
                k2++;
            }
            data[i][0] = Integer.toHexString(k);
            k++;
        }
        
        String columns[] = {"  ", "0", "1", "2", "3", "4", "5", "6", "7",
                            "8", "9", "A", "B", "C", "D", "E", "F"};
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
    }
}