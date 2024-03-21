layui.use(['table', 'admin','laydate', 'ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;

    /**
     * 文件发送管理
     */
    var receiveFileInfo = {
        tableId: "receiveFileTable"
    };

    /**
     * 初始化表格的列
     */
    receiveFileInfo.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'shareTitle', sort: true, title: '主题'},
            {field: 'createUser', sort: true, title: '发送人'},
            {field: 'createTime', sort: true, title: '接收时间'},
            {field: 'remark', sort: true, title: '备注'},
            {field: 'isRead', sort: true, title: '是否已读'},
            {field: 'isReply', sort: true, title: '是否回复'},
            /*{field: 'replyInfo', sort: true, title: '回复内容'},*/
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + receiveFileInfo.tableId,
        url: Feng.ctxPath + '/fileReceive/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: receiveFileInfo.initColumn()
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
    receiveFileInfo.search = function () {
        var queryData = {};

        queryData['shareTitle'] = $('#shareTitle').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(receiveFileInfo.tableId, {
            where: queryData, page: {curr: 1}
        });
    };
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    receiveFileInfo.jumpDetailPage = function (data) {
        func.open({
            height: 800,
            title: '详情',
            content: Feng.ctxPath + '/fileReceive/detail?id=' + data.id,
            tableId: receiveFileInfo.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };
    /**
     * 跳转回复页面
     * @param data
     */
    receiveFileInfo.jumpReplyPage = function (data) {
        var isReply = data.isReply;
        if(isReply == "是"){
            Feng.error("您已经回复过了！");
        }else{
            func.open({
                height: 400,
                title: '回复',
                content: Feng.ctxPath + '/fileReceive/toReply?id=' + data.id,
                tableId: receiveFileInfo.tableId,
                endCallback: function () {
                    table.reload(receiveFileInfo.tableId);
                }
            });
        }
    };
    /**
     * 跳转转发页面
     * @param data
     */
    receiveFileInfo.jumpForwardPage = function (data) {
        func.open({
            height: 800,
            title: '转发',
            content: Feng.ctxPath + '/fileReceive/toForward?id=' + data.id,
            tableId: receiveFileInfo.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        receiveFileInfo.search();
    });

    // 工具条点击事件
    table.on('tool(' + receiveFileInfo.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'detail') {
            receiveFileInfo.jumpDetailPage(data);
        } else if (layEvent === 'reply') {
            receiveFileInfo.jumpReplyPage(data);
        } else if (layEvent === 'forward') {
            receiveFileInfo.jumpForwardPage(data);
        }
    });
});

