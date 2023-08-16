package io.github.eeroom.springmvc.controller;

import io.github.eeroom.springmvc.aspnet.AspNetHttpContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;


@RestController
public class YunpanController implements ApplicationContextAware {
    ApplicationContext applicationContext;

    /*

     */
    @RequestMapping("/download")
    public void  download(){
        var context= this.applicationContext.getBean(AspNetHttpContext.class);
        var response=context.getResponse();
        var filePath="C:\\Users\\Administrator\\Downloads\\VSCodeSetup-x64-1.61.1.exe";
        var file=new File(filePath);
        var fileName=file.getName();
        var fileNameEncoding= URLEncoder.encode(fileName, Charset.forName("utf-8"));
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",String.format("attachment;filename*=utf-8'zh_cn'%s",fileNameEncoding));
        response.setHeader("Content-Length",String.valueOf(file.length()));
        var length=0;
        var bufferSize=1;
        var buffer=new byte[bufferSize];
        try (var fs=new FileInputStream(file)){

            while ((length=fs.read(buffer,0,bufferSize))>0){
                response.getOutputStream().write(buffer,0,length);
                response.flushBuffer();
            }
        } catch (FileNotFoundException e) {
            throw  new RuntimeException(String.format("指定的文件不存,路径:%s",filePath),e);
        } catch (IOException e) {
            throw new RuntimeException(String.format("文件读写发送异常,请检查权限，文件大小等，路径:%s",filePath),e);
        }
    }

    /**
     * todo 分片上传文件
     */
    @RequestMapping("/uploadfile")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Throwable{
        response.getWriter().write("hell11111111111111111");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
