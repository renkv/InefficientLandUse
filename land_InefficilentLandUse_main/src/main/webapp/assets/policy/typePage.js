layui.use(['table', 'admin', 'laydate','ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;

    /**
     * 文件政策管理
     */
    var policyMainTable = {
        tableId: "policyMainTable"
    };

    /**
     * 初始化表格的列
     */
    policyMainTable.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'fileId', hide: true, title: ''},
            {field: 'policyName', sort: true, title: '政策名称',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showDetail">' + d.policyName+ '</a></div>';
                    return html;
                }},
            {field: 'policyType', sort: true, title: '政策类型'},
            {field: 'fileName', sort: true, title: '文件名称',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showPre">' + d.fileName+ '</a></div>';
                    return html;
                }},
            {field: 'fileSizeKb', sort: true, title: '文件大小'},
            {field: 'filePath', sort: true, title: '文件路径'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'createUserName',sort: true, title: '创建人'},
            {field: 'updateTime', sort: true, title: '修改时间'},
            {field: 'updateUserName', sort: true, title: '修改人'}
        ]];
    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + policyMainTable.tableId,
        url: Feng.ctxPath + '/policy/selectList?policyType='+$("#policyType").val(),
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: policyMainTable.initColumn(),
        done:function(res, curr, count){
            $("[data-field = 'policyType']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("国家级");
                }else if($(this).text() == '2'){
                    $(this).text("省级");
                }else if($(this).text() == '3'){
                    $(this).text("市级");
                }else if($(this).text() == '4'){
                    $(this).text("县级");
                }
            });
        }
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
    policyMainTable.search = function () {
        var queryData = {};
        queryData['policyType'] = $('#policyType').val();
        queryData['policyName'] = $('#policyName').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(policyMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    table.on('tool(policyMainTable)', function (obj) {
        var data = obj.data;//获取监听点击当前行的所有信息[object，object]
        var layEvent = obj.event;
        if (layEvent === 'showDetail') {
            func.open({
                height: 1000,
                title: '详情',
                content: Feng.ctxPath + '/policy/detail?fileId=' + data.fileId,
                tableId: policyMainTable.tableId,
                endCallback: function () {
                    //table.reload(shareFileTInfo.tableId);
                }
            });
        }else if(layEvent === 'showPre'){
            window.open(Feng.ctxPath+"/policy/showPre?fileId="+data.fileId);
        }
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        policyMainTable.search();
    });



});
