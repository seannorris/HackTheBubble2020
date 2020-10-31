package logic.player.ai;

import logic.Game;
import logic.piece.Piece;

import java.util.Stack;

/**
 * An extension of the Game class with details specific to the AI player.<br>
 * {@inheritDoc}
 */
public class AIGame extends Game
{
    private final Stack<Move> moves = new Stack<>();
    
    /**
     * Overrides the copying constructor of the game class.
     * Can only be created by copying an existing game.
     * @param game The game to copy.
     */
    public AIGame(Game game)
    {
        super(game);
    }
    
    /**
     * Calculates the static value of the board.
     * If the game is won return either a very large or very negative value depending on player.
     * Pieces in play are worth more than in hand.
     * Pieces further up the board are worth more, and this is amplified for promotable pieces.
     * Pieces in the centre column are worth more as they have more moves available.
     * @param player2 Which player is 'friendly'.
     * @return The value of the board.
     */
    public int value(boolean player2)
    {
        if(isWon()) //detect victory
            return 20000000 * (getWinner() == player2 ? 1 : -1);
        
        var out = 0;
        for(var entry : getBoard().entrySet())
        {
            var piece = entry.getValue();
            var coord = entry.getKey();
            out += getValue(player2, piece) *
                    ((player2 ? HEIGHT - coord.getY() - 1 : coord.getY()) * (piece.isPromotable() ? 2 : 1) + 1)
                    + coord.getX() == WIDTH / 2 || coord.getX() == (WIDTH - 1) / 2 ? 10 : 0;
        }
        for(var piece : getPlayer1Hand())
            out += getValue(player2, piece);
        for(var piece : getPlayer2Hand())
            out += getValue(player2, piece);
        
        return out;
    }
    
    /**
     * Returns the value of a piece, multiplied by -1 if owned by the other player.
     * @param player2 Which player is 'friendly'.
     * @param piece The piece to get the value of.
     * @return The multiplied value.
     */
    private int getValue(boolean player2, Piece piece)
    {
        return piece.getValue() * (piece.isPlayer2() == player2 ? 1 : -1);
    }
    
    /**
     * Rolls back one move from the move stack.
     * Calls {@link #rollback(int)} with numberMoves 1.
     */
    public void rollback()
    {
        rollback(1);
    }
    
    /**
     * Rolls back a specified number of moves from move stack.
     * @param numberMoves The number of moves to roll back.
     */
    public void rollback(int numberMoves)
    {
        if(numberMoves > 0)
            setWon(false);
        
        while(numberMoves > 0 && !moves.empty())
        {
            moves.pop().reverse();
            setPlayer2Turn(!isPlayer2Turn());
            numberMoves--;
        }
    }
    
    /**
     * {@inheritDoc}
     * In this case the move is also added to the move stack.
     */
    public void move(Piece piece, Piece.Move move)
    {
        var newPos = Coord.move(piece.getPosition(), move);
        moves.push(getBoard().containsKey(newPos)
                ? new TakeMove(new BoardPosition(piece.getPosition()), new BoardPosition(newPos), new HandPosition(isPlayer2Turn()))
                : new Move(new BoardPosition(piece.getPosition()), new BoardPosition(newPos)));
        super.move(piece, move);
        
    }
    
    /**
     * {@inheritDoc}
     * In this case the placement is also added to the move stack.
     */
    @Override
    public void place(Piece piece, Coord position)
    {
        moves.push(new Move(new HandPosition(isPlayer2Turn(), piece), new BoardPosition(position)));
        super.place(piece, position);
        
    }
    
    /**
     * Represents a move in the game that can be rolled back.
     * Stores a copy of the piece before the move that can be placed back to reverse it.
     */
    private class Move
    {
        private final Position oldPos;
        private final Piece oldPiece;
        private final Position newPos;
    
        /**
         * Constructor for move.
         * @param oldPos The position to move from.
         * @param newPos The position to move to.
         */
        public Move(Position oldPos, Position newPos)
        {
            this.oldPos = oldPos;
            this.oldPiece = oldPos.getPiece().getCopy(AIGame.this);
            this.newPos = newPos;
        }
    
        /**
         * Reverses the move by removing the piece at the new position and placing the copied piece in the original position.
         */
        public void reverse()
        {
            newPos.removePiece();
            oldPos.addPiece(oldPiece);
        }
    }
    
    /**
     * Represents a move where a piece is captured.
     */
    private class TakeMove extends Move
    {
        private final Move takenMove;
    
        /**
         * Constructor for TakeMove.
         * @param oldPos The position to move from.
         * @param newPos The position to move to.
         * @param takenNewPos The position to move the taken piece to.
         */
        public TakeMove(Position oldPos, Position newPos, Position takenNewPos)
        {
            super(oldPos, newPos);
            takenMove = new Move(newPos, takenNewPos);
        }
    
        /**
         * {@inheritDoc}
         * Then does the same for the captured piece.
         */
        public void reverse()
        {
            super.reverse();
            takenMove.reverse();
        }
    }
    
    /**
     * Represents the position of a piece on the board or in a player's hand.
     */
    private interface Position
    {
        /**
         * Returns the piece object stored at the position.
         * @return The piece object.
         */
        Piece getPiece();
    
        /**
         * Removes the piece stored at the position.
         */
        void removePiece();
    
        /**
         * Adds the specified piece to the position.
         * @param piece The piece to add.
         */
        void addPiece(Piece piece);
    }
    
    /**
     * Represents the position of a piece on the board.
     */
    private class BoardPosition implements Position
    {
        private final Coord position;
    
        /**
         * Constructor for BoardPosition.
         * @param position The position on the board.
         */
        public BoardPosition(Coord position)
        {
            this.position = position;
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public Piece getPiece()
        {
            return getBoard().get(position);
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void removePiece()
        {
            getBoard().remove(position);
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void addPiece(Piece piece)
        {
            getBoard().put(position, piece);
        }
    }
    
    /**
     * Represents the position of a piece in a player's hand.
     */
    private class HandPosition implements Position
    {
        private final boolean player2;
        private final int index;
    
        /**
         * Constructor for HandPosition.
         * @param player2 Boolean; true if the piece is in player 2's hand.
         * @param index The index of the piece in the player's hand.
         */
        public HandPosition(boolean player2, int index)
        {
            this.player2 = player2;
            this.index = index;
        }
    
        /**
         * Constructor for HandPosition.  Assumes piece is added to end of hand.
         * @param player2 Boolean; true if the piece is in player 2's hand.
         */
        public HandPosition(boolean player2)
        {
            this(player2, getPlayer(player2).getHand().size());
        }
    
        /**
         * Constructor for HandPosition.  Retrieves the index of the specified piece.
         * @param player2 Boolean; true if the piece is in player 2's hand.
         * @param piece The piece object.
         */
        public HandPosition(boolean player2, Piece piece)
        {
            this(player2, getPlayer(player2).getHand().indexOf(piece));
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public Piece getPiece()
        {
            return getPlayer(player2).getHand().get(index);
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void removePiece()
        {
            getPlayer(player2).getHand().remove(index);
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void addPiece(Piece piece)
        {
            getPlayer(player2).getHand().add(index, piece);
        }
    }
}
