package com.codegenerator.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;

public class TranslatorCode {
    public void translate(Path targetDir, AdditionalInfo additionalInfo) {
        Path fullPath = Paths.get(targetDir.toString(), additionalInfo.getProjectName());
        List<Path> javaPathList = makeJavaPath(additionalInfo);

        String[] commands =
                makeExternalTranslatorCommand(additionalInfo, javaPathList, fullPath, targetDir);

        execute(commands);
    }

    private List<Path> findRecursively(Path dir, String extension) {
        List<Path> fileNameList = new ArrayList<>();
        try (Stream<Path> walkStream = Files.walk(Paths.get(dir.toString()))) {
            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                if (f.toString().endsWith(extension)) {
                    fileNameList.add(f);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNameList;
    }

    private List<Path> makeJavaPath(AdditionalInfo additionalInfo) {
        List<Path> javaPathList = null;
        javaPathList = findRecursively(Paths.get(additionalInfo.getJarDirectory()),
                CodeGeneratorConstant.JAR_EXTENSION);
        javaPathList.addAll(findRecursively(
                Paths.get(additionalInfo.getTranslatorDirectory(), CodeGeneratorConstant.JARS),
                CodeGeneratorConstant.JAR_EXTENSION));

        javaPathList.add(0,
                Paths.get(additionalInfo.getTranslatorDirectory(), CodeGeneratorConstant.BIN));
        return javaPathList;
    }

    private String[] makeExternalTranslatorCommand(AdditionalInfo additionalInfo,
            List<Path> javaPathList, Path fullPath, Path targetDir) {
        String[] commands = new String[7];
        commands[0] = "java";
        commands[1] = "-classpath";
        commands[2] = javaPathList.stream().map(p -> p.toString()).reduce(new String(),
                (total, p) -> (total + File.pathSeparator + p.toString()));
        commands[3] = "Translators.CodeGenerator";
        commands[4] = Paths.get(additionalInfo.getTranslatorDirectory(), CodeGeneratorConstant.BIN)
                .toString();
        commands[5] = fullPath.toString();
        commands[6] = targetDir.toString();
        return commands;
    }

    private void execute(String[] commands) {
        Runtime rt = Runtime.getRuntime();
        Thread resultThread = null;
        try {
            Process p = rt.exec(commands);

            StreamGobbler errThread = new StreamGobbler(p.getErrorStream());
            OutputStreamWriter outputStream = new OutputStreamWriter(p.getOutputStream());
            BufferedWriter writer = new BufferedWriter(outputStream);

            resultThread = new StreamGobbler(p.getInputStream());
            errThread.start();
            resultThread.start();

            p.waitFor();
            p = null;
            resultThread.join();
            errThread.join();

            outputStream.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class StreamGobbler extends Thread {
        InputStream is;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                br.close();
                is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
