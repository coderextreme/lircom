import java.awt.*;
import java.util.*;

public class StackLayout implements LayoutManager {
	static final char X = 'X';
	static final char Y = 'Y';
	char direction = X;
	int offset = 15;
	public StackLayout (int offset, char direction) {
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
			if (direction == X) {
				xloc += offset;
			}
			if (direction == Y) {
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
			if (direction == X) {
				width += offset;
				if (d.height > height) {
					height = d.height;
				}
			}
			if (direction == Y) {
				height += offset;
				if (d.width > width) {
					width = d.width;
				}
			}
		}
		if (c != null) {
			Dimension d = c.getMinimumSize();
			if (direction == X) {
				width += d.width - offset;
			}
			if (direction == Y) {
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
			if (direction == X) {
				width += offset;
				if (d.height > height) {
					height = d.height;
				}
			}
			if (direction == Y) {
				height += offset;
				if (d.width > width) {
					width = d.width;
				}
			}
		}
		if (c != null) {
			Dimension d = c.getPreferredSize();
			if (direction == X) {
				width += d.width - offset;
			}
			if (direction == Y) {
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
			if (direction == X) {
				if (x >= xloc && x <= xloc+offset) {
					c = parent.getComponent(i);
					break;
				}
				xloc += offset;
			}
			if (direction == Y) {
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
			if (direction == X) {
				if (x >= xloc && x <= xloc+offset) {
					c = i;
					break;
				}
				xloc += offset;
			}
			if (direction == Y) {
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
}
