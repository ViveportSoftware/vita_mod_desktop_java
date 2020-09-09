package com.htc.vita.mod.desktop.io;

import com.htc.vita.core.log.Logger;
import com.htc.vita.core.util.StringUtils;

import java.io.*;
import java.util.Scanner;

public class FileUtils {
    public static String readAllText(File file) {
        return readAllText(
                file,
                StringUtils.STRING_ENCODING_UTF_8
        );
    }

    public static String readAllText(
            File file,
            String encoding) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(
                    new FileInputStream(file),
                    encoding
            );
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append(lineSeparator);
            }
        } catch (FileNotFoundException e) {
            Logger.getInstance(FileUtils.class.getSimpleName()).error(e.toString());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return builder.toString();
    }

    public static boolean writeAllText(
            File file,
            String data) {
        return writeAllText(
                file,
                data,
                StringUtils.STRING_ENCODING_UTF_8
        );
    }

    public static boolean writeAllText(
            File file,
            String data,
            String encoding) {
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(
                    new FileOutputStream(file),
                    encoding
            );
            writer.write(data);
            writer.flush();
            return true;
        } catch (Exception e) {
            Logger.getInstance(FileUtils.class.getSimpleName()).error(e.toString());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                // Skip
            }
        }
        return false;
    }
}
