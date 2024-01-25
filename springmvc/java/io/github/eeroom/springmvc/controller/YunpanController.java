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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
public class YunpanController implements ApplicationContextAware {
    ApplicationContext applicationContext;

    /*

     */
    @RequestMapping("/download")
    public void  download(){
        var context= this.applicationContext.getBean(AspNetHttpContext.class);
        var response=context.getResponse();
        var filePath="D:\\Code\\openssl-1.1.1w.tar.gz";
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

    @RequestMapping("/dir")
    public void download4dir() throws Throwable{
        var context= this.applicationContext.getBean(AspNetHttpContext.class);
        var response=context.getResponse();
        response.setContentType("application/octet-stream");
        var dirpath="D:\\01Tools\\camunda-modeler-4.9.0-win-x64\\";
        File dir4zip = new File(dirpath).getCanonicalFile();
        var fileName=dir4zip.getName()+".zip";
        var fileNameEncoding= URLEncoder.encode(fileName, Charset.forName("utf-8"));
        response.setHeader("Content-Disposition",String.format("attachment;filename*=utf-8'zh_cn'%s",fileNameEncoding));
        var bufferSize=8192;
        response.setBufferSize(bufferSize);

        var lstFile=new ArrayList<File>();
        var lstDir=new LinkedList<File>();
        lstDir.push(dir4zip);
        while (lstDir.size()>0){
            for (var file:lstDir.pop().listFiles()){
                if(file.isDirectory()){
                    lstDir.push(file);
                }else if(file.isFile()){
                    lstFile.add(file);
                }
            }
        }
        var length=0;
        var buffer=new byte[bufferSize];

        java.util.zip.ZipOutputStream zipOutputStream=new java.util.zip.ZipOutputStream(response.getOutputStream());
        for (var file:lstFile){
            var entryName= file.getCanonicalPath().replace(dir4zip.getCanonicalPath()+File.pathSeparator,"");
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            try(var fs=new FileInputStream(file)){
                while ((length=fs.read(buffer,0,bufferSize))>0){
                    zipOutputStream.write(buffer,0,length);
                }
            }
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
        response.flushBuffer();
    }

    private static void addFilesToZip(ZipOutputStream zos, File fileToAdd, String parentPath) throws IOException {
        if (parentPath == null || parentPath.isEmpty()) {
            parentPath = "";
        } else {
            parentPath += "/";
        }

        for (File file : fileToAdd.listFiles()) {
            if (file.isDirectory()) {
                addFilesToZip(zos, file, parentPath + file.getName());
            } else {
                byte[] buffer = new byte[1024];

                FileInputStream fis = new FileInputStream(file);
                zos.putNextEntry(new ZipEntry(parentPath + file.getName()));

                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                fis.close();
            }
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
