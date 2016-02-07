package com.drknotter.episodilyzer.server.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by plunkett on 1/30/16.
 */
public class FilesystemUtils {
    static File downloadZipFile(InputStream in, File seriesDir, int seriesId) throws IOException {
        File zipFile = new File(seriesDir, Integer.toString(seriesId) + ".zip");

        OutputStream zipFileOutputStream = new FileOutputStream(zipFile);
        byte[] buffer = new byte[1024];

        int count;
        while ((count = in.read(buffer)) != -1) {
            zipFileOutputStream.write(buffer, 0, count);
        }

        zipFileOutputStream.flush();
        zipFileOutputStream.close();

        return zipFile;
    }

    static void unzip(File zipFile, File dir) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.isDirectory()) {
                new File(dir, zipEntry.getName()).mkdir();
            } else {
                byte[] buffer = new byte[1024];
                FileOutputStream fileOutputStream = new FileOutputStream(
                        new File(dir, zipEntry.getName()));

                int count;
                while ((count = zipInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, count);
                }

                zipInputStream.closeEntry();
                fileOutputStream.close();
            }

        }
        zipInputStream.close();
    }

    static void nukeDirectory(File file)
    {
        if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                nukeDirectory(child);
            }
        }
        file.delete();
    }
}
