package logic.player.ai;

import logic.Game;
import logic.piece.Piece;
import logic.player.Player;
import ui.BlockingThread;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an AI player.<br>
 * {@inheritDoc}
 */
public class AIPlayer extends Player
{
    private final ui.game.Game game;
    private final int depth;
    
    /**
     * Constructor for AIPlayer.
     * @param player2 Boolean; true if player is player 2.
     * @param game The UI game handler the player belongs to.
     * @param name The name of the player.
     * @param depth How far down the tree to check when doing minmax.
     */
    public AIPlayer(boolean player2, ui.game.Game game, String name, int depth)
    {
        super(player2, name);
        this.game = game;
        this.depth = depth;
    }
    
    /**
     * {@inheritDoc}
     * In this case create a new AI processor thread and process UI input until it's done.
     * If the game is paused, wait for it to resume before doing move.
     */
    @Override
    public void doMove()
    {
        var processor = new AIMoveProcessor(game.getGame());
        try
        {
            if(!game.getTerminalHandler().await(processor) || game.getGame().isWon())
            {
                game.getGame().win(!isPlayer2());
                processor.kill();
                return;
            }
            while(game.isPaused() && game.getTerminalHandler().getInput() && !game.getGame().isWon());
            if(game.isPaused() || game.getGame().isWon())
            {
                if(!game.getGame().isWon())
                    game.getGame().win(!isPlayer2());
                
                processor.kill();
                return;
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        assert processor.getBestMove() != null;
        processor.getBestMove().doMoveOn(game.getGame());
    }
    
    /**
     * Represents a move that can be performed in the game.
     */
    private interface Move
    {
        /**
         * Does the move on a specified game.
         * @param game The game to do the move on.
         */
        void doMoveOn(Game game);
    }
    
    /**
     * Represents a move from one board position to another.
     */
    private static class BoardMove implements Move
    {
        private final Piece.Move move;
        private final Game.Coord coord;
    
        /**
         * Constructor for BoardMove.
         * @param coord The coord to move from.
         * @param move The move to perform.
         */
        public BoardMove(Game.Coord coord, Piece.Move move)
        {
            this.move = move;
            this.coord = coord;
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void doMoveOn(Game game)
        {
            game.move(game.getBoard().get(coord), move);
        }
    }
    
    /**
     * Represents a move from a player's hand to the board.
     */
    private static class PlaceMove implements Move
    {
        private final Game.Coord position;
        private final int index;
    
        /**
         * Constructor for PlaceMove
         * @param index The hand index to take the piece from.
         * @param position The position on the board to place the piece.
         */
        public PlaceMove(int index, Game.Coord position)
        {
            this.index = index;
            this.position = position;
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void doMoveOn(Game game)
        {
            game.place(game.getPlayer(game.isPlayer2Turn()).getHand().get(index), position);
        }
    }
    
    /**
     * Performs the minmax operation in parallel.  Uses one thread for each CPU core.<br>
     * {@inheritDoc}
     */
    private class AIMoveProcessor extends BlockingThread
    {
        private Move bestMove;
        private volatile Integer max;
        private final AIMoveThread[] threads;
        private volatile boolean stopped;
    
        /**
         * Constructor for AIMoveProcessor.
         * Creates a thread for each CPU core and assigns top level min operations for each possible core to each one in turn.
         * @param game The game to do the minmax process on.
         */
        public AIMoveProcessor(Game game)
        {
            threads = new AIMoveThread[Runtime.getRuntime().availableProcessors()];
            for(var x = 0; x < threads.length; x++)
                threads[x] = new AIMoveThread(game);
            
            var threadIndex = 0;
            for(var entry : game.possibleMoves().entrySet())
                for(var move : entry.getValue())
                {
                    threads[threadIndex].addMove(new BoardMove(entry.getKey(), move));
                    threadIndex = bound(threadIndex + 1);
                }
    
            for(var entry : game.possiblePlacements().entrySet())
                for(var position : entry.getValue())
                {
                    threads[threadIndex].addMove(new PlaceMove(entry.getKey(), position));
                    threadIndex = bound(threadIndex + 1);
                }
        }
    
        /**
         * Used to keep track of which thread to assign each move to.
         * If the input is outside the range of the threads array, set it to 0.
         * @param in The index to bound.
         * @return The bounded index.
         */
        private int bound(int in)
        {
            return in < threads.length ? in : 0;
        }
    
        /**
         * Checks if the result of a min operation is a new maximum and updates the stored maximum and best move is so.
         * @param result The result of the operation.
         * @param move The move that got that result.
         */
        private synchronized void checkMax(int result, Move move)
        {
            if(max == null || result > max)
            {
                max = result;
                bestMove = move;
            }
        }
    
        /**
         * Getter for the best move found.
         * @return The best move.
         */
        public Move getBestMove()
        {
            return bestMove;
        }
    
        /**
         * Stops the threads.
         */
        public void kill()
        {
            stopped = true;
        }
    
        /**
         * {@inheritDoc}
         * In this case starts all threads and then waits for each to finish.
         */
        @Override
        protected void operation()
        {
            for(var thread : threads)
                thread.start();
            
            for(var thread : threads)
            {
                try
                {
                    if(stopped)
                        return;
                    
                    thread.join();
                }
                catch(InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    
        /**
         * A single thread to perform minmax operations.
         */
        private class AIMoveThread extends Thread
        {
            private final Set<Move> toProcess = new HashSet<>();
            private final AIGame workingGame;
    
            /**
             * Constructor for AIMoveThread.
             * @param game The game to copy and perform the minmax operation on.
             */
            public AIMoveThread(Game game)
            {
                workingGame = new AIGame(game);
            }
    
            /**
             * Adds a move to the set of moves to process.
             * @param move The move to add.
             */
            public void addMove(Move move)
            {
                toProcess.add(move);
            }
    
            /**
             * Performs each operation from the set of moves to process.
             */
            public void run()
            {
                for(var move : toProcess)
                {
                    if(stopped)
                        return;
                    
                    checkMax(doStage(move, max, depth, false), move);
                }
            }
    
            /**
             * Minmax operation.
             * If won just return static value.
             * Makes sure to roll back move before returning.
             * @param move The move to check.
             * @param previous The best result of the previous stage so far.
             * @param depth The remaining layers to process.
             * @param max Boolean; true if max stage, false if min.
             * @return The value of the operation.
             */
            private int doStage(Move move, Integer previous, int depth, boolean max)
            {
                move.doMoveOn(workingGame);
                if(depth <= 0 || workingGame.isWon())
                {
                    var out = workingGame.value(isPlayer2());
                    workingGame.rollback();
                    return out;
                }
        
                Integer current = null;
                for(var entry : workingGame.possibleMoves().entrySet())
                    for(var possibleMove : entry.getValue())
                    {
                        if(stopped)
                            return 0;
    
                        var result = doStage(new BoardMove(entry.getKey(), possibleMove), current, depth - 1, !max);
                
                        if(current == null)
                            current = result;
                        else if(max)
                            current = Math.max(current, result);
                        else
                            current = Math.min(current, result);
                
                        if(prune(current, previous, max))
                            return current;
                    }
        
                for(var entry : workingGame.possiblePlacements().entrySet())
                    for(var possiblePlacement : entry.getValue())
                    {
                        if(stopped)
                            return 0;
                        
                        var result = doStage(new PlaceMove(entry.getKey(), possiblePlacement), current, depth - 1, !max);
                
                        if(current == null)
                            current = result;
                        else if(max)
                            current = Math.max(current, result);
                        else
                            current = Math.min(current, result);
                
                        if(prune(current, previous, max))
                            return current;
                    }
        
                workingGame.rollback();
                assert current != null; //I don't think stalemate is a possibility???
                return current;
            }
    
            /**
             * Returns true if the current branch can be pruned.
             * If value is over 5000000 for max or under -5000000 for min also prune it, as game is won.
             * Rolls back if can be pruned.
             * @param current The best result of this stage.
             * @param previous The best result of the previous stage.
             * @param max Boolean; true if max stage, false if min.
             * @return Boolean; true if can be pruned.
             */
            private boolean prune(Integer current, Integer previous, boolean max)
            {
                if(previous == null || (max ? current < previous : previous < current) && (max ? current < 5000000 : current > -5000000))
                    return false;
        
                workingGame.rollback();
                return true;
            }
        }
    }
}
