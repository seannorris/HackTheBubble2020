package ui.menu;

import ui.input.selection.layout.SelectableResponsiveSection;

/**
 * Represents a section that can be used in the menu class.<br>
 * {@inheritDoc}
 */
public interface MenuItem extends SelectableResponsiveSection
{
    /**
     * Gets the minimum possible width of the section.
     * @return The minimum width.
     */
    int getContentWidth();
}
