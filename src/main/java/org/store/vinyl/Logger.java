package org.store.vinyl;

import java.io.*;
public class Logger {
  private final File logFile;
  private static Logger instance;

  private Logger() {
    logFile = new File("LogFile.json");

    try {
      logFile.createNewFile();
      System.out.println(logFile.getAbsolutePath());
      System.out.println("Exists after create: " + logFile.exists());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  public void log(String text) {
    System.out.println("LOG CALLED: " + text);

    try (BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true))) {
      out.write(text);
      out.newLine();
      out.flush();
      System.out.println("Logged to: " + logFile.getAbsolutePath());
      System.out.println("File size: " + logFile.length());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}