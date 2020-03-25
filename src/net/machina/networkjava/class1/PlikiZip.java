package net.machina.networkjava.class1;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PlikiZip {
    public PlikiZip() {
    }

    public void generateZipArchive(File[] files, String archName, Path archPath) {
        try {
            if(files.length < 1) {
                System.out.println("Brak plików do archiwizacji!");
                return;
            }
            System.out.println("Tworzę archiwum ZIP");
            ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(archPath.resolve(archName).toString()));
            for(File f : files) {
                ZipEntry entry = new ZipEntry(f.getName());
                zipStream.putNextEntry(entry);
                FileInputStream fileStream = new FileInputStream(f);
                System.out.println("Archiwizuję " + f.getName());
                byte[] buffer = new byte[1024];
                int length;
                while((length = fileStream.read(buffer))>=0) {
                    zipStream.write(buffer, 0, length);
                }
                fileStream.close();
                zipStream.closeEntry();
            }
            zipStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
