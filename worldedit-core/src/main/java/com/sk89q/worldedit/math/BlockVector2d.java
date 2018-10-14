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

import com.google.common.collect.ComparisonChain;
import com.sk89q.worldedit.math.transform.AffineTransform;

import java.util.Comparator;

/**
 * An immutable 2-dimensional vector.
 */
public final class BlockVector2d {
    
    public static final BlockVector2d ZERO = new BlockVector2d(0, 0);
    public static final BlockVector2d UNIT_X = new BlockVector2d(1, 0);
    public static final BlockVector2d UNIT_Z = new BlockVector2d(0, 1);
    public static final BlockVector2d ONE = new BlockVector2d(1, 1);

    /**
     * A comparator for BlockVector2ds that orders the vectors by rows, with x as the
     * column and z as the row.
     *
     * For example, if x is the horizontal axis and z is the vertical axis, it
     * sorts like so:
     *
     * <pre>
     * 0123
     * 4567
     * 90ab
     * cdef
     * </pre>
     */
    public static final Comparator<BlockVector2d> COMPARING_GRID_ARRANGEMENT = (a, b) -> {
        return ComparisonChain.start()
                .compare(a.getBlockZ(), b.getBlockZ())
                .compare(a.getBlockX(), b.getBlockX())
                .result();
    };

    private final int x, z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public BlockVector2d(double x, double z) {
        this((int) Math.floor(x), (int) Math.floor(z));
    }

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public BlockVector2d(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Get the X coordinate.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the X coordinate.
     *
     * @return the x coordinate
     */
    public int getBlockX() {
        return x;
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public BlockVector2d withX(int x) {
        return new BlockVector2d(x, z);
    }

    /**
     * Get the Z coordinate.
     *
     * @return the z coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Get the Z coordinate.
     *
     * @return the z coordinate
     */
    public int getBlockZ() {
        return z;
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public BlockVector2d withZ(int z) {
        return new BlockVector2d(x, z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector2d add(BlockVector2d other) {
        return add(other.x, other.z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param z the value to add
     * @return a new vector
     */
    public BlockVector2d add(int x, int z) {
        return new BlockVector2d(this.x + x, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public BlockVector2d add(BlockVector2d... others) {
        int newX = x, newZ = z;

        for (BlockVector2d other : others) {
            newX += other.x;
            newZ += other.z;
        }

        return new BlockVector2d(newX, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector2d subtract(BlockVector2d other) {
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
    public BlockVector2d subtract(int x, int z) {
        return new BlockVector2d(this.x - x, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public BlockVector2d subtract(BlockVector2d... others) {
        int newX = x, newZ = z;

        for (BlockVector2d other : others) {
            newX -= other.x;
            newZ -= other.z;
        }

        return new BlockVector2d(newX, newZ);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector2d multiply(BlockVector2d other) {
        return multiply(other.x, other.z);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param z the value to multiply
     * @return a new vector
     */
    public BlockVector2d multiply(int x, int z) {
        return new BlockVector2d(this.x * x, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public BlockVector2d multiply(BlockVector2d... others) {
        int newX = x, newZ = z;

        for (BlockVector2d other : others) {
            newX *= other.x;
            newZ *= other.z;
        }

        return new BlockVector2d(newX, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public BlockVector2d multiply(int n) {
        return multiply(n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector2d divide(BlockVector2d other) {
        return divide(other.x, other.z);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param z the value to divide by
     * @return a new vector
     */
    public BlockVector2d divide(int x, int z) {
        return new BlockVector2d(this.x / x, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public BlockVector2d divide(int n) {
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
    public int lengthSq() {
        return x * x + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(BlockVector2d other) {
        return Math.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public int distanceSq(BlockVector2d other) {
        int dx = other.x - x;
        int dz = other.z - z;
        return dx * dx + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public BlockVector2d normalize() {
        double len = length();
        double x = this.x / len;
        double z = this.z / len;
        return new BlockVector2d(x, z);
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public int dot(BlockVector2d other) {
        return x * other.x + z * other.z;
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, and Z are the lowest)
     * @param max the maximum point (X, Y, and Z are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(BlockVector2d min, BlockVector2d max) {
        return x >= min.x && x <= max.x
                && z >= min.z && z <= max.z;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public BlockVector2d floor() {
        // already floored, kept for feature parity with Vector2d
        return this;
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public BlockVector2d ceil() {
        // already raised, kept for feature parity with Vector2d
        return this;
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public BlockVector2d round() {
        // already rounded, kept for feature parity with Vector2d
        return this;
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public BlockVector2d abs() {
        return new BlockVector2d(Math.abs(x), Math.abs(z));
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
    public BlockVector2d transform2D(double angle, double aboutX, double aboutZ, double translateX, double translateZ) {
        angle = Math.toRadians(angle);
        double x = this.x - aboutX;
        double z = this.z - aboutZ;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x2 = x * cos - z * sin;
        double z2 = x * sin + z * cos;
        return new BlockVector2d(
                x2 + aboutX + translateX,
                z2 + aboutZ + translateZ);
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v2 the second vector
     * @return minimum
     */
    public BlockVector2d getMinimum(BlockVector2d v2) {
        return new BlockVector2d(
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
    public BlockVector2d getMaximum(BlockVector2d v2) {
        return new BlockVector2d(
            Math.max(x, v2.x),
            Math.max(z, v2.z)
        );
    }

    public Vector2d toVector2d() {
        return new Vector2d(x, z);
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

    /**
     * Creates a 3D vector by adding a zero Y component to this vector.
     *
     * @return a new vector
     */
    public BlockVector3d toBlockVector3d() {
        return toBlockVector3d(0);
    }

    /**
     * Creates a 3D vector by adding the specified Y component to this vector.
     *
     * @param y the Y component
     * @return a new vector
     */
    public BlockVector3d toBlockVector3d(int y) {
        return new BlockVector3d(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockVector2d)) {
            return false;
        }

        BlockVector2d other = (BlockVector2d) obj;
        return other.x == this.x && other.z == this.z;

    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Integer.hashCode(x);
        hash = 31 * hash + Integer.hashCode(z);
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }

}