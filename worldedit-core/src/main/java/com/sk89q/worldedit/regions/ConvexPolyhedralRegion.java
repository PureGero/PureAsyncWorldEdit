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

import static com.google.common.base.Preconditions.checkNotNull;

import com.sk89q.worldedit.math.BlockVector3d;
import com.sk89q.worldedit.math.Vector3d;
import com.sk89q.worldedit.regions.polyhedron.Edge;
import com.sk89q.worldedit.regions.polyhedron.Triangle;
import com.sk89q.worldedit.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

public class ConvexPolyhedralRegion extends AbstractRegion {

    /**
     * Vertices that are contained in the convex hull.
     */
    private final Set<BlockVector3d> vertices = new LinkedHashSet<>();

    /**
     * Triangles that form the convex hull.
     */
    private final List<Triangle> triangles = new ArrayList<>();

    /**
     * Vertices that are coplanar to the first 3 vertices.
     */
    private final Set<BlockVector3d> vertexBacklog = new LinkedHashSet<>();

    /**
     * Minimum point of the axis-aligned bounding box.
     */
    private BlockVector3d minimumPoint;

    /**
     * Maximum point of the axis-aligned bounding box.
     */
    private BlockVector3d maximumPoint;

    /**
     * Accumulator for the barycenter of the polyhedron. Divide by vertices.size() to get the actual center.
     */
    private BlockVector3d centerAccum = BlockVector3d.ZERO;

    /**
     * The last triangle that caused a {@link #contains(Vector3d)} to classify a point as "outside". Used for optimization.
     */
    private Triangle lastTriangle;

    /**
     * Constructs an empty mesh, containing no vertices or triangles.
     *
     * @param world the world
     */
    public ConvexPolyhedralRegion(@Nullable World world) {
        super(world);
    }

    /**
     * Constructs an independent copy of the given region.
     *
     * @param region the region to copy
     */
    public ConvexPolyhedralRegion(ConvexPolyhedralRegion region) {
        this(region.world);
        vertices.addAll(region.vertices);
        triangles.addAll(region.triangles);
        vertexBacklog.addAll(region.vertexBacklog);

        minimumPoint = region.minimumPoint;
        maximumPoint = region.maximumPoint;
        centerAccum = region.centerAccum;
        lastTriangle = region.lastTriangle;
    }

    /**
     * Clears the region, removing all vertices and triangles.
     */
    public void clear() {
        vertices.clear();
        triangles.clear();
        vertexBacklog.clear();

        minimumPoint = null;
        maximumPoint = null;
        centerAccum = BlockVector3d.ZERO;
        lastTriangle = null;
    }

    /**
     * Add a vertex to the region.
     *
     * @param vertex the vertex
     * @return true, if something changed.
     */
    public boolean addVertex(BlockVector3d vertex) {
        checkNotNull(vertex);

        lastTriangle = null; // Probably not necessary

        if (vertices.contains(vertex)) {
            return false;
        }

        if (vertices.size() == 3) {
            if (vertexBacklog.contains(vertex)) {
                return false;
            }

            if (containsRaw(vertex.toVector3d())) {
                return vertexBacklog.add(vertex);
            }
        }

        vertices.add(vertex);

        centerAccum = centerAccum.add(vertex);

        if (minimumPoint == null) {
            minimumPoint = maximumPoint = vertex;
        } else {
            minimumPoint = minimumPoint.getMinimum(vertex);
            maximumPoint = maximumPoint.getMaximum(vertex);
        }


        switch (vertices.size()) {
        case 0:
        case 1:
        case 2:
            // Incomplete, can't make a mesh yet
            return true;

        case 3:
            // Generate minimal mesh to start from
            final BlockVector3d[] v = vertices.toArray(new BlockVector3d[vertices.size()]);

            triangles.add((new Triangle(v[0].toVector3d(), v[1].toVector3d(), v[2].toVector3d())));
            triangles.add((new Triangle(v[0].toVector3d(), v[2].toVector3d(), v[1].toVector3d())));
            return true;
        }

        // Look for triangles that face the vertex and remove them
        final Set<Edge> borderEdges = new LinkedHashSet<>();
        for (Iterator<Triangle> it = triangles.iterator(); it.hasNext(); ) {
            final Triangle triangle = it.next();

            // If the triangle can't be seen, it's not relevant
            if (!triangle.above(vertex.toVector3d())) {
                continue;
            }

            // Remove the triangle from the mesh
            it.remove();

            // ...and remember its edges
            for (int i = 0; i < 3; ++i) {
                final Edge edge = triangle.getEdge(i);
                if (borderEdges.remove(edge)) {
                    continue;
                }

                borderEdges.add(edge);
            }
        }

        // Add triangles between the remembered edges and the new vertex.
        for (Edge edge : borderEdges) {
            triangles.add(edge.createTriangle(vertex.toVector3d()));
        }

        if (!vertexBacklog.isEmpty()) {
            // Remove the new vertex 
            vertices.remove(vertex);

            // Clone, clear and work through the backlog
            final List<BlockVector3d> vertexBacklog2 = new ArrayList<>(vertexBacklog);
            vertexBacklog.clear();
            for (BlockVector3d vertex2 : vertexBacklog2) {
                addVertex(vertex2);
            }

            // Re-add the new vertex after the backlog.
            vertices.add(vertex);
        }

        return true;
    }

    public boolean isDefined() {
        return !triangles.isEmpty();
    }

    @Override
    public BlockVector3d getMinimumPoint() {
        return minimumPoint;
    }

    @Override
    public BlockVector3d getMaximumPoint() {
        return maximumPoint;
    }
    
    @Override
    public Vector3d getCenter() {
        return centerAccum.toVector3d().divide(vertices.size());
    }

    @Override
    public void expand(BlockVector3d... changes) throws RegionOperationException {
    }

    @Override
    public void contract(BlockVector3d... changes) throws RegionOperationException {
    }

    @Override
    public void shift(BlockVector3d change) throws RegionOperationException {
        Vector3d vec = change.toVector3d();
        shiftCollection(vertices, change);
        shiftCollection(vertexBacklog, change);

        for (int i = 0; i < triangles.size(); ++i) {
            final Triangle triangle = triangles.get(i);

            final Vector3d v0 = vec.add(triangle.getVertex(0));
            final Vector3d v1 = vec.add(triangle.getVertex(1));
            final Vector3d v2 = vec.add(triangle.getVertex(2));

            triangles.set(i, new Triangle(v0, v1, v2));
        }

        minimumPoint = change.add(minimumPoint);
        maximumPoint = change.add(maximumPoint);
        centerAccum = change.multiply(vertices.size()).add(centerAccum);
        lastTriangle = null;
    }

    private static void shiftCollection(Collection<BlockVector3d> collection, BlockVector3d change) {
        final List<BlockVector3d> tmp = new ArrayList<>(collection);
        collection.clear();
        for (BlockVector3d vertex : tmp) {
            collection.add(change.add(vertex));
        }
    }

    @Override
    public boolean contains(BlockVector3d position) {
        if (!isDefined()) {
            return false;
        }

        final int x = position.getBlockX();
        final int y = position.getBlockY();
        final int z = position.getBlockZ();

        final BlockVector3d min = getMinimumPoint();
        final BlockVector3d max = getMaximumPoint();

        if (x < min.getBlockX()) return false;
        if (x > max.getBlockX()) return false;
        if (y < min.getBlockY()) return false;
        if (y > max.getBlockY()) return false;
        if (z < min.getBlockZ()) return false;
        if (z > max.getBlockZ()) return false;

        return containsRaw(position.toVector3d());
    }

    private boolean containsRaw(Vector3d pt) {
        if (lastTriangle != null && lastTriangle.above(pt)) {
            return false;
        }

        for (Triangle triangle : triangles) {
            if (lastTriangle == triangle) {
                continue;
            }

            if (triangle.above(pt)) {
                lastTriangle = triangle;
                return false;
            }
        }

        return true;
    }

    public Collection<BlockVector3d> getVertices() {
        if (vertexBacklog.isEmpty()) {
            return vertices;
        }

        final List<BlockVector3d> ret = new ArrayList<>(vertices);
        ret.addAll(vertexBacklog);

        return ret;
    }

    public Collection<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public AbstractRegion clone() {
        return new ConvexPolyhedralRegion(this);
    }
}
