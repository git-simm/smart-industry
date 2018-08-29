<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">  用户登录
    <#elseif section="css">
        <@cssRef url="/static/css/signin.css"/>
<style type="text/css">
    body {
        font-size: 12px;
        background-color: #999 !important;
        /*此处背景图自行替换*/
        min-height: 100vh;
        box-sizing: border-box;
        margin: 0;
        padding-top: calc(50vh - 240px) !important;
        font: 150%/1.6 Baskerville, Palatino, serif;
        font-family: 'Open Sans', sans-serif;
        background: #092756;
        background: -moz-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%),-moz-linear-gradient(top,  rgba(57,173,219,.25) 0%, rgba(42,60,87,.4) 100%), -moz-linear-gradient(-45deg,  #670d10 0%, #092756 100%);
        background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -webkit-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -webkit-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
        background: -o-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -o-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -o-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
        background: -ms-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -ms-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -ms-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
        background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), linear-gradient(to bottom,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), linear-gradient(135deg,  #670d10 0%,#092756 100%);
        filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#3E1D6D', endColorstr='#092756',GradientType=1 );
    }

    /**
     * 整体居中功能；
     * 背景透明虚化
     * 溢出隐藏
     * 边缘圆角化
     * 文字增加淡阴影
     */
    .description {
        width: 400px;
        height: 300px;
        position: relative;
        margin: 0 auto;
        padding: 1em;
        background: hsla(0, 0%, 100%, .6) border-box;
        overflow: hidden;
        border-radius: .3em;
        box-shadow: 0 0 0 1px hsla(0, 0%, 100%, .3) inset, 0 .5em 1em rgba(0, 0, 0, 0.6);
        text-shadow: 0 1px 1px hsla(0, 0%, 100%, .3);
    }

    /*使用滤镜模糊边缘*/
    .description::before {
        -webkit-filter: blur(20px);
        filter: blur(20px);
    }

    .signin {
        margin-top: calc(50vh) !important;
    }
</style>
    <#elseif section="content">
<div style="position: fixed; top: 0; left: 0; color: white;">
    <h2 style="margin: 10px;">有轨车辆模拟仿真系统</h2>
</div>
	<form class="form-signin description" style="width: 400px;padding:20px 40px;" method="post" role="form" onsubmit="return false;">
        <h1 class="center" style="margin-top: 20px;margin-bottom: 10px;">用户登录</h1>
        <!-- username, password 这两个参数被Login-Filter直接解析-->
        <input type="text" name="name" autocomplete="off" class="form-control" placeholder="工号" required autofocus/>
        <input type="password" name="psw" autocomplete="new-password" class="form-control" placeholder="密码" required/>
        <button class="btn btn-lg btn-primary btn-block" id="btn_login">登录</button>
    </form>
    <#elseif section="scripts">
    <script type="text/javascript">
        $(function () {
           $("#btn_login").click(function () {
                $(this).attr("disabled", true);
                var $form = $(".form-signin");
                if ($form.find("input[name='name']").eq(0).val() === "") return;
                if ($form.find("input[name='psw']").eq(0).val() === "") return;
                Login();
            });
        });

        function Login() {
            $.ajax({
                async: false,
                type: "Post",
                url: Zq.Utility.GetPath("/loginvalid"),
                data: $(".form-signin").serializeObject(),
                success:function(url){
                    window.location.href = url.geturl();
                },
                error: function (ex) {
                    $("#btn_login").removeAttr("disabled");
                }
            });
        }
    </script>
    </#if>
</@layout>