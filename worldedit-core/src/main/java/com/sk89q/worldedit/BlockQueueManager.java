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

import java.util.ArrayList;
import java.util.List;

public class BlockQueueManager {
    private List<BlockQueue> queues = new ArrayList<>();
    
    public void addBlockQueue(BlockQueue queue) {
        if (!queues.contains(queue)) {
            queues.add(queue);
        }
    }
    
    public long doTick() {
        if (queues.isEmpty()) return 0;
        BlockQueue queue = queues.get(0);
        if (queue.isEmpty()) {
            queue.close();
            queues.remove(0);
            return 0;
        }
        long t = System.currentTimeMillis();
        long t2 = t;
        try {
            do {
                for (int i = 0; i < 100; i++)
                    queue.doBlock();
                t2 = System.currentTimeMillis();
            } while (t2 - t < 40 && !queue.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            queues.remove(0);
            // TODO - CANCELLED DUE TO ERROR message
        }
        return t2 - t;
    }
}
