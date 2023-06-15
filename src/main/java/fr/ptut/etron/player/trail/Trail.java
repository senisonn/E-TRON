package fr.ptut.etron.player.trail;

import fr.ptut.etron.utils.Direction;
import fr.ptut.etron.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Trail {

    public static final int TRAIL_MAX_PARTS = 25;
    public static final int DEFAULT_MAX_LENGTH = 300;

    private List<TrailPart> parts;
    private int trailMaxLength;
    private int length;
    // private boolean shouldBeEmpty;

    public Trail(Vector2 start, Direction direction) {
        this.parts = new ArrayList<>();
        this.trailMaxLength = DEFAULT_MAX_LENGTH;
        this.length = 0;
        /* this.shouldBeEmpty = shouldBeEmpty;

        if (!shouldBeEmpty)
            this.parts.add(new TrailPart(start, direction)); */
        this.parts.add(new TrailPart(start, direction));
    }

    public void update() {
        int delta = this.length - this.trailMaxLength;
        if (delta <= 0)
            return;

        this.length -= delta;

        TrailPart firstPart;
        while (delta > 0) {
            firstPart = parts.get(0);

            delta -= firstPart.subLength(delta);

            if (firstPart.getLength() == 0)
                parts.remove(firstPart);
        }
    }
    public void render(Graphics g) {
        for (TrailPart part : new ArrayList<>(parts)) {
            part.render(g);
        }
    }

    public void reset() {
        this.parts.clear();
        this.trailMaxLength = DEFAULT_MAX_LENGTH;
        this.length = 0;
    }

    public void addPart(TrailPart part) {
        if (parts.size() == TRAIL_MAX_PARTS) {
            final TrailPart removed = parts.remove(0);
            this.length -= removed.getLength();
        }

        parts.add(part);
    }
    public TrailPart getFirstPart() {
        if (parts.size() == 0)
            return null;
        return parts.get(0);
    }
    public TrailPart getLastPart() {
        if (parts.size() == 0)
            return null;
        return parts.get(parts.size() - 1);
    }
    public List<TrailPart> getParts() {
        return parts;
    }

    public int getTrailMaxLength() {
        return trailMaxLength;
    }
    public float getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public void addLength(float length) {
        this.length += length;
    }

    /* public boolean shouldBeEmpty() {
        return shouldBeEmpty;
    } */

    public static class TrailPart {

        public static final int TRAIL_THICKNESS = 3;
        public static final int TRAIL_THICKNESS_HALF = TRAIL_THICKNESS / 2;

        private Vector2 start;
        private final Direction direction;
        private int length;

        public TrailPart(Vector2 start, Direction direction) {
            this.start = start;
            this.direction = direction;
            this.length = 0;
        }

        public void render(Graphics g) {
            switch (this.direction) {
                case NORTH:
                    g.fillRect((int) (start.getX() - TRAIL_THICKNESS_HALF), (int) start.getY(), TRAIL_THICKNESS, length + TRAIL_THICKNESS_HALF);
                    break;
                case EAST:
                    g.fillRect((int) (start.getX() - length - TRAIL_THICKNESS_HALF), (int) (start.getY() - TRAIL_THICKNESS_HALF), length + TRAIL_THICKNESS_HALF, TRAIL_THICKNESS);
                    break;
                case SOUTH:
                    g.fillRect((int) (start.getX() - TRAIL_THICKNESS_HALF), (int) (start.getY() - length - TRAIL_THICKNESS_HALF), TRAIL_THICKNESS, length + TRAIL_THICKNESS_HALF);
                    break;
                case WEST:
                    g.fillRect((int) (start.getX() + TRAIL_THICKNESS_HALF), (int) (start.getY() - TRAIL_THICKNESS_HALF), length, TRAIL_THICKNESS);
                    break;
            }
        }

        public Vector2 getStart() {
            return start;
        }
        public void setStart(Vector2 start) {
            this.start = start;
        }
        public Vector2 getTopLeftCorner() {
            switch (this.direction) {
                case NORTH: return new Vector2(start.getX() - TRAIL_THICKNESS_HALF, start.getY());
                case EAST: return new Vector2(start.getX() - length - TRAIL_THICKNESS_HALF, start.getY() - TRAIL_THICKNESS_HALF);
                case SOUTH: return new Vector2(start.getX() - TRAIL_THICKNESS_HALF, start.getY() - length - TRAIL_THICKNESS_HALF);
                case WEST: return new Vector2(start.getX(), start.getY() - TRAIL_THICKNESS_HALF);
            }
            return null;
        }
        public Vector2 getBottomRightCorner() {
            switch (this.direction) {
                case NORTH: return new Vector2(start.getX() + TRAIL_THICKNESS_HALF, start.getY() + length + TRAIL_THICKNESS_HALF);
                case EAST: return new Vector2(start.getX(), start.getY() + TRAIL_THICKNESS_HALF);
                case SOUTH: return new Vector2(start.getX() + TRAIL_THICKNESS_HALF, start.getY());
                case WEST: return new Vector2(start.getX() + length + TRAIL_THICKNESS_HALF, start.getY() + TRAIL_THICKNESS_HALF);
            }
            return null;
        }
        public void addLength(int length) {
            this.length += length;
        }
        public int subLength(int length) {
            if (this.length - length >= 0) {
                this.length -= length;
                return length;
            }
            final int currentLength = this.length;
            this.length = 0;
            return currentLength;
        }
        public int getLength() {
            return length;
        }
    }
}
