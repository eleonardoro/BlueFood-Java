package com.eleonardo.bluefood.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.nio.file.Files;
import java.nio.file.Path;

public class IOUtils {
  
  public static void copy(InputStream in, String fileName, String outputDir) throws IOException {
    Files.copy(in, Paths.get(outputDir, fileName), StandardCopyOption.REPLACE_EXISTING);
  }
  
  public static byte[] getByte(Path path) throws IOException{
    return Files.readAllBytes(path);
  }

}
