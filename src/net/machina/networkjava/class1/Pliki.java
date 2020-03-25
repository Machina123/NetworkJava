package net.machina.networkjava.class1;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Pliki {
    public Pliki() {
    }

    public Path getFilePath() {
        File f = new File("temp.txt");
//        System.out.println(f.getAbsolutePath());
        return f.toPath().toAbsolutePath().getParent();
    }

    public File[] getAllFiles(Path location) {
        File[] tmpFiles = location.toFile().listFiles((dir, name) -> name.endsWith(".dat"));
        for(File f : tmpFiles) {
            if(f.isFile()) System.out.println(f.getName());
                //  getFileInfo(f);
        }
        return tmpFiles;
    }

    public void getFileInfo(File f) {
        System.out.println("Nazwa: " + f.getName() + "\n" +
                "Root: " + f.toPath().getRoot().toString() + "\n" +
                "Podkatalogi: " + f.toPath().getNameCount() + "\n" +
                "Wielkość (B): " + f.length() + "\n" +
                "Modyfikacja: " + f.lastModified() + "\n" +
                "Ukryty? " + (f.isHidden() ? "Tak" : "Nie") + "\n" +
                "Wolne miejsce: " + f.getFreeSpace());
    }

    public void moveFile(File[] files, Path srcPath, String dest) {
        Path destPath = srcPath.resolve(dest);
        if(Files.exists(destPath)) System.out.println("Katalog istnieje");
        else {
            System.out.println("Katalog nie istnieje, tworzenie...");
            try {
                Files.createDirectory(destPath);
                System.out.println("Katalog utworzony!");
            } catch (IOException e) {
                System.out.println("Nie udało się utworzyć katalogu");
                e.printStackTrace();
            }
        }
        System.out.println(srcPath);
        System.out.println(destPath);
        for(File f : files) {
            try {
                Files.copy(Paths.get(f.getAbsolutePath()), destPath.resolve(f.getName()));
                System.out.println("Przenoszę plik do " + destPath.resolve(f.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
