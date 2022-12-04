import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.CREATE;

public class Main {
    static ArrayList<String> list = new ArrayList<>();
    static Scanner in = new Scanner(System.in);

    static boolean trySave = false;


    public static void main(String[] args) {

        final String menu = "A = Add D = Delete V = View Q = Quit O = Open S = Save C = Clear";
        String cmd;

        do {
            displayList();

            cmd = SafeInputs.getRegExString(in, menu, "[AaDdVvQqOoSsCc]");
            cmd = cmd.toUpperCase();
            switch (cmd) {
                case "A" -> {
                    add(in, list);
                    System.out.println();
                }
                case "C" -> clearList();
                case "D" -> {
                    delete(in, list);
                    if (list.size() == 0) {
                        System.out.println("No items in the list");
                    }
                }
                case "O" -> open();
                case "S" -> saveFile();
                case "V" -> print(list);
                case "Q" -> {
                    boolean saved = SafeInputs.getYNConfirm(in, "Do you want to save your list?");
                    if (saved)
                        saveFile();
                    else {
                        System.exit(0);
                    }

                }
            }
        } while (true);
    }

    private static void displayList() {
        System.out.println("--------------------------------------");
        if (list.size() != 0)
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%3d%35s", i + 1, list.get(i));
                System.out.println();
            }
        else
            System.out.println("---         List is empty          ---");
        System.out.println();

    }

    public static void print(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + " " + list.get(i));
        }
        System.out.println();
    }

    public static void add(Scanner in, ArrayList<String> list) {
        String item = SafeInputs.getNonZeroLength(in, "What item do you want to add?");
        list.add(item);

    }

    public static void clearList() {
        list.clear();
    }

    public static void delete(Scanner in, ArrayList<String> list) {
        int item = SafeInputs.getRangedInt(in, "What item do you want to remove?", 1, 1000);
        list.remove(item - 1);
    }

    public static void open() {
        JFileChooser chooser = new JFileChooser();
        File select;
        String rec;
        if (!trySave) {
            try {
                File workingDirectory = new File(System.getProperty("user.dir"));

                chooser.setCurrentDirectory(workingDirectory);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    select = chooser.getSelectedFile();
                    Path file = select.toPath();
                    InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    list.clear();

                    while (reader.ready()) {
                        rec = reader.readLine();
                        list.add(rec);
                    }

                } else {
                    System.out.println("Couldn't pick a file to process, run again.");
                    System.exit(0);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found!!!");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            boolean save = SafeInputs.getYNConfirm(in,"List has not been saved, would you like to save? ");
            if (save)
                saveFile();
            else {
                try {
                    File workingDirectory = new File(System.getProperty("user.dir"));

                    chooser.setCurrentDirectory(workingDirectory);

                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        select = chooser.getSelectedFile();
                        Path file = select.toPath();
                        InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        list.clear();

                        while (reader.ready()) {
                            rec = reader.readLine();
                            list.add(rec);
                        }

                    } else {
                        System.out.println("Failed to choose a file to process, run again!");
                        System.exit(0);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveFile() {
        try {
            Scanner in = new Scanner(System.in);

            String fileName = SafeInputs.getNonZeroLength(in, "Name your saved file: ");
            fileName = fileName + ".txt";
            File myFile = new File(fileName);
            if (myFile.createNewFile()) {
                System.out.println("Saving the list " + myFile.getName());


            } else {
                System.out.println("Overwriting previous save");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (String push : list) {
                writer.write(push, 0, push.length());
            }
            writer.close();
            System.out.println("Data file written!");


        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();

        }
    }
}
