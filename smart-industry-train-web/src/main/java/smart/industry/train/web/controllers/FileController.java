package smart.industry.train.web.controllers;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import smart.industry.train.biz.dao.SysUpfilesBiz;
import smart.industry.train.biz.dao.base.DesignSolutionListBiz;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.SysUpfiles;
import smart.industry.utils.entity.MultipartFileParam;
import smart.industry.utils.files.FileUtil;
import smart.industry.utils.files.Md5Util;
import smart.industry.utils.files.MultipartFileUploadUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 文件处理请求
 */
@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SysUpfilesBiz sysUpfilesBiz;
    @Autowired
    private DesignSolutionListBiz designSolutionListBiz;

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
    @Transactional
    public String handleFileUpload(@RequestParam("file") MultipartFile file,HttpServletRequest req) {
        if (!file.isEmpty()) {
            try {
                //MultipartFileParam param = MultipartFileUploadUtil.parse(req);
                //计算文件MD5值
                //Md5Util.getMD5(file);
                //获取解决方案ID
                Integer solutionId = Integer.parseInt(req.getParameter("solutionId"));
                Integer fileType = Integer.parseInt(req.getParameter("fileType"));
                // 文件保存路径
                String filePath = request.getSession().getServletContext().getRealPath("/uploader/");
                File fileSourcePath = new File(filePath);
                String suffix = FileUtil.getSuffix(file.getOriginalFilename());
                File fileSource = new File(fileSourcePath, UUID.randomUUID().toString()+ suffix);
                System.out.println(fileSource.getPath());
                if (!fileSourcePath.exists()) {
                    fileSourcePath.mkdirs();
                }
                if (fileSource.exists()) {
                    fileSource.delete();
                }
                file.transferTo(fileSource);
                //保存文件到数据库
                Integer fileId = saveFile(file, fileSource);
                saveDesignList(file, solutionId, fileType, fileId);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功";
        } else {
            return "上传失败，因为文件是空的.";
        }
    }

    /**
     * 保存方案信息file
     *      *
     * @param  @param solutionId
     * @param fileType
     * @param fileId
     */
    private void saveDesignList(MultipartFile file, Integer solutionId, Integer fileType, Integer fileId) {
        DesignSolutionList item = new DesignSolutionList();
        item.setFileId(fileId);
        item.setSolutionId(solutionId);
        item.setType(fileType);
        item.setName(file.getOriginalFilename());
        designSolutionListBiz.add(item);
    }

    /**
     * 保存物理文件
     * @param file
     * @param fileSource
     * @return
     */
    private Integer saveFile(MultipartFile file, File fileSource) {
        SysUpfiles sysUpfiles = new SysUpfiles();
        sysUpfiles.setFileName(file.getOriginalFilename());
        sysUpfiles.setFilePath(fileSource.getPath());
        sysUpfiles.setSuffix(FileUtil.getSuffix(file.getOriginalFilename()));
        sysUpfiles.setFileSize(file.getSize());
        sysUpfilesBiz.add(sysUpfiles);
        return sysUpfiles.getId();
    }
}
