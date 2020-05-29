package com.chappie.engine.algorithms;

import java.util.LinkedList;
import java.util.List;

import com.chappie.engine.math.Vector2i;

/**
 * pathfinding algorithm
 * 
 * @author Chappie
 * @since 4/5/2020
 */
public class Pathfinder {
	
	@SuppressWarnings("unused")
	private static class Node {
		private Node parent;
		private Vector2i position;
		private float g_cost, h_cost, f_cost; 
		// g -> distance from beginning
		// h -> distance from end
		// f -> g+h
		
		public Node(Node parent, Vector2i position) {
			this.parent = parent;
			this.position = position;
		}
		
		public Node(Node parent) {
			this.parent = parent;
		}
		
		public float getGCost() { return g_cost; }
		public float getHCost() { return h_cost; }
		public float getFCost() { return f_cost; }
		public void setFCost(float f_cost) { this.f_cost = f_cost; }
		public void setGCost(float g_cost) { this.g_cost = g_cost; }
		public void setHCost(float h_cost) { this.h_cost = h_cost; }
		public Vector2i getPosition() { return position; }
		public void setPosition(Vector2i position) { this.position = position; }
		public void setParent(Node n) { this.parent = n; }
		public Node getParent() { return parent; }
	}
	
	private int plane[][];
	
	private static final int BARRIER = 1;
	private List<Node> OPEN, // to evaluate 
					   CLOSED; // evaluated; 
	private List<Vector2i> PATH; // the final path
	private boolean existsPath;
	
	private Vector2i start, target;
	
	public Pathfinder(int plane[][], Vector2i start, Vector2i target) {
		this.plane = plane;
		OPEN = new LinkedList<Node>();
		CLOSED = new LinkedList<Node>();
		PATH = new LinkedList<Vector2i>();
		OPEN.add(new Node(null, start));
		this.start = start;
		this.target = target;
		this.existsPath = true;
	}
	
	public List<Vector2i> getPath() {
		Node end = new Node(null, target);
		Node curr = null;
		while (true) {
			curr = getOptimal(OPEN);
			OPEN.remove(curr);
			CLOSED.add(curr);
			
			if (Vector2i.equals(curr.getPosition(), end.getPosition())) {
				end.setParent(curr);
				break;
			}
			
			for (int yt = -1; yt <= 1; yt++) {
				for (int xt = -1; xt <= 1; xt++) {
					Vector2i pos = new Vector2i(curr.getPosition().getX()+xt, curr.getPosition().getY()+yt);
					if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() >= plane[0].length || pos.getY() >= plane.length) continue;
					
					if (!isContained(CLOSED, pos)) {
						if (plane[pos.getX()][pos.getY()] != BARRIER) {
							if (!isContained(OPEN, pos)) {
								Node n = new Node(curr, pos);
								n.setGCost((float) Vector2i.getDistance(start, pos));
								n.setHCost((float) Vector2i.getDistance(end.getPosition(), pos));
								n.setFCost(n.getGCost()+n.getHCost());
								OPEN.add(n);
							} else {
								float tempG = (float) (Vector2i.getDistance(start, curr.getPosition())+Vector2i.getDistance(curr.getPosition(), start));
								if (OPEN.get(OPEN.indexOf(getNode(OPEN, pos))).getGCost() < tempG) OPEN.get(OPEN.indexOf(getNode(OPEN, pos))).setGCost(tempG);
							}
						}
					}
				}
			}
			if (CLOSED.size() >= plane.length*plane[0].length) // if we checked every point on the plane and there is no path
				break;
		}
		
		Node n = end.getParent();
		while (!Vector2i.equals(n.getPosition(), start)) {
			PATH.add(n.getPosition());
			n = n.getParent();
		}
		
		return PATH;
	}
	
	private static Node getOptimal(List<Node> list) {
		float min_f = Integer.MAX_VALUE;
		Node min = new Node(null, new Vector2i(0,0));
		for (Node p : list) {
			if (p.getFCost() < min_f) {
				min = p;
				min_f = p.getFCost();
			}
		}
		return min;
	}
	
	private static boolean isContained(List<Node> list, Vector2i n) {
		for (Node curr : list) if (Vector2i.equals(curr.getPosition(), n)) return true;
		return false;
	}
	
	private static Node getNode(List<Node> list, Vector2i p) {
		for (Node curr : list)
			if (Vector2i.equals(curr.getPosition(), p))
				return curr;
		return new Node(null, new Vector2i(-1));
	}
	
	public int[][] getPlane() {
		return plane;
	}

	public void setPlane(int[][] plane) {
		this.plane = plane;
	}
	
	public boolean existsPath() {
		return existsPath;
	}
	
	public static List<Vector2i> getPath(int plane[][], Vector2i start, Vector2i target) {
		Node end = new Node(null, target);
		List<Node> OPEN = new LinkedList<Node>();
		List<Node> CLOSED = new LinkedList<Node>();
		List<Vector2i> PATH = new LinkedList<Vector2i>();
		Node curr = null;
		while (true) {
			curr = getOptimal(OPEN);
			OPEN.remove(curr);
			CLOSED.add(curr);
			
			if (Vector2i.equals(curr.getPosition(), end.getPosition())) {
				end.setParent(curr);
				break;
			}
			
			for (int yt = -1; yt <= 1; yt++) {
				for (int xt = -1; xt <= 1; xt++) {
					Vector2i pos = new Vector2i(curr.getPosition().getX()+xt, curr.getPosition().getY()+yt);
					if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() >= plane[0].length || pos.getY() >= plane.length) continue;
					
					if (!isContained(CLOSED, pos)) {
						if (plane[pos.getX()][pos.getY()] != BARRIER) {
							if (!isContained(OPEN, pos)) {
								Node n = new Node(curr, pos);
								n.setGCost((float) Vector2i.getDistance(start, pos));
								n.setHCost((float) Vector2i.getDistance(end.getPosition(), pos));
								n.setFCost(n.getGCost()+n.getHCost());
								OPEN.add(n);
							} else {
								float tempG = (float) (Vector2i.getDistance(start, curr.getPosition())+Vector2i.getDistance(curr.getPosition(), start));
								if (OPEN.get(OPEN.indexOf(getNode(OPEN, pos))).getGCost() < tempG) OPEN.get(OPEN.indexOf(getNode(OPEN, pos))).setGCost(tempG);
							}
						}
					}
				}
			}
			if (CLOSED.size() >= plane.length*plane[0].length) // if we checked every point on the plane and there is no path
				break;
		}
		
		Node n = end.getParent();
		while (!Vector2i.equals(n.getPosition(), start)) {
			PATH.add(n.getPosition());
			n = n.getParent();
		}
		
		return PATH;
	}
	
	public static List<Vector2i> getPath(boolean plane[][], Vector2i start, Vector2i target) {
		Node end = new Node(null, target);
		List<Node> OPEN = new LinkedList<Node>();
		List<Node> CLOSED = new LinkedList<Node>();
		List<Vector2i> PATH = new LinkedList<Vector2i>();
		Node curr = null;
		while (true) {
			curr = getOptimal(OPEN);
			OPEN.remove(curr);
			CLOSED.add(curr);
			
			if (Vector2i.equals(curr.getPosition(), end.getPosition())) {
				end.setParent(curr);
				break;
			}
			
			for (int yt = -1; yt <= 1; yt++) {
				for (int xt = -1; xt <= 1; xt++) {
					Vector2i pos = new Vector2i(curr.getPosition().getX()+xt, curr.getPosition().getY()+yt);
					if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() >= plane[0].length || pos.getY() >= plane.length) continue;
					
					if (!isContained(CLOSED, pos)) {
						if (!plane[pos.getX()][pos.getY()]) {
							if (!isContained(OPEN, pos)) {
								Node n = new Node(curr, pos);
								n.setGCost((float) Vector2i.getDistance(start, pos));
								n.setHCost((float) Vector2i.getDistance(end.getPosition(), pos));
								n.setFCost(n.getGCost()+n.getHCost());
								OPEN.add(n);
							} else {
								float tempG = (float) (Vector2i.getDistance(start, curr.getPosition())+Vector2i.getDistance(curr.getPosition(), start));
								if (OPEN.get(OPEN.indexOf(getNode(OPEN, pos))).getGCost() < tempG) OPEN.get(OPEN.indexOf(getNode(OPEN, pos))).setGCost(tempG);
							}
						}
					}
				}
			}
			if (CLOSED.size() >= plane.length*plane[0].length) // if we checked every point on the plane and there is no path
				break;
		}
		
		Node n = end.getParent();
		while (!Vector2i.equals(n.getPosition(), start)) {
			PATH.add(n.getPosition());
			n = n.getParent();
		}
		
		return PATH;
	}
	
}
