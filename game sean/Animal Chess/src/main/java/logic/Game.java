package logic;

import logic.piece.Chick;
import logic.piece.Elephant;
import logic.piece.Giraffe;
import logic.piece.Lion;
import logic.piece.Piece;
import logic.player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a game of Animal Chess.
 */
public class Game
{
    private boolean player2Turn;
    private Player player1;
    private Player player2;
    private final Map<Coord, Piece> board;
    private boolean won;
    private boolean winner;
    private GameHandler handler;
    
    public static final int HEIGHT = 4;
    public static final int WIDTH = 3;
    
    /**
     * Constructor for the game.
     * @param handler The GameHandler object associated with the game.
     * @param player1 The player object for player 1.
     * @param player2 The player object for player 2.
     */
    private Game(GameHandler handler, Player player1, Player player2)
    {
        this.handler = handler;
        this.player1 = player1;
        this.player2 = player2;
        board = new HashMap<>();
    }
    
    /**
     * Constructor for the game with an array of pieces specified.
     * @param player1 The player object for player 1.
     * @param player2 The player object for player 2.
     * @param handler The GameHandler object associated with the game.
     * @param pieces The array of pieces.
     */
    public Game(Player player1, Player player2, GameHandler handler, Piece[] pieces)
    {
        this(handler, player1, player2);
        initialisePieces(pieces);
    }
    
    /**
     * Constructor for the game with default pieces.
     * @param player1 The player object for player 1.
     * @param player2 The player object for player 2.
     * @param handler The GameHandler object associated with the game.
     */
    public Game(Player player1, Player player2, GameHandler handler)
    {
        this(handler, player1, player2);
        initialisePieces(new Piece[]{
                new Elephant(false, Coord.at(0, 0)),
                new Lion(false, Coord.at(1, 0), this),
                new Giraffe(false, Coord.at(2, 0)),
                new Chick(false, Coord.at(1, 1)),
                new Chick(true, Coord.at(1, HEIGHT - 2)),
                new Giraffe(true, Coord.at(0, HEIGHT - 1)),
                new Lion(true, Coord.at(1, HEIGHT - 1), this),
                new Elephant(true, Coord.at(2, HEIGHT - 1))
        });
    }
    
    /**
     * Constructor to define a new game with the board filled but no players or game handler.
     */
    public Game()
    {
        this((Player)null, null, null);
    }
    
    /**
     * A constructor that creates a copy of an existing game.
     * Gets copies of pieces and players too.
     * @param toCopy The game to copy.
     */
    protected Game(Game toCopy)
    {
        board = new HashMap<>(toCopy.board.size());
        toCopy.board.forEach((coord, piece) -> board.put(coord, piece.getCopy(this)));
        this.player1 = toCopy.player1.getCopy(this);
        this.player2 = toCopy.player2.getCopy(this);
        this.player2Turn = toCopy.player2Turn;
        this.won = toCopy.won;
        this.winner = toCopy.winner;
        this.handler = toCopy.handler;
    }
    
    /**
     * Gets a copy of the game.
     * @return The copy.
     */
    public Game getCopy()
    {
        return new Game(this);
    }
    
    /**
     * Sets the players and game handler retrospectively.
     * @param player1 The player object for player 1.
     * @param player2 The player object for player 2.
     * @param handler The GameHandler object associated with the game.
     */
    public void initialise(Player player1, Player player2, GameHandler handler)
    {
        this.handler = handler;
        this.player1 = player1;
        this.player2 = player2;
    }
    
    /**
     * Adds each piece from an array of pieces to the board or to the appropriate player's hand.
     * @param pieces The array of pieces.
     */
    private void initialisePieces(Piece[] pieces)
    {
        for(var piece : pieces)
        {
            if(piece.getPosition() != null)
                board.put(piece.getPosition(), piece);
            else
                getPlayer(piece).getHand().add(piece);
        }
    }
    
    /**
     * Getter for the board.
     * @return The board.
     */
    public Map<Coord, Piece> getBoard()
    {
        return board;
    }
    
    /**
     * Returns the piece at a certain position.
     * @param position A Coord object containing the piece's position.
     * @return The piece.
     */
    public Piece getPiece(Coord position)
    {
        return board.get(position);
    }
    
    /**
     * Getter for player2Turn.
     * @return Boolean; true if it is currently player 2's turn.
     */
    public boolean isPlayer2Turn()
    {
        return player2Turn;
    }
    
    /**
     * Getter for player1.
     * @return The player object of player 1.
     */
    public Player getPlayer1()
    {
        return player1;
    }
    
    /**
     * Getter for player2.
     * @return The player object of player 2.
     */
    public Player getPlayer2()
    {
        return player2;
    }
    
    /**
     * Getter for player 1's hand.
     * @return The list of pieces in player 1's hand.
     */
    public List<Piece> getPlayer1Hand()
    {
        return player1.getHand();
    }
    
    /**
     * Getter for player 2's hand.
     * @return The list of pieces in player 2's hand.
     */
    public List<Piece> getPlayer2Hand()
    {
        return player2.getHand();
    }
    
    /**
     * Called when a player wins the game.
     * @param player2 Boolean representing the winner; true if the winner is player 2.
     */
    public void win(boolean player2)
    {
        won = true;
        winner = player2;
    }
    
    /**
     * Gets every possible move as a map of coordinates to sets of moves.
     * @return The map.
     */
    public Map<Coord, Set<Piece.Move>> possibleMoves()
    {
        var out = new HashMap<Coord, Set<Piece.Move>>(board.size());
    
        board.forEach((coord, piece) ->
        {
            var moves = new HashSet<Piece.Move>();
            for(var move : piece.getMoveDirections())
                if(canMove(piece, Coord.move(coord, move)))
                    moves.add(move);
        
            out.put(coord, moves);
        });
        
        return out;
    }
    
    /**
     * Gets every possible placement as a map of hand indexes of the current player to sets of moves.
     * @return The map.
     */
    public Map<Integer, Set<Coord>> possiblePlacements()
    {
        var out = new HashMap<Integer, Set<Coord>>(getPlayer(player2Turn).getHand().size());
    
        for(int i = 0; i < getPlayer(player2Turn).getHand().size(); i++)
        {
            var positions = new HashSet<Coord>();
            for(var x = 0; x < WIDTH; x++)
                for(var y = 0; y < HEIGHT; y++)
                {
                    var coord = Coord.at(x, y);
                    if(canPlace(getPlayer(player2Turn).getHand().get(i), coord))
                        positions.add(coord);
                }
        
            out.put(i, positions);
        }
        
        return out;
    }
    
    /**
     * Returns true if a specified piece can move to a new position.
     * Checks if the piece is allowed to move this turn.
     * Checks if the piece is contained in the board.
     * Checks if the piece can actually move to the new position.
     * Checks if the new position is on the board or is a valid promotion.
     * Checks if the new position is either empty or contains an enemy.
     * @param piece The piece to check.
     * @param newPosition The new position to check.
     * @param skipPlayerCheck If this is true then return true, even if the piece does not belong to the player who's turn it is.
     * @return Boolean; true if the move is valid.
     */
    public boolean canMove(Piece piece, Coord newPosition, boolean skipPlayerCheck)
    {
        return (piece.isPlayer2() == player2Turn || skipPlayerCheck)
                && board.containsValue(piece) && piece.canMoveTo(newPosition)
                && ((piece.isPromotable() && piece.isPromotionRow(newPosition) && newPosition.getX() == piece.getPosition().getX())
                    ||  newPosition.getX() >= 0 && newPosition.getX() < WIDTH && newPosition.getY() >= 0 && newPosition.getY() < HEIGHT)
                && (!board.containsKey(newPosition) || piece.isPlayer2() != board.get(newPosition).isPlayer2());
    }
    
    /**
     * Returns true if a specified piece can move to a new position.
     * Calls {@link #canMove(Piece, Coord, boolean)} with skipPlayerCheck false.
     * @param piece The piece to check.
     * @param newPosition The new position to check.
     * @return Boolean; true if the move is valid.
     */
    public boolean canMove(Piece piece, Coord newPosition)
    {
        return canMove(piece, newPosition, false);
    }
    
    /**
     * Returns true if a specified piece can be placed in a specified position.
     * Checks if the piece can be placed this turn.
     * Checks if the piece is not contained in the board already.
     * Checks if the piece is in it's player's hand.
     * @param piece The piece to check.
     * @param position The position to check.
     * @param skipPlayerCheck If this is true then return true, even if the piece does not belong to the player who's turn it is.
     * @return Boolean; true if the placement is valid.
     */
    public boolean canPlace(Piece piece, Coord position, boolean skipPlayerCheck)
    {
        return (piece.isPlayer2() == player2Turn || skipPlayerCheck)
                && !board.containsKey(position)
                && getPlayer(piece).getHand().contains(piece);
    }
    
    /**
     * Returns true if a specified piece can be placed in a specified position.
     * Calls {@link #canPlace(Piece, Coord, boolean)} with skipPlayerCheck false.
     * @param piece The piece to check.
     * @param position The position to check.
     * @return Boolean; true if the placement is valid.
     */
    public boolean canPlace(Piece piece, Coord position)
    {
        return canPlace(piece, position, false);
    }
    
    /**
     * Performs the specified move on the specified piece.
     * @param piece The piece to perform the move on.
     * @param move The move to perform.
     */
    public void move(Piece piece, Piece.Move move)
    {
        var newPos = Coord.move(piece.getPosition(), move);
        if(!canMove(piece, newPos))
            return;
        
        var oldPiece = board.get(newPos);
        if(oldPiece != null)
            getEnemy(oldPiece).getHand().add(oldPiece.captured());
        
        if(piece.isPromotionRow(newPos))
            board.put(piece.getPosition(), piece.promoted());
        else
        {
            board.remove(piece.getPosition());
            board.put(newPos, piece.moved(newPos));
        }
        
        player2Turn = !player2Turn;
    }
    
    /**
     * Places the specified piece in the specified position.
     * @param piece The piece to place.
     * @param position - The position to place the piece.
     */
    public void place(Piece piece, Coord position)
    {
        if(!canPlace(piece, position))
            return;
        
        getPlayer(piece).getHand().remove(piece);
        board.put(position, piece.placed(position));
        
        player2Turn = !player2Turn;
    }
    
    /**
     * Call each player's doMove method in turn, interspersed with the GameHandler's moveEnd method.
     * When the game is over call the players' gameOver methods.
     */
    public void play()
    {
        while(!won)
        {
            getPlayer(player2Turn).doMove();
            handler.moveEnd();
        }
        player1.gameOver();
        player2.gameOver();
    }
    
    /**
     * Get the player object specified by a boolean.
     * @param player2 The boolean; true for player 2.
     * @return The appropriate player.
     */
    public Player getPlayer(boolean player2)
    {
        return player2 ? this.player2 : this.player1;
    }
    
    /**
     * Returns the player associated with a specified piece.
     * @param piece The piece.
     * @return The player object.
     */
    public Player getPlayer(Piece piece)
    {
        return getPlayer(piece.isPlayer2());
    }
    
    /**
     * Get the enemy player of a certain piece.
     * @param piece The piece.
     * @return The player object.
     */
    public Player getEnemy(Piece piece)
    {
        return getPlayer(!piece.isPlayer2());
    }
    
    /**
     * Getter for won.
     * @return Boolean; true if game is won.
     */
    public boolean isWon()
    {
        return won;
    }
    
    /**
     * Setter for won, available to subclasses.
     * @param won The new value for won.
     */
    protected void setWon(boolean won)
    {
        this.won = won;
    }
    
    /**
     * Getter for winner.
     * @return Boolean; true if player 2 is the winner, false if player 1.
     */
    public boolean getWinner()
    {
        return winner;
    }
    
    /**
     * Sets which player's turn it is.
     * @param player2Turn Boolean; true for player 2's turn.
     */
    public void setPlayer2Turn(boolean player2Turn)
    {
        this.player2Turn = player2Turn;
    }
    
    /**
     * Getter for the GameHandler.
     * @return The GameHandler object.
     */
    public GameHandler getHandler()
    {
        return handler;
    }
    
    /**
     * Represents a coordinate of a piece in the game board.
     */
    public static class Coord
    {
        private final int x;
        private final int y;
    
        /**
         * Constructor for Coord.
         * @param x The x coordinate.
         * @param y The y coordinate.
         */
        public Coord(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    
        /**
         * Shortcut method to get Coord object for specified position.
         * @param x The x coordinate.
         * @param y The y coordinate.
         * @return The Coord object.
         */
        public static Coord at(int x, int y)
        {
            return new Coord(x, y);
        }
    
        /**
         * Gets a new coordinate by applying the specified move to specified coordinate.
         * @param in The coordinate to apply the move to.
         * @param move The move to apply.
         * @return The new coordinate with the move applied.
         */
        public static Coord move(Coord in, Piece.Move move)
        {
            return new Coord(in.getX() + move.getXDelta(), in.getY() + move.getYDelta());
        }
    
        /**
         * Getter for the x coordinate.
         * @return The x coordinate.
         */
        public int getX()
        {
            return x;
        }
    
        /**
         * Getter for the y coordinate.
         * @return The y coordinate.
         */
        public int getY()
        {
            return y;
        }
    
        /**
         * Returns true if two coordinates are equal.
         * @param in The coordinate to check.
         * @return Boolean; true if the two coordinates are equal.
         */
        public boolean equals(Object in)
        {
            if(!(in instanceof Coord))
                return super.equals(in);
            var coord = (Coord)in;
            return coord.getX() == x && coord.getY() == y;
        }
    
        /**
         * Used by hash sets and maps to improve efficiency.
         * Should be unique as possible which is done by adding one component of the coordinate to a multiple of the other that is larger than can be found in the game.
         * @return An as unique as possible integer value representing the piece.
         */
        @Override
        public int hashCode()
        {
            return x + y * 2 * WIDTH;
        }
    }
}
