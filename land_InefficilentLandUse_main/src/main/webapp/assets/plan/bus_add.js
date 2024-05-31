
layui.use(['table','layer', 'form', 'admin', 'laydate', 'ax', 'formSelects'], function () {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var $ax = layui.ax;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;
    var formSelects = layui.formSelects;


    /**
     * 文件发送管理
     */
    var addLandTable = {
        tableId: "addLandTable"
    };

    /**
     * 初始化表格的列
     */
    addLandTable.initColumn = function () {
        return [[
            {fixed: 'left',type: 'checkbox'},
            {field: 'id', hide: true,align: 'center',title: 'ID'},
            {field: 'landCode', hide: true, align: 'center', title: '编码'},
            {field: 'xmc', sort: false,align: 'center', title: '县名称'},
            {field: 'pqbh', hide: true, sort: false,align: 'center', title: '片区编号'},
            {field: 'xmmc', sort: false,align: 'center', title: '项目名称'},
            {field: 'dkbh', sort: false,align: 'center', title: '地块编号'},
            {field: 'dkmj', sort: false,align: 'center', title: '地块面积'},
            {field: 'dlmc', sort: false,align: 'center', title: '大类名称'},
            {field: 'xlmc', sort: false, align: 'center',title: '小类名称'},
            {field: 'xzyt', sort: false, align: 'center',title: '现状用途'},
            {field: 'ghyt', hide: true, sort: false, align: 'center',title: '规划用途'},
            {field: 'remark', sort: false, align: 'center',title: '备注'},
            {field: 'createUserName', hide: true, sort: false,align: 'center', title: '创建人名字'},
            {field: 'createTime',  hide: true,sort: false,align: 'center', title: '创建时间'},
            {field: 'updateUserName',  hide: true,sort: false,align: 'center', title: '修改人名字'},
            {field: 'updateTime',  hide: true,sort: false,align: 'center', title: '修改时间'}

        ]];
    };

    //渲染时间选择框
    laydate.render({
        elem: '#planStartTime',
        range: false,
        min: Feng.currentDate()
    });

    // 渲染表格
    table.render({
        id: addLandTable.tableId,
        elem: '#' + addLandTable.tableId,
        url:Feng.ctxPath + '/landdetail/selectList?category=',
        cellMinWidth: 100,
        cols: addLandTable.initColumn()
    });

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
        qxAjax.start();
        ajax.start();
        hyajax.start();
        form.render();
    };
    activeDlSelect();

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

    /**
     * 点击查询按钮
     */
    addLandTable.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $('#timeLimit').val();
        queryData['xdm'] = '';
        queryData['xmmc'] = $('#xmmcSelect').val();
        var value = $('select[name="xdmSelect"]').next().find('.layui-this').attr('lay-value');
        var landStatus = $('select[name="landStatus"]').next().find('.layui-this').attr('lay-value');
        if(value != undefined){
            queryData['xdm'] = value;
        }else{
            queryData['xdm'] = "";
        }
        if(landStatus != undefined){
            queryData['landStatus'] = landStatus;
        }else{
            queryData['landStatus'] = "";
        }

        table.reload(addLandTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        addLandTable.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        /*debugger;
        $("#landDiv111").html("");
        var html = '<div class="layui-card-header">低效用地基础信息</div>\n' +
            '            <div class="layui-card-body">\n' +
            '                <div class="layui-form-item layui-row">\n' +
            '                    <div class="layui-inline layui-col-md6">\n' +
            '                        <label class="layui-form-label">低效用地类型</label>\n' +
            '                        <div class="layui-input-block">\n' +
            '                            <select name="category" id="category" lay-filter="categorySelect">\n' +
            '                                <option value="">请选择地块类型</option>\n' +
            '                                <option value="towns">低效城镇用地</option>\n' +
            '                                <option value="industries">低效产业用地</option>\n' +
            '                                <option value="villages">低效村庄用地</option>\n' +
            '                            </select>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>';
        $("#landDiv111").html(html);*/
        $("#landDiv").css("display","block");
        form.render();
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var checkStatus = table.checkStatus(addLandTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.landCode);
        });
        if (ids.length != 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        var id = ids[0];
        var ajax = new $ax(Feng.ctxPath + "/plan/savePlan?landCode="+id, function (data) {
            Feng.success("保存成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("保存失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
        //添加 return false 可成功跳转页面
        return false;
    });
});

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

