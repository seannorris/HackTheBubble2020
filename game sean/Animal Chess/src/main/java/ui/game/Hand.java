package ui.game;

import ui.TerminalHandler;
import ui.drawing.colour.ColouredChar;
import ui.drawing.composition.BufferedSectionComposer;
import ui.drawing.composition.BufferedSectionGrid;
import ui.drawing.composition.SectionGrid;
import ui.input.EscapeHandler;
import ui.input.selection.SelectableBufferedGrid;
import ui.input.selection.SelectableBufferedSection;
import ui.input.selection.SelectableGrid;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a player's hand.<br>
 * {@inheritDoc}
 */
public class Hand extends SelectableGrid<Piece, BufferedSectionComposer<SelectableBufferedSection>, SelectableBufferedSection>
{
    public static final int HORIZONTAL_GAP = 2;
    public static final int VERTICAL_GAP = 1;
    public static final int MAX_ROWS = 4;
    public static final int MAX_COLUMNS = 2;
    public static final int WIDTH = MAX_COLUMNS * (HORIZONTAL_GAP + Piece.WIDTH) + HORIZONTAL_GAP;
    public static final int HEIGHT = MAX_ROWS * (VERTICAL_GAP + Piece.HEIGHT) + VERTICAL_GAP;
    public static final ColouredChar BORDER = new ColouredChar('█');
    
    private final TerminalHandler terminalHandler;
    private final EscapeHandler escapeHandler;
    private final boolean flipped;
    
    /**
     * Constructor for Hand.
     * @param game The game the hand belongs to.
     * @param flipped If the hand is the opposite player to usual (e.g player 2 has right hand).
     * @param terminalHandler The TerminalHandler.
     * @param escapeHandler The escape handler.
     * @param enabled If the hand is enabled for selection.
     */
    public Hand(Game game, boolean flipped, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled)
    {
        super(getGrid(game), terminalHandler, escapeHandler, enabled);
        this.terminalHandler = terminalHandler;
        this.escapeHandler = escapeHandler;
        this.flipped = flipped;
    }
    
    /**
     * Sets up the underlying section grid.
     * @param game The game the hand will belong to.
     * @return The section grid.
     */
    private static SectionGrid<Piece, BufferedSectionComposer<SelectableBufferedSection>, SelectableBufferedSection> getGrid(Game game)
    {
        var composer = new BufferedSectionComposer<SelectableBufferedSection>(WIDTH, HEIGHT);
        
        var sections = new Piece[MAX_ROWS][MAX_COLUMNS];
        for(var row = 0; row < MAX_ROWS; row++)
            for(var col = 0; col < MAX_COLUMNS; col++)
                sections[row][col] = new Piece(false, null, game);
    
        return new BufferedSectionGrid<>(composer, sections);
    }
    
    /**
     * Re-renders the hand with the correct number of pieces.
     * The pieces are drawn in order.
     * @param pieces The pieces to draw.
     * @param enabled If all pieces should be enabled for selection/
     * @param selectable One piece that should be selectable.
     * @param pieceFlipped If the piece should have its move directions inverted.
     */
    public void updateHand(List<logic.piece.Piece> pieces, boolean enabled, Piece selectable, boolean pieceFlipped)
    {
        var composer = getGrid().getComposer();
        composer.getSections().clear();
        for(var row : composer.getBuffer())
            Arrays.fill(row, FILLER);
        
        for(var row = 0; row < MAX_ROWS; row++)
            for(var col = 0; col < MAX_COLUMNS; col++)
                getSections()[row][col].setPiece(null, false);
                
        
        var remaining = pieces.size() % MAX_COLUMNS;
        for(var i = 0; i < pieces.size(); i++)
        {
            var piece = pieces.get(i);
            
            var row = (i) / MAX_COLUMNS;
            var flippedRow = this.flipped ? MAX_ROWS - row - 1: row;
            var y = (VERTICAL_GAP + Piece.HEIGHT) * row;
            var flippedY = this.flipped ? composer.getRows() - y - BorderedPiece.HEIGHT : y;
    
            var col = i % MAX_COLUMNS;
            var x = (HORIZONTAL_GAP + Piece.WIDTH) * col;
            
            if(remaining > 0 && i > pieces.size() - MAX_COLUMNS)
            {
                col += (MAX_COLUMNS - remaining + 1) / 2;
                x += ((HORIZONTAL_GAP + Piece.WIDTH) * remaining + 1) / 2;
            }
            
            var flippedCol = this.flipped ? MAX_COLUMNS - col - 1 : col;
            var flippedX = this.flipped ? composer.getColumns() - x - BorderedPiece.WIDTH : x;
            
            var section = getSections()[flippedRow][flippedCol];
            section.setPiece(piece, pieceFlipped);
            section.setEnabled(enabled || section.equals(selectable));
            composer.addSection(new BorderedPiece(section), flippedX, flippedY);
        }
        
        recheckSections();
    }
    
    /**
     * Represents a piece in the hand.<br>
     * {@inheritDoc}
     */
    private class BorderedPiece extends SelectableBufferedGrid<Piece, BufferedSectionComposer<Piece>, Piece>
    {
        public static final int WIDTH = Piece.WIDTH + 2 * HORIZONTAL_GAP;
        public static final int HEIGHT = Piece.HEIGHT + 2 * VERTICAL_GAP;
    
        /**
         * Constructor for BorderedPiece.
         * @param piece The piece to border.
         */
        public BorderedPiece(Piece piece)
        {
            super( new BufferedSectionGrid<>(new BufferedSectionComposer<>(WIDTH, HEIGHT, BORDER),
                    new Piece[][]{new Piece[]{piece}}), terminalHandler, escapeHandler, false);
            getGrid().getComposer().addSection(piece, HORIZONTAL_GAP, VERTICAL_GAP);
            recheckSections();
        }
    
    }
}
