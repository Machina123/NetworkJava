package net.machina.networkjava.class1;

import java.io.File;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        SerializacjaDanych sd = new SerializacjaDanych();
//        sd.generateSerializableFile("osoba.dat");
//        sd.readSerializableFile("osoba.dat");
        Pliki p = new Pliki();
        Path startPath = p.getFilePath();
        File[] files = p.getAllFiles(startPath);
//        p.moveFile(files, startPath, "katalog");
        PlikiZip zip = new PlikiZip();
        zip.generateZipArchive(files, "osoby.zip", startPath);
    }
}
