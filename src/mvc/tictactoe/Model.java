package mvc.tictactoe;

import com.mrjaffesclass.apcs.messenger.*;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {

  // Messaging system for the MVC
  private final Messenger mvcMessaging;
  private boolean whoseMove = false;
  private boolean gameOver = false;
  private String[][] board;

  // Model's data variables

  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
    mvcMessaging = messages;
    this.board = new String[3][3];
  }
  
  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
    this.newGame();
    this.mvcMessaging.subscribe("playerMove", this);
    this.mvcMessaging.subscribe("newGame", this);

  }
  
    private void newGame() {
    for(int row=0; row<this.board.length; row++) {
      for (int col=0; col<this.board[0].length; col++) {
        this.board[row][col] = "";
      }
    }
    this.whoseMove = false;
    this.gameOver = false;
  }

  
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
       // Display the message to the console for debugging
    if (messagePayload != null) {
      System.out.println("MSG: received by model: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by model: "+messageName+" | No data sent");
    }
    String winner = this.isWinner();
    // playerMove message handler
    if (messageName.equals("playerMove")) {
      // Get the position string and convert to row and col
      String position = (String)messagePayload;
      Integer row = Integer.valueOf(position.substring(0,1));
      Integer col = Integer.valueOf(position.substring(1,2));
      



      // If square is blank...
      
        if (this.board[row][col].equals("")) {
            // ... then set X or O depending on whose move it is
            if (this.whoseMove) {
                this.board[row][col] = "X";
                this.whoseMove = false;
            } else {
                this.board[row][col] = "O";
                this.whoseMove = true;
            }
        // Send the boardChange message along with the new board 
        this.mvcMessaging.notify("boardChange", this.board);
        }      
    // newGame message handler
    
    
        if (!winner.equals("")) {
            this.gameOver = true;
            this.mvcMessaging.notify("gameOver", winner);
            System.out.println("Game Over");
        }
        
    } else if (messageName.equals("newGame")) {
      // Reset the app state
      this.newGame();
      // Send the boardChange message along with the new board 
      this.mvcMessaging.notify("boardChange", this.board);
    }

  }

      private String isWinner() {
  // Get the text contents of each button.  
  // Be sure you're accessing the
  // buttons in the order you want.  
  // The Netbeans UI sometimes mixes up
  // the numbers on the button names so they're not in the 
  // order you expect
  String[][] status = new String[3][3];
  status[0][0] = board[0][0];
  status[0][1] = board[0][1];
  status[0][2] = board[0][2];
  status[1][0] = board[1][0];
  status[1][1] = board[1][1];
  status[1][2] = board[1][2];
  status[2][0] = board[2][0];
  status[2][1] = board[2][1];
  status[2][2] = board[2][2];

  // Check the rows and columns for a tic tac toe
  for (int i=0; i<3; i++) {
    if (status[i][0].equals(status[i][1]) && status[i][0].equals(status[i][2]))
      return status[i][0];
  }
   for(int i = 1; i<3; i++) {
    if (status[0][i].equals(status[1][i]) && status[0][i].equals(status[2][i]))
      return status[0][i];
  }

  // Check the diagonals
  if (status[0][0].equals(status[1][1]) && status[0][0].equals(status[2][2]))
    return status[0][0];
  if (status[0][2].equals(status[1][1]) && status[0][2].equals(status[2][0]))
    return status[0][2];

  // If we haven't found it, then return a blank string
  return "";
}

  
  

}
