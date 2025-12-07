package com.comp2042.controller;

import com.comp2042.logic.events.MoveEvent;
import com.comp2042.view.DownData;
import com.comp2042.view.ViewData;

/**
 * An interface for listening to game input events.
 */
public interface InputEventListener {

    /**
     * Called when a "down" event occurs.
     * @param event The move event.
     * @return The data for updating the view.
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Called when a "left" event occurs.
     * @param event The move event.
     * @return The data for updating the view.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Called when a "right" event occurs.
     * @param event The move event.
     * @return The data for updating the view.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Called when a "rotate" event occurs.
     * @param event The move event.
     * @return The data for updating the view.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Called when a "hard drop" event occurs.
     * @param event The move event.
     * @return The data for updating the view.
     */
    DownData onHardDropEvent(MoveEvent event);

    /**
     * Called when a "hold" event occurs.
     * @param event The move event.
     * @return The data for updating the view.
     */
    ViewData onHoldEvent(MoveEvent event);

    /**
     * Called when a new game is created.
     */
    void createNewGame();
}
