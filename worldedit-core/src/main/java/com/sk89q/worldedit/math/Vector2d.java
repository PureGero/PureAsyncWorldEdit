/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.math;

import com.sk89q.worldedit.math.transform.AffineTransform;

/**
 * An immutable 2-dimensional vector.
 */
public final class Vector2d {
    
    public static final Vector2d ZERO = new Vector2d(0, 0);
    public static final Vector2d UNIT_X = new Vector2d(1, 0);
    public static final Vector2d UNIT_Z = new Vector2d(0, 1);
    public static final Vector2d ONE = new Vector2d(1, 1);

    private final double x, z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public Vector2d(double x, double z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Copy another vector.
     *
     * @param other the other vector
     */
    public Vector2d(Vector2d other) {
        this.x = other.x;
        this.z = other.z;
    }

    /**
     * Get the X coordinate.
     *
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector2d withX(double x) {
        return new Vector2d(x, z);
    }

    /**
     * Get the Z coordinate.
     *
     * @return the z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector2d withZ(double z) {
        return new Vector2d(x, z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d add(Vector2d other) {
        return add(other.x, other.z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param z the value to add
     * @return a new vector
     */
    public Vector2d add(double x, double z) {
        return new Vector2d(this.x + x, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d add(Vector2d... others) {
        double newX = x, newZ = z;

        for (Vector2d other : others) {
            newX += other.x;
            newZ += other.z;
        }

        return new Vector2d(newX, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d subtract(Vector2d other) {
        return subtract(other.x, other.z);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param x the value to subtract
     * @param z the value to subtract
     * @return a new vector
     */
    public Vector2d subtract(double x, double z) {
        return new Vector2d(this.x - x, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d subtract(Vector2d... others) {
        double newX = x, newZ = z;

        for (Vector2d other : others) {
            newX -= other.x;
            newZ -= other.z;
        }

        return new Vector2d(newX, newZ);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d multiply(Vector2d other) {
        return multiply(other.x, other.z);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param z the value to multiply
     * @return a new vector
     */
    public Vector2d multiply(double x, double z) {
        return new Vector2d(this.x * x, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d multiply(Vector2d... others) {
        double newX = x, newZ = z;

        for (Vector2d other : others) {
            newX *= other.x;
            newZ *= other.z;
        }

        return new Vector2d(newX, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector2d multiply(double n) {
        return multiply(n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d divide(Vector2d other) {
        return divide(other.x, other.z);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param z the value to divide by
     * @return a new vector
     */
    public Vector2d divide(double x, double z) {
        return new Vector2d(this.x / x, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector2d divide(double n) {
        return divide(n, n);
    }

    /**
     * Get the length of the vector.
     *
     * @return length
     */
    public double length() {
        return Math.sqrt(lengthSq());
    }

    /**
     * Get the length, squared, of the vector.
     *
     * @return length, squared
     */
    public double lengthSq() {
        return x * x + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(Vector2d other) {
        return Math.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public double distanceSq(Vector2d other) {
        double dx = other.x - x;
        double dz = other.z - z;
        return dx * dx + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector2d normalize() {
        return divide(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public double dot(Vector2d other) {
        return x * other.x + z * other.z;
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, and Z are the lowest)
     * @param max the maximum point (X, Y, and Z are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(Vector2d min, Vector2d max) {
        return x >= min.x && x <= max.x
                && z >= min.z && z <= max.z;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector2d floor() {
        return new Vector2d(Math.floor(x), Math.floor(z));
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector2d ceil() {
        return new Vector2d(Math.ceil(x), Math.ceil(z));
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector2d round() {
        return new Vector2d(Math.floor(x + 0.5), Math.floor(z + 0.5));
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector2d abs() {
        return new Vector2d(Math.abs(x), Math.abs(z));
    }

    /**
     * Perform a 2D transformation on this vector and return a new one.
     *
     * @param angle in degrees
     * @param aboutX about which x coordinate to rotate
     * @param aboutZ about which z coordinate to rotate
     * @param translateX what to add after rotation
     * @param translateZ what to add after rotation
     * @return a new vector
     * @see AffineTransform another method to transform vectors
     */
    public Vector2d transform2D(double angle, double aboutX, double aboutZ, double translateX, double translateZ) {
        angle = Math.toRadians(angle);
        double x = this.x - aboutX;
        double z = this.z - aboutZ;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x2 = x * cos - z * sin;
        double z2 = x * sin + z * cos;
        return new Vector2d(
                x2 + aboutX + translateX,
                z2 + aboutZ + translateZ);
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v2 the second vector
     * @return minimum
     */
    public Vector2d getMinimum(Vector2d v2) {
        return new Vector2d(
            Math.min(x, v2.x),
            Math.min(z, v2.z)
        );
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v2 the second vector
     * @return maximum
     */
    public Vector2d getMaximum(Vector2d v2) {
        return new Vector2d(
            Math.max(x, v2.x),
            Math.max(z, v2.z)
        );
    }
    
    public static BlockVector2d toBlockPoint(double x, double z) {
        return new BlockVector2d(x, z);
    }

    /**
     * Create a new {@link BlockVector2d} from this vector.
     *
     * @return a new {@link BlockVector2d}
     */
    public BlockVector2d toBlockPoint() {
        return toBlockPoint(x, z);
    }

    /**
     * Creates a 3D vector by adding a zero Y component to this vector.
     *
     * @return a new vector
     */
    public Vector3d toVector3d() {
        return toVector3d(0);
    }

    /**
     * Creates a 3D vector by adding the specified Y component to this vector.
     *
     * @param y the Y component
     * @return a new vector
     */
    public Vector3d toVector3d(double y) {
        return new Vector3d(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2d)) {
            return false;
        }

        Vector2d other = (Vector2d) obj;
        return other.x == this.x && other.z == this.z;

    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Double.hashCode(x);
        hash = 31 * hash + Double.hashCode(z);
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }

}