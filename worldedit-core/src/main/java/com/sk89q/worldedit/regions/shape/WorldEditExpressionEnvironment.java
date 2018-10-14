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

package com.sk89q.worldedit.regions.shape;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.internal.expression.runtime.ExpressionEnvironment;
import com.sk89q.worldedit.math.BlockVector3d;
import com.sk89q.worldedit.math.Vector3d;

public class WorldEditExpressionEnvironment implements ExpressionEnvironment {

    private final Vector3d unit;
    private final Vector3d zero2;
    private Vector3d current = Vector3d.ZERO;
    private EditSession editSession;

    public WorldEditExpressionEnvironment(EditSession editSession, Vector3d unit, Vector3d zero) {
        this.editSession = editSession;
        this.unit = unit;
        this.zero2 = zero.add(0.5, 0.5, 0.5);
    }

    public BlockVector3d toWorld(double x, double y, double z) {
        // unscale, unoffset, round-nearest
        return new Vector3d(x, y, z).multiply(unit).add(zero2).toBlockPoint();
    }

    public Vector3d toWorldRel(double x, double y, double z) {
        return current.add(x, y, z);
    }

    @Override
    public int getBlockType(double x, double y, double z) {
        return editSession.getBlock(toWorld(x, y, z)).getBlockType().getLegacyId();
    }

    @Override
    public int getBlockData(double x, double y, double z) {
        return 0;
    }

    @Override
    public int getBlockTypeAbs(double x, double y, double z) {
        return editSession.getBlock(toWorld(x, y, z)).getBlockType().getLegacyId();
    }

    @Override
    public int getBlockDataAbs(double x, double y, double z) {
        return 0;
    }

    @Override
    public int getBlockTypeRel(double x, double y, double z) {
        return editSession.getBlock(toWorld(x, y, z)).getBlockType().getLegacyId();
    }

    @Override
    public int getBlockDataRel(double x, double y, double z) {
        return 0;
    }

    public void setCurrentBlock(Vector3d current) {
        this.current = current;
    }

}
