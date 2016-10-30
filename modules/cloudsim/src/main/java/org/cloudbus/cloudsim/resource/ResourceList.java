package org.cloudbus.cloudsim.resource;

import java.util.HashMap;

public class ResourceList extends HashMap<String, Resource> implements Comparable<ResourceList> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8568315408871355756L;

	private ResourceList() {
	}

	public ResourceList(Resource res) {
		put(res.name, res);
	}

	public ResourceList remove(ResourceList resList) {
		ResourceList result = copy();
		for (Resource res : resList.values()) {
			remove(res);
		}
		return result;
	}

	public ResourceList add(ResourceList resList) {
		ResourceList result = this.copy();
		for (Resource r : resList.values()) {
			result.add(r);
		}
		return result;
	}

	public ResourceList copy() {
		ResourceList result = new ResourceList();
		for (Resource r : values()) {
			result.add(r.copy());
		}
		return result;
	}

	public ResourceList filter(Class c) {
		ResourceList result = new ResourceList();
		for (Resource r : values()) {
			if (r.getClass() == c) {
				result.put(r.name, r);
			}
		}
		return result;
	}

	private void remove(Resource res) {
		if (containsKey(res.name)) {
			Resource thisRes = get(res.name);
			assert thisRes.sameAs(res);
			Resource rLeft = thisRes.sub(res);
			put(rLeft.name, rLeft);
		}
	}

	private void add(Resource res) {
		if (containsKey(res)) {
			Resource rNew = res.add(get(res.name));
			put(rNew.name, rNew);
		} else {
			put(res.name, res);
		}
	}

	@Override
	public int compareTo(ResourceList arg0) {
		int result = 1;
		ResourceList resList1 = aggregateByClass();
		ResourceList resList2 = arg0.aggregateByClass();
		for (String resName : resList2.keySet()) {
			if (!resList1.containsKey(resName)) {
				result = -1;
				break;
			} else {
				Resource r1 = resList1.get(resName);
				Resource r2 = resList2.get(resName);
				if (r1.compareTo(r2) == -1) {
					result = -1;
					break;
				} else if (r1.compareTo(r2) == 0) {
					result = 0;
				}
			}
		}

		return result;
	}

	private ResourceList aggregateByClass() {
		ResourceList result = new ResourceList();
		for (Resource r : values()) {
			result.put(r.getClass().getName(), r);
		}
		return result;
	}
}
