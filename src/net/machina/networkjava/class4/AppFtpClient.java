package net.machina.networkjava.class4;

import com.jcraft.jsch.*;

import java.util.Scanner;
import java.util.Vector;

public class AppFtpClient {

    private JSch jsch;
    private Session session;
    private FtpData ftpData;
    private Channel channel;
    private ChannelSftp channelSftp;
    private String home;
    private String host;

    public AppFtpClient(String nHost) {
        this.host = nHost;
        this.jsch = new JSch();
        // utworzenie obiektu z danymi do logowania
        this.ftpData = new FtpData();
        Start();
    }

    public void Start() {
        if (connect()) {
            boolean working = true;
            System.out.println("Nawiązano połączenie serwerem ");
            System.out.println("Proszę wybrać opcję:");
            System.out.println("1) wyświetl wszystkie pliki");
            System.out.println("2) pobierz plik z serwera");
            System.out.println("3) wyślij plik na serwer");
            System.out.println("4) usuń plik");
            System.out.println("5) zmień nazwę pliku");
            System.out.println("6) przejdz do katalogu");
            System.out.println("q) wyjście");

            Scanner input = new Scanner(System.in);
            while (working) {
                try {
                    System.out.println("Aktualna lokalizacja: " + channelSftp.pwd());
                    switch (input.nextLine()) {
                        case "1":
                            System.out.println("Podaj lokliację lub pozostaw puste");
                            String location = input.nextLine();
                            listAllFiles(location);
                            break;
                        case "2":
                            System.out.println("Podaj lokalizację pliku na serwerze:");
                            String servweFile = input.nextLine();
                            System.out.println("Podaj loklizację do zapisania ");
                            String locationDest = input.nextLine();
                            downloadFile(servweFile, locationDest);
                            break;
                        case "3":
                            System.out.println("Podaj lokalizację pliku z dysku:");
                            String localFile = input.nextLine();
                            System.out.println("Podaj lokalizację na serwerze:");
                            String serverDest = input.nextLine();
                            uploadFile(localFile, serverDest);
                            break;
                        case "4":
                            System.out.println("Proszę o podanie pliku do usunięcia:");
                            String delFile = input.nextLine();
                            removeFile(delFile);
                            break;
                        case "5":
                            System.out.println("Proszę podać plik do zmiany nazwy:");
                            String oldName = input.nextLine();
                            System.out.println("Proszę podać nową nazwe pliku:");
                            String newName = input.nextLine();
                            renameFile(channelSftp.pwd(), oldName, newName);
                            break;
                        case "6":
                            System.out.println("Proszę podać katalog:");
                            String folder = input.nextLine();
                            changeDirectory(folder);
                            break;
                        case "q":
                            System.out.println("kończę prace");
                            working = false;
                            break;
                        default:
                            System.out.println("nie podano odpowiedniej komendy");
                            break;
                    }
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
            disconnect();
        } else {
            System.out.println("Nie udało się nawiązać połączenia z serwerem: ");
            System.out.println(host);
        }
    }

    public boolean connect() {
        try {
            session = jsch.getSession(ftpData.GetLogin(), host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(ftpData.GetPassword());
            session.connect(10000);
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            home = channelSftp.getHome();
            System.out.println(home);
            return true;
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        channel.disconnect();
        session.disconnect();
    }

    @SuppressWarnings("rawtypes")
    public void listAllFiles(String location) {
        try {
            String tempLoc;
            if(location.isEmpty()) tempLoc = channelSftp.pwd();
            else tempLoc = channelSftp.pwd() + "/" + location;
            Vector files = channelSftp.ls(tempLoc);
            for (Object file : files) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) file;
                if(!lsEntry.getFilename().endsWith("."))
                    System.out.println(lsEntry.getFilename() + " " + lsEntry.getAttrs().getSize());
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public void changeDirectory(String location) {
        try {
            channelSftp.cd(location);
        } catch (SftpException e) {
            System.out.println("Błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void downloadFile(String remotePath, String localPath) {
        try {
            String tempLoc = null;
            if(remotePath.contains("/")) {
                if(channelSftp.ls(channelSftp.pwd() + "/" + remotePath).size() > 0) {
                    tempLoc = channelSftp.pwd() + "/" + remotePath;
                }
            } else {
                tempLoc = channelSftp.pwd() + "/" + remotePath;
            }
            channelSftp.get(tempLoc, localPath);
            System.out.println("Plik pobrany");
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Nie udało się pobrać pliku: " + e.getMessage());
        }
    }

    public void uploadFile(String localPath, String remotePath) {
        try {
            String[] tempLoc;
            if(remotePath.contains("/")) {
                tempLoc = remotePath.split("/");
                for(String pathPart : tempLoc) {
                    if(pathPart.length() > 0 || pathPart.contains(".")) {
                        try {
                            channelSftp.cd(pathPart);
                        } catch (SftpException e) {
                            System.out.println("Katalog " + pathPart + " nie istnieje. Utworzyć? (T/N)");
                            Scanner scanner = new Scanner(System.in);
                            if(scanner.nextLine().equalsIgnoreCase("t")) {
                                channelSftp.mkdir(pathPart);
                                channelSftp.cd(pathPart);
                                System.out.println("Katalog utworzony: " + pathPart);
                            } else {
                                System.out.println("Wysyłanie przerwane");
                                break;
                            }
                        }
                    }
                }
            }
            channelSftp.put(localPath, channelSftp.pwd());
            System.out.println("Plik wysłany");
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public void removeFile(String filename) {
        try {
            String path = channelSftp.pwd() + "/" + filename;
            if(channelSftp.ls(path).size() > 0){
                System.out.println("Operacja jest nieodwracalna!");
                System.out.println("Czy na pewno chcesz usunąć plik " + filename + "? (T/N)");
                Scanner scanner = new Scanner(System.in);
                if(scanner.nextLine().equalsIgnoreCase("t")) {
                    channelSftp.rm(filename);
                    System.out.println("Plik usunięty");
                }
            }
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Nie można usunąć pliku: " + e.getMessage());
        }
    }

    public void renameFile(String location, String oldName, String newName) {
        try {
            channelSftp.rename(location + "/" + oldName, location + "/" + newName);
            System.out.println("Zmieniono nazwę");
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Nie udało się zmienić nazwy: " + e.getMessage());
        }
    }

}