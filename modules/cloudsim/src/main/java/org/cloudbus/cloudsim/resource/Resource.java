package org.cloudbus.cloudsim.resource;

public abstract class Resource implements Comparable<Resource>{

	protected final String name;
	protected double capacity = Double.MAX_VALUE;
	protected double speed = Double.MAX_VALUE;
	protected double latency = 0;

	public Resource(String _name, double _cap, double _spd) {
		name = _name;
		capacity = _cap;
		speed = _spd;
	}

	public Resource(Resource _r) {
		name = _r.name;
		capacity = _r.capacity;
		speed = _r.speed;
		latency = _r.latency;
	}

	public abstract <T extends Resource> T add(T op2);

	public abstract <T extends Resource> T sub(T op2);

	public boolean sameAs(Resource op2) {
		return getClass() == op2.getClass() && name.equals(op2.name);
	}

	public abstract <T extends Resource> T copy();
	
	public abstract boolean hasRoomFor(Resource r);
	
}
