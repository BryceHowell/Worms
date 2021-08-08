import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.Math;

class TouchStore {
	Hashtable H;
	int total;
	TouchStore() {
		H=new Hashtable(100);
		total=0;
		}
	
	void touch(int uid) {
		Integer I=(Integer)H.get(uid);
		if (I==null) { GeneStore.increment(uid); H.put(uid,1); } else {
			H.put(uid,I+1);
			}		
		total++;
		}
	void clear() {
		for (Enumeration e = H.keys() ; e.hasMoreElements() ;) {
			Integer UID=(Integer)e.nextElement();
			GeneStore.decrement((int)UID);
			}
		H.clear();
		total=0;
		}
	int select() {
		int summa=0;
		int selection=(int)Math.floor(total*Math.random());
		for (Enumeration e = H.keys() ; e.hasMoreElements() ;) {
			Integer UID=(Integer)e.nextElement();
			summa+=(Integer)H.get(UID);
			if (selection<summa) return (Integer)UID;
			}
		return 0;
		}

}