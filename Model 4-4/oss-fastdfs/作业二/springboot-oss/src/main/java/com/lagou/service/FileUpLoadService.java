package com.lagou.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.lagou.bean.OSSResult;
import com.lagou.config.AliyunConfig;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;
@Service
public class FileUpLoadService {
    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    private OSSClient  ossClient;

    double max = 5;
    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".jpg",
            ".jpeg", ".png"};
    public OSSResult upload(MultipartFile multipartFile){
        // 校验图片格式
        boolean  isLegal = false;
        for (String type:IMAGE_TYPE){
            if(StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(),type)){
                isLegal = true;
                break;
            }
        }

        OSSResult upLoadResult = new OSSResult();
        double fileSize = (double) multipartFile.getSize() / 1048576;
        if(fileSize > max){
               upLoadResult.setStatus("error, file too large");
               return  upLoadResult;
        }
        
        if (!isLegal){
            upLoadResult.setStatus("error");
            return  upLoadResult;
        }
        String fileName = multipartFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        try {
            ossClient.putObject(aliyunConfig.getBucketName(),filePath,
                    new ByteArrayInputStream(multipartFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            // 上传失败
            upLoadResult.setStatus("error");
            return  upLoadResult;
        }
        upLoadResult.setStatus("done");
        upLoadResult.setName(aliyunConfig.getUrlPrefix()+filePath);
        upLoadResult.setUid(filePath);
        return  upLoadResult;
    }
    // 生成不重复的文件路径和文件名
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + UUID.randomUUID().toString() + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }

    public OSSResult delete(String fileName) {
        ossClient.deleteObject(aliyunConfig.getBucketName(),fileName);
        OSSResult result = new OSSResult();
        result.setStatus("done");
        result.setUid(fileName);
        return  result;

    }

    public void download(String fileName, HttpServletResponse response) throws IOException {
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getBucketName(), fileName);
        BufferedInputStream bs =null;
        OutputStream os = null;
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition","attachment;fileName=test.png");
        try {
            bs = new BufferedInputStream(ossObject.getObjectContent());
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bs.read(buffer);

            while (i != -1){
               os.write(buffer,0,i);
               i= bs.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bs.close();
            os.close();
        }


    }
}
