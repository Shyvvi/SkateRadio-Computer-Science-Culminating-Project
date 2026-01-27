package net.shyvv.core;

import java.awt.*;

// unfortunately went unused after I switched to using JavaFX :(
// JavaFX is a lifesaver though, I probably wouldn't have been able to get this project done without it
@Deprecated
public class Vec2d {
    private double x;
    private double y;
    /**
     * creates an object for storing x and y values
     * @param x x value
     * @param y y value
     */
    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * creates an object for storing x and y values
     * @param dimension the Dimension object which will be turned into a Vec2d
     */
    public Vec2d(Dimension dimension) {
        this.x = dimension.getWidth();
        this.y = dimension.getHeight();
    }
    /**
     * creates an object for storing x and y values
     * @param point the Point object which will be turned into a Vec2d
     */
    public Vec2d(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * sets the values of this Vec2d to 0
     */
    public void clear() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * returns the x value of this Vec2d
     * @return this.x
     */
    public double getX() {
        return this.x;
    }

    /**
     * returns the y value of this Vec2d
     * @return this.y
     */
    public double getY() {
        return this.y;
    }

    /**
     * returns the x value of this Vec2d as an integer
     * @return this.x casted to an integer
     */
    public int getIntX() {
        return (int) this.x;
    }
    /**
     * returns the y value of this Vec2d as an integer
     * @return this.y casted to an integer
     */
    public int getIntY() {
        return (int) this.y;
    }

    /**
     * returns a JSwing Dimension object with the values of this Vec2d
     * @return Dimension objects with the x and y values of this Vec2d
     */
    public Dimension getDimension() {
        return new Dimension((int)this.x, (int)this.y);
    }

    /**
     * returns a JSwing Point object with the values of this Vec2d
     * @return Point objects with the x and y values of this Vec2d
     */
    public Point getPoint() {
        return new Point((int)this.x, (int)this.y);
    }

    /**
     * multiplies both values of this Vec2d by a double
     * @param value the value to multiply both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d multiply(double value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    /**
     * multiplies the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to multiply this vector by
     * @return this Vec2d
     */
    public Vec2d multiply(Vec2d vec2d) {
        this.x *= vec2d.getX();
        this.y *= vec2d.getY();
        return this;
    }

    /**
     * divides both values of this Vec2d by a double
     * @param value the value to divide both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d divide(double value) {
        this.y /= value;
        this.y /= value;
        return this;
    }
    /**
     * divides the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to divide this vector by
     * @return this Vec2d
     */
    public Vec2d divide(Vec2d vec2d) {
        this.y /= vec2d.getX();
        this.y /= vec2d.getY();
        return this;
    }

    /**
     * subtracts both values of this Vec2d by a double
     * @param value the value to subtract both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d subtract(double value) {
        this.y -= value;
        this.y -= value;
        return this;
    }
    /**
     * subtracts the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to subtract this vector by
     * @return this Vec2d
     */
    public Vec2d subtract(Vec2d vec2d) {
        this.y -= vec2d.getX();
        this.y -= vec2d.getY();
        return this;
    }

    /**
     * adds both values of this Vec2d by a double
     * @param value the value to add both the x and y values of this Vec2d
     * @return this Vec2d
     */
    public Vec2d add(double value) {
        this.y += value;
        this.y += value;
        return this;
    }
    /**
     * adds the x and y values of this Vec2d by the x and y values of another Vec2d
     * @param vec2d the vector to add this vector by
     * @return this Vec2d
     */
    public Vec2d add(Vec2d vec2d) {
        this.y += vec2d.getX();
        this.y += vec2d.getY();
        return this;
    }
}
