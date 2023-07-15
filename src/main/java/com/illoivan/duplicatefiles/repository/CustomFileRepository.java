package com.illoivan.duplicatefiles.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.illoivan.duplicatefiles.model.CustomFile;
import com.illoivan.duplicatefiles.repository.search.CustomReadBuffer;

public class CustomFileRepository {

    public List<CustomFile> searchFiles(String strPath, String algorithm, CustomReadBuffer readBuffer) {
        Path path = Paths.get(strPath);

        List<File> pathsFile = new ArrayList<>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) throws IOException {
                pathsFile.add(file.toFile());
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(path, matcherVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pathsFile.isEmpty()) {
        	return new ArrayList<>();
        }
        return pathsFile.parallelStream()
        		.map(file -> new CustomFile(file,algorithm,readBuffer))
        		.collect(Collectors.toList());
    }
}
