
import java.io.*;

import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author wulft
 * 
 * Use the thread safe NIO IO library to read a file
 */
public class NIOReadTextFile
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        JFileChooser chooser = new JFileChooser();

        File selectedFile;
        String rec = "";

        ArrayList<String> lines = new ArrayList<>();

       try
       {
        // uses a fixed known path:
       //  Path file = Paths.get("c:\\My Documents\\data.txt");

       // use the toolkit to get the current working directory of the IDE
       // Not sure if the toolkit is thread safe...
       File workingDirectory = new File(System.getProperty("user.dir"));

       // Typiacally, we want the user to pick the file so we use a file chooser
       // kind of ugly code to make the chooser work with NIO.
       // Because the chooser is part of Swing it should be thread safe.
       chooser.setCurrentDirectory(workingDirectory);
       // Using the chooser adds some complexity to the code.
       // we have to code the complete program within the conditional return of
       // the filechooser because the user can close it without picking a file

       if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
       {
          selectedFile = chooser.getSelectedFile();
          Path file = selectedFile.toPath();
          int wordCnt = 0;
          int charCount = 0;

           // Typical java pattern of inherited classes
           // we wrap a BufferedWriter around a lower level BufferedOutputStream
           InputStream in =
                   new BufferedInputStream(Files.newInputStream(file, CREATE));
           BufferedReader reader =
                   new BufferedReader(new InputStreamReader(in));

           // Finally we can read the file LOL!
           int lineCnt = 0;
           while(reader.ready())


           {

              rec = reader.readLine();
              lines.add(rec);
              lineCnt++;
              // echo to screen

               String Lines [] = rec.split(" ");
               wordCnt += Lines.length;

               charCount += rec.length();


           }
           for(String l:lines)
           {
               System.out.println(l);
           }

           // try-catch block to handle exceptions
           try {

           }
           catch (Exception e) {
               System.err.println(e.getMessage());
           }

           System.out.println("Summary Report:");

           System.out.println("The file name is " + selectedFile);

           System.out.println("There are " + lineCnt +" lines.");

           System.out.println("There is " + charCount + " characters");

           System.out.println("There is " + wordCnt + " word(s)");


           reader.close(); // must close the file to seal it and flush buffer
           System.out.println("\n\nData file read!");

       }
       else  // user closed the file dialog without choosing
       {
           System.out.println("Failed to choose a file to process");
           System.out.println("Run the program again!");
           System.exit(0);
       }
       }  // end of TRY
       catch (FileNotFoundException e)
       {
           System.out.println("File not found!!!");
           e.printStackTrace();
       }
       catch (IOException e)
       {
           e.printStackTrace();
       }
    }





}
