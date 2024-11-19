package com.example.migrations;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MigrationFileReader {

    /**
     * Возвращает список SQL-файлов миграций из заданной директории, отсортированных по имени.
     *
     * @param folderPath Путь к директории с файлами миграций.
     * @return Список файлов миграций.
     */
    public List<File> getSqlMigrations(File folderPath) {
        if (!folderPath.exists() || !folderPath.isDirectory()) {
            throw new IllegalArgumentException("Provided path is not a directory: " + folderPath);
        }

        // Фильтруем только SQL-файлы
        File[] files = folderPath.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".sql");
            }
        });

        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }

        // Сортируем файлы по имени (предполагается, что имя начинается с номера версии)
        List<File> sortedFiles = new ArrayList<>();
        Collections.addAll(sortedFiles, files);
        sortedFiles.sort((file1, file2) -> {
            String name1 = file1.getName();
            String name2 = file2.getName();
            return extractVersion(name1) - extractVersion(name2);
        });

        return sortedFiles;
    }

    /**
     * Извлекает номер версии из имени файла. Предполагается, что имя начинается с числа.
     *
     * @param fileName Имя файла.
     * @return Номер версии.
     */
    private int extractVersion(String fileName) {
        try {
            String version = fileName.split("_")[0]; // Например, "001_create_table.sql" -> "001"
            return Integer.parseInt(version);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid file name format: " + fileName, e);
        }
    }
}
