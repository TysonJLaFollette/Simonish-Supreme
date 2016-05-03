package cs2410.demo.simonish;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cs2410.demo.component.ColorPanel;
import cs2410.demo.component.ScorePanel;

/**
 * @author Tyson J. LaFollette
 *An improved simonish game. Featuring sound and images! Also game modes!
 */
public class MainGame implements MouseListener, ActionListener {
	/**
	 * The main game frame. Contains everything.
	 */
	private JFrame frame;
	/**
	 * The main game pane. Contains everything but the menu.
	 */
	private JPanel pane;
	/**
	 * This box contains the colored panels. Does not contain scoreboard.
	 */
	private JPanel gameArea;
	/**
	 * An array of all available colors.
	 */
	private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,Color.BLACK,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,Color.PINK};
	/**
	 * Array that contains all colorpanels. Used for scrambles.
	 */
	private ColorPanel[] panels = new ColorPanel[4];
	/**
	 * The scorepanel. Shows scores and start button.
	 */
	private ScorePanel scorePanel = new ScorePanel();
	/**
	 * The sequence that the player must repeat.
	 */
	private ArrayList<ColorPanel> compSeq = new ArrayList<ColorPanel>();
	/**
	 * Your friendly neighborhood random number generator.
	 */
	private Random rand = new Random();
	/**
	 * Iterator for moving through compSeq.
	 */
	private Iterator<ColorPanel> iter;
	/**
	 * Whether or not it is the player's turn.
	 */
	boolean playerTurn = false;
	/**
	 * Whether or not sound and images are enabled.
	 */
	boolean annoyances = true;
	/**
	 * Whether or not adaptive speed mode is enabled.
	 */
	boolean adaptive = false;
	/**
	 * Whether or not scramble mode is enabled.
	 */
	boolean scramble = false;
	/**
	 * Whether or not reverse-it mode is enabled.
	 */
	boolean reverse = false;
	/**
	 * Size of gameboard and game array. (In # of panels).
	 */
	int size = 4;
	/**
	 * Length of time, in milliseconds, which a colorpanel should glow.
	 */
	int lightTime = 500;
	/**
	 * Amount of time, in milliseconds, between color flashes.
	 */
	int gapTime = 250;
	
	/**
	 * The menu bar for simonish supreme.
	 */
	JMenuBar menuBar;
	/**
	 * The settings submenu.
	 */
	JMenu settingsMenu;
	/**
	 * Opens color chooser dialogue.
	 */
	JMenuItem chooseColorItem;
	/**
	 * Turns sound and images on or off.
	 */
	JMenuItem toggleAnnoyancesItem;
	/**
	 * Mode submenu.
	 */
	JMenu modeMenu;
	/**
	 * Toggles scramble game mode.
	 */
	JMenuItem shuffleItem;
	/**
	 * Toggles reverse-it mode.
	 */
	JMenuItem reverseItem;
	/**
	 * Toggles both scramble and reverse-it modes.
	 */
	JMenuItem mindBlownItem;
	/**
	 * Game speed submenu.
	 */
	JMenu speedMenu;
	/**
	 * Slow speed.
	 */
	JMenuItem speedOne;
	/**
	 * Default speed.
	 */
	JMenuItem speedTwo;
	/**
	 * Fast speed.
	 */
	JMenuItem speedThree;
	/**
	 * Speed increases every round.
	 */
	JMenuItem speedAdaptive;
	/**
	 * Game size submenu.
	 */
	JMenu sizeMenu;
	/**
	 * Default size.
	 */
	JMenuItem size4Item;
	/**
	 * 3x3 size.
	 */
	JMenuItem size9Item;
	/**
	 * 5x5 size.
	 */
	JMenuItem size25Item;
	/**
	 * Stats submenu.
	 */
	JMenu statsMenu;
	/**
	 * Displays highscores.
	 */
	JMenuItem highScoreItem;
	/**
	 * Clears highscores.
	 */
	JMenuItem clearHighScoreItem;
	/**
	 * Displays gaming history.
	 */
	JMenuItem historyItem;
	/**
	 * Clears history file.
	 */
	JMenuItem clearHistoryItem;
	/**
	 * Help submenu.
	 */
	JMenu helpMenu;
	/**
	 * Displays about dialog.
	 */
	JMenuItem aboutItem;
	/**
	 * Displays rules of the game.
	 */
	JMenuItem rulesItem;
	
	/**
	 * Our one and only midi channel.
	 */
	private MidiChannel midi;
	
	/**
	 * Main function. Creates view and starts logic.
	 */
	private MainGame() {
		frame = new JFrame("Simonish Supreme");
		menuBar = new JMenuBar();
		settingsMenu = new JMenu("Settings");
		chooseColorItem = new JMenuItem("Choose color");
		modeMenu = new JMenu("Mode");
		toggleAnnoyancesItem = new JMenuItem("Toggle Annoyances");
		statsMenu = new JMenu("Stats");
		highScoreItem = new JMenuItem("High score");
		clearHighScoreItem = new JMenuItem("Clear high scores");
		speedAdaptive = new JMenuItem("Adaptive");
		historyItem = new JMenuItem("History");
		clearHistoryItem = new JMenuItem("Clear history");
		speedOne = new JMenuItem("Slow");
		gameArea = new JPanel();
		pane = (JPanel)frame.getContentPane();
		speedTwo = new JMenuItem("Medium");
		speedThree = new JMenuItem("Fast");
		size4Item = new JMenuItem("2x2");
		size9Item = new JMenuItem("3x3");
		size25Item = new JMenuItem("5x5");
		helpMenu = new JMenu("Help");
		aboutItem = new JMenuItem("About");
		rulesItem = new JMenuItem("Rules");
		speedMenu = new JMenu("Speed");
		sizeMenu = new JMenu("Size");
		shuffleItem = new JMenuItem("Scramble");
		reverseItem = new JMenuItem("Reverse-it");
		mindBlownItem = new JMenuItem("Blow your mind!");
		
		
		
		for (int i = 0; i < panels.length; i++) {
			panels[i] = new ColorPanel(colors[i]);
			panels[i].addMouseListener(this);
			gameArea.add(panels[i]);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		pane.setLayout(new BorderLayout());
		gameArea.setLayout(new GridLayout(2, 2));
		gameArea.setPreferredSize(new Dimension(400, 400));
		
		menuBar.add(settingsMenu);
		settingsMenu.add(chooseColorItem);
		settingsMenu.add(speedMenu);
		speedMenu.add(speedOne);
		speedMenu.add(speedTwo);
		speedMenu.add(speedThree);
		speedMenu.add(speedAdaptive);
		settingsMenu.add(sizeMenu);
		sizeMenu.add(size4Item);
		sizeMenu.add(size9Item);
		sizeMenu.add(size25Item);
		settingsMenu.add(modeMenu);
		modeMenu.add(shuffleItem);
		modeMenu.add(reverseItem);
		modeMenu.add(mindBlownItem);
		menuBar.add(statsMenu);
		statsMenu.add(highScoreItem);
		statsMenu.add(clearHighScoreItem);
		statsMenu.add(historyItem);
		statsMenu.add(clearHistoryItem);
		menuBar.add(helpMenu);
		helpMenu.add(aboutItem);
		helpMenu.add(rulesItem);
		scorePanel.addStartListener(this);
		chooseColorItem.addActionListener(this);
		toggleAnnoyancesItem.addActionListener(this);
		highScoreItem.addActionListener(this);
		clearHighScoreItem.addActionListener(this);
		historyItem.addActionListener(this);
		clearHistoryItem.addActionListener(this);
		speedOne.addActionListener(this);
		speedTwo.addActionListener(this);
		speedThree.addActionListener(this);
		speedAdaptive.addActionListener(this);
		size4Item.addActionListener(this);
		size9Item.addActionListener(this);
		size25Item.addActionListener(this);
		aboutItem.addActionListener(this);
		rulesItem.addActionListener(this);
		shuffleItem.addActionListener(this);
		reverseItem.addActionListener(this);
		mindBlownItem.addActionListener(this);
		pane.add(gameArea);		
		pane.add(scorePanel, BorderLayout.NORTH);
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
	        midi = synth.getChannels()[0];
	        Instrument[] instr = synth.getDefaultSoundbank().getInstruments();
	        synth.loadInstrument(instr[60]);
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Resets the game.
	 */
	private void reset() {
		scorePanel.disableStart();
		
		for (ColorPanel p : panels) {
			p.reset();
		}
		
		scorePanel.resetScore();
		compSeq.clear();
	}
	
	/**
	 * Creates new item in sequence, lights up sequence.
	 */
	private void compTurn() {
		playerTurn = false;
		if(reverse){
			//reverse the arraylist
			Collections.reverse(compSeq);
		}
		//pause briefly between rounds
		try {
			Thread.sleep(gapTime*3);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		compSeq.add(panels[rand.nextInt(size)]);
		
		iter = compSeq.iterator();
		
		while(iter.hasNext()) {
			ColorPanel tmp = iter.next();
			tmp.pressed();
			
			try {
				Thread.sleep(lightTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			tmp.released();
			
			try {
				Thread.sleep(gapTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(adaptive){
			lightTime *= .8;
			gapTime *= .8;
		}
		if(scramble){
			//shuffle the board
			List<ColorPanel> templist;
			templist = Arrays.asList(panels);
			Collections.shuffle(templist);
			templist.toArray(panels);
			for(int i = 0; i < size; i ++){
				gameArea.remove(panels[i]);
			}
			for (int i = 0; i < panels.length; i++) {
				gameArea.add(panels[i]);
			}
			frame.pack();
			frame.update(frame.getGraphics());
		}
		if(reverse){
			//reverse the arraylist
			Collections.reverse(compSeq);
		}
		iter = compSeq.iterator();
		
		playerTurn = true;
	}
	
	/**
	 * @param panel The panel to check.
	 * @return whether the panel is the correct one.
	 * Determines if the clicked panel is the next in sequence.
	 */
	private boolean isCorrectClick(ColorPanel panel) {
		return iter.next() == panel;
	}
	
	/**
	 * Increments the score when round is over.
	 */
	private void roundWon() {
		scorePanel.incrScore();
	}

	/**
	 * Handles endgame when player messes up.
	 */
	private void gameOver() {
		playerTurn = false;
		IncrHistory();
		String msg = "Game Over\n" + "Your Score was " + scorePanel.getScore();
		String playerName = "";
		if (scorePanel.isNewHighScore()) {
			msg += "\nCongratulations! You set a new high score!\nWhat is your name?";
			//Now ask for their name to put on the high score table.
			playerName = JOptionPane.showInputDialog(msg);
			scorePanel.addHighScore(playerName);
		}
		else {
			JOptionPane.showMessageDialog(null, msg);
		}
		scorePanel.enableStart();
	}
	/**
	 * Adds record of game to history.
	 */
	private void IncrHistory(){
		//open history file. 
		Scanner fileInput = null;
		try {
			fileInput = new Scanner(new FileReader("data/history.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//load file into arrays.
		int numberOfGames = Integer.parseInt(fileInput.nextLine());
		int[] history = new int[numberOfGames];
		for(int i = 0; i < numberOfGames; i++){
			history[i] = Integer.parseInt(fileInput.nextLine());
		}
		//increment numberofgames
		numberOfGames++;
		//add new score to end.
		PrintWriter fileOutput = null;
		try {
			fileOutput = new PrintWriter("data/history.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileOutput.println(Integer.toString(numberOfGames));
		for(int i = 0; i < numberOfGames - 1; i++){
			fileOutput.println(Integer.toString(history[i]));
		}
		fileOutput.println(Integer.toString(scorePanel.getScore()));
		fileOutput.close();
	}
	/**
	 * Brings up the high score table.
	 */
	private void ShowHighScoreTable(){
		//displays high score table.
		//create high score table as string.
		String output = "";
		//read file
		Scanner fileInput = null;
		try {
			fileInput = new Scanner(new FileReader("data/highscores.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//skip first line. It's game history.
		for(int i = 0; i < 10; i++){
			if(fileInput.hasNextLine()){
				output += Integer.toString(i+1) + ". " +  fileInput.nextLine() + "\n";
			}
			
		}
		//output string as contents of JOptionPane.
		JOptionPane.showMessageDialog(null, "High Scores:\n" + output);
	}
	/**
	 * Displays the rules of the game.
	 */
	private void ShowRules(){
		//displays rules.
		//output changes based on selected mode.
		JOptionPane.showMessageDialog(null, new JLabel("Rules: \n Match the pattern.",JLabel.CENTER));
	}
	/**
	 * Brings up the game history screen.
	 */
	private void ShowHistory(){
		//displays history
		int numberOfGames;
		int totalScore = 0;
		int averageScore;
		//reads from save file to find this info.
		Scanner fileInput = null;
		try {
			fileInput = new Scanner(new FileReader("data/history.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		numberOfGames = Integer.parseInt(fileInput.nextLine());
		for(int i = 0; i < numberOfGames; i++){
			totalScore += Integer.parseInt(fileInput.nextLine());
		}
		if(numberOfGames != 0){
			averageScore = totalScore/numberOfGames;
		}
		else{
			averageScore = 0;
		}
		JOptionPane.showMessageDialog(null, "History:\n" + "Number of games played: " + numberOfGames + "\nAverage score: " + averageScore);
	}
	/**
	 * Clears all gaming history.
	 */
	private void ClearHistory(){
		PrintWriter fileOutput = null;
		try {
			fileOutput = new PrintWriter("data/history.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileOutput.println("0");
		fileOutput.close();
	}
	/**
	 * Show the about dialog.
	 */
	private void ShowAbout(){
		//displays about page
		JOptionPane.showMessageDialog(null, "This is a game made by Tyson J. LaFollette. Yay! Isn't it wonderful?");
	}
	/**
	 * Allows the user to set the color of a colorpanel.
	 */
	private void SetColor(){
		//sets color of chosen square to selected color.
		int selectedIndex = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter box number to recolor:")) -1;
		ColorPanel thePanel;
		Color chosenColor;
		thePanel = panels[selectedIndex];
		chosenColor = thePanel.getBackground();
		chosenColor = JColorChooser.showDialog(null, "Choose new color:", chosenColor);
		thePanel.setBackground(chosenColor);
	}
	/**
	 * @param args Command line arguments.
	 * Starts game in a new thread.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				new MainGame();				
			}});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//do nothing
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!playerTurn) return;
		ColorPanel tmp = (ColorPanel)e.getSource();
		if(annoyances){
			tmp.penguinize("data/flat-lemmling-Cartoon-penguin.png");
			if(tmp.getColor().getRGB() == Color.RED.darker().darker().getRGB()){
				midi.noteOn(40, 150);
			}
			else if(tmp.getColor().getRGB() == Color.GREEN.darker().darker().getRGB()){
				midi.noteOn(50, 150);
			}
			else if(tmp.getColor().getRGB() == Color.YELLOW.darker().darker().getRGB()){
				midi.noteOn(60, 150);
			}
			else if(tmp.getColor().getRGB() == Color.BLUE.darker().darker().getRGB()){
				midi.noteOn(70, 150);
			}
		}
		tmp.pressed();
		if (!isCorrectClick(tmp)) {
			this.gameOver();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!playerTurn) return;
		midi.allNotesOff();
		((ColorPanel)e.getSource()).released();
		if (!iter.hasNext()) {
			this.roundWon();
			new Thread(new Runnable() {
				@Override
				public void run() {
					compTurn();
				}
			}).start();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//do nothing		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton){
			this.reset();
			this.compTurn();
		}
		if(e.getSource()==chooseColorItem){
			SetColor();
		}
		if(e.getSource() == highScoreItem){
			ShowHighScoreTable();
		}
		if(e.getSource() == clearHighScoreItem){
			scorePanel.clearHighScore();
		}
		if(e.getSource() == rulesItem){
			ShowRules();
		}
		if(e.getSource() == historyItem){
			ShowHistory();
		}
		if(e.getSource() == clearHistoryItem){
			ClearHistory();
		}
		if(e.getSource()== aboutItem){
			ShowAbout();
		}
		if(e.getSource()== toggleAnnoyancesItem){
			annoyances = !annoyances;
		}
		if(e.getSource()== speedOne){
			lightTime = 1000;
			gapTime = 500;
		}
		if(e.getSource()== speedTwo){
			lightTime = 500;
			gapTime = 250;
		}
		if(e.getSource()== speedThree){
			lightTime = 250;
			gapTime = 125;
		}
		if(e.getSource()== speedAdaptive){
			lightTime = 1000;
			gapTime = 500;
			adaptive = true;
		}
		if(e.getSource()== size4Item){
			for(int i = 0; i < size; i ++){
				gameArea.remove(panels[i]);
			}
			size = 4;
			gameArea.setLayout(new GridLayout(2, 2));
			for (int i = 0; i < panels.length; i++) {
				panels[i] = new ColorPanel(colors[i%12]);
				panels[i].addMouseListener(this);
				gameArea.add(panels[i]);
			}
			frame.pack();
			frame.update(frame.getGraphics());
		}
		if(e.getSource()== size9Item){
			for(int i = 0; i < size; i ++){
				gameArea.remove(panels[i]);
			}
			size = 9;
			panels = new ColorPanel[size];
			gameArea.setLayout(new GridLayout(3, 3));
			for (int i = 0; i < panels.length; i++) {
				panels[i] = new ColorPanel(colors[i%12]);
				panels[i].addMouseListener(this);
				gameArea.add(panels[i]);
			}
			frame.pack();
			frame.update(frame.getGraphics());
		}
		if(e.getSource()== size25Item){
			for(int i = 0; i < size; i ++){
				gameArea.remove(panels[i]);
			}
			size = 25;
			panels = new ColorPanel[size];
			gameArea.setLayout(new GridLayout(5, 5));
			for (int i = 0; i < panels.length; i++) {
				panels[i] = new ColorPanel(colors[i%12]);
				panels[i].addMouseListener(this);
				gameArea.add(panels[i]);
			}
			frame.pack();
			frame.update(frame.getGraphics());
		}
		if(e.getSource()== shuffleItem){
			scramble = !scramble;
		}
		if(e.getSource()== reverseItem){
			reverse = !reverse;
		}
		if(e.getSource()== mindBlownItem){
			reverse = !reverse;
			scramble = !scramble;
		}
	}

}
