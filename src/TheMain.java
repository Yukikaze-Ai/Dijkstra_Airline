/*
 * Hongyun Du
 * HXD171530
 * CS 3345
 * PROJECT 6
 */
import java.io.*;
import java.util.*;

public class TheMain {
	static final boolean COST = true;
	static final boolean TIME = false;
	static final String dataPath = "data.txt";
	static final String queryPath = "query.txt";
	static final String outputPath = "result.txt";
	static int citiesSize = 0;

	static public void main(String[] ags) {
		Vert cities[] = new Vert[0];
		cities = ReadData(cities); // read the data file(Air path file)
		Vert[] tempCity = CopyVerts(cities);
		String Start;
		String Dest;
		boolean solution = COST;
		Scanner sc = new Scanner(System.in);
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(queryPath));
			BufferedReader br = new BufferedReader(reader);
			File outputFile = new File(outputPath);
			outputFile.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
			String temp;
			
			while ((temp = br.readLine()) != null) {
				String s[] = temp.split("\\|");
				if (s.length != 3) {
					out.write("Invalid Query Format\n");
					continue;
				}

				Start = s[0];
				Dest = s[1];
				if (s[2].compareTo("T") == 0)
					solution = TIME;
				else if (s[2].compareTo("C") == 0)
					solution = COST;
				else {
					out.write("Invalid Query Format\n");
					continue;
				}

				int StartIndx = getIndex(tempCity, Start);
				int DestIndx = getIndex(tempCity, Dest);

				if (StartIndx == -1 || DestIndx == -1) {
					out.write("NO FLIGHT AVAILABLE FOR THE REQUEST\n");
					continue;
				}

				Initialize(tempCity, StartIndx);
				try {
					Dijk(tempCity, StartIndx, DestIndx, solution);
				} catch (Exception e) {
					System.out.println(e);
				}
				if (tempCity[DestIndx].path == null) {
					out.write("NO FLIGHT AVAILABLE FOR THE REQUEST\n");
					continue;
				}
				Vert tmp = tempCity[DestIndx];
				double time = tmp.time, cost = tmp.cost;
				LinkedList<String> Path = new LinkedList<String>();
				while (tmp != null) {
					Path.addLast(tmp.name);
					tmp = tmp.path;
				}

				out.write(Path.size() - 1 + "|");
				for (int i = Path.size() - 1; i >= 0; i--) {
					out.write(Path.get(i) + "|");
				}
				out.write(cost + "|" + time+"\n");
			}
			br.close();
			out.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		sc.close();
		System.out.print("Done");

	}

	public static int getIndex(Vert[] ary, String Name) {
		// find the index of the target city with its name;
		for (int i = 0; i < ary.length; i++) {
			if (ary[i].name.compareTo(Name) == 0)
				return i;
		}
		return -1;
	}

	public static void Initialize(Vert[] Origin, int StartIndx) {
		// set all things to default
		for (int i = 0; i < Origin.length; i++) {
			Origin[i].known = false;
			Origin[i].cost = Double.MAX_VALUE;
			Origin[i].time = Double.MAX_VALUE;
			Origin[i].path = null;
		}
		Origin[StartIndx].cost = 0;
		Origin[StartIndx].time = 0;
	}

	public static void Dijk(Vert[] Origin, int StartIndx, int DestIndx, boolean solution) throws Exception {
		// the main method
		Origin[StartIndx].known = true;

		PriorityQueues WeightedVert = new PriorityQueues(); // store the cities that their cost and time are not
															// infinity;
		if (IsAllknown(Origin) == true)
			return;// if all vertices are known, stop
		edge[] egs = Origin[StartIndx].getAlledges();
		while (IsAllknown(Origin) == false) {
			if (egs.length == 0)
				return; // if a vertex has no out edge, stop
			for (int i = 0; i < egs.length; i++) {
				if (solution == COST) {
					if (egs[i].to.known == false && egs[i].to.cost > egs[i].from.cost + egs[i].cost) { // if the
																										// adjacent
																										// vertex is
																										// unknown, and
																										// its cost is
																										// greater than
																										// the new one,
																										// change it
						egs[i].to.cost = egs[i].from.cost + egs[i].cost;
						egs[i].to.time = egs[i].from.time + egs[i].time;
						egs[i].to.path = egs[i].from;
						WeightedVert.add(egs[i].to); // push it in to the priority queue
					}
				} else {
					if (egs[i].to.known == false && egs[i].to.time > egs[i].from.time + egs[i].time) { // if the
																										// adjacent
																										// vertex is
																										// unknown, and
																										// its time is
																										// greater than
																										// the new one,
																										// change it
						egs[i].to.cost = egs[i].from.cost + egs[i].cost;
						egs[i].to.time = egs[i].from.time + egs[i].time;
						egs[i].to.path = egs[i].from;
						WeightedVert.add(egs[i].to); // push it in to the priority queue
					}
				}
			}
			Vert NextVert = WeightedVert.deque(solution); // get a Vertex that has smallest Time/Cost
			if (NextVert == null)
				return; // If there is no vertex, stop.
			NextVert.known = true; // set next vertex to known
			egs = NextVert.getAlledges(); // get all edges of the next vertex
		}
	}

	public static boolean IsAllknown(Vert[] ary) {
		// if all vertices are known, return true, otherwise, return false
		for (int i = 0; i < ary.length; i++) {
			if (ary[i].known == true)
				return false;
		}
		return true;
	}

	public static Vert[] CopyVerts(Vert[] v) {
		// return a copy of origin array
		Vert[] newVert = new Vert[citiesSize];
		for (int i = 0; i < citiesSize; i++) {
			newVert[i] = v[i];
		}
		return newVert;
	}

	public static Vert[] ReadData(Vert cities[]) {
		// read data file and store it in array
		Scanner sc = new Scanner(System.in);
		LinkedList<String> lines = new LinkedList<String>();
		try {
			// open the input file
			InputStreamReader reader = new InputStreamReader(new FileInputStream(dataPath));
			BufferedReader br = new BufferedReader(reader);

			String temp;
			while ((temp = br.readLine()) != null) {
				lines.addLast(temp);
			}
			br.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		sc.close();
		cities = new Vert[lines.size() * 2];

		for (int indx = 0; indx < lines.size(); indx++) {
			String temp = lines.get(indx);
			String[] s = temp.split("\\|");
			if (s.length != 4) // if the result have more or less than 4 part, Skip this line and print error
								// message
			{
				System.out.println("Invalid Data Format");
				continue;
			}
			String start = s[0];
			String end = s[1];
			double c = -1;
			double t = -1;
			try {
				c = Double.valueOf(s[2]);	//These Double.valueOf will throw exception if inputs aren't numbers.
				t = Double.valueOf(s[3]);
			} catch (Exception e) {
				System.out.println("Invalid Data Format");
				continue;
			}
			int IndxOfStart = -1;
			int IndxOfEnd = -1;
			for (int i = 0; i < citiesSize; i++) {
				//try to find the start city and the end city in the exit array 
				if (cities[i].name.compareTo(start) == 0)
					IndxOfStart = i;
				if (cities[i].name.compareTo(end) == 0)
					IndxOfEnd = i;
			}
			//if not found, the index will be -1;
			if (IndxOfStart == -1) {
				cities[citiesSize] = new Vert(start);//if the city doesn't exit, create a new vertex
				if (IndxOfEnd == -1) {
					cities[citiesSize + 1] = new Vert(end);//if the city doesn't exit, create a new vertex
					cities[citiesSize].addNewAdj(cities[citiesSize + 1], c, t);	//connect the cities through edges
					citiesSize += 2;	
				} else {
					cities[citiesSize].addNewAdj(cities[IndxOfEnd], c, t);//connect the cities through edges
					citiesSize++;
				}
			} else {
				if (IndxOfEnd == -1) {
					cities[citiesSize] = new Vert(end);//if the city doesn't exit, create a new vertex
					cities[IndxOfStart].addNewAdj(cities[citiesSize], c, t);//connect the cities through edges
					citiesSize++;
				} else {
					cities[IndxOfStart].addNewAdj(cities[IndxOfEnd], c, t);//connect the cities through edges
				}
			}

		}
		return cities;
	}
}

class Vert {
	String name;
	double cost;
	double time;

	LinkedList<edge> adjEdges = new LinkedList<edge>();
	boolean known = false;
	Vert path;

	void addNewAdj(Vert b, double c, double t) {
		adjEdges.addLast(new edge(this, b, c, t));
	}

	public int SizeOfAdj() {
		return adjEdges.size();
	}

	public edge[] getAlledges() {
		edge[] tmp = new edge[adjEdges.size()];
		for (int i = 0; i < adjEdges.size(); i++) {
			tmp[i] = adjEdges.get(i);
		}
		return tmp;
	}

	Vert(String n) {
		name = n;
		adjEdges = new LinkedList<edge>();
	}

	Vert() {
	}
}

class edge {
	Vert from;
	Vert to;
	double cost = 0;
	double time = 0;

	edge(Vert a, Vert b, double c, double t) {
		from = a;
		to = b;
		cost = c;
		time = t;
	}

	edge() {
	}
}