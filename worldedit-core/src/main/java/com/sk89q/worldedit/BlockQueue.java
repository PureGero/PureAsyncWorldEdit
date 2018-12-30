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

package com.sk89q.worldedit;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.Queue;

import com.sk89q.worldedit.EditSession.Stage;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockStateHolder;

/**
 * A place to queue blocks for async modification.
 * 
 * @author PureGero
 */
public class BlockQueue extends AbstractDelegateExtent implements Closeable {
    
    EditSession session;
    
    private Queue<Queued> queue = new LinkedList<>();
    
    public BlockQueue(Extent extent, EditSession session) {
        super(extent);
        this.session = session;
    }

    @Override
    public boolean setBlock(BlockVector3 location, BlockStateHolder block) throws WorldEditException {
        return queue(location, block);
    }
    
    public boolean queue(BlockVector3 position, BlockStateHolder block) {
        if (queue.isEmpty())
            WorldEdit.getInstance().getBlockQueueManager().addBlockQueue(this);;
        queue.add(new Queued(position, block));
        return true;
    }
    
    public void doBlock() throws WorldEditException {
        if (queue.isEmpty()) return;
        Queued q = queue.poll();
        super.setBlock(q.position, q.block);
    }
    
    @Override
    public void close() {
        session.close();
    }
    
    public int size() {
        return queue.size();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    private static class Queued {
        private BlockVector3 position;
        private BlockStateHolder block;
        
        private Queued(BlockVector3 position, BlockStateHolder block) {
            this.position = position;
            this.block = block;
        }
    }

}
