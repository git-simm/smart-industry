var thumbnailViewer = function(options){
    var t_main = null;
    var t_thumb = null;
    var getSVGDocument = function(objectElem){
        var svgDoc = objectElem.contentDocument;
        if(! svgDoc){
            svgDoc = objectElem.getSVGDocument();
        }
        return svgDoc;
    }
    
    var bindThumbnail = function(main, thumb){

        if(! t_main && main){
            t_main = main;
        }
        if(! t_thumb && thumb){
            t_thumb = thumb;
        }
        if(! t_main || ! t_thumb){
            return;
        }

        var resizeTimer;
        var interval = 300; //msec
        window.addEventListener('resize', function(event){
            if (resizeTimer !== false) {
                clearTimeout(resizeTimer);
            }
            resizeTimer = setTimeout(function () {
                t_main.resize();
                t_thumb.resize();
            }, interval);
        });

        t_main.setOnZoom(function(level){
            t_thumb.updateThumbScope();
            if(options.onZoom){
                options.onZoom(t_main, t_thumb, level);
            }
        });

        t_main.setOnPan(function(point){
            t_thumb.updateThumbScope();
            if(options.onPan){
                options.onPan(t_main, t_thumb, point);
            }
        });

        var _updateThumbScope = function (main, thumb, scope, line1, line2){
            var mainPanX   = main.getPan().x
            , mainPanY   = main.getPan().y
            , mainWidth  = main.getSizes().width
            , mainHeight = main.getSizes().height
            , mainZoom   = main.getSizes().realZoom
            , thumbPanX  = thumb.getPan().x
            , thumbPanY  = thumb.getPan().y
            , thumbZoom  = thumb.getSizes().realZoom;

            var thumByMainZoomRatio =  thumbZoom / mainZoom;

            var scopeX = thumbPanX - mainPanX * thumByMainZoomRatio;
            var scopeY = thumbPanY - mainPanY * thumByMainZoomRatio;
            var scopeWidth = mainWidth * thumByMainZoomRatio;
            var scopeHeight = mainHeight * thumByMainZoomRatio;
            
            scope.setAttribute("x", scopeX + 1);
            scope.setAttribute("y", scopeY + 1);
            scope.setAttribute("width", scopeWidth - 2);
            scope.setAttribute("height", scopeHeight - 2);
            /*
              line1.setAttribute("x1", scopeX + 1);
              line1.setAttribute("y1", scopeY + 1);
              line1.setAttribute("x2", scopeX + 1 + scopeWidth - 2);
              line1.setAttribute("y2", scopeY + 1 + scopeHeight - 2);
              line2.setAttribute("x1", scopeX + 1);
              line2.setAttribute("y1", scopeY + 1 + scopeHeight - 2);
              line2.setAttribute("x2", scopeX + 1 + scopeWidth - 2);
              line2.setAttribute("y2", scopeY + 1);
            */
        };

        t_thumb.updateThumbScope = function(){
            var scope = document.getElementById('scope'+ options.key);
            var line1 = document.getElementById('line1'+options.key);
            var line2 = document.getElementById('line2'+options.key);
            _updateThumbScope(t_main, t_thumb, scope, line1, line2);
        }
        t_thumb.updateThumbScope();

        var _updateMainViewPan = function(clientX, clientY, scopeContainer, main, thumb){
            var dim = scopeContainer.getBoundingClientRect()
            , mainWidth   = main.getSizes().width
            , mainHeight  = main.getSizes().height
            , mainZoom    = main.getSizes().realZoom
            , thumbWidth  = thumb.getSizes().width
            , thumbHeight = thumb.getSizes().height
            , thumbZoom =  thumb.getSizes().realZoom;

            var thumbPanX = clientX - dim.left - thumbWidth / 2;
            var thumbPanY = clientY - dim.top - thumbHeight / 2;
            var mainPanX = - thumbPanX * mainZoom / thumbZoom;
            var mainPanY = - thumbPanY * mainZoom / thumbZoom;
            main.pan({x:mainPanX, y:mainPanY});
        };

        var updateMainViewPan = function(evt){
            if(evt.which == 0 && evt.button == 0){
                return false;
            }
            var scopeContainer = document.getElementById(options.scopeContainer);
            _updateMainViewPan(evt.clientX, evt.clientY, scopeContainer, t_main, t_thumb);
        }

        var scopeContainer = document.getElementById(options.scopeContainer);
        scopeContainer.addEventListener('click', function(evt){
            updateMainViewPan(evt);
        });

        scopeContainer.addEventListener('mousemove', function(evt){
            updateMainViewPan(evt);
        });
    };
    /**
     * 主界面的变更监听
     * @type {HTMLElement | null}
     */
    var mainViewObjectElem = document.getElementById(options.mainViewId);
    mainViewObjectElem.addEventListener("load", mainLoad, false);
    function mainLoad(){
        var mainViewSVGDoc = getSVGDocument(mainViewObjectElem);
        if(options.onMainViewSVGLoaded){
            options.onMainViewSVGLoaded(mainViewSVGDoc);
        }

        var beforePan = function(oldPan, newPan){
            var stopHorizontal = false
                , stopVertical = false
                , gutterWidth = 100
                , gutterHeight = 100
                // Computed variables
                , sizes = this.getSizes()
                , leftLimit = -((sizes.viewBox.x + sizes.viewBox.width) * sizes.realZoom) + gutterWidth
                , rightLimit = sizes.width - gutterWidth - (sizes.viewBox.x * sizes.realZoom)
                , topLimit = -((sizes.viewBox.y + sizes.viewBox.height) * sizes.realZoom) + gutterHeight
                , bottomLimit = sizes.height - gutterHeight - (sizes.viewBox.y * sizes.realZoom);
            customPan = {};
            customPan.x = Math.max(leftLimit, Math.min(rightLimit, newPan.x));
            customPan.y = Math.max(topLimit, Math.min(bottomLimit, newPan.y));
            return customPan;
        };

        var main = svgPanZoom('#'+options.mainViewId, {
            zoomEnabled: true,
            controlIconsEnabled: true,
            fit: true,
            center: true,
            beforePan: beforePan
        });

        bindThumbnail(main, undefined);
        if(options.onMainViewShown){
            options.onMainViewShown(mainViewSVGDoc, main);
        }
    }

    var thumbViewObjectElem = document.getElementById(options.thumbViewId);
    thumbViewObjectElem.addEventListener("load", function(){

        var thumbViewSVGDoc = getSVGDocument(thumbViewObjectElem);
        if(options.onThumbnailSVGLoaded){
            options.onThumbnailSVGLoaded(thumbViewSVGDoc);
        }

        var thumb = svgPanZoom('#'+options.thumbViewId, {
            zoomEnabled: false,
            panEnabled: false,
            controlIconsEnabled: false,
            dblClickZoomEnabled: false,
            preventMouseEventsDefault: true,
        });

        bindThumbnail(undefined, thumb);
        if(options.onThumbnailShown){
            options.onThumbnailShown(thumbViewSVGDoc, thumb);
        }

    }, false);
};

