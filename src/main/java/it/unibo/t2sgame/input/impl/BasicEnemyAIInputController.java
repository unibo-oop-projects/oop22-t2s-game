package it.unibo.t2sgame.input.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.unibo.t2sgame.core.components.impl.PhysicsComponent;
import it.unibo.t2sgame.core.entity.api.Entity;
import it.unibo.t2sgame.input.api.Command;
import it.unibo.t2sgame.input.api.Directions;
import it.unibo.t2sgame.input.api.InputController;

public class BasicEnemyAIInputController implements InputController {
    private static final int RIGHT_MAX_ABSOLUTE_ANGLE = 45;
    private static final int LEFT_MIN_ABSOLUTE_ANGLE = 135;
    private final List<Command> availableCommands;
    private final Queue<Command> commandsQueue;

    public BasicEnemyAIInputController() {
        this.commandsQueue = new ConcurrentLinkedQueue<>();
        this.availableCommands = new LinkedList<>();
        for (var direction : Directions.values()) {
            this.availableCommands.add(new Move(direction));
        }
    }

    @Override
    public Queue<Command> getCommandsQueue() {
        computeNextCommand();
        var defensiveQueue = new LinkedList<>(this.commandsQueue);
        this.commandsQueue.clear();
        return defensiveQueue;
    }

    private void computeNextCommand() {
        this.commandsQueue.add(entity -> {
            if (entity.getWorld().isEmpty()) {
                throw new IllegalArgumentException();
            }
            final var world = entity.getWorld().get();
            var closestPlayer = world.getPlayers().stream()
                    .min((p1, p2) -> Double.compare(distanceBetweenEntities(entity, p1),
                            distanceBetweenEntities(entity, p2)));
            closestPlayer.ifPresent(player -> {
                final var dX = player.getPosition().getX() - entity.getPosition().getX();
                final var dY = player.getPosition().getY() - entity.getPosition().getY();
                final var angle = Math.toDegrees(Math.atan2(dY, dX)); // get the angle between the player and the enemy in degrees
                entity.notifyComponent(PhysicsComponent.class, () -> findDirectionGivenAngle(angle));
            });
        });
    }

    private double distanceBetweenEntities(final Entity e1, final Entity e2) {
        return e1.getPosition().distance(e2.getPosition());
    }

    private Directions findDirectionGivenAngle(final Double angle) {
        final var absAngle = Math.abs(angle);
        if (absAngle <= RIGHT_MAX_ABSOLUTE_ANGLE) { 
            // the angle is between -45 and 45 degrees
            return Directions.RIGHT;
        } else if (absAngle >= LEFT_MIN_ABSOLUTE_ANGLE) {
            // the angle is between -135 and 135 degrees
            return Directions.LEFT;
        } else if (angle < 0) {
            // the angle is between -45 and -135 degrees
            return Directions.UP;
        } else {
            // the angle is between 45 and 135 degrees
            return Directions.DOWN;
        }
    }

}
