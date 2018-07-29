package smart.industry.train.web.controllers;

import freemarker.template.utility.DateUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import smart.industry.utils.entity.MultipartFileParam;
import smart.industry.utils.files.MultipartFileUploadUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件处理请求
 */
@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/uploader")
    public String uploader(){
        return "manager/file_uploader";
    }
    /**
     * 文件上传具体实现方法;
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file,HttpServletRequest req) {
        if (!file.isEmpty()) {
            try {
                //获取解决方案ID
                System.out.println(req.getParameter("solutionId"));
                // 文件保存路径
                String filePath = request.getSession().getServletContext().getRealPath("/uploader/");
                File fileSourcePath = new File(filePath);
                File fileSource = new File(fileSourcePath, file.getOriginalFilename());
                System.out.println(fileSource.getPath());
                if (!fileSourcePath.exists()) {
                    fileSourcePath.mkdirs();
                }
                if (fileSource.exists()) {
                    fileSource.delete();
                }
                file.transferTo(fileSource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功";
        } else {
            return "上传失败，因为文件是空的.";
        }
    }
}
