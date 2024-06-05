layui.use(['table','layer', 'form', 'admin', 'laydate', 'ax', 'formSelects'], function () {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var $ax = layui.ax;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;
    var formSelects = layui.formSelects;

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
// 渲染表格
    table.render({
        id: addLandTable.tableId,
        elem: '#' + addLandTable.tableId,
        page: true,
        url:Feng.ctxPath + '/landdetail/selectList?category=',
        cellMinWidth: 100,
        cols: addLandTable.initColumn()
    });
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    /**
     * 点击查询按钮
     */
    addLandTable.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $('#timeLimit').val();
        queryData['xmmc'] = $('#xmmc').val();
        queryData['deptName'] = $('#deptName').val();
        var landType = $('select[name="landType"]').next().find('.layui-this').attr('lay-value');
        var landStatus = $('select[name="landStatus"]').next().find('.layui-this').attr('lay-value');
        if(landType != undefined){
            queryData['landType'] = value;
        }else{
            queryData['landType'] = "";
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
    $("#saveButton").bind("click", function () {
        var checkStatus = table.checkStatus(addLandTable.tableId);
        var ids = [];
        var xmmcs = [];
        var datas = [];
        var id = "";
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.landCode);
            xmmcs.push(o.xmmc);
            datas.push(o);
        });
        if (ids.length != 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        if (ids.length == 1) {
            id = ids[0];
            parent.LandInfoDlg.data.landCode=datas[0].landCode;
            parent.LandInfoDlg.data.xmmc=datas[0].xmmc;
            parent.LandInfoDlg.data.dkmj=datas[0].dkmj;
        }
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

});