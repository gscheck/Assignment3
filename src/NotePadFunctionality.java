import javax.swing.*;
import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.*;


// Revision: AB
// Date: 6/7/2019
// Revision by: Garth Scheck

// class for notepad functionality
public class NotePadFunctionality {

    // dictionary for storing file name with path
    Dictionary recentFiles = new Hashtable();

    // method for saving text to file
    public void save(String text)
    {
        File fileToWrite = null;
        JFileChooser fc = new JFileChooser();

        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            fileToWrite = fc.getSelectedFile();
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
            out.println(text);
            JOptionPane.showMessageDialog(null, "File is saved successfully...");
            out.close();
        } catch (IOException ex) {
        }
    }

    // method for printing text
    public void print(Printable printJob)
    {
        try{
            PrinterJob pjob = PrinterJob.getPrinterJob();
            pjob.setJobName("Sample Command Pattern");
            pjob.setCopies(1);
            pjob.setPrintable(printJob);

            if (pjob.printDialog() == false)
                return;
            pjob.print();
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(null,
                    "Printer error" + pe, "Printing error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // method for reading file into a string
    public String read(Object fName)
    {

        Object filePath = recentFiles.get(fName);
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get((String)filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    // method for opening a file
    public String open()
    {
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String fName = dialog.getFile();


        String filePath = dialog.getDirectory() + fName;

        recentFiles.put(fName, filePath);
        return fName;
    }
}
