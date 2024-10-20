
import swiftbot.Button;
import swiftbot.ImageSize;
import swiftbot.SwiftBotAPI;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import javax.imageio.ImageIO;


public class as3 {

	

	private static SwiftBotAPI API = new SwiftBotAPI();//declared as a private so it can be used w put constant redefining

	//api also doesnt permit repeated calling of api, produces error

	private static int[] colourToLightUpR = {255,0,0};
	private static int[] colourToLightUpB = {0,255,0};
	//colour format is RBG
	private static int[] colourToLightUpG = {0,0,255};
	private static int[] colourToLightUpY = {255,0,255};

	private static int tallyB = 0;
	private static int tallyG = 0;
	private static int tallyR = 0;
	private static int lightEncounter = 0;
	private static long startTime1 = System.nanoTime();
	private static long endTime1 = System.nanoTime();

	private static boolean isButtAPressed; //to create desired methods
	private static boolean isButtBPressed;
	private static boolean isButtXPressed;
	private static boolean isButtYPressed;

	public static void main(String[] args) throws IOException{
	
	int tallyR = 0;
	int tallyG = 0;
	int tallyB = 0;


	API.enableButton(Button.A, ()->{

	System.out.println("Button A has been pressed"); //print statement to include UI user 
	isButtAPressed = true;
	
	});


	//initialize other buttons 

	//B isnt intialized because it is not neeeded

	

	API.enableButton(Button.X, ()->{

	System.out.println("Button X has been pressed");

	isButtXPressed = true;

	try {
	pressedX(isButtAPressed, isButtAPressed, endTime1);
	} catch (IOException e) {
	e.printStackTrace();
	}

	API.stopMove();

	System.out.println("Do you want to display an execution log");
	System.out.println("Press Y for YES AND X for no");
	});

	API.enableButton(Button.Y, ()->{

	System.out.println("Button Y has been pressed");

	isButtYPressed = true;

	displayExecLog();

	});


	System.out.println("Press A to start program");

	for (int i = 0; i < 5; i++) {

	API.setButtonLight(Button.A, true); //flash buttA for 5sec

	//signals which butt to press

	try {	//additional functionality
	Thread.sleep(500);
	} catch (InterruptedException e) {
	e.printStackTrace();
	}

	API.setButtonLight(Button.A, false);

	try {
	Thread.sleep(500);
	} catch(InterruptedException e) {
	e.printStackTrace();
	}
  }

	while(true) {

	if(isButtAPressed == true) {

	long startTime = System.nanoTime();//nano time must be divided by one million to convert to seconds

	System.out.println("PROCEEDING");
	
	API.fillUnderlights(colourToLightUpY);

	System.out.println("Looking for traffic light!");
	break;
	}

	try {
	Thread.sleep(250);
	} catch (InterruptedException e) {System.out.println(e);}
	}
	
	if(isButtAPressed == true) {
	lowInitSpeed();
	}
}

	static String chosenCol = "";//string for colour decided 
	private static long startTime;


	public static void lowInitSpeed() throws IOException{

	API.fillUnderlights(colourToLightUpY);
	API.startMove(70,70);// lower speed to ensure ultrasound accuracy

	
	boolean continueMove = true;
	double disToTrafLight = 0;


	while(continueMove) {

	disToTrafLight = API.useUltrasound(); //when object within 15cm stop and get ready to decide image colour
	if (disToTrafLight < 20) {
	continueMove = false;
	API.stopMove();
	System.out.println("Current distance to traffic light is :" + disToTrafLight + "cm. Now deciding colour...");
	decideCol(continueMove, 0, 0, 0);
	break;

	}

	}

}


	public static void decideCol(boolean continueMove, int tallyG, int tallyB, int tallyR) throws IOException { //how to error handle if other obj other than traf light is deteced

	try {
	BufferedImage img = API.takeStill(ImageSize.SQUARE_48x48); //small resolution for faster computation
	System.out.println("Taking image");
	ImageIO.write(img, "jpg", new File("/home/pi/Documents/testImage.jpg")); //where image is stored as buffer
	} catch (Exception e) { // removed traditional exception handling commands and only included 'Exception e' to scan all 
	e.printStackTrace(); 

	System.out.println("PICTURE TAKEN, PROCESSING NOW");

	}

	//try diff image sizes in testing to find best time and space complexity

	String fn = ("/home/pi/Documents/testImage.jpg");

	BufferedImage img = ImageIO.read(new File(fn));

	int avgR = 0; //initializing colour variables	
	int avgG = 0; //^will change when updated from camera
	int avgB = 0;


	for(int x = 0;x<img.getWidth();++x)

	{

	for(int y=0;y<img.getHeight();++y)
	{
	int p = img.getRGB(y,x);


	int r = (p >> 16) & 0xFF;

	int g = (p >> 8) & 0xFF;

	int b = p & 0xFF;


	avgR += r;
	avgG += g;
	avgB += b;
	}
   }

	avgR /= (img.getWidth()*img.getHeight());
	avgG /= (img.getWidth()*img.getHeight());
	avgB /= (img.getWidth()*img.getHeight());


	System.out.println(avgR + "," + avgG + "," + avgB);

	if (avgR > avgG && avgR > avgB){

	System.out.println("The traffic light is red");
	String chosenCol = "Red";
	detectRed(chosenCol);
	}

	if (avgG > avgR && avgG > avgB){
	System.out.println("The traffic light is green");
	String chosenCol = "Green";
	
	detectGreen(chosenCol);
	}
	
	if (avgB > avgR && avgB > avgG){

	System.out.println("The traffic light is blue");
	String chosenCol = "Blue";

	detectBlue(chosenCol);

	 }	
	}
	
	//GREEN

	public static void detectGreen(String chosenCol) throws IOException {

	if (chosenCol.equals("Green")) { //uses equals() instead of == to compare current variable and not its reference

	API.fillUnderlights(colourToLightUpG);

	tallyG ++;
	lightEncounter ++;

	System.out.println("NOW IN GREEN STATE");

	System.out.println("Passing light wihtin the next 2 seconds...");

	try {
	Thread.sleep(1000);
	} catch (InterruptedException e) {
	e.printStackTrace();
	}

	API.startMove(100,100);

	try {	//move past traffic light in 2 seconds
	Thread.sleep(2000);
	} catch (InterruptedException e) {
	e.printStackTrace();
	}
	
	API.stopMove();
	}

	try {
	Thread.sleep(500); //last step of green lights
	} catch (InterruptedException e) {
	e.printStackTrace();
	}
	
	API.fillUnderlights(colourToLightUpY);
	//go back to low init speed
	lowInitSpeed();

	}

	//BLUE

	public static void detectBlue(String chosenCol) throws IOException {

	if (chosenCol.equals("Blue")) { 
	API.fillUnderlights(colourToLightUpB);
	tallyB ++;
	lightEncounter ++;
	System.out.println("NOW IN BLUE STATE");

	API.stopMove();

	try {
	Thread.sleep(500); //study guide says to stop for 1/2 second and then blink
	} catch (InterruptedException e) {
	e.printStackTrace();
	}

	API.disableUnderlights();

	try {
	Thread.sleep(400);
	} catch (InterruptedException e) {
	e.printStackTrace();
	}
	API.fillUnderlights(colourToLightUpB);

	try {
	Thread.sleep(400);
	} catch (InterruptedException e) {
	e.printStackTrace();
	}
	API.disableUnderlights();

	try {
	Thread.sleep(400);
	} catch (InterruptedException e1) {
	e1.printStackTrace();
	}

	API.fillUnderlights(colourToLightUpB); //unconventional light flashing method

	try {
	Thread.sleep(400);
	} catch (InterruptedException e11) {
	e11.printStackTrace();
	}

	API.disableUnderlights();

	try {
	Thread.sleep(400);
	} catch (InterruptedException e111) { //e1,e11,e111 are different exceptions being caught
	e111.printStackTrace();
	}

	API.fillUnderlights(colourToLightUpB);

	API.stopMove(); //get ready to turn 90 degrees to the L

	try {
	API.startMove(0, 50);
	Thread.sleep(1035);
    API.stopMove();
	} catch (InterruptedException e) {
	}

	//move forward for a second

	try {
	API.startMove(75, 75);
	Thread.sleep(1000);
	API.stopMove();
	} catch (InterruptedException e) {
	}

	//retrace 1 - move back

	try {
	API.startMove(-75, -75);
	Thread.sleep(1000);
	API.stopMove();
	} catch (InterruptedException e) {
	}

	//retrace rotate back to angle

	try {
	API.startMove(0, -50);
	Thread.sleep(1035);
	API.stopMove();

	} catch (InterruptedException e) {
		
	 }
	}
	lowInitSpeed();
   }

	//RED

	public static void detectRed(String chosenCol) throws IOException { //additional functionality to stop until traffic ahead is clear

	if (chosenCol.equals("Red")) { 

	API.fillUnderlights(colourToLightUpR);

	tallyR ++;
	lightEncounter ++;

	System.out.println("NOW IN RED STATE");

	API.stopMove();

	try {
	Thread.sleep(500);
	} catch (InterruptedException e) {
	e.printStackTrace();
	}

	boolean continueMoveR = true;

	while(continueMoveR) {

	double disToVeh = API.useUltrasound(); //when object within 15cm stop and get ready to decide image colour
	if (disToVeh < 10) {
	continueMoveR = false;

	API.stopMove();
	
	System.out.println("Path not clear...waiting");
	
	decideCol(continueMoveR, tallyR, tallyR, tallyR);
	break;

	}

	if(disToVeh >= 10) {
	continueMoveR = true;
	try {
	Thread.sleep(1000); //after vehicle not detected move forward for a second and continue
	} catch (InterruptedException e) {

	e.printStackTrace();
	 }
	}
	lowInitSpeed();

	}
  }
}
//if x is pressed	
	public static void pressedX(boolean isButtYPressed, boolean isButtXPressed, long endTime1) throws IOException {
	
	API.disableButton(Button.X);

	API.stopMove();

	System.out.println("Press Y to see execution log or X to write to text file");
	API.enableButton(Button.X, ()-> {
		System.out.println("You have chosen to write log to text file");
	});

	

	if(isButtYPressed == true) {
	System.out.println("Printing execution log...");
	displayExecLog();
	}

	

	else if(isButtXPressed == true) {
	logInTxtF(chosenCol, lightEncounter, lightEncounter, lightEncounter, chosenCol);
	System.out.println("Printed to text file");

	}
}

 public static void displayExecLog () { //press Y

	API.stopMove();
	long endTime = System.nanoTime();//divided by a billion to calculate run time in seconds

	long executionTimeMs = (endTime - startTime) / 1000000; //longs can not hold numbers greater than x value hence convert to million and then billion

	double executionTimeSec = (executionTimeMs) / 1000000000.0;
	int totalVisit = tallyR + tallyB + tallyG;
	
	String mostVisit = "";
	
	int encounter = 0;
	
	if (tallyR > tallyG && tallyR > tallyB){
	mostVisit = "Red";
	encounter = tallyR;
	}

	else if (tallyG > tallyR && tallyG > tallyB){
	mostVisit = "Green";
	encounter = tallyG;
	}

	else if (tallyB > tallyG && tallyB > tallyR){
	mostVisit = "Blue";
	encounter = tallyB;
	}

	

	else if (tallyR == tallyB && tallyR > tallyG) {
	System.out.println("Tally Red and Tally Blue are equal and greater than Tally Green");
	}

	else if (tallyR == tallyG && tallyR > tallyB) {
	System.out.println("Tally Red and Tally Green are equal and greater than Tally Blue");
	}

	else if (tallyB == tallyG && tallyB > tallyR) {
	System.out.println("Tally Blue and Tally Green are equal and greater than Tally Red");
	}

	System.out.println("The number of times the SwiftBot encountered traffic lights was: " + lightEncounter);
	System.out.println("The most frequent traffic light colour encountered was: " + mostVisit);
	System.out.println("The number of times " + mostVisit + " traffic lights were encountered was: " + encounter);
	System.out.println("The total duration of program execution is: " + executionTimeSec + " seconds"); 

	try {
	Thread.sleep(2000);
	} catch (InterruptedException e) {
	e.printStackTrace(); //2 second pause before closing program
	}
	System.exit(0);
	}

	public static void logInTxtF (String mostVisit, int executionTimeSec, int lightEncounter, int lightEncounter2, String encounter) { //press x

	try {
	BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
	writer.write("YOOOOOO The number of times the SwiftBot encountered traffic lights was: " + lightEncounter + "\n" +

	"The most frequent traffic light colour encountered was: " + mostVisit + "\n" +

	"The number of times " + mostVisit + " traffic lights were encountered was: " + encounter + "\n" +

	"The total duration of program execution is: " + executionTimeSec + " seconds\n");

	writer.close();
	
	} catch (IOException e) {

	e.printStackTrace();
	}
	
	System.out.println("Written to text file");
	}
}

	

	





	

	

	

	



