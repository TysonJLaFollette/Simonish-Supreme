/**
 * 
 */
package cs2410.demo.component;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Tyson J. LaFollette
 *The scoreboard for simonish supreme.
 */
public class ScorePanel extends JPanel{
	private int score = 0;
	private int highScore = 0;
	private boolean newHighScore = false;
	private JLabel highScoreLabel = new JLabel();
	private JLabel scoreLabel = new JLabel();
	private JButton startBtn = new JButton("Start");
	
	/**
	 * Creates a new scorpanel object.
	 */
	public ScorePanel() {
		this.setLayout(new GridLayout(1,3));
		highScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(highScoreLabel);
		startBtn.setHorizontalAlignment(JLabel.CENTER);
		this.add(startBtn);
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(scoreLabel);
		this.updateScoreView();
	}
	
	/**
	 * Resets score to 0.
	 */
	public void resetScore() {
		score = 0;
		updateScoreView();
		newHighScore = false;
	}
	
	/**
	 * Raises score by 1.
	 */
	public void incrScore() {
		score++;
		if (score > highScore) {
			newHighScore = true;
			highScore = score;
		}
		updateScoreView();
	}
	
	/**
	 * @return the Score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * @return Whether the score is a new high score.
	 * checks score file to see if the new score is a high score.
	 */
	public boolean isNewHighScore() {
		String[] names = new String[10];
		int[] scores = new int[10];
		for(int i = 0; i<10; i++){
			//initialize arrays to default values.
			scores[i] = 0;
			names[i] = "a ";
		}
		
		Scanner fileInput = null;
		try {
			fileInput = new Scanner(new FileReader("data/highscores.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//load file into arrays.
		for(int i = 0; i < 10; i++){
			Scanner innerScanner = new Scanner(fileInput.nextLine());
			names[i] = innerScanner.next();
			scores[i] = innerScanner.nextInt();
		}
		//check current score against previous scores.
		for(int i = 0; i < 10; i++){
			if(score > scores[i]){
				newHighScore = true;
				return newHighScore;
			}
		}
		newHighScore = false;
		return newHighScore;
	}
	/**
	 * @param name Player's name.
	 * Adds the given name and current score to high scores file.
	 */
	public void addHighScore(String name){
		//read previous scores into arrays.
				String[] names = new String[10];
				int[] scores = new int[10];
				for(int i = 0; i<10; i++){
					//initialize arrays to default values.
					scores[i] = 0;
					names[i] = "a ";
				}
				
				Scanner fileInput = null;
				try {
					fileInput = new Scanner(new FileReader("data/highscores.txt"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//load file into arrays.
				for(int i = 0; i < 10; i++){
					Scanner innerScanner = new Scanner(fileInput.nextLine());
					names[i] = innerScanner.next();
					scores[i] = innerScanner.nextInt();
				}
				
				//put player's name and score into array.
				for(int i = 0; i<10;i++){
					if(getScore() > scores[i]){
						//shift all scores down one, destroying lowest.
						for (int j = 9; j>i;j--){
							scores[j] = scores[j-1];
							names[j] = names[j-1];
						}
						scores[i] = getScore();
						names[i] = name;
						break;
					}
				}
				PrintWriter fileOutput = null;
				try {
					fileOutput = new PrintWriter("data/highscores.txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i = 0; i < 10; i++){
					fileOutput.println(names[i] + " " + scores[i]);
				}
				fileOutput.close();
	}
	/**
	 * Erases all high scores.
	 */
	public void clearHighScore(){
		//clears all high scores.
		PrintWriter fileOutput = null;
		try {
			fileOutput = new PrintWriter("data/highscores.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < 10; i++){
			fileOutput.println("a 0");
		}
		fileOutput.close();
	}
	/**
	 * @param list
	 * Adds a start listener.
	 */
	public void addStartListener(ActionListener list) {
		startBtn.addActionListener(list);
	}
	
	/**
	 * Disables the startbutton
	 */
	public void disableStart() {
		startBtn.setEnabled(false);
	}
	
	/**
	 * Enables the start button.
	 */
	public void enableStart() {
		startBtn.setEnabled(true);
	}
	
	/**
	 * Updates graphics of scoreboard, displaying updated score.
	 */
	private void updateScoreView() {
		highScoreLabel.setText("High Score\n" + highScore);
		scoreLabel.setText("Score\n" + score);
		this.update(this.getGraphics());
	}
}
