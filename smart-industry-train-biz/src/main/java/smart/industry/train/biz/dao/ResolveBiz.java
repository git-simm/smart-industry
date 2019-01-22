package smart.industry.train.biz.dao;

import org.apache.ibatis.cache.decorators.BlockingCache;
import org.kabeja.Main;
import org.kabeja.dxf.*;
import org.kabeja.svg.SVGUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import smart.industry.train.biz.entity.*;
import smart.industry.train.biz.enums.TaskStateEnum;
import smart.industry.train.biz.mypoi.DesignXlsBiz;
import smart.industry.train.biz.mypoi.ReferBiz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 解析组件
 */
@Service
public class ResolveBiz {
    private final Logger logger = LoggerFactory.getLogger(ResolveBiz.class);
    /**
     * 设计方案明细
     */
    @Autowired
    private DesignSolutionListBiz designSolutionListBiz;
    @Autowired
    private SysUpfilesBiz sysUpfilesBiz;
    @Autowired
    private SysTasksBiz sysTasksBiz;
    @Autowired
    private SysDxfAttrBiz sysDxfAttrBiz;
    @Autowired
    private DesignDetailBlockBiz designDetailBlockBiz;
    @Autowired
    private DesignBlockAttrBiz designBlockAttrBiz;

    /**
     * 临时变量
     */
    class TempData{
        private DesignSolutionList detail;
        private SysUpfiles file;
        private SysTasks sysTask;

        public DesignSolutionList getDetail() {
            return detail;
        }

        public void setDetail(DesignSolutionList detail) {
            this.detail = detail;
        }

        public SysUpfiles getFile() {
            return file;
        }

        public void setFile(SysUpfiles file) {
            this.file = file;
        }

        public SysTasks getSysTask() {
            return sysTask;
        }

        public void setSysTask(SysTasks sysTask) {
            this.sysTask = sysTask;
        }
    }

    /**
     * 任务解析
     *
     * @param sysTasks
     */
    public void resolveTask(SysTasks sysTasks) {
        TempData data = new TempData();
        data.sysTask = sysTasks;
        data.detail = designSolutionListBiz.selectByPrimaryKey(sysTasks.getDetailId());
        if (data.detail == null) return;
        data.file = sysUpfilesBiz.selectByPrimaryKey(data.detail.getFileId());
        if (data.file == null) return;
        String suffix = data.file.getSuffix();
        boolean succ = false;
        //1.修改系统任务的状态
        data.sysTask.setState(TaskStateEnum.Converting.getValue());
        sysTasksBiz.update(data.sysTask);
        try {
            if (suffix.equals(".dxf")) {
                succ = resolve(data);
            } else if (suffix.equals(".xls") || suffix.equals(".xlsx")) {
                //解析excel清单
                succ = resolveExcel(data);
            } else if (suffix.equals(".txt")) {
                //解析导出文件对应的elcad方案的路径
                succ = resolveRefer(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            //3.修改系统任务的状态
            if (succ) {
                data.sysTask.setState(TaskStateEnum.Completed.getValue());
                sysTasksBiz.update(data.sysTask);
            } else {
                data.sysTask.setState(TaskStateEnum.Ready.getValue());
                sysTasksBiz.update(data.sysTask);
            }
        }
    }

    @Autowired
    private ReferBiz referBiz;

    /**
     * 解析文件dxf在工程中的对应关系
     */
    private boolean resolveRefer(TempData data) throws Exception {
        return referBiz.resolve(data.detail, data.file);
    }

    /**
     * 设计清单的解析
     */
    @Autowired
    private DesignXlsBiz designXlsBiz;

    /**
     * 解析excel清单
     */
    public boolean resolveExcel(TempData data) throws Exception {
        return designXlsBiz.resolve(data.file);
    }

    /**
     * dxf文件转换成svg
     *
     * @throws Exception
     */
    public boolean resolve(TempData data) throws Exception {
        Main main = new Main();
        main.process();
        File f = new File(data.file.getFilePath());
        String suffix = data.file.getSuffix();
        String output = f.getPath().replace(suffix, ".svg");
        if (f.exists() && f.isFile()) {
            //文件转换
            DXFDocument doc = main.parseFile(f, output);
            //完成dxf信息的保存
            saveDxfMsg(doc,data);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * 保存dxf文件信息
     *
     * @param doc
     */
    @Transactional
    public void saveDxfMsg(DXFDocument doc,TempData data) {
        //2.先清理掉，有关改detailId的文件信息
        DesignDetailBlock filter = new DesignDetailBlock();
        filter.setDetailId(data.detail.getId());
        filter.setFilter("detailId = #{detailId}");
        designDetailBlockBiz.deleteByFilter(filter);

        DesignBlockAttr filter2 = new DesignBlockAttr();
        filter2.setDetailId(data.detail.getId());
        filter2.setFilter("detailId = #{detailId}");
        designBlockAttrBiz.deleteByFilter(filter2);

//        Iterator blocks = doc.getDXFBlockIterator();
//        while (blocks.hasNext()) {
//            DXFBlock block = (DXFBlock) blocks.next();
//            //1.保存block信息
//            DesignDetailBlock designDetailBlock = saveBlockMsg(block.getName());
//            Iterator entities = block.getDXFEntitiesIterator();
//            while (entities.hasNext()) {
//                Object entity = entities.next();
//                if (entity instanceof DXFAttdef) {
//                    DXFAttdef dxfAttdef = (DXFAttdef) entity;
//                    //2.解析保存sys_attr信息
//                    Integer attrId = saveDxfAttrMsg(dxfAttdef.getAttr());
//                    //3.保存attr信息
//                    saveBlockAttr(designDetailBlock.getId(), attrId, dxfAttdef.getAttr(), dxfAttdef.getValue());
//                }
//            }
//        }
        /**
         * 解析层
         */
        parseLayer(doc,data);
    }

    /**
     * 格式化layer
     *
     * @param doc
     */
    private void parseLayer(DXFDocument doc,TempData data) {
        //遍历layer
//        Iterator i = doc.getDXFLayerIterator();
//        while (i.hasNext()) {
//            DXFLayer layer = (DXFLayer) i.next();
//            this.parseEntity(doc,layer,data);
//        }
        //解析图片信息
        DXFLayer layer = doc.getDXFLayer("SYMBOL");
        if (layer != null) {
            this.parseEntity(doc, layer,data);
        }
    }

    /**
     * 格式化entity
     *
     * @param doc
     * @param layer
     */
    protected void parseEntity(DXFDocument doc, DXFLayer layer,TempData data) {
        //String lt = layer.getLineType();
        // the stroke-width
        //int lineWeight = layer.getLineWeight();
        // the stroke-width
        Double lw = null;
        Iterator types = layer.getDXFEntityTypeIterator();
        List<DesignBlockAttr> blockAttrs = new ArrayList<>();
        while (types.hasNext()) {
            String type = (String) types.next();
            ArrayList list = (ArrayList) layer.getDXFEntities(type);
            Iterator i = list.iterator();
            while (i.hasNext()) {
                DXFEntity entity = (DXFEntity) i.next();
                if (entity instanceof DXFInsert) {
                    DXFInsert insert = (DXFInsert) entity;
                    //获取key值
                    String ek = getEntityKey(insert);
                    DesignDetailBlock designDetailBlock = saveBlockMsg(insert.getBlockID(),ek,data);
                    //2.解析保存sys_attr信息
                    List<DXFEntity> attrs = insert.getAttrList();//获取图标对应的属性
                    //获取block对象信息
                    DXFBlock block = doc.getDXFBlock(insert.getBlockID());
                    List<DXFAttdef> attdefs = block.getDXFDefList();
                    if(attrs==null) continue;
                    int flag =0;
                    //保存实体信息
                    for (DXFEntity attr : attrs){
                        if(flag >= attdefs.size()) break;
                        //获取属性定义
                        DXFAttdef def = attdefs.get(flag);
                        flag++;
                        DXFAttrib attrib = (DXFAttrib)attr;
                        if(attrib != null){
                            if(def!= null){
                                String attrName = def.getAttr();
                                if(attrName.contains("Representation")|| attrName.contains("Wire")
                                        || attrName.contains("Connector") || attrName.contains("Item")
                                        || attrName.contains("Dest")){
                                    Integer attrId = saveDxfAttrMsg(attrName);
                                    if(StringUtils.isEmpty(attrib.getText()))continue;
                                    //3.保存attr信息(搜集所有的信息，准备批量保存)
                                    blockAttrs.add(getBlockAttr(designDetailBlock.getId(),attrId,attrName,attrib.getText(),data));
                                    //saveBlockAttr(designDetailBlock.getId(),attrId,attrName,attrib.getText());
                                }
                            }
                        }
                    }
                }
            }
        }
        //批量保存
        blockAttrs.forEach(a->{
            designBlockAttrBiz.add(a);
        });
    }

    /**
     * 获取实体的key
     * @param insert
     * @return
     */
    private String getEntityKey(DXFInsert insert){
        return insert.getKey();
    }

    /**
     * 属性缓存
     */
    private static HashMap<String,Integer> attrCache = new HashMap<>();
    private static Object _lock = new Object();
    /**
     * 保存dxf属性信息
     *
     * @param name
     */
    @Transactional
    public Integer saveDxfAttrMsg(String name) {
        if(attrCache.containsKey(name))return attrCache.get(name);
        synchronized (_lock){
            if(attrCache.containsKey(name))return attrCache.get(name);
            //1.读取数据库中待执行的任务列表
            SysDxfAttr filter = new SysDxfAttr();
            filter.setName(name);
            filter.setFilter("name = #{name}");
            List<SysDxfAttr> attrs = sysDxfAttrBiz.selectByFilter(filter);
            if (CollectionUtils.isEmpty(attrs)) {
                //保存成新的记录
                sysDxfAttrBiz.add(filter);
                attrCache.put(name,filter.getId());
                return filter.getId();
            } else {
                attrCache.put(name,attrs.get(0).getId());
                return attrs.get(0).getId();
            }
        }
    }

    /**
     * 保存block信息
     *
     * @param blockName
     */
    @Transactional
    public DesignDetailBlock saveBlockMsg(String blockName,String key,TempData data) {
        DesignDetailBlock block = new DesignDetailBlock();
        block.setDetailId(data.detail.getId());
        block.setName(key);
        block.setSymbolName(blockName);
        designDetailBlockBiz.add(block);
        return block;
    }

    /**
     * 保存block属性信息
     *
     * @param blockId
     * @param attrId
     * @param attrVal
     */
    @Transactional
    public void saveBlockAttr(Integer blockId, Integer attrId, String attrName, String attrVal,TempData data) {
        DesignBlockAttr attr = new DesignBlockAttr();
        attr.setDetailId(data.detail.getId());
        attr.setAttrName(attrName);
        attr.setAttrId(attrId);
        attr.setBlockId(blockId);
        attr.setValue(attrVal);
        designBlockAttrBiz.add(attr);
    }

    /**
     * 获取实体
     * @param blockId
     * @param attrId
     * @param attrName
     * @param attrVal
     * @return
     */
    public DesignBlockAttr getBlockAttr(Integer blockId, Integer attrId, String attrName, String attrVal,TempData data) {
        DesignBlockAttr attr = new DesignBlockAttr();
        attr.setDetailId(data.detail.getId());
        attr.setAttrName(attrName);
        attr.setAttrId(attrId);
        attr.setBlockId(blockId);
        attr.setValue(attrVal);
        return attr;
    }
}
