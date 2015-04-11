package ca.georgian.comp1011_assignment5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Title: COMP1011 - Project 1 - Memory Game
 * 
 * Description:
 * This program is a card matching memory game. The user will pick their first card and attempt
 * to find the second of that card. If they are successful the pair will disappear and they will
 * be awarded a point. If they are unsuccessful, the second card will turn back over and they 
 * may attempt to match their card to another again. If the user selects their first card a 
 * second time, it will be unselected. The user has 30 seconds to attempt to match all of the 
 * pairs. The game will end when either the user matches all pairs, or when the timer runs out.
 * After the game ends, the user will be prompted if they want to play again, or quit the game.
 * 
 * @author Blaine Parr and Cody Hutchinson
 * @version April 11, 2015
 */
public class MainActivity extends Activity {
	//instance variables
	private int _cardOne, _cardTwo;
	private String _cardOneString, _cardTwoString;
	private ArrayList<String> _cards;
	private int _score;
	private boolean _firstSelection;
	private Handler _handler = new Handler();
	private TextView _scoreTextView;
	private TextView _timerTextView;
	private TextView _infoTextView;
	private Context _context = this;
	private CountDownTimer timer;
	
	//constants
	private final int SCORE_TO_WIN = 8;
	
	//declare all of the ImageButtons
	private ImageButton _imageButton11, _imageButton12, _imageButton13, _imageButton14,
	_imageButton21, _imageButton22, _imageButton23, _imageButton24,
	_imageButton31, _imageButton32, _imageButton33, _imageButton34,
	_imageButton41, _imageButton42, _imageButton43, _imageButton44;
	
	//add all ImageButtons to an array
	private ImageButton[] _imageButtons = {_imageButton11, _imageButton12, _imageButton13, _imageButton14,
	_imageButton21, _imageButton22, _imageButton23, _imageButton24,
	_imageButton31, _imageButton32, _imageButton33, _imageButton34,
	_imageButton41, _imageButton42, _imageButton43, _imageButton44};
	
	//add all ImageButton ids to an array
	private int _ids[] = {R.id.imageButton11, R.id.imageButton12, R.id.imageButton13, R.id.imageButton14,
	R.id.imageButton21, R.id.imageButton22, R.id.imageButton23, R.id.imageButton24,
	R.id.imageButton31, R.id.imageButton32, R.id.imageButton33, R.id.imageButton34,
	R.id.imageButton41, R.id.imageButton42, R.id.imageButton43, R.id.imageButton44};
	
	//Public Methods////////////////////////////////////////////////////////////////////////////
	/**
	 * This method sets up the sets up the app upon startup.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_init(); //call init
	} //method onCreate ends

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} //method onCreateOptionsMenu

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} //if ends
		return super.onOptionsItemSelected(item);
	} //method onOptionsItemSelected
	
	//Private Methods////////////////////////////////////////////////////////////////////////////
	/**
	 * This method sets up the game, initializing the imageButtons, click events, timer and score.
	 */
	private void _init() {
		//initialize variables
		this._cardOne = -1;
		this._cardTwo = -1;
		this._firstSelection = true;
		this._score = 0;
		
		//generate the 8 pairs of cards
		this._cards = pickEightCards();
				
		//get the scoreTextView and set it to display 0
		this._scoreTextView = (TextView)findViewById(R.id.scoreTextView);
		this._scoreTextView.setText("Score: 0");
				
		//get the timerTextView and set it to display 30
		this._timerTextView = (TextView)findViewById(R.id.timerTextView);
		this._timerTextView.setText("Timer: 30");
		
		//get the infoTextView and set it to display Pick a card, any card!
		this._infoTextView = (TextView)findViewById(R.id.infoTextView);
		this._infoTextView.setText("Pick a card, any card!");
				
		//set up all of the buttons for click
		for(int i = _imageButtons.length - 1; i >= 0; i--) {
					
			//link each ImageButton to its id
			this._imageButtons[i] = (ImageButton)findViewById(this._ids[i]);
			
			//set the imageButton to display the cardBack, this is to ensure no cards stay flipped on reset
			this._imageButtons[i].setImageResource(getResources().getIdentifier("cardback", "drawable", getPackageName()));
			
			//set the first card's background colour to white, this is to ensure no cards stay highlighted on reset
			this._imageButtons[i].setBackgroundColor(Color.rgb(255, 255, 255));
			
			//keep track of the cards index, necessary for use in the onClick event
			final int cardIndex = i;
					
			//pick a random card from all of the possibilities
			final String cardString = this._cards.get((int)(Math.random() * this._cards.size()));
					
			//remove the card that was picked so it can't be picked again
			this._cards.remove(cardString);
			
			//set the image to be visible
			this._imageButtons[i].setVisibility(View.VISIBLE);
					
			//set up the click event for the ImageButton
			this._imageButtons[i].setOnClickListener(new View.OnClickListener() {
						
				@Override
				public void onClick(View v) {
					//call cardPicked when the button is clicked, sending its array index and
					//the string the card generated
					_cardPicked(cardIndex, cardString);
				} //method onClick ends
			});
		} //for ends
	} //method init ends
	
	/**
	 * This method takes care of all of the actions that occur when a card is clicked.
	 * @param card The array index of the card that was picked.
	 * @param cardString The name of the card that was picked.
	 */
	private void _cardPicked(int card, String cardString) {
		//if it's the first card selected, start the timer
		if(this._firstSelection) {
			
			//set firstSelection to false
			this._firstSelection = false;
			
			//timer starts at 30000 milliseconds, decrementing every 1000
			this.timer = new CountDownTimer(30000, 1000) {

				//onTick update the timerTextView with how many seconds are remaining
			     public void onTick(long millisUntilFinished) {
			         _timerTextView.setText("Timer: " + millisUntilFinished / 1000);
			     } //method onTick ends

			     //when the timer finishes
			     public void onFinish() {
			    	 _endGame();
			     } //method onFinish ends
			  }.start(); //start the timer
		} //if ends
		
		//if both cards haven't already been picked
		if(this._cardTwo == -1) {
			//check whether the first card or second card has been selected
			//if its the first card, set cardOneString equal to the cardString
			//and cardOne equal to its array index, else do the same but for cardTwo/CardTwoString
			if(this._cardOne == -1) {
				this._cardOne = card;
				this._cardOneString = cardString;
				
				//set infoText view to display Now pick another card!
				this._infoTextView.setText("Now pick another card!");
			
				//display the card's face image
				this._imageButtons[this._cardOne].setImageResource(getResources().getIdentifier(cardString, "drawable", getPackageName()));
				
				//set the background colour to red
				this._imageButtons[this._cardOne].setBackgroundColor(Color.rgb(255, 0, 0));
			} //if ends
			else {
				this._cardTwo = card;
				this._cardTwoString = cardString;
			
				//display the cards face image
				this._imageButtons[this._cardTwo].setImageResource(getResources().getIdentifier(cardString, "drawable", getPackageName()));
			} //else ends
		
			//if a second card has been selected
			if(this._cardTwo != -1) {
			
				//if the two strings match, but are not the same card picked twice...
				if(this._cardOneString == this._cardTwoString && this._cardOne != this._cardTwo) {
					//set both cards to be invisible
					this._imageButtons[this._cardOne].setVisibility(View.INVISIBLE);
					this._imageButtons[this._cardTwo].setVisibility(View.INVISIBLE);
					
					//increase the player's score
					this._score++;
					
					this._infoTextView.setText("Right!");
					
					//display the updated score
					this._scoreTextView.setText("Score: " + this._score);
					
					//set both cards to be unselected
					this._cardOne  = -1;
					this._cardTwo = -1;
					
					//if the user has matched all of the cards...
					if(this._score == this.SCORE_TO_WIN) {
						//stop the timer
						this.timer.cancel();
						
						//call endGame
						this._endGame();
					} //if ends
				} //if ends
				
				//else if the user chose the same card twice
				else if(this._cardOne == this._cardTwo) {
					this._infoTextView.setText("Pick a card, any card!");
					
					//set the card's background color back to white
					this._imageButtons[this._cardOne].setBackgroundColor(Color.rgb(255, 255, 255));
					
					//flip the card back over
					_imageButtons[_cardOne].setImageResource(getResources().getIdentifier("cardback", "drawable", getPackageName())); 
					
					//set both cards to be unselected
					this._cardOne  = -1;
					this._cardTwo = -1;
				} //else if
				
				//otherwise the user got the card wrong
				else {
					this._infoTextView.setText("Wrong! Pick again....");
					
					//wait half a second and then flip the second card back over
					this._handler.postDelayed(new Runnable() { 
						public void run() {
							_imageButtons[_cardTwo].setImageResource(getResources().getIdentifier("cardback", "drawable", getPackageName())); 
			 			 
							//set cardTwo to be unselected
							_cardTwo = -1;
						} //method run ends
					}, 500);
				} //else ends
			} //if ends
		} //if ends
	} //method cardPicked ends
	
	/**
	 * This method displays the end game AlertDialog and presents the user the option to play
	 * again or exit the app.
	 */
	public void _endGame() {
	//display 0 on the timer
   	 this._timerTextView.setText("Timer: 0");
   	 
   	 //set up the AlertDialog Builder
   	 AlertDialog.Builder builder = new AlertDialog.Builder(this._context);
   	 
   	 //set the title as Game Over
   	 builder.setTitle("Game Over");
   	 
   	 //display the user's final score
        builder.setMessage("Final Score: " + _score);
        
        //allow the dialog to be canceled
        builder.setCancelable(true);
        
        //set up the positive button to allow the user to play again
        builder.setPositiveButton("Play Again?", new DialogInterface.OnClickListener() {
       	 public void onClick(DialogInterface dialog, int id) {
       		 	_init(); //call init
                dialog.cancel(); //close the dialog
            } //method onClick ends
        });
        
        //set up the negative button to close the app
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
       	 public void onClick(DialogInterface dialog, int id) {
       		 System.exit(0); //close the app
       	 } //method onClick ends
        });

        //create and display the AlertDialog
        AlertDialog alert = builder.create();
        alert.show();
	} //method endGame ends
	
	/**
	 *  This method picks eight cards out of a 52 card deck and shuffles 2 copies of each into a
	 *  16 member ArrayList.
	 * @return An ArrayList with 8 pairs of random cards
	 */
	private static ArrayList<String> pickEightCards() {
		Random random = new Random();
		ArrayList<String> deckOfCards = new ArrayList<String>();
		String[] cardSuit={"c","d","h","s"};
		ArrayList<String> cardSelected = new ArrayList<String>();
		String card;
		
		// create ArrayList of cards
		for (String suit : cardSuit) 
			for (int index = 1; index < 14; index++) 
				deckOfCards.add("card_"+index+suit);

		// remove eight random cards and put them into another list
		for (int index = 0; index < 8; index++) {
			card = deckOfCards.remove(random.nextInt(deckOfCards.size()));
			// add the same random card to the new list twice
			cardSelected.add(card);
			cardSelected.add(card);
		} // end for
		
		// shuffle the card list
		Collections.shuffle(cardSelected);
		
		return cardSelected;
	} //end method pickEightCards
} // end card memory game
