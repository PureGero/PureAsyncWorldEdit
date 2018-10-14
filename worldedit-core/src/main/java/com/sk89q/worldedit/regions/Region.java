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

package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.math.BlockVector2d;
import com.sk89q.worldedit.math.BlockVector3d;
import com.sk89q.worldedit.math.Vector3d;
import com.sk89q.worldedit.world.World;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Represents a physical shape.
 */
public interface Region extends Iterable<BlockVector3d>, Cloneable {

    /**
     * Get the lower point of a region.
     *
     * @return min. point
     */
    BlockVector3d getMinimumPoint();

    /**
     * Get the upper point of a region.
     *
     * @return max. point
     */
    BlockVector3d getMaximumPoint();

    /**
     * Get the center point of a region.
     * Note: Coordinates will not be integers
     * if the corresponding lengths are even.
     *
     * @return center point
     */
    Vector3d getCenter();

    /**
     * Get the number of blocks in the region.
     *
     * @return number of blocks
     */
    int getArea();

    /**
     * Get X-size.
     *
     * @return width
     */
    int getWidth();

    /**
     * Get Y-size.
     *
     * @return height
     */
    int getHeight();

    /**
     * Get Z-size.
     *
     * @return length
     */
    int getLength();

    /**
     * Expand the region.
     *
     * @param changes array/arguments with multiple related changes
     * @throws RegionOperationException
     */
    void expand(BlockVector3d... changes) throws RegionOperationException;

    /**
     * Contract the region.
     *
     * @param changes array/arguments with multiple related changes
     * @throws RegionOperationException
     */
    void contract(BlockVector3d... changes) throws RegionOperationException;

    /**
     * Shift the region.
     *
     * @param change the change
     * @throws RegionOperationException
     */
    void shift(BlockVector3d change) throws RegionOperationException;

    /**
     * Returns true based on whether the region contains the point.
     *
     * @param position the position
     * @return true if contained
     */
    boolean contains(BlockVector3d position);

    /**
     * Get a list of chunks.
     *
     * @return a list of chunk coordinates
     */
    Set<BlockVector2d> getChunks();

    /**
     * Return a list of 16*16*16 chunks in a region
     *
     * @return the chunk cubes this region overlaps with
     */
    Set<BlockVector3d> getChunkCubes();

    /**
     * Sets the world that the selection is in.
     *
     * @return the world, or null
     */
    @Nullable World getWorld();

    /**
     * Sets the world that the selection is in.
     *
     * @param world the world, which may be null
     */
    void setWorld(@Nullable World world);

    /**
     * Make a clone of the region.
     *
     * @return a cloned version
     */
    Region clone();

    /**
     * Polygonizes a cross-section or a 2D projection of the region orthogonal to the Y axis.
     *
     * @param maxPoints maximum number of points to generate. -1 for no limit.
     * @return the points.
     */
    List<BlockVector2d> polygonize(int maxPoints);
}
