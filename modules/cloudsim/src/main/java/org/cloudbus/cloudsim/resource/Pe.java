/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;

/**
 * CloudSim Pe (Processing Element) class represents CPU unit, defined in terms
 * of Millions Instructions Per Second (MIPS) rating.<br>
 * <b>ASSUMPTION:<b> All PEs under the same Machine have the same MIPS rating.
 * 
 * @author Manzur Murshed
 * @author Rajkumar Buyya
 * @since CloudSim Toolkit 1.0
 */
public class Pe extends Resource {

	private final Logger logger = LoggerFactory.getLogger(Pe.class);

	public Pe(String _name, double _cap, double _spd) {
		super(_name, _cap, _spd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <Pe extends Resource> Pe add(Pe op2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Pe extends Resource> Pe sub(Pe op2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Pe extends Resource> Pe copy() {
		// TODO Auto-generated method stub
		return null;
	}



}
