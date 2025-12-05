package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SBrickTest {

    private SBrick sBrick;

    @BeforeEach
    void setUp() {
        // This method runs before each test.
        // It's a good place to create a fresh instance of the class you're testing.
        sBrick = new SBrick();
    }

    @Test
    @DisplayName("SBrick should have 2 shape variations")
    void getShapeMatrix_ShouldReturnTwoShapes() {
        // Arrange: The sBrick is already created in setUp()

        // Act: Call the method we want to test
        List<int[][]> shapes = sBrick.getShapeMatrix();

        // Assert: Check if the result is what we expect
        assertNotNull(shapes, "The shape matrix should not be null.");
        assertEquals(2, shapes.size(), "SBrick should have exactly 2 shape variations.");
    }

    @Test
    @DisplayName("getShapeMatrix should return a deep copy")
    void getShapeMatrix_ShouldReturnDeepCopy() {
        // Arrange
        List<int[][]> firstCallShapes = sBrick.getShapeMatrix();
        
        // Act: Modify the list we received
        firstCallShapes.get(0)[0][0] = 99; // Change a value in the first shape

        // Assert: The new call should return an unmodified, original list
        List<int[][]> secondCallShapes = sBrick.getShapeMatrix();
        assertNotEquals(99, secondCallShapes.get(0)[0][0], "Modifying the returned list should not affect the brick's internal state.");
    }
}