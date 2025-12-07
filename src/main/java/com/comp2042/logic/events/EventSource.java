package com.comp2042.logic.events;

/**
 * Specifies the source of a game event.
 */
public enum EventSource {
    /**
     * The event was triggered by user input.
     */
    USER,
    /**
     * The event was triggered by an automated thread (e.g., gravity).
     */
    THREAD
}
