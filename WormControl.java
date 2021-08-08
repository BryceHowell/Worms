import java.lang.Math;

public class WormControl {
	// receive input
	//     it would be nicest to have a setter for each sensory/bodily input
	//     but an array of float could still be done
	
	// run control
	
	// transmit output
	//     if i do setters for input i might as well do getters for output
	
	// serialization
	// breeding with another WormControl
	//        if not the same exact class (I might need reflection for this)
	//        just choose one of the parents at random
	//        otherwise each derived class specifies how to intermix the parents within its class only
	
	int geneLength;
	double geneGrowth;
	double geneReproduction;
	double geneShare;
	double geneTurn;
	
	
	double turn;
	double growth,reproduction;
	int movement;
	double share;
	
	void initialize() {
		geneLength=13;
		geneGrowth=0.5+0.1*Math.random();
		geneReproduction=0.5+0.1*Math.random();
		geneShare=0.1+0.8*Math.random();
		geneTurn=1.0+2.0*Math.random();
			
		}
		
	WormControl() {
		turn=0;
		growth=0;
		reproduction=0;
		movement=1;
		share=0.5;
		}
	
	void dump(){
		System.out.println("length growth repro share turn  (genes)");
		System.out.println(geneLength+" "+geneGrowth+" "+geneReproduction+" "+geneShare+" "+geneTurn);
		System.out.println("turn growth repro movement share (actions)");
		System.out.println(turn+" "+growth+" "+reproduction+" "+movement+" "+share);
		
		}
	
	void receive(int length, double energy, double development, double egg, int lastTouch) {
	  if (lastTouch<0) movement=-1; else movement=1;
	  // lastTouch has a negative value for touching a worm; -1 is self, -2 is another worm
	  if (length<geneLength) {
		//System.out.println("CORRECT BRANCH IN WC");
	    reproduction=0;
		growth=geneGrowth;
	    } else {
	    reproduction=geneReproduction;
		growth=0;
		}
	  //System.out.println("length geneLength "+length+" "+geneLength);
	  share=geneShare;
	  turn=Math.PI*(Math.random()>0.5 ? -1.0 : 1.0)*Math.pow(Math.random(),geneTurn)/2.0;
	  }
	
	double getTurn() { return turn; }
	double getGrowth() { return growth; }
	double getReproduction() { return reproduction; }
	int getMovement() { return movement; }
	double getShare() { return share; }
	
	public WormControl(WormControl A, WormControl B) {
	  	
	  if (Math.random()>0.5) geneLength=A.geneLength; else geneLength=B.geneLength;
	  if (Math.random()>0.5) geneGrowth=A.geneGrowth; else geneGrowth=B.geneGrowth;
	  if (Math.random()>0.5) geneReproduction=A.geneReproduction; else geneReproduction=B.geneReproduction;
	  if (Math.random()>0.5) geneShare=A.geneShare; else geneShare=B.geneShare;
	  if (Math.random()>0.5) geneTurn=A.geneTurn; else geneTurn=B.geneTurn;

	  if (Math.random()<0.05) {
       geneLength-=(int)(Math.floor(3.0*Math.random())-1.0);
       if (geneLength<7) geneLength=7;
       }
	  if (Math.random()<0.05) geneTurn*=Math.exp(Math.random()-0.5);
	  if (Math.random()<0.05) geneGrowth=Math.abs(geneGrowth+0.2*Math.random()-0.1);
	  if (Math.random()<0.05) geneReproduction=Math.abs(geneReproduction+0.2*Math.random()-0.1);
      if (Math.random()<0.05) { 
		geneShare+=0.2*Math.random()-0.1; 
		if (geneShare<0.05) geneShare=0.05;
		if (geneShare>0.95) geneShare=0.95;
	    }
	  
	  }
	
	
	}