package it.unibo.t2sgame.core.api;

import java.util.Optional;

/**
 * This interface abstracts the concept of a "Game engine". 
 * A game engine is the core of a game software architecture, which allows us
 * to synchronize differents domain of the game, such as Input's handling with the 
 * Physic's handling.
 */
public interface GameEngine {
    /**
     * This method rapresent the game loop of the
     * current GameEngine implementation.
     */
    void run();
    /**
     * Set the a new Game to be reproduced 
     * @param g the new game
     * 
     * @return this 
     */
    GameEngine setGame(Game g);
    /**
     * 
     * @return the current game played
     */
    Optional<Game> getGame();
}
