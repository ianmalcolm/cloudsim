/*
 * Title: CloudSim Toolkit Description: CloudSim (Cloud Simulation) Toolkit for Modeling and
 * Simulation of Clouds Licence: GPL - http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.PeList;
import org.cloudbus.cloudsim.resource.Pe;
import org.cloudbus.cloudsim.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Host executes actions related to management of virtual machines (e.g.,
 * creation and destruction). A host has a defined policy for provisioning
 * memory and bw, as well as an allocation policy for Pe's to virtual machines.
 * A host is associated to a datacenter. It can host virtual machines.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class Host extends SimEntity {

	private final Logger logger = LoggerFactory.getLogger(Host.class);

	/** The allocation policy. */
	private VmScheduler vmScheduler;

	/** The vm list. */
	private final List<Vm> vmList = new ArrayList<Vm>();

	/** The resource list. */
	private final List<Resource> resList = new ArrayList<Resource>();

	/** state tells whether this machine is working properly or has failed. */

	/** The vms migrating in. */
	private final List<Vm> vmsMigratingIn = new ArrayList<Vm>();

	/** The VMs migrating out. */
	private final List<Vm> vmsMigratingOut = new ArrayList<Vm>();

	/** The datacenter where the host is placed. */
	private Datacenter datacenter;

	/**
	 * Instantiates a new host.
	 * 
	 */
	public Host(String _name, List<? extends Resource> _resList, VmScheduler _vmScheduler) {
		super(_name);
		resList.addAll(_resList);
		vmScheduler = _vmScheduler;
	}

	/**
	 * Requests updating of processing of cloudlets in the VMs running in this
	 * host.
	 * 
	 * @param currentTime
	 *            the current time
	 * @return expected time of completion of the next cloudlet in all VMs in
	 *         this host. Double.MAX_VALUE if there is no future events expected
	 *         in this host
	 * @pre currentTime >= 0.0
	 * @post $none
	 */
	public double updateVmsProcessing(double currentTime) {
		double smallerTime = Double.MAX_VALUE;

		for (Vm vm : getVmList()) {
			double time = vm.updateVmProcessing(currentTime, getVmScheduler().getAllocatedResForVm(vm));
			if (time > 0.0 && time < smallerTime) {
				smallerTime = time;
			}
		}

		return smallerTime;
	}

	/**
	 * Returns a VM object.
	 * 
	 * @param vmId
	 *            the vm id
	 * @param userId
	 *            ID of VM's owner
	 * @return the virtual machine object, $null if not found
	 * @pre $none
	 * @post $none
	 */
	public Vm getVm(int vmId, int userId) {
		for (Vm vm : getVmList()) {
			if (vm.getId() == vmId && vm.getUserId() == userId) {
				return vm;
			}
		}
		return null;
	}

	/**
	 * Gets the pes number.
	 * 
	 * @return the pes number
	 */
	public int getNumberOfPes() {
		return getPeList().size();
	}

	/**
	 * Gets the free pes number.
	 * 
	 * @return the free pes number
	 */
	public int getNumberOfFreePes() {
		return PeList.getNumberOfFreePes(getPeList());
	}

	/**
	 * Gets the total mips.
	 * 
	 * @return the total mips
	 */
	public int getTotalMips() {
		return PeList.getTotalMips(getPeList());
	}

	/**
	 * Allocates PEs for a VM.
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
	public boolean allocateResForVm(Vm vm, List<? extends Resource> resShare) {
		return getVmScheduler().allocateResForVm(vm, resShare);
	}

	/**
	 * Releases PEs allocated to a VM.
	 * 
	 * @param vm
	 *            the vm
	 * @pre $none
	 * @post $none
	 */
	public void deallocateResForVm(Vm vm) {
		getVmScheduler().deallocateResForVm(vm);
	}

	/**
	 * Returns the MIPS share of each Pe that is allocated to a given VM.
	 * 
	 * @param vm
	 *            the vm
	 * @return an array containing the amount of MIPS of each pe that is
	 *         available to the VM
	 * @pre $none
	 * @post $none
	 */
	public List<Double> getAllocatedMipsForVm(Vm vm) {
		return getVmScheduler().getAllocatedResForVm(vm);
	}

	/**
	 * Gets the total allocated MIPS for a VM over all the PEs.
	 * 
	 * @param vm
	 *            the vm
	 * @return the allocated mips for vm
	 */
	public double getTotalAllocatedMipsForVm(Vm vm) {
		return getVmScheduler().getTotalAllocatedResForVm(vm);
	}

	/**
	 * Returns maximum available MIPS among all the PEs.
	 * 
	 * @return max mips
	 */
	public double getMaxAvailableMips() {
		return getVmScheduler().getMaxAvailableRes();
	}

	/**
	 * Gets the free mips.
	 * 
	 * @return the free mips
	 */
	public List<? extends Resource> getAvailableRes() {
		return getVmScheduler().getAvailableRes();
	}

	/**
	 * Gets the machine storage.
	 * 
	 * @return the machine storage
	 * @pre $none
	 * @post $result >= 0
	 */

	/**
	 * Gets the VM scheduler.
	 * 
	 * @return the VM scheduler
	 */
	public VmScheduler getVmScheduler() {
		return vmScheduler;
	}

	/**
	 * Sets the VM scheduler.
	 * 
	 * @param vmScheduler
	 *            the vm scheduler
	 */
	protected void setVmScheduler(VmScheduler vmScheduler) {
		this.vmScheduler = vmScheduler;
	}

	/**
	 * Gets the pe list.
	 * 
	 * @param <T>
	 *            the generic type
	 * @return the pe list
	 */
	@SuppressWarnings("unchecked")
	public <T extends Pe> List<T> getPeList() {
		return (List<T>) resList;
	}

	/**
	 * Sets the pe list.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param peList
	 *            the new pe list
	 */
	protected <T extends Pe> void setPeList(List<T> peList) {
		this.resList = peList;
	}

	/**
	 * Gets the vm list.
	 * 
	 * @param <T>
	 *            the generic type
	 * @return the vm list
	 */
	@SuppressWarnings("unchecked")
	public <T extends Vm> List<T> getVmList() {
		return (List<T>) vmList;
	}

	/**
	 * Sets the particular Pe status on this Machine.
	 * 
	 * @param peId
	 *            the pe id
	 * @param status
	 *            Pe status, either <tt>Pe.FREE</tt> or <tt>Pe.BUSY</tt>
	 * @return <tt>true</tt> if the Pe status has changed, <tt>false</tt>
	 *         otherwise (Pe id might not be exist)
	 * @pre peID >= 0
	 * @post $none
	 */
	public boolean setPeStatus(int peId, int status) {
		return PeList.setPeStatus(getPeList(), peId, status);
	}

	/**
	 * Gets the vms migrating in.
	 * 
	 * @return the vms migrating in
	 */
	public List<Vm> getVmsMigratingIn() {
		return vmsMigratingIn;
	}

	/**
	 * Gets the data center.
	 * 
	 * @return the data center where the host runs
	 */
	public Datacenter getDatacenter() {
		return datacenter;
	}

	/**
	 * Sets the data center.
	 * 
	 * @param datacenter
	 *            the data center from this host
	 */
	public void setDatacenter(Datacenter datacenter) {
		this.datacenter = datacenter;
	}

	@Override
	public void startEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processEvent(SimEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub

	}

	public List<Resource> getResList() {
		return resList;
	}

}
