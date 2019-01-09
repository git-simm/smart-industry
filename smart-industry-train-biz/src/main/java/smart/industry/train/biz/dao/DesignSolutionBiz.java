package smart.industry.train.biz.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.DesignDetailBlock;
import smart.industry.train.biz.entity.DesignSolution;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.SysUpfiles;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.enums.FileTypeEnum;
import smart.industry.train.biz.mapper.DesignSolutionMapper;
import smart.industry.utils.StringUtils;
import smart.industry.utils.exceptions.AjaxException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 设计方案业务逻辑层
 */
@Service
public class DesignSolutionBiz extends BaseBiz<DesignSolutionMapper,DesignSolution> {
    @Autowired
    private SysUpfilesBiz sysUpfilesBiz;
    @Autowired
    private DesignSolutionListBiz designSolutionListBiz;
    @Autowired
    private DesignDetailBlockBiz designDetailBlockBiz;

    @Override
    public DesignSolution getFilter(Paging paging) throws IllegalAccessException, InstantiationException {
        DesignSolution filter = DesignSolution.class.newInstance();
        filter.setName(paging.getSearchKey());
        filter.setFilter("name like concat('%',#{name},'%')");
        return filter;
    }

    /**
     * 获取方案列表
     * @param classId
     * @return
     */
    public List<DesignSolution> getListByClassId(Integer classId) {
        DesignSolution filter = new DesignSolution();
        filter.setClassId(classId);
        filter.setFilter("classId=#{classId}");
        return selectByFilter(filter);
    }

    /**
     * 删除方案相关信息
     * @param id
     * @return
     */
    @Transactional
    public Integer delSolution(Integer id){
        Integer result = this.delete(id);
        List<DesignSolutionList> files = designSolutionListBiz.getAllListBySolution(id);
        //删除方案
        result += delFiles2(files);
        return result;
    }
    /**
     * 删除文件
     * @param files
     * @return
     */
    @Transactional
    public Integer delFiles2(List<DesignSolutionList> files) {
        Integer result = 0;
        if(CollectionUtils.isEmpty(files)) return result;
        //准备删除方案列表对应的文件
        for(DesignSolutionList detail : files){
            if(detail!=null && detail.getFileId()!=null){
                result += sysUpfilesBiz.delete(detail.getFileId());
            }
            result += designSolutionListBiz.delete(detail.getId());
        }
        return result;
    }
    /**
     * 删除文件
     * @param files
     * @return
     */
    @Transactional
    public Integer delFiles(List<Integer> files) {
        Integer result = 0;
        if(CollectionUtils.isEmpty(files)) return result;
        //准备删除方案列表对应的文件
        for(Integer file : files){
            DesignSolutionList detail = designSolutionListBiz.selectByPrimaryKey(file);
            if(detail!=null && detail.getFileId()!=null){
                result += sysUpfilesBiz.delete(detail.getFileId());
            }
            result += designSolutionListBiz.delete(file);
        }
        return result;
    }

    /**
     * 获取文件树
     * @param id
     * @return
     */
    @Transactional
    public List<JSONObject> getFileTree(Integer id) {
        DesignSolution solution = this.selectByPrimaryKey(id);
        if(solution==null){
            throw new AjaxException("不存在主键为["+id+"]的解决方案");
        }
        List<DesignSolutionList> solutionFiles = designSolutionListBiz.getAllListBySolution(id);
        List<Integer> ids = solutionFiles.stream().map(a -> a.getFileId()).collect(Collectors.toList());
        List<SysUpfiles> files = new ArrayList<>();
        if(!CollectionUtils.isEmpty(ids)){
            String param = StringUtils.join(ids,",");
            SysUpfiles filter = new SysUpfiles();
            filter.setFilter("id in ("+ param +")");
            files = sysUpfilesBiz.selectByFilter(filter);
        }
        //开始拼装树状结构
        List<JSONObject> result = new ArrayList<>();
        result.addAll(wrapData(solutionFiles,files,FileTypeEnum.Design));
        result.addAll(wrapData(solutionFiles,files,FileTypeEnum.Standard));
        result.addAll(wrapData(solutionFiles,files,FileTypeEnum.Bill));
        return result;
    }

    /**
     * 组装数据
     * @param solutionFiles
     * @param files
     * @param type
     * @return
     */
    public List<JSONObject> wrapData(List<DesignSolutionList> solutionFiles,List<SysUpfiles> files ,FileTypeEnum type){
        List<JSONObject> result = new ArrayList<>();
        JSONObject root = new JSONObject();
        String rootId = "10000"+type.getValue();
        root.put("id" ,rootId );
        root.put("pId" ,null );
        root.put("name" ,type.getName() );
        root.put("fileId",null);
        root.put("filePath",null);
        result.add(root);

        List<DesignSolutionList> solutionListsFilter = solutionFiles.stream().filter(a->a.getType().equals(type.getValue())).collect(Collectors.toList());
        solutionListsFilter.forEach(a->{
            JSONObject obj = new JSONObject();
            obj.put("id",a.getId());
            obj.put("pId",rootId);
            obj.put("fileId",a.getFileId());
            Optional<SysUpfiles> fileOp = files.stream().filter(f->f.getId().equals(a.getFileId())).findFirst();
            if(fileOp.isPresent()){
                SysUpfiles file = fileOp.get();
                String path = file.getRelativePath();
                if(StringUtils.isNotBlank(path) && file.getSuffix().equals(".dxf")){
                    path = file.getRelativePath().replace(file.getSuffix(),".svg");
                }
                String projPath = file.getProjPath(),fileName = file.getFileName().replace(file.getSuffix(),"");
                obj.put("filePath",path);
                obj.put("fileName",fileName);
                obj.put("projPath",projPath);
                //String fileName = file.getFileName().replace(file.getSuffix(),"");
                if(StringUtils.isNotBlank(projPath)){
                    fileName = projPath.substring(projPath.indexOf("|")+1);
                    obj.put("folderPath",fileName.substring(0,fileName.lastIndexOf("|")));
                    obj.put("relName" ,fileName.substring(fileName.lastIndexOf("|")+1));
                }else{
                    obj.put("folderPath",fileName);
                    obj.put("relName","0");
                }
                obj.put("name",fileName);
                //这个位置需要做性能优化，一次查询所有的相关数据
                obj.put("linkMap",designDetailBlockBiz.getLinkMap(a.getId()));
            }
            result.add(obj);
        });
        //对结果进行排序
        result.sort((o1,o2)->{
            if(StringUtils.isBlank(o1.getString("folderPath"))) return -1;
            if(StringUtils.isBlank(o2.getString("folderPath"))) return 1;
            if(o1.getString("folderPath").equals(o2.getString("folderPath"))){
                if(o1.getString("relName").length() == (o2.getString("relName").length())){
                    return o1.getString("relName").compareTo(o2.getString("relName"));
                }else{
                    return o1.getString("relName").length() - o2.getString("relName").length();
                }
            }
            return o1.getString("folderPath").compareTo(o2.getString("folderPath"));
        });
        return result;
    }

    /**
     * 文件数量统计
     * @param id
     * @return
     */
    @Transactional
    public Integer fileSummary(Integer id) {
        //获取所有的文件，归类进行处理
        List<DesignSolutionList> solutionFiles = designSolutionListBiz.getAllListBySolution(id);
        Long designCount = solutionFiles.stream().filter(a->a.getType().equals(FileTypeEnum.Design.getValue())).count();
        Long standardCount = solutionFiles.stream().filter(a->a.getType().equals(FileTypeEnum.Standard.getValue())).count();
        Long billCount = solutionFiles.stream().filter(a->a.getType().equals(FileTypeEnum.Bill.getValue())).count();
        DesignSolution designSolution = baseMapper.selectByPrimaryKey(id);
        designSolution.setBillCount(billCount.intValue());
        designSolution.setDesignCount(designCount.intValue());
        designSolution.setStandardCount(standardCount.intValue());
        return baseMapper.updateByPrimaryKeySelective(designSolution);
    }
}
