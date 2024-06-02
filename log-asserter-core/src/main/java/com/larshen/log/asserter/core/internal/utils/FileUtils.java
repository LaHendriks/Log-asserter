package com.larshen.log.asserter.core.internal.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static String[] getAllLinesFromFiles(Path... files) {
        List<String> lines = new ArrayList<>();
        for(Path filePath : files){
            try(Stream<String> linesStream = Files.lines(filePath)){
                lines.addAll(linesStream.collect(Collectors.toList()));
            }catch (FileNotFoundException e) {
                throw new RuntimeException(String.format("File %s with path %s was not found!",
                        filePath.toFile().getName(), filePath), e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return lines.toArray(new String[0]);
    }
}
