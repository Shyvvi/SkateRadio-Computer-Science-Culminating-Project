package net.shyvv.core;

import java.awt.*;

public class Vec2d {
    private double x;
    private double y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d(Dimension dimension) {
        this.x = dimension.getWidth();
        this.y = dimension.getHeight();
    }

    public Vec2d(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    public void clear() {
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }

    public int getIntX() {
        return (int) this.x;
    }
    public int getIntY() {
        return (int) this.y;
    }

    public Dimension getDimension() {
        return new Dimension((int)this.x, (int)this.y);
    }

    public Point getPoint() {
        return new Point((int)this.x, (int)this.y);
    }

    public Vec2d multiply(double value) {
        this.x *= value;
        this.y *= value;
        return this;
    }
    public Vec2d multiply(Vec2d vec2d) {
        this.x *= vec2d.getX();
        this.y *= vec2d.getY();
        return this;
    }

    public Vec2d divide(double value) {
        this.y /= value;
        this.y /= value;
        return this;
    }
    public Vec2d divide(Vec2d vec2d) {
        this.y /= vec2d.getX();
        this.y /= vec2d.getY();
        return this;
    }

    public Vec2d subtract(double value) {
        this.y -= value;
        this.y -= value;
        return this;
    }
    public Vec2d subtract(Vec2d vec2d) {
        this.y -= vec2d.getX();
        this.y -= vec2d.getY();
        return this;
    }

    public Vec2d add(double value) {
        this.y += value;
        this.y += value;
        return this;
    }
    public Vec2d add(Vec2d vec2d) {
        this.y += vec2d.getX();
        this.y += vec2d.getY();
        return this;
    }
}
