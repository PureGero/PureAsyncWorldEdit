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
import com.sk89q.worldedit.regions.iterator.RegionIterator;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.storage.ChunkStore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractRegion implements Region {

    protected World world;

    public AbstractRegion(World world) {
        this.world = world;
    }

    @Override
    public Vector3d getCenter() {
        return getMinimumPoint().add(getMaximumPoint()).toVector3d().divide(2);
    }

    /**
     * Get the iterator.
     *
     * @return iterator of points inside the region
     */
    @Override
    public Iterator<BlockVector3d> iterator() {
        return new RegionIterator(this);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void shift(BlockVector3d change) throws RegionOperationException {
        expand(change);
        contract(change);
    }

    @Override
    public AbstractRegion clone() {
        try {
            return (AbstractRegion) super.clone();
        } catch (CloneNotSupportedException exc) {
            return null;
        }
    }

    @Override
    public List<BlockVector2d> polygonize(int maxPoints) {
        if (maxPoints >= 0 && maxPoints < 4) {
            throw new IllegalArgumentException("Cannot polygonize an AbstractRegion with no overridden polygonize method into less than 4 points.");
        }

        final BlockVector3d min = getMinimumPoint();
        final BlockVector3d max = getMaximumPoint();

        final List<BlockVector2d> points = new ArrayList<>(4);

        points.add(new BlockVector2d(min.getX(), min.getZ()));
        points.add(new BlockVector2d(min.getX(), max.getZ()));
        points.add(new BlockVector2d(max.getX(), max.getZ()));
        points.add(new BlockVector2d(max.getX(), min.getZ()));

        return points;
    }

    /**
     * Get the number of blocks in the region.
     *
     * @return number of blocks
     */
    @Override
    public int getArea() {
        BlockVector3d min = getMinimumPoint();
        BlockVector3d max = getMaximumPoint();

        return (max.getX() - min.getX() + 1) *
                (max.getY() - min.getY() + 1) *
                (max.getZ() - min.getZ() + 1);
    }

    /**
     * Get X-size.
     *
     * @return width
     */
    @Override
    public int getWidth() {
        BlockVector3d min = getMinimumPoint();
        BlockVector3d max = getMaximumPoint();

        return max.getX() - min.getX() + 1;
    }

    /**
     * Get Y-size.
     *
     * @return height
     */
    @Override
    public int getHeight() {
        BlockVector3d min = getMinimumPoint();
        BlockVector3d max = getMaximumPoint();

        return max.getY() - min.getY() + 1;
    }

    /**
     * Get Z-size.
     *
     * @return length
     */
    @Override
    public int getLength() {
        BlockVector3d min = getMinimumPoint();
        BlockVector3d max = getMaximumPoint();

        return max.getZ() - min.getZ() + 1;
    }

    /**
     * Get a list of chunks.
     *
     * @return a set of chunks
     */
    @Override
    public Set<BlockVector2d> getChunks() {
        final Set<BlockVector2d> chunks = new HashSet<>();

        final BlockVector3d min = getMinimumPoint();
        final BlockVector3d max = getMaximumPoint();

        final int minY = min.getBlockY();

        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                if (!contains(new BlockVector3d(x, minY, z))) {
                    continue;
                }

                chunks.add(new BlockVector2d(
                    x >> ChunkStore.CHUNK_SHIFTS,
                    z >> ChunkStore.CHUNK_SHIFTS
                ));
            }
        }

        return chunks;
    }

    @Override
    public Set<BlockVector3d> getChunkCubes() {
        final Set<BlockVector3d> chunks = new HashSet<>();

        final BlockVector3d min = getMinimumPoint();
        final BlockVector3d max = getMaximumPoint();

        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    if (!contains(new BlockVector3d(x, y, z))) {
                        continue;
                    }

                    chunks.add(new BlockVector3d(
                        x >> ChunkStore.CHUNK_SHIFTS,
                        y >> ChunkStore.CHUNK_SHIFTS,
                        z >> ChunkStore.CHUNK_SHIFTS
                    ));
                }
            }
        }

        return chunks;
    }

}
