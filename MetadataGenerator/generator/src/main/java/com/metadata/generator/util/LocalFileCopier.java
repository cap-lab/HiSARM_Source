package com.metadata.generator.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LocalFileCopier {
    public static void copyBinaryFile(Path remoteFile, Path localFile) throws Exception {
        Files.copy(remoteFile, localFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyDirectory(Path remoteDir, Path localDir) throws Exception {
        localDir.toFile().mkdirs();

        File dir = remoteDir.toFile();
        for (File file : dir.listFiles()) {
            if (file.getName().equals(".") || file.getName().equals("..")) {
                continue;
            }
            Path newRemotePath = Paths.get(remoteDir.toString(), file.getName());
            Path newLocalPath = Paths.get(localDir.toString(), file.getName());
            if (file.isDirectory()) {
                newLocalPath.toFile().mkdirs();
                copyDirectory(newRemotePath, newLocalPath);
            } else {
                copyBinaryFile(newRemotePath, newLocalPath);
            }
        }
    }
}
