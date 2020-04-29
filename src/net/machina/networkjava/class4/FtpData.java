package net.machina.networkjava.class4;

public class FtpData {

    private String login;
    private String password;

    public FtpData(){
        this.login = "";        // login do klienta FTP
        this.password = "";     // hasło do klienta FTP
    }

    public String GetLogin(){
        return this.login;
    }

    public String GetPassword(){
        return this.password;
    }
}