package main.groovy.app

import main.java.swing.tabledialog.DialogTableTester

import javax.swing.DefaultListModel
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JFrame
import  main.java.app.*
import main.java.swing.slider.*

import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.JTextField
import javax.swing.JTree
import java.awt.BorderLayout


import main.java.swing.slider.SideBar.SideBarMode;
import main.java.swing.slider.SideBar;
import main.java.swing.slider.SidebarSection;
import main.java.swing.tabledialog.DialogTableTester

import javax.swing.*;
import java.awt.*;


public class mainFrame extends JFrame{
    JList<String> listServSeg

    public  void run () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setSize(1000, 800)
        add(getPanel())
        setVisible(true)
    }

    public  JPanel getPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout())
        JTabbedPane tabbedPane = new JTabbedPane()
        JPanel listPanel = new JPanel(new BorderLayout());
        SideBar sideBar = new SideBar(SideBarMode.TOP_LEVEL, true, 300, true);

        SidebarSection ss1 = new SidebarSection(sideBar, "Seguimientos Servicios", getSeguimientoServicios(), null);
        sideBar.addSection(ss1);

        SidebarSection ss2 = new SidebarSection(sideBar, "Maniobras", getManiobras(), null);
        sideBar.addSection(ss2);

        listPanel.add(sideBar, BorderLayout.WEST);
        listPanel.add(getTablePeticiones(), JLabel.CENTER);
        tabbedPane.add("Slider Bar", listPanel);
        mainPanel.add(tabbedPane);

        return mainPanel;
    }

    private  JList<String> getSeguimientoServicios() {
        DefaultListModel<String> model = new DefaultListModel<String>();
        model.add(0, "      Seguimiento numpeticion");
        model.add(1, "      Peticiones por Servicio");
        model.add(2, "      Servicios Por cliente");
        model.add(3, "      Datos Banda Ancha");
        listServSeg = new JList<String>();
        listServSeg.addListSelectionListener({ e ->
            if (e.valueIsAdjusting) {
                println  listServSeg.getSelectedIndex()
            }
        });
        listServSeg.setModel(model);
        return listServSeg;
    }

    private  JList<String> getManiobras() {
        DefaultListModel<String> model = new DefaultListModel<String>();
        model.add(0, "      .....");
        model.add(1, "      .....");
        model.add(2, "      .....");
        model.add(3, "      .....");
        JList<String> list = new JList<String>();
        list.setModel(model);
        return list;
    }

    private JPanel getTablePeticiones(){
        return DialogTableTester.getPanel()
    }
}


