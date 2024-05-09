
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
            {field: 'landCode', align: 'center', title: '编码'},
            {field: 'xmc', sort: false,align: 'center', title: '县名称'},
            {field: 'pqbh', sort: false,align: 'center', title: '片区编号'},
            {field: 'xmmc', sort: false,align: 'center', title: '项目名称'},
            {field: 'dkbh', sort: false,align: 'center', title: '地块编号'},
            {field: 'dkmj', sort: false,align: 'center', title: '地块面积'},
            {field: 'dlmc', sort: false,align: 'center', title: '大类名称'},
            {field: 'xlmc', sort: false, align: 'center',title: '小类名称'},
            {field: 'xzyt', sort: false, align: 'center',title: '现状用途'},
            {field: 'ghyt', sort: false, align: 'center',title: '规划用途'},
            {field: 'remark', sort: false, align: 'center',title: '备注'},
            {field: 'createUserName', sort: false,align: 'center', title: '创建人名字'},
            {field: 'createTime', sort: false,align: 'center', title: '创建时间'},
            {field: 'updateUserName', sort: false,align: 'center', title: '修改人名字'},
            {field: 'updateTime', sort: false,align: 'center', title: '修改时间'}

        ]];
    };

    // 渲染表格
    table.render({
        id: addLandTable.tableId,
        elem: '#' + addLandTable.tableId,
        url:Feng.ctxPath + '/landdetail/selectList?category=',
        cellMinWidth: 100,
        cols: addLandTable.initColumn()
    });
    /**
     * 点击查询按钮
     */
    addLandTable.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $('#timeLimit').val();
        queryData['xdm'] = '';
        queryData['xmmc'] = $('#xmmc').val();
        queryData['landStatus'] = $('#landStatus').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        queryData['xdm'] = value;

        table.reload(addLandTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        addLandTable.search();
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var checkStatus = table.checkStatus(addLandTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        debugger;
        if (ids.length != 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        var id = ids[0];
        var ajax = new $ax(Feng.ctxPath + "/cycle/saveLandDis?id="+id, function (data) {
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

