package gui;
import genes.Gene;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	@SuppressWarnings("unused")
	private HallOfFamePanel hallOfFame; // The hall of fame panel.
	private JPanel evolvePanel;
	private RightPanel rp; // The right panel.
	private BiomorphManager bm; // The Biomorph Manager used to arrange and organise Biomorphs
	private JButton evolveButton;
	private JButton resetButton;
	private JPanel buttonPanel;
	private JCheckBox[] checkBoxArr;
	private ArrayList<Biomorph> selectedForEvol;
	/**
	 * Constructor
	 */
	public GraphicsMain()
	{
		// *0* Initialise variables
		final int blankSpace = 3;
		int width = 800;
		int height = 720;
		bm = new BiomorphManager();
		// *1* Create components
		mainFrame = new JFrame("Group 5 Biomorph Simulation");
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fileMenu = new FileMenu(bm, this);
		evolveButton = new JButton("Evolve");
		evolveButton.setSize(new Dimension(70, 20));
		resetButton = new JButton("Reset");
		evolveButton.setSize(new Dimension(70, 20));
		mainPanel = new MainBiomorphPanel(null);
		Biomorph biomorphs[] = new Biomorph[8];
		for (int i = 0; i < biomorphs.length; i++) biomorphs[i] = null;
		mutationPanel = new MutationPanel(biomorphs);
		checkBoxArr = new JCheckBox[9];
		for (int i = 0; i < checkBoxArr.length; i++)
		{
			String number;
			if(i==checkBoxArr.length-1){
				number = "Random";
			}
			else{ 
				number = "" + (i + 1);
			}
			JCheckBox box = new JCheckBox(number);
			checkBoxArr[i] = box;
		}
		rp = new RightPanel();
		selectedForEvol = new ArrayList<Biomorph>(8);
		// *2* Create containers
		contentPanel = new JPanel(new GridBagLayout());
		buttonPanel = new JPanel(new BorderLayout());
		evolvePanel = new JPanel();
		JPanel boxPanel = new JPanel(new GridBagLayout());
		// *3* Specify layout managers
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setMinimumSize(new Dimension(width, height));
		((JPanel) mainFrame.getContentPane()).setBorder(new EmptyBorder(blankSpace, blankSpace, blankSpace, blankSpace));
		contentPanel.setMinimumSize(new Dimension(width, height));
		GridBagConstraints gbc = new GridBagConstraints();
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// *4* Add components to containers
		gbc.gridx = 1;
		gbc.gridy = 0;
		contentPanel.add(mainPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		contentPanel.add(mutationPanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		boxPanel.add(new JLabel("Check the boxes to evolve the corresponding Biomorphs"), gbc);
		gbc.gridwidth = 1;
		gbc.gridy = 2;
		int i = 0;
		for (JCheckBox box : checkBoxArr)
		{
			gbc.gridx = i;
			boxPanel.add(box, gbc);
			i++;
		}
		evolvePanel.add(evolveButton, BorderLayout.CENTER);
		evolvePanel.add(resetButton, BorderLayout.CENTER);
		buttonPanel.add(rp.getContents(), BorderLayout.WEST);
		buttonPanel.add(evolvePanel, BorderLayout.EAST);
		buttonPanel.add(boxPanel, BorderLayout.SOUTH);
		mainFrame.add(fileMenu.getContents(), BorderLayout.NORTH);
		mainFrame.add(contentPanel, BorderLayout.WEST);
		mainFrame.add(buttonPanel, BorderLayout.EAST);
		// *5* Create action listeners
		mainFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				exitApp();
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
		checkBoxArr[0].addActionListener(this);
		// *6* Pack and display
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	/**
	 * Evolves the biomorphs when the Evolve button is pressed.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Evolve"))
		{
			refreshMutationPanel();
			// check if checkboxes are selected
			for (int i = 0; i < checkBoxArr.length; i++)
			{
				// check if each box has been selected
				if (checkBoxArr[i].isSelected())
				{
					if(checkBoxArr[i].getText()=="Random"){
						selectedForEvol.add(bm.createAndAdd());
					}
					else{
					// add corresponding biomorph to an arraylist
					selectedForEvol.add(bm.getSpecific(i));
					}
					System.out.println("boxes selected are " + (i + 1));
				}
			}
			Biomorph returnBiomorph;
			// evolve using selected biomorphs
			if (selectedForEvol.size() > 0)
			{
				// assign Biomorph variable so that multiple evolutions can occur
				returnBiomorph = bm.getSpecific(0);
				// parse the Biomorphs selected for evolution
				for (int i = 0; i < selectedForEvol.size(); i++)
				{
					// evolve two biomorphs
					returnBiomorph = bm.evolveClo(returnBiomorph, selectedForEvol.get(i));
					// bug tracking
					System.out.println("internal evolution happened");
					for (Gene gene : returnBiomorph.getGenes())
					{
						System.out.println("Biomorph" + i);
						System.out.println(gene.getValue());
					}
				}
			}
			else
			{
				returnBiomorph = bm.evolveClo(bm.getSpecific(1), bm.getRandomBiomorph());
			}
			selectedForEvol.clear();
			bm.addSpecific(returnBiomorph);
			mainPanel.setBiomorph(bm.getSpecific(0));
			fileMenu.updateBiomorph(bm.getSpecific(0));
		}
		if (e.getActionCommand().equals("Reset"))
		{
			bm = new BiomorphManager();
			fileMenu.updateBM(bm);
			refreshMainPanel();
		}
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
			mutationPanel.setBiomorph(i, bm.getSpecific(i));
		}
	}
	/**
	 * Scales the biomorph windows according to the size of the main frame.
	 */
	private void resize()
	{
		mainPanel.resize((int) (mainFrame.getHeight() * 0.6));
		mutationPanel.resize((int) (mainFrame.getHeight() * 0.15));
	}
	/**
	 * Shows a confirmation dialog to exit the application.
	 */
	private void exitApp()
	{
		int response = JOptionPane.showConfirmDialog(mainFrame, "Are you sure that you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}
	/**
	 * Main method
	 */
	public static void main(String[] args)
	{
		new GraphicsMain();
	}
}