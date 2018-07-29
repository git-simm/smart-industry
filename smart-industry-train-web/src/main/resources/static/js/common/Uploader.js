Zq.Utility.RegisterNameSpace("smart.uploader");
//闭包引入命名空间
(function (ns, undefined) {
    ns.Init = function(options) {
        var defaultParam = {
            success: null,
            server: window.location.origin + '/upfiles/process',//文件接收服务端。
            auto: false, //选完文件后，是否自动上传。
            pick: '.filePicker',//选择文件的按钮。可选。
            chunked: true, //] [默认值：false] 是否要分片处理大文件上传。
            chunkSize: 5242880,//如果要分片，分多大一片？ 默认大小为5M.
            chunkRetry: 3,//如果某个分片由于网络问题出错，允许自动重传多少次？
            fromData: { clientId: '1' },
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            }
        };
        options = $.extend(defaultParam,options);
        var uploader = WebUploader.create(options);
        //开始上传
        $(".beginloader").click(function () {
            uploader.upload();
        });

        // 当有文件添加进来的时候
        uploader.on('fileQueued', function (file) {
            var $li = $('<div id="' + file.id + '" class="file-item thumbnail" style="float:left;">' +
                        '<div class="file-panel"><span class="center remove-this">删除</span></div>' +
                        '<img>' +
                        '<div class="info">' + file.name + '</div>' +
                        '</div>'),
                $img = $li.find('img');
            //删除文件
            $li.on('click', '.remove-this', function () {
                uploader.removeFile(file, true);
                $li.remove();
                //uploader.cancelFile(file, true);
            });
            // $list为容器jQuery实例
            $(".fileList").append($li);
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            uploader.makeThumb(file, function (error, src) {
                if (error) {
                    $img.replaceWith('<span style="display: block;width: 100%;height: 30px;text-align: center;color: chocolate;">不能预览</span>');
                    return;
                }
                $img.attr('src', src);
            }, 100, 100);
        });
        // 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id),
                $percent = $li.find('.progress span');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<p class="progress"><span style="height: 20px;display: block;background: #666;"></span></p>').appendTo($li).find('span');
            }
            $percent.css('width', percentage * 100 + '%');
        });

        //****** 大文件分片上传的事件 begin ***
        /**
         * 文件上传前，初始化一个id
         */
        uploader.on('uploadStart', function (file) {
            uploader.options.formData.clientId = WebUploader.Base.guid();
        });
        uploader.on('uploadBeforeSend', function (object, data, headers) {
            var $li = $('#' + data.id),
                $percent = $li.find('.progress span');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<p class="progress"><span style="height: 20px;display: block;background: #666;"></span></p>').appendTo($li).find('span');
            }
            var percentage = Math.ceil((data.chunk / data.chunks) * 100);
            $percent.css('width', percentage + '%');
        });
        //****** 大文件分片上传的事件 end ***

        // 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on('uploadSuccess', function (file, json) {
            if (options.success) {
                options.success(file, json);
            }
            $('#' + file.id).addClass('upload-state-done');
            $('#' + file.id).find('.progress').remove();
            var $li = $('#' + file.id),
                $success = $li.find('div.success');
            // 避免重复创建
            if (!$success.length) {
                $success = $('<div class="success"></div>').appendTo($li);
            }
            $success.text('上传成功');
        });

        // 文件上传失败，显示上传出错。
        uploader.on('uploadError', function (file) {
            var $li = $('#' + file.id),
                $error = $li.find('div.error');

            // 避免重复创建
            if (!$error.length) {
                $error = $('<div class="error"></div>').appendTo($li);
            }
            $error.text('上传失败');
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on('uploadComplete', function (file) {
        });
    };
})(SmartMonitor.Uploader);
