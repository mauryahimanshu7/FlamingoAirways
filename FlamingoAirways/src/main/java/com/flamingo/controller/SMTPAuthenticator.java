package com.flamingo.controller;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class SMTPAuthenticator extends Authenticator
{
public PasswordAuthentication getPasswordAuthentication()
{
    String username = "mauryahimanshu7@gmail.com";
    String password = "qwerty123";
    return new PasswordAuthentication(username, password);
}
}