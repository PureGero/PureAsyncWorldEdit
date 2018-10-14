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
import com.sk89q.worldedit.world.storage.ChunkStore;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an ellipsoid region.
 */
public class EllipsoidRegion extends AbstractRegion {

    /**
     * Stores the center.
     */
    private BlockVector3d center;

    /**
     * Stores the radii plus 0.5 on each axis.
     */
    private Vector3d radius;

    /**
     * Construct a new instance of this ellipsoid region.
     *
     * @param pos1 the first position
     * @param pos2 the second position
     */
    public EllipsoidRegion(BlockVector3d pos1, Vector3d pos2) {
        this(null, pos1, pos2);
    }

    /**
     * Construct a new instance of this ellipsoid region.
     *
     * @param world the world
     * @param center the center
     * @param radius the radius
     */
    public EllipsoidRegion(World world, BlockVector3d center, Vector3d radius) {
        super(world);
        this.center = center;
        setRadius(radius);
    }

    public EllipsoidRegion(EllipsoidRegion ellipsoidRegion) {
        this(ellipsoidRegion.world, ellipsoidRegion.center, ellipsoidRegion.getRadius());
    }

    @Override
    public BlockVector3d getMinimumPoint() {
        return center.toVector3d().subtract(getRadius()).toBlockPoint();
    }

    @Override
    public BlockVector3d getMaximumPoint() {
        return center.toVector3d().add(getRadius()).toBlockPoint();
    }

    @Override
    public int getArea() {
        return (int) Math.floor((4.0 / 3.0) * Math.PI * radius.getX() * radius.getY() * radius.getZ());
    }

    @Override
    public int getWidth() {
        return (int) (2 * radius.getX());
    }

    @Override
    public int getHeight() {
        return (int) (2 * radius.getY());
    }

    @Override
    public int getLength() {
        return (int) (2 * radius.getZ());
    }

    private BlockVector3d calculateDiff(BlockVector3d... changes) throws RegionOperationException {
        BlockVector3d diff = BlockVector3d.ZERO.add(changes);

        if ((diff.getBlockX() & 1) + (diff.getBlockY() & 1) + (diff.getBlockZ() & 1) != 0) {
            throw new RegionOperationException(
                    "Ellipsoid changes must be even for each dimensions.");
        }

        return diff.divide(2).floor();
    }

    private Vector3d calculateChanges(BlockVector3d... changes) {
        Vector3d total = Vector3d.ZERO;
        for (BlockVector3d change : changes) {
            total = total.add(change.abs().toVector3d());
        }

        return total.divide(2).floor();
    }

    @Override
    public void expand(BlockVector3d... changes) throws RegionOperationException {
        center = center.add(calculateDiff(changes));
        radius = radius.add(calculateChanges(changes));
    }

    @Override
    public void contract(BlockVector3d... changes) throws RegionOperationException {
        center = center.subtract(calculateDiff(changes));
        Vector3d newRadius = radius.subtract(calculateChanges(changes));
        radius = new Vector3d(1.5, 1.5, 1.5).getMaximum(newRadius);
    }

    @Override
    public void shift(BlockVector3d change) throws RegionOperationException {
        center = center.add(change);
    }

    /**
     * Get the center.
     *
     * @return center
     */
    @Override
    public Vector3d getCenter() {
        return center.toVector3d();
    }

    /**
     * Set the center.
     *
     * @param center the center
     */
    public void setCenter(BlockVector3d center) {
        this.center = center;
    }

    /**
     * Get the radii.
     *
     * @return radii
     */
    public Vector3d getRadius() {
        return radius.subtract(0.5, 0.5, 0.5);
    }

    /**
     * Set the radii.
     *
     * @param radius the radius
     */
    public void setRadius(Vector3d radius) {
        this.radius = radius.add(0.5, 0.5, 0.5);
    }

    @Override
    public Set<BlockVector2d> getChunks() {
        final Set<BlockVector2d> chunks = new HashSet<>();

        final BlockVector3d min = getMinimumPoint();
        final BlockVector3d max = getMaximumPoint();
        final int centerY = center.getBlockY();

        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                if (!contains(new BlockVector3d(x, centerY, z))) {
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
    public boolean contains(BlockVector3d position) {
        return position.subtract(center).toVector3d().divide(radius).lengthSq() <= 1;
    }

    /**
     * Returns string representation in the format
     * "(centerX, centerY, centerZ) - (radiusX, radiusY, radiusZ)".
     *
     * @return string
     */
    @Override
    public String toString() {
        return center + " - " + getRadius();
    }

    public void extendRadius(Vector3d minRadius) {
        setRadius(minRadius.getMaximum(getRadius()));
    }

    @Override
    public EllipsoidRegion clone() {
        return (EllipsoidRegion) super.clone();
    }

}
