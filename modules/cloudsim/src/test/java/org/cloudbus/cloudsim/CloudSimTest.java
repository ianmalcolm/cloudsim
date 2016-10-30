package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.resource.Pe;
import org.cloudbus.cloudsim.resource.RAM;
import org.cloudbus.cloudsim.resource.Resource;
import org.junit.Test;

public class CloudSimTest {

	@Test
	public void newTest() {
		Pe p1 = new Pe("p1", 100, 100);
		Pe p2 = new Pe("p2", 99, 100);
		RAM a3 = new RAM("p2", 99, 100);
		Pe p4 = new Pe("p3", 99, 100);
		List<Resource> rList = new ArrayList<Resource>();
		rList.add(p1);
		rList.add(p2);
		rList.add(a3);
		rList.add(p4);
		Resource r1 = rList.get(0);
		Resource r2 = rList.get(1);
		Resource r3 = rList.get(2);
		Resource r4 = rList.get(3);
		System.out.println(r1.getClass()==r2.getClass());
		System.out.println(r2.getClass()==r3.getClass());
		System.out.println(r2.sameAs(r3));
		System.out.println(r2.sameAs(r4));
		RAM p3 = p1.sub(a3);
	}

}
