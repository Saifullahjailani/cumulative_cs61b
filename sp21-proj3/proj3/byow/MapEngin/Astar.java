package byow.MapEngin;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Author@ Saifullah Jaialni
 */
public class Astar implements Serializable {
    private final Node[][] nodes;
    private final Position start;
    private final Position end;
    PriorityQueue<Node> fringe;
    int height;
    int width;
    TETile[][] tiles;
    private final boolean wallCheck = false;

    public Astar(TETile[][] tiles, Position start, Position end, int width, int height) {
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

    public Astar(TETile[][] tiles, Position start, Position end, int width, int height, boolean wallCheck) {
        wallCheck = true;
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

    public static void main(String[] args) {

    }

    private void init() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position p = new Position(i, j);
                Node n = new Node(p);
                if (wallCheck) {
                    n.isBlocked = (tiles[i][j] != Tileset.FLOOR);
                }
                nodes[i][j] = n;
                fringe.add(n);
            }
        }
        Node n = new Node(start);
        n.distTo = 0;
        n.visited = true;
        n.priority = (int) Math.round(start.euclidian(end));
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

    public void process() {
        Node ptr = fringe.poll();
        if (ptr == null) {
            return;
        }
        while (!ptr.p.equals(end)) {
            Collection<Node> neighbors = neighbors(ptr.p);
            for (Node neighbor : neighbors) {
                if (!neighbor.isBlocked) {
                    if (!neighbor.visited) {
                        Node n = new Node(neighbor);
                        n.distTo = 1 + ptr.distTo;
                        n.edgeTo = ptr;
                        n.priority = n.distTo + (int) Math.round(n.p.euclidian(end));
                        relax(neighbor, n);
                    } else {
                        if ((ptr.priority) < neighbor.priority) {
                            Node n = new Node(neighbor);
                            n.distTo = 1 + ptr.distTo;
                            n.edgeTo = ptr;
                            n.priority = n.distTo + (int) Math.round(n.p.euclidian(end));
                            relax(neighbor, n);
                        }
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
        while (!ptr.p.equals(start)) {
            if (ptr == null) {
                return null;
            }
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
        public Position p;
        public int distTo;
        public Node edgeTo;
        public boolean visited;
        public boolean isBlocked;
        public int priority;

        public Node(Position p) {
            this.p = new Position(p);
            distTo = Integer.MAX_VALUE;
            edgeTo = null;
            isBlocked = false;
            visited = false;
            priority = Integer.MAX_VALUE;
        }

        public Node(Node n) {
            this.p = n.p;
            this.edgeTo = n.edgeTo;
            this.distTo = n.distTo;
            this.isBlocked = n.isBlocked;
            this.visited = !n.visited;
            this.priority = n.priority;
        }

        public int compareTo(Node n) {
            return (priority - n.priority);
        }

    }
}
