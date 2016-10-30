/*
 * Title: CloudSim Toolkit Description: CloudSim (Cloud Simulation) Toolkit for Modeling and
 * Simulation of Clouds Licence: GPL - http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.lists.PeList;
import org.cloudbus.cloudsim.resource.Resource;
import org.cloudbus.cloudsim.resource.ResourceList;

/**
 * VmScheduler is an abstract class that represents the policy used by a VMM to
 * share processing power among VMs running in a host.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public abstract class VmScheduler {

	/** The peList. */
	protected final ResourceList resList;

	/** The map of VMs to PEs. */
	protected final Map<Vm, ResourceList> resAllocation;

	// /** The MIPS that are currently allocated to the VMs. */
	// protected final List<? extends Resource> availableRes;

	/** The VMs migrating in. */
	protected final List<Vm> vmsMigratingIn;

	/** The VMs migrating out. */
	protected final List<Vm> vmsMigratingOut;

	/**
	 * Creates a new HostAllocationPolicy.
	 * 
	 * @param pelist
	 *            the pelist
	 * @pre peList != $null
	 * @post $none
	 */
	public VmScheduler(ResourceList _resList) {
		resList = _resList;
		resAllocation = new HashMap<Vm, ResourceList>();
		vmsMigratingIn = new ArrayList<Vm>();
		vmsMigratingOut = new ArrayList<Vm>();
	}

	/**
	 * Allocates resources for a VM.
	 * 
	 * @param vm
	 *            the vm
	 * @param mipsShare
	 *            the mips share
	 * @return $true if this policy allows a new VM in the host, $false
	 *         otherwise
	 * @pre $none
	 * @post $none
	 */
	public abstract boolean allocateResForVm(Vm vm, ResourceList requestedRes);

	/**
	 * Releases resources allocated to a VM.
	 * 
	 * @param vm
	 *            the vm
	 * @pre $none
	 * @post $none
	 */
	public abstract void deallocateResForVm(Vm vm);

	/**
	 * Releases releases allocated to all the VMs.
	 * 
	 * @pre $none
	 * @post $none
	 */
	public void deallocateResForAllVms() {
		for (ResourceList list : getResMap().values()) {
			list.clear();
		}
	}

	/**
	 * Gets the resources allocated for vm.
	 * 
	 * @param vm
	 *            the vm
	 * @return the pes allocated for vm
	 */
	public ResourceList getResAllocatedForVM(Vm vm) {
		return getResMap().get(vm);
	}

	public ResourceList getResList() {
		return resList;
	}

	public Map<Vm, ResourceList> getResMap() {
		return resAllocation;
	}

	/**
	 * Returns available current available capacity/speed of all Resources.
	 * 
	 * @return max mips
	 */
	public ResourceList getAvailableRes() {
		ResourceList result = resList;
		for (ResourceList rListUsed : resAllocation.values()) {
			result = result.remove(rListUsed);
		}
		return result;
	}

	/**
	 * Returns available current available capacity/speed of all Resources.
	 * 
	 * @return max mips
	 */
	public ResourceList getAvailableRes(Class c) {
		ResourceList result = resList.filter(c);
		for (ResourceList rListUsed : resAllocation.values()) {
			result = result.remove(rListUsed);
		}
		return result;
	}

	public List<Vm> getVmsMigratingIn() {
		return vmsMigratingIn;
	}

	public List<Vm> getVmsMigratingOut() {
		return vmsMigratingOut;
	}

}
