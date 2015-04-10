package ca.georgian.comp1011_assignment5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	//instance variables
	private ImageButton _cardOne, _cardTwo;
	private String _cardOneString, _cardTwoString;
	private ArrayList<String> _cards;
	private int _pickCounter = 0;
	
	
	private ImageButton _imageButton11, _imageButton12, _imageButton13, _imageButton14,
	_imageButton21, _imageButton22, _imageButton23, _imageButton24,
	_imageButton31, _imageButton32, _imageButton33, _imageButton34,
	_imageButton41, _imageButton42, _imageButton43, _imageButton44;
	
	private ImageButton[] _imageButtons = {_imageButton11, _imageButton12, _imageButton13, _imageButton14,
			_imageButton21, _imageButton22, _imageButton23, _imageButton24,
			_imageButton31, _imageButton32, _imageButton33, _imageButton34,
			_imageButton41, _imageButton42, _imageButton43, _imageButton44};
	
	private int _ids[] = {R.id.imageButton11, R.id.imageButton12, R.id.imageButton13, R.id.imageButton14,
			R.id.imageButton21, R.id.imageButton22, R.id.imageButton23, R.id.imageButton24,
			R.id.imageButton31, R.id.imageButton32, R.id.imageButton33, R.id.imageButton34,
			R.id.imageButton41, R.id.imageButton42, R.id.imageButton43, R.id.imageButton44};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this._cards = pickEightCards();
		
		//set up all of the buttons for click
		for(int i = _imageButtons.length - 1; i >= 0; i--) {
			this._imageButtons[i] = (ImageButton)findViewById(this._ids[i]);
			
			final int cardIndex = i;
			
			//pick a random card from all of the possibilities
			final String cardString = this._cards.get((int)(Math.random() * this._cards.size()));
			
			//remove the card that was picked so it can't be picked again
			this._cards.remove(cardString);
			
			this._imageButtons[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Log.e("Button", "Card picked: " + cardString);
					cardPicked(_imageButtons[cardIndex], cardString);
				}
			});
		} //for ends
	}

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
	public void cardPicked(ImageButton card, String cardString) {
		//check whether the first card or second card has been selected
		//if its the first card, set cardOneName equal to the cardName
		if(this._pickCounter % 2 == 0) {
			this._cardOne = card;
			this._cardOneString = cardString;
			
			Log.e("Button", "Card One: " + cardString);
		} //if ends
		
		//else set cardTwoName equal to the cardName
		else {
			this._cardTwo = card;
			this._cardTwoString = cardString;
			
			Log.e("Button", "Card Two: " + cardString);
		} //else ends
		
		//if a second card has been selected
		if(this._cardTwo != null) {
			
			//if the two strings match, but are not the same card picked twice...
			if(this._cardOneString == this._cardTwoString && this._cardOne != this._cardTwo) {
				Log.e("Button", "They Match!");
			} //if ends
			
			//set both cards to null
			this._cardOne = null;
			this._cardTwo = null;
		} //if ends
		
		//increase the pickCounter
		this._pickCounter++;
	} //method cardPicked ends
	
	//other stuff DELETE THIS COMMENT LATER/////////////////////////////////////////////////////
	public void load() {
		HashMap<Integer, String> grid = new HashMap<Integer, String>();
		boolean correctAnswer = false;
		
		Iterator<String> iterator = pickEightCards().iterator();
		//initialize the key
		int key=0;
		// build my Hashmap which I call "grid"
		while (iterator.hasNext()) {
			String value = (String) iterator.next();
			grid.put(key, value);
			key++;
		}
	
		do {
		String s1 = getInput("Enter the first memory location: ");
		String s2 = getInput("Enter the second memory location: ");
		
		System.out.println("The first memory location contains: " + grid.get(Integer.parseInt(s1)));
		System.out.println("The first memory location contains: " + grid.get(Integer.parseInt(s2)));
		
		if (grid.get(Integer.parseInt(s1)).equals(grid.get(Integer.parseInt(s2)))) {
			System.out.println("RIGHT!");
			correctAnswer = true;
		} // end if
		else {
			System.out.println("Try Again!");
		} // end else
		} while (correctAnswer == false);

	} // end main

	private static String getInput(String prompt) {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(prompt);
		System.out.flush();
		
		try {
			return stdin.readLine();
		} // end try 
		catch (Exception e) {
			return "Error: " + e.getMessage();
		} // end catch
		
	} // end getInput


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
