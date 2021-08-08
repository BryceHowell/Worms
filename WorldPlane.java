import java.lang.*;

public class World {
   // data members
   private boolean [][] wormplane;
   private boolean [][] algaplane;
   int height,width;
   
   Vector wormList;
   
   int foodpop;
   int uid;
   int time;
   
   static int deltaX[]={1,1,0,-1,-1,-1,0,1};
   static int deltaY[]={0,1,1,1,0,-1,-1,-1};
   
   static float LUNCH=12.0;
   
   
	World(int horz, int vert) {
     wormplane=new boolean [horz][vert];
	 algaplane=new boolean [horz][vert];
	 wormList=new Vector(horz*vert/512,64);
	 time=0;
	 uid=1;
	 width=horz; height=vert;
     }
	
	// another method to populate alga and worms should be written
	
	int algaPopulate(int popsize) {
		int x,y;
		for (int i=0; i<popsize; i++) {
			x=Math.floor(width*Math.random());
			y=Math.floor(height*Math.random());			
			if (algaplane[x][y]) i--; else algaplane[x][y]=true;			
			}
		}
   
	int wrapX(int x) {
		return (x+width)%width;
		}
		
	int wrapY(int y) {
		return (y+height)%height;		
		}

	float wrapX(float x) {
		return (x+width)%width;
		}
		
	float wrapY(float y) {
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
					algaplane[c][d]=true;
					foodpop++;
				}
			} else 
			if (algaplane[c][d]) {
				algaplane[a][b]=true;
				foodpop++;
			}
		}
	}
	
   
    // function members -- must be modified to update the Graphics plane
	public boolean isWorm(int x, int y) {
		return wormplane[x][y];
		}
	  
	public boolean isAlga(int x, int y) {
		return algaplane[x][y];
		}
	  
	public void setWorm(int x, int y) {
		wormplane[x][y]=true;
		}
	 
	public void setAlga(int x, int y) {
		algaplane[x][y]=true;
		}
	
	public void clearWorm(int x, int y) {
		wormplane[x][y]=false;
		// check for Alga, paint Alga if there else paint blank color
		}
	 
	public void clearAlga(int x, int y) {
		algaplane[x][y]=false;
		}
	
	
	
    public static void main(String[] args) {
		System.out.println("this is a test\n");
        }
	 
	}