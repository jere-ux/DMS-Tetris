package com.comp2042.view;

import com.comp2042.logic.events.ClearRow;

/**
 * Represents the data sent down to the view, combining row clearing information with general view data.
 * This class is immutable.
 */
public final class DownData {
    /**
     * Information about rows that have been cleared.
     */
    private final ClearRow clearRow;
    /**
     * The current visual state of the game.
     */
    private final ViewData viewData;

    /**
     * Constructs a new DownData object.
     *
     * @param clearRow The data about cleared rows.
     * @param viewData The general view data.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the row clearing information.
     *
     * @return The {@link ClearRow} object.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the general view data.
     *
     * @return The {@link ViewData} object.
     */
    public ViewData getViewData() {
        return viewData;
    }
}
