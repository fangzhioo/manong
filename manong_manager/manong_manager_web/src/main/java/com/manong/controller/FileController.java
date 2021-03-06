package com.manong.controller;

import com.manong.fastdfs.FastDFSClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pojo.ResponseJsonResult;

import java.io.IOException;

@Controller
public class FileController {

    @RequestMapping(value = "uploadfile",method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonResult fileupload(@RequestParam MultipartFile uploadfile) throws IOException {
        if(uploadfile == null){
            return null;
        }
        String fileID = FastDFSClient.uploadFile(uploadfile.getInputStream(),uploadfile.getOriginalFilename());
        if(fileID != null){
            System.out.print("上传文件成功\n");
        }

        ResponseJsonResult responseJsonResult = new ResponseJsonResult();
        responseJsonResult.setStatus(200);
        responseJsonResult.setMsg(fileID);
        return responseJsonResult;
    }
}
