Zq.Utility.RegisterNameSpace("three.train");
(function (ns, undefined) {
    //私有全局变量
    var container, stats, cube, plane, group, controls;
    var objects = [];
    var targetRotationX = 0, targetRotationY = 0;
    var targetRotationOnMouseDownX = 0, targetRotationOnMouseDownY = 0;
    var mouseX = 0, mouseY = 0;
    var mouseXOnMouseDown = 0, mouseYOnMouseDown = 0;
    var windowHalfX = window.innerWidth / 2;
    var windowHalfY = window.innerHeight / 2;

    var scene, camera;
    var myRender = new THREE.WebGLRenderer();

    //将场景添加到界面上
    ns.Init = function () {
        container = document.createElement('div');
        document.body.appendChild(container);

        var info = document.createElement('div');
        info.style.position = 'absolute';
        info.style.top = '10px';
        info.style.width = '100%';
        container.appendChild(info);

        camera = new THREE.CinematicCamera(50, window.innerWidth / window.innerHeight, 1, 10000);
        camera.setLens(5);
        camera.position.set(0, 200, 400);
        //看向原点
        camera.lookAt(new THREE.Vector3(0, 0, 0));
        scene = new THREE.Scene();
        myRender.shadowMapEnabled = true;
        myRender.shadowMapSoft = true;
        myRender.setPixelRatio(window.devicePixelRatio);
        myRender.setSize(window.innerWidth, window.innerHeight);
        container.appendChild(myRender.domElement);


        InitControls();

        document.body.appendChild(myRender.domElement);

        AddLight();
        AddPlain();
        //AddCube();
        //var line = train.lines.AddLine();

        group = new THREE.Object3D(); //实例化一个THREE.Object3D对象
        group.add(plane); //在对象里面添加第一个子元素
        train.lines.AddLight(group);
        train.lines.AddTrain(function (obj) {
            group.add(obj);
            objects.push(obj);
            //场景重绘
            myRender.render(scene, camera);
        });
        scene.add(group); //将对象组添加到场景当中


        InitEvent();
        //开启动画侦听
        animate();
        myRender.render(scene, camera);
        train.selection.Init({
            render: myRender, objs: objects, camara: camera, scene: scene
        });
    }

    function InitControls() {
        controls = new THREE.OrbitControls(camera, myRender.domElement);
        controls.minDistance = 20;
        controls.maxDistance = 500;
        controls.enablePan = false;
    }

    /**
     * 添加点光源
     * @constructor
     */
    function AddLight() {
        //环境光
        scene.add(new THREE.HemisphereLight(0xddeeff, 0x0f0e0d, 0.5));
        var light = new THREE.DirectionalLight(0xffffff);
        light.position.set(800, 800, 800).normalize();
        scene.add(light);
    }

    /**
     * 绘制立方体
     * @constructor
     */
    function AddCube() {
        var geometry = new THREE.BoxGeometry(200, 200, 200);
        //绘制3d图形
        cube = new THREE.Mesh(geometry, train.textures.TrainMaterial);
        cube.name = "立方体";
        scene.add(cube);
        //更改立方体的位置
    }

    /**
     * 添加地板
     * @constructor
     */
    function AddPlain() {
        //new THREE.GridHelper
        //plane = ThreeSimm.CyclePlaneMaterial(1000, 800, 50, 40,
        //        Zq.Utility.GetPath("/const/textures/grid.png"),new THREE.MeshPhongMaterial({color: "#333333"}));
        var floorGeometry = new THREE.PlaneBufferGeometry(1000, 800);
        plane = new THREE.Mesh(floorGeometry, new THREE.MeshPhongMaterial({color: "#666666"}));
        plane.rotation.x = -Math.PI / 2.0;
        plane.position.y = -45;
    }

    function InitEvent() {
        stats = new Stats();
        container.appendChild(stats.dom);
        document.addEventListener('mousedown', onDocumentMouseDown, false);
        document.addEventListener('touchstart', onDocumentTouchStart, false);
        document.addEventListener('touchmove', onDocumentTouchMove, false);
        document.addEventListener('dblclick', onDocumentDblClick, false);
        document.addEventListener('dblclick', onDocumentDblClick, false);
        document.addEventListener('mousewheel', onMouseWheel, false);
        //
        window.addEventListener('resize', onWindowResize, false);
    }

    /**
     * 双击镜头推进
     * @param event
     */
    function onDocumentDblClick(event) {
        if (camera.position.z <= 0) return;
        var per = 0.9;
        camera.position.x *= per;
        camera.position.y *= per;
        camera.position.z *= per;
    }

    /**
     * 鼠标滚轮事件
     */
    function onMouseWheel(event) {
        var event = event || window.event;
        // 定义一个标志，当滚轮向下滚时，执行一些操作
        var down = event.wheelDelta ? event.wheelDelta < 0 : event.detail > 0;
        var per = 1;
        if (down) {
            per = 1.1;
        } else {
            per = 0.9;
        }
        if (camera.position.z > 0) {
            var z = camera.position.z * per;
            if (z < 1500) {
                camera.position.x *= per;
                camera.position.y *= per;
                camera.position.z = z;
            }
        }
        //阻止默认事件
        if (event.preventDefault) {/*FF 和 Chrome*/
            event.preventDefault();// 阻止默认事件
        }
        return false;
    }

    function onWindowResize() {
        windowHalfX = window.innerWidth / 2;
        windowHalfY = window.innerHeight / 2;
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        myRender.setSize(window.innerWidth, window.innerHeight);
    }

    function onDocumentMouseDown(event) {
        event.preventDefault();
        document.addEventListener('mousemove', onDocumentMouseMove, false);
        document.addEventListener('mouseup', onDocumentMouseUp, false);
        document.addEventListener('mouseout', onDocumentMouseOut, false);
        document.addEventListener('dblclick', onDocumentDblClick, false);
        document.addEventListener('mousewheel', onMouseWheel, false);
        mouseXOnMouseDown = event.clientX - windowHalfX;
        mouseYOnMouseDown = event.clientY - windowHalfY;
        targetRotationOnMouseDownX = targetRotationX;
        targetRotationOnMouseDownY = targetRotationY;
        //targetRotationOnMouseDown = targetRotation;
    }

    function onDocumentMouseMove(event) {
        mouseX = event.clientX - windowHalfX;
        mouseY = event.clientY - windowHalfY;
        targetRotationX = targetRotationOnMouseDownX + (mouseX - mouseXOnMouseDown) * 0.02;
        targetRotationY = targetRotationOnMouseDownY + (mouseY - mouseYOnMouseDown) * 0.02;
    }

    function onDocumentMouseUp(event) {
        document.removeEventListener('mousemove', onDocumentMouseMove, false);
        document.removeEventListener('mouseup', onDocumentMouseUp, false);
        document.removeEventListener('mouseout', onDocumentMouseOut, false);
        train.selection.onSelected(event);
    }

    function onDocumentMouseOut(event) {
        document.removeEventListener('mousemove', onDocumentMouseMove, false);
        document.removeEventListener('mouseup', onDocumentMouseUp, false);
        document.removeEventListener('mouseout', onDocumentMouseOut, false);
    }

    function onDocumentTouchStart(event) {
        if (event.touches.length === 1) {
            event.preventDefault();
            mouseXOnMouseDown = event.touches[0].pageX - windowHalfX;
            mouseYOnMouseDown = event.touches[0].pageY - windowHalfY;
            targetRotationOnMouseDownX = targetRotationX;
            targetRotationOnMouseDownY = targetRotationY;
        }
    }

    function onDocumentTouchMove(event) {
        if (event.touches.length === 1) {
            event.preventDefault();
            mouseX = event.touches[0].pageX - windowHalfX;
            mouseY = event.touches[0].pageY - windowHalfY;
            targetRotationX = targetRotationOnMouseDownX + (mouseX - mouseXOnMouseDown) * 0.05;
            targetRotationY = targetRotationOnMouseDownY + (mouseY - mouseYOnMouseDown) * 0.05;
        }
    }

    /**
     * 创建监听动画
     */
    function animate() {
        //requestAnimationFrame会把每一帧中的所有DOM操作集中起来，在一次重绘或回流中就完成，并且重绘或回流的时间间隔紧紧跟随浏览器的刷新频率
        requestAnimationFrame(animate);
        stats.begin();
        render();
        stats.end();

    }

    function render() {
        group.rotation.y += (targetRotationX - group.rotation.y) * 0.05;
        group.rotation.x += (targetRotationY - group.rotation.x) * 0.05;
        myRender.render(scene, camera);
    }
})(three.train);
$(function () {
    three.train.Init();
});