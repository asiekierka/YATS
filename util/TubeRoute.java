package YATS.util;

import net.minecraftforge.common.ForgeDirection;
import YATS.util.LazUtils.XYZCoords;

public class TubeRoute implements Comparable<TubeRoute> {
	public XYZCoords destination;
	public ForgeDirection heading, initialdir;
	public int distance;
	public boolean completed;
	
	public TubeRoute(XYZCoords destination, ForgeDirection heading, ForgeDirection initialdir, int distance) {
		this.destination = destination;
		this.heading = heading;
		this.initialdir = initialdir;
		this.distance = distance;
		this.completed = false;
	}

	@Override
	public int compareTo(TubeRoute route) {
		return this.distance - route.distance;
	}
	
	@Override
	public int hashCode() {
		return this.destination.hashCode() * 31
				+ heading.ordinal() * 7
				+ initialdir.ordinal() * 7
				+ distance * (completed ? 1 : 0);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof TubeRoute)) return false;
		TubeRoute other = (TubeRoute)o;
		return this.hashCode() == other.hashCode();
	}
}