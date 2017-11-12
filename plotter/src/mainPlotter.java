import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class mainPlotter extends Applet{

	Random r = new Random();
	
	String[] set;
	int[] h;
	int[] w;
	int[] x;
	int[] y;
	int[] moveValue;
	double[] level;
	boolean[] alive;
	boolean[] draw;
	boolean[] gender;
	
	int printAmt = r.nextInt(78) + 2;
	int width = 700;
	int height = 600;
	
	public void init(){
		setSize(width,height);
			
		set = new String[printAmt * 10];
		h = new int[printAmt * 10];
		w = new int[printAmt * 10];
		x = new int[printAmt * 10];
		y = new int[printAmt * 10];
		moveValue = new int[printAmt * 10];
		level = new double[printAmt * 10];
		alive = new boolean[printAmt * 10];
		draw = new boolean[printAmt * 10];
		gender = new boolean[printAmt * 10];
		
		for(int i = 0; i < printAmt; i++){
			String dna = printDNA();
			
			String name = "" + i;
			boolean dir = (new File("AI").mkdir());
			boolean made = (new File("AI//" + name).mkdir());
				File movementFile = new File("AI//" + name + "//Movement_" + name + ".txt");
				File killedFile = new File("AI//" + name + "//Killed_" + name + ".txt");
				File dnaFile = new File("AI///" + name + "//DNA_" + name + ".txt");
				File rememberFile = new File("AI//" + name + "//Remember_" + name + ".txt");
				
				try{
					movementFile.createNewFile();
					killedFile.createNewFile();
					dnaFile.createNewFile();
					rememberFile.createNewFile();
				}catch(Exception e){}
				
			try{
				FileWriter dnaFileWrite = new FileWriter("AI//"+name+"//DNA_"+name+".txt", true);
				BufferedWriter writtenMovement = new BufferedWriter(dnaFileWrite);
				
				writtenMovement.write(dna);
				writtenMovement.close();
			}catch(Exception e){}
			
			String[] dnaStrings = dna.split(("(?<=\\G.{4})"));
			
				for(int y = 0; y < dnaStrings.length; y++){
					String[] dnaSubSet = dnaStrings[y].split(("(?<=\\G.{1})"));
					System.out.println(dnaStrings[y]);
				}
				
			String s = "set";
			int fX = r.nextInt(getWidth() + 1);
			int fY = r.nextInt(getHeight() + 1);
			int mv = r.nextInt(9) + 1;
			double lvl = r.nextInt(1000) + 1;
			boolean alv = true;
			boolean stilldraw = true;
			boolean g = r.nextBoolean();
			
			set[i] = s;
			x[i] = fX;
			y[i] = fY;
			moveValue[i] = mv;
			level[i] = lvl;
			alive[i] = alv;
			draw[i] = stilldraw;
			gender[i] = g;
		}
		
		for(int u = 0; u < (printAmt * 5); u++){
			if(set[u] != "set"){
				set[u] = "unset";
			}
		}
		
		final Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			public void run() {
				repaint();
			}
		}, 10, 500);
		
		final Timer t1 = new Timer();
		t1.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				move();
			}
		}, 0, 500);
		
		//Define Objects
		
	}
		
	public void move(){
		Random dir = new Random();
		
		for(int b = 0; b < printAmt * 5; b++){
			if(alive[b] == true){
			
			//Collision
				for(int c = 0; c < printAmt  * 5; c++){
					if(x[c] > x[b] - 20 && x[c] < x[b] + 20){
						if(y[c] > y[b] - 20 && y[c] < y[b] + 20){
							if(gender[c] == gender[b]){
								if(c != b && alive[c] == true && alive[b] == true){
									double clvl = level[c];
									double blvl = level[b];
									if(clvl > blvl){
										alive[b] = false;
											double toadd = level[b] / 2;
											level[c] = level[c] + toadd;
										killed(b, c);
									}else{
										alive[c] = false;
											double toadd = level[c] / 2;
											level[b] = level[b] + toadd;
										killed(c, b);
									}
									if(clvl == blvl){
										alive[b] = false;
										alive[c] = false;
									killed(b, c);
									killed(c, b);
									}
								}
							}else{
								//Have baby
								//System.out.println("Female, Male collision");
							}
						}
					}
				}
			
			//Chase movement
				for(int w = 0; w < printAmt * 5; w++){
					if(alive[w] == true){
						if(x[w] > x[b] - 250 && x[w] < x[b] + 250 && y[w] > y[b] - 250 && y[w] < y[b] + 250){
							if(level[w] < level[b]){
								if(x[w] > x[b]){
									x[b] = x[b] + moveValue[b];
								}
								if(x[w] < x[b]){
									x[b] = x[b] - moveValue[b];
								}
								if(y[w] > y[b]){
									y[b] = y[b] + moveValue[b];
								}
								if(y[w] < y[b]){
									y[b] = y[b] - moveValue[b];
								}
							}
						}
					}else{
						int direction = dir.nextInt(4) + 1;
						if(direction == 1){
							y[b] = y[b] - moveValue[b];
						}
						if(direction == 2){
							y[b] = y[b] + moveValue[b];
						}
						if(direction == 3){
							x[b] = x[b] + moveValue[b];
						}
						if(direction == 4){
							x[b] = x[b] - moveValue[b];
						}
					}
				write(w,x[w],y[w]);
				}
				
			//Bounds
				if(x[b] > getWidth()){
					x[b] = getWidth();
				}
				if(x[b] < 0){
					x[b] = 0;
				}
				if(y[b] > getHeight()){
					y[b] = getHeight();
				}
				if(y[b] < 0){
					y[b] = 0;
				}
			}else{
				x[b] = x[b];
				
				y[b] = y[b];
			}
		}
	}
	
	public void write(int fdot, int x, int y){
		if(alive[fdot] == true){
			try{
				String dot = "" + fdot;
				FileWriter moveFile = new FileWriter("AI//"+dot+"//Movement_"+dot+".txt", true);
				BufferedWriter writtenMovement = new BufferedWriter(moveFile);
				
				String coordinates = "("+x+","+y+")";
				
				writtenMovement.write(coordinates);
				writtenMovement.newLine();
				writtenMovement.close();
				
			}catch(Exception e){}
		}
	}
	
	public void killed(int dot_killed, int murderer){
		try{
			String dot = "" + murderer;
			FileWriter moveFile = new FileWriter("AI//"+dot+"//Killed_"+dot+".txt", true);
			BufferedWriter writtenMovement = new BufferedWriter(moveFile);
			
			String write = "" + dot_killed;
			
			writtenMovement.write(write);
			writtenMovement.newLine();
			writtenMovement.close();
			
		}catch(Exception e){}
	}
	
	public String printDNA(){
		//DNA **IMPORTANT!!! Parent 1
			boolean A11 = r.nextBoolean();
			boolean A21 = r.nextBoolean();
			
			boolean B11 = r.nextBoolean();
			boolean B21 = r.nextBoolean();
			
			boolean C11 = r.nextBoolean();
			boolean C21 = r.nextBoolean();
			
			boolean D11 = r.nextBoolean();
			boolean D21 = r.nextBoolean();
		
		//DNA **IMPORTANT!!! Parent 2
			boolean A12 = r.nextBoolean();
			boolean A22 = r.nextBoolean();
			
			boolean B12 = r.nextBoolean();
			boolean B22 = r.nextBoolean();
			
			boolean C12 = r.nextBoolean();
			boolean C22 = r.nextBoolean();
			
			boolean D12 = r.nextBoolean();
			boolean D22 = r.nextBoolean();			
		
			String out = "";
		
	//Parent 1
		//Choose A Parent 1
			if(A11 == true){
				out = out + "A";
			}else{
				out = out + "a";
			}
			if(A21 == true){
				out = out + "A";
			}else{
				out = out + "a";
			}
		//Choose A Parent 2
			if(A12 == true){
				out = out + "A";
			}else{
				out = out + "a";
			}
			if(A22 == true){
				out = out + "A";
			}else{
				out = out + "a";
			}
			
			
		//Choose B Parent 1
			if(B11 == true){
				out = out + "B";
			}else{
				out = out + "b";
			}
			if(B21 == true){
				out = out + "B";
			}else{
				out = out + "b";
			}
		//Choose B Parent 2
			if(B12 == true){
				out = out + "B";
			}else{
				out = out + "b";
			}
			if(B22 == true){
				out = out + "B";
			}else{
				out = out + "b";
			}
			
		//Choose C Parent 1
			if(C11 == true){
				out = out + "C";
			}else{
				out = out + "c";
			}
			if(C21 == true){
				out = out + "C";
			}else{
				out = out + "c";
			}
		//Choose C Parent 2
			if(C12 == true){
				out = out + "C";
			}else{
				out = out + "c";
			}
			if(C22 == true){
				out = out + "C";
			}else{
				out = out + "c";
			}
			
		//Choose D Parent 1
			if(D11 == true){
				out = out + "D";
			}else{
				out = out + "d";
			}
			if(D21 == true){
				out = out + "D";
			}else{
				out = out + "d";
			}
		//Choose D Parent 2
			if(D12 == true){
				out = out + "D";
			}else{
				out = out + "d";
			}
			if(D22 == true){
				out = out + "D";
			}else{
				out = out + "d";
			}
	return out;
	}
	
	public void paint(Graphics g){		
		
		
		for(int k = 0; k < (printAmt * 5); k++){
			if(set != null){
				if(alive[k] == true){
					if(gender[k] == true){
						g.setColor(Color.BLUE);
						g.fillOval(x[k] - 5, y[k] -5, 10, 10);
					}else{
						g.setColor(Color.PINK);
						g.fillOval(x[k] - 5, y[k] -5, 10, 10);
					}
					
					String plevel = Double.toString(level[k]);
					String id = Integer.toString(k);
					g.drawString(plevel, x[k] - 15, y[k] - 12);
					g.drawString(id, x[k] - 7, y[k] + 23);
					
					g.setColor(Color.BLACK);
					//Top line
					g.drawLine(x[k] - 10, y[k] - 10, x[k] + 10, y[k] - 10);
					
					//Bottom Line
					g.drawLine(x[k] - 10, y[k] + 10, x[k] + 10, y[k] + 10);
					
					//Left Line
					g.drawLine(x[k] - 10, y[k] - 10, x[k] - 10, y[k] + 10);
					
					//Left Line
					g.drawLine(x[k] + 10, y[k] - 10, x[k] + 10, y[k] + 10);
				}else{
					if(gender[k] == true){
						g.setColor(Color.BLUE);
						g.drawOval(x[k] - 5, y[k] -5, 10, 10);
					}else{
						g.setColor(Color.PINK);
						g.drawOval(x[k] - 5, y[k] -5, 10, 10);
					}
				}
			}
		}
	}
}