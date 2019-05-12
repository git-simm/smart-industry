<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title"> 测试方案编辑
    <#elseif section="css">
        <@cssRef url="/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
    <style type="text/css">
        .treeContainer {
            padding: 10px;
            position: fixed;
            bottom: 40px;
            top: 83px;
            left: 0;
            border: 1px solid #ccc;
            border-width: 0 1px 0 0;
            background-color: ghostwhite;
            overflow: auto;
            overflow-y: auto;
    </style>
    <#elseif section="content">
<div class="page-content mini-model" id="app">
    <form id="form-main" onsubmit="javascript: return false;">
        <input type="hidden" name="id" v-model="entity.id"/>
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">名称：</label>
                <div class="col-10">
                    <input type="text" name="name" v-model="entity.name" required/>
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right">描述：</label>
                <div class="col-10">
                    <input type="text" name="name" v-model="entity.remark"/>
                </div>
            </div>
            <div class="form-group padding10">
            <#-- 树形多选控件 -->
                <div class="treeContainer col-12">
                    <ul id="soluTree" class="ztree"></ul>
                </div>
            </div>
        </div>
        <div class="mini-model-footer">
            <div class="textCenter padding5">
                <button class="btn btn-sm btn-primary btn-round" @click="save">保存</button>
                <a id="close" class="btn btn-sm btn-default btn-round" onclick="SmartMonitor.Common.Close()">取消</a>
            </div>
        </div>
    </form>
</div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/ztree/jquery.ztree.all.js"/>
      <script type="text/javascript">
          var app = new Vue({
              el: '#app',
              data: {
                  entity: JSON.parse('${entity}'),
                  zTree: {},
                  setting: {
                      view: {
                          expandSpeed: "fast",
                          selectedMulti: false,
                          dblClickExpand: function (treeId, treeNode) {
                              return treeNode.level > 0;
                          }
                      },
                      data: {
                          simpleData: {
                              enable: true
                          },
                          key: {
                              //title: "projFile",
                              name: "projFile"
                          }
                      },
                      check: {
                          enable: true
                      },
                      callback: {
                          onDblClick: function () {
                              this.save();
                          }
                      }
                  }
              },
              mounted() {
                  this.initTree();
              },
              methods: {
                  save: function () {
                      if (!Smart.Common.FormValid()) return false;
                      var data = this.getData();
                      var url = "/partion/edit";
                      if (SmartMonitor.Common.GetMode($("#form-main")) === "add") {
                          url = "/partion/add";
                      }
                      $.ajax({
                          async: false,
                          type: "Post",
                          dataType: "json",
                          contentType: 'application/json',
                          url: url.geturl(),
                          data: JSON.stringify(data),
                          success: function (result) {
                              //新增成功
                              SmartMonitor.Common.Close(true);
                          }
                      });
                  },
                  getData: function () {
                      var me = this;
                      var nodes = this.zTree.getCheckedNodes(true);
                      var list = nodes.filter(function (a) {
                          return a.filePath > '';
                      }).map(function (a) {
                          return {
                              partionId: me.id,
                              solutionFileId: a.id,
                          };
                      });
                      me.entity.partionList = list;
                      return me.entity;
                  },
                  getList: function (callback) {
                      $.ajax({
                          async: false,
                          url: Zq.Utility.GetPath("/solution/getfiles"),
                          data: {id: this.entity.solutionId},
                          success: function (data) {
                              if (data != null && data.length > 0) {
                                  data.forEach(function (item) {
                                      item.open = true;
                                  })
                              }
                              callback(data);
                          }
                      });
                  },
                  /**
                   * 初始化ztree控件
                   */
                  initTree: function () {
                      var me = this;
                      me.getList(function (data) {
                          me.entity.partionList = me.entity.partionList || [];
                          var ids = me.entity.partionList.map(function (a) {
                              return a.solutionFileId;
                          }) || [];
                          if (ids.length > 0) {
                              data.forEach(function (a) {
                                  a.checked = ids.includes(a.id);
                              });
                          }
                          //对返回的数据做选中处理
                          me.zTree = $.fn.zTree.init($("#soluTree"), me.setting, data);
                          //选中第一个节点
                          var nodes = me.zTree.getNodes();
                          if (nodes.length > 0) {
                              me.zTree.selectNode(nodes[0]);
                              me.zTree.expandNode(nodes[0], true, true, true);
                          }
                      });
                  }
              }
          });
      </script>
    </#if>
</@layout>
