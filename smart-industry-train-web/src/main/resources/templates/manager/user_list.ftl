<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">方案管理
    <#elseif section="css">
        <@cssRef url="/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
        <@cssRef url="/static/_resources/context/context.standalone.css"/>
    <#elseif section="content">
    <div style="position: relative;" class="col-2">
        <div class="ztreeMenu">组织架构</div>
        <div class="treeContainer col-2">
            <ul id="soluTree" class="ztree"></ul>
        </div>
    </div>
    <div class="col-10 col-md-10 col-sm-10">
        <div class="mini-model">
            <table class="table table-bordered table-hover table-striped marginBottom0">
                <caption>
                    <div class="pullLeft marginTop5">
                        <label class="inline-block textRight">用户列表</label>
                    </div>
                    <div class="pullRight">
                        <div class="input-group width200">
                            <input class="form-control" placeholder="请输入方案名称"
                                   type="text" name="searchkey" value="" autocomplete="off"/>
                            <span class="input-group-addon" onclick="user.list.ReFresh()">
                        <i class="icon-search"></i>
                    </span>
                        </div>
                    </div>
                    <div class="pullRight marginRight10">
                        <button class="btn btn-sm btn-primary" onclick="user.list.Add()"><i class="icon icon-plus"></i>&nbsp;新增</button>
                    </div>
                </caption>
            </table>
            <table id="list" class="table table-bordered table-hover col-12"></table>
            <div style="display: none;" id="list_opr_template">
                <button class="btn btn-xs btn-info" title="编辑" onclick="{edit}">
                    <i class="icon-edit"></i>
                </button>
                <button class="btn btn-xs btn-info" title="重置密码" onclick="{resetpsw}">
                    <i class="icon-lock"></i>
                </button>
                <button class="btn btn-xs btn-danger" title="删除" onclick="{del}">
                    <i class="icon-trash"></i>
                </button>
            </div>
        </div>
    </div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/ztree/jquery.ztree.all.js"/>
        <@jsRef "/static/_resources/context/context.js"/>
        <@jsRef "/static/js/manager/org.manager.js"/>
        <@jsRef "/static/js/manager/user.list.js"/>
    </#if>
</@layout>