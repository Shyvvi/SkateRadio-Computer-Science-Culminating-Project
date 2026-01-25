package net.shyvv.core;

import java.awt.*;

public class Vec2d {
    private double x;
    private double y;
    /**
     * Creates an object for storing x and y values
     * @param x x value
     * @param y y value
     */
    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates an object for storing x and y values
     * @param dimension the Dimension object which will be turned into a Vec2d
     */
    public Vec2d(Dimension dimension) {
        this.x = dimension.getWidth();
        this.y = dimension.getHeight();
    }
    /**
     * Creates an object for storing x and y values
     * @param point the Point object which will be turned into a Vec2d
     */
    public Vec2d(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * Sets the values of this Vec2d to 0
     */
    public void clear() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Returns the x value of this Vec2d
     * @return this.x
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns the y value of this Vec2d
     * @return this.y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Returns the x value of this Vec2d as an integer
     * @return this.x casted to an integer
     */
    public int getIntX() {
        return (int) this.x;
    }
    /**
     * Returns the y value of this Vec2d as an integer
     * @return this.y casted to an integer
     */
    public int getIntY() {
        return (int) this.y;
    }

    /**
     * Returns a JSwing Dimension object with the values of this Vec2d
     * @return Dimension objects with the x and y values of this Vec2d
     */
    public Dimension getDimension() {
        return new Dimension((int)this.x, (int)this.y);
    }

    /**
     * Returns a JSwing Point object with the values of this Vec2d
     * @return Point objects with the x and y values of this Vec2d
     */
    public Point getPoint() {
        return new Point((int)this.x, (int)this.y);
    }

    /**
     * Multiplies both values of this Vec2d by a double
     * @param value the value to multiply both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d multiply(double value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    /**
     * Multiplies the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to multiply this vector by
     * @return this Vec2d
     */
    public Vec2d multiply(Vec2d vec2d) {
        this.x *= vec2d.getX();
        this.y *= vec2d.getY();
        return this;
    }

    /**
     * Divides both values of this Vec2d by a double
     * @param value the value to divide both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d divide(double value) {
        this.y /= value;
        this.y /= value;
        return this;
    }
    /**
     * Divides the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to divide this vector by
     * @return this Vec2d
     */
    public Vec2d divide(Vec2d vec2d) {
        this.y /= vec2d.getX();
        this.y /= vec2d.getY();
        return this;
    }

    /**
     * Subtracts both values of this Vec2d by a double
     * @param value the value to subtract both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d subtract(double value) {
        this.y -= value;
        this.y -= value;
        return this;
    }
    /**
     * Subtracts the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to subtract this vector by
     * @return this Vec2d
     */
    public Vec2d subtract(Vec2d vec2d) {
        this.y -= vec2d.getX();
        this.y -= vec2d.getY();
        return this;
    }

    /**
     * Adds both values of this Vec2d by a double
     * @param value the value to add both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d add(double value) {
        this.y += value;
        this.y += value;
        return this;
    }
    /**
     * Adds the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to add this vector by
     * @return this Vec2d
     */
    public Vec2d add(Vec2d vec2d) {
        this.y += vec2d.getX();
        this.y += vec2d.getY();
        return this;
    }
}
