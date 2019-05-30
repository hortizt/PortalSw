package main.java.app;

import main.java.swing.slider.SideBar.SideBarMode;
import main.java.swing.slider.SideBar;
import main.java.swing.slider.SidebarSection;

import javax.swing.*;
import java.awt.*;


public class main extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	public main(){
		
	}
	
	
	public static void main (String[] args) {
		main t = new main();
		
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t.setSize(500, 600);
		t.add(main.getPanel());
		t.setVisible(true);
	}

	public  void run () {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 600);
		add(main.getPanel());
		setVisible(true);
	}
	
	public static JPanel getPanel() {
		
		Icon iconMail24 = new ImageIcon("src/main/resource/img/Mail/Mail_24x24.png");
		Icon iconMail16 = new ImageIcon("src/main/resource/img/Mail/Mail_16x16.png");
		Icon iconCal24 = new ImageIcon("src/main/resource/img/Calendar/Calendar_24x24.png");
		Icon iconGlobe24 = new ImageIcon("src/main/resource/img/Globe/Globe_24x24.png");
		
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel listPanel = new JPanel(new BorderLayout());
		
		SideBar sideBar = new SideBar(SideBarMode.TOP_LEVEL, true, 300, true);

		JTree tree = new JTree();
		
		
		
		SidebarSection ss1 = new SidebarSection(sideBar, "Calendars", tree, iconCal24);
		sideBar.addSection(ss1);

		SideBar innerSideBar = new SideBar(SideBarMode.INNER_LEVEL, true, -1, true);
		


//		innerSideBar.addSection(new SidebarSection(innerSideBar, "American Partners", getInner1(), iconMail16));
		JPanel ap = new JPanel(new BorderLayout());
		ap.add(new JTextField("American Partners"));
		innerSideBar.addSection(new SidebarSection(innerSideBar, ap, getInner1(), iconMail16));
		innerSideBar.addSection(new SidebarSection(innerSideBar, "Internal", getInner2(), iconMail16));
		innerSideBar.addSection(new SidebarSection(innerSideBar, "Promotions", getInner3(), iconMail16));
		
		SidebarSection ss2 = new SidebarSection(sideBar, "Mail Groups",  innerSideBar, iconMail24);
		sideBar.addSection(ss2);

		SidebarSection ss3 = new SidebarSection(sideBar, "Logistics Partners", getInner4(), iconGlobe24);
		sideBar.addSection(ss3);
		
		listPanel.add(sideBar, BorderLayout.WEST);
		listPanel.add(new JLabel("<html><body><h1>central panel</html>", JLabel.CENTER));
		
		tabbedPane.add("Slider Bar", listPanel);
		
		mainPanel.add(tabbedPane);
		return mainPanel;
	}


	private static JList<String> getInner4() {
		DefaultListModel<String> model = new DefaultListModel<String>();
		model.add(0, "Bill Gates");
		model.add(1, "Steven Spielberg");
		model.add(2, "Donald Trump");
		model.add(3, "Steve Jobs");		
		 
		JList<String> list = new JList<String>();
		
		list.setModel(model);
		return list;
	}


	private static JComponent getInner2() {
		JList<String> list = getInner4();
		return list;
	}
	
	private static JComponent getInner1() {
		DefaultListModel<String> model = new DefaultListModel<String>();
		model.add(0, "Bill Gates");
		model.add(1, "Steven Spielberg");
		 
		JList<String> list = new JList<String>();
		
		list.setModel(model);
		return list;
	}

	private static JComponent getInner3() {
		DefaultListModel<String> model = new DefaultListModel<String>();
		model.add(0, "Steve Jobs");		
		 
		JList<String> list = new JList<String>();
		
		list.setModel(model);
		return list;
	}
}
