package solitaire;

import java.awt.*;

public class StackLayout implements LayoutManager2 {
	static final String X = "X";
	static final String Y = "Y";
	String direction = X;
	int offset = 15;
	public StackLayout (int offset, String direction) {
		this.offset = offset;
		this.direction = direction;
	}
	public void addLayoutComponent(String name, Component comp) {
	}
	public void layoutContainer(Container parent) {
		int xloc = 0;
		int yloc = 0;
		//for (int i = 0; i < parent.getComponentCount(); i++) {
		for (int i = parent.getComponentCount()-1; i >= 0 ; i--) {
			Component c = parent.getComponent(i);
			c.setLocation(xloc, yloc);
			c.setSize(c.getPreferredSize());
			if (direction.equals(X)) {
				xloc += offset;
			}
			if (direction.equals(Y)) {
				yloc += offset;
			}
		}
	}
	public Dimension minimumLayoutSize(Container parent) {
		int width = 0;
		int height = 0;
		Component c = null;
		// for (int i = 0; i < parent.getComponentCount(); i++) {
		for (int i = parent.getComponentCount()-1; i >= 0 ; i--) {
			c = parent.getComponent(i);
			Dimension d = c.getMinimumSize();
			if (direction.equals(X)) {
				width += offset;
				if (d.height > height) {
					height = d.height;
				}
			}
			if (direction.equals(Y)) {
				height += offset;
				if (d.width > width) {
					width = d.width;
				}
			}
		}
		if (c != null) {
			Dimension d = c.getMinimumSize();
			if (direction.equals(X)) {
				width += d.width - offset;
			}
			if (direction.equals(Y)) {
				height += d.height - offset;
			}
		}
		Dimension ret = new Dimension(width, height);
		return ret;
	}
	public Dimension preferredLayoutSize(Container parent) {
		int width = 0;
		int height = 0;
		Component c = null;
		// for (int i = 0; i < parent.getComponentCount(); i++) {
		for (int i = parent.getComponentCount()-1; i >= 0 ; i--) {
			c = parent.getComponent(i);
			Dimension d = c.getPreferredSize();
			if (direction.equals(X)) {
				width += offset;
				if (d.height > height) {
					height = d.height;
				}
			}
			if (direction.equals(Y)) {
				height += offset;
				if (d.width > width) {
					width = d.width;
				}
			}
		}
		if (c != null) {
			Dimension d = c.getPreferredSize();
			if (direction.equals(X)) {
				width += d.width - offset;
			}
			if (direction.equals(Y)) {
				height += d.height - offset;
			}
		}
		Dimension ret = new Dimension(width, height);
		return ret;
	}
	public void removeLayoutComponent(Component comp) {
	}
	public Component getComponentAt(Container parent, int x, int y) {
		int xloc = 0;
		int yloc = 0;
		Component c = null;
		for (int i = parent.getComponentCount()-1; i >= 0 ; i--) {
			if (direction.equals(X)) {
				if (x >= xloc && x <= xloc+offset) {
					c = parent.getComponent(i);
					break;
				}
				xloc += offset;
			}
			if (direction.equals(Y)) {
				if (y >= yloc && y <= yloc+offset) {
					c = parent.getComponent(i);
					break;
				}
				yloc += offset;
			}
		}
		if (c == null) {
			c = parent.getComponent(0);
		}
		return c;
	}
	public int getComponentIndex(Container parent, int x, int y) {
		int xloc = 0;
		int yloc = 0;
		int c = -1;
		for (int i = parent.getComponentCount()-1; i >= 0 ; i--) {
			if (direction.equals(X)) {
				if (x >= xloc && x <= xloc+offset) {
					c = i;
					break;
				}
				xloc += offset;
			}
			if (direction.equals(Y)) {
				if (y >= yloc && y <= yloc+offset) {
					c = i;
					break;
				}
				yloc += offset;
			}
		}
		if (c == -1) {
			c = 0;
		}
		return c;
	}
	public void	addLayoutComponent(Component comp, Object constraints) {
        }
	public float	getLayoutAlignmentX(Container target) {return 0.0f;}
	public float	getLayoutAlignmentY(Container target) {return 0.0f;}
	public void	invalidateLayout(Container target) {}
	public Dimension	maximumLayoutSize(Container target) {
		return preferredLayoutSize(target);
	}
}
