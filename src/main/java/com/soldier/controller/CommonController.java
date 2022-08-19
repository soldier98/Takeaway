package com.soldier.controller;

import com.soldier.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 【文件上传下载】
 *
 * @author soldier97
 * @date 2022/8/2 16:09
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${takeaway.path}")
    private String basePath;

    /**
     * @param file
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //springboot 封装好的，只需要用multipartFile就可以接受浏览器传过来的文件
     * @Date 2022/8/3
     **/
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        //取得原文件名字
        String originalFilename = file.getOriginalFilename();
        //取得文件后缀名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //UUID算法生成存在服务器的名字,防止文件名重复
        String fileName = UUID.randomUUID()+substring;

        //将file里的文件转存到服务器
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Result<>(1,"",fileName);

    }

    /**
     * @param name
     * @param response
     * @return void
     * @Author soldier
     * @Description //文件下载
     * @Date 2022/8/2
     **/
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //创建输入流，读取文件信息
        InputStream inputStream = null;

        //创建写出流
        ServletOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(basePath + name));
            outputStream = response.getOutputStream();


            byte[] temp = new byte[1024];
            int len;
            while ((len = inputStream.read(temp)) != -1) {
                outputStream.write(temp, 0, len);
                outputStream.flush();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }


    }

}
