# Tetris Game 

## GitHub Repository
**Repository Link:https://github.com/jere-ux/DMS-Tetris

---

## Compilation Instructions

### Prerequisites
- **Java Development Kit (JDK):** Version 11 or higher
- **JavaFX SDK:** Version 11 or higher
- **Maven:** Version 3.6 or higher 
- **IDE:** IntelliJ IDEA,  or any Java-compatible IDE .

### Step-by-Step Compilation

1.**Verify Project Structure**
   Ensure your project has the following structure:
   ```
   src/
   ├── main/
   │   ├── java/com/comp2042/
   │   └── resources/
   └── test/java/com/comp2042/
   ```

2.**Configure JavaFX**
    - Download JavaFX SDK from [openjfx.io](https://openjfx.io)
    - Add JavaFX libraries to your project classpath
    - Set VM options: `--module-path [path-to-javafx-lib] --add-modules javafx.controls,javafx.fxml,javafx.media`

3.**Build with Maven**
   ```bash
   mvn clean compile
   ```

4.**Run the Application**
   ```bash
   mvn javafx:run
   ```

   Or run the Main class directly:
   ```bash
   java --module-path [path-to-javafx-lib] --add-modules javafx.controls,javafx.fxml,javafx.media -cp target/classes com.comp2042.app.Main
   ```

### Dependencies Required
- JavaFX Controls
- JavaFX FXML
- JavaFX Media
- JUnit 5 (for testing)

---

## Implemented and Working Properly

### Core Game Mechanics
1. **Multiple Game Modes**
    - **Type A (Speed Curve):** Progressive speed increase as lines are cleared, creating escalating difficulty
    - **Type B (Normal):** Classic Tetris gameplay with consistent speed
    - **Type C (Obstacles):** Starts with pyramid obstacle formation, includes bomb power-up system

2. **Advanced Tetris Features**
    - **Ghost Piece Preview:** Semi-transparent preview showing where the current piece will land
    - **Hard Drop:** Instant piece placement using spacebar with bonus scoring (2 points per row)
    - **Hold Functionality:** Ability to hold current piece for later use (C key), following modern Tetris standards
    - **Preview System:** Shows next three upcoming pieces for strategic planning
    - **7-Bag Randomization:** Fair piece distribution system ensuring no prolonged droughts

3. **Scoring System**
    - Base scoring for line clears with exponential bonuses (50 × lines²)
    - Hard drop bonus points (2 points per row dropped)
    - Soft drop scoring (1 point per manual down movement)
    - Persistent high score tracking per game mode using file-based storage
    - Individual leaderboards for each game mode

4. **User Interface Enhancements**
    - **Animated Main Menu:** Cyberpunk-themed with falling Tetris pieces, neon glow effects
    - **Pause Menu:** In-game pause functionality (P key) with resume and new game options
    - **Game Over Screen:** Name entry for leaderboard submission, options for replay or main menu
    - **Score Notifications:** Animated floating text for score bonuses
    - **Speed Notifications:** Visual feedback when game speed increases (Type A mode)
    - **Power-Up Progress Bar:** Visual indicator for bomb power-up progress (Type C mode)

5. **Visual Effects**
    - Smooth animations for piece movement and rotation
    - Fire particle effects for bomb explosions
    - Fade and scale animations for block removal
    - Neon glow effects on UI elements
    - Cyberpunk aesthetic with custom styling

6. **Audio**
    - Background menu music with looping playback
    - Automatic music management (stops on game start/quit)

7. **Bomb Power-Up System (Type C)**
    - Progress bar charges after clearing 5 lines
    - Bomb piece destroys 3×3 area on placement
    - Gravity simulation after bomb detonation
    - Visual fire explosion effects
    - Strategic gameplay element requiring careful placement

8. **Leaderboard System**
    - Persistent storage using serialization
    - Separate leaderboards for each game mode
    - Top 5 scores displayed per mode
    - Automatic sorting by score

9. **Game State Management**
    - Proper pause/resume functionality
    - Game over detection with collision at spawn point
    - New game initialization with mode-specific setup
    - Clean state transitions between menu and game

---

## Implemented but Not Working Properly

### Minor Issues

1. **Menu Music Loading**
    - **Issue:** Console warning if `menu.mp3` file is missing from resources
    - **Impact:** Low - game functions normally, just no background music
    - **Attempted Fix:** Added try-catch error handling and console message
    - **Reason Not Fully Fixed:** Requires proper audio file in resources; works when file present

2. **Leaderboard Name Validation**
    - **Issue:** No input validation for empty names or special characters
    - **Impact:** Medium - could allow blank entries in leaderboard
    - **Attempted Fix:** Basic null/empty check implemented
    - **Reason Not Fully Fixed:** Would benefit from regex validation and character limits

3. **Window Resizing**
    - **Issue:** Game window is set to non-resizable, but manual override could break layout
    - **Impact:** Low - explicitly disabled in code
    - **Note:** Fixed dimensions ensure consistent visual experience

---

## Features Not Implemented

1.**Online Multiplayer**
    - **Reason:** Requires networking infrastructure, server setup, synchronization logic, and significant architectural changes beyond single-player scope.

2.**Custom Themes/Skins**
    - **Reason:** Time constraints. Would require asset management system, theme configuration files, and dynamic style loading.



3**Sound Effects**
    - **Reason:** Focused on visual polish first; audio effects would require multiple sound files and audio mixing logic.



4. **Level Progression with Different Backgrounds**
    - **Reason:** Static background sufficient for current implementation; dynamic backgrounds require asset loading system.

---

## New Java Classes

### Controller Package (`com.comp2042.controller`)

1. **`MenuController.java`**
    - **Purpose:** Handles all main menu interactions, animations, and navigation
    - **Key Features:** Background particle animation, level selection, menu music control
    - **Location:** `src/main/java/com/comp2042/controller/MenuController.java`

2. **`GameController.java`** (Modified from original)
    - **Purpose:** Central game logic coordinator with enhanced features
    - **Key Features:** AnimationTimer-based game loop, speed progression, bomb handling
    - **Location:** `src/main/java/com/comp2042/controller/GameController.java`

3. **`InputEventListener.java`** (Enhanced interface)
    - **Purpose:** Defines event handling contract for game inputs
    - **New Methods:** `onHardDropEvent()`, `onHoldEvent()`
    - **Location:** `src/main/java/com/comp2042/controller/InputEventListener.java`

### Model Package (`com.comp2042.model`)

1.**`GameLevel.java`**
    - **Purpose:** Defines game mode enumeration
    - **Enum Values:** `TYPE_A_SPEED_CURVE`, `TYPE_B_NORMAL`, `TYPE_C_OBSTACLES`
    - **Location:** `src/main/java/com/comp2042/model/GameLevel.java`

2.**`LeaderboardManager.java`**
    - **Purpose:** Manages leaderboard data persistence and retrieval
    - **Key Features:** Serialization-based storage, mode-specific leaderboards
    - **Location:** `src/main/java/com/comp2042/model/LeaderboardManager.java`

3.**`ScoreEntry.java`**
    - **Purpose:** Represents individual leaderboard entry
    - **Key Features:** Implements `Serializable` and `Comparable` for sorting
    - **Location:** `src/main/java/com/comp2042/model/ScoreEntry.java`

7. **`NextShapeInfo.java`**
    - **Purpose:** Encapsulates next shape data with rotation state
    - **Location:** `src/main/java/com/comp2042/model/NextShapeInfo.java`

8. **`DownData.java`** (Model version)
    - **Purpose:** Data transfer object for downward movement results
    - **Location:** `src/main/java/com/comp2042/model/DownData.java`

### Logic Package (`com.comp2042.logic`)

9. **`HighScoreManager.java`**
    - **Purpose:** Manages high score persistence using Properties files
    - **Key Features:** Mode-specific high score tracking
    - **Location:** `src/main/java/com/comp2042/logic/HighScoreManager.java`

10. **`SimpleBoard.java`** (Heavily modified - see Modified Classes)

### Logic.Bricks Package (`com.comp2042.logic.bricks`)

11. **`Bomb.java`**
- **Purpose:** Special bomb piece for Type C obstacles mode
- **Key Features:** Single-cell piece, implements `isBomb()` marker method
- **Location:** `src/main/java/com/comp2042/logic/bricks/Bomb.java`

### Logic.Events Package (`com.comp2042.logic.events`)

12. **`EventSource.java`** (Relocated)
- **Purpose:** Distinguishes user input from automated events
- **Location:** `src/main/java/com/comp2042/logic/events/EventSource.java`

13. **`EventType.java`** (Enhanced)
- **Purpose:** Defines all possible game input events
- **New Types:** `HARD_DROP`, `HOLD`
- **Location:** `src/main/java/com/comp2042/logic/events/EventType.java`

14. **`MoveEvent.java`** (Relocated)
- **Purpose:** Event object carrying type and source information
- **Location:** `src/main/java/com/comp2042/logic/events/MoveEvent.java`

15. **`ClearRow.java`** (Relocated)
- **Purpose:** Encapsulates row clearing results
- **Location:** `src/main/java/com/comp2042/logic/events/ClearRow.java`

### Logic.Helper Package (`com.comp2042.logic.helper`)

16. **`BrickRotator.java`** (Relocated with enhancements)
- **Purpose:** Handles brick rotation logic
- **New Feature:** `getBrick()` method for hold functionality
- **Location:** `src/main/java/com/comp2042/logic/helper/BrickRotator.java`

17. **`MatrixOperations.java`** (Relocated)
- **Purpose:** Utility class for matrix operations
- **Location:** `src/main/java/com/comp2042/logic/helper/MatrixOperations.java`

### View Package (`com.comp2042.view`)

18. **`PauseMenuPanel.java`**
- **Purpose:** Custom JavaFX component for pause menu
- **Key Features:** Resume and new game buttons with event handlers
- **Location:** `src/main/java/com/comp2042/view/PauseMenuPanel.java`

19. **`GameOverPanel.java`** (Enhanced)
- **Purpose:** Enhanced game over screen with name entry
- **New Features:** TextField for name, submit score button, main menu button
- **Location:** `src/main/java/com/comp2042/view/GameOverPanel.java`

20. **`ViewData.java`** (Enhanced)
- **Purpose:** Data transfer object for view state
- **New Features:** Three-piece preview, ghost position, hold piece data
- **Location:** `src/main/java/com/comp2042/view/ViewData.java`

21. **`GuiController.java`** (Heavily modified - see Modified Classes)

22. **`NotificationPanel.java`** (Enhanced)
- **Purpose:** Animated notification display
- **Location:** `src/main/java/com/comp2042/view/NotificationPanel.java`

---

## Modified Java Classes

### Major Modifications

1. **`SimpleBoard.java`**
    - **Original Location:** `src/main/java/com/comp2042/SimpleBoard.java`
    - **New Location:** `src/main/java/com/comp2042/logic/SimpleBoard.java`
    - **Changes Made:**
        - Added `holdBrick()` method implementing hold functionality with `canHold` flag
        - Added `getGhostYPosition()` for ghost piece calculation
        - Added `createPyramidObstacle()` generating 4-row pyramid at board bottom
        - Added `isCurrentBrickBomb()` checking if current piece is bomb
        - Added `detonateBomb()` implementing 3×3 explosion logic
        - Added `handleGravity()` for column-wise gravity after explosions
        - Added `getBrickPosition()` exposing current brick coordinates
        - Added `getBrickGenerator()` accessor for power-up progress tracking
        - Fixed board dimensions (now height×width instead of width×height)
        - Enhanced `createNewBrick()` with centered spawn position calculation
    - **Reason:** Core gameplay enhancements required new mechanics

2. **`GuiController.java`**
    - **Original Location:** `src/main/java/com/comp2042/GuiController.java`
    - **New Location:** `src/main/java/com/comp2042/view/GuiController.java`
    - **Changes Made:**
        - Added ghost brick panel rendering with 30% opacity
        - Added three next-piece preview panels with optimized refresh logic
        - Added hold brick panel display
        - Added pause menu integration with P key toggle
        - Added hard drop handling (spacebar) with animation
        - Added hold functionality (C key)
        - Added `togglePause()` method with timeline control
        - Added `returnToMainMenu()` for navigation
        - Added `showSpeedNotification()` with fade animation
        - Added `showScoreNotification()` with enhanced positioning
        - Added `spawnFireEffect()` for bomb explosions with 30 particles
        - Added `animateBlockRemoval()` with parallel fade and scale transitions
        - Added `updatePowerUpProgressBar()` for Type C mode
        - Added `setPowerUpContainerVisibility()` for mode-specific UI
        - Added `getGamePanelOffset()` helper for accurate positioning
        - Enhanced `refreshBrick()` with ghost piece and hold piece updates
        - Added score and high score label binding
        - Added leaderboard manager integration
        - Added game over name submission handling
    - **Reason:** Extensive UI enhancements and new feature integration

3. **`GameController.java`**
    - **Original:** Simple event delegation
    - **Changes Made:**
        - Replaced Timeline with AnimationTimer for precise frame control
        - Added `dropInterval` variable for dynamic speed adjustment
        - Added `linesClearedSinceSpeedUp` counter for Type A progression
        - Added `startGameLoop()` implementing 60 FPS game loop with delta time
        - Enhanced `onDownEvent()` with bomb detection and detonation
        - Added `handleBombAftermath()` for post-explosion processing
        - Added `onHardDropEvent()` with distance calculation and scoring
        - Added `onHoldEvent()` delegating to board
        - Added mode-specific initialization (pyramid for Type C)
        - Added speed progression logic (25% faster every 2 lines in Type A)
        - Added power-up progress tracking for Type C
        - Added proper game over with high score update
    - **Reason:** Complex game loop and mode-specific logic required overhaul

4. **`Score.java`**
    - **Original Location:** `src/main/java/com/comp2042/Score.java`
    - **New Location:** `src/main/java/com/comp2042/logic/Score.java`
    - **Changes Made:**
        - Added `highScore` IntegerProperty for tracking
        - Added `HighScoreManager` integration
        - Added `levelType` field for mode-specific scores
        - Added `highScoreProperty()` accessor
        - Added `updateHighScore()` method with persistence
        - Constructor now loads saved high score for current mode
    - **Reason:** Persistent high score tracking across sessions

5. **`RandomBrickGenerator.java`**
    - **Changes Made:**
        - Implemented 7-bag system replacing pure random generation
        - Added `currentBag` list shuffled for fair distribution
        - Added `fillBag()` creating new shuffled bag of all 7 pieces
        - Added `ensureQueueHasEnoughBricks()` for lookahead management
        - Added `getNextBricks(count)` for three-piece preview
        - Added `powerUpProgress` tracking for Type C mode
        - Added `incrementPowerUpProgress()` for line clear tracking
        - Added `resetPowerUpProgress()` for new game initialization
        - Added bomb piece generation when progress reaches 5
    - **Reason:** Fair piece distribution and power-up system

6. **`Board.java`** (Interface)
    - **Changes Made:**
        - Added `getGhostYPosition()` method signature
        - Added `holdBrick()` method signature
        - Added `createPyramidObstacle()` method signature
        - Added `handleGravity()` method signature
    - **Reason:** Interface must match implementation enhancements

7. **`Brick.java`** (Interface)
    - **Changes Made:**
        - Added `isBomb()` default method returning false
    - **Reason:** Marker method for bomb piece identification

8. **`MatrixOperations.java`**
    - **Changes Made:**
        - Fixed `checkOutOfBound()` to include `targetY >= 0` check
        - Added private constructor with `AssertionError` to prevent instantiation
        - Enhanced comments for utility class pattern
    - **Reason:** Bug fix for ghost piece calculation, enforce utility class pattern

9. **`ViewData.java`**
    - **Original:** Basic brick and next brick data
    - **Changes Made:**
        - Added `nextThreeBricks` List for three-piece preview
        - Added `ghostYPosition` field for ghost piece
        - Added `holdBrickData` field for hold piece
        - Added multiple constructors for backward compatibility
        - Added `getNextThreeBricks()` with deep copy
        - Added `getGhostYPosition()` accessor
        - Added `getHoldBrickData()` accessor with null check
    - **Reason:** Support for advanced Tetris features

10. **`Main.java`**
    - **Original Location:** `src/main/java/com/comp2042/Main.java`
    - **New Location:** `src/main/java/com/comp2042/app/Main.java`
    - **Changes Made:**
        - Changed initial FXML to load menu (`Menu.fxml`) instead of game
        - Updated window dimensions to 600×600
        - Changed title from "TetrisJFX" to "TETRIS"
        - Removed direct GameController instantiation (now handled by MenuController)
        - Added null check for primaryStage
- **Reason:** Game flow now starts at menu instead of directly in game

### Minor Modifications

11. **All Brick Classes (IBrick, JBrick, LBrick, OBrick, SBrick, TBrick, ZBrick)**
    - **Changes Made:**
        - Updated imports to use `com.comp2042.logic.helper.MatrixOperations` or `com.comp2042.logic.MatrixOperations`
    - **Reason:** Package restructuring for better organization

---

## Unexpected Problems


### Problem 1: Ghost Piece Collision Bug
- **Description:** Ghost piece calculation was crashing when pieces spawned at top of board due to negative Y-coordinate checks in `MatrixOperations.intersect()`.
- **Root Cause:** `checkOutOfBound()` method missing `targetY >= 0` validation.
- **Solution:** Added boundary check for negative Y coordinates.
- **Impact:** Prevented ArrayIndexOutOfBoundsException during ghost piece rendering.


### Problem 2: StackPane Center Alignment for Notifications
- **Description:** Score notifications were appearing in wrong positions and not properly centering.
- **Original Approach:** Used absolute positioning with `layoutX`/`layoutY`.
- **Solution:** Changed notification parent to StackPane with CENTER alignment. Notifications now use `TranslateTransition` for relative movement from centered position.
- **Lesson Learned:** StackPane's built-in alignment is more reliable than manual positioning calculations.

### Problem 3: Hold Piece Exploit
- **Description:** Players could spam hold key to effectively pause game and plan moves indefinitely.
- **Solution:** Implemented `canHold` flag that sets to false on hold and resets to true only when piece locks.
- **Lesson Learned:** Game mechanics require careful state management to prevent exploits.

### Problem 4: Bomb Gravity Creating Gaps
- **Description:** After bomb detonation, blocks above would fall but create visual gaps due to immediate matrix update.
- **Solution:** Implemented column-wise gravity algorithm that shifts blocks down one cell at a time, followed by separate line clear check.
- **Lesson Learned:** Complex board state changes need careful sequencing: explosion → gravity → line clear → spawn.



### Problem 5: FXML Resource Loading Issues
- **Description:** Occasional NullPointerException when loading CSS and FXML files.
- **Solution:** Used `getClass().getResource()` with absolute paths (`/menu.fxml`) and added null checks with error messages.
- **Lesson Learned:** Resource loading in JavaFX requires careful path management and error handling.


---

## Testing

### Unit Tests Implemented
- **`ScoreTest.java`:** Tests for score addition, high score update, and reset functionality
- **`SBrickTest.java`:** Tests for shape matrix retrieval and deep copy verification

### Manual Testing Performed
- All three game modes tested for full gameplay sessions
- Pause/resume functionality verified
- Leaderboard persistence tested across application restarts
- Ghost piece accuracy validated
- Hard drop and hold mechanics tested extensively
- Bomb explosion and gravity tested with various scenarios
- UI responsiveness tested with rapid key inputs

---

## Known Limitations
1.**Resolution:** Fixed window size (600×600) - not responsive to different screen sizes
2.**Accessibility:** No colorblind mode or alternative color schemes
3.**Input:** Keyboard only - no gamepad support
4.**Audio:** Only menu music - no sound effects for game actions

---


