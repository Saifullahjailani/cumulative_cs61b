package byow.Game;

import byow.MapEngin.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Author@ Saifullah Jaialni
 */
public class Dijkstra {
    private final Node[][] nodes;
    private final Position start;
    private final Position end;
    private PriorityQueue<Node> fringe;
    private int height;
    private int width;
    private TETile[][] tiles;

    public Dijkstra(TETile[][] tiles, Position start, Position end, int width, int height) {
        fringe = new PriorityQueue<>();
        nodes = new Node[width][height];
        this.start = new Position(start);
        this.end = new Position(end);
        this.tiles = tiles;
        this.height = height;
        this.width = width;
        init();
        process();
    }

    private void init() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position p = new Position(i, j);
                Node n = new Node(p);
                n.isBlocked = tiles[i][j] != Tileset.FLOOR
                        && tiles[i][j] != Tileset.ENEMY
                        && tiles[i][j] != Tileset.AVATAR;
                nodes[i][j] = n;
                fringe.add(n);
            }
        }
        Node n = new Node(start);
        n.distTo = 0;
        n.visited = true;
        relax(nodes[start.getX()][start.getY()], n);

    }

    private void changePriority(Node from, Node to) {
        fringe.remove(from);
        fringe.add(to);
    }

    private void changeNodes(Node from, Node to) {
        nodes[from.p.getX()][from.p.getY()] = to;
    }

    private void relax(Node from, Node to) {
        changePriority(from, to);
        changeNodes(from, to);
    }

    private void process() {
        Node ptr = fringe.poll();
        while (!ptr.p.equals(end)) {
            if (ptr == null) {
                return;
            }
            Collection<Node> neighbors = neighbors(ptr.p);
            for (Node neighbor : neighbors) {
                if (!neighbor.isBlocked) {
                    if (!neighbor.visited) {
                        Node n = new Node(neighbor);
                        n.distTo = 1 + ptr.distTo;
                        n.edgeTo = ptr;
                        relax(neighbor, n);
                    } else if (neighbor.visited && (ptr.distTo + 1) < neighbor.distTo) {
                        Node n = new Node(neighbor);
                        n.distTo = 1 + ptr.distTo;
                        n.edgeTo = ptr;
                        relax(neighbor, n);
                    }
                }
            }
            ptr = fringe.poll();
        }


    }

    private Node getNode(Position p) {
        return nodes[p.getX()][p.getY()];
    }

    public LinkedList<Position> optimalPath() {
        LinkedList<Position> p = new LinkedList<>();
        Node ptr = getNode(end);
        p.addFirst(ptr.p);
        if (ptr == null) {
            return null;
        }
        while (!ptr.p.equals(start)) {

            p.addFirst(ptr.p);
            ptr = ptr.edgeTo;
        }
        p.addFirst(start);
        return p;
    }


    private Collection<Node> neighbors(Position p) {
        Collection<Node> n = new LinkedList<>();

        if (p.getX() >= 0 && p.getX() < width - 1) {
            n.add(nodes[p.getX() + 1][p.getY()]);
        }

        if (p.getX() > 0 && p.getX() < width) {
            n.add(nodes[p.getX() - 1][p.getY()]);
        }

        if (p.getY() >= 0 && p.getY() < height - 1) {
            n.add(nodes[p.getX()][p.getY() + 1]);
        }

        if (p.getY() < height && p.getY() > 0) {
            n.add(nodes[p.getX()][p.getY() - 1]);
        }

        return n;
    }

    public void printNodes() {
        System.out.println();
        for (int i = width - 1; i >= 0; i--) {
            for (int j = 0; j < height; j++) {
                System.out.print(nodes[i][j].distTo + " ");
            }
            System.out.println();
        }
    }

    public void printedgeTo(Position p) {
        p = nodes[p.getX()][p.getY()].edgeTo.p;
        System.out.println(p.getX() + " " + p.getY());
    }

    private static class Node implements Comparable<Node> {
        private Position p;
        private int distTo;
        private Node edgeTo;
        private boolean visited;
        private boolean isBlocked;

         Node(Position p) {
            this.p = new Position(p);
            distTo = Integer.MAX_VALUE;
            edgeTo = null;
            isBlocked = false;
            visited = false;
        }

        Node(Node n) {
            this.p = n.p;
            this.edgeTo = n.edgeTo;
            this.distTo = n.distTo;
            this.isBlocked = n.isBlocked;
            this.visited = !n.visited;
        }

        public int compareTo(Node n) {
            return distTo - n.distTo;
        }

    }

    private class comp implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            return n1.compareTo(n2);
        }
    }

}
