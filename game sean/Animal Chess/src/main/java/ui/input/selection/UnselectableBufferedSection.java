package ui.input.selection;

import ui.drawing.BufferedSection;
import ui.drawing.colour.ColouredChar;

/**
 * An unselectable buffered section.<br>
 * {@inheritDoc}
 * @param <T> The type of underlying buffered section to store.
 */
public class UnselectableBufferedSection<T extends BufferedSection> extends UnselectableSection<T> implements SelectableBufferedSection
{
    /**
     * Constructor for UnselectableBufferedSection
     * @param section The underlying section.
     */
    public UnselectableBufferedSection(T section)
    {
        super(section);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public ColouredChar[][] getBuffer()
    {
        return getSection().getBuffer();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public void copyBuffer(BufferedSection input, int row, int col)
    {
        getSection().copyBuffer(input, row, col);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public ColouredChar getChar(int row, int col)
    {
        return getSection().getChar(row, col);
    }
}
