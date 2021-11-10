

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    @Before
    public PebbleGame setUp()
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
        PebbleGame testGame = setUp();
        assertEquals("Number of players did not match expected value", 3, testGame.getNumberOfPlayers(), 0);
        for (int[] range : testGame.getRangess()) {
          for (int i = 0; i < range.legnth; i ++) {

          }
        }

    }

    public void initialiseWhiteBags()
    {
      PebbleGame testGame = setUp();
      ArrayList<Bag> whiteBags = testGame.getWhiteBags();

      for (Bag whiteBag : whiteBags) {
        assertSame("White bag is incorrect colour", "White", whiteBag.getColour());
      }

      for (int i; i < whiteBags.size(); i ++) {
        switch (i) {
          case 0:
            assertSame("White bag has incorrect name", "A", whiteBag.getName());
            break;
          case 1:
            assertSame("White bag has incorrect name", "B", whiteBag.getName());
            break;
          default:
            assertSame("White bag has incorrect name", "C", whiteBag.getName());
            break;
        }
      }
    }
}
