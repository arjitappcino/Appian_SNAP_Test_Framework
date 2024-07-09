/**
 * Author: Xebia | Appcino
 * Framework: SNAP Appian Test Framework (https://github.com/arjitappcino/Snap-Appian-Test-Framework)
 * Class Description: This is a utility class which has various important methods that you'll be using while writing complex flows in test cases.
 */

package utils;

import base.BaseClass;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import javax.imageio.ImageIO;

public class Utility extends BaseClass {

    static TakesScreenshot finalScrShot;

    public String screenshotBase64() {
        String screenshotBase64 = "";
        try {
            finalScrShot = ((TakesScreenshot) driver);
            File src = finalScrShot.getScreenshotAs(OutputType.FILE);
            byte[] fileContent = FileUtils.readFileToByteArray(src);

            screenshotBase64 = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(fileContent);


            String directory = "./testResults/" + artifactsFolder + "/screenshots";
            Path path = Paths.get(directory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
            String fileName = "screenshot_" + dateFormat.format(new Date()) + ".png";

            File destFile = new File(directory, fileName);

            ImageIO.write(ImageIO.read(src), "png", destFile);
        } catch (IOException e) {
            System.out.println(e);
        }
        return screenshotBase64;
    }

    @Override
    public int getRandomIntegerFromTo(int min, int max) {
        return super.getRandomIntegerFromTo(min, max);
    }

    public String randomTextGenerate(int charCount) {
        Lorem lorem = LoremIpsum.getInstance();
        String loremIpsum = lorem.getWords(charCount);

        return loremIpsum;
    }

    public static String currentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = now.format(formatter);
        return "ARTIFACTS_" + formattedDateTime;
    }

    public static void createTestArtifactsFolder() {
        String testResultsFolderName = "testResults";
        String testFolderName = artifactsFolder;
        String screenshotsFolderName = "screenshots";

        // Create a File object for the test results folder
        File testResultsFolder = new File(testResultsFolderName);

        // Create the test results folder if it doesn't exist
        if (!testResultsFolder.exists()) {
            boolean isTestResultsFolderCreated = testResultsFolder.mkdir();
            if (isTestResultsFolderCreated) {
                System.out.println("Test results folder created successfully!");
            } else {
                System.out.println("Failed to create test results folder");
            }
        }

        // Create a File object for the test folder inside the test results folder
        File testFolder = new File(testResultsFolder, testFolderName);

        // Create the test folder if it doesn't exist
        if (!testFolder.exists()) {
            boolean isTestFolderCreated = testFolder.mkdir();
            if (isTestFolderCreated) {
                System.out.println("Test folder created successfully!");

                // Create a File object for the screenshots folder inside the test folder
                File screenshotsFolder = new File(testFolder, screenshotsFolderName);

                // Create the screenshots folder
                boolean isScreenshotsFolderCreated = screenshotsFolder.mkdir();
                if (isScreenshotsFolderCreated) {
                    System.out.println("Screenshots folder created successfully!");
                } else {
                    System.out.println("Failed to create screenshots folder");
                }
            } else {
                System.out.println("Failed to create test folder");
            }
        } else {
            System.out.println("Test folder already exists");
        }
    }

    public static String capitalizeText(String input){
        String[] words = input.split(" ");
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                output.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
            } else {
                output.append(" ").append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
            }
        }

        return output.toString();
    }

    public static String percentToDecimal(String percentage){
        String valueString = percentage.replace("%", "");
        double value = Double.parseDouble(valueString) / 100.0;
        return String.valueOf(value);
    }

    public static void deleteDirectoryContents(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (Files.exists(path) && Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw exc;
                    }
                }
            });
        } else {
            throw new IllegalArgumentException("The provided path is either not a directory or does not exist.");
        }
    }
    public static void openReportInBrowser(String directoryPath) throws IOException {
        Path dirPath = Paths.get(directoryPath);
        if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                for (Path entry : stream) {
                    if (Files.isDirectory(entry)) {
                        Path reportPath = entry.resolve("report.html");
                        if (Files.exists(reportPath) && Files.isRegularFile(reportPath)) {
                            openFileInBrowser(reportPath.toFile());
                            return;
                        }
                    }
                }
                throw new IllegalArgumentException("No 'Report.html' file found in subfolders.");
            }
        } else {
            throw new IllegalArgumentException("The provided path is either not a directory or does not exist.");
        }
    }

    private static void openFileInBrowser(File file) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) {
                desktop.browse(file.toURI());
            } else {
                throw new IllegalArgumentException("The file does not exist.");
            }
        } else {
            throw new UnsupportedOperationException("Desktop is not supported on this system.");
        }
    }
}

