package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    static final String FILE_NAME = "src/main/java/pl/coderslab/tasks.csv";
    static String[][] tasks;

    public static void main(String[] args) {
        printWelcomeMessage();
        printOptions();
        tasks = loadDatatoTab();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            switch (input) {
                case "add":
                    addTask();
                    break;
                case "list":
                    showList(tasks);
                    break;
                case "remove":
                    if (tasks.length == 0) {
                        System.out.println("List of tasks is empty.");
                    } else {
                        removeTask(tasks);
                    }
                    break;
                case "exit":
                    saveTasks(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select a correct option.");

            }
        }
    }


    public static void saveTasks(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);
        String[] lines = new String[tasks.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void removeTask(String[][] tab) {
        int index = 0;
        do {
            try {
                showList(tasks);
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please select number to remove");
                while (true) {
                    while (!(scanner.hasNextInt())) {
                        String wrongInput = scanner.nextLine();
                        System.out.println("Incorrect value. Please give a number");
                    }
                    index = scanner.nextInt();

                    if (index >= 0) {
                        tasks = ArrayUtils.remove(tab, index);
                        System.out.println("Task number " + index + " is removed");
                        break;
                    } else {
                        System.out.println("Please give number grater or equal 0: ");
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Incorrect number of task. Please give a number lower than " + (tasks.length-1) + ": ");
            }
        } while (index < -1 || index >= tab.length);
        showList(tasks);
    }

    public static void showList(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description: ");
        String description = scanner.nextLine();
        System.out.println("Please add task due data: ");
        String dueData = scanner.nextLine();
        System.out.println("Is your task is important: true/false");
        String isImportant = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueData;
        tasks[tasks.length - 1][2] = isImportant;
        showList(tasks);
    }

    public static String[][] loadDatatoTab() {
        File file = new File(FILE_NAME);
        StringBuilder sb = new StringBuilder();
        int countRow = 0;
        String line;
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                sb.append(line).append(";");
                countRow++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        String allFile = sb.toString();
        String[] lines = allFile.split(";");

        String[][] tab = new String[countRow][];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = lines[i].split(",");
        }
        return tab;
    }

    public static void printOptions() {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);

        String[] menu = {"add", "remove", "list", "exit"};
        for (String option : menu) {
            System.out.println(option);
        }
    }

    private static void printWelcomeMessage() {
        String userName = System.getProperty("user.name");
        System.out.println("Hello " + userName);
    }

}