layui.use(['table', 'admin', 'laydate','ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;

    /**
     * 文件发送管理
     */
    var shareFileTInfo = {
        tableId: "shareFileTable"
    };

    /**
     * 初始化表格的列
     */
    shareFileTInfo.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'shareId', hide: true, title: ''},
            {field: 'shareNo', sort: true, title: '编号'},
            {field: 'shareTitle', sort: true, title: '主题'},
            {field: 'shareType', sort: true, title: '类型'},
            {field: 'createUserName', sort: true, title: '创建人'},
            {field: 'receiveUserName', sort: true, title: '接收人'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'createUser',hide:true, sort: true, title: '数据创建人'},
            {field: 'updateTime', sort: true, title: '修改时间'},
            {field: 'updateUserName', sort: true, title: '修改人'},
            {field: 'remark', sort: true, title: '备注'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    shareFileTInfo.search = function () {
        var queryData = {};

        queryData['shareNo'] = $('#shareNo').val();
        queryData['shareTitle'] = $('#shareTitle').val();
        queryData['shareType'] = $('#shareType').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(shareFileTInfo.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 弹出添加
     */
    shareFileTInfo.openAddPage = function () {
        func.open({
            height: 800,
            title: '新增文件发送',
            content: Feng.ctxPath + '/fileShare/add',
            tableId: shareFileTInfo.tableId,
            endCallback: function () {
                table.reload(shareFileTInfo.tableId);
            }
        });
    };

    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    shareFileTInfo.jumpEditPage = function (data) {
        func.open({
            height: 800,
            title: '编辑',
            content: Feng.ctxPath + '/fileShare/edit?shareId=' + data.shareId,
            tableId: shareFileTInfo.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    shareFileTInfo.jumpDetailPage = function (data) {
        func.open({
            height: 800,
            title: '详情',
            content: Feng.ctxPath + '/fileShare/detail?shareId=' + data.shareId,
            tableId: shareFileTInfo.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };

    /**
     * 导出excel按钮
     */
    shareFileTInfo.exportExcel = function () {
        var checkRows = table.checkStatus(shareFileTInfo.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    shareFileTInfo.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/fileShare/deleteById?shareId="+data.shareId, function (data) {
                Feng.success("删除成功!");
                table.reload(shareFileTInfo.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.shareFileTInfo.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + shareFileTInfo.tableId,
        url: Feng.ctxPath + '/fileShare/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: shareFileTInfo.initColumn()
    });
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        shareFileTInfo.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {

        shareFileTInfo.openAddPage();

    });

    // 导出excel
    $('#btnExp').click(function () {
        shareFileTInfo.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + shareFileTInfo.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            shareFileTInfo.jumpEditPage(data);
        } else if (layEvent === 'delete') {
            shareFileTInfo.onDeleteItem(data);
        }else if (layEvent === 'detail') {
            shareFileTInfo.jumpDetailPage(data);
        }
    });
});
/*$(function () {
    var panehHidden = false;
    if ($(this).width() < 769) {
        panehHidden = true;
    }
    $('#myContiner').layout({initClosed: panehHidden, west__size: 260});
});*/
