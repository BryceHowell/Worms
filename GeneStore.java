import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.Math;


class WormTuple {
	WormControl W;
	int reference_count;
	
}

public class GeneStore {
	static Hashtable H=new Hashtable(2000);
	
    static void register(int uid, WormControl wc) {
		WormTuple wt=new WormTuple();
		wt.W=wc; wt.reference_count=1;
		H.put(uid,wt);
		}
	
	static WormControl getWormControl(int uid) {
		WormTuple wt=(WormTuple)H.get(uid);
		if (wt!=null) return wt.W; else return null;
		}
	
	static void increment(int uid){
		WormTuple wt=(WormTuple)H.get(uid);		
		if (wt!=null) wt.reference_count++; 
		}
	static void decrement(int uid){
		WormTuple wt=(WormTuple)H.get(uid);
		if (wt!=null) {
			wt.reference_count--;
			if (wt.reference_count==0) H.remove(uid);
			}
		}		
	static void unregister(int uid) {
		decrement(uid);
		}
		
	static void status(int uid) {
		WormTuple wt=(WormTuple)H.get(uid);
		if (wt!=null) {
			
			System.out.println("value "+wt.reference_count); 
			} else System.out.println("UID NOT PRESENT");
		}
		
	public static void main(String [] args) {
		WormControl wc=new WormControl();
		
		register(12,wc);
		register(13,wc);
		register(14,wc);
		register(15,wc);
		status(12);
		increment(12);
		increment(12);
		status(12);
		decrement(12);
		decrement(12);
		unregister(12);
		status(12);
		TouchStore T=new TouchStore();
		T.touch(13);
		T.touch(14);
		T.touch(15);
		T.touch(15);
		System.out.println(T.select());
		status(15);
		T.clear();
		status(15);
		System.out.println(T.select());
		getWormControl(15);
		}
}


 
