package smart.industry.utils.files;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import smart.industry.utils.entity.MultipartFileParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MultipartFileUploadUtil {

    /**
     * 在HttpServletRequest中获取分段上传文件请求的信息
     *
     * @param request
     * @return
     */
    public static MultipartFileParam parse(HttpServletRequest request) throws Exception {
        MultipartFileParam param = new MultipartFileParam();

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        param.setMultipart(isMultipart);
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 得到所有的表单域，它们目前都被当作FileItem
            List<FileItem> fileItems = upload.parseRequest((RequestContext) request);
            for (FileItem fileItem : fileItems) {
                System.out.println("field name has:" + fileItem.getFieldName());
                if (!"file".equals(fileItem.getFieldName())) {
                    System.out.println("field val has:" + fileItem.getString());
                }

                if (fileItem.getFieldName().equals("id")) {
                    param.setId(fileItem.getString());
                } else if (fileItem.getFieldName().equals("name")) {
                    param.setFileName(new String(fileItem.getString().getBytes(
                            "ISO-8859-1"), "UTF-8"));
                } else if (fileItem.getFieldName().equals("chunks")) {
                    param.setChunks(Integer.parseInt(fileItem.getString()));
                } else if (fileItem.getFieldName().equals("chunk")) {
                    param.setChunk(Integer.parseInt(fileItem.getString()));
                } else if (fileItem.getFieldName().equals("file")) {
                    param.setFileItem(fileItem);
                    param.setSize(fileItem.getSize());
                } else {
                    param.getParam().put(fileItem.getFieldName(), fileItem.getString());
                }
            }
        }
        return param;
    }
}