package ui.drawing.layout;

import ui.drawing.colour.ColouredStringBuilder;

/**
 * Shows a different section if smaller than a certain size.<br>
 * {@inheritDoc}
 * @param <T> A common type between the two sections.
 * @param <U> The type of the normal section.
 * @param <V> The type of the section shown when smaller than the minimum size.
 */
public class MinimumSizeSection<T extends ResponsiveSection, U extends T, V extends T> extends DrawableResponsiveSection
{
    private final U section;
    private final V tooSmallSection;
    private final int minimumRows;
    private final int minimumColumns;
    
    /**
     * Constructor for MinimumSizeSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to display if the minimum size is satisfied.
     * @param tooSmallSection The section to display if the minimum size is not satisfied.
     * @param minimumRows The minimum number of rows in characters.
     * @param minimumColumns The minimum number of columns in characters.
     */
    public MinimumSizeSection(int rows, int columns, U section, V tooSmallSection, int minimumRows, int minimumColumns)
    {
        super(rows, columns);
        this.section = section;
        this.tooSmallSection = tooSmallSection;
        this.minimumRows = minimumRows;
        this.minimumColumns = minimumColumns;
        currentSection().setSize(rows, columns);
    }
    
    /**
     * {@inheritDoc}
     * In this case get the row from the currently selected section.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        currentSection().getRow(builder, row, startCol, maxCols);
    }
    
    /**
     * {@inheritDoc}
     * In this case set the size of the selected section too.
     */
    public void setSize(int rows, int columns)
    {
        super.setSize(rows, columns);
        currentSection().setSize(rows, columns);
    }
    
    /**
     * Get the normal section if the size is greater than or equal to minimum size in both dimensions.
     * Otherwise return the too small section.
     * @return The selected section.
     */
    protected T currentSection()
    {
        return getRows() < minimumRows || getColumns() < minimumColumns ? tooSmallSection : section;
    }
    
    /**
     * {@inheritDoc}
     * In this case update the selected section.
     */
    @Override
    public void update()
    {
        currentSection().update();
        super.update();
    }
}
