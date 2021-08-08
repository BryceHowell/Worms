import java.lang.*;
import java.util.Vector;
import java.awt.*;
import java.io.*;

class Segment {
	int a,b; // store coords as ints
	double x,y; // store coords as floats
	Segment next;
	}

public class World {
	// static constants for the worms, since inner classes can't HAVE static constants :P
	static double SEGMENT_METABOLISM=0.01;
	static int MINIMUM_ADULT=7;
	static double SEGDEVCOST=100.0;
	static double SEGENRSTORE=50.0;


   // data members
   private boolean [][] wormplane;
   private boolean [][] algaplane;
   int height,width;
   
   Vector wormList;
   int nextWorm;
   
   int foodpop;
   int next_uid;
   int time;
   
   static int deltaX[]={1,1,0,-1,-1,-1,0,1};
   static int deltaY[]={0,1,1,1,0,-1,-1,-1};
   
   static float LUNCH=12.0f;
   
   // somewhere in here I need a master DB of worm_controls
   // and each worm still has to get its own DB of "genetic donors"
   
   
	World(int horz, int vert) {
     	 wormplane=new boolean [horz][vert];
	 algaplane=new boolean [horz][vert];
	 wormList=new Vector(horz*vert/512,64);
	 time=0;
	 next_uid=1;
	 width=horz; height=vert;
     }
	
	PrintWriter stats;
	
	PrintWriter survivorship;
	PrintWriter ancestry;
	void setAncestryFile(String fileName) {
		try {
		ancestry=new PrintWriter(new FileWriter(fileName)); 
		} catch (Exception e) { 
		 System.out.println(e.toString()); }
		
	}
	void writeAncestor(int age, int offspring) {
		if (ancestry==null) return;
		survivorship.println(age+" , "+offspring);
		survivorship.flush();
		}
	void closeAncestry() {
		ancestry.close();
		}
// this is a lame way to handle things atm. I should just serialize 
// and save dead worms to file, then run an analysis program that 
// goes through the data to create graphs
	void setSurvivorFile(String fileName) {
		try {
		survivorship=new PrintWriter(new FileWriter(fileName)); 
		} catch (Exception e) { 
		 System.out.println(e.toString()); }
		
	}

	void writeSurvivor(int age, int offspring) {
		if (survivorship==null) return;
		survivorship.println(age+" , "+offspring);
		survivorship.flush();
		}

	void closeSurvivor() {
		survivorship.close();
		}

	void setStatsFile(String fileName) {
		try {
		stats=new PrintWriter(new FileWriter(fileName)); 
		} catch (Exception e) { 
		 System.out.println(e.toString()); }
		
	}
	
	void writeStats() {
		if (stats==null) return;
		stats.println(wormPopulation()+" , "+foodpop);
		stats.flush();
		}
	
	void closeStats() {
		stats.close();
		}
	
	// another method to populate alga and worms should be written
	
	Graphics G;
	int gwidth, gheight;
	int gxoff,gyoff;
	
	void setGraphics(Graphics g, int wide, int high, int xoffset, int yoffset) {
	G=g;
	gwidth=wide;
	gheight=high;
	gxoff=xoffset;
	gyoff=yoffset;
	}
	
	void draw() {
		if (G==null) return;
		int portwidth=Math.min(gwidth,width);
		int portheight=Math.min(gheight,height);
		int foundred=0;
		for (int i=0; i<portwidth; i++) 
			for (int j=0; j<portheight; j++) {
				int x=(gxoff+i)%width;
				int y=(gyoff+j)%height;
				Color there;
				if (isWorm(x,y)) there=Color.white; else
					if (isAlga(x,y)) {there=Color.red; foundred++;}else
						there=Color.black;
				G.setColor(there);
				G.fillRect(i,j,1,1);
				}
		

	}
	
	
	
	void algaPopulate(int popsize) {
		int x,y;
		for (int i=0; i<popsize; i++) {
			x=(int)Math.floor(width*Math.random());
			y=(int)Math.floor(height*Math.random());			
			if (algaplane[x][y]) i--; else setAlga(x,y);			
			}
		foodpop=popsize;
		}
   
    @SuppressWarnings("unchecked")
	void wormPopulate(int popsize) {
		for (int i=0; i<popsize; i++) {
		    Worm w=new Worm();
			w.seedWorm(width*Math.random(),height*Math.random());
			wormList.add(w);
			}
		}
   
	int wrapX(int x) {
		return (x+width)%width;
		}
		
	int wrapY(int y) {
		return (y+height)%height;		
		}

	double wrapX(double x) {
		return (x+width)%width;
		}
		
	double wrapY(double y) {
		return (y+height)%height;		
		}

		
    // organic growth of alga
	void algaGrowth(int totalSunshine) {
		int a,b,c,d,t;
		for (int i=0; i<totalSunshine; i++) {
			a=(int)(Math.random()*width); 
			b=(int)(Math.random()*height); 
			t=(int)(Math.random()*8);
			c=wrapX(a+deltaX[t]);
			d=wrapY(b+deltaY[t]);
			if (algaplane[a][b]) {
				if (!algaplane[c][d]) {
					//algaplane[c][d]=true;
					setAlga(c,d);
					foodpop++;
				}
			} else 
			if (algaplane[c][d]) {
				//algaplane[a][b]=true;
				setAlga(a,b);
				foodpop++;
			}
		}
	}
	
	public void simCycle() {
		// following code produces a short low-growth season
		//int seasonTime=time/1024;
		//if (seasonTime%16==15) algaGrowth(width*height/2048); else algaGrowth(width*height/512);
		algaGrowth(width*height/512);
		// 
		nextWorm=0;
		while (nextWorm<wormList.size()) {
			Worm currentWorm=(Worm)wormList.elementAt(nextWorm);
			currentWorm.animate();
			nextWorm++;
			}
		if (time%100==0) writeStats();
		time++;
		}
	
	
	
	public int wormPopulation() {
		return wormList.size();
		}
   
    // function members -- must be modified to update the Graphics plane
	public boolean isWorm(int x, int y) {
		return wormplane[x][y];
		}
	  
	public boolean isAlga(int x, int y) {
		return algaplane[x][y];
		}

	public void updatePixel(int i, int j) {
		if (G==null) return;
		int x=(i-gxoff+width)%width;
		int y=(j-gyoff+height)%height;
		if (0<x && x<gwidth && 0<y && y<gheight) {
			Color there;
			if (isWorm(i,j)) there=Color.white; else
				if (isAlga(i,j)) there=Color.red; else
					there=Color.black;
			G.setColor(there);
			G.fillRect(x,y,1,1);
			
			}
		}
		
	public void setWorm(int x, int y) {
		wormplane[x][y]=true;
		//if (0<x && x<70 && 0<y && y<30) ANSIdriver.ansiplot("O",x,y,1,36);
		updatePixel(x,y);
		}
	 
	public void setAlga(int x, int y) {
		algaplane[x][y]=true;
		//if (0<=x && x<70 && 0<=y && y<30 && !isWorm(x,y)) ANSIdriver.ansiplot(".",x,y,1,31);
		updatePixel(x,y);
		}
	
	public void clearWorm(int x, int y) {
		wormplane[x][y]=false;
		// check for Alga, paint Alga if there else paint blank color
		//if (0<x && x<70 && 0<y && y<30) if (!isAlga(x,y)) ANSIdriver.ansiplot(" ",x,y,1,37); else ANSIdriver.ansiplot(".",x,y,1,31);
		updatePixel(x,y);
		}
	 
	public void clearAlga(int x, int y) {
		algaplane[x][y]=false;
		//if (0<x && x<70 && 0<y && y<30 && !isWorm(x,y)) ANSIdriver.ansiplot(" ",x,y,1,37); 
		updatePixel(x,y);
		}
	
	public void initialize(int foodPop, int wormPop) {
		algaPopulate(foodPop);
		wormPopulate(wormPop);
		}
	
    public static void main(String[] args) {
		World petri=new World(100,100);
		petri.initialize(1500,10);
		while (true) {
			petri.simCycle();
			}
        }
	 
	 
	public class Worm extends Object {
		

		//data members
		Segment head;	
		Segment tail;
		int length;
		int age;
		double dir;
		double energy;
		double development;
		double egg;
		int lastTouch; // saves what the worm ran into the last turn
		// 0 nothing, 1 food, -1 worm
	
		WormControl WC;
		TouchStore TS;
		int uid;
		
		int maternal_uid;
		int paternal_uid;
		int offspring; // number of offspring this worm spawned
		// some sort of worm_control object goes here. 
		// the default is the normal set of genes, else it's the neural net i wrote before 
		//
		// ordinary constructor should take a location, perhaps
		// another constructor, for loading, should take a body length
		// reproduction method, should take other parent's dna as arg
		// destructor should erase pixels as well as deallocate
		// reverse of body method *
		// metabolism/growth/egg growth method? *
		// growth of worm body *
		//
		void metabolism(double growth, double reproduction, boolean movement) {
			energy-=reproduction+growth;
			development+=growth;
			egg+=reproduction;
			energy-=0.1+length*SEGMENT_METABOLISM; 
			if (movement) energy-=0.9+length*SEGMENT_METABOLISM;
			}
	
		void forward() {
			double nx,ny;
			int a,b;
			nx=head.x+Math.cos(dir);
			ny=head.y+Math.sin(dir);
			if (nx<0) nx+=(double)width; 
			if (nx>=width) nx-=(double)width;
			if (ny<0) ny+=(double)height; 
			if (ny>=height) ny-=(double)height;
			a=(int)Math.floor(nx);
			b=(int)Math.floor(ny);
			lastTouch=0;
			
			if (isWorm(a,b) && !(head.a==a && head.b==b))  {
				Segment curr=head; curr=curr.next;
				while (curr!=head) {
					if (curr.a==a && curr.b==b) { lastTouch=-1; break; }		  
					curr=curr.next;
					} 
			
			if (lastTouch!=-1) { 
				int found=-1;			
				// search for other worm			 
				for (int i=0; i<wormList.size(); i++) {
					Worm check=(Worm)wormList.get(i);
					if (check==this) continue;
					curr=check.head;
				
					int dx=Math.abs(a-head.a); 
					int dy=Math.abs(b-head.b);
					int dmax=check.length+2;
					if (dx>width/2) dx=Math.abs(width-dx);
					if (dy>height/2) dy=Math.abs(height-dy);
					// conversion to integer for the head coords?
					if (dx*dx+dy*dy<dmax*dmax) {
						for (int j=0; j<check.length; j++) {
							if (curr.a==a && curr.b==b) {
								found=i;
								lastTouch=-2;
								break;
								}
							curr=curr.next;
							}
						if (found!=-1) break;				
						}	
				
					}
			
				// do genestore crap
				if (length>=MINIMUM_ADULT && found>=0) {
					Worm check=(Worm)wormList.get(found);
					if (check.length>=MINIMUM_ADULT) check.TS.touch(uid);
					}
				} else lastTouch=-1;
			} else if (isAlga(a,b)) {
			lastTouch=1; 
			double fullness=energy/(SEGENRSTORE*length);
			energy+=LUNCH*(1.0-fullness*fullness);
			clearAlga(a,b);
			foodpop--;
			} else lastTouch=0;
			if (lastTouch>=0) {
				clearWorm(tail.a,tail.b);
				setWorm(a,b);
				tail.a=a; tail.b=b;
				tail.x=nx; tail.y=ny;
				head=tail;
				tail=tail.next;
				}
 
			}
	
	void reverse() {
		double alpha,beta,theta;
		
		Segment neck=tail.next;
	    Segment pred=head;
		Segment curr=tail;
		Segment succ=curr.next;
        for (int seg=0; seg<length; seg++) {
			curr.next=pred;
			pred=curr;
			curr=succ;
			succ=curr.next;
			}
		curr=head;
		head=tail;
		tail=curr;
		alpha=head.x-neck.x;
		beta=head.y-neck.y;
		if (alpha==0. && beta==0.) theta=Math.PI+dir; else
			if (beta!=0. && alpha==0.) theta=Math.PI*beta/Math.abs(beta); else
				theta=Math.atan2(beta,alpha);
        dir=theta;
		lastTouch=0;
		}
	  
	void growSegment() {
		// just stick a segment between tail and head which becomes the new tail... and has tail's location
		Segment grow=new Segment();
		grow.next=tail;
		head.next=grow;
		grow.x=tail.x;
		grow.y=tail.y;
		grow.a=tail.a;
		grow.b=tail.b;
		tail=grow;
		length++;
		}
		
	void setSegments(int size) {
		tail=head=new Segment();
		for (int i=1; i<size; i++) {
			Segment newseg=new Segment();
			newseg.next=head;
			head=newseg;
			}
		tail.next=head;
		length=size;
		}
		
	void seedWorm(double x, double y) {		
		length=5;
		setSegments(length);
		Segment loop=head;
		for (int i=0; i<length; i++) {
			loop.x=x; loop.y=y;
			loop.a=(int)Math.floor(x);
			loop.b=(int)Math.floor(y);
			loop=loop.next;
			}
		setWorm(head.a,head.b);
		dir=2.0*Math.PI*Math.random();
		energy=500.0;
		development=0.0;
		egg=0.0;
		lastTouch=0;
		// genetic initialization goes here
		WC=new WormControl();
		WC.initialize();
		TS=new TouchStore();
		uid=next_uid;
		maternal_uid=0;
		paternal_uid=0;
		offspring=0;
		GeneStore.register(uid,WC);
		next_uid++;
		}
		
	void birth() {
		offspring++;
		egg-=2.0*SEGDEVCOST;
		Worm child=new Worm();
		child.uid=next_uid;
		child.maternal_uid=uid;
		next_uid++;
		child.dir=dir+(Math.random()-0.5)*Math.PI;
		child.length=(int)Math.floor((3.0+length/2.0)/2.0);
		Segment curr=head;
		for (int seg=0; seg<child.length; seg++) curr=curr.next;
		child.head=curr;
        	child.tail=head.next;
		 
		tail=child.head.next;
		child.head.next=child.tail;
		head.next=tail;
 
		length-=child.length;
		child.age=0;
		child.development=0.0;
		child.egg=0;
		
		child.energy=WC.getShare()*energy;
		energy=energy-child.energy;
		
		lastTouch=0;
		
		// select parent using TouchStore
		int parent_uid=TS.select();
		child.paternal_uid=parent_uid;
		WormControl parentWC;
		if (parent_uid==0) parentWC=WC; else parentWC=GeneStore.getWormControl(parent_uid);
		// breed new WormControl
		child.WC=new WormControl(WC,parentWC);
		TS.clear();
		// register new WormControl with GeneStore
		child.TS=new TouchStore();
		GeneStore.register(child.uid,child.WC);
		wormList.add(child);
		}
		
	void death() {
		writeSurvivor(age,offspring);
		Segment curr=tail;
		Segment prev=null;
		for (int i=0; i<length; i++) {
			clearWorm(curr.a,curr.b);       
			curr=curr.next;
			}
		curr=tail; prev=head;
		for (int i=0; i<length; i++) {
			prev.next=null;			
			prev=curr;
			curr=curr.next;
			}
		head=null; tail=null;
		//touch_store_clear
		TS.clear();
		//genestore_unregister
		GeneStore.unregister(uid);
		wormList.remove(this);
		nextWorm--;
		}
		
	void animate() {
		age++;
		// pass values to wormcontrol, get action values
		WC.receive(length,energy,development,egg,lastTouch);
		// metabolism
		metabolism(WC.getGrowth(),WC.getReproduction(),WC.getMovement()!=0);
		if (energy<0) { death(); return; }
		// movement
	    if (WC.getMovement()==-1) {
			reverse();
			} else {
			dir+=WC.getTurn();
			forward();
			}
	   // eating is handled in forward
	   
	   // check for reproduction and growth
		if (development>=SEGDEVCOST) {
			development-=SEGDEVCOST;
			growSegment();
			}
		if (egg>=2.0*SEGDEVCOST && length>=MINIMUM_ADULT) birth();
			
	   
	  }
	
	}
	 
	 
	 
	}
