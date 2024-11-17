package com.example.seckill.utils;

import com.example.seckill.dto.LoginDto;
import com.example.seckill.pojo.User;
import com.example.seckill.service.UserService;
import com.example.seckill.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * 生成用户工具类
 *
 * @author: LC
 * @date 2022/3/4 3:29 下午
 * @ClassName: UserUtil
 */
@Component
public class UserUtil {

    @Autowired
    UserService userService;

    public void CreateUser() throws IOException {
        List<User> list = new ArrayList<>();
//        生成用户
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setId(13000000000L + i);
            user.setNickname("user" + i);
            user.setSalt("1a2b3c");
            user.setPassword("e9ae97ec209c4aa113d921083f767bd7");
            list.add(user);
        }
        userService.saveBatch(list);
        String urlString = "http://localhost:8080/login/tologin";
        File file = new File("D:\\completedProject\\SecKill\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            co.setRequestProperty("Content-Type", "application/json");
            OutputStream out = co.getOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            String params = objectMapper.writeValueAsString(new LoginDto(user.getId().toString(), "18525493565"));
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = ((String) respBean.getObject());
            System.out.println("create userTicket : " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();
        System.out.println("over");


    }

}
