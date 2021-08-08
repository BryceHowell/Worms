import javax.swing.JFrame;
import javax.swing.JLabel;
import java.lang.Math;
import java.awt.*;

//import statements
//Check if window closes automatically. Otherwise add suitable code
public class WormFrame extends JFrame {

    static  Image OSI;    // An off-screen images that holds the picture
                    //    of the Mandelbrot set.  This is copied onto
                    //    the drawing surface, if it exists.  It is
                    //    created by the computational thread.
                    
    static  Graphics OSG; // A graphics context for drawing on OSI.
	static int width,height;
	
	

	public static void main(String args[]) {
		WormFrame WF=new WormFrame();
		width=WF.getWidth();
		height=WF.getHeight();
		OSI=WF.createImage(width,height);
        // Create the off-screen image where the picture will
        // be stored, and fill it with black to start.
        OSG=OSI.getGraphics();
        OSG.setColor(Color.black);
        OSG.fillRect(0,0,width,height);		
		
		World petri=new World(width,height);
		petri.initialize(131072,10);
		petri.setGraphics(OSG,width,height,0,0);
		petri.draw();
		petri.setStatsFile("popdata.txt");
		petri.setSurvivorFile("survivor.txt");
		WF.repaint();
		while (true) {
			petri.simCycle();
			//if (petri.wormList.size()==0) System.out.println("population dead");
			WF.repaint();
			}
	
		
	}
	
	
	
	WormFrame() {
		//JLabel jlbHelloWorld = new JLabel("Hello World");
		//add(jlbHelloWorld);
		
		this.setSize(1024,1024);
		setResizable(false);
		setUndecorated(false);
		// pack();
		setVisible(true);
		
		
		
	}
	
	public void paint(Graphics g) {
            // Called by the system to paint the drawing surface.
            // This copies the off-screen image onto the screen,
            // if the off-screen image exists.  If not, it just
            // fills the drawing surface with black.
         if (OSI == null) {
            g.setColor(Color.black);
            g.fillRect(0,0,getWidth(),getHeight());
         }
         else {
            g.drawImage(OSI,0,0,null);
         }
      }	
	
	
}
