package smart.industry.train.biz.entity;

import smart.industry.train.biz.entity.base.BaseEntity;

/**
 * 对比记录
 */
public class CompareRecord extends BaseEntity {
    public String detailId,blockId,item,representation,wireNumber,destItem1,destConnector1,destItem2,destConnector2;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public String getWireNumber() {
        return wireNumber;
    }

    public void setWireNumber(String wireNumber) {
        this.wireNumber = wireNumber;
    }

    public String getDestItem1() {
        return destItem1;
    }

    public void setDestItem1(String destItem1) {
        this.destItem1 = destItem1;
    }

    public String getDestConnector1() {
        return destConnector1;
    }

    public void setDestConnector1(String destConnector1) {
        this.destConnector1 = destConnector1;
    }

    public String getDestItem2() {
        return destItem2;
    }

    public void setDestItem2(String destItem2) {
        this.destItem2 = destItem2;
    }

    public String getDestConnector2() {
        return destConnector2;
    }

    public void setDestConnector2(String destConnector2) {
        this.destConnector2 = destConnector2;
    }
}
