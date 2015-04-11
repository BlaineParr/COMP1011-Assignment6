package ca.georgian.comp1011_assignment5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	//instance variables
	private int _cardOne, _cardTwo;
	private String _cardOneString, _cardTwoString;
	private ArrayList<String> _cards;
	private int _score;
	private int _pickCounter = 0;
	private Handler _handler = new Handler();
	private TextView _scoreTextView;
	private TextView _timerTextView;
	private Context _context = this;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	} //method onCreate ends

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//My Methods////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method sets up the game, initializing the imageButtons, click events, timer and score.
	 */
	public void init() {
		//initialize variables
		this._cardOne = -1;
		this._cardTwo = -1;
		this._pickCounter = 0;
		
		//generate the 8 pairs of cards
		this._cards = pickEightCards();
				
		//get the scoreTextView and set it to display 0
		this._scoreTextView = (TextView)findViewById(R.id.scoreTextView);
		this._scoreTextView.setText("Score: 0");
				
		//get the timerTextView
		this._timerTextView = (TextView)findViewById(R.id.timerTextView);
		this._timerTextView.setText("Timer: 30");
				
		//set up all of the buttons for click
		for(int i = _imageButtons.length - 1; i >= 0; i--) {
					
			//link each ImageButton to its id
			this._imageButtons[i] = (ImageButton)findViewById(this._ids[i]);
			
			//set the imageButton to display the cardBack, this is to ensure no cards stay flipped on reset
			this._imageButtons[i].setImageResource(getResources().getIdentifier("cardback", "drawable", getPackageName()));
			
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
					cardPicked(cardIndex, cardString);
				} //method onClick ends
			});
		} //for ends
	} //method init ends
	
	/**
	 * This method takes care of all of the actions that occur when a card is clicked.
	 * @param card The array index of the card that was picked.
	 * @param cardString The name of the card that was picked.
	 */
	public void cardPicked(int card, String cardString) {
		//if it's the first card selected, start the timer
		if(this._pickCounter == 0) {
			//timer starts at 30000 milliseconds, decrementing every 1000
			new CountDownTimer(30000, 1000) {

				//onTick update the timerTextView with how many seconds are remaining
			     public void onTick(long millisUntilFinished) {
			         _timerTextView.setText("Timer: " + millisUntilFinished / 1000);
			     } //method onTick ends

			     //when the timer finishes
			     public void onFinish() {
			    	 //display 0 on the timer
			    	 _timerTextView.setText("Timer: 0");
			    	 
			    	 //set up the AlertDialog Builder
			    	 AlertDialog.Builder builder = new AlertDialog.Builder(_context);
			    	 
			    	 //set the title as Game Over
			    	 builder.setTitle("Game Over");
			    	 
			    	 //display the user's final score
			         builder.setMessage("Final Score: " + _score);
			         
			         //allow the dialog to be canceled
			         builder.setCancelable(true);
			         
			         //set up the positive button to allow the user to play again
			         builder.setPositiveButton("Play Again?", new DialogInterface.OnClickListener() {
			        	 public void onClick(DialogInterface dialog, int id) {
			        		 init(); //call init
			                 dialog.cancel(); //close the dialog
			             } //method onClick ends
			         });
			         
			         //set up the negative button to close the app
			         builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			        	 public void onClick(DialogInterface dialog, int id) {
			        		 System.exit(0); //close the app
			        	 } //method onClick ends
			         });

			         AlertDialog alert11 = builder.create();
			         alert11.show();
			     } //method onFinish ends
			  }.start(); //start the timer
		} //if ends
		
		//if both cards haven't already been picked
		if(this._cardTwo == -1) {
			//check whether the first card or second card has been selected
			//if its the first card, set cardOneString equal to the cardString
			//and cardOne equal to its array index, else do the same but for cardTwo/CardTwoString
			if(this._pickCounter % 2 == 0) {
				this._cardOne = card;
				this._cardOneString = cardString;
			
				//display the cards face image
				this._imageButtons[this._cardOne].setImageResource(getResources().getIdentifier(cardString, "drawable", getPackageName()));
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
					
					//display the updated score
					this._scoreTextView.setText("Score: " + this._score);
				} //if ends
			
				//wait half a second and then flip the cards back over
				this._handler.postDelayed(new Runnable() { 
					public void run() {
						_imageButtons[_cardOne].setImageResource(getResources().getIdentifier("cardback", "drawable", getPackageName()));
						_imageButtons[_cardTwo].setImageResource(getResources().getIdentifier("cardback", "drawable", getPackageName())); 
		 			 
						//set both cards to -1
						_cardOne = -1;
						_cardTwo = -1;
					} //method run ends
				}, 500);
			} //if ends
		
			//increase the pickCounter
			this._pickCounter++;
		} //if ends
	} //method cardPicked ends
	
	//other stuff DELETE THIS COMMENT LATER/////////////////////////////////////////////////////
	// pick eight cards out of a 52 card deck and shuffle 2 copies of each into a 16 member ArrayList
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
	} // end pickEightCards method

} // end card memory game
