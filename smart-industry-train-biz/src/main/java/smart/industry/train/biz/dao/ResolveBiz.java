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
import smart.industry.train.biz.entity.*;
import smart.industry.train.biz.enums.TaskStateEnum;
import smart.industry.train.biz.mypoi.DesignXlsBiz;
import smart.industry.train.biz.mypoi.ReferBiz;

import java.io.File;
import java.util.ArrayList;
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

    private DesignSolutionList detail;
    private SysUpfiles file;
    private SysTasks sysTask;

    /**
     * 任务解析
     *
     * @param sysTasks
     */
    public void resolveTask(SysTasks sysTasks) {
        this.sysTask = sysTasks;
        detail = designSolutionListBiz.selectByPrimaryKey(sysTasks.getDetailId());
        if (detail == null) return;
        file = sysUpfilesBiz.selectByPrimaryKey(detail.getFileId());
        if (file == null) return;
        String suffix = file.getSuffix();
        boolean succ = false;
        //1.修改系统任务的状态
        sysTask.setState(TaskStateEnum.Converting.getValue());
        sysTasksBiz.update(sysTask);
        try {
            if (suffix.equals(".dxf")) {
                succ = resolve();
            } else if (suffix.equals(".xls") || suffix.equals(".xlsx")) {
                //解析excel清单
                succ = resolveExcel();
            } else if (suffix.equals(".txt")) {
                //解析导出文件对应的elcad方案的路径
                succ = resolveRefer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            //3.修改系统任务的状态
            if (succ) {
                sysTask.setState(TaskStateEnum.Completed.getValue());
                sysTasksBiz.update(sysTask);
            } else {
                sysTask.setState(TaskStateEnum.Ready.getValue());
                sysTasksBiz.update(sysTask);
            }
        }
    }

    @Autowired
    private ReferBiz referBiz;

    /**
     * 解析文件dxf在工程中的对应关系
     */
    private boolean resolveRefer() throws Exception {
        return referBiz.resolve(detail, file);
    }

    /**
     * 设计清单的解析
     */
    @Autowired
    private DesignXlsBiz designXlsBiz;

    /**
     * 解析excel清单
     */
    public boolean resolveExcel() throws Exception {
        return designXlsBiz.resolve(file);
    }

    /**
     * dxf文件转换成svg
     *
     * @throws Exception
     */
    public boolean resolve() throws Exception {
        Main main = new Main();
        main.process();
        File f = new File(file.getFilePath());
        String suffix = file.getSuffix();
        String output = f.getPath().replace(suffix, ".svg");
        if (f.exists() && f.isFile()) {
            //文件转换
            DXFDocument doc = main.parseFile(f, output);
            //完成dxf信息的保存
            saveDxfMsg(doc);
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
    public void saveDxfMsg(DXFDocument doc) {
        //2.先清理掉，有关改detailId的文件信息
        DesignDetailBlock filter = new DesignDetailBlock();
        filter.setDetailId(detail.getId());
        filter.setFilter("detailId = #{detailId}");
        designDetailBlockBiz.deleteByFilter(filter);

        DesignBlockAttr filter2 = new DesignBlockAttr();
        filter2.setDetailId(detail.getId());
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
        parseLayer(doc);
    }

    /**
     * 格式化layer
     *
     * @param doc
     */
    private void parseLayer(DXFDocument doc) {
        //遍历layer
//        Iterator i = doc.getDXFLayerIterator();
//        while (i.hasNext()) {
//            DXFLayer layer = (DXFLayer) i.next();
//            this.parseEntity(doc,layer);
//        }
        //解析图片信息
        DXFLayer layer = doc.getDXFLayer("SYMBOL");
        if (layer != null) {
            this.parseEntity(doc, layer);
        }
    }

    /**
     * 格式化entity
     *
     * @param doc
     * @param layer
     */
    protected void parseEntity(DXFDocument doc, DXFLayer layer) {
        //String lt = layer.getLineType();
        // the stroke-width
        //int lineWeight = layer.getLineWeight();
        // the stroke-width
        Double lw = null;
        Iterator types = layer.getDXFEntityTypeIterator();

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
                    DesignDetailBlock designDetailBlock = saveBlockMsg(insert.getBlockID(),ek);
                    //2.解析保存sys_attr信息
                    List<DXFEntity> attrs = insert.getAttrList();//获取图标对应的属性
                    //保存实体信息
                    for (DXFEntity attr : attrs){
                        DXFAttrib attrib = (DXFAttrib)attr;
                        if(attrib != null){
                            String key = attrib.getCode()+"@"+attrib.getLayerName()+"@"+attrib.getFlag();
                            DXFAttdef def = doc.getAttdef(key);
                            if(def!= null){
                                String attrName = def.getAttr();
                                Integer attrId = saveDxfAttrMsg(attrName);
                                //3.保存attr信息
                                saveBlockAttr(designDetailBlock.getId(),attrId,attrName,attrib.getText());
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * 获取实体的key
     * @param insert
     * @return
     */
    private String getEntityKey(DXFInsert insert){
        DXFEntity et = insert.getAttrEntity(0);
        if(et != null){
            DXFAttrib temp = (DXFAttrib)et;
            if(temp!=null){
                return temp.getText();
            }else{
                return et.getID();
            }
        }
        return null;
    }
    /**
     * 保存dxf属性信息
     *
     * @param name
     */
    @Transactional
    public Integer saveDxfAttrMsg(String name) {
        //1.读取数据库中待执行的任务列表
        SysDxfAttr filter = new SysDxfAttr();
        filter.setName(name);
        filter.setFilter("name = #{name}");
        List<SysDxfAttr> attrs = sysDxfAttrBiz.selectByFilter(filter);
        if (CollectionUtils.isEmpty(attrs)) {
            //保存成新的记录
            sysDxfAttrBiz.add(filter);
            return filter.getId();
        } else {
            return attrs.get(0).getId();
        }
    }

    /**
     * 保存block信息
     *
     * @param blockName
     */
    @Transactional
    public DesignDetailBlock saveBlockMsg(String blockName,String key) {
        DesignDetailBlock block = new DesignDetailBlock();
        block.setDetailId(detail.getId());
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
    public void saveBlockAttr(Integer blockId, Integer attrId, String attrName, String attrVal) {
        DesignBlockAttr attr = new DesignBlockAttr();
        attr.setDetailId(detail.getId());
        attr.setAttrName(attrName);
        attr.setAttrId(attrId);
        attr.setBlockId(blockId);
        attr.setValue(attrVal);
        designBlockAttrBiz.add(attr);
    }
}
