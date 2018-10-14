package smart.industry.train.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import smart.industry.train.biz.dao.DesignSolutionListBiz;
import smart.industry.train.biz.dao.SysTasksBiz;
import smart.industry.train.biz.dao.SysUpfilesBiz;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.SysTasks;
import smart.industry.train.biz.entity.SysUpfiles;
import smart.industry.train.biz.enums.TaskStateEnum;
import smart.industry.utils.files.FileUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    @Autowired
    private SysTasksBiz sysTasksBiz;

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
                // 文件保存路径(获取资源路径 uploader)
                String suffix = FileUtil.getSuffix(file.getOriginalFilename());
                String fileName = UUID.randomUUID().toString()+ suffix;
                String folder = "/static/uploader/";
                String relativePath = folder +fileName;
                File fileSourcePath = new File(ResourceUtils.getFile("classpath:").getPath()+ folder);
                File fileSource = new File(fileSourcePath, fileName);
                System.out.println(fileSource.getPath());
                if (!fileSourcePath.exists()) {
                    fileSourcePath.mkdirs();
                }
                if (fileSource.exists()) {
                    fileSource.delete();
                }
                file.transferTo(fileSource);
                //保存文件到数据库
                Integer fileId = saveFile(file, fileSource,relativePath);
                Integer detailId = saveDesignList(file, solutionId, fileType, fileId);
                //保存系统同步任务
                saveSysTask(detailId);
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
    private Integer saveDesignList(MultipartFile file, Integer solutionId, Integer fileType, Integer fileId) {
        DesignSolutionList item = new DesignSolutionList();
        item.setFileId(fileId);
        item.setSolutionId(solutionId);
        item.setType(fileType);
        item.setName(file.getOriginalFilename());
        designSolutionListBiz.add(item);
        return item.getId();
    }

    /**
     * 保存系统任务
     *      *
     * @param  @param solutionId
     * @param detailId
     */
    private void saveSysTask(Integer detailId) {
        SysTasks item = new SysTasks();
        item.setDetailId(detailId);
        item.setState(TaskStateEnum.Ready.getValue());
        sysTasksBiz.add(item);
    }

    /**
     * 保存物理文件
     * @param file
     * @param fileSource
     * @param relativePath
     * @return
     */
    private Integer saveFile(MultipartFile file, File fileSource, String relativePath) {
        SysUpfiles sysUpfiles = new SysUpfiles();
        sysUpfiles.setFileName(file.getOriginalFilename());
        sysUpfiles.setFilePath(fileSource.getPath());
        sysUpfiles.setSuffix(FileUtil.getSuffix(file.getOriginalFilename()));
        sysUpfiles.setFileSize(file.getSize());
        sysUpfiles.setRelativePath(relativePath);
        sysUpfilesBiz.add(sysUpfiles);
        return sysUpfiles.getId();
    }
}
