/*
 * Hongyun Du
 * HXD171530
 * CS 3345
 * PROJECT 6
 */
public class PriorityQueues {
	static final boolean COST = true;
	static final boolean TIME = false;
	static final int DefaultSize=15;
	Vert costHeap[] = new Vert[1];
	Vert timeHeap[] = new Vert[1];
	int currentSize = 0;

	public void add(Vert obj) {
		if (costHeap.length > (currentSize + 1)) {
			costHeap[++currentSize] = obj;
			timeHeap[currentSize] = obj;
			AfterInsertion();
		} else {
			Enlarge(costHeap);
			Enlarge(timeHeap);
			add(obj);
		}

	}
	public boolean IsEmpty()
	{
		return (currentSize==0);
	}
	private void AfterInsertion() {
		if (currentSize == 1 || currentSize == 0)
			return;
		else {
			int temp1 = currentSize;
			int temp2 = currentSize;
			while (temp1 / 2 > 0) {
				if (costHeap[temp1].cost < costHeap[temp1 / 2].cost) {
					costHeap[0] = costHeap[temp1 / 2];
					costHeap[temp1 / 2] = costHeap[temp1];
					costHeap[temp1] = costHeap[0];
					costHeap[0] = null;
					temp1 = temp1 / 2;
				} else
					break;
			}
			while (temp2 / 2 > 0) {
				if (timeHeap[temp2].time < timeHeap[temp2 / 2].time) {
					timeHeap[0] = timeHeap[temp2 / 2];
					timeHeap[temp2 / 2] = timeHeap[temp2];
					timeHeap[temp2] = timeHeap[0];
					timeHeap[0] = null;
					temp2 = temp2 / 2;
				} else
					break;
			}
		}

	}

	private void AfterDeletion() {
		if (currentSize == 1 || currentSize == 0)
			return;
		else {
			int temp1 = 1;
			int temp2 = 1;
			while (temp1 <= currentSize) {
				if (temp1 * 2 <= currentSize && costHeap[temp1].cost > costHeap[temp1 * 2].cost) {
					if ((temp1 * 2 + 1) <= currentSize && costHeap[(temp1 * 2) + 1].cost < costHeap[temp1 * 2].cost) {
						costHeap[0] = costHeap[(temp1 * 2) + 1];
						costHeap[(temp1 * 2) + 1] = costHeap[temp1];
						costHeap[temp1] = costHeap[0];
						costHeap[0] = null;
						temp1 = (temp1 * 2) + 1;
					} else {
						costHeap[0] = costHeap[temp1 * 2];
						costHeap[temp1 * 2] = costHeap[temp1];
						costHeap[temp1] = costHeap[0];
						costHeap[0] = null;
						temp1 = temp1 * 2;
					}
				} else if ((temp1 * 2 + 1) <= currentSize && costHeap[temp1].cost > costHeap[(temp1 * 2) + 1].cost) {
					costHeap[0] = costHeap[(temp1 * 2) + 1];
					costHeap[(temp1 * 2) + 1] = costHeap[temp1];
					costHeap[temp1] = costHeap[0];
					costHeap[0] = null;
					temp1 = (temp1 * 2) + 1;
				} else
					break;
			}
			while (temp2 <= currentSize) {
				if (temp2 * 2 <= currentSize && timeHeap[temp2].time > timeHeap[temp2 * 2].time) {
					if ((temp2 * 2 + 1) <= currentSize && timeHeap[(temp2 * 2) + 1].time < timeHeap[temp2 * 2].time) {
						timeHeap[0] = timeHeap[temp2 * 2 + 1];
						timeHeap[temp2 * 2 + 1] = timeHeap[temp2];
						timeHeap[temp2] = timeHeap[0];
						timeHeap[0] = null;
						temp2 = temp2 * 2 + 1;
					} else {
						timeHeap[0] = timeHeap[temp2 * 2];
						timeHeap[temp2 * 2] = timeHeap[temp2];
						timeHeap[temp2] = timeHeap[0];
						timeHeap[0] = null;
						temp2 = temp2 * 2;
					}
				} else if (temp2 * 2 + 1 <= currentSize && timeHeap[temp2].time > timeHeap[temp2 * 2 + 1].time) {
					timeHeap[0] = timeHeap[temp2 * 2 + 1];
					timeHeap[temp2 * 2 + 1] = timeHeap[temp2];
					timeHeap[temp2] = timeHeap[0];
					timeHeap[0] = null;
					temp2 = temp2 * 2 + 1;
				} else
					break;
			}
		}
	}

	public Vert deque(boolean type) throws Exception {
		Vert ret;
		if (currentSize == 0)
			return null;
		if (type == COST) {
			ret = deleteMin(costHeap);
			int indx = find(timeHeap, ret.name);
			if (indx == -1)
				throw new Exception("Not Found");
			else
				popUP(timeHeap, indx);
			currentSize--;
		} else {
			ret = deleteMin(timeHeap);
			int indx = find(costHeap, ret.name);
			if (indx == -1)
				throw new Exception("Not Found");
			else
				popUP(costHeap, indx);
			currentSize--;
		}
		AfterDeletion();
		return ret;
	}

	private Vert deleteMin(Vert[] heap) {
		Vert ret = heap[1];
		heap[1] = heap[currentSize];
		heap[currentSize] = null;
		return ret;

	}

	private void popUP(Vert[] heap, int indx) {

		while (indx > 0) {
			heap[indx] = heap[indx / 2];
			indx /= 2;
		}
		heap[1] = heap[currentSize];
	}
	public Vert findMin(boolean solution)
	{
		if(solution==COST)
			return costHeap[1];
		else
			return timeHeap[1];
	}
	public int find(Vert[] heap, String n) {
		for (int i = 1; i < heap.length; i++) {
			if (heap[i].name.compareTo(n)==0)
				return i;
		}
		return -1;
	}

	public PriorityQueues() {
		costHeap = new Vert[DefaultSize + 1];
		timeHeap = new Vert[DefaultSize + 1];
		currentSize = 0;
	};

	public PriorityQueues(int size) {
		costHeap = new Vert[size + 1];
		timeHeap = new Vert[size + 1];
		currentSize = 0;
	}

	private Vert[] Enlarge(Vert[] ary) {
		Vert[] newAry = new Vert[(ary.length * 2) + 1];
		for (int i = 0; i < ary.length; i++)
			newAry[i] = ary[i];
		return newAry;
	}
}
