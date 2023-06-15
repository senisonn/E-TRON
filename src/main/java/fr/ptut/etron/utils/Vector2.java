package fr.ptut.etron.utils;

public class Vector2 {

    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void set(Vector2 position) {
        this.x = position.getX();
        this.y = position.getY();
    }
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }
    public void add(Vector2 position) {
        this.x += position.getX();
        this.y += position.getY();
    }
    public void sub(float x, float y) {
        this.x -= x;
        this.y -= y;
    }
    public void sub(Vector2 position) {
        this.x -= position.getX();
        this.y -= position.getY();
    }

    public Vector2 mult(float value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public float simpleLength() {
        final float x = Math.abs(this.x);
        final float y = Math.abs(this.y);

        if (x > y)
            return x;
        return y;
    }
    public float simpleDistance(Vector2 position) {
        if (this.x == position.getX())
            return position.y - this.y;
        else
            return position.x - this.x;
    }

    public Vector2 clone() {
        return new Vector2(this.x, this.y);
    }

    @Override
    public String toString() {
        return "Position(" + x + "," + y + ")";
    }
}