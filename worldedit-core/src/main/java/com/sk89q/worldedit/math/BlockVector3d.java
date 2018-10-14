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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ComparisonChain;
import com.sk89q.worldedit.math.transform.AffineTransform;

import java.util.Comparator;

/**
 * An immutable 3-dimensional vector.
 */
public final class BlockVector3d {

    public static final BlockVector3d ZERO = new BlockVector3d(0, 0, 0);
    public static final BlockVector3d UNIT_X = new BlockVector3d(1, 0, 0);
    public static final BlockVector3d UNIT_Y = new BlockVector3d(0, 1, 0);
    public static final BlockVector3d UNIT_Z = new BlockVector3d(0, 0, 1);
    public static final BlockVector3d ONE = new BlockVector3d(1, 1, 1);

    // thread-safe initialization idiom
    private static final class YzxOrderComparator {
        private static final Comparator<BlockVector3d> YZX_ORDER = (a, b) -> {
            return ComparisonChain.start()
                    .compare(a.y, b.y)
                    .compare(a.z, b.z)
                    .compare(a.x, b.x)
                    .result();
        };
    }

    /**
     * Returns a comparator that sorts vectors first by Y, then Z, then X.
     * 
     * <p>
     * Useful for sorting by chunk block storage order.
     */
    public static Comparator<BlockVector3d> sortByCoordsYzx() {
        return YzxOrderComparator.YZX_ORDER;
    }

    private final int x, y, z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public BlockVector3d(double x, double y, double z) {
        this((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public BlockVector3d(int x, int y, int z) {
        this.x = x;
        this.y = y;
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
    public BlockVector3d withX(int x) {
        return new BlockVector3d(x, y, z);
    }

    /**
     * Get the Y coordinate.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Get the Y coordinate.
     *
     * @return the y coordinate
     */
    public int getBlockY() {
        return y;
    }

    /**
     * Set the Y coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public BlockVector3d withY(int y) {
        return new BlockVector3d(x, y, z);
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
    public BlockVector3d withZ(int z) {
        return new BlockVector3d(x, y, z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector3d add(BlockVector3d other) {
        return add(other.x, other.y, other.z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param y the value to add
     * @param z the value to add
     * @return a new vector
     */
    public BlockVector3d add(int x, int y, int z) {
        return new BlockVector3d(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public BlockVector3d add(BlockVector3d... others) {
        int newX = x, newY = y, newZ = z;

        for (BlockVector3d other : others) {
            newX += other.x;
            newY += other.y;
            newZ += other.z;
        }

        return new BlockVector3d(newX, newY, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector3d subtract(BlockVector3d other) {
        return subtract(other.x, other.y, other.z);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param x the value to subtract
     * @param y the value to subtract
     * @param z the value to subtract
     * @return a new vector
     */
    public BlockVector3d subtract(int x, int y, int z) {
        return new BlockVector3d(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public BlockVector3d subtract(BlockVector3d... others) {
        int newX = x, newY = y, newZ = z;

        for (BlockVector3d other : others) {
            newX -= other.x;
            newY -= other.y;
            newZ -= other.z;
        }

        return new BlockVector3d(newX, newY, newZ);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector3d multiply(BlockVector3d other) {
        return multiply(other.x, other.y, other.z);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param y the value to multiply
     * @param z the value to multiply
     * @return a new vector
     */
    public BlockVector3d multiply(int x, int y, int z) {
        return new BlockVector3d(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public BlockVector3d multiply(BlockVector3d... others) {
        int newX = x, newY = y, newZ = z;

        for (BlockVector3d other : others) {
            newX *= other.x;
            newY *= other.y;
            newZ *= other.z;
        }

        return new BlockVector3d(newX, newY, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public BlockVector3d multiply(int n) {
        return multiply(n, n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public BlockVector3d divide(BlockVector3d other) {
        return divide(other.x, other.y, other.z);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param y the value to divide by
     * @param z the value to divide by
     * @return a new vector
     */
    public BlockVector3d divide(int x, int y, int z) {
        return new BlockVector3d(this.x / x, this.y / y, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public BlockVector3d divide(int n) {
        return divide(n, n, n);
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
        return x * x + y * y + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(BlockVector3d other) {
        return Math.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public int distanceSq(BlockVector3d other) {
        int dx = other.x - x;
        int dy = other.y - y;
        int dz = other.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public BlockVector3d normalize() {
        double len = length();
        double x = this.x / len;
        double y = this.y / len;
        double z = this.z / len;
        return new BlockVector3d(x, y, z);
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public double dot(BlockVector3d other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Gets the cross product of this and another vector.
     *
     * @param other the other vector
     * @return the cross product of this and the other vector
     */
    public BlockVector3d cross(BlockVector3d other) {
        return new BlockVector3d(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, and Z are the lowest)
     * @param max the maximum point (X, Y, and Z are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(BlockVector3d min, BlockVector3d max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
    }

    /**
     * Clamp the Y component.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a new vector
     */
    public BlockVector3d clampY(int min, int max) {
        checkArgument(min <= max, "minimum cannot be greater than maximum");
        if (y < min) {
            return new BlockVector3d(x, min, z);
        }
        if (y > max) {
            return new BlockVector3d(x, max, z);
        }
        return this;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public BlockVector3d floor() {
        // already floored, kept for feature parity with Vector3d
        return this;
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public BlockVector3d ceil() {
        // already raised, kept for feature parity with Vector3d
        return this;
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public BlockVector3d round() {
        // already rounded, kept for feature parity with Vector3d
        return this;
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public BlockVector3d abs() {
        return new BlockVector3d(Math.abs(x), Math.abs(y), Math.abs(z));
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
    public BlockVector3d transform2D(double angle, double aboutX, double aboutZ, double translateX, double translateZ) {
        angle = Math.toRadians(angle);
        double x = this.x - aboutX;
        double z = this.z - aboutZ;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x2 = x * cos - z * sin;
        double z2 = x * sin + z * cos;

        return new BlockVector3d(
            x2 + aboutX + translateX,
            y,
            z2 + aboutZ + translateZ
        );
    }

    /**
     * Get this vector's pitch as used within the game.
     *
     * @return pitch in radians
     */
    public double toPitch() {
        double x = getX();
        double z = getZ();

        if (x == 0 && z == 0) {
            return getY() > 0 ? -90 : 90;
        } else {
            double x2 = x * x;
            double z2 = z * z;
            double xz = Math.sqrt(x2 + z2);
            return Math.toDegrees(Math.atan(-getY() / xz));
        }
    }

    /**
     * Get this vector's yaw as used within the game.
     *
     * @return yaw in radians
     */
    public double toYaw() {
        double x = getX();
        double z = getZ();

        double t = Math.atan2(-x, z);
        double tau = 2 * Math.PI;

        return Math.toDegrees(((t + tau) % tau));
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v2 the second vector
     * @return minimum
     */
    public BlockVector3d getMinimum(BlockVector3d v2) {
        return new BlockVector3d(
                Math.min(x, v2.x),
                Math.min(y, v2.y),
                Math.min(z, v2.z)
        );
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v2 the second vector
     * @return maximum
     */
    public BlockVector3d getMaximum(BlockVector3d v2) {
        return new BlockVector3d(
                Math.max(x, v2.x),
                Math.max(y, v2.y),
                Math.max(z, v2.z)
        );
    }

    /**
     * Creates a 2D vector by dropping the Y component from this vector.
     *
     * @return a new {@link BlockVector2d}
     */
    public BlockVector2d toBlockVector2d() {
        return new BlockVector2d(x, z);
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockVector3d)) {
            return false;
        }

        BlockVector3d other = (BlockVector3d) obj;
        return other.x == this.x && other.y == this.y && other.z == this.z;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Integer.hashCode(x);
        hash = 31 * hash + Integer.hashCode(y);
        hash = 31 * hash + Integer.hashCode(z);
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}