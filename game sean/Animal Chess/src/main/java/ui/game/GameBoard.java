package ui.game;

import logic.Game.Coord;
import ui.TerminalHandler;
import ui.drawing.DrawableBufferedSection;
import ui.drawing.colour.ColouredChar;
import ui.drawing.composition.BufferedSectionComposer;
import ui.drawing.composition.BufferedSectionGrid;
import ui.drawing.composition.SectionGrid;
import ui.input.EscapeHandler;
import ui.input.KeyHandler;
import ui.input.selection.DrawableSelectableBufferedSection;
import ui.input.selection.SelectableBufferedSection;
import ui.input.selection.SelectableGrid;

import java.util.Map;
import java.util.Set;

/**
 * Represents the game board.<br>
 * {@inheritDoc}
 */
public class GameBoard extends SelectableGrid<SelectableBufferedSection, BufferedSectionComposer<SelectableBufferedSection>, SelectableBufferedSection>
{
    public static final int HORIZONTAL_GAP = 2;
    public static final int VERTICAL_GAP = 1;
    public static final int ROWS = logic.Game.HEIGHT;
    public static final int COLUMNS = logic.Game.WIDTH;
    public static final int WIDTH = COLUMNS * (HORIZONTAL_GAP + Piece.WIDTH) + HORIZONTAL_GAP;
    public static final int HEIGHT = ROWS * (VERTICAL_GAP + Piece.HEIGHT) + VERTICAL_GAP;
    public static final ColouredChar BORDER = new ColouredChar('█');
    
    private boolean flipped;
    private final Game game;
    
    /**
     * Constructor for GameBoard.
     * @param game The game the board belongs to.
     * @param terminalHandler The TerminalHandler.
     * @param escapeHandler The escape handler.
     * @param enabled Boolean; true if the game board is enabled for selection.
     */
    public GameBoard(Game game, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled)
    {
        super(getGrid(game), terminalHandler, escapeHandler, enabled);
        this.game = game;
    }
    
    /**
     * Gets the underlying section grid for the board.
     * Add the promotion indicators to the top of the board then add the pieces.
     * @param game The game the board will belong to.
     * @return The section grid.
     */
    private static SectionGrid<SelectableBufferedSection, BufferedSectionComposer<SelectableBufferedSection>, SelectableBufferedSection> getGrid(Game game)
    {
        var composer = new BufferedSectionComposer<SelectableBufferedSection>(WIDTH, HEIGHT, BORDER);
        
        var sections = new SelectableBufferedSection[ROWS + 1][COLUMNS];
        for(var col = 0; col < COLUMNS; col++)
        {
            var indicator = new PromotionIndicator(false, game);
            sections[0][col] = indicator;
            composer.addSection(indicator, col * Piece.WIDTH + (col + 1) * HORIZONTAL_GAP + (Piece.WIDTH - PromotionIndicator.WIDTH) / 2, 0);
        }
        
        for(var row = 1; row < sections.length; row++)
            for(var col = 0; col < COLUMNS; col++)
            {
                var piece = new Piece(false, null, game);
                sections[row][col] = piece;
                composer.addSection(piece, col * Piece.WIDTH + (col + 1) * HORIZONTAL_GAP,
                        (row - 1) * Piece.HEIGHT + row * VERTICAL_GAP);
            }
        return new BufferedSectionGrid<>(composer, sections);
    }
    
    /**
     * Updates the board to reflect the game board.
     * Enables pieces that should be selectable.
     * @param boardState The game board.
     * @param enabled A set of enabled coordinates.
     * @param flip True if player 2 is at the bottom of the board.
     */
    public void updateBoard(Map<Coord, logic.piece.Piece> boardState, Set<Coord> enabled, boolean flip)
    {
        this.flipped = flip;
        
        for(var col = 0; col < getSections()[0].length; col++)
        {
            var coord = getFlippedCoord(0, col);
            ((PromotionIndicator)getSections()[0][col]).setCoord(enabled != null
                    && enabled.contains(coord) ? coord : null);
        }
            
        for(var row = 1; row < getSections().length; row++)
            for(var col = 0; col < getSections()[row].length; col++)
            {
                var piece = (Piece)getSections()[row][col];
                var coord = getFlippedCoord(row, col);
                piece.setPiece(boardState.getOrDefault(coord, null), flip);
                piece.setEnabled(enabled != null && enabled.contains(coord));
            }
    }
    
    /**
     * Gets the coordinate, flipped if flipped is set to true.
     * @param row The y coordinate.
     * @param col The x coordinate.
     * @return A Coord object with the appropriate coordinate.
     */
    private Coord getFlippedCoord(int row, int col)
    {
        return Coord.at(flipped ? COLUMNS - col - 1 : col,
                       !flipped ? ROWS    - row     : row - 1);
    }
    
    /**
     * Takes a piece object and finds it's coordinate on the board.
     * @param piece The Piece object.
     * @return The coordinate on the board.
     */
    public Coord getCoord(Piece piece)
    {
        for(var row = 1; row < getSections().length; row++)
            for(var col = 0; col < getSections()[row].length; col++)
                if(getSections()[row][col].equals(piece))
                    return getFlippedCoord(row, col);
        
        return null;
    }
    
    /**
     * Gets a set of all the coordinates that a specified piece can be placed.
     * @param set The set of possible coordinates.
     * @param piece The piece to check.
     */
    public void getPlaceable(Set<Coord> set, logic.piece.Piece piece)
    {
        for(var row = 1; row < getSections().length; row++)
            for(var col = 0; col < getSections()[row].length; col++)
            {
                var coord = getFlippedCoord(row, col);
                if(game.getGame().canPlace(piece, coord))
                    set.add(coord);
            }
    }
    
    /**
     * Represents a piece's ability to promote from the last row in the game.<br>
     * {@inheritDoc}
     */
    private static class PromotionIndicator extends DrawableSelectableBufferedSection<DrawableBufferedSection>
    {
        private Coord coord;
        private boolean focused;
        private final Game game;
        
        public static final int WIDTH = 3;
        public static final char SYMBOL = '^';
        public static final ColouredChar ENABLED_CHAR_PLAYER1 = new ColouredChar(SYMBOL, Piece.PLAYER1_COLOUR, Piece.ENABLED_COLOUR);
        public static final ColouredChar ENABLED_CHAR_PLAYER2 = new ColouredChar(SYMBOL, Piece.PLAYER2_COLOUR, Piece.ENABLED_COLOUR);
        public static final ColouredChar FOCUSED_CHAR_PLAYER1 = new ColouredChar(SYMBOL, Piece.PLAYER1_COLOUR, Piece.PLAYER1_FOCUSED_COLOUR);
        public static final ColouredChar FOCUSED_CHAR_PLAYER2 = new ColouredChar(SYMBOL, Piece.PLAYER2_COLOUR, Piece.PLAYER2_FOCUSED_COLOUR);
    
        /**
         * Constructor for PromotionIndicator.
         * @param enabled If the indicator should be enabled.
         * @param game The game the promotion indicator belongs to.
         */
        public PromotionIndicator(boolean enabled, Game game)
        {
            super(new DrawableBufferedSection(WIDTH, VERTICAL_GAP, BORDER), enabled);
            this.game = game;
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        protected void unfocus()
        {
            focused = false;
        }
    
        /**
         * {@inheritDoc}
         */
        public KeyHandler focus(KeyHandler current)
        {
            focused = true;
            return super.focus(current);
        }
    
        /**
         * {@inheritDoc}
         * In this case performs the promotion.
         */
        @Override
        public KeyHandler select(KeyHandler current)
        {
            game.moveTo(coord);
            return current;
        }
    
        /**
         * Getter for the promotion coordinate.
         * @return The coordinate.
         */
        public Coord getCoord()
        {
            return coord;
        }
    
        /**
         * Setter for the promotion coordinate.
         * @param coord The new Coordinate.
         */
        public void setCoord(Coord coord)
        {
            setEnabled(coord != null);
            this.coord = coord;
        }
    
        /**
         * {@inheritDoc}
         * In this case get the appropriate symbol and fill the indicator with it.
         */
        @Override
        public void update()
        {
            var symbol = BORDER;
            if(enabled())
                symbol = focused ? ((game.getGame().isPlayer2Turn() ? FOCUSED_CHAR_PLAYER2 : FOCUSED_CHAR_PLAYER1))
                        : ((game.getGame().isPlayer2Turn() ? ENABLED_CHAR_PLAYER2 : ENABLED_CHAR_PLAYER1));
            
            for(var row = 0; row < VERTICAL_GAP; row++)
                for(var col = 0; col < WIDTH; col++)
                    getBuffer()[row][col] = symbol;
                
            super.update();
        }
    }
}
