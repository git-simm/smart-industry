package smart.industry.train.biz.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.base.BaseBiz;
import smart.industry.train.biz.entity.*;
import smart.industry.train.biz.entity.base.Paging;
import smart.industry.train.biz.enums.FileTypeEnum;
import smart.industry.train.biz.enums.TaskStateEnum;
import smart.industry.train.biz.mapper.DesignClassMapper;
import smart.industry.train.biz.mapper.DesignSolutionMapper;
import smart.industry.utils.StringUtils;
import smart.industry.utils.environment.EnvUtil;
import smart.industry.utils.exceptions.AjaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 设计方案业务逻辑层
 */
@Service
public class DesignSolutionBiz extends BaseBiz<DesignSolutionMapper, DesignSolution> {
    @Autowired
    private SysUpfilesBiz sysUpfilesBiz;
    @Autowired
    private DesignSolutionListBiz designSolutionListBiz;
    @Autowired
    private DesignDetailBlockBiz designDetailBlockBiz;
    @Autowired
    private SysTasksBiz sysTasksBiz;
    @Autowired
    private DesignClassBiz designClassBiz;
    @Autowired
    private DesignClassMapper designClassMapper;

    @Override
    public DesignSolution getFilter(Paging paging) throws IllegalAccessException, InstantiationException {
        DesignSolution filter = DesignSolution.class.newInstance();
        filter.setName(paging.getSearchKey());
        String filterSeq = "name like concat('%',#{name},'%')";
        if(paging.getClassId()!=null){
            List<String> classIds = getAllClassIds(paging.getClassId()).stream().map(a->a.toString()).collect(Collectors.toList());
            filterSeq+=" and classId in ("+ String.join(",",classIds) +")";
        }
        filter.setFilter(filterSeq);
        return filter;
    }

    /**
     * 获取组织架构下所有的信息
     * @param parentId
     * @return
     */
    public List<Integer> getAllClassIds(Integer parentId){
        List<Integer> result = new ArrayList<>();
        List<DesignClass> list = designClassBiz.getList();
        getAllClassIds(list,result,parentId);
        return result;
    }

    /**
     * 获取所有组织架构信息
     * @param list
     * @param result
     * @param parentId
     * @return
     */
    private void getAllClassIds(List<DesignClass> list,List<Integer> result,Integer parentId){
        if(result.contains(parentId)) return;
        result.add(parentId);
        //查找子级
        List<Integer> children = list.stream().filter(a-> parentId.equals(a.getPId())).map(a->a.getId()).collect(Collectors.toList());
        children.stream().forEach(a->{
            getAllClassIds(list,result,a);
        });
    }

    /**
     * 获取方案列表
     *
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
     *
     * @param id
     * @return
     */
    @Transactional
    public Integer delSolution(Integer id) {
        Integer result = this.delete(id);
        List<DesignSolutionList> files = designSolutionListBiz.getAllListBySolution(id);
        //删除方案
        result += delFiles2(files);
        return result;
    }

    /**
     * 删除文件
     *
     * @param files
     * @return
     */
    @Transactional
    public Integer delFiles2(List<DesignSolutionList> files) {
        Integer result = 0;
        if (CollectionUtils.isEmpty(files)) return result;
        //准备删除方案列表对应的文件
        for (DesignSolutionList detail : files) {
            if (detail != null && detail.getFileId() != null) {
                result += sysUpfilesBiz.delete(detail.getFileId());
            }
            result += designSolutionListBiz.delete(detail.getId());
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param files
     * @return
     */
    @Transactional
    public Integer delFiles(List<Integer> files) {
        Integer result = 0;
        if (CollectionUtils.isEmpty(files)) return result;
        //准备删除方案列表对应的文件
        for (Integer file : files) {
            DesignSolutionList detail = designSolutionListBiz.selectByPrimaryKey(file);
            if (detail != null && detail.getFileId() != null) {
                result += sysUpfilesBiz.delete(detail.getFileId());
            }
            result += designSolutionListBiz.delete(file);
        }
        return result;
    }

    @Autowired
    private DesignSolutionPartionBiz designSolutionPartionBiz;
    /**
     * 获取文件树
     * @param id
     * @param cardId
     * @param queryLink
     * @return
     */
    @Transactional
    public List<JSONObject> getFileTree(Integer id, Integer cardId, boolean queryLink) {
        //1.先获取卡片上对应的文件信息列表
        List<Integer> fileList = new ArrayList<>();
        if(cardId!=null){
            List<DesignSolutionPartion> list = designSolutionPartionBiz.getCards(null,cardId);
            if(list.size()>0){
                fileList = list.get(0).getPartionList().stream().map(a->a.getSolutionFileId()).collect(Collectors.toList());
            }
        }
        DesignSolution solution = this.selectByPrimaryKey(id);
        if (solution == null) {
            throw new AjaxException("不存在主键为[" + id + "]的解决方案");
        }
        List<DesignSolutionList> solutionFiles = designSolutionListBiz.getAllListBySolution(id);
        if(!CollectionUtils.isEmpty(fileList)){
            List<Integer> finalFileList = fileList;
            solutionFiles = solutionFiles.stream().filter(a-> finalFileList.contains(a.getId())).collect(Collectors.toList());
        }

        List<Integer> ids = solutionFiles.stream().map(a -> a.getFileId()).collect(Collectors.toList());
        List<SysUpfiles> files = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            String param = StringUtils.join(ids, ",");
            SysUpfiles filter = new SysUpfiles();
            filter.setFilter("id in (" + param + ")");
            files = sysUpfilesBiz.selectByFilter(filter);
        }
        //开始拼装树状结构
        List<JSONObject> result = new ArrayList<>();
        result.addAll(wrapData(solutionFiles, files, FileTypeEnum.Design, queryLink));
        result.addAll(wrapData(solutionFiles, files, FileTypeEnum.Standard, queryLink));
        result.addAll(wrapData(solutionFiles, files, FileTypeEnum.Bill, queryLink));
        return result;
    }

    /**
     * 组装数据
     *
     * @param solutionFiles
     * @param files
     * @param type
     * @param queryLink
     * @return
     */
    public List<JSONObject> wrapData(List<DesignSolutionList> solutionFiles, List<SysUpfiles> files, FileTypeEnum type, boolean queryLink) {
        List<JSONObject> result = new ArrayList<>();
        JSONObject root = new JSONObject();
        String rootId = "10000" + type.getValue();
        root.put("id", rootId);
        root.put("pId", null);
        root.put("name", type.getName());
        root.put("projFile",type.getName());
        root.put("fileId", null);
        root.put("filePath", null);
        result.add(root);

        List<DesignSolutionList> solutionListsFilter = solutionFiles.stream().filter(a -> a.getType().equals(type.getValue())).collect(Collectors.toList());
        solutionListsFilter.forEach(a -> {
            JSONObject obj = new JSONObject();
            obj.put("id", a.getId());
            obj.put("pId", rootId);
            obj.put("fileId", a.getFileId());
            Optional<SysUpfiles> fileOp = files.stream().filter(f -> f.getId().equals(a.getFileId())).findFirst();
            if (fileOp.isPresent()) {
                SysUpfiles file = fileOp.get();
                String path = file.getRelativePath();
                if (StringUtils.isNotBlank(path) && file.getSuffix().equals(".dxf")) {
                    path = file.getRelativePath().replace(file.getSuffix(), ".svg");
                }
                String projPath = file.getProjPath(), fileName = file.getFileName().replace(file.getSuffix(), "");
                obj.put("filePath", path);
                obj.put("suffix", file.getSuffix());
                obj.put("fileName", fileName);
                obj.put("projPath", projPath);
                if (StringUtils.isNotBlank(projPath)) {
                    fileName = projPath.substring(projPath.indexOf("|") + 1);
                    obj.put("folderPath", fileName.substring(0, fileName.lastIndexOf("|")));
                    obj.put("relName", fileName.substring(fileName.lastIndexOf("|") + 1));
                } else {
                    obj.put("folderPath", fileName);
                    obj.put("relName", "0");
                }
                obj.put("name", fileName);
                String projFile = file.getProjFile();
                if(StringUtils.isBlank(projFile)){
                    obj.put("projFile",fileName);
                }else{
                    obj.put("projFile",fileName+"("+projFile+")");
                }
                if(queryLink){
                    //这个位置需要做性能优化，一次查询所有的相关数据
                    obj.put("linkMap", designDetailBlockBiz.getLinkMap(a.getId()));
                }
            }
            result.add(obj);
        });
        //对结果进行排序
        result.sort((o1, o2) -> {
            if (StringUtils.isBlank(o1.getString("folderPath"))) return -1;
            if (StringUtils.isBlank(o2.getString("folderPath"))) return 1;
            if (o1.getString("folderPath").equals(o2.getString("folderPath"))) {
                if (o1.getString("relName").length() == (o2.getString("relName").length())) {
                    return o1.getString("relName").compareTo(o2.getString("relName"));
                } else {
                    return o1.getString("relName").length() - o2.getString("relName").length();
                }
            }
            return o1.getString("folderPath").compareTo(o2.getString("folderPath"));
        });
        return result;
    }

    /**
     * 文件数量统计
     *
     * @param id
     * @return
     */
    @Transactional
    public Integer fileSummary(Integer id) {
        //获取所有的文件，归类进行处理
        List<DesignSolutionList> solutionFiles = designSolutionListBiz.getAllListBySolution(id);
        solutionFiles.stream().forEach(a -> {
            //获取文件，生产解析任务
            saveSysTask(a.getId());
        });
        Long designCount = solutionFiles.stream().filter(a -> a.getType().equals(FileTypeEnum.Design.getValue())).count();
        Long standardCount = solutionFiles.stream().filter(a -> a.getType().equals(FileTypeEnum.Standard.getValue())).count();
        Long billCount = solutionFiles.stream().filter(a -> a.getType().equals(FileTypeEnum.Bill.getValue())).count();
        DesignSolution designSolution = baseMapper.selectByPrimaryKey(id);
        designSolution.setBillCount(billCount.intValue());
        designSolution.setDesignCount(designCount.intValue());
        designSolution.setStandardCount(standardCount.intValue());
        return baseMapper.updateByPrimaryKeySelective(designSolution);
    }

    /**
     * 保存系统任务
     * *
     *
     * @param @param   solutionId
     * @param detailId
     */
    private void saveSysTask(Integer detailId) {
        //判断任务是否已经存在
        SysTasks filter = new SysTasks();
        filter.setDetailId(detailId);
        filter.setFilter("detailId=#{detailId}");
        List<SysTasks> exists = sysTasksBiz.selectByFilter(filter);
        if (CollectionUtils.isEmpty(exists)) {
            SysTasks item = new SysTasks();
            item.setDetailId(detailId);
            item.setState(TaskStateEnum.Ready.getValue());
            item.setMachine(EnvUtil.getMachineName());
            sysTasksBiz.add(item);
        }
    }
}
