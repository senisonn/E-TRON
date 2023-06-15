package fr.ptut.etron.utils;

public enum Direction {
    NORTH(3, 1),
    EAST(0, 2),
    SOUTH(1, 3),
    WEST(2, 0);

    private int anticlockwiseId;
    private int clockwiseId;

    Direction(int anticlockwiseId, int clockwiseId) {
        this.anticlockwiseId = anticlockwiseId;
        this.clockwiseId = clockwiseId;
    }

    public Direction getAnticlockwise() {
        return Direction.values()[anticlockwiseId];
    }
    public Direction getClockwise() {
        return Direction.values()[clockwiseId];
    }
    public Vector2 getForwardVector() {
        Vector2 vector2 = null;
        switch (this) {
            case NORTH:
                vector2 = new Vector2(0, -1);
                break;
            case EAST:
                vector2 = new Vector2(1, 0);
                break;
            case SOUTH:
                vector2 = new Vector2(0, 1);
                break;
            case WEST:
                vector2 = new Vector2(-1, 0);
                break;
        }
        return vector2;
    }

    public int getAnticlockwiseId() {
        return anticlockwiseId;
    }
    public int getClockwiseId() {
        return clockwiseId;
    }
}