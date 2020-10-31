package ui.menu;

import ui.TerminalHandler;
import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredStringBuilder;
import ui.drawing.composition.BasicSectionComposer;
import ui.drawing.composition.BasicSectionGrid;
import ui.drawing.layout.BorderSection;
import ui.drawing.layout.Filler;
import ui.input.EscapeHandler;
import ui.input.KeyHandler;
import ui.input.selection.DrawableSelectableSection;
import ui.input.selection.SelectableGrid;
import ui.input.selection.SelectableSection;
import ui.input.selection.UnselectableSection;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.button.Button;
import ui.menu.button.MenuButton;

import java.util.ArrayList;

/**
 * Represents a bordered box with a section in the top and a row of buttons at the bottom.<br>
 * {@inheritDoc}
 */
public class Dialog extends DrawableSelectableSection
{
    private final SelectableGrid<SelectableSection, BasicSectionComposer<SelectableSection>, SelectableSection> grid;
    private final BorderSection<SelectableGrid<SelectableSection, BasicSectionComposer<SelectableSection>, SelectableSection>> borderSection;
    
    /**
     * Constructor for Dialog.
     * Spread the buttons out at the bottom if there is room.
     * @param section The section for the main area of the dialog.
     * @param buttons The buttons to show at the bottom of the dialog.
     * @param terminalHandler The TerminalHandler.
     * @param escapeHandler The escape handler.
     * @param enabled Boolean; true if the dialog is enabled for selection.
     * @param filler The filler character to use for the space around the buttons.
     * @param borderColour The foreground colour of the border.
     * @param borderBackgroundColour The background colour of the border.
     */
    public Dialog(Section section, Button[] buttons, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled, ColouredChar filler, Colour borderColour, BackgroundColour borderBackgroundColour)
    {
        super(enabled);
        var buttonsWidth = 1;
        for(var button : buttons)
            buttonsWidth += button.getColumns() + 1;
        var extra = Math.max(section.getColumns() - buttonsWidth, 0) / (buttons.length + 1);
        var menuButtons = new ArrayList<SelectableSection>();
        menuButtons.add(new UnselectableSection<>(new Filler(3, extra + 1, filler)));
        for(var button : buttons)
        {
            menuButtons.add(new MenuButton<>(3, button, filler));
            menuButtons.add(new UnselectableSection<>(new Filler(3, extra + 1, filler)));
        }
        
        grid = new SelectableGrid<>(new BasicSectionGrid<>(new SelectableSection[][]{
                new SelectableSection[]{section instanceof SelectableSection ? (SelectableSection)section : new UnselectableSection<>(section)},
                new SelectableSection[]{new SelectableCentredSection<>(3, Math.max(buttonsWidth, section.getColumns()), new SelectableGrid<>(
                new BasicSectionGrid<>(new SelectableSection[][]{menuButtons.toArray(new SelectableSection[0])}), terminalHandler, escapeHandler, true), filler)}}),
                terminalHandler, escapeHandler, true);
        grid.setParent(this);
        this.borderSection = new BorderSection<>(grid, BorderSection.Border.BLOCK, borderColour, borderBackgroundColour);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying bordered section.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        borderSection.getRow(builder, row, startCol, maxCols);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying bordered section.
     */
    @Override
    public int getRows()
    {
        return borderSection.getRows();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying bordered section.
     */
    @Override
    public int getColumns()
    {
        return borderSection.getColumns();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying bordered section.
     */
    @Override
    public void update()
    {
        borderSection.update();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying bordered section.
     */
    @Override
    protected void unfocus()
    {
    
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying selectable grid.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        return grid.select(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying selectable grid.
     */
    @Override
    public KeyHandler focus(KeyHandler current)
    {
        return grid.focus(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass to the underlying selectable grid.
     */
    public KeyHandler getHandler()
    {
        return grid.getHandler();
    }
    
    /**
     * Getter for the underlying section grid.
     * @return The underlying section grid.
     */
    public SelectableGrid<SelectableSection, BasicSectionComposer<SelectableSection>, SelectableSection> getGrid()
    {
        return grid;
    }
}