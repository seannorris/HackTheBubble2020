package logic.piece;

import logic.Game;
import logic.Game.Coord;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a piece on the game board or in a player's hand.  Contains the piece's position, player, and movable directions.
 */
public abstract class Piece
{
    private Set<Move> moveDirections;
    private boolean player2;
    private Coord position;
    private final boolean promotable;
    private final char representation;
    private int id;
    
    private static int nextID = 0;
    
    /**
     * Constructor for the Piece class with id specified.
     * @param moveDirections An array of the directions the piece can move.
     * @param player2 A boolean that is true if the piece belongs to player 2.
     * @param position A Coord object containing the piece's coordinate on the board, or null if in hand.
     * @param promotable A boolean representing if the piece has a special action upon reaching the last row.
     * @param representation A character representing the piece.
     * @param id An ID which is unique for each piece, so that multiple objects can represent the same piece.
     */
    protected Piece(Move[] moveDirections, boolean player2, Coord position, boolean promotable, char representation, int id)
    {
        this(promotable, representation);
        this.moveDirections = new HashSet<>(Arrays.asList(moveDirections));
        if(player2)
            reverseDirection();
        
        this.player2 = player2;
        this.position = position;
        this.id = id;
    }
    
    /**
     * Constructor for the Piece class with auto-id.
     * @param moveDirections An array of the directions the piece can move.
     * @param player2 A boolean that is true if the piece belongs to player 2.
     * @param position A Coord object containing the piece's coordinate on the board, or null if in hand.
     * @param promotable A boolean representing if the piece has a special action upon reaching the last row.
     * @param representation A character representing the piece.
     */
    protected Piece(Move[] moveDirections, boolean player2, Coord position, boolean promotable, char representation)
    {
        this(moveDirections, player2, position, promotable, representation, getNextID());
    }
    
    /**
     * Constructor for the piece with only subclass relevant information.
     * @param promotable A boolean representing if the piece has a special action upon reaching the last row.
     * @param representation A character representing the piece.
     */
    protected Piece(boolean promotable, char representation)
    {
        this.promotable = promotable;
        this.representation = representation;
    }
    
    /**
     * Copies the move directions to a new set.
     * @return The copied set.
     */
    protected Set<Move> getMoveDirectionsCopy()
    {
        return new HashSet<>(moveDirections);
    }
    
    /**
     * Auto generates a new unique id.
     * @return The new id.
     */
    private static int getNextID()
    {
        return nextID++;
    }
    
    /**
     * Gets a new copied version of the current piece object with subclass specific details only.
     * @param game The game the new piece should belong to.
     * @return The new copied piece instance.
     */
    protected abstract Piece getCopiedInstance(Game game);
    
    /**
     * Gets a full copy of the piece using the getCopiedInstance method then setting common values.
     * @param game The game the new piece should belong to.
     * @return The copied piece.
     */
    public Piece getCopy(Game game)
    {
        var out = getCopiedInstance(game);
        out.moveDirections = getMoveDirectionsCopy();
        out.player2 = player2;
        out.position = position;
        out.id = id;
        return out;
    }
    
    /**
     * Inverts the move directions of the piece (i.e. change player).
     */
    private void reverseDirection()
    {
        var old = moveDirections;
        moveDirections = new HashSet<>(old.size());
        old.forEach(x -> moveDirections.add(x.getReversed()));
    }
    
    /**
     * Getter for piece position.
     * @return Coord object containing piece position.
     */
    public Coord getPosition()
    {
        return position;
    }
    
    /**
     * Getter for promotable.
     * @return Boolean; true if piece can be promoted.
     */
    public boolean isPromotable()
    {
        return promotable;
    }
    
    /**
     * Checks if the specified move is one the piece can make (solely on directions the piece can move).
     * @param move The move to check.
     * @return Boolean; true if move is possible.
     */
    public boolean checkMoveDirection(Move move)
    {
        return moveDirections.contains(move);
    }
    
    /**
     * Called when the piece is moved.
     * @param newPosition The position the piece has been moved to.
     * @return The piece to place in the new position (can be different in case of promotion).
     */
    public Piece moved(Coord newPosition)
    {
        position = newPosition;
        return this;
    }
    
    /**
     * Called if the piece is promoted by moving beyond the last row.
     * @return The promoted piece, e.g. a rooster.
     */
    public Piece promoted()
    {
        return this;
    }
    
    /**
     * Called when the piece is captured.
     * @return The captured piece, belonging to the other player.  May be different object in case of rooster.
     */
    public Piece captured()
    {
        setPlayer(!player2);
        position = null;
        return this;
    }
    
    /**
     * Called when the piece is placed on the board.
     * @param position The position the piece has been placed.
     * @return The placed piece object.
     */
    public Piece placed(Coord position)
    {
        this.position = position;
        return this;
    }
    
    /**
     * Sets the player the piece belongs to and, if different, flips move directions.
     * @param player2 The new player2 boolean value.
     */
    private void setPlayer(boolean player2)
    {
        if(player2 == this.player2)
            return;
        
        reverseDirection();
        this.player2 = player2;
    }
    
    /**
     * Getter for player2.
     * @return Boolean; true if piece belongs to player 2.
     */
    public boolean isPlayer2()
    {
        return player2;
    }
    
    /**
     * Returns true if the specified coordinate is in the last row for the piece.
     * @param position The position to check.
     * @return Boolean; true if in the last row.
     */
    public boolean isLastRow(Coord position)
    {
        return player2 ? position.getY() == 0 : position.getY() == Game.HEIGHT - 1;
    }
    
    /**
     * Returns true if the specified coordinate is one row off the board past the last row.
     * @param position The position to check.
     * @return Boolean; true if just past the last row.
     */
    public boolean isPromotionRow(Coord position)
    {
        return player2 ? position.getY() == -1 : position.getY() == Game.HEIGHT;
    }
    
    /**
     * Returns true if the piece could move to a certain coordinate.
     * (doesn't take into account being off board or occupied)
     * @param newPosition The position to check.
     * @return Boolean, true if move is possible.
     */
    public boolean canMoveTo(Coord newPosition)
    {
        if(position == null)
            return false;
        
        if(Math.abs(Math.abs(newPosition.getX()) - Math.abs(position.getX())) > 1
        || Math.abs(Math.abs(newPosition.getY()) - Math.abs(position.getY())) > 1)
            return false;
        
        return checkMoveDirection(getMove(newPosition));
    }
    
    /**
     * Gets a move enum value for specified coordinate.
     * @param newPosition The coordinate to get the move to.
     * @return The Move enum value.
     */
    public Move getMove(Coord newPosition)
    {
        return Move.fromDelta(newPosition.getX() - position.getX(),
                newPosition.getY() - position.getY());
    }
    
    /**
     * Returns true if the specified piece can be captured by this piece.
     * @param piece The piece to check.
     * @return Boolean; true if piece can be taken.
     */
    public boolean canCapture(Piece piece)
    {
        return piece.isPlayer2() != player2
                && canMoveTo(piece.getPosition());
    }
    
    /**
     * Getter for representation.
     * @return The character representation of the piece.
     */
    public char getRepresentation()
    {
        return representation;
    }
    
    /**
     * Getter for the possible move directions of the piece.
     * @return The set of Move enum values.
     */
    public Set<Move> getMoveDirections()
    {
        return moveDirections;
    }
    
    /**
     * Calculates the value of the piece for use in static board state analysis for the AI.
     * @return The value of the piece.
     */
    public int getValue()
    {
        return moveDirections.size() * 10;
    }
    
    /**
     * Checks if two pieces are equal (have same ID).
     * @param o The piece to compare to.
     * @return True if the piece has the same id as this piece.
     */
    @Override
    public boolean equals(Object o)
    {
        //Intellij auto generated
        if(this == o) return true;
        if(!(o instanceof Piece)) return false;
        Piece piece = (Piece)o;
        return id == piece.id;
    }
    
    /**
     * Used by hash sets and maps to improve efficiency.
     * Should be unique as possible and in this case is unique.
     * @return An as unique as possible integer value representing the piece.
     */
    @Override
    public int hashCode()
    {
        return id;
    }
    
    /**
     * Getter for ID of the piece.
     * @return The ID of the piece.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Enum representing possible move directions of a piece.
     */
    public enum Move
    {
        UP_LEFT,
        UP,
        UP_RIGHT,
        LEFT,
        RIGHT,
        DOWN_RIGHT,
        DOWN,
        DOWN_LEFT;
    
        /**
         * Returns the opposite move to this.
         * @return The opposite move.
         */
        public Move getReversed()
        {
            switch(this)
            {
                case UP_LEFT:
                    return DOWN_RIGHT;
                case UP:
                    return DOWN;
                case UP_RIGHT:
                    return DOWN_LEFT;
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                case DOWN_RIGHT:
                    return UP_LEFT;
                case DOWN:
                    return UP;
                case DOWN_LEFT:
                    return UP_RIGHT;
            }
            throw new RuntimeException("invalid move type");
        }
    
        /**
         * Returns the x component of the move.
         * @return The x component.
         */
        public int getXDelta()
        {
            switch(this)
            {
                case LEFT:
                case UP_LEFT:
                case DOWN_LEFT:
                    return -1;
                case RIGHT:
                case UP_RIGHT:
                case DOWN_RIGHT:
                    return 1;
                default:
                    return 0;
            }
        }
    
        /**
         * Returns the y component of the move.
         * @return The y component.
         */
        public int getYDelta()
        {
            switch(this)
            {
                case UP:
                case UP_LEFT:
                case UP_RIGHT:
                    return 1;
                case DOWN:
                case DOWN_LEFT:
                case DOWN_RIGHT:
                    return -1;
                default:
                    return 0;
            }
        }
    
        /**
         * Gets a move from x and y components.
         * @param x The x component.
         * @param y The y component.
         * @return The move value.
         */
        public static Move fromDelta(int x, int y)
        {
            for(var move : values())
                if(x == move.getXDelta() && y == move.getYDelta())
                    return move;
                
            return null;
        }
    }
}
