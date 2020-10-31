package ui.menu;

import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredString;
import ui.drawing.colour.ColouredStringBuilder;
import ui.drawing.composition.BasicSectionGrid;
import ui.drawing.layout.CentredSection;
import ui.input.KeyHandler;
import ui.input.selection.DrawableSelectableSection;

/**
 * Represents a multiple choice option chooser.<br>
 * {@inheritDoc}
 */
public class OptionChooser extends DrawableSelectableSection
{
    private final ColouredString[] options;
    private int selected;
    private final BasicSectionGrid<Section, Section> grid;
    private final ColouredChar leftArrow;
    private final ColouredChar rightArrow;
    private final CentredSection<ColouredString> selection;
    private final BackgroundColour unfocusedColour;
    private final BackgroundColour focusedColour;
    private boolean focused;
    
    private static final char LEFT_ARROW = '<';
    private static final char RIGHT_ARROW = '>';
    
    /**
     * Constructor for OptionChooser.
     * Sets width to maximum option length.
     * @param options The available options.
     * @param textColour The text colour.
     * @param arrowColour The colour of the arrows that indicate the ability to pick options.
     * @param unfocusedColour The background colour when unfocused.
     * @param focusedColour The background colour when focused.
     * @param enabled Boolean; true if enabled for selection.
     */
    public OptionChooser(String[] options, Colour textColour, Colour arrowColour, BackgroundColour unfocusedColour, BackgroundColour focusedColour, boolean enabled)
    {
        super(enabled);
        this.unfocusedColour = unfocusedColour;
        this.focusedColour = focusedColour;
        
        this.options = new ColouredString[options.length];
        var maxLength = 0;
        for(var x = 0 ; x < options.length; x++)
        {
            if(options[x].length() > maxLength)
                maxLength = options[x].length();
            
            this.options[x] = new ColouredString(options[x], textColour, unfocusedColour);
        }
        selection = new CentredSection<>(1, maxLength, this.options[selected], new ColouredChar(Section.FILLER_CHAR, textColour, unfocusedColour));
        this.leftArrow = new ColouredChar(Section.FILLER_CHAR, arrowColour, unfocusedColour);
        this.rightArrow = new ColouredChar(Section.FILLER_CHAR, arrowColour, unfocusedColour);
        this.grid = new BasicSectionGrid<>(new Section[][]{new Section[]{leftArrow, selection, rightArrow}});
    }
    
    /**
     * {@inheritDoc}
     * In this case set the background colour to the focused colour.
     */
    public KeyHandler focus(KeyHandler current)
    {
        focused = true;
        updateColour();
        return super.focus(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case set the background colour to the unfocused colour.
     */
    @Override
    protected void unfocus()
    {
        focused = false;
        updateColour();
    }
    
    /**
     * Sets the background colour to either the focused colour or unfocused colour.
     * Shows the arrows if focused.
     */
    private void updateColour()
    {
        var colour = focused ? focusedColour : unfocusedColour;
        leftArrow.setBackgroundColour(colour);
        leftArrow.setChar(focused && options.length > 1 ? LEFT_ARROW : FILLER_CHAR);
        rightArrow.setBackgroundColour(colour);
        rightArrow.setChar(focused && options.length > 1 ? RIGHT_ARROW : FILLER_CHAR);
        selection.setBackgroundColour(colour);
        options[selected].setBackgroundColour(colour);
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
     * Increment or decrement the selection by 1 and bound it to the available options.
     * @param decrement Boolean; true to decrement instead of increment.
     */
    private void incrementSelection(boolean decrement)
    {
        selected += (decrement ? -1 : 1);
        while(selected >= options.length)
            selected -= options.length;
        while(selected < 0)
            selected += options.length;
        selection.setSection(options[selected]);
        updateColour();
    }
    
    /**
     * {@inheritDoc}
     * In this case increment the selection.
     */
    @Override
    public KeyHandler secondaryNext(KeyHandler current)
    {
        incrementSelection(false);
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case decrement the selection.
     */
    @Override
    public KeyHandler secondaryPrevious(KeyHandler current)
    {
        incrementSelection(true);
        return current;
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
    
    /**
     * {@inheritDoc}
     * In this case just keeps focus.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        return focus(current);
    }
    
    /**
     * Getter for the currently selected index.
     * @return The selected index.
     */
    public int getSelected()
    {
        return selected;
    }
    
    /**
     * Setter for the currently selected index.
     * @param selected The new index.
     */
    public void setSelected(int selected)
    {
        this.selected = selected;
        selection.setSection(options[selected]);
        updateColour();
    }
    
    /**
     * Getter for the string representation of the current selection.
     * @return The string representation.
     */
    public String getSelection()
    {
        return options[selected].getString();
    }
}
