/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.reali_masina;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout.Constraints;
import javax.swing.border.EmptyBorder;
import virtuali.ir.reali.masina.reali_masina.RealiMasina;

/**
 *
 * @author unknown
 */
public class Frame extends JFrame {
    
    JPanel p = new JPanel();
    JPanel registrai_panel;
    JPanel atmintis_panel;
    JPanel something_panel = new JPanel();
    
    
    // Constructor
    public Frame(RealiMasina reali_masina) {
        this.registrai_panel = new RegistraiPanel(reali_masina);
        this.atmintis_panel = new AtmintisPanel(reali_masina);
        setTitle("Reali masina");
        setSize(750, 550);
        //setLayout(new GridBagLayout());
        setLocation(0, 0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        p.add(registrai_panel);
        p.add(atmintis_panel);
        
        add(p, BorderLayout.WEST);
            
    }   
    
    public void change()
    {
        removeAll();
    }
}
