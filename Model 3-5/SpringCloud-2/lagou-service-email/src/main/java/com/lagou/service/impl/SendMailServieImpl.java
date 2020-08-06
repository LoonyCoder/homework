package com.lagou.service.impl;

import com.lagou.edu.service.SendMailServie;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendMailServieImpl implements SendMailServie {

    @Value("${email.password}")
    String  email_pwd;

    @Value("${email.from}")
    String from;

    Properties properties ;

    public  SendMailServieImpl(){
        properties= new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.debug", "true");
    }

    public boolean sendMail(String email, String code){

        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        try {
            message.setFrom(new InternetAddress(from));
            // 设置收件人邮箱地址
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));//一个收件人
            // 设置邮件标题
            message.setSubject("注册验证");
            // 设置邮件内容
            message.setText("你的验证码是: "+code+ "请尽快完成注册，10分钟内有效");
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户
            transport.connect(from, email_pwd);//
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }
}
