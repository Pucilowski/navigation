package com.pucilowski.navigation.maze.model.grid;

import com.pucilowski.navigation.maze.model.Cell;
import com.pucilowski.navigation.maze.model.Neighborship;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by martin on 10/12/13.
 */
public abstract class Grid {

    public final int width;
    public final int height;
    public final int sides;
    public Cell[][] cells;

    public BooleanGrid booleanGrid;//= new BooleanGrid(this);
    public int maxDepth = 1;


    public Grid(int width, int height, int sides) {
        this.width = width;
        this.height = height;
        this.sides = sides;
        cells = new Cell[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[x][y] = new Cell(x, y, sides);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[x][y];

                for (int index = 0; index < sides; index++) {
                    Cell neighbor = getNeighbor(cell, index);
                    if (neighbor == null) continue;

                    cell.neighborships[index] = new Neighborship(cell, neighbor, index);
                }
            }
        }

    }

    public boolean contains(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public void gen() {
        //booleanGrid = new BooleanGrid(this);
    }

    public void connect(Neighborship n) {

        int source = (int) Math.pow(2, n.index);
        int opp = getOppositeIndex(n.source, n.index);
        int target = (int) Math.pow(2, opp);

        n.source.walls |= source;
        n.target.walls |= target;
    }

    public boolean isConnected(Neighborship n) {
        int source = n.getBit();
        int opp = getOppositeIndex(n.source, n.index);
        int target = (int) Math.pow(2, opp);

        return (n.source.walls & source) == source && (n.target.walls & target) == target;

    }


    public abstract Point getOffset(Cell cell, int index);

    public int getOppositeIndex(Cell cell, int index) {

        return (index + (sides / 2)) % sides;

        //return cell.x % 2 == cell.y % 2 ? identical[index] : distinct[index];
    }

    public final Cell getNeighbor(Cell cell, int index) {
        Point o = getOffset(cell, index);

        int x = cell.x + o.x;
        int y = cell.y + o.y;
        if (!contains(x, y)) return null;

        return cells[x][y];
    }

    public final Cell[] getNeighbors(Cell cell) {
        ArrayList<Cell> adjacent = new ArrayList<Cell>();

        for (int i = 0; i < sides; i++) {
            Cell n = getNeighbor(cell, i);
            if (n == null) continue;
            adjacent.add(n);
        }

        return adjacent.toArray(new Cell[adjacent.size()]);
    }

    // paint

    public abstract Point getLocation(Cell cell, int size);

    public abstract Point getPoint(Cell cell, int index, int size);

    public abstract Polygon getSide(Cell cell, int index, int size);

    public abstract Polygon getPolygon(Cell cell, int size);
}
