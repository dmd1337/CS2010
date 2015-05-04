package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import biomorphHandling.*;
/**
 * The main window for the Biomorph Simulation.
 * @author Charandeep Rai, Jack Taylor, Tom Connolly
 * @version 30/04/2015
 */
public class GraphicsMain implements ActionListener
{
	private JFrame mainFrame; // The main frame used for the GUI
	private FileMenu fileMenu; // The file menu to be displayed at the top of the window
	private JPanel contentPanel; // The content panel, containing the biomorph panel, mutation panel and right panel.
	private MainBiomorphPanel mainPanel; // The main biomorph panel, containing the large biomorph window.
	private MutationPanel mutationPanel; // The mutation panel, containing 8 smaller biomorph windows.
	private HallOfFamePanel hallOfFame; // The hall of fame panel.
	private JPanel evolvePanel;
	private RightPanel rightPanel; // The right panel.
	private BiomorphManager bm; // The Biomorph Manager used to arrange and organise Biomorphs
	private JButton evolveButton;
	private JButton resetButton;
	private JButton loadToMainWindowButton;
	private JButton motherButton;
	private JButton fatherButton;
	private JButton resetToOrigBioButton;
	private JPanel buttonPanel;
	private JCheckBox[] selectMutation;
	private ButtonGroup buttonGroup;
	private JRadioButton[] selectHallOfFame;
	private ArrayList<Biomorph> selected;
	private Biomorph savedBiomorph;
	/**
	 * Constructor
	 */
	public GraphicsMain()
	{
		// *0* Initialise variables
		final int blankSpace = 5;
		int width = 1024;
		int height = 720;
		bm = new BiomorphManager();
		// *1* Create components
		mainFrame = new JFrame("Group 5 Biomorph Simulation");
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fileMenu = new FileMenu(bm, this,bm.getEvolStats());
		evolveButton = new JButton("Create");
		evolveButton.setSize(new Dimension(20, 20));
		resetButton = new JButton("Reset");
		resetButton.setSize(new Dimension(20, 20));
		loadToMainWindowButton = new JButton("Load to main window");
		loadToMainWindowButton.setSize(new Dimension(20, 20));
		motherButton = new JButton("Load mother");
		motherButton.setSize(new Dimension(20, 20));
		fatherButton = new JButton("Load father");
		fatherButton.setSize(new Dimension(20, 20));
		resetToOrigBioButton = new JButton("Reset to original Biomorph");
		resetToOrigBioButton.setSize(new Dimension(20, 20));
		mainPanel = new MainBiomorphPanel(null);
		Biomorph biomorphs[] = new Biomorph[8];
		for (int i = 0; i < biomorphs.length; i++) biomorphs[i] = null;
		mutationPanel = new MutationPanel(biomorphs);
		hallOfFame = new HallOfFamePanel(biomorphs);
		selectMutation = new JCheckBox[9];
		for (int i = 0; i < selectMutation.length; i++)
		{
			String number;
			if (i == selectMutation.length - 1) number = "Random";
			else number = "" + (i + 1);
			selectMutation[i] = new JCheckBox(number);
		}
		buttonGroup = new ButtonGroup();
		selectHallOfFame = new JRadioButton[4];
		for (int i = 0; i < selectHallOfFame.length; i++)
		{
			selectHallOfFame[i] = new JRadioButton("" + (i + 1));
			buttonGroup.add(selectHallOfFame[i]);
		}
		rightPanel = new RightPanel(mainPanel.getBiomorph(), (int)(mainFrame.getWidth() - (mainFrame.getHeight() * 0.825 + 40)));
		selected = new ArrayList<Biomorph>(8);
		// *2* Create containers
		contentPanel = new JPanel(new GridBagLayout());
		buttonPanel = new JPanel(new BorderLayout());
		evolvePanel = new JPanel();
		evolvePanel.setLayout(new GridLayout(0,2));
		//evolvePanel.setSize(new Dimension(10,40));
		JPanel selectMutationPanel = new JPanel(new GridBagLayout());
		JPanel selectHallOfFamePanel = new JPanel(new GridBagLayout());
		JPanel boxPanel = new JPanel(new BorderLayout());
		// *3* Specify layout managers
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setMinimumSize(new Dimension(width, height));
		((JPanel) mainFrame.getContentPane()).setBorder(new EmptyBorder(blankSpace, blankSpace, blankSpace, blankSpace));
		contentPanel.setMinimumSize(new Dimension(width, height));
		GridBagConstraints gbc = new GridBagConstraints();
		// *4* Add components to containers
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		contentPanel.add(hallOfFame, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		contentPanel.add(mainPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		contentPanel.add(mutationPanel, gbc);
		evolvePanel.add(evolveButton);
		evolvePanel.add(resetButton);
		evolvePanel.add(loadToMainWindowButton);
		evolvePanel.add(Box.createRigidArea(new Dimension(0,5)));
		evolvePanel.add(motherButton);
		evolvePanel.add(fatherButton);
		evolvePanel.add(resetToOrigBioButton);
		JPanel buttons = new JPanel(new BorderLayout());
		buttons.add(rightPanel, BorderLayout.NORTH);
		buttons.add(evolvePanel, BorderLayout.SOUTH);
		buttonPanel.add(buttons,BorderLayout.NORTH);
		boxPanel.add(selectMutationPanel, BorderLayout.NORTH);
		boxPanel.add(selectHallOfFamePanel, BorderLayout.SOUTH);
		buttonPanel.add(boxPanel, BorderLayout.SOUTH);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 9;
		selectMutationPanel.add(new JLabel("Mutation"), gbc);
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		int i = 0;
		for (JCheckBox box : selectMutation)
		{
			gbc.gridx = i;
			selectMutationPanel.add(box, gbc);
			i++;
		}
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		selectHallOfFamePanel.add(new JLabel("Hall of Fame"), gbc);
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		i = 0;
		for (JRadioButton button : selectHallOfFame)
		{
			gbc.gridx = i;
			selectHallOfFamePanel.add(button, gbc);
			i++;
		}
		mainFrame.add(fileMenu.getContents(), BorderLayout.NORTH);
		mainFrame.add(contentPanel, BorderLayout.WEST);
		mainFrame.add(buttonPanel, BorderLayout.EAST);
		// *5* Create action listeners
		mainFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
				//exitApp();
			}
		});
		mainFrame.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				resize();
			}
		});
		evolveButton.addActionListener(this);
		resetButton.addActionListener(this);
		loadToMainWindowButton.addActionListener(this);
		motherButton.addActionListener(this);
		fatherButton.addActionListener(this);
		resetToOrigBioButton.addActionListener(this);
		// *6* Pack and display
		mainFrame.pack();
		mainFrame.setVisible(true);
		resize();
	}
	/**
	 * Evolves the biomorphs when the Evolve button is pressed.
	 */
	public void actionPerformed(ActionEvent e)
	{
		//code to find out whether a window has focus (not being used yet)
		mainPanel.getCanvas().addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0)
			{
				//System.out.println("MAIN WINDOW FOCUS: YES");	
			}

			@Override
			public void focusLost(FocusEvent arg0)
			{
				//System.out.println("MAIN WINDOW FOCUS: NO");
			}
			
		});

		refreshMutationPanel();
		bm.createAndAdd();
		// check if checkboxes are selected
		for (int i = 0; i < selectMutation.length; i++)
		{
			// check if each box has been selected and create an array full of selected biomorphs
			if (selectMutation[i].isSelected())
			{
				if (selectMutation[i].getText() == "Random")
				{
					selected.add(bm.createAndAdd());
				}
				else
				{
					// add corresponding biomorph to an arraylist
					selected.add(bm.getSpecific(i+1));
				}
			}
		}
		// code run after 'Evolve' button clicked
		if (e.getActionCommand().equals("Evolve")||e.getActionCommand().equals("Create"))
		{			
			evolveAction();
		}
		// code run after 'Reset' button clicked
		if (e.getActionCommand().equals("Reset"))
		{
			resetAction();
		}
		// code run after 'Load to main window' button clicked
		if (e.getActionCommand().equals("Load to main window"))
		{
			if (selected.size() == 1)
			{
				bm.addSpecific(selected.get(0));
				refreshMainPanel();
			}
			else
			{
			}
			selected.clear();
			rightPanel.reset();
		}
		if (e.getActionCommand().equals("Add to Hall of Fame"))
		{
			if (mainPanel.getBiomorph() == null) JOptionPane.showMessageDialog(mainFrame, "The main panel contains no biomorph to add.");
			else
			{
				if (hallOfFame.getBiomorph(0) == null) hallOfFame.setBiomorph(0, mainPanel.getBiomorph());
				else if (hallOfFame.getBiomorph(1) == null) hallOfFame.setBiomorph(1, mainPanel.getBiomorph());
				else if (hallOfFame.getBiomorph(2) == null) hallOfFame.setBiomorph(2, mainPanel.getBiomorph());
				else if (hallOfFame.getBiomorph(3) == null) hallOfFame.setBiomorph(3, mainPanel.getBiomorph());
				else JOptionPane.showMessageDialog(mainFrame, "The Hall of Fame can only store 4 biomorphs.");
			}
		}
		if (e.getActionCommand().equals("Remove Selected"))
		{
			int selection = -1;
			for (int i = 0; i < selectHallOfFame.length; i++) if (selectHallOfFame[i].isSelected()) selection = i;
			if (selection != -1) hallOfFame.setBiomorph(selection, null);
			else JOptionPane.showMessageDialog(mainFrame, "No Hall of Fame biomorph selected.");
		}
		if (e.getActionCommand().equals("Clear Hall of Fame"))
		{
			for (int i = 0; i < 4; i++) hallOfFame.setBiomorph(i, null);
		}
		if (e.getActionCommand().equals("Load mother") && mainPanel.getBiomorph()!=null && mainPanel.getBiomorph().getMother()!=null){
			savedBiomorph = mainPanel.getBiomorph();
			mainPanel.setBiomorph(mainPanel.getBiomorph().getMother());
		}
		if (e.getActionCommand().equals("Load father") && mainPanel.getBiomorph()!=null && mainPanel.getBiomorph().getFather()!=null){
			savedBiomorph = mainPanel.getBiomorph();
			mainPanel.setBiomorph(mainPanel.getBiomorph().getFather());
		}
		if (e.getActionCommand().equals("Reset to original Biomorph") && savedBiomorph!=null){
			mainPanel.setBiomorph(savedBiomorph);
		}
		if (e.getActionCommand().equals("Add to Main Window"))
		{
			int selection = -1;
			for (int i = 0; i < selectHallOfFame.length; i++) if (selectHallOfFame[i].isSelected()) selection = i;
			if (selection != -1)
			{
				if (hallOfFame.getBiomorph(selection) != null) mainPanel.setBiomorph(hallOfFame.getBiomorph(selection));
				else JOptionPane.showMessageDialog(mainFrame, "The selected biomorph does not exist.");
			}
			else JOptionPane.showMessageDialog(mainFrame, "No Hall of Fame biomorph selected.");
		}

		//clean up after action
		for(JCheckBox box : selectMutation){
			box.setSelected(false);
		}
		rightPanel.update(bm.getSpecific(0));
	}
	/**
	 * Refreshes the contents of the main biomorph panel.
	 */
	public void refreshMainPanel()
	{
		mainPanel.setBiomorph(bm.getSpecific(0));
		fileMenu.updateBiomorph(bm.getSpecific(0));
		refreshMutationPanel();
	}
	/**
	 * Refreshes the contents of the mutation panel.
	 */
	public void refreshMutationPanel()
	{
		for (int i = 7; i >= 0; i--)
		{
			mutationPanel.setBiomorph(i, bm.getSpecific(i + 1));
		}
	}
	/**
	 * Scales the biomorph windows according to the size of the main frame.
	 */
	private void resize()
	{
		mainPanel.resize((int)(mainFrame.getHeight() * 0.6) - 1);
		mutationPanel.resize((int)(mainFrame.getHeight() * 0.15) - 1);
		hallOfFame.resize((int)(mainFrame.getHeight() * 0.225));
		rightPanel.resize((int)(mainFrame.getWidth() - (mainFrame.getHeight() * 0.825 + 40)));
		buttonPanel.setPreferredSize(new Dimension((int)(mainFrame.getWidth() - (mainFrame.getHeight() * 0.825 + 40)), 100));
		mainPanel.revalidate();
		mutationPanel.revalidate();
		hallOfFame.revalidate();
		rightPanel.revalidate();
	}
	/**
	 * Shows a confirmation dialog to exit the application.
	 */
	@SuppressWarnings("unused")
	private void exitApp()
	{
		int response = JOptionPane.showConfirmDialog(mainFrame, "Are you sure that you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}
	private void evolveAction(){
		// biomorph to be evolved and given to the main window
		Biomorph returnBiomorph;
		mainPanel.setBiomorph(bm.getSpecific(0));
		evolveButton.setText("Evolve");
		// evolve using selected biomorphs
		if (selected.size() > 0)
		{
			// assign Biomorph variable so that multiple evolutions can occur
			returnBiomorph = bm.getSpecific(0);
			// parse the Biomorphs selected for evolution
			for (int i = 0; i < selected.size(); i++)
			{
				// evolve two biomorphs
				returnBiomorph = bm.evolve(returnBiomorph, selected.get(i));
				// bug tracking
				System.out.println(returnBiomorph);
				System.out.println(selected.get(i));
			}
		}
		else
		{
			returnBiomorph = bm.evolve(bm.getSpecific(0), bm.getSpecific(bm.getSize()-1));
		}
		selected.clear();
		bm.addSpecific(returnBiomorph);
		refreshMainPanel();
		rightPanel.update(returnBiomorph);
	}
	
	public void resetAction(){
		bm = new BiomorphManager();
		fileMenu.updateBM(bm);
		fileMenu.updateES(bm.getEvolStats());
		refreshMainPanel();
		selected.clear();
		rightPanel.reset();
	}
	
	public void clearAll(){
		resetAction();
		mainPanel.setBiomorph(null);
		for(int i=0;i<8;i++){
			mutationPanel.setBiomorph(i, null);
		}
		for(int i=0;i<4;i++){
			hallOfFame.setBiomorph(i, null);
		}
		rightPanel = new RightPanel(bm.getSpecific(0), (int)(mainFrame.getWidth() - (mainFrame.getHeight() * 0.825 + 40)));
	}
	/**
	 * Main method
	 */
	public static void main(String[] args)
	{
		new GraphicsMain();
	}
}