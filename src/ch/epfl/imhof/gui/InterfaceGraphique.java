package ch.epfl.imhof.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.Box;

public class InterfaceGraphique {

    private static final int TEXT_WIDTH = 200;

    public static void main(String[] args) {
        createUI();
    }

    private static void createUI() {
        JFrame w = new JFrame("Map creator");

        String[] arguments = new String[8];

        // Panneau osm
        JLabel osmFilePlease = new JLabel("Fichier osm.gz");
        JTextField osmPath = new JTextField();
        osmPath.setPreferredSize(new Dimension(TEXT_WIDTH, osmPath
                .getPreferredSize().height));
        osmPath.setEditable(false);
        JButton chooseOsm = new JButton("Choisir...");
        chooseOsm.addActionListener(e -> {
            JFileChooser c = new JFileChooser();
            if (c.showDialog(w, "Choisir") == JFileChooser.APPROVE_OPTION) {
                osmPath.setText(c.getSelectedFile().getPath());
                arguments[0] = c.getSelectedFile().getPath();
            }
        });
        JPanel osmPanel = new JPanel(new FlowLayout());
        osmPanel.add(osmFilePlease);
        osmPanel.add(osmPath);
        osmPanel.add(chooseOsm);

        // Panneau hgt
        JLabel hgtFilePlease = new JLabel("Fichier hgt");
        JTextField hgtPath = new JTextField();
        hgtPath.setEditable(false);
        hgtPath.setPreferredSize(new Dimension(TEXT_WIDTH, hgtPath
                .getPreferredSize().height));
        JButton chooseHgt = new JButton("Choisir");
        chooseHgt.addActionListener(e -> {
            JFileChooser c = new JFileChooser();
            if (c.showDialog(w, "Choisir") == JFileChooser.APPROVE_OPTION) {
                osmPath.setText(c.getSelectedFile().getPath());
                arguments[1] = c.getSelectedFile().getPath();
            }
        });
        JPanel hgtPanel = new JPanel(new FlowLayout());
        hgtPanel.add(hgtFilePlease);
        hgtPanel.add(hgtPath);
        hgtPanel.add(chooseHgt);
        
        //Lat et long Panel
        JLabel latitudeBottomLeft = new JLabel("Latitude du point bas gauche: ");
        JTextField latBL = new JTextField();
        latBL.setEditable(true);
        JPanel latBlP = new JPanel(new FlowLayout());
        latBlP.add(latitudeBottomLeft);
        latBlP.add(latBL);

        JLabel longitudeBottomLeft = new JLabel("Longitude du point bas gauche: ");
        JTextField lonBL = new JTextField();
        lonBL.setEditable(true);
        JPanel lonBlP = new JPanel(new FlowLayout());
        lonBlP.add(longitudeBottomLeft);
        lonBlP.add(lonBL);
        
        JLabel latitudeTopRight = new JLabel("Latitude du point haut droite: ");
        JTextField latTR = new JTextField();
        latTR.setEditable(true);
        JPanel latTrP = new JPanel(new FlowLayout());
        latTrP.add(latitudeTopRight);
        latTrP.add(latTR);
        
        JLabel longitudeTopRight = new JLabel("Longitude du point haut droite: ");
        JTextField lonTR = new JTextField();
        lonTR.setEditable(true);
        JPanel lonTrP = new JPanel(new FlowLayout());
        lonTrP.add(longitudeTopRight);
        lonTrP.add(lonTR);

        JPanel LatLonPanel = new JPanel();
        LatLonPanel.setLayout(new BoxLayout(LatLonPanel, BoxLayout.Y_AXIS));
        LatLonPanel.add(latBlP);
        LatLonPanel.add(lonBlP);
        LatLonPanel.add(latTrP);
        LatLonPanel.add(lonTrP);

        // panneau principal
        JPanel mainP = new JPanel();
        mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
        mainP.add(osmPanel);
        mainP.add(hgtPanel);
        mainP.add(LatLonPanel);
        

        
        w.setContentPane(mainP);
        w.pack();
        w.setVisible(true);

    }

}
