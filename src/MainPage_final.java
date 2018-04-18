
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shivam
 */
public class MainPage_final{

    
    
     DataInputStream d_in ;
     DataOutputStream d_out;
     ServerSocket m_ser ;
     
     public MainPage_final(){
     while(true){
        try{
        System.out.println("Trying to create server");
        m_ser = new ServerSocket(6377);
        System.out.println("Started");
        Socket cli = m_ser.accept();
        System.out.println("Started 2");
        d_in = new DataInputStream(cli.getInputStream());
        d_out = new DataOutputStream(cli.getOutputStream());
        d_out.writeUTF("HEY"); 
        while(true){
        String in=d_in.readUTF();
        if(in.equals("MouseButton"))
            MoveMouse();
        if(in.equals("scr"))
            ScreenShot();
        if(in.equals("prss"))
            process();
        if(in.equals("msg"))
            msg();
        if(in.equals("key"))
            key();
        }
        }catch(Exception E){try {
            System.out.println(E);
           m_ser.close();
            }
            catch (IOException ex) {
              //  Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
            }
}
    }       
     
     }
      public static void main(String args[]){
      MainPage_final f1 = new MainPage_final();
      }
      
    private void MoveMouse() throws IOException, InterruptedException, AWTException {

        String res = "";
    res = d_in.readUTF();
   // if(res.equals("end"))
     //   break;
    int len = res.length();
    int x=0,y=0;
    int del = res.indexOf(":");
    for(int i=0;i<del;i++){
     x = x*10+(res.charAt(i) - '0');
    }
    for(int i=del+1;i<len;i++){
     y = y*10+(res.charAt(i) - '0');
    }
    System.out.println(""+x+" "+y);
      Thread.sleep(1);
   Robot r = new Robot();
   r.mouseMove(x, y);
  // Thread.sleep(10);
   r.mousePress(InputEvent.BUTTON1_MASK);
   r.mouseRelease(InputEvent.BUTTON1_MASK);
        
        
    }

    private void ScreenShot() {
        try {
            Robot robot = new Robot();
            String format = "jpg";
            String fileName = "FullScreenshot." + format;
             
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            File f = new File(fileName);
            ImageIO.write(screenFullImage, format, f);
            System.out.println("A full screenshot saved!");
            sendFile("FullScreenshot.jpg");     
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        } }

    private void process() throws IOException {
      String cmd = d_in.readUTF();
      ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",cmd);          
      builder.redirectErrorStream(true); 
      Process p = builder.start(); 
      BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream())); 
      String line; 
      String[] arofp = new String [1000];
      int i=0;
      while (true) { 
            line = r.readLine(); 
            if (line == null) { break; }
          int l1 = line.indexOf(".", 0);
          if(l1>0){
           arofp[i] =  line.substring(0, l1);
           i++;
          }
          System.out.println(line+"L1 :"+l1); 
        
   }
      d_out.writeInt(i-1);
       for(int j=0;arofp[j]!=null;j++)
          d_out.writeUTF(arofp[j]);
      System.out.println("Sent Array");
}

    private void msg() {
        String mm;
         try {
             mm = d_in.readUTF();
              Thread t = new Thread(new Runnable(){
        public void run(){
            JOptionPane.showMessageDialog(null, mm);
        }
    });
  t.start();
           //  JOptionPane.showMessageDialog(null, mm, "Admin", JOptionPane.WARNING_MESSAGE);
         } catch (IOException ex) {
             Logger.getLogger(MainPage_final.class.getName()).log(Level.SEVERE, null, ex);
         }
       
    }

    private void sendFile(String fullScreenshotjpg) throws IOException {
        BufferedInputStream bis = null;
         try {
             File myFile = new File(fullScreenshotjpg);
             byte[] mybytearray = new byte[(int) myFile.length()];
             bis = new BufferedInputStream(new FileInputStream(myFile));
             bis.read(mybytearray, 0, mybytearray.length);
             int size = mybytearray.length;
             System.out.println("OUPUT BYTES ARE "+size );
             d_out.writeDouble(size);
             d_out.write(mybytearray, 0, mybytearray.length);
             d_out.flush();
         } catch (FileNotFoundException ex) {
             Logger.getLogger(MainPage_final.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
             try {
                 bis.close();
             } catch (IOException ex) {
                 Logger.getLogger(MainPage_final.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
    }

    private void key() {
         try {
             Robot r = new Robot();
           
         } catch (AWTException ex) {
             Logger.getLogger(MainPage_final.class.getName()).log(Level.SEVERE, null, ex);
         }
       
    }
  
    
}
