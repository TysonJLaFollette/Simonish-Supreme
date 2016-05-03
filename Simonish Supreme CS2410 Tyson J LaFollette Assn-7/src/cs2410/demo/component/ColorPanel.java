/**
 * 
 */
package cs2410.demo.component;

import java.awt.Color;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Tyson J. LaFollette
 *Colorpanel objects to build game board in simonish.
 */
public class ColorPanel extends JPanel {
	private Color color;
	private JLabel myLabel;
	
	/**
	 * @param color The color of the new panel.
	 * Creates a new panel of the chosen color.
	 */
	public ColorPanel(Color color) {
		this.myLabel = new JLabel();
		this.add(myLabel);
		this.color = color.darker().darker();
		this.setBackground(this.color);
	}
	/**
	 * @param image The image to place in panel.
	 * Sets teh background image for the colorpanel.
	 */
	public void penguinize(String image){
		ImageIcon myImage = new ImageIcon(image);
		myLabel.setIcon(myImage);
	}
	/**
	 * What to do when this panel is clicked.
	 */
	public void pressed() {
		this.setBackground(color.brighter().brighter());
		this.update(this.getGraphics());
	}
	
	/**
	 * What to do when this panel is released.
	 */
	public void released() {
		this.setBackground(color);
		myLabel.setIcon(null);
		this.update(this.getGraphics());
	}
	
	/**
	 * Reset this color panel to default state.
	 */
	public void reset() {
		this.setBackground(this.color);
		this.update(this.getGraphics());
	}
	/**
	 * @return the color of the panel.
	 */
	public Color getColor(){
		return color;
	}
}
