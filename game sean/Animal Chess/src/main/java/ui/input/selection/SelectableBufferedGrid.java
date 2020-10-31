package ui.input.selection;

import ui.TerminalHandler;
import ui.drawing.BufferedSection;
import ui.drawing.colour.ColouredChar;
import ui.drawing.composition.BufferedSectionComposer;
import ui.drawing.composition.SectionGrid;
import ui.input.EscapeHandler;

/**
 * A buffered selectable grid.<br>
 * {@inheritDoc}
 * @param <T> The type of selectable buffered section stored in the underlying section grid.
 * @param <U> The type of underlying buffered section composer used by the underlying section grid.
 * @param <V> The type of buffered section stored in the underlying section composer.  (Unfortunately due to java limitations this has to extend selectable buffered section).
 */
public class SelectableBufferedGrid<T extends V, U extends BufferedSectionComposer<V>, V extends SelectableBufferedSection> extends SelectableGrid<T, U, V> implements SelectableBufferedSection
{
    /**
     * Constructor for SelectableBufferedGrid.
     * @param grid The underlying SectionGrid to use.
     * @param terminalHandler The TerminalHandler object.
     * @param escapeHandler The escape handler to use when escape is pressed.
     * @param enabled Boolean; true if section is enabled for selection.
     */
    public SelectableBufferedGrid(SectionGrid<T, U, V> grid, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled)
    {
        super(grid, terminalHandler, escapeHandler, enabled);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying buffered section grid.
     */
    @Override
    public ColouredChar[][] getBuffer()
    {
        return getGrid().getComposer().getBuffer();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying buffered section grid.
     */
    @Override
    public void copyBuffer(BufferedSection input, int row, int col)
    {
        getGrid().getComposer().copyBuffer(input, row, col);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying buffered section grid.
     */
    @Override
    public ColouredChar getChar(int row, int col)
    {
        return getGrid().getComposer().getChar(row, col);
    }
}
