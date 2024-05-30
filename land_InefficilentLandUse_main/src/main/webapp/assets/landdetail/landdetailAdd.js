
layui.use(['layer','upload', 'form', 'laydate','admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload = layui.upload;
    var laydate = layui.laydate;
    var category;

    var activeDlSelect = function () {
        var deptName = $("#deptName").val();
        //初始化区县
        $("#xdm").html('<option value="">请选择区县</option>');
        var qxAjax = new $ax(Feng.ctxPath + "/dict/listDictsByCode?dictTypeCode=sjzqx", function (data) {
            for (var i = 0; i < data.data.length; i++) {
                var name = data.data[i].name;
                var code = data.data[i].code;
                if(data.data[i].parentId === 0){
                    if(deptName != "" ){
                        if(deptName.indexOf(name) >= 0){
                            $("#xdm").append('<option value="' + code + '">'  + name + '</option>');
                        }
                    }else{
                        $("#xdm").append('<option value="' + code + '">'  + name + '</option>');
                    }
                }
            }
        }, function (data) {
        });
        //初始化大类
        $("#dldm").html('<option value="">请选择大类</option>');
        var ajax = new $ax(Feng.ctxPath + "/dict/listDictsByCode?dictTypeCode=LAND_TYPE", function (data) {
            category = data.data;
            for (var i = 0; i < data.data.length; i++) {
                var name = data.data[i].name;
                var code = data.data[i].code;
                if(data.data[i].parentId === 0){
                    $("#dldm").append('<option value="' + code + '">'  + name + '</option>');
                }
            }
        }, function (data) {
        });

        //初始化行业
        $("#hydm").html('<option value="">请选择行业</option>');
        var hyajax = new $ax(Feng.ctxPath + "/dict/listDictsByCode?dictTypeCode=INDUSTRY", function (data) {
            for (var i = 0; i < data.data.length; i++) {
                var name = data.data[i].name;
                var code = data.data[i].code;
                if(data.data[i].parentId === 0){
                    $("#hydm").append('<option value="' + code + '">'  + name + '</option>');
                }
            }
        }, function (data) {
        });
        //初始化处置类型选择
        $("#landStatus").html('<option value="">请选择处置类型</option>');
        $("#landStatus").append('<option value="1">收储再开发</option>');
        $("#landStatus").append('<option value="2">自主开发</option>');
        $("#landStatus").append('<option value="3">技术提升</option>');
        $("#landStatus").append('<option value="4">复垦耕地</option>');
        $("#landStatus").append('<option value="5">司法处置或转让</option>');
        qxAjax.start();
        ajax.start();
        hyajax.start();
        form.render();
    };
    activeDlSelect();

    //渲染时间选择框
    laydate.render({
        elem: '#kssj',
        range: false
    });
    laydate.render({
        elem: '#wcsj',
        range: false
    });
    laydate.render({
        elem: '#crsj',
        range: false
    });

    form.on('select(dldmSelect)',function (data){
        var value = data.value;
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#dlmc").val(text);
        $("#xldm").html('<option value="">请选择小类</option>');
        if(value != "" && category.length >= 0){
            for (var i = 0; i < category.length; i++) {
                var name = category[i].name;
                var code = category[i].code;
                if(code != value && code.includes(value)){
                    $("#xldm").append('<option value="' + code + '">'  + name + '</option>');
                }
            }
        }
        form.render();
    });
    form.on('select(xldmSelect)',function (data){
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#xlmc").val(text);
    });
    form.on('select(hydmSelect)',function (data){
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#hymc").val(text);
    });
    form.on('select(xdmSelect)',function (data){
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#xmc").val(text);
    });
    //获取指定class的父节点
    function getParents(element, className) {
        //dom.getAttribute('class')==dom.className，两者等价
        var returnParentElement = null;
        function getpNode(element, className) {
            //创建父级节点的类数组
            let pClassList = element.parentNode.getAttribute('class');
            let pNode = element.parentNode;
            if (!pClassList) {
                //如果未找到类名数组，就是父类无类名，则再次递归
                getpNode(pNode, className);
            } else if (pClassList && pClassList.indexOf(className) < 0) {
                //如果父类的类名中没有预期类名，则再次递归
                getpNode(pNode, className);
            } else if (pClassList && pClassList.indexOf(className) > -1) {
                returnParentElement = pNode;
            }

        }
        getpNode(element, className);
        //console.log(returnParentElement);
        return returnParentElement;
    }

    //实施状态
    form.on('select(sfssSelect)',function (data){
        var value = data.value;
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        var firstNode = getParents(e,"layui-row");
        var html = '';
        if(value == "0"){
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">实施状态</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="sfss" id="sfss" lay-filter="sfssSelect">\n' +
                '                                <option value="">请选择实施状态</option>\n' +
                '                                <option value="0" selected = "selected">未实施</option>\n' +
                '                                <option value="1">正在实施</option>\n' +
                '                                <option value="2">实施已完成</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div><div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">未实施原因</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="wssyy" id="wssyy">\n' +
                '                                <option value="">请选择未实施原因</option>\n' +
                '                                <option value="1">未到计划的再开发时序</option>\n' +
                '                                <option value="2">企业原因导致无法实施</option>\n' +
                '                                <option value="3">政府原因导致无法实施</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md12">\n' +
                '                        <label class="layui-form-label">具体原因描述</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <textarea name="tjyy" id="tjyy" class="layui-textarea"></textarea>\n' +
                '                        </div>\n' +
                '                    </div>';
        }else if(value == "1"){
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">实施状态</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="sfss" id="sfss" lay-filter="sfssSelect">\n' +
                '                                <option value="">请选择实施状态</option>\n' +
                '                                <option value="0">未实施</option>\n' +
                '                                <option value="1" selected="selected">正在实施</option>\n' +
                '                                <option value="2">实施已完成</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">开始时间</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="kssj" name="kssj" placeholder="请输入开始时间" type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>';
        }else if(value == "2"){
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">实施状态</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="sfss" id="sfss" lay-filter="sfssSelect">\n' +
                '                                <option value="">请选择实施状态</option>\n' +
                '                                <option value="0">未实施</option>\n' +
                '                                <option value="1">正在实施</option>\n' +
                '                                <option value="2" selected="selected">实施已完成</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">完成时间</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="wcsj" name="wcsj" placeholder="请输入完成时间" type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>';
        }else{
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">实施状态</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="sfss" id="sfss" lay-filter="sfssSelect">\n' +
                '                                <option value="">请选择实施状态</option>\n' +
                '                                <option value="0">未实施</option>\n' +
                '                                <option value="1">正在实施</option>\n' +
                '                                <option value="2">实施已完成</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div>';
        }
        firstNode.innerHTML = (html);
        form.render();
    });



    //处置类型
    form.on('select(statusSelect)',function (data){
        var value = data.value;
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        var firstNode = getParents(e,"layui-row");
        var html = '';
        if(value == "1"){
             html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">处置类型</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="landStatus" id="landStatus" lay-filter="statusSelect">\n' +
                '                                <option value="">请选择处置类型</option>\n' +
                '                                <option value="1" selected="selected">收储再开发</option>\n' +
                '                                <option value="2">自主开发</option>\n' +
                '                                <option value="3">技术提升</option>\n' +
                '                                <option value="4">复垦耕地</option>\n' +
                '                                <option value="5">司法处置或转让</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div><div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">再开发时序</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="zkfsx" name="zkfsx" type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">开发状态</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="scStatus" name="scStatus"  type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">成本核算</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input name="cbhs" id="cbhs"   type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">协议签订</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="xyqd" name="xyqd"    type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">土地供应</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="tdgy" name="tdgy"   type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md12">\n' +
                '                            <label class="layui-form-label">具体进展情况</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <textarea name="sczkfjz" id="sczkfjz" class="layui-textarea"></textarea>\n' +
                '                            </div>\n' +
                '                        </div>';

        }else if(value == "2"){
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">处置类型</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="landStatus" id="landStatus" lay-filter="statusSelect">\n' +
                '                                <option value="">请选择处置类型</option>\n' +
                '                                <option value="1">收储再开发</option>\n' +
                '                                <option value="2" selected="selected">自主开发</option>\n' +
                '                                <option value="3">技术提升</option>\n' +
                '                                <option value="4">复垦耕地</option>\n' +
                '                                <option value="5">司法处置或转让</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div><div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">再开发时序</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="zkfsx" name="zkfsx"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">规划条件变更</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input name="ghtjbg" id="ghtjbg"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">规划工程审批</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="ghgcsp" name="ghgcsp"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">成效认定</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="cxrd" name="cxrd"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>';
        }else if(value == "3"){
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">处置类型</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="landStatus" id="landStatus" lay-filter="statusSelect">\n' +
                '                                <option value="">请选择处置类型</option>\n' +
                '                                <option value="1">收储再开发</option>\n' +
                '                                <option value="2">自主开发</option>\n' +
                '                                <option value="3" selected="selected">技术提升</option>\n' +
                '                                <option value="4">复垦耕地</option>\n' +
                '                                <option value="5">司法处置或转让</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div><div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">再开发时序</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="zkfsx" name="zkfsx"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">提升方案编制</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input name="tsfabz" id="tsfabz"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">提升方案实施</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="tsfass" name="tsfass"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">提升成效证明</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="tscxzm" name="tscxzm" type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>';
        }else if(value == "4"){
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">处置类型</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="landStatus" id="landStatus" lay-filter="statusSelect">\n' +
                '                                <option value="">请选择处置类型</option>\n' +
                '                                <option value="1">收储再开发</option>\n' +
                '                                <option value="2">自主开发</option>\n' +
                '                                <option value="3">技术提升</option>\n' +
                '                                <option value="4" selected="selected">复垦耕地</option>\n' +
                '                                <option value="5">司法处置或转让</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div><div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">再开发时序</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="zkfsx" name="zkfsx"  placeholder="请输入再开发时序" type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">项目立项</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input name="xmlx" id="xmlx" placeholder="请输入项目立项" type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">项目施工</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="xmsg" name="xmsg"  placeholder="请输入项目施工" type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">项目验收</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="xmys" name="xmys"  placeholder="请输入项目验收" type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                        <div class="layui-inline layui-col-md6">\n' +
                '                            <label class="layui-form-label">项目指标收益</label>\n' +
                '                            <div class="layui-input-block">\n' +
                '                                <input id="xmzbsy" name="xmzbsy"  placeholder="请输入项目指标收益" type="text" class="layui-input"/>\n' +
                '                            </div>\n' +
                '                        </div>';
        }else if(value == "5"){
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">处置类型</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="landStatus" id="landStatus" lay-filter="statusSelect">\n' +
                '                                <option value="">请选择处置类型</option>\n' +
                '                                <option value="1">收储再开发</option>\n' +
                '                                <option value="2">自主开发</option>\n' +
                '                                <option value="3">技术提升</option>\n' +
                '                                <option value="4">复垦耕地</option>\n' +
                '                                <option value="5" selected="selected">司法处置或转让</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div><div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">再开发时序</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="zkfsx" name="zkfsx"  type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                ' <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">基本情况</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input name="jbqk" id="jbqk" placeholder="请输入基本情况" type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                    <div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">地块推介</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <input id="dktj" name="dktj" placeholder="请输入地块推介" type="text" class="layui-input"/>\n' +
                '                        </div>\n' +
                '                    </div>';
        }else{
            html = '<div class="layui-inline layui-col-md6">\n' +
                '                        <label class="layui-form-label">处置类型</label>\n' +
                '                        <div class="layui-input-block">\n' +
                '                            <select name="landStatus" id="landStatus" lay-filter="statusSelect">\n' +
                '                                <option value="">请选择处置类型</option>\n' +
                '                                <option value="1">收储再开发</option>\n' +
                '                                <option value="2">自主开发</option>\n' +
                '                                <option value="3">技术提升</option>\n' +
                '                                <option value="4">复垦耕地</option>\n' +
                '                                <option value="5">司法处置或转让</option>\n' +
                '                            </select>\n' +
                '                        </div>\n' +
                '                    </div>';
        }
        firstNode.innerHTML = (html);
        form.render();
    });

    /**
     *
     * @param data 需要限制小数位数的数字
     * @param num 保留小数位数
     * @returns {*}
     */



    //执行实例
    var uploadInst = upload.render({
        elem: '#areaBtn'
        , url: Feng.ctxPath + '/landInfo/areaUpload'
        ,accept: 'file'
        ,acceptMime:'file/txt'
        ,size: 50 //最大允许上传的文件大小
        ,done: function (res) {
            if (res.code==500){
                Feng.error("上传失败!" + res.message);
            }else{
                $("#areaInfo").val(res.data.areaInfo);
                $("#areaFileName").val(res.data.originalFilename);
                $("#landArea").val(res.data.area);
            }
        }
        ,error: function () {
            Feng.error("上传失败!");
            //请求异常回调
        }
    });
    //多文件列表示例
    var demoListView = $('#demoList')
        ,uploadListIns = upload.render({
        elem: '#testList'
        ,url: Feng.ctxPath + '/system/upload?businessKey='+$("#businessKey").val() //改成您自己的上传接口
        ,accept: 'file'
        ,multiple: true
        ,auto: false
        ,bindAction: '#testListAction'
        ,choose: function(obj){
            files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //读取本地文件
            obj.preview(function(index, file, result){
                var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td style="display:none"></td>'
                    ,'<td>'+ file.name +'</td>'
                    ,'<td>'+ (file.size/1024).toFixed(1) +'kb</td>'
                    ,'<td>等待上传</td>'
                    ,'<td>'
                    ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                    ,'</tr>'].join(''));

                //单个重传
                tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function(){
                    var tds = tr.children();
                    var fileID = tds.eq(0).html();
                    /*var receiveUserName = $("#receiveUserName").val();
                    var receiveUser = $("#receiveUser").val();
                    var shareTitle = $("#shareTitle").val();
                    var businessKey = $("#businessKey").val();
                    var shareId = $("#shareId").val();
                    var remark = $("#remark").val();*/
                    if(fileID != ""){
                        $.ajax({
                            url: Feng.ctxPath + '/system/deleteFiles',
                            type: "POST",
                            data: {"ids":fileID},
                            dataType:"json",
                            success: function (res) {
                                delete files[index]; //删除对应的文件
                                tr.remove();
                                uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                            },
                            error: function (err) {
                                console.log('err', err);
                            }
                        });
                    }else{
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    }
                   /* $("#receiveUserName").val(receiveUserName);
                    $("#receiveUser").val(receiveUser);
                    $("#shareTitle").val(shareTitle);
                    $("#businessKey").val(businessKey);
                    $("#shareId").val(shareId);
                    $("#remark").val(remark);*/
                });
                demoListView.append(tr);
            });
        }
        ,done: function(res, index, upload){
            if(res.success){ //上传成功
                var tr = demoListView.find('tr#upload-'+ index)
                    ,tds = tr.children();
                tds.eq(0).html(res.data.fileId);
                tds.eq(3).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(4).html('<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'); //清空操作
                //删除
                tr.find('.demo-delete').on('click', function(){
                    var fileID = tds.eq(0).html();
                   /* var receiveUserName = $("#receiveUserName").val();
                    var receiveUser = $("#receiveUser").val();
                    var shareTitle = $("#shareTitle").val();
                    var businessKey = $("#businessKey").val();
                    var shareId = $("#shareId").val();
                    var remark = $("#remark").val();*/
                    //debugger;
                    if(fileID != ""){
                        $.ajax({
                            url: Feng.ctxPath + '/system/deleteFiles',
                            type: "POST",
                            data: {"ids":fileID},
                            dataType:"json",
                            success: function (res) {
                                delete files[index]; //删除对应的文件
                                tr.remove();
                                uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                            },
                            error: function (err) {
                                console.log('err', err);
                            }
                        });
                    }else{
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    }
                 /*   $("#receiveUserName").val(receiveUserName);
                    $("#receiveUser").val(receiveUser);
                    $("#shareTitle").val(shareTitle);
                    $("#businessKey").val(businessKey);
                    $("#shareId").val(shareId);
                    $("#remark").val(remark);*/
                });
                return delete this.files[index]; //删除文件队列已经上传成功的文件
            }
            this.error(index, upload);
        }
        ,error: function(index, upload){
            var tr = demoListView.find('tr#upload-'+ index)
                ,tds = tr.children();
            tds.eq(3).html('<span style="color: #FF5722;">上传失败</span>');
            tds.eq(4).find('.demo-reload').removeClass('layui-hide'); //显示重传
        }
    });
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/landdetail/saveLandDetail", function (data) {
            Feng.success("提交成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("提交失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
        return false;
    });
});

function deleteFile(obj,fileId){
    var td = $(obj).parent();
    var tr = td.parent("tr");
    if(fileId != ""){
        $.ajax({
            url: Feng.ctxPath + '/system/deleteFiles',
            type: "POST",
            data: {"ids":fileId},
            dataType:"json",
            success: function (res) {
                tr.remove();
            },
            error: function (err) {
                console.log('err', err);
            }
        });
    }else{
        tr.remove();
    }
}

function numLimit(data,num){
    // 先把非数字的都替换掉(空)，除了数字和.
    data = data.replace(/[^\d.]/g, "");
    // 必须保证第一个为数字而不是.
    data = data.replace(/^\./g, "");
    // 保证只有出现一个.而没有多个.
    data = data.replace(/\.{3,}/g, "");
    // 保证.只出现一次，而不能出现两次以上
    data =data
        .replace(".", "$#$")
        .replace(/\./g, "")
        .replace("$#$", ".");
    // 限制几位小数
    let subscript = -1;
    for (let i in data) {
        if (data[i] === ".") {
            subscript = i;
        }
        if (subscript !== -1) {
            if (i - subscript > num) {
                data = data.substring(0, data.length - 1);
            }
        }
    }
    return data;
}
