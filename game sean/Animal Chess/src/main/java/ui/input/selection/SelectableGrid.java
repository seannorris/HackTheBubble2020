package ui.input.selection;

import ui.TerminalHandler;
import ui.drawing.colour.ColouredStringBuilder;
import ui.drawing.composition.SectionComposer;
import ui.drawing.composition.SectionGrid;
import ui.input.EscapeHandler;
import ui.input.KeyHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A selectable grid of sections built upon a SectionGrid.
 * Sections can be moved between using the movement keys.<br>
 * {@inheritDoc}
 * @param <T> The type of selectable section stored in the underlying section grid.
 * @param <U> The type of underlying section composer used by the underlying section grid.
 * @param <V> The type of section stored in the underlying section composer.  (Unfortunately due to java limitations this has to extend selectable section).
 */
public class SelectableGrid<T extends V, U extends SectionComposer<V>, V extends SelectableSection> extends DrawableSelectableSection implements SelectableSection
{
    private List<TreeMap<Integer, T>> sections;
    private int selectedCol = -1;
    private int selectedRow = -1;
    private boolean lastPrevious;
    private final SectionGrid<T, U, V> grid;
    private final TerminalHandler terminalHandler;
    private EscapeHandler escapeHandler;
    
    /**
     * Constructor for SelectableGrid.
     * @param grid The underlying SectionGrid to use.
     * @param terminalHandler The TerminalHandler object.
     * @param escapeHandler The escape handler to use when escape is pressed.
     * @param enabled Boolean; true if section is enabled for selection.
     */
    public SelectableGrid(SectionGrid<T, U, V> grid, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled)
    {
        super(enabled, terminalHandler, escapeHandler, false);
        this.grid = grid;
        this.terminalHandler = terminalHandler;
        this.escapeHandler = escapeHandler;
        recheckSections();
    }
    
    /**
     * Checks if two key handlers are equal and if so refocuses the current selection.
     * This would be different if the parent to the selectable grid took control of input.
     * @param current The current KeyHandler.
     * @param returned The KeyHandler returned by the parent selection handler.
     * @return Either the parent's KeyHandler or the refocused KeyHandler.
     */
    private KeyHandler refocus(KeyHandler current, KeyHandler returned)
    {
        return returned.equals(current) ? focus(current) : returned;
    }
    
    /**
     * Gets the closest enabled section in the specified row.
     * Repeatedly finds higher and lower element from specified point until one is enabled or null is returned.
     * @param selectedRow The row to check.
     * @return The closest section or null if none available.
     */
    private Map.Entry<Integer, T> getClosest(int selectedRow)
    {
        var row = sections.get(selectedRow);
        var next = row.ceilingEntry(selectedCol);
        if(next != null && next.getKey() == selectedCol && next.getValue().enabled())
            return next;
    
        var higher = getHigher(selectedRow, selectedCol);
        var lower = getLower(selectedRow, selectedCol);
        if(higher == null)
            return lower;
        if(lower == null)
            return higher;
        
        var check = lastPrevious ? higher.getKey() - selectedCol >  selectedCol - lower.getKey()
                                 : higher.getKey() - selectedCol >= selectedCol - lower.getKey();
        lastPrevious = !check;
        return check ? higher : lower;
    }
    
    /**
     * Gets the next item in the selected row that is enabled or null if none are available.
     * @param selectedRow The row to check.
     * @param selectedCol The column to check from.
     * @return The first available section or null if none available.
     */
    private Map.Entry<Integer, T> getHigher(int selectedRow, int selectedCol)
    {
        var row = sections.get(selectedRow);
        var higher = row.higherEntry(selectedCol);
        while(higher != null && !higher.getValue().enabled())
            higher = row.higherEntry(higher.getKey());
        return higher;
    }
    
    /**
     * Gets the previous item in the selected row that is enabled or null if none are available.
     * @param selectedRow The row to check.
     * @param selectedCol The column to check from.
     * @return The first available section or null if none available.
     */
    private Map.Entry<Integer, T> getLower(int selectedRow, int selectedCol)
    {
        var row = sections.get(selectedRow);
        var lower = row.lowerEntry(selectedCol);
        while(lower != null && !lower.getValue().enabled())
            lower = row.lowerEntry(lower.getKey());
        return lower;
    }
    
    /**
     * {@inheritDoc}
     * In this case check each row above the currently selected one and if an available piece is found select it.
     * If none are found pass the next call up to the parent selection handler.
     */
    @Override
    public KeyHandler primaryNext(KeyHandler current)
    {
        if(selectedRow < 0 || selectedCol < 0)
            checkEnabled();
        if(selectedRow >= sections.size() - 1 || selectedRow < 0 || selectedCol < 0)
            return refocus(current, super.primaryNext(current));
        
        for(var row = selectedRow + 1; row < sections.size(); row++)
        {
            var out = getClosest(row);
            if(out != null)
            {
                selectedRow = row;
                selectedCol = out.getKey();
                return out.getValue().focus(current);
            }
        }
        return refocus(current, super.primaryNext(current));
    }
    
    /**
     * {@inheritDoc}
     * In this case check each row below the currently selected one and if an available piece is found select it.
     * If none are found pass the previous call up to the parent selection handler.
     */
    @Override
    public KeyHandler primaryPrevious(KeyHandler current)
    {
        if(selectedRow < 0 || selectedCol < 0)
            checkEnabled();
        if(selectedRow <= 0 || selectedCol < 0)
            return refocus(current, super.primaryPrevious(current));
        
        for(var row = selectedRow - 1; row >= 0; row--)
        {
            var out = getClosest(row);
            if(out != null)
            {
                selectedRow = row;
                selectedCol = out.getKey();
                return out.getValue().focus(current);
            }
        }
        return refocus(current, super.primaryPrevious(current));
    }
    
    /**
     * {@inheritDoc}
     * In this case check each row to the right of the currently selected one and if an available piece is found select it.
     * If none are found pass the next call up to the parent selection handler.
     */
    @Override
    public KeyHandler secondaryNext(KeyHandler current)
    {
        lastPrevious = false;
        if(selectedRow < 0)
            checkEnabled();
        if(selectedCol < 0 || selectedRow < 0 || selectedCol >= sections.get(selectedRow).size() - 1)
            return refocus(current, super.secondaryNext(current));
        
        var next = getHigher(selectedRow, selectedCol);
        if(next == null)
            return refocus(current, super.secondaryNext(current));
        
        selectedCol = next.getKey();
        return next.getValue().focus(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case check each row to the left of the currently selected one and if an available piece is found select it.
     * If none are found pass the previous call up to the parent selection handler.
     */
    @Override
    public KeyHandler secondaryPrevious(KeyHandler current)
    {
        lastPrevious = true;
        if(selectedRow < 0)
            checkEnabled();
        if(selectedCol < 0 || selectedRow < 0)
            return super.secondaryPrevious(current);
        
        var next = getLower(selectedRow, selectedCol);
        if(next == null)
            return refocus(current, super.secondaryPrevious(current));
    
        selectedCol = next.getKey();
        return next.getValue().focus(current);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void unfocus()
    {
    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case try to get the currently selected section.
     * If it's not available then recheck all the sections and find the closest that is.
     */
    @Override
    public KeyHandler focus(KeyHandler current)
    {
        if(selectedCol < 0 || selectedRow < 0)
            recheckSections();
    
        if(selectedCol < 0 || selectedRow < 0)
            return current;
        
        var section = this.sections.get(selectedRow).get(selectedCol);
        if(section == null || !section.enabled())
        {
            recheckSections();
            section = this.sections.get(selectedRow).get(selectedCol);
        }
        
        return section == null || !section.enabled() ? current : section.focus(super.focus(current));
    }
    
    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        grid.getRow(builder, row, startCol, maxCols);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section grid.
     */
    @Override
    public int getRows()
    {
        return grid.getRows();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section grid.
     */
    @Override
    public int getColumns()
    {
        return grid.getColumns();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section grid.
     */
    @Override
    public void update()
    {
        grid.update();
        super.update();
    }
    
    /**
     * Checks each section in the grid to make sure at least one is enabled.
     * Calls the checkEnabled method of any contained SelectableGrids.
     * If the currently selected section is no longer enabled find the closest one and enable that instead.
     */
    public void checkEnabled()
    {
        if(sections.size() <= 0)
            return;
        
        for(var row : grid.getSections())
            for(var section : row)
                if(section instanceof SelectableGrid)
                    ((SelectableGrid)section).checkEnabled();
                
        for(var row = 0; (selectedRow == -1 || selectedCol == -1) && row < sections.size(); row++)
        {
            var sections = this.sections.get(row);
            for(var col = 0; (selectedRow == -1 || selectedCol == -1) && col < sections.size(); col++)
                if(sections.get(col) != null && sections.get(col).enabled())
                {
                    selectedCol = col;
                    selectedRow = row;
                }
        }
        
        if(selectedRow != -1 && selectedCol != -1 && !this.sections.get(selectedRow).get(selectedCol).enabled())
        {
            var nearest = getClosest(selectedRow);
            for(var diff = 1; nearest == null && (selectedRow + diff < sections.size() || selectedRow - diff >= 0); diff++)
            {
                if(selectedRow + diff < sections.size())
                {
                    nearest = getClosest(selectedRow + diff);
                    if(nearest != null)
                        selectedRow += diff;
                }
                if(nearest == null && selectedRow - diff >= 0)
                {
                    nearest = getClosest(selectedRow - diff);
                    if(nearest != null)
                        selectedRow -= diff;
                }
            }
            if(nearest != null)
                selectedCol = nearest.getKey();
        }
        
        setEnabled(selectedRow != -1 && selectedCol != -1 && this.sections.get(selectedRow).get(selectedCol).enabled());
    }
    
    /**
     * Recreates the underlying data structure used to find selections.
     * This is a list of maps of column to section.
     * The selected row refers to the index in the list.
     */
    public void recheckSections()
    {
        var sections = grid.getSections();
        var previousRows = this.sections != null ? this.sections.size() : -1;
        this.sections = new ArrayList<>();
        for(var row = 0; row < sections.length; row++)
        {
            var hasSection = false;
            for(var col = 0; col < sections[row].length; col++)
            {
                if(sections[row][col] != null)
                {
                    if(!hasSection)
                    {
                        this.sections.add(new TreeMap<>());
                        hasSection = true;
                    }
                    this.sections.get(this.sections.size() - 1).put(col, sections[row][col]);
                    sections[row][col].setParent(this);
                    sections[row][col].setHandler(new MovementSelectionHandler<>(terminalHandler, escapeHandler, sections[row][col], false));
                    if((selectedRow == -1 || selectedCol == -1) && sections[row][col].enabled())
                    {
                        selectedCol = col;
                        selectedRow = this.sections.size() - 1;
                    }
                }
            }
        }
        if(this.sections.size() == 0 || selectedRow < 0 || selectedCol < 0)
        {
            selectedRow = -1;
            selectedCol = -1;
        }
        else
            selectedRow = previousRows > 0 ? (int)Math.round((selectedRow + 1) * ((double)this.sections.size() / previousRows)) - 1 : 0;
        
        checkEnabled();
    }
    
    /**
     * Returns the underlying section grid as an array of sections.
     * @return The array of sections.
     */
    protected T[][] getSections()
    {
        return grid.getSections();
    }
    
    /**
     * Returns the underlying section grid.
     * @return The section grid.
     */
    protected SectionGrid<T, U, V> getGrid()
    {
        return grid;
    }
    
    /**
     * Setter for the escape handler.
     * @param escapeHandler The new EscapeHandler.
     */
    public void setEscapeHandler(EscapeHandler escapeHandler)
    {
        this.escapeHandler = escapeHandler;
        super.setHandler(terminalHandler, escapeHandler, false);
        recheckSections();
    }
}
