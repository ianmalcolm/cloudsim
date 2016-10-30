/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Cloud Information Service (CIS) is an entity that provides cloud resource
 * registration, indexing and discovery services. The Cloud hostList tell their
 * readiness to process Cloudlets by registering themselves with this entity.
 * Other entities such as the resource broker can contact this class for
 * resource discovery service, which returns a list of registered resource IDs.
 * In summary, it acts like a yellow page service. This class will be created by
 * CloudSim upon initialisation of the simulation. Hence, do not need to worry
 * about creating an object of this class.
 * 
 * @author Manzur Murshed
 * @author Rajkumar Buyya
 * @since CloudSim Toolkit 1.0
 */
public class CloudInformationService extends SimEntity {

	private final Logger logger = LoggerFactory.getLogger(CloudInformationService.class);

	/** For all types of hostList. */
	private final List<SimEntity> resList;

	/** Only for AR hostList. */
	private final List<SimEntity> arList;

	/** List of all regional GIS. */
	private final List<SimEntity> gisList;

	/**
	 * Allocates a new CloudInformationService object.
	 * 
	 * @param name
	 *            the name to be associated with this entity (as required by
	 *            SimEntity class)
	 * @throws Exception
	 *             This happens when creating this entity before initialising
	 *             CloudSim package or this entity name is <tt>null</tt> or
	 *             empty
	 * @pre name != null
	 * @post $none
	 */
	public CloudInformationService(String name) throws Exception {
		super(name);
		resList = new LinkedList<SimEntity>();
		arList = new LinkedList<SimEntity>();
		gisList = new LinkedList<SimEntity>();
	}

	/**
	 * Starts the CloudInformationService entity.
	 */
	@Override
	public void startEntity() {
	}

	/**
	 * Processes events scheduled for this entity.
	 * 
	 * @param ev
	 *            the event to be handled.
	 * @see SimEntity#processEvent(SimEvent)
	 */
	@Override
	public void processEvent(SimEvent ev) {
		int id = -1; // requester id
		switch (ev.getTag()) {
		// storing regional GIS id
		case REGISTER_REGIONAL_GIS: {
			gisList.add((SimEntity) ev.getData());
			break;
		}
		// request for all regional GIS list
		case REQUEST_REGIONAL_GIS: {

			// Get reference of the entity that send this event
			SimEntity dest = (SimEntity) ev.getData();

			// Send the regional GIS list back to sender
			send(dest, 0L, ev.getTag(), gisList);
			break;
		}

		// A resource is requesting to register.
		case REGISTER_RESOURCE: {
			resList.add((SimEntity) ev.getData());
			break;
		}
		// A resource that can support Advance Reservation
		case REGISTER_RESOURCE_AR: {
			resList.add((SimEntity) ev.getData());
			arList.add((SimEntity) ev.getData());
			break;
		}
		// A Broker is requesting for a list of all hostList.
		case RESOURCE_LIST: {

			// Get reference of the entity that send this event
			SimEntity dest = (SimEntity) ev.getData();

			// Send the resource list back to the sender
			send(dest, 0L, ev.getTag(), resList);
			break;
		}
		// A Broker is requesting for a list of all hostList.
		case RESOURCE_AR_LIST: {

			// Get reference of the entity that send this event
			SimEntity dest = (SimEntity) ev.getData();

			// Send the resource AR list back to the sender
			send(dest, 0L, ev.getTag(), arList);
			break;
		}
		default:
			processOtherEvent(ev);
			break;
		}
	}

	/**
	 * Shutdowns the CloudInformationService.
	 */
	@Override
	public void shutdownEntity() {
		notifyAllEntity();
	}

	/**
	 * Gets the list of all CloudResource IDs, including hostList that support
	 * Advance Reservation.
	 * 
	 * @return LinkedList containing resource IDs. Each ID is represented by an
	 *         Integer object.
	 * @pre $none
	 * @post $none
	 */
	public List<SimEntity> getList() {
		return resList;
	}

	/**
	 * Gets the list of CloudResource IDs that <b>only</b> support Advanced
	 * Reservation.
	 * 
	 * @return LinkedList containing resource IDs. Each ID is represented by an
	 *         Integer object.
	 * @pre $none
	 * @post $none
	 */
	public List<SimEntity> getAdvReservList() {
		return arList;
	}

	/**
	 * Checks whether a given resource ID supports Advanced Reservations or not.
	 * 
	 * @param id
	 *            a resource ID
	 * @return <tt>true</tt> if this resource supports Advanced Reservations,
	 *         <tt>false</tt> otherwise
	 * @pre id != null
	 * @post $none
	 */
	public boolean resourceSupportAR(Integer id) {
		if (id == null) {
			return false;
		}

		return resourceSupportAR(id.intValue());
	}

	/**
	 * Checks whether a given resource ID supports Advanced Reservations or not.
	 * 
	 * @param id
	 *            a resource ID
	 * @return <tt>true</tt> if this resource supports Advanced Reservations,
	 *         <tt>false</tt> otherwise
	 * @pre id >= 0
	 * @post $none
	 */
	public boolean resourceSupportAR(SimEntity id) {
		boolean flag = false;
		if (id == null) {
			flag = false;
		} else {
			flag = checkResource(arList, id);
		}

		return flag;
	}

	/**
	 * Checks whether the given CloudResource ID exists or not.
	 * 
	 * @param id
	 *            a CloudResource id
	 * @return <tt>true</tt> if the given ID exists, <tt>false</tt> otherwise
	 * @pre id >= 0
	 * @post $none
	 */
	public boolean resourceExist(SimEntity id) {
		boolean flag = false;
		if (id == null) {
			flag = false;
		} else {
			flag = checkResource(resList, id);
		}

		return flag;
	}

	/**
	 * Checks whether the given CloudResource ID exists or not.
	 * 
	 * @param id
	 *            a CloudResource id
	 * @return <tt>true</tt> if the given ID exists, <tt>false</tt> otherwise
	 * @pre id != null
	 * @post $none
	 */
	public boolean resourceExist(Integer id) {
		if (id == null) {
			return false;
		}
		return resourceExist(id.intValue());
	}

	// //////////////////////// PROTECTED METHODS ////////////////////////////

	/**
	 * This method needs to override by a child class for processing other
	 * events. These events are based on tags that are not mentioned in
	 * {@link #body()} method.
	 * 
	 * @param ev
	 *            a Sim_event object
	 * @pre ev != null
	 * @post $none
	 */
	protected void processOtherEvent(SimEvent ev) {
		if (ev == null) {
			logger.warn("CloudInformationService.processOtherEvent(): "
					+ "Unable to handle a request since the event is null.");
			return;
		}

		logger.warn("CloudInformationSevice.processOtherEvent(): " + "Unable to handle a request from "
				+ ev.getSource().getName() + " with event tag = " + ev.getTag());
	}

	/**
	 * Notifies the registered entities about the end of simulation. This method
	 * should be overridden by the child class
	 */
	protected void processEndSimulation() {
		// this should be overridden by the child class
	}

	// ////////////////// End of PROTECTED METHODS ///////////////////////////

	/**
	 * Checks for a list for a particular resource id.
	 * 
	 * @param list
	 *            list of hostList
	 * @param id
	 *            a resource ID
	 * @return true if a resource is in the list, otherwise false
	 * @pre list != null
	 * @pre id > 0
	 * @post $none
	 */
	private boolean checkResource(Collection<SimEntity> list, SimEntity id) {
		boolean flag = false;
		if (list.isEmpty() || id == null) {
			return flag;
		}

		SimEntity obj = null;
		Iterator<SimEntity> it = list.iterator();

		// a loop to find the match the resource id in a list
		while (it.hasNext()) {
			obj = it.next();
			if (obj == id) {
				flag = true;
				break;
			}
		}

		return flag;
	}

	/**
	 * Tells all registered entities the end of simulation.
	 * 
	 * @pre $none
	 * @post $none
	 */
	private void notifyAllEntity() {
		logger.info(super.getName() + ": Notify all CloudSim entities for shutting down.");

		signalShutdown(resList);
		signalShutdown(gisList);

		// reset the values
		resList.clear();
		gisList.clear();
	}

	/**
	 * Sends a signal to all entity IDs mentioned in the given list.
	 * 
	 * @param list
	 *            List storing entity IDs
	 * @pre list != null
	 * @post $none
	 */
	protected void signalShutdown(Collection<SimEntity> list) {
		// checks whether a list is empty or not
		if (list == null) {
			return;
		}

		Iterator<SimEntity> it = list.iterator();
		SimEntity obj = null;

		// Send END_OF_SIMULATION event to all entities in the list
		while (it.hasNext()) {
			obj = it.next();
			send(obj, 0L, SimTags.END_OF_SIMULATION);
		}
	}

}
