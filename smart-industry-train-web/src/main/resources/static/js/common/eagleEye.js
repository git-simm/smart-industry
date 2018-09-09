/**
 * 鹰眼插件
 * @constructor
 */
function EagleEye() {
    var eagleeye = {}, visible = false, boxDraging = false, isOpen = true, isLoadImage = false, viewSize = [
        0, 0], imageSize = [0, 0], imageOffset, boxBorder = [0, 0];
    var svgContainerId = "svgContainer", eagleEyeId = "eagleEye",
        eagleEyeHeaderId = "eagleEye-header", eagleEyeZoomId = "eagleEye-zoom",
        eagleEyeCloseId = "eagleEye-close", eagleEyeContentId = "eagleEye-content",
        eagleEyeThumbnailWrapId = "eagleEye-thumbnail-wrap", eagleEyeThumbnailId = "eagleEye-thumbnail";
    var eagleEyeBoxId = "eagleEye-box";
    var $svgContainer = null, $eagleEye = null, $eagleEyeHeader = null, $eagleEyeZoom = null, $eagleEyeClose = null, $eagleEyeContent = null, $eagleEyeThumbnailWrap = null, $eagleEyeThumbnail = null, $eagleEyeBox = null;
    function initViewSize() {
        $eagleEyeContent.css({
            width : $eagleEye.width(),
            height : $eagleEye.height()
            - $eagleEyeHeader.outerHeight()
        });
        viewSize = [$eagleEyeContent.width(), $eagleEyeContent.height()];
    }
    function initEagleEyePosition() {
        $eagleEye.css({
            top : -$eagleEye.outerHeight() - 2,
            left : $svgContainer.width() - $eagleEye.outerWidth()
            - 15
        });
        boxBorder[0] = $eagleEyeBox.outerWidth() - $eagleEyeBox.width();
        boxBorder[1] = $eagleEyeBox.outerHeight() - $eagleEyeBox.height();
        updateImageOffset();
        updateBox();
    }
    function updateEagleEyePosition() {
        $eagleEye.css({
            top : -$eagleEye.outerHeight(),
            left : $svgContainer.width() - $eagleEye.outerWidth()
            - 15
        });
        updateImageOffset();
        updateBox();
    }
    function updateZoom() {
        var zoom = Math.floor($.topology.view.scale() * 100);
        $eagleEyeZoom.text("缩放至" + zoom + "%");
    }
    function setBoxSize(width, height) {
        $eagleEyeBox.css({
            width : width,
            height : height
        });
    }
    function updateBox() {
        if (boxDraging) {
            return;
        }
        var bbox = $.topology.view.getBBox(), viewport = $.topology.view.viewport();
        if (!bbox || !viewport) {
            return;
        }
        var x = 0, y = 0, width = 0, height = 0;
        x = imageSize[0] * viewport.x / bbox.width;
        y = imageSize[1] * viewport.y / bbox.height;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        x += imageOffset.left;
        y += imageOffset.top;
        width = viewport.width / bbox.width * imageSize[0];
        height = viewport.height / bbox.height * imageSize[1];
        width -= boxBorder[0];
        height -= boxBorder[1];
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        setBoxSize(width, height);
        $eagleEyeBox.offset({
            left : x,
            top : y
        });
    }
    function updateViewport() {
        var boxOffset = $eagleEyeBox.offset(), bbox = $.topology.view.getBBox();
        var x = (boxOffset.left - imageOffset.left) / imageSize[0]
            * bbox.width, y = (boxOffset.top - imageOffset.top)
            / imageSize[1] * bbox.height, width = $eagleEyeBox
                .outerWidth()
            / imageSize[0] * bbox.width, height = $eagleEyeBox
                .outerHeight()
            / imageSize[1] * bbox.height;
        $.topology.view.viewport(x, y, width, height);
    }
    function bindEvents() {
        $eagleEye.draggable({
            handle : "#" + eagleEyeHeaderId,
            containment : "#" + svgContainerId,
            cancel : "#" + eagleEyeCloseId,
            scroll : false,
            drag : function(event, ui) {
                updateImageOffset();
            },
            stop : function() {
                updateImageOffset();
            }
        });
        $eagleEyeBox.draggable({
            containment : "#eagleEye-thumbnail",
            scroll : false,
            start : function() {
                boxDraging = true;
            },
            drag : function(event, ui) {
                updateViewport();
            },
            stop : function() {
                boxDraging = false;
            }
        });
        $eagleEyeClose.button({
            icons : {
                primary : "ui-icon-close"
            },
            text : false
        });
        $eagleEyeClose.click(function() {
            eagleeye.close();
        });
    }
    function initImage(image) {
        var scale = Math.min(viewSize[0] / image.width, viewSize[1]
            / image.height);
        imageSize = [image.width * scale, image.height * scale];
        image.width = imageSize[0];
        image.height = imageSize[1];
        $eagleEyeThumbnailWrap.html(image);
        $eagleEyeThumbnail = $("#" + eagleEyeThumbnailId);
        $eagleEyeThumbnailWrap.css({
            left : (viewSize[0] - imageSize[0]) / 2,
            top : (viewSize[1] - imageSize[1]) / 2
        });
        isLoadImage = true;
    }
    function updateImageOffset() {
        imageOffset = $eagleEyeThumbnail.offset();
    }
    eagleeye.init = function() {
        isLoadImage = false;
        $svgContainer = $("#" + svgContainerId);
        $eagleEye = $("#" + eagleEyeId);
        $eagleEyeHeader = $("#" + eagleEyeHeaderId);
        $eagleEyeZoom = $("#" + eagleEyeZoomId);
        $eagleEyeClose = $("#" + eagleEyeCloseId);
        $eagleEyeContent = $("#" + eagleEyeContentId);
        $eagleEyeThumbnailWrap = $("#" + eagleEyeThumbnailWrapId);
        $eagleEyeBox = $("#" + eagleEyeBoxId);
        $eagleEye.disableSelection();
        bindEvents();
        $eagleEyeBox.css({
            opacity : 0.3
        });
        setBoxSize(0, 0);
        $eagleEyeZoom.empty();
        $eagleEyeThumbnailWrap.empty();
        eagleeye.hide();
        eagleeye.loadImage({
            //TODO:加载缩略图路径
            path : "/static/thumbnail/svg-test.png".geturl(),
            success : function(image) {
                initViewSize();
                initImage(image);
                eagleeye.show(true);
                updateZoom();
                initEagleEyePosition();
                eagleeye.bindContainerEvents();
                $($.topology.graphEngine.canvas).on("change",
                    function(evt, args) {
                        if (isLoadImage) {
                            eagleeye.show(true);
                            updateBox();
                        }
                        updateZoom();
                    });
            }
        });
    };
    eagleeye.reset = function() {
        isLoadImage = false;
        eagleeye.hide();
        setBoxSize(0, 0);
        $eagleEyeZoom.empty();
        $eagleEyeThumbnailWrap.empty();
    };
    eagleeye.loadImage = function(options) {
        var image = new Image();
        image.id = eagleEyeThumbnailId;
        image.onerror = function() {
        };
        image.onload = function() {
            options.success(image);
        };
        image.src = options.path + "?timestamp=" + new Date().getTime();
    };
    eagleeye.bindContainerEvents = function() {
        if ($svgContainer.attr("eagleeye-event")) {
            return;
        }
        $svgContainer.attr("eagleeye-event", "true");
        $svgContainer.resize(function() {
            updateEagleEyePosition();
        });
    };
    eagleeye.open = function() {
        isOpen = true;
        eagleeye.show(true);
    };
    eagleeye.close = function() {
        isOpen = false;
        eagleeye.hide(true);
    };
    eagleeye.show = function(animate) {
        if (!isOpen || !isLoadImage) {
            return;
        }
        if (!visible) {
            visible = true;
            if (animate
                && !($.browser.msie && Number($.browser.version
                    .split(".")[0]) <= 8)) {
                $eagleEye.stop(true, true);
                $eagleEye.fadeIn("slow");
            } else {
                $eagleEye.show();
            }
            updateImageOffset();
            updateBox();
        }
    };
    eagleeye.hide = function(animate) {
        if (visible) {
            visible = false;
            if (animate
                && !($.browser.msie && Number($.browser.version
                    .split(".")[0]) <= 8)) {
                $eagleEye.stop(true, true);
                $eagleEye.fadeOut("slow");
            } else {
                $eagleEye.hide();
            }
        }
    };
    eagleeye.isOpen = function() {
        return isOpen;
    };
    eagleeye.visible = function() {
        return visible;
    };
    return eagleeye;
}