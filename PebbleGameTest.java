import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * The test class PebbleGameTest. This class is used to test the functionality of a selection of
  methods in the project. If one of these tests fails, the PebbleGame will most likely not
  execute correctly.
 *
 * @author Sam, Matt
 * @version 1.0
 */
public class PebbleGameTest
{

    /**
     * Used to set up an instance of PebbleGame with 3 players and ranges from the file
     testRanges.txt.
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
     * Tests to make sure that the white bags have been set up correctly, specifically testing their
     colour and name variables.
     */

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

    /**
     * Used to test the function that check whether the black bags are empty or not. Tests the two
     possible outcomes - either at least one bag is empty or no bags are empty.
     */

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

    /**
     * Used to test the calculate score method. Simply used to ensure the method sums up the pebble
     weights correctly.
     */

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

    /**
     * Used to test the method that removes an element from an array and returns the array with the
     element removed.
     */

    @Test
    public void removeElementFromArrayTest() {
      PebbleGame testGame = setUpPebbleGame();

      int[] testArray2 = {1, 2, 3, 4, 5};


      int[] testArray3 = testGame.removeElementFromArray(testArray2, 2);

      testGame.removeElementFromArray(testArray2, 2);
      assertEquals("The element at index 2 should have been removed.", testArray3[2], testArray2[3]);
    }

    /**
     * Tests to ensure the black bags are correctly initialised. Works almost identically to the
     method that tests the white bags, with the additional functionality of ensuring that the bags
     are filled with pebbles upon creation.
     */

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

    /**

     */

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
