package ui.menu.button;

import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredString;
import ui.drawing.colour.ColouredStringBuilder;
import ui.drawing.composition.BasicSectionGrid;
import ui.input.KeyHandler;
import ui.input.selection.DrawableSelectableSection;
import ui.input.selection.SelectableSection;

/**
 * Represents a selectable button.<br>
 * {@inheritDoc}
 */
public abstract class Button extends DrawableSelectableSection implements SelectableSection
{
    private static final char LEFT_BRACKET = '[';
    private static final char RIGHT_BRACKET = ']';
    
    private final BasicSectionGrid<Section, Section> grid;
    private final ColouredString text;
    private final ColouredChar leftBracket;
    private final ColouredChar rightBracket;
    private final BackgroundColour focusedColour;
    private final BackgroundColour unfocusedColour;
    
    /**
     * Constructor for Button.
     * @param text The button text.
     * @param textColour The button text colour.
     * @param bracketColour The colour of the brackets that indicate this is a button.
     * @param unfocusedColour The background colour when unfocused.
     * @param focusedColour The background colour when focused.
     * @param enabled Boolean; true if button is enabled for selection.
     */
    public Button(String text, Colour textColour, Colour bracketColour, BackgroundColour unfocusedColour, BackgroundColour focusedColour, boolean enabled)
    {
        super(enabled);
        this.text = new ColouredString(text, textColour, unfocusedColour);
        this.leftBracket = new ColouredChar(LEFT_BRACKET, bracketColour, unfocusedColour);
        this.rightBracket = new ColouredChar(RIGHT_BRACKET, bracketColour, unfocusedColour);
        grid = new BasicSectionGrid<>(new Section[][]{new Section[]{leftBracket, this.text, rightBracket}});
        this.focusedColour = focusedColour;
        this.unfocusedColour = unfocusedColour;
    }
    
    /**
     * {@inheritDoc}
     * In this case set the background colour to the focused colour.
     */
    @Override
    public KeyHandler focus(KeyHandler current)
    {
        leftBracket.setBackgroundColour(focusedColour);
        text.setBackgroundColour(focusedColour);
        rightBracket.setBackgroundColour(focusedColour);
        return super.focus(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case set the background colour to the unfocused colour.
     */
    @Override
    protected void unfocus()
    {
        leftBracket.setBackgroundColour(unfocusedColour);
        text.setBackgroundColour(unfocusedColour);
        rightBracket.setBackgroundColour(unfocusedColour);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying section grid.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        grid.getRow(builder, row, startCol, maxCols);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying section grid.
     */
    @Override
    public int getRows()
    {
        return grid.getRows();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying section grid.
     */
    @Override
    public int getColumns()
    {
        return grid.getColumns();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying section grid.
     */
    @Override
    public void update()
    {
        grid.update();
    }
}
