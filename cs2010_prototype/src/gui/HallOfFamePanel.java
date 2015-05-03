package gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import biomorphHandling.Biomorph;
/**
 * Class for the Hall of Fame panel.
 * @author Jack Taylor
 * @version 30/04/2015
 */
public class HallOfFamePanel extends JPanel
{
	private static final long serialVersionUID = 5281756429180071005L;
	protected JPanel panel[];
	protected OpenGLCanvas canvas[];
	protected int size;
	/**
	 * Constructor
	 * @param biomorphs The array of biomorphs for initialising the OpenGL canvases.
	 */
	public HallOfFamePanel(Biomorph biomorphs[])
	{
		super(new GridBagLayout());
		size = 150;
		panel = new JPanel[4];
		canvas = new OpenGLCanvas[4];
		for (int i = 0; i < panel.length; i++) panel[i] = new JPanel();
		for (int i = 0; i < canvas.length; i++)
		{
			if (i < biomorphs.length && biomorphs[i] != null) canvas[i] = new OpenGLCanvas(biomorphs[i], size);
			else canvas[i] = new OpenGLCanvas(null, size);
			panel[i].add(canvas[i].getCanvas());
		}
		GridBagConstraints gbc = new GridBagConstraints();
		for (int i = 0; i < panel.length; i++)
		{
			gbc.gridx = 0;
			gbc.gridy = i;
			add(panel[i], gbc);
			if (gbc.gridy == 3) panel[i].setBorder(new EmptyBorder(-5, -5, -5, -3));
			else panel[i].setBorder(new EmptyBorder(-5, -5, -4, -3));
		}
		setVisible(true);
	}
	/**
	 * Changes the size of each OpenGL canvas. The length and width will always be the same.
	 * @param size The new size for the canvases
	 */
	public void resize(int newSize)
	{
		this.size = newSize;
		for (int i = 0; i < panel.length; i++) panel[i].setSize(size, size);
		for (int i = 0; i < canvas.length; i++) canvas[i].getCanvas().setSize(size, size);
	}
	/**
	 * Returns the biomorph displayed on a specified canvas.
	 */
	public Biomorph getBiomorph(int index)
	{
		return canvas[index].getBiomorph();
	}
	/**
	 * Changes the biomorph to be displayed on a specified canvas.
	 * @param index The canvas to change
	 * @param biomorph The new biomorph to display
	 */
	public void setBiomorph(int index, Biomorph biomorph)
	{
		canvas[index].setBiomorph(biomorph);
	}
}