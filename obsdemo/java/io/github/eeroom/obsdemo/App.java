package io.github.eeroom.obsdemo;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.GetObjectRequest;
import com.obs.services.model.ListObjectsRequest;
import com.obs.services.model.ObjectListing;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class App {
    static  String ak="";
    static  String sk="";

    public static void main(String[] args) throws Throwable {
        //listobj();
        var cfg = new ObsConfiguration();
        cfg.setSocketTimeout(30 * 1000);
        cfg.setConnectionTimeout(10 * 1000);
        var endpoint = "https://obs.cn-south-1.myhuaweicloud.com";
        cfg.setEndPoint(endpoint);

        var bucketName = "eeroom-win";
        var obsclient = new ObsClient(ak, sk, cfg);


        var localFileName="d:/obs-download/Silverlight5.1.50918.0-运行时.zip";
        var file= new File(localFileName);
        var pFile=file.getParentFile();
        if(!pFile.exists()){
            pFile.mkdir();
        }
        if(!file.exists()){
            file.createNewFile();
        }

        //断点续传
        var raf=new RandomAccessFile(file,"rw");
        var fileLength= raf.length();
        raf.seek(fileLength);
        var remoteFileName="Silverlight5.1.50918.0-运行时.zip";
        var request=new GetObjectRequest(bucketName,remoteFileName);
        request.setRangeStart(fileLength);

        var filemeta=obsclient.getObjectMetadata(bucketName,remoteFileName,null);
        var startposition=filemeta.getContentLength();
        if(fileLength>startposition){
            throw  new RuntimeException("本地文件的长度超过obs上的文件长度！");
        }
        if(fileLength==startposition.longValue() ){
            return;
        }
        request.setRangeEnd(startposition);
        var md5=filemeta.getContentMd5();
        try(var fs=obsclient.getObject(request).getObjectContent();raf) {
                var buffer=new byte[8*1024];
                int length=0;
                while ((length=fs.read(buffer))>0){
                    raf.write(buffer,0,length);
                    //break;
                }
        }


    }

    private static void listobj() {
        var cfg = new ObsConfiguration();
        cfg.setSocketTimeout(30 * 1000);
        cfg.setConnectionTimeout(10 * 1000);
        var endpoint = "https://obs.cn-south-1.myhuaweicloud.com";
        cfg.setEndPoint(endpoint);

        var obsclient = new ObsClient(ak, sk, cfg);

        var bucketName = "eeroom-win";

        var listObjectsRequest = new ListObjectsRequest(bucketName);
        //设定本次最大的获取对象个数
        listObjectsRequest.setMaxKeys(2);
        //对象key的前缀，区分大小写，对于有目录层级的情况，那么wch/mm/Sisaa.txt是不符合的
        //listObjectsRequest.setPrefix("Si");
        var lstFile = new ArrayList<FileInfo>();
        String marker = null;
        ObjectListing objlist=null;
        do {
            listObjectsRequest.setMarker(marker);
            objlist = obsclient.listObjects(listObjectsRequest);

            marker = objlist.getNextMarker();
            var lsttmp = objlist.getObjects().stream().map(x -> new FileInfo(x.getObjectKey(), x.getMetadata().getContentLength(), x.getMetadata().getLastModified()))
                    .collect(Collectors.toList());
            lstFile.addAll(lsttmp);
        }while (objlist.isTruncated());
        System.out.print("hello world");
    }
}
