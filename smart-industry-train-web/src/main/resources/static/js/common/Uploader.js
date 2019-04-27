﻿/**
 * 文件上传插件
 */
var myUploader = function (options) {
    this.uploader = null;
    var param = {
        list: null,
        btn: null,
        pick: '#picker',
        accept: {
            title: 'dxf',
            extensions: 'dxf',
            mimeTypes: 'application/dxf'
        },
        state: 'pending'
    };
    param = $.extend(param, options);
    this.uploader = WebUploader.create({
        // 不压缩image
        resize: false,
        // swf文件路径
        swf: Zq.Utility.GetPath('/static/_resources/uploader/Uploader.swf'),
        // 文件接收服务端。
        server: Zq.Utility.GetPath('/file/upload'),
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: param.pick,
        chunked: false, //] [默认值：false] 是否要分片处理大文件上传。
        chunkSize: 5242880,//如果要分片，分多大一片？ 默认大小为5M.
        chunkRetry: 3,//如果某个分片由于网络问题出错，允许自动重传多少次？
        accept: param.accept
    });
    var tempUploader = this.uploader;
    // 当有文件添加进来的时候
    this.uploader.on('fileQueued', function (file) {
        param.list.append('<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<p class="state">等待上传...</p>' +
            '</div>');

        files.push(file.id);
    });

    // 文件上传过程中创建进度条实时显示。
    this.uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div></div>').appendTo($li).find('.progress-bar');
        }
        $li.find('p.state').text('上传中');
        $percent.css('width', percentage * 100 + '%');
    });
    var files = [];
    var completed = [];
    //上传文件前，添加参数
    this.uploader.on('uploadBeforeSend', function (obj, data, headers) {
        // data.DelFilePath = parentObj.siblings(".upload-path").val();
        //  data.ItemCode = $("#txtItemCode").val();
        data.solutionId = solutionId;
        data.fileType = param.fileType;

    });

    this.uploader.on('uploadSuccess', function (file) {
        $('#' + file.id).find('p.state').text('已上传');
    });

    this.uploader.on('uploadError', function (file) {
        $('#' + file.id).find('p.state').text('上传出错');
    });

    this.uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').fadeOut();
        completed.push(file.id);
    });

    this.uploader.on('all', function (type) {
        if (type === 'startUpload') {
            param.state = 'uploading';
        } else if (type === 'stopUpload') {
            param.state = 'paused';
        } else if (type === 'uploadFinished') {
            param.state = 'done';
        }

        if (param.state === 'uploading') {
            param.btn.text('暂停上传');
        } else {
            param.btn.text('开始上传');
        }
    });
    /*var tempUploader = this.uploader;
    param.btn.on('click', function () {
        if (state === 'uploading') {
            tempUploader.stop();
        } else {
            tempUploader.upload();
        }
    });*/

    //设计方案ID
    var solutionId = null;
    this.upload = function(id,callback){
        solutionId = id;
        if (param.state === 'uploading') {
            tempUploader.stop();
        } else {
            tempUploader.upload();
            var interval = setInterval(function(){
                if(files.length === completed.length){
                    //清理掉定时器
                    clearInterval(interval);
                    //执行完毕后的回调
                    callback();
                }
            }, 500);
        }
    }

    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }
};