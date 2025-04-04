
/**
 *
 * @author ahmed_nada
 */
//public class Video {
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size; 
import org.opencv.highgui.HighGui;
import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import org.opencv.imgcodecs.*; // imread, imwrite, etc
import org.opencv.videoio.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import java.lang.Object;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.Object.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.embedded.windows.*;//Win64FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javax.swing.JPanel;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.Color;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.*;
import uk.co.caprica.vlcj.player.base.*;
import uk.co.caprica.vlcj.player.component.*;
import uk.co.caprica.vlcj.media.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.player.base.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import com.sun.jna.Native;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Tutorial {
    String filePath; 
    private JFrame frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public Tutorial(String filePath) {
        String vlcPath = "C:\\Program Files\\VideoLAN\\VLC";
        //NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        //System.loadLibrary(vlcPath);
        //Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        
        this.filePath = filePath;
        frame = new JFrame("Media Player");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().media().play(filePath);
    }
}


class VideoDecoder { // video cecoder class that prepares the video for the video shot/cut detector class
    int frame_number;
    int frames_per_second; // number of frames per second
    int video_length; // number of frames in the video
    String videoFilename; // file name
    VideoCapture camera;
    int intensityBins[] = new int[25]; // intensity bins used to calculate the distance matrix
    int intensityMatrix[][];
    double distance[]; // distance matrix
    double ts; // lower bound threshold
    double tb; // upper bound threshold
    int tor = 2; // number of values forgiven before a shot end candidate is decided

    public VideoDecoder(String videoFilename, int tor) {
        this.videoFilename = videoFilename;
        this.tor = tor;
        this.initialize(); // initilizing the videocapture
    }

    public void initialize() {
        if (!Paths.get(this.videoFilename).toFile().exists()) { // checking if file exists
            System.out.println("File " + this.videoFilename + " does not exist!");
            return;
        }
        camera = new VideoCapture(this.videoFilename); // open the video file

        if (!camera.isOpened()) {
            System.out.println("Error! Camera can't be opened!");
            return;
        } else {
            System.out.println("Video is open");
            video_length = (int) camera.get(Videoio.CAP_PROP_FRAME_COUNT); // getting video properties 
            frames_per_second = (int) camera.get(Videoio.CAP_PROP_FPS); // getting video properties 
            frame_number = (int) camera.get(Videoio.CAP_PROP_POS_FRAMES); // getting video properties 
            //System.out.println("Number of Frames: " + video_length);
            //System.out.println(frames_per_second + " Frames per Second");
            //System.out.println("Starting frame number is " + frame_number);
        }
    }

    public void terminate() {
        camera.release(); // release the video capture thread
    }

    public void seek(int i) {
        camera.set(Videoio.CAP_PROP_POS_FRAMES, i); // seek in the video by setting the current frame pointer at a certain position
        frame_number = (int) camera.get(Videoio.CAP_PROP_POS_FRAMES);
        //System.out.println("Current Frame number is " + frame_number);
    }

    public void writeIntensity() { // back ported method from assignment 2 to dump the intensity matrix into a file
        System.out.println("Intensity Matrix Columns length " + intensityMatrix[0].length);
        System.out.println("Intensity Matrix Rows length " + intensityMatrix.length);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < intensityMatrix.length; i++)//for each row
        {
            for (int j = 0; j < intensityMatrix[0].length; j++)//for each column
            {
                builder.append(intensityMatrix[i][j] + "");//append to the output string
                if (j < intensityMatrix.length - 1)//if this is not the last row element
                {
                    builder.append(",");//then add comma
                }
            }
            builder.append("\n");//append new line at the end of the row
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("intensity.txt")); // create and open a new file writer in a file named intensity.txt
            writer.write(builder.toString());//save the string representation of the board
            writer.close(); // close the buffered writer
            System.out.println("intensity.txt is closed and ready for parsing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getIntensity(BufferedImage image, int height, int width, int imageCount) { // backported method from assignmnet 2 to get the intensity bins
        //System.out.println("imageCount is " + imageCount);
        for (int x = 0; x < width; x = x + 1) { // nested for loop to go over each pixel in each image, going over width
            for (int y = 0; y < height; y = y + 1) { // going over height
                Color mycolor = new Color(image.getRGB(x, y)); // get RGB values in a new color object
                double I = (0.299 * mycolor.getRed()) + (0.587 * mycolor.getGreen()) + (0.114 * mycolor.getBlue()); // calculate the 
                //intensity of the pixel as per the equation in the assignment
                //System.out.println("Intensity is " + I + " for x=" + x + " and y=" + y); // print the intensity of each pixel
                if (I <= 10) { // if statements to create the intinsity bins, increement the bin value if the intensity falls into the bin intensity range
                    intensityBins[0]++;
                } else if (I <= 20) {
                    intensityBins[1]++;
                } else if (I <= 30) {
                    intensityBins[2]++;
                } else if (I <= 40) {
                    intensityBins[3]++;
                } else if (I <= 50) {
                    intensityBins[4]++;
                } else if (I <= 60) {
                    intensityBins[5]++;
                } else if (I <= 70) {
                    intensityBins[6]++;
                } else if (I <= 80) {
                    intensityBins[7]++;
                } else if (I <= 90) {
                    intensityBins[8]++;
                } else if (I <= 100) {
                    intensityBins[9]++;
                } else if (I <= 110) {
                    intensityBins[10]++;
                } else if (I <= 120) {
                    intensityBins[11]++;
                } else if (I <= 130) {
                    intensityBins[12]++;
                } else if (I <= 140) {
                    intensityBins[13]++;
                } else if (I <= 150) {
                    intensityBins[14]++;
                } else if (I <= 160) {
                    intensityBins[15]++;
                } else if (I <= 170) {
                    intensityBins[16]++;
                } else if (I <= 180) {
                    intensityBins[17]++;
                } else if (I <= 190) {
                    intensityBins[18]++;
                } else if (I <= 200) {
                    intensityBins[19]++;
                } else if (I <= 210) {
                    intensityBins[20]++;
                } else if (I <= 220) {
                    intensityBins[21]++;
                } else if (I <= 230) {
                    intensityBins[22]++;
                } else if (I <= 240) {
                    intensityBins[23]++;
                } else if (I <= 255) {
                    intensityBins[24]++;
                }
            }
        }
        //System.out.println("Insity Bin is \n" + Arrays.toString(intensityBins));
        for (int p = 0; p < 25; p++) { // for loop to copy the array into the intensity matrix
            intensityMatrix[imageCount][p] = intensityBins[p]; // copy the intensity bin array for the current image to its row in the bigger intensity matrix
            intensityBins[p] = 0; // reset the intensity matrix before running sgain on the next image
        }
    }

    public void getIntensityMatrix(int startFrame, int endFrame) { // method to go over the video frame by frame and calculate each frame's intensity 
        this.seek(startFrame); // set position of the vodeo at the start position
        this.intensityMatrix = new int[(endFrame - startFrame) + 1][25]; // create the intensity matrix
        //this.intensityMatrix = new int[501][25];
        Mat frame = new Mat();
        while (camera.read(frame)) //read frame by frame
        {
            try {
                BufferedImage image;
                if (frame_number >= startFrame && frame_number <= endFrame) {
                    Imgcodecs.imwrite("x.jpg", frame); // write the frame into an image
                    image = ImageIO.read(new File("x.jpg")); // read each image sequential
                    int width = image.getWidth(); // get picture width to be used at getIntensity and getcolorCode functions
                    int height = image.getHeight(); // get heigth 
                    getIntensity(image, height, width, frame_number - startFrame); // calculate intensity bin for the current frame
                    frame_number++; // increment frame counter                    
                } else {
                    break;
                }
            } catch (IOException e) {
                System.out.println("Error occurred when reading the file."); // printed if an exception is caught
            }
        }
//        System.out.println("Insity Matrix is \n");
//        for (int o = 0; o < (endFrame - 1000) + 1; o++) { // for loop to print the intensity matrix
//            //System.out.println(Arrays.toString(intensityMatrix[o]));
//        }
    }

    public void getDistance(int startFrame, int endFrame) { // function to calculate the distance between two consecutive frames, 2 consecutive intensity bins
        distance = new double[endFrame - startFrame];
        for (int row = 0; row < endFrame - startFrame; row = row + 1) {
            for (int column = 0; column < 25; column++) {
                double dist = ((intensityMatrix[row][column]) - (intensityMatrix[row + 1][column]));
                if (dist < 0) {
                    dist = dist * -1; // getting the modulus of the distance if -ve value then get the absolute +ve
                }
                distance[row] = distance[row] + dist; // piling up the distance from column to column
            }
            //System.out.println("distance of row " + row + " is " + distance[row]);
        }
    }

    public double getStandardDeviation(double[] distArray) { // function to calculate the standard deviation 
        int rows = distArray.length;
        //System.out.println("rows in STD function is " + rows);
        int columns = distArray.length;
        //double[] average = new double[columns];
        double average = getAverage(distArray);
        double std = 0;
        double stdd = 0;
        for (int p = 0; p < columns; p++) {
            //for (int q = 0; q < rows; q++) {
            stdd += Math.pow((distArray[p] - average), 2); // calculate the standard deviation
            //}
            //System.out.println("std is " + Math.sqrt(stdd / (rows - 1)));            
        }
        std = Math.sqrt(stdd / (rows - 1));
        System.out.println("std is " + std);
        return std;
    }

    public double calculateSD(double distArray[]) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = distArray.length;
        for (double num : distArray) {
            sum += num;
        }
        double mean = sum / length;
        for (double num : distArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        System.out.println("StandardDev is " + Math.sqrt(standardDeviation / length));
        return Math.sqrt(standardDeviation / length);
    }

    public double getAverage(double[] distArray) { // function to calculate the average
        //int rows = doubleArray.length;
        int columns = distArray.length;
        double average = 0;
        double sum = 0;
        for (int p = 0; p < columns; p++) {
            sum += distArray[p];
        }
        average = sum / columns;
        //System.out.println("Average is "+ average);
        return average;
    }

    public double getTb() { // function to calculate upper bound threshold Tb using the equation in the assignmnet
        double Tb = 0;
        //Tb = getStandardDeviation(distance);
        //Tb = getAverage(distance) + (calculateSD(distance)*11);
        Tb = getAverage(distance) + getStandardDeviation(distance) * 11; // average + 11 x STD
        //System.out.println("Tb is " + Tb);
        return Tb;
    }

    public double getTs() { // function to calculate the lower bound threshold Ts
        double Ts = 0;
        //Tb = getStandardDeviation(distance);
        //Tb = getAverage(distance) + (calculateSD(distance)*11);
        Ts = getAverage(distance) * 2; // as per the equation in the paper
        //System.out.println("Ts is " + Ts);
        return Ts;
    }

    public void identifyCuts(double[] distArray, int frameStart) { // function to identify the cuts in the video
        int columns = distArray.length;
        double tb = this.getTb();
        double ts = this.getTs();
        boolean potentialStartFound = false;
        boolean oneLessThanTsFound = false;
        int Ce = frameStart; // intializing 
        int Cs = frameStart; // initilizing
        int FsCandi = frameStart; // initilizing
        int FeCandi = frameStart;//initilizing
        for (int p = 0; p < columns; p++) { // go over the distance array if any value exceeds Tb then a cut is identified
            if (distArray[p] > tb) {
                Cs = p + frameStart;
                Ce = p + frameStart + 1;
                System.out.println("There is a cut start at " + Cs); // cut start is the end of the previous shot
                System.out.println("There is a cut end at " + Ce); // cut end is the start of the next shot
            }
        }
    }

    public ArrayList<Integer> getCuts(double[] distArray, int frameStart) { // same as prevoius function but outputs an array list of cuts
        ArrayList<Integer> cutList = new ArrayList<Integer>();
        int columns = distArray.length;
        double tb = this.getTb();
        double ts = this.getTs();
        boolean potentialStartFound = false;
        boolean oneLessThanTsFound = false;
        int Ce = 0 + frameStart;
        int Cs = 0 + frameStart;
        int FsCandi = 0 + frameStart;
        int FeCandi = 0 + frameStart;
        for (int p = 0; p < columns; p++) {
            if (distArray[p] > tb) {
                Cs = p + frameStart; // end of previous shot
                Ce = p + frameStart + 1; // first frame of new shot
                cutList.add(Cs);
                cutList.add(Ce);
                System.out.println("There is a cut start at " + Cs);
                System.out.println("There is a cut end at " + Ce);
            }
        }
        return cutList;
    }

    public ArrayList<Integer> getListGradualTransition(double[] distArray) { // function to get the gradual transition
        ArrayList<Integer> gradualTransitionList = new ArrayList<Integer>();
        int columns = distArray.length;
        this.tb = this.getTb(); // get Tb
        this.ts = this.getTs(); // get Ts
        int counter = tor; // value of number of distances below Ts to be forgiven 
        boolean potentialStartFound = false;
        int Ce = 1000;
        int Cs = 1000;
        int FsCandiInitial = 1000;
        int FeCandiInitial = 1000;
        int FeCandi = 1000;
        int FsCandi = 1000;
        double sum = 0;
        for (int p = 0; p <= columns; p++) {
            if (p == columns) {
                if (counter == 0) {
                    FeCandi = FeCandiInitial + p - tor - 1;
                    if (sum > tb && potentialStartFound == true) {
                        System.out.println("Fs+1 is " + FsCandi + " and Found a real end for the Candidate Start at " + FeCandi);
                        gradualTransitionList.add(FsCandi);
                        gradualTransitionList.add(FeCandi);
                    }
                } else {
                    System.out.println("Hit the end of distance Array");
                }
            } else if (distArray[p] > ts && distArray[p] < tb && potentialStartFound == false) { // mark a potential candidate if no value between Tb and Ts and no current candidate
                FsCandi = FsCandiInitial + p; // set Candidate star
                //System.out.println("found a value between ts and tb");
                //System.out.println("Found a potential Candidate Start at " + FsCandi);
                potentialStartFound = true;
                counter = tor; // set counter to Tor
                sum = sum + distArray[p]; // increment sum
                continue;
            } else if ((distArray[p] > tb) && potentialStartFound == true) { // hit a cut case
                if (distArray[p - 1] < ts) { // needs to be a for loop to go back tor times and check if any values are less than ts
                    sum = sum - distArray[p - 1]; // check if value before cut is less than Ts or not, if less than Ts, then decrement Sum
                    FeCandi = FeCandiInitial + p - 1; // decrement end by 2
                } else {
                    FeCandi = FeCandiInitial + p; // otherwise set Fe to Fe initial + p 
                }
                if (sum > tb && potentialStartFound == true) { // check if the sum us more or less than Tb to decide on a candidate
                    FsCandi = FsCandi + 1;
                    System.out.println("Fs+1 is " + FsCandi + " and Found a real end for the Candidate Start at " + FeCandi);
                    gradualTransitionList.add(FsCandi); // add Fs and Fe into the Array List
                    gradualTransitionList.add(FeCandi);
                }
                potentialStartFound = false; // reset if potiential candidate to false
                sum = 0; // resrt sum value to 0
                //break;
            } else if (distArray[p] < ts && potentialStartFound == true) { // if distance value is less thsn Ts
                //System.out.println("Decriminting counter and adding to sum");
                sum = sum + distArray[p]; // add sitance to sum
                counter--; // decrement coutner
                if (counter == 0) { // check if the counter exhaused Tor or not, if it is 0 then we must stop
                    //System.out.println("counter is 0, have to stop here and decrement the previous distance value");
                    FeCandi = FeCandiInitial + p; // set Fe
                    //System.out.println("Decrementing last value");
                    sum = sum - distArray[p]; // decrement sum
                    //System.out.println("Decrementing second last value");
                    if (distArray[p - 1] < ts) { // check on the previous value if it is as well less than Ts
                        sum = sum - distArray[p - 1]; // if less than Ts .. remove its distance from sum
                        FeCandi = FeCandi - 1; // update Fe
                    }
                    if (sum > tb && potentialStartFound == true) { // candiate check whether more or less than Tb 
                        FsCandi = FsCandi + 1;
                        System.out.println("Fs+1 is " + FsCandi + " and Found a real end for the Candidate Start at " + FeCandi);
                        gradualTransitionList.add(FsCandi);
                        gradualTransitionList.add(FeCandi);
                    }
                    potentialStartFound = false; // reset candidate
                    sum = 0; // reset sum
                }
            } else if (distArray[p] > ts && potentialStartFound == true && counter >= 1) { // if value is more than Ts
                sum = sum + distArray[p]; // add diatance value to sum
                if (distArray[p] > ts) {
                    counter = tor; // reset counter
                }
            }
        }
        return gradualTransitionList;
    }

    public static BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public void readIntensityFile(int startFrame, int endFrame) { // read the intesity.txt file function
        System.out.println("Reading intensity.txt");
        this.intensityMatrix = new int[(endFrame - startFrame) + 1][25];
        Scanner read;
        String line = "";
        int lineNumber = 0;
        try {
            //System.out.println("Inside try");
            read = new Scanner(new File("intensity.txt")); // a new scanner object to read the intensity.txt
            while (read.hasNextLine()) { // while loop the scanner object has another line do the following 
                // System.out.println("Inside while loop");
                line = read.nextLine(); // read the following line
                String[] cols = line.split(","); // create a string array filled with the intensity bins after removing the comma separator 
                //System.out.println(Arrays.toString(cols));
                for (int j = 0; j < 25; j++) {
                    intensityMatrix[lineNumber][j] = Integer.parseInt(cols[j]); // parse the string value as integer and then add them in intensity matrix 
                }
                lineNumber++;
            }

            //Print the intensity Matrix
            //System.out.println("Reconstructed Intensity Matrix");
            for (int o = 0; o < 100; o++) {
                //System.out.println(Arrays.toString(intensityMatrix[o])); // print the intensity matrix
            }
        } catch (FileNotFoundException EE) { // catch statement if the intensity.txt file was not created previously by running the readImage class functions
            System.out.println("The file intensity.txt does not exist");
        }
    }
}

public class Video extends JFrame { // class backported from Assignment 2 but had some modifications 
    VideoCapture camera;
    //String videoFilename;
    private JLabel photographLabel = new JLabel();  // new jlabel  
    private JButton[] button; //creates an array of JButtons for images
    private int[] buttonOrder = new int[101]; //creates an array to keep up with the image order
    private int[] selectedImages = new int[99];
    private double[] imageSize = new double[100]; //keeps up with the image sizes
    private GridLayout gridLayout1;
    private GridLayout gridLayout2;
    private GridLayout gridLayout3;
    private GridLayout gridLayout4;
    private JPanel panelBottom1;
    private JPanel panelBottom2;
    private JPanel panelTop;
    private JPanel buttonPanel;
    //private int[][] intensityMatrix = new int[100][25]; // intensity matrix that will be used to copy the data from text file
    //private int[][] colorCodeMatrix = new int[100][64]; // color code matrix that will be used to copy the data from text file
    //private double[][] colorCodeIntensityMatrix = new double[100][89];
    
    int picNo = 0; // variable to hold the pic/image index
    int imageCount = 1; //keeps up with the number of images displayed since the first page.
    int selectedImagesIndex;
    int pageNo = 1; // variable to hold the page number
    int iteration = -1;
    JButton previousPage;
    JButton nextPage;
    JCheckBox checkbox; // relevance feedback check box
    JCheckBox iconCheckBox; // check box for each image in the first page
    VideoDecoder vdec;
    ArrayList<Integer> gradualTransitionList = new ArrayList<Integer>(); // array lists to populate the gradual transiotn frames
    ArrayList<Integer> cutList = new ArrayList<Integer>(); // array list to populate the frame cuts
    //String videoFilename;
    //VideoCapture camera;

    public Video(String videoFilename) {
        String filePath = "C:\\Users\\ahmed_nada\\Desktop\\UW GCSDD\\CSS 584 - Multimedia Database\\Assignements\\Assignment 4\\20020924_juve_dk_02a.avi";
//        camera = new VideoCapture(filePath);
//
//        if (!camera.isOpened()) {
//            System.out.println("Error! Camera can't be opened!");
//            return;
//        }
//        else{
//            System.out.println("Video is now open");
//        }
//        
//        play(); // method i was trying to use to play te file as frames but it only works if i cal lit from main
        
        vdec = new VideoDecoder(videoFilename, 2); // create the new video decoder
        
        //vdec.getIntensityMatrix(1000, 4999); // uncoimment this to calculate the intenstiy matrix 
        //vdec.writeIntensity(); // dump the intensity matrix into file
        vdec.readIntensityFile(1000, 4999); // read the dumped intensity matrix and uncomment the above 2 lines
        vdec.getDistance(1000, 4999); // get the distance matrix based on the given intenstiy matrix
        this.cutList = vdec.getCuts(vdec.distance, 1000); // get the cut statrt and end
        this.gradualTransitionList = vdec.getListGradualTransition(vdec.distance); // get the gradual transition statrt and end
        //this.gradualTransitionList = new ArrayList<Integer>(
        //Arrays.asList(1866, 1866, 2406, 2421, 2676, 2677, 3200, 3200, 3551, 3569, 3618, 3637, 3765, 3774, 3838, 3849, 3928, 3941, 4042, 4048, 4299, 4299, 4561, 4580, 4607, 4620, 4776, 4789, 4986, 4994));
        System.out.println("Cuts list is:");
        System.out.println(Arrays.toString(this.cutList.toArray()));
        System.out.println("Gradual Transition list is:");
        System.out.println(Arrays.toString(this.gradualTransitionList.toArray()));

        //The following lines set up the interface including the layout of the buttons and JPanels.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Video Shot Detection");
        panelBottom1 = new JPanel();
        panelBottom2 = new JPanel();
        panelTop = new JPanel();
        buttonPanel = new JPanel();
        gridLayout1 = new GridLayout(4, 5, 5, 5);
        gridLayout2 = new GridLayout(2, 1, 5, 5);
        gridLayout3 = new GridLayout(1, 2, 5, 5);
        gridLayout4 = new GridLayout(2, 3, 5, 5);
        setLayout(gridLayout2);
        panelBottom1.setLayout(gridLayout1);
        panelBottom2.setLayout(gridLayout1);
        panelTop.setLayout(gridLayout3);
        add(panelTop);
        add(panelBottom1);
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(gridLayout4);
        panelTop.add(photographLabel);
        panelTop.add(buttonPanel);
        checkbox = new JCheckBox("Cuts"); // Check Box to enable the simplified relevance feedback
        previousPage = new JButton("Previous Page"); // previous 20 images
        nextPage = new JButton("Next Page"); // next 20 images
        buttonPanel.add(checkbox); // add the relevance feedback checkbox
        nextPage.addActionListener(new nextPageHandler()); // setting action for each button using ActionListener
        previousPage.addActionListener(new previousPageHandler());
        //intensity.addActionListener(new intensityHandler());
        //colorCode.addActionListener(new colorCodeHandler());
        //colorCodeIntensity.addActionListener(new colorCodeIntensityHandler()); // action listener for the colorCodeIntensity
        //refresh.addActionListener(new refreshHandler());
        checkbox.addActionListener(new checkboxHandler());
        //iconCheckBox.addActionListener(new IconCheckBoxHandler());
        setSize(1100, 750);
        // this centers the frame on the screen
        setLocationRelativeTo(null);

        button = new JButton[(this.gradualTransitionList.size() / 2) + (this.cutList.size() / 2) +1]; // JButton array to hold all the cuts and gradual transition frames
        System.out.println("Size of button array is " + button.length); // so far i have got 28 Cuts + gradual transions
        /*This for loop goes through the images in the database and stores them as icons and adds
         * the images to JButtons and then to the JButton array
         */
        Mat frame = new Mat();
        BufferedImage image1;
        //JLabel vidpanel = new JLabel();
        //JFrame jframe = new JFrame("Title");
        //jframe.setContentPane(vidpanel);
        //jframe.setVisible(true);
        //System.out.println("First ITem is " + this.gradualTransitionList.get(0));
        for (int i = 0; i < this.gradualTransitionList.size(); i = i + 2) { // loop to go over the gradual transition frames
            int y = this.gradualTransitionList.get(i); // get the frame number of the gradual transition from the array list one by one (increment by 2 to hop over the Fe)
            vdec.seek(y); // seek the video to the frame number
            vdec.camera.read(frame); // read the frame 
            Imgcodecs.imwrite(y + ".jpg", frame); // write frame into image
            try {
                image1 = ImageIO.read(new File(y + ".jpg")); // read the image into buffered image
            } catch (IOException e) {
                System.out.println("Error occurred when reading the file."); // printed if an exception is caught
            }
            ImageIcon icon = new ImageIcon(y + ".jpg"); // create icon of the image
            if (icon != null) {
                int m = (i / 2) + 1;
                //System.out.println("i is " + i);
                //System.out.println("m is " + m);
                button[m] = new JButton(icon); // add the image icon in the button array
                button[m].addActionListener(new IconButtonHandler(m, icon, this.gradualTransitionList.get(i), this.gradualTransitionList.get(i + 1))); // add action listener to the Jbutton
                buttonOrder[m] = m; // add the index of each button in the button order array that will be used later on for image preview
            }
            //vidpanel.setIcon(imagei);
            //vidpanel.repaint();
        }
               
        for (int i = 0; i < this.cutList.size()-2; i = i + 2) { // loop to go over tthe cut frames
            int y = this.cutList.get(i);
            //System.out.println("will seek to " + y);
            vdec.seek(y); 
            vdec.camera.read(frame);
            Imgcodecs.imwrite(y + ".jpg", frame);
            try {
                image1 = ImageIO.read(new File(y + ".jpg"));
            } catch (IOException e) {
                System.out.println("Error occurred when reading the file."); // printed if an exception is caught
            }
            ImageIcon icon = new ImageIcon(y + ".jpg");
            if (icon != null) {
                int m = (i / 2) + 1+(this.gradualTransitionList.size()/2);
                //System.out.println("i is " + i);
                //System.out.println("m is " + m);
                button[m] = new JButton(icon); // add the image icon in the button array
                if (i ==0){
                    button[m].addActionListener(new IconButtonHandlerCut(m, icon, 1000, this.cutList.get(i))); // if this is the first cut then the shot maybe started at the first frame #1000
                }else{
                    //int z=
                    //System.out.println("Creating button handler cut for frame start at "+ this.cutList.get(i) + " and ends at " + (this.cutList.get(i + 2)));
                    button[m].addActionListener(new IconButtonHandlerCut(m, icon, this.cutList.get(i), (this.cutList.get(i + 2)-1))); // Frame start is at end and Frame end is at the next frame start -1
                }
                buttonOrder[m] = m; // add the index of each button in the button order array that will be used later on for image preview
            }
            //vidpanel.setIcon(imagei);
            //vidpanel.repaint();
        }
        System.out.println("done");
        displayFirstPage(); // display first page method which shows the gradual transiton image icons
    }
    
    public static BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
    
    public void play(){ // function to play the video that i have been trying to get it to work but it only works if called from main 
    Mat frame = new Mat();
    VideoCapture camera = this.camera;
//    String filePath = "C:\\Users\\ahmed_nada\\Desktop\\UW GCSDD\\CSS 584 - Multimedia Database\\Assignements\\Assignment 4\\20020924_juve_dk_02a.avi";
//    VideoCapture camera = new VideoCapture(filePath);
    JFrame jframe = new JFrame("Title");
    jframe.setSize(1100, 750);
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JLabel vidpanel = new JLabel();
    jframe.setContentPane(vidpanel);
    jframe.setVisible(true);
        while (true) {
            if (camera.read(frame)) {
                ImageIcon image = new ImageIcon(Mat2BufferedImage(frame));
                vidpanel.setIcon(image);
                vidpanel.repaint();
            }
        }
    }
    
    private void displayFirstPage() { // function to display the gradual transition page
        imageCount = 1;
        pageNo = 1;
        previousPage.setEnabled(false); // in the firstp age, previous page should be disabled
        nextPage.setEnabled(true); // next page is enabled
        int imageButNo = 0;
        panelBottom1.removeAll(); // refresh the bottom panel
        int u = 0;
        for (int i = 1; i <= 15; i++) {
            imageButNo = buttonOrder[i]; // get the image button number from the buttonOrder array
            System.out.println("Image button number is " + imageButNo);
            panelBottom1.add(button[imageButNo]); // add the button of imageButton number to the bottom panel
            int p = this.gradualTransitionList.get(u);
            //JLabel label1 = new JLabel(imageButNo + ".jpg"); // add a label for the button
            JLabel label1 = new JLabel(p + ".jpg"); // add a label for the button            
            label1.setForeground(Color.BLACK); // set the color to balck
            label1.setFont(new Font("SansSerif", Font.BOLD, 28)); // set the font type and font size
            panelBottom1.add(label1); // add the label to the bottom panel for each new button
            imageCount++; // increment the image count
            u = u + 2;
        }
        panelBottom1.revalidate(); // revalidate and repaint the bottom panel
        panelBottom1.repaint();
    }
    
    private void displayFirstPageCB() { // function to display the cuts
        imageCount = 1;
        pageNo = 1;
        previousPage.setEnabled(false); // in the firstp age, previous page should be disabled
        nextPage.setEnabled(true); // next page is enabled
        int imageButNo = 0;
        panelBottom1.removeAll(); // refresh the bottom panel
        int u=1;
        for (int i = 16; i < 26; i++) {
            imageButNo = buttonOrder[i]; // get the image button number from the buttonOrder array
            System.out.println("Image button number is " + imageButNo);
            panelBottom1.add(button[imageButNo]); // add the button of imageButton number to the bottom panel
            int p = this.cutList.get(u);
            //JLabel label1 = new JLabel(imageButNo + ".jpg"); // add a label for the button
            JLabel label1 = new JLabel(p + ".jpg"); // add a label for the button            
            label1.setForeground(Color.BLACK); // set the color to balck
            label1.setFont(new Font("SansSerif", Font.BOLD, 28)); // set the font type and font size
            panelBottom1.add(label1); // add the label to the bottom panel for each new button
            imageCount++; // increment the image count
            u = u + 2;
        }
        panelBottom1.revalidate(); // revalidate and repaint the bottom panel
        panelBottom1.repaint();
    }
    
    private class checkboxHandler implements ActionListener { // the check box handler just to show either the gradual transion once the cuts check box is selected it will show the cuts

        public void actionPerformed(ActionEvent e) {
            if (checkbox.isSelected()) {
                //System.out.println("Selected");
                if (pageNo == 1) {
                    displayFirstPageCB(); // show the cuts page
                }
                //flushSelectedImages();

            } else {
                //System.out.println("Unselected");
                if (pageNo == 1) {
                    displayFirstPage(); // else show the gradual transitions page
                }
                //flushSelectedImages();
            }
        }
    }

    private class IconButtonHandler implements ActionListener { // the icon button handler is supposed to open a new JFrame and display the frames of the gradual transion one by one
        // however for some reason if it is not working as expected and the photograph panel does not re-paint to show the new frame so i decided to just let it accumulate the frames next
        // to each other on the frame and finally display the frame with all shots on that one Frame
        JLabel vidpanel;// = new JLabel();
        JFrame jframe;// = new JFrame("Video");
        int pNo = 0;
        ImageIcon iconUsed;
        ImageIcon imagei;
        ImageIcon icon;
        Mat frame;// = new Mat();
        BufferedImage image1;
        int frameStart;
        int frameEnd;

        IconButtonHandler(int i, ImageIcon j, int s, int e) {
            pNo = i; // variable to store the image number when clicked
            iconUsed = j;  //sets the icon to the one used in the button
            frameStart = s;
            frameEnd = e;
            //photographLabel.repaint();
            //photographLabel.setVisible(true);
            jframe = new JFrame("Video for gradual transiotion starting at " + s);
//            vidpanel = new JLabel();
            frame = new Mat();
            //icon = new ImageIcon();
            //panelTop.add(videoLabel);
        }

        public BufferedImage Mat2BufferedImage(Mat m) { // function that trnasforms the mat into buffered image
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return image;
        }

        public void displayImage(BufferedImage img) {
            //jframe = new JFrame("Video for gradual transiotion starting at " + frameStart);
            vidpanel = new JLabel();
            //frame = new Mat();
            /*ImageIcon*/ //icon = new ImageIcon(img);
            icon = new ImageIcon(img);
            //JFrame frame = new JFrame();
            jframe.setLayout(new FlowLayout());
            jframe.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
            //JLabel lbl = new JLabel();
            vidpanel.setIcon(icon);
            jframe.add(vidpanel);
            jframe.revalidate();
            jframe.repaint();
            //jframe.setVisible(true);
            try {
                Thread.sleep(500);
//                jframe.revalidate();
//                jframe.repaint();
//                jframe.setVisible(true);
                //jframe.setVisible(true);
            } catch (InterruptedException u) {
                u.printStackTrace();
            }
            //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            //frame.setVisible(false); //you can't see me!
            jframe.setVisible(true);
            //jframe.dispose(); //Destr
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        public void actionPerformed(ActionEvent e) {
            photographLabel.setIcon(iconUsed);
            //Mat frame = new Mat();
            //VideoCapture camera = new VideoCapture("C:/MyName/MyPc/Desktop/TheLordOfTheRings.mp4");
            VideoCapture camera = vdec.camera;
            //JFrame jframe = new JFrame("MyTitle");
            //jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //JLabel vidpanel = new JLabel();
            //jframe.setContentPane(vidpanel);
            //jframe.setVisible(true);
            int counter = 0;
            //while (counter < 5) {
            System.out.println("Frame Start is " + frameStart);
            System.out.println("Frame End is " + frameEnd);
            vdec.camera.set(Videoio.CAP_PROP_POS_FRAMES, frameStart);
            int frame_number = (int) vdec.camera.get(Videoio.CAP_PROP_POS_FRAMES);
            System.out.println("Frame number is set to " + frame_number);
            Mat frame = new Mat();
            BufferedImage image1;
            while (frameStart <= frameEnd) { // while loop to read all frames from gradual transioton start till gradual transdition end
                System.out.println("Current frame playing is " + frameStart); 
                if (camera.read(frame)) { // read frame by frame
                    //System.out.println("playing");
                    //image1 = this.Mat2BufferedImage(frame);
                    ImageIcon image = new ImageIcon(this.Mat2BufferedImage(frame)); // transform the mat frame into icon
                    //vidpanel.setIcon(image);
                    //vidpanel.revalidate();
                    //vidpanel.repaint();
                    image1 = this.Mat2BufferedImage(frame);
                    this.displayImage(image1); // display the image
                }
                //counter++;
                frameStart++; // increment the frame start
            }
            //}
        }
    }
    
    private class IconButtonHandlerCut implements ActionListener {

        JLabel vidpanel;// = new JLabel();
        JFrame jframe;// = new JFrame("Video");
        int pNo = 0;
        ImageIcon iconUsed;
        ImageIcon imagei;
        ImageIcon icon;
        Mat frame;// = new Mat();
        BufferedImage image1;
        int frameStart;
        int frameEnd;

        IconButtonHandlerCut(int i, ImageIcon j, int s, int e) {
            pNo = i; // variable to store the image number when clicked
            iconUsed = j;  //sets the icon to the one used in the button
            frameStart = s;
            frameEnd = e;
            //photographLabel.repaint();
            //photographLabel.setVisible(true);
            jframe = new JFrame("Video for Cuts starting at " + frameStart + "and ends at "+ frameEnd);
//            vidpanel = new JLabel();
            frame = new Mat();
            //icon = new ImageIcon();
            //panelTop.add(videoLabel);
        }

        public BufferedImage Mat2BufferedImage(Mat m) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return image;
        }

        public void displayImage(BufferedImage img) {
            //jframe = new JFrame("Video for gradual transiotion starting at " + frameStart);
            vidpanel = new JLabel();
            //frame = new Mat();
            /*ImageIcon*/ //icon = new ImageIcon(img);
            icon = new ImageIcon(img);
            //JFrame frame = new JFrame();
            jframe.setLayout(new FlowLayout());
            jframe.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
            //JLabel lbl = new JLabel();
            vidpanel.setIcon(icon);
            jframe.add(vidpanel);
            jframe.revalidate();
            jframe.repaint();
            //jframe.setVisible(true);
            try {
                Thread.sleep(500);
//                jframe.revalidate();
//                jframe.repaint();
//                jframe.setVisible(true);
                //jframe.setVisible(true);
            } catch (InterruptedException u) {
                u.printStackTrace();
            }
            //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            //frame.setVisible(false); //you can't see me!
            jframe.setVisible(true);
            //jframe.dispose(); //Destr
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        public void actionPerformed(ActionEvent e) {
            photographLabel.setIcon(iconUsed);
            //Mat frame = new Mat();
            //VideoCapture camera = new VideoCapture("C:/MyName/MyPc/Desktop/TheLordOfTheRings.mp4");
            VideoCapture camera = vdec.camera;
            //JFrame jframe = new JFrame("MyTitle");
            //jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //JLabel vidpanel = new JLabel();
            //jframe.setContentPane(vidpanel);
            //jframe.setVisible(true);
            int counter = 0;
            //while (counter < 5) {
            System.out.println("Frame Start is " + frameStart);
            System.out.println("Frame End is " + frameEnd);
            vdec.camera.set(Videoio.CAP_PROP_POS_FRAMES, frameStart);
            int frame_number = (int) vdec.camera.get(Videoio.CAP_PROP_POS_FRAMES);
            System.out.println("Frame number is set to " + frame_number);
            Mat frame = new Mat();
            BufferedImage image1;
            while (frameStart <= frameEnd) {
                System.out.println("Current frame playing is " + frameStart);
                if (camera.read(frame)) {
                    //System.out.println("playing");
                    //image1 = this.Mat2BufferedImage(frame);
                    ImageIcon image = new ImageIcon(this.Mat2BufferedImage(frame));
                    //vidpanel.setIcon(image);
                    //vidpanel.revalidate();
                    //vidpanel.repaint();
                    image1 = this.Mat2BufferedImage(frame);
                    this.displayImage(image1);
                }
                //counter++;
                frameStart++;
            }
            //}
        }
    }

    private class IconButtonHandler2 implements ActionListener { // failed attempts to let the JFrame display all frames one by one

        JLabel vidpanel;// = new JLabel();
        JFrame jframe;// = new JFrame("Video");
        JPanel panel;
        int pNo = 0;
        ImageIcon iconUsed;
        ImageIcon imagei;
        ImageIcon icon;
        Mat frame;// = new Mat();
        BufferedImage image1;
        int frameStart;
        int frameEnd;

        IconButtonHandler2(int i, ImageIcon j, int s, int e) {
            pNo = i; // variable to store the image number when clicked
            iconUsed = j;  //sets the icon to the one used in the button
            frameStart = s;
            frameEnd = e;
            //photographLabel.repaint();
            //photographLabel.setVisible(true);
            jframe = new JFrame("Video for gradual transiotion starting at " + s);
            //jframe.setVisible();
            //.setVisible();
            vidpanel = new JLabel();
            panel = new JPanel();
            frame = new Mat();
            //icon = new ImageIcon();
            //panelTop.add(videoLabel);
        }

        public BufferedImage Mat2BufferedImage(Mat m) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return image;
        }

        public void displayImage(BufferedImage img) {
            //jframe = new JFrame("Video for gradual transiotion starting at " + frameStart);
            //vidpanel = new JLabel();
            //frame = new Mat();
            /*ImageIcon*/ //icon = new ImageIcon(img);
            icon = new ImageIcon(img);
            System.out.println("playing-4");
            //JFrame frame = new JFrame();
            jframe.setLayout(new FlowLayout());

            jframe.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
            //JLabel lbl = new JLabel();
            vidpanel.setIcon(icon);
            System.out.println("playing-5");
            //jframe.add(vidpanel);
            panel.add(vidpanel);
            jframe.add(panel);
            System.out.println("playing-6");
            vidpanel.setVisible(true);

            //jframe.setVisible(true);
            //jframe.revalidate();
            //jframe.repaint();
            vidpanel.revalidate();
            vidpanel.repaint();
            panel.repaint();
            panel.revalidate();
            panel.setVisible(true);

            jframe.repaint();
            jframe.setVisible(true);
            //jframe.setVisible(true);
            //vidpanel.setVisible(true);
            try {
                Thread.sleep(500);
//                jframe.revalidate();
//                jframe.repaint();
//                jframe.setVisible(true);
                //jframe.setVisible(true);
            } catch (InterruptedException u) {
                u.printStackTrace();
            }
            //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            //frame.setVisible(false); //you can't see me!
            vidpanel.setVisible(true);
            //jframe.dispose(); //Destr
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        public void actionPerformed(ActionEvent e) {
            //Mat frame = new Mat();
            //VideoCapture camera = new VideoCapture("C:/MyName/MyPc/Desktop/TheLordOfTheRings.mp4");
            VideoCapture camera = vdec.camera;
            //JFrame jframe = new JFrame("MyTitle");
            //jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //JLabel vidpanel = new JLabel();
            //jframe.setContentPane(vidpanel);
            jframe.setVisible(true);
            int counter = 0;
            //while (counter < 5) {
            System.out.println("Frame Start is " + frameStart);
            System.out.println("Frame End is " + frameEnd);
            vdec.camera.set(Videoio.CAP_PROP_POS_FRAMES, frameStart);
            int frame_number = (int) vdec.camera.get(Videoio.CAP_PROP_POS_FRAMES);
            System.out.println("Frame number is set to " + frame_number);
            //Mat frame = new Mat();
            BufferedImage image1;

            while (frameStart <= frameEnd) {
                System.out.println("Current frame playing is " + frameStart);
                if (camera.read(frame)) {
                    System.out.println("playing");
                    //image1 = this.Mat2BufferedImage(frame);
                    ImageIcon image = new ImageIcon(this.Mat2BufferedImage(frame));
                    System.out.println("playing-2");
                    //vidpanel.setIcon(image);
                    //vidpanel.revalidate();
                    //vidpanel.repaint();
                    image1 = this.Mat2BufferedImage(frame);
                    System.out.println("playing-3");
                    this.displayImage(image1);
                    vidpanel.revalidate();
                    vidpanel.repaint();
                    jframe.setContentPane(vidpanel);
                    jframe.repaint();
                    System.out.println("playing-10");
                    //jframe.setVisible();

                }
                //counter++;
                frameStart++;
            }
        }
    }

    private class IconButtonHandler3 implements ActionListener { // failed attempt to let the Jframe play rhe frames one by one

        JLabel vidpanel;// = new JLabel();
        JLabel photographLabel;
        JFrame jframe;// = new JFrame("Video");
        int pNo = 0;
        ImageIcon iconUsed;
        ImageIcon imagei;
        ImageIcon icon;
        Mat frame;// = new Mat();
        BufferedImage image1;
        int frameStart;
        int frameEnd;

        IconButtonHandler3(int i, ImageIcon j, int s, int e) {
            pNo = i; // variable to store the image number when clicked
            iconUsed = j;  //sets the icon to the one used in the button
            frameStart = s;
            frameEnd = e;
            //photographLabel.repaint();
            //photographLabel.setVisible(true);
            //jframe = new JFrame("Video for gradual transiotion starting at " + s);
            photographLabel = new JLabel();
            frame = new Mat();
            //icon = new ImageIcon();
            //panelTop.add(videoLabel);
        }

        public BufferedImage Mat2BufferedImage(Mat m) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return image;
        }

        public void displayImage(BufferedImage img) {
            //jframe = new JFrame("Video for gradual transiotion starting at " + frameStart);
            vidpanel = new JLabel();
            //frame = new Mat();
            /*ImageIcon*/ //icon = new ImageIcon(img);
            icon = new ImageIcon(img);
            //JFrame frame = new JFrame();
            jframe.setLayout(new FlowLayout());
            jframe.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
            //JLabel lbl = new JLabel();
            vidpanel.setIcon(icon);
            jframe.add(vidpanel);
            jframe.revalidate();
            jframe.repaint();
            //jframe.setVisible(true);
            try {
                Thread.sleep(500);
//                jframe.revalidate();
//                jframe.repaint();
//                jframe.setVisible(true);
                //jframe.setVisible(true);
            } catch (InterruptedException u) {
                u.printStackTrace();
            }
            //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            //frame.setVisible(false); //you can't see me!
            jframe.setVisible(true);
            //jframe.dispose(); //Destr
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        public void actionPerformed(ActionEvent e) {
            photographLabel.setIcon(iconUsed);
            //jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //vidpanel = new JLabel();
            //jframe.setContentPane(photographLabel);
            //jframe.setVisible(true);
            //picNo = pNo; // set the picNo variable to the current icon image
            System.out.println("Frame Start is " + frameStart);
            System.out.println("Frame End is " + frameEnd);
            vdec.camera.set(Videoio.CAP_PROP_POS_FRAMES, frameStart);
            int frame_number = (int) vdec.camera.get(Videoio.CAP_PROP_POS_FRAMES);
            System.out.println("Frame number is set to " + frame_number);
            //Mat frame = new Mat();
            BufferedImage image1;
            while (frameStart <= frameEnd) {
                System.out.println("Current frame playing is " + frameStart);
                if (vdec.camera.read(frame)) {
                    Imgcodecs.imwrite("z.jpg", frame);
                    try {
                        image1 = ImageIO.read(new File("z.jpg"));
                        imagei = new ImageIcon(image1);
                        //vidpanel.setIcon(imagei);
                        //jframe.setContentPane(vidpanel);
                        photographLabel.setIcon(imagei);
                        add(panelTop);
                        add(panelBottom1);
                        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
                        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
                        photographLabel.setHorizontalAlignment(JLabel.CENTER);
                        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                        panelTop.add(photographLabel);
                        panelTop.add(buttonPanel);
                        //panelTop.add(photographLabel);
                        //panelTop.add(buttonLabel);
                        //getContentPane().add(photographLabel);
                        //setContentPane(photographLabel);
//                         photographLabel.revalidate();
//                         photographLabel.repaint();
//                         panelTop.revalidate();
//                         panelTop.repaint();
                        //jframe.setVisible(true);
                        //vidpanel.repaint();
                        frameStart++;
//                        photographLabel.setIcon(imagei);
//                        
//                        photographLabel.repaint();
//                        photographLabel.validate();
//                       photographLabel.setVisible(true);
//                        frameStart++;
                        //java.util.concurrent.TimeUnit.SECONDS.sleep(2);
                        try {
                            panelTop.setVisible(true);
                            Thread.sleep(500);
                        } catch (InterruptedException u) {
                            u.printStackTrace();
                        }
//                        videoLabel.setIcon(imagei);
//                        videoLabel.repaint();
//                        videoLabel.validate();
//                        videoLabel.setVisible(true);
//                        //panelTop.add(photographLabel);
//                        frameStart++;
                        //continue;
                    } catch (IOException x) {
                        System.out.println("Error occurred when reading the file."); // printed if an exception is caught
                    }
                } else {
                    System.out.println("Breaking");
                    break;
                }
            }
            System.out.println("Finished playing video");
            //photographLabel.setIcon(iconUsed);
            //photographLabel.setIcon(iconUsed);
            //resetColorCodeIntensity(); // if another image icon has been selected, this is a new query image so reset all color code intensity

        }
    }

    private class nextPageHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (pageNo < 5) {
                pageNo++;
            }
            System.out.println("current page number is " + pageNo);
            if (pageNo < 5) {
                nextPage.setEnabled(true);
                previousPage.setEnabled(true);
            } else if (pageNo == 5) {
                nextPage.setEnabled(false);
                previousPage.setEnabled(true);
            }
            int imageButNo = 0;
            int endImage = imageCount + 20; // next page handler should show the images starting the image count till the image count + 20
            if (endImage <= 101) { // if statement to make sure endimage does not exceed the number of images
                panelBottom1.removeAll(); // refresh bottom panel 
                for (int i = imageCount; i < endImage; i++) { //for loop from the current image count to the endImage counter
                    imageButNo = buttonOrder[i]; // get the image index from the buttonOrder array
                    panelBottom1.add(button[imageButNo]);
                    JLabel label1 = new JLabel(imageButNo + ".jpg");
                    label1.setForeground(Color.BLACK);
                    label1.setFont(new Font("SansSerif", Font.BOLD, 28));
                    panelBottom1.add(label1);
                    imageCount++;
                }
                panelBottom1.revalidate();
                panelBottom1.repaint();
            }
        }
    }

    private class previousPageHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (pageNo > 1) {
                pageNo--;
            }
            System.out.println("current page number is " + pageNo);
            if (pageNo > 1) {
                previousPage.setEnabled(true);
                nextPage.setEnabled(true);
            } //            else if (pageNo == 1 && checkbox.isSelected()) { // if first page and checkbox is selected, then display the firstPAgeCB function
            //                displayFirstPageCB();
            //                return;
            //                //previousPage.setEnabled(false);
            //                //nextPage.setEnabled(true);
            //            } 
            else if (pageNo == 1) {
                previousPage.setEnabled(false); // if first page, disable the rpevious button
                nextPage.setEnabled(true);
            }
            int imageButNo = 0;
            int startImage = imageCount - 40; // the start image of the previous page is the current - 40 
            int endImage = imageCount - 20; // the end image of the previouis page is the current count - 20
            if (startImage >= 1) {
                panelBottom1.removeAll();
                /*The for loop goes through the buttonOrder array starting with the startImage value
             * and retrieves the image at that place and then adds the button to the panelBottom1.
                 */
                for (int i = startImage; i < endImage; i++) { // same for loop as in the nextPageHandler
                    imageButNo = buttonOrder[i];
                    panelBottom1.add(button[imageButNo]);
                    JLabel label1 = new JLabel(imageButNo + ".jpg");
                    label1.setForeground(Color.BLACK);
                    label1.setFont(new Font("SansSerif", Font.BOLD, 28));
                    panelBottom1.add(label1);
                    imageCount--; // decrementing the imageCount so if nextPAge is called again, it will display the 20 next images
                }
                panelBottom1.revalidate();
                panelBottom1.repaint();
            }
        }
    }

    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
        String filePath = "C:\\Users\\ahmed_nada\\Desktop\\UW GCSDD\\CSS 584 - Multimedia Database\\Assignements\\Assignment 4\\20020924_juve_dk_02a.avi";
        String filePath1 = "C:\\Users\\ahmed_nada\\Desktop\\UW GCSDD\\CSS 584 - Multimedia Database\\Assignements\\Assignment 4\\CSS584_Video3\\build\\classes\\20020924_juve_dk_02a.avi";
        String vlcPath = "C:\\Program Files\\VideoLAN\\VLC";
        //NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        //System.loadLibrary(vlcPath);
        //Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        //LibXUtil.initialise();

//        Mat frame = new Mat();
//        VideoCapture camera = new VideoCapture(filePath);
//        JFrame jframe = new JFrame("Title");
//        jframe.setSize(1100, 750);
//        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JLabel vidpanel = new JLabel();
//        jframe.setContentPane(vidpanel);
//        jframe.setVisible(true);
//
//        while (true) {
//            if (camera.read(frame)) {
//                ImageIcon image = new ImageIcon(Mat2BufferedImage(frame));
//                vidpanel.setIcon(image);
//                vidpanel.repaint();
//            }
//        }
        
        SwingUtilities.invokeLater(new Runnable() {
            //Tutorial thisApp = new Tutorial(filePath);
            public void run() {
                Video app = new Video(filePath);
                //Video app = new Video (filePath1);
                app.setVisible(true);
            }
        });
    }
}

//class Tutorial {
//
//    private final JFrame frame;
//
//    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
//
//    private final JButton pauseButton;
//
//    private final JButton rewindButton;
//
//    private final JButton skipButton;
//
//    public static void main(final String[] args) {
//        new NativeDiscovery().discover();
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new Tutorial(args);
//            }
//        });
//    }
//
//    public Tutorial(String[] args) {
//        frame = new JFrame("My First Media Player");
//        frame.setBounds(100, 100, 600, 400);
//        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                System.out.println(e);
//                mediaPlayerComponent.release();
//                System.exit(0);
//            }
//        });
//
//        JPanel contentPane = new JPanel();
//        contentPane.setLayout(new BorderLayout());
//
//        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
//        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
//
//        JPanel controlsPane = new JPanel();
//        pauseButton = new JButton("Pause");
//        controlsPane.add(pauseButton);
//        rewindButton = new JButton("Rewind");
//        controlsPane.add(rewindButton);
//        skipButton = new JButton("Skip");
//        controlsPane.add(skipButton);
//        contentPane.add(controlsPane, BorderLayout.SOUTH);
//
//        pauseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mediaPlayerComponent.getMediaPlayer().controls().pause();
//            }
//        });
//
//        rewindButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mediaPlayerComponent.getMediaPlayer().controls().setPosition(-10000);
//            }
//        });
//
//        skipButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mediaPlayerComponent.getMediaPlayer().controls().setPosition(10000);
//            }
//        });
//
//        mediaPlayerComponent.getMediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
//            @Override
//            public void playing(MediaPlayer mediaPlayer) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        frame.setTitle(String.format(
//                            "My First Media Player - %s",
//                            mediaPlayerComponent.getMediaPlayer().getMediaMeta().getTitle()
//                        ));
//                    }
//                });
//            }
//
//            @Override
//            public void finished(MediaPlayer mediaPlayer) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeWindow();
//                    }
//                });
//            }
//
//            @Override
//            public void error(MediaPlayer mediaPlayer) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        JOptionPane.showMessageDialog(
//                            frame,
//                            "Failed to play media",
//                            "Error",
//                            JOptionPane.ERROR_MESSAGE
//                        );
//                        closeWindow();
//                    }
//                });
//            }
//        });
//
//        frame.setContentPane(contentPane);
//        frame.setVisible(true);
//
//        mediaPlayerComponent.getMediaPlayer().playMedia(args[0]);
//    }
//
//    private void closeWindow() {
//        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//    }
//}

//class VideoPanel extends JPanel {
//
//    private EmbeddedMediaPlayerComponent mymediaPlayer;
//    private String vlcPath = "d:/Program Files/VideoLAN/VLC";
//    private String mediaPath = "d:/testvideo/test2.mov";
//    private EmbeddedMediaPlayer mediaPlayer;
//
//    private Canvas canvas;
//
//    public VideoPanel() {
//        setLayout(new BorderLayout(0, 0));
//
//        Canvas canvas_1 = new Canvas();
//        add(canvas_1, BorderLayout.CENTER);
//
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
//        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
//
//        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
//        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas_1);
//        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
//        mediaPlayer.setVideoSurface(videoSurface);
//        mediaPlayer.prepareMedia(mediaPath);
//        mediaPlayer.playMedia(mediaPath);
//        mediaPlayer.this.setVisible(true);
//    }
//}

//class VLCPlayer {
//
//    public EmbeddedMediaPlayerComponent mediaPlayerComponent;//= new EmbeddedMediaPlayerComponent();

//This is the path for libvlc.dll
//    public static void main(String[] args) {
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
//        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
//        SwingUtilities.invokeLater(() -> {
//            VLCPlayer vlcPlayer = new VLCPlayer();
//        });

    //}

//    public VLCPlayer() {
//
////MAXIMIZE TO SCREEN
//        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
//
//        JFrame frame = new JFrame();
//
//        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
//
//        frame.setContentPane(mediaPlayerComponent);
//
//        frame.setLocation(0, 0);
//        frame.setSize(300, 400);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//
//        mediaPlayerComponent.
//    }
//}
