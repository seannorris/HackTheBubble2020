package ui.menu;

import ui.drawing.Section;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredString;
import ui.drawing.composition.BasicSectionComposer;
import ui.drawing.composition.Coordinate;
import ui.drawing.layout.CentredSection;
import ui.drawing.layout.Filler;
import ui.input.KeyHandler;
import ui.input.selection.SelectableSection;
import ui.input.selection.SelectionHandler;

/**
 * Represents a combination of a selectable section and a label to fit in a menu.<br>
 * {@inheritDoc}
 * @param <T> The type of selectable section to label.
 */
public class LabeledMenuItem<T extends SelectableSection> extends BasicSectionComposer<CentredSection<Section>> implements MenuItem
{
    private final T section;
    private final Filler filler;
    private final CentredSection<Section> fillerCentredSection;
    private final ColouredString label;
    
    public static final int EXTRA_SPACE = 5;
    
    /**
     * Constructor for LabeledMenuItem.
     * @param height The height to take up (centred vertically).
     * @param section The section to label.
     * @param label The label.
     * @param dots The character to fill the space between the label and the section.
     * @param filler The character to fill the surrounding space.
     */
    public LabeledMenuItem(int height, T section, ColouredString label, ColouredChar dots, ColouredChar filler)
    {
        super(label.getColumns() + section.getColumns(), height);
        this.section = section;
        this.label = label;
        this.filler = new Filler(1, 0, dots);
        addSection(new CentredSection<>(height, label.getColumns(), label, filler), 0, 0);
        fillerCentredSection = new CentredSection<>(height, this.filler.getColumns(), this.filler, filler);
        addSection(new CentredSection<>(height, section.getColumns(), section, filler), label.getColumns() + this.filler.getColumns(), 0);
    }
    
    /**
     * {@inheritDoc}
     * In this case work out the new position in the underlying section composer for each element and reposition them.
     */
    @Override
    public void setSize(int rows, int columns)
    {
        super.setSize(rows, columns);
        var oldX = label.getColumns() + filler.getColumns();
        filler.setSize(1, Math.max(columns - section.getColumns() - label.getColumns(), 0));
        var newX = label.getColumns() + filler.getColumns();
        getSections().put(Coordinate.at(newX, 0), getSections().remove(Coordinate.at(oldX, 0)));
        if(oldX == label.getColumns() && newX > oldX)
            addSection(fillerCentredSection, oldX, 0);
        for(var section : getSections().values())
            section.setSize(rows, section.getSection().getColumns());
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public boolean enabled()
    {
        return section.enabled();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public void setParent(SelectionHandler parent)
    {
        section.setParent(parent);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public void setHandler(KeyHandler handler)
    {
        section.setHandler(handler);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public KeyHandler getHandler()
    {
        return section.getHandler();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public KeyHandler primaryNext(KeyHandler current)
    {
        return section.primaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public KeyHandler primaryPrevious(KeyHandler current)
    {
        return section.primaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public KeyHandler secondaryNext(KeyHandler current)
    {
        return section.secondaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public KeyHandler secondaryPrevious(KeyHandler current)
    {
        return section.secondaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        return section.select(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the labeled section.
     */
    @Override
    public KeyHandler focus(KeyHandler current)
    {
        return section.focus(current);
    }
    
    
    /**
     * {@inheritDoc}
     * In this case get the width of the label and section, plus a bit of extra spacing for aesthetic purposes.
     */
    public int getContentWidth()
    {
        return section.getColumns() + label.getColumns() + EXTRA_SPACE;
    }
    
    /**
     * Getter for the labeled section.
     * @return The labeled section.
     */
    public T getSection()
    {
        return section;
    }
}
