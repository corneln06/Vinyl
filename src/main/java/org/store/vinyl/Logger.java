package org.store.vinyl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  private final File logFile;
  private static Logger instance;
  private final ObjectMapper mapper = new ObjectMapper();

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

  public synchronized void log(
          String message,
          String by,
          String ip,
          String vinylTitle)
  {
      try {
      LocalDateTime now = LocalDateTime.now();
      String date =
              now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      String time =
              now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
      ObjectNode root;
      if(logFile.length() == 0){
          root = mapper.createObjectNode();
      }
      else{
          root = (ObjectNode) mapper.readTree(logFile);
      }
      if(!root.has(date)){
          root.set(date, mapper.createArrayNode());
      }
      ArrayNode logs =
              (ArrayNode) root.get(date);
      ObjectNode newLog =
              mapper.createObjectNode();
      newLog.put("message", message);
      newLog.put("time", time);
      newLog.put("by", by);
      newLog.put("ip", ip);
      newLog.put("vinyl", vinylTitle);
      logs.add(newLog);
      mapper.writerWithDefaultPrettyPrinter()
              .writeValue(logFile, root);
  }
  catch(Exception e){
      e.printStackTrace();
  }
  }
}