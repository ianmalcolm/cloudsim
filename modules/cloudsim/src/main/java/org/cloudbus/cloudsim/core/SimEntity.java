/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.core;

import java.util.ArrayList;

import org.cloudbus.cloudsim.core.predicates.Predicate;

/**
 * This class represents a simulation entity. An entity handles events and can
 * send events to other entities. When this class is extended, there are a few
 * methods that need to be implemented:
 * <ul>
 * <li>{@link #startEntity()} is invoked by the {@link Simulation} class when
 * the simulation is started. This method should be responsible for starting the
 * entity up.
 * <li>{@link #processEvent(SimEvent)} is invoked by the {@link Simulation}
 * class whenever there is an event in the deferred queue, which needs to be
 * processed by the entity.
 * <li>{@link #shutdownEntity()} is invoked by the {@link Simulation} before the
 * simulation finishes. If you want to save data in log files this is the method
 * in which the corresponding code would be placed.
 * </ul>
 * 
 * @author Marcos Dias de Assuncao
 * @since CloudSim Toolkit 1.0
 */
public abstract class SimEntity implements Cloneable {

	public static enum STATE {
		RUNNABLE, WAITING, HOLDING, FINISHED;
		boolean active = true;
	};

	/** The name. */
	protected final String name;

	/** The buffer for selected incoming events. */
	protected ArrayList<SimEvent> inbuf = new ArrayList<SimEvent>();

	/** The buffer for selected outgoing events. */
	protected ArrayList<SimEvent> outbuf = new ArrayList<SimEvent>();

	/** The entity's current state. */
	protected STATE state;

	/**
	 * Each time CloudSim runs this entity informs the current time by update
	 * this double time variable
	 */
	protected double clock;

	/**
	 * Creates a new entity.
	 * 
	 * @param name
	 *            the name to be associated with this entity
	 */
	public SimEntity(String name) {
		if (name.indexOf(" ") != -1) {
			throw new IllegalArgumentException("Entity names can't contain spaces.");
		}
		this.name = name;
		state = STATE.RUNNABLE;
	}

	/**
	 * Get the name of this entity.
	 * 
	 * @return The entity's name
	 */
	public String getName() {
		return name;
	}

	// The schedule functions

	/**
	 * Send an event to another entity by id number, with data. Note that the
	 * tag <code>9999</code> is reserved.
	 * 
	 * @param dest
	 *            The unique id number of the destination entity
	 * @param clock
	 *            the simulation time the event should be sent
	 * @param tag
	 *            An user-defined number representing the type of event.
	 * @param data
	 *            The data to be sent with the event.
	 */
	public void send(SimEntity dest, double delay, SimEvent.TAG tag, Object data) {
		SimEvent e = new SimEvent(SimEvent.TYPE.SEND, clock + delay, this, dest, tag, data);
		outbuf.add(e);
	}

	/**
	 * Send an event to another entity through a port with a given name, with
	 * <b>no</b> data. Note that the tag <code>9999</code> is reserved.
	 * 
	 * @param dest
	 *            The name of the port to send the event through
	 * @param clock
	 *            the simulation time the event should be sent
	 * @param tag
	 *            An user-defined number representing the type of event.
	 */
	public void send(SimEntity dest, double delay, SimEvent.TAG tag) {
		send(dest, delay, tag, null);
	}

	/**
	 * Set the entity to be inactive for a time period.
	 * 
	 * @param delay
	 *            the time period for which the entity will be inactive
	 */
	public void pause(double delay) {
		state.active = false;
		send(this, delay, SimEvent.TAG.SIMENTITY_ACTIVATE, null);
	}

	/**
	 * Count how many events matching a predicate are waiting in the entity's
	 * deferred queue.
	 * 
	 * @param p
	 *            The event selection predicate
	 * @return The count of matching events
	 */
	public int numEventsWaiting(Predicate p) {
		int cnt = 0;
		for (SimEvent e : inbuf) {
			if (p.match(e)) {
				cnt++;
			}
		}
		return cnt;
	}

	/**
	 * Count how many events are waiting in the entity's deferred queue.
	 * 
	 * @return The count of events
	 */
	public int numEventsWaiting() {
		return numEventsWaiting(SimEvent.ANY);
	}

	/**
	 * Extract the first event matching a predicate waiting in the entity's
	 * deferred queue.
	 * 
	 * @param p
	 *            The event selection predicate
	 * @return the simulation event
	 */
	public SimEvent selectEvent(Predicate p) {
		SimEvent event = null;
		for (SimEvent e : inbuf) {
			if (p.match(e)) {
				event = e;
			}
		}
		return event;
	}

	/**
	 * Cancel the first event matching a predicate waiting in the entity's
	 * future queue.
	 * 
	 * @param p
	 *            The event selection predicate
	 * @return The number of events cancelled (0 or 1)
	 */
	public void cancelEvent(Predicate p) {
		ArrayList<SimEvent> toRemove = new ArrayList<SimEvent>();
		for (SimEvent e : inbuf) {
			if (p.match(e)) {
				toRemove.add(e);
			}
		}
		inbuf.removeAll(toRemove);
	}

	/**
	 * Get the first event matching a predicate from the deferred queue, or if
	 * none match, wait for a matching event to arrive.
	 * 
	 * @param p
	 *            The predicate to match
	 * @return the simulation event
	 */
	public SimEvent getNextEvent(Predicate p) {
		SimEvent event = null;
		for (SimEvent e : inbuf) {
			if (p.match(e)) {
				event = e;
				break;
			}
		}
		if (event != null) {
			inbuf.remove(event);
		}
		return event;
	}

	/**
	 * Wait for an event matching a specific predicate. This method does not
	 * check the entity's deferred queue.
	 * 
	 * @param p
	 *            The predicate to match
	 */
	public void waitForEvent(Predicate p) {
		// CloudSim.wait(id, p);
		state = STATE.WAITING;
	}

	/**
	 * Get the first event waiting in the entity's deferred queue, or if there
	 * are none, wait for an event to arrive.
	 * 
	 * @return the simulation event
	 */
	public SimEvent getNextEvent() {
		return getNextEvent(SimEvent.ANY);
	}

	/**
	 * This method is invoked by the {@link Simulation} class when the
	 * simulation is started. This method should be responsible for starting the
	 * entity up.
	 */
	public abstract void startEntity();

	/**
	 * This method is invoked by the {@link Simulation} class whenever there is
	 * an event in the deferred queue, which needs to be processed by the
	 * entity.
	 * 
	 * @param ev
	 *            the event to be processed by the entity
	 */
	public abstract void processEvent(SimEvent ev);

	/**
	 * This method is invoked by the {@link Simulation} before the simulation
	 * finishes. If you want to save data in log files this is the method in
	 * which the corresponding code would be placed.
	 */
	public abstract void shutdownEntity();

	public void run(double _time) {
		this.clock = _time;

		if (state.active) {
			SimEvent ev = getNextEvent();

			while (ev != null) {
				processEvent(ev);
				if (state != STATE.RUNNABLE) {
					break;
				}
				ev = getNextEvent();
			}
		}
	}

	/**
	 * Get a clone of the entity. This is used when independent replications
	 * have been specified as an output analysis method. Clones or backups of
	 * the entities are made in the beginning of the simulation in order to
	 * reset the entities for each subsequent replication. This method should
	 * not be called by the user.
	 * 
	 * @return A clone of the entity
	 * @throws CloneNotSupportedException
	 *             the clone not supported exception
	 */
	@Override
	protected final Object clone() throws CloneNotSupportedException {
		SimEntity copy = (SimEntity) super.clone();
		copy.appendEvent(null);
		return copy;
	}

	// --------------- PACKAGE LEVEL METHODS ------------------

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	protected STATE getState() {
		return state;
	}

	/**
	 * Gets the event buffer.
	 * 
	 * @return the event buffer
	 */
	protected ArrayList<SimEvent> getEventBuffer() {
		return inbuf;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the new state
	 */
	protected void setState(STATE state) {
		this.state = state;
	}

	/**
	 * Sets the event buffer.
	 * 
	 * @param e
	 *            the new event buffer
	 */
	protected void appendEvent(SimEvent e) {
		inbuf.add(e);
	}

}
