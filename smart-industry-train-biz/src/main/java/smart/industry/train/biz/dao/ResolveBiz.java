package smart.industry.train.biz.dao;

import org.kabeja.Main;
import org.kabeja.dxf.DXFAttdef;
import org.kabeja.dxf.DXFBlock;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import smart.industry.train.biz.entity.*;
import smart.industry.train.biz.enums.TaskStateEnum;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * 解析组件
 */
@Service
public class ResolveBiz {
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
     * @param sysTasks
     */
    public void resolveTask(SysTasks sysTasks){
        this.sysTask = sysTasks;
        detail = designSolutionListBiz.selectByPrimaryKey(sysTasks.getDetailId());
        if(detail == null) return;
        file = sysUpfilesBiz.selectByPrimaryKey(detail.getFileId());
        if(file == null) return;
        if(file.getSuffix().equals(".dxf")){
            try {
                resolve();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * dxf文件转换成svg
     * @throws Exception
     */
    public void resolve() throws Exception {
        Main main = new Main();
        main.process();
        File f = new File(file.getFilePath());
        String suffix = file.getSuffix();
        String output = f.getName().replace(suffix,".svg");
        if (f.exists() && f.isFile()) {
            //文件转换
            DXFDocument doc = main.parseFile(f, output);
            //完成dxf信息的保存
            saveDxfMsg(doc);
        }
    }

    /**
     * 保存dxf文件信息
     * @param doc
     */
    @Transactional
    public void saveDxfMsg(DXFDocument doc){
        //先清理掉，有关改detailId的文件信息
        DesignDetailBlock filter = new DesignDetailBlock();
        filter.setDetailId(detail.getId());
        filter.setFilter("detailId = #{detailId}");
        designDetailBlockBiz.deleteByFilter(filter);

        DesignBlockAttr filter2 = new DesignBlockAttr();
        filter2.setDetailId(detail.getId());
        filter2.setFilter("detailId = #{detailId}");
        designBlockAttrBiz.deleteByFilter(filter2);

        Iterator blocks = doc.getDXFBlockIterator();
        while (blocks.hasNext()) {
            DXFBlock block = (DXFBlock) blocks.next();
            //1.保存block信息
            DesignDetailBlock designDetailBlock = saveBlockMsg(block.getName());
            Iterator entities = block.getDXFEntitiesIterator();
            while(entities.hasNext()){
                Object entity = entities.next();
                if(entity instanceof DXFAttdef){
                    DXFAttdef dxfAttdef = (DXFAttdef)entity;
                    //2.解析保存sys_attr信息
                    Integer attrId = saveDxfAttrMsg(dxfAttdef.getAttr());
                    //3.保存attr信息
                    saveBlockAttr(designDetailBlock.getId(),attrId,dxfAttdef.getAttr(),dxfAttdef.getValue());
                }
            }
        }
        //3.修改系统任务的状态
        sysTask.setState(TaskStateEnum.Completed.getValue());
        sysTasksBiz.update(sysTask);
    }

    /**
     * 保存dxf属性信息
     * @param name
     */
    @Transactional
    public Integer saveDxfAttrMsg(String name){
        //1.读取数据库中待执行的任务列表
        SysDxfAttr filter = new SysDxfAttr();
        filter.setName(name);
        filter.setFilter("name = #{name}");
        List<SysDxfAttr> attrs = sysDxfAttrBiz.selectByFilter(filter);
        if(CollectionUtils.isEmpty(attrs)){
            //保存成新的记录
            sysDxfAttrBiz.add(filter);
            return filter.getId();
        }else{
            return attrs.get(0).getId();
        }
    }

    /**
     * 保存block信息
     * @param blockName
     */
    @Transactional
    public DesignDetailBlock saveBlockMsg(String blockName){
        DesignDetailBlock block = new DesignDetailBlock();
        block.setDetailId(detail.getId());
        block.setName(blockName);
        designDetailBlockBiz.add(block);
        return block;
    }

    /**
     * 保存block属性信息
     * @param blockId
     * @param attrId
     * @param attrVal
     */
    @Transactional
    public void saveBlockAttr(Integer blockId,Integer attrId,String attrName,String attrVal){
        DesignBlockAttr attr = new DesignBlockAttr();
        attr.setDetailId(detail.getId());
        attr.setAttrName(attrName);
        attr.setAttrId(attrId);
        attr.setBlockId(blockId);
        attr.setValue(attrVal);
        designBlockAttrBiz.add(attr);
    }
}
