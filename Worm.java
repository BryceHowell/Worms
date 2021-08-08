import java.lang.Math;

class Segment {
	int a,b; // store coords as ints
	double x,y; // store coords as floats
	Segment next;
	}

public class Worm extends Object {

	static double SEGMENT_METABOLISM=0.01;
	static int MINIMUM_ADULT=7;
	static double SEGDEVCOST=100.0;
	static double SEGENRSTORE=50.0;

// mapping uid -> (wormcontrol, int)  to hold the wormcontrol and know when reference count is down to 0
// per worm : uid -> int (touch count)   

	//data members
	Segment head;	
	Segment tail;
	int length;
	double dir;
	double energy;
	double development;
	double egg;
	int lastTouch; // saves what the worm ran into the last turn
	
	//WormControl
	
	
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
	void metabolism(double reproduction, double growth, boolean movement) {
		energy-=reproduction+growth;
		development+=growth;
		egg+=reproduction;
		energy-=0.1+length*SEGMENT_METABOLISM; 
		if (movement) energy-=0.9+length*SEGMENT_METABOLISM;
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
		}
		
	void setSegments(int size) {
		tail=head=new Segment();
		for (int i=1; i<size; i++) {
			Segment newseg=new Segment();
			newseg.next=head;
			head=newseg;
			}
		tail.next=head;
		}
		
	void seedWorm(double x, double y) {
		length=5;
		setSegments(length);
		Segment loop=head;
		for (int i=0; i<length; i++) {
			loop.x=x; loop.y=y;
			loop.a=(int)Math.floor(x);
			loop.b=(int)Math.floor(y);
			}
		dir=2.0f*Math.PI*Math.random();
		energy=500.0f;
		development=0f;
		egg=0f;
		lastTouch=0;
		// genetic initialization goes here
		}
	
	
	}
