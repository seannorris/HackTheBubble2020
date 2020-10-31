package ui.input.selection;

import ui.TerminalHandler;
import ui.drawing.BufferedSection;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredStringBuilder;
import ui.input.EscapeHandler;
import ui.input.KeyHandler;

/**
 * Represents a drawable selectable buffered section.<br>
 * {@inheritDoc}
 * @param <T> The type of underlying buffered section to use.
 */
public abstract class DrawableSelectableBufferedSection<T extends BufferedSection> extends DrawableSelectableSection implements SelectableBufferedSection
{
    private final T section;
    
    /**
     * Constructor for DrawableSelectableBufferedSection with no underlying key handler.
     * @param section The underlying buffered section.
     * @param enabled Boolean; true if section is enabled for selection.
     */
    public DrawableSelectableBufferedSection(T section, boolean enabled)
    {
        super(enabled);
        this.section = section;
    }
    
    /**
     * Constructor for DrawableSelectableBufferedSection with an underlying key handler.
     * @param section The underlying buffered section.
     * @param enabled Boolean; true if section is enabled for selection.
     * @param handler The underlying key handler.
     */
    public DrawableSelectableBufferedSection(T section, boolean enabled, KeyHandler handler)
    {
        super(enabled, handler);
        this.section = section;
    }
    
    /**
     * Constructor for DrawableSelectableBufferedSection with an underlying movement selection handler.
     * @param section The underlying buffered section.
     * @param enabled Boolean; true if section is enabled for selection.
     * @param terminalHandler The TerminalHandler object.
     * @param escapeHandler The escape handler to use when escape is pressed.
     * @param horizontal Boolean; true if the primary direction is horizontal.
     */
    public DrawableSelectableBufferedSection(T section, boolean enabled, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean horizontal)
    {
        super(enabled, terminalHandler, escapeHandler, horizontal);
        this.section = section;
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying buffered section.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        section.getRow(builder, row, startCol, maxCols);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying buffered section.
     */
    @Override
    public int getRows()
    {
        return section.getRows();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying buffered section.
     */
    @Override
    public int getColumns()
    {
        return section.getColumns();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying buffered section.
     */
    @Override
    public void update()
    {
        section.update();
        super.update();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying buffered section.
     */
    @Override
    public ColouredChar[][] getBuffer()
    {
        return section.getBuffer();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying buffered section.
     */
    @Override
    public void copyBuffer(BufferedSection input, int row, int col)
    {
        section.copyBuffer(input, row, col);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying buffered section.
     */
    @Override
    public ColouredChar getChar(int row, int col)
    {
        return section.getChar(row, col);
    }
}
