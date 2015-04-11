/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.virtuali_masina.VirtualiMasinaFrame;


import gui.reali_masina.*;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SpringLayout.Constraints;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import virtuali.ir.reali.masina.reali_masina.RealiMasina;

/**
 *
 * @author unknown
 */
public class FrameVirt extends JFrame {
    
    JPanel p = new JPanel();
    public VirtRegistraiPanel registrai_panel;
    public VirtAtmintisPanel atmintis_panel;   
    JButton zingsninis_button = new JButton("Vienas zingnis");
    JButton nuolatinis_button = new JButton("Ivykdyti visa");
    JPanel buttons_panel = new JPanel();

    // Constructor
    public FrameVirt(final RealiMasina reali_masina) {
        this.registrai_panel = new VirtRegistraiPanel(reali_masina);
        this.atmintis_panel = new VirtAtmintisPanel(reali_masina);
        
        setTitle("Virtuali masina");
        setSize(850, 450);
        //setLayout(new GridBagLayout());
        setLocation(0, 0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        p.setLayout(new GridBagLayout());
        GridBagConstraints c_atmintis = new GridBagConstraints();
        c_atmintis.gridx = 2;
        c_atmintis.gridy = 0;
        
        GridBagConstraints c_registrai = new GridBagConstraints();
        c_registrai.gridx = 0;
        c_registrai.gridy = 0;

        GridBagConstraints c_zingsniai = new GridBagConstraints();
        c_zingsniai.gridx = 0;
        c_zingsniai.gridy = 1;

        GridBagConstraints c_nuolatinis = new GridBagConstraints();
        c_nuolatinis.gridx = 0;
        c_nuolatinis.gridy = 2;
        
        nuolatinis_button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                reali_masina.nuolatinis = true;
            }
        
        });

        zingsninis_button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                reali_masina.zingsnis = true;
            }
        });
        
        //p.setLayout(new BorderLayout());
        p.add(registrai_panel, c_registrai);
        p.add(zingsninis_button, c_zingsniai);
        p.add(nuolatinis_button, c_nuolatinis);
        p.add(atmintis_panel, c_atmintis);
        
    /*    p.add(registrai_panel, BorderLayout.WEST);
        p.add(zingsninis_button, BorderLayout.SOUTH);
        p.add(nuolatinis_button, BorderLayout.NORTH);
        p.add(atmintis_panel, BorderLayout.EAST);
    */      
        add(p, BorderLayout.WEST);
        
    }   
    
    public void atnaujintiLentele() {
        atmintis_panel.atnaujintiLentele();
    }

    public void atnaujintiRegistrus() {
        registrai_panel.atnaujintiRegistrus();
    }
}
