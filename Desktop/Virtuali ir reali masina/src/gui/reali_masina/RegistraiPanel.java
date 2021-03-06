/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.reali_masina;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import virtuali.ir.reali.masina.reali_masina.RealiMasina;

/**
 *
 * @author unknown
 */

public class RegistraiPanel extends JPanel
    implements ActionListener {

    JTextField[] registrai;
    String[] registru_vardai = {"R", "R2", "C", "IC",
        "PTR", "MODE", "PI", "SI", "TI", "SEM", "BENDR", "L"};
    int f = 0;
    
    public RegistraiPanel(RealiMasina reali_masina)
    {
        GridLayout layout = new GridLayout(6,6);
        setLayout(layout);
        TitledBorder border = BorderFactory.createTitledBorder("Registrai");
        border.setTitleColor(Color.blue); 
        border.setTitleFont(new Font("", 4, 10));
        setBorder(border);
        
        registrai = new JTextField[registru_vardai.length];
        registrai[0] = new JTextField(String.copyValueOf(
                reali_masina.getR().getDvigubasZodis()), 4);
        registrai[1] = new JTextField(String.copyValueOf(
                reali_masina.getR2().getDvigubasZodis()), 4);
        registrai[2] = new JTextField(String.valueOf(
                reali_masina.getC()), 4);
        registrai[3] = new JTextField(String.valueOf(
                reali_masina.getIC()), 4);
        registrai[4] = new JTextField(String.valueOf(
                reali_masina.getPTR()), 4);
        registrai[5] = new JTextField(String.valueOf(
                reali_masina.getMODE()), 4);
        registrai[6] = new JTextField(String.valueOf(
                reali_masina.getPI()), 4);        
        registrai[7] = new JTextField(String.valueOf(
                reali_masina.getSI()), 4);        
        registrai[8] = new JTextField(String.valueOf(
                reali_masina.getTI()), 4);        
        registrai[10] = new JTextField(String.valueOf(
                reali_masina.getBendra()), 4);        
        registrai[11] = new JTextField(String.valueOf(
                reali_masina.getL()), 4);        
        registrai[9] = new JTextField(String.valueOf(
                reali_masina.getSEM()), 4);        

        
        for (int i = 0; i < registru_vardai.length; i++)
        {       
            JLabel label = new JLabel(registru_vardai[i]);
            label.setLabelFor(registrai[i]);
            label.setVisible(true);            
            add(label);
            add(registrai[i]);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
