

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * The test class PebbleGameTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class PebbleGameTest
{
    /**
     * Default constructor for test class PebbleGameTest
     */
    public PebbleGameTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
   
    public PebbleGame setUpPebbleGame()
    {
      String f = "testRanges.txt";
      PebbleGame testGame = new PebbleGame(3, f, f, f);

      return testGame;
    }
    
    

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }


    @Test
    public void PebbleGameTest()
    {
        PebbleGame testGame = setUpPebbleGame();
        assertEquals("Number of players did not match expected value", 3, testGame.getNumberOfPlayers(), 0);
        for (int[] range : testGame.getRangess()) {
          for (int i = 0; i < range.length; i ++) {

          }
        }

    }
    
    @Test
    public void initialiseWhiteBagsTest()
    {
      PebbleGame testGame = setUpPebbleGame();
      ArrayList<Bag> whiteBags = testGame.getWhiteBags();

      for (Bag whiteBag : whiteBags) {
        assertSame("White bag is incorrect colour", "White", whiteBag.getColour());
      }

      for (int i = 0; i < whiteBags.size(); i ++) {
        switch (i) {
          case 0:
            assertSame("White bag has incorrect name", "A", whiteBags.get(i).getName());
            break;
          case 1:
            assertSame("White bag has incorrect name", "B", whiteBags.get(i).getName());
            break;
          default:
            assertSame("White bag has incorrect name", "C", whiteBags.get(i).getName());
            break;
        }
      }
    }
    
    @Test
    public void checkBlackBagsEmptyTest()
    {
        PebbleGame testGame = setUpPebbleGame();
        ArrayList<Bag> blackBags = testGame.getBlackBags();
        
         
        assertFalse("Empty Black bags are not being detected" , testGame.checkBlackBagsEmpty());  
        
        for (Bag blackBag : blackBags) {
            blackBag.getPebbles().clear();
        }
        
        testGame.setBlackBags(blackBags);
        
        assertTrue("Empty Black bags are being detected", testGame.checkBlackBagsEmpty());  

    }
    
    @Test
    public void calculateScoreTest()
    {
        Object lock = new Object();
        PebbleGame testGame = setUpPebbleGame();
        
        PebbleGame.Player testPlayer = testGame.new Player(lock);
        ArrayList<Pebble> pebs = new ArrayList<Pebble>();
        for (int i = 0; i < 10; i ++)
        {
            pebs.add(new Pebble(i+1));
        }
        testPlayer.setPebbles(pebs);
        
        assertEquals("calculateScore() is incorrect", 55, testPlayer.calculateScore());
    }
    
    @Test
    public void removeElementFromArrayTest() {
      PebbleGame testGame = setUpPebbleGame();
      
      int[] testArray2 = {1, 2, 3, 4, 5};
      

      int[] testArray3 = testGame.removeElementFromArray(testArray2, 2);
      
      testGame.removeElementFromArray(testArray2, 2);
      assertEquals("The element at index 2 should have been removed.", testArray3[2], testArray2[3]);
    }
    
    @Test
    public void initialiseBlackBagsTest() {

      PebbleGame testGame = setUpPebbleGame();
      ArrayList<Bag> blackBags = testGame.getBlackBags();

      for (Bag blackBag : blackBags) {
        assertSame("Black bag is incorrect colour", "Black", blackBag.getColour());
      }

      for (int i = 0; i < blackBags.size(); i ++) {
        switch (i) {
          case 0:
            assertSame("White bag has incorrect name", "X", blackBags.get(i).getName());
            break;
          case 1:
            assertSame("White bag has incorrect name", "Y", blackBags.get(i).getName());
            break;
          default:
            assertSame("White bag has incorrect name", "Z", blackBags.get(i).getName());
            break;
        }
      }

      for (Bag blackBag : blackBags) {
        assertNotSame("Black bag is empty", 0, blackBag.getPebbles().size());
      }
    }
    
    @Test
    public void readInRangesTest() {
      int[] range = PebbleGame.readInRanges("testRanges.txt");
      int[] validValues = {8, 9, 10, 11, 12};

      assertSame("Range is incorrect length", 100, range.length);

  		for (int i = 0; i < range.length; i ++) {
  			boolean contains = PebbleGame.contains(validValues, range[i]);
  			assertTrue("Ranges contains a value not defined in the input file.", contains);
  		}
    }
}



















