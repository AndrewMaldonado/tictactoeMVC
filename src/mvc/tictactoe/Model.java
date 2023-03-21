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
  private boolean whoseMove;
  private boolean gameOver;
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
    this.mvcMessaging.subscribe("gameOver", this);
    whoseMove = false;
    gameOver = false;

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

        if(!this.gameOver) {
            
        }
    
    // newGame message handler
        if(this.gameOver) {
            System.out.println("Game Over");
        }
    

        
    } else if (messageName.equals("newGame")) {
      // Reset the app state
      this.newGame();
      // Send the boardChange message along with the new board 
      this.mvcMessaging.notify("boardChange", this.board);
    }

  }
  
   public String isWinner() {
    for (int i=0; i<3; i++) {
        if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && board[i][0] != null) {
            this.mvcMessaging.notify("gameOver");
        } 
    }
    for(int i = 1; i<3; i++) {
        if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && board[0][i] != null) {
            this.mvcMessaging.notify("gameOver");
        } 
    }

    // Check the diagonals
    if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])) {
        this.mvcMessaging.notify("gameOver");
    }
    if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])) {
        this.mvcMessaging.notify("gameOver");
    }

  // If we haven't found it, then return a blank string
    return "";
    }
}
