/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.core;

import org.cloudbus.cloudsim.core.predicates.PredicateAny;
import org.cloudbus.cloudsim.core.predicates.PredicateNone;

/**
 * This class represents a simulation event which is passed between the entities
 * in the simulation.
 * 
 * @author Costas Simatos
 * @see Simulation
 * @see SimEntity
 */
public class SimEvent implements Cloneable, Comparable<SimEvent> {

	public static enum TYPE {
		ENULL,
		SEND,
		HOLD_DONE,
		CREATE	
	}
	
	public static enum TAG {
		
		SIMENTITY_ACTIVATE,
		
		SIMENTITY_DEACTIVATE,
		
		NULL,
		
		/** Denotes the end of simulation */
		END_OF_SIMULATION,

		/**
		 * Denotes an abrupt end of simulation. That is, one event of this type
		 * is enough for {@link CloudSimShutdown} to trigger the end of the
		 * simulation
		 */
		ABRUPT_END_OF_SIMULATION,

		/**
		 * Denotes insignificant simulation entity or time. This tag will not be
		 * used for identification purposes.
		 */
		INSIGNIFICANT,

		/** Sends an Experiment object between UserEntity and Broker entity */
		EXPERIMENT,

		/**
		 * Denotes a grid resource to be registered. This tag is normally used
		 * between CloudInformationService and CloudResouce entity.
		 */
		REGISTER_RESOURCE,

		/**
		 * Denotes a grid resource, that can support advance reservation, to be
		 * registered. This tag is normally used between CloudInformationService
		 * and CloudResouce entity.
		 */
		REGISTER_RESOURCE_AR,

		/**
		 * Denotes a list of all hostList, including the ones that can support
		 * advance reservation. This tag is normally used between
		 * CloudInformationService and CloudSim entity.
		 */
		RESOURCE_LIST,

		/**
		 * Denotes a list of hostList that only support advance reservation.
		 * This tag is normally used between CloudInformationService and
		 * CloudSim entity.
		 */
		RESOURCE_AR_LIST,

		/**
		 * Denotes grid resource characteristics information. This tag is
		 * normally used between CloudSim and CloudResource entity.
		 */
		RESOURCE_CHARACTERISTICS,

		/**
		 * Denotes grid resource allocation policy. This tag is normally used
		 * between CloudSim and CloudResource entity.
		 */
		RESOURCE_DYNAMICS,

		/**
		 * Denotes a request to get the total number of Processing Elements
		 * (PEs) of a resource. This tag is normally used between CloudSim and
		 * CloudResource entity.
		 */
		RESOURCE_NUM_PE,

		/**
		 * Denotes a request to get the total number of free Processing Elements
		 * (PEs) of a resource. This tag is normally used between CloudSim and
		 * CloudResource entity.
		 */
		RESOURCE_NUM_FREE_PE,

		/**
		 * Denotes a request to record events for statistical purposes. This tag
		 * is normally used between CloudSim and CloudStatistics entity.
		 */
		RECORD_STATISTICS,

		/** Denotes a request to get a statistical list. */
		RETURN_STAT_LIST,

		/**
		 * Denotes a request to send an Accumulator object based on category
		 * into an event scheduler. This tag is normally used between
		 * ReportWriter and CloudStatistics entity.
		 */
		RETURN_ACC_STATISTICS_BY_CATEGORY,

		/**
		 * Denotes a request to register a CloudResource entity to a regional
		 * CloudInformationService (GIS) entity
		 */
		REGISTER_REGIONAL_GIS,

		/**
		 * Denotes a request to get a list of other regional GIS entities from
		 * the system GIS entity
		 */
		REQUEST_REGIONAL_GIS,

		/**
		 * Denotes request for grid resource characteristics information. This
		 * tag is normally used between CloudSim and CloudResource entity.
		 */
		RESOURCE_CHARACTERISTICS_REQUEST,

		/** This tag is used by an entity to send ping requests */
		INFOPKT_SUBMIT,

		/** This tag is used to return the ping request back to sender */
		INFOPKT_RETURN,

		/**
		 * Denotes the return of a Cloudlet back to sender. This tag is normally
		 * used by CloudResource entity.
		 */
		CLOUDLET_RETURN,

		/**
		 * Denotes the submission of a Cloudlet. This tag is normally used
		 * between CloudSim User and CloudResource entity.
		 */
		CLOUDLET_SUBMIT,

		/**
		 * Denotes the submission of a Cloudlet with an acknowledgement. This
		 * tag is normally used between CloudSim User and CloudResource entity.
		 */
		CLOUDLET_SUBMIT_ACK,

		/** Cancels a Cloudlet submitted in the CloudResource entity. */
		CLOUDLET_CANCEL,

		/** Denotes the status of a Cloudlet. */
		CLOUDLET_STATUS,

		/** Pauses a Cloudlet submitted in the CloudResource entity. */
		CLOUDLET_PAUSE,

		/**
		 * Pauses a Cloudlet submitted in the CloudResource entity with an
		 * acknowledgement.
		 */
		CLOUDLET_PAUSE_ACK,

		/** Resumes a Cloudlet submitted in the CloudResource entity. */
		CLOUDLET_RESUME,

		/**
		 * Resumes a Cloudlet submitted in the CloudResource entity with an
		 * acknowledgement.
		 */
		CLOUDLET_RESUME_ACK,

		/** Moves a Cloudlet to another CloudResource entity. */
		CLOUDLET_MOVE,

		/**
		 * Moves a Cloudlet to another CloudResource entity with an
		 * acknowledgement.
		 */
		CLOUDLET_MOVE_ACK,

		/**
		 * Denotes a request to create a new VM in a Datacentre With
		 * acknowledgement information sent by the Datacentre
		 */
		VM_CREATE,

		/**
		 * Denotes a request to create a new VM in a Datacentre With
		 * acknowledgement information sent by the Datacentre
		 */
		VM_CREATE_ACK,

		/**
		 * Denotes a request to destroy a new VM in a Datacentre
		 */
		VM_DESTROY,

		/**
		 * Denotes a request to destroy a new VM in a Datacentre
		 */
		VM_DESTROY_ACK,

		/**
		 * Denotes a request to migrate a new VM in a Datacentre
		 */
		VM_MIGRATE,

		/**
		 * Denotes a request to migrate a new VM in a Datacentre With
		 * acknowledgement information sent by the Datacentre
		 */
		VM_MIGRATE_ACK,

		/**
		 * Denotes an event to send a file from a user to a datacenter
		 */
		VM_DATA_ADD,

		/**
		 * Denotes an event to send a file from a user to a datacenter
		 */
		VM_DATA_ADD_ACK,

		/**
		 * Denotes an event to remove a file from a datacenter
		 */
		VM_DATA_DEL,

		/**
		 * Denotes an event to remove a file from a datacenter
		 */
		VM_DATA_DEL_ACK,

		/**
		 * Denotes an internal event generated in a PowerDatacenter
		 */
		VM_DATACENTER_EVENT,

		/**
		 * Denotes an internal event generated in a Broker
		 */
		VM_BROKER_EVENT,

		Network_Event_UP,

		Network_Event_send,

		RESOURCE_Register,

		Network_Event_DOWN,

		Network_Event_Host,

		NextCycle		

	};

	/** internal event type **/
	private final TYPE etype;

	/** time at which event should occur **/
	private final double time;

	/** time that the event was removed from the queue for service **/
	private double endWaitingTime;

	/** id of entity who scheduled event **/
	private SimEntity entSrc;

	/** id of entity event will be sent to **/
	private SimEntity entDst;

	/** the user defined type of the event **/
	private final TAG tag;

	/** any data the event is carrying **/
	private final Object data;

	private long serial = -1;

	/** A standard predicate that matches any event. */
	public static final PredicateAny ANY = new PredicateAny();

	/** A standard predicate that does not match any events. */
	public static final PredicateNone NONE = new PredicateNone();

	/**
	 * Create a blank event.
	 */
	public SimEvent() {
		etype = TYPE.ENULL;
		time = -1L;
		endWaitingTime = -1.0;
		entSrc = null;
		entDst = null;
		tag = TAG.NULL;
		data = null;
	}

	// ------------------- PACKAGE LEVEL METHODS --------------------------
	SimEvent(TYPE evtype, double time, SimEntity src, SimEntity dest, TAG tag, Object edata) {
		etype = evtype;
		this.time = time;
		entSrc = src;
		entDst = dest;
		this.tag = tag;
		data = edata;
	}

	SimEvent(TYPE evtype, double time, SimEntity src) {
		etype = evtype;
		this.time = time;
		entSrc = src;
		entDst = null;
		tag = TAG.NULL;
		data = null;
	}

	protected void setSerial(long serial) {
		this.serial = serial;
	}

	/**
	 * Used to set the time at which this event finished waiting in the event
	 * 
	 * @param end_waiting_time
	 */
	protected void setEndWaitingTime(double end_waiting_time) {
		endWaitingTime = end_waiting_time;
	}

	@Override
	public String toString() {
		return "Event tag = " + tag + " source = " + entSrc.getName() + " destination = " + entDst.getName();
	}

	/**
	 * The internal type
	 * 
	 * @return
	 */
	public TYPE getType() {
		return etype;
	}

	// ------------------- PUBLIC METHODS --------------------------

	/**
	 * @see Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(SimEvent event) {
		if (event == null) {
			return 1;
		} else if (time < event.time) {
			return -1;
		} else if (time > event.time) {
			return 1;
		} else if (serial < event.serial) {
			return -1;
		} else if (this == event) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Get the unique id number of the entity which received this event.
	 * 
	 * @return the id number
	 */
	public SimEntity getDestination() {
		return entDst;
	}

	/**
	 * Get the unique id number of the entity which scheduled this event.
	 * 
	 * @return the id number
	 */
	public SimEntity getSource() {
		return entSrc;
	}

	/**
	 * Get the simulation time that this event was scheduled.
	 * 
	 * @return The simulation time
	 */
	public double eventTime() {
		return time;
	}

	/**
	 * Get the simulation time that this event was removed from the queue for
	 * service.
	 * 
	 * @return The simulation time
	 */
	public double endWaitingTime() {
		return endWaitingTime;
	}

	/**
	 * Get the unique id number of the entity which scheduled this event.
	 * 
	 * @return the id number
	 */
	public SimEntity scheduledBy() {
		return entSrc;
	}

	/**
	 * Get the user-defined tag of this event.
	 * 
	 * @return The tag
	 */
	public TAG getTag() {
		return tag;
	}

	/**
	 * Get the data passed in this event.
	 * 
	 * @return A reference to the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Create an exact copy of this event.
	 * 
	 * @return The event's copy
	 */
	@Override
	public Object clone() {
		return new SimEvent(etype, time, entSrc, entDst, tag, data);
	}

	/**
	 * Set the source entity of this event.
	 * 
	 * @param s
	 *            The unique id number of the entity
	 */
	public void setSource(SimEntity s) {
		entSrc = s;
	}

	/**
	 * Set the destination entity of this event.
	 * 
	 * @param d
	 *            The unique id number of the entity
	 */
	public void setDestination(SimEntity d) {
		entDst = d;
	}
}
