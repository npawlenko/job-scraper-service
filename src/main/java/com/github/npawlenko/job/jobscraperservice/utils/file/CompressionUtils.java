package com.github.npawlenko.job.jobscraperservice.utils.file;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class CompressionUtils {

    public static void extractFileFromZipArchiveFirstMatch(File zipArchive, String fileName, File outFile) {
        try (ZipFile zipFile = new ZipFile(zipArchive)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (entry.getName().endsWith(fileName)) {
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        IOUtils.copy(zipFile.getInputStream(entry), fos);
                    }
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
