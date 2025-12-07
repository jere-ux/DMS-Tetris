package com.comp2042.logic.events;

/**
 * Represents a user input event for moving or rotating a tetromino.
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent.
     * @param eventType The type of move (e.g., DOWN, LEFT, RIGHT).
     * @param eventSource The source of the event (e.g., a specific player).
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of move event.
     * @return The event type.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of the event.
     * @return The event source.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
