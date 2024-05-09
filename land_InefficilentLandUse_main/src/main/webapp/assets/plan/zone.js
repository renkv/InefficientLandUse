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
    var busMainTable = {
        tableId: "busMainTable"
    };

    /**
     * 初始化表格的列
     */
    busMainTable.initColumn = function () {
        return [[
            {fixed: 'left',type: 'checkbox'},
            {field: 'id', hide: true,align: 'center',fixed: 'left', title: 'ID'},
            {field: 'landCode', align: 'center',fixed: 'left', title: '地块编码'},
            {field: 'planCode', align: 'center',fixed: 'left', title: '计划编码',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showDetail">' + d.planCode+ '</a></div>';
                    return html;
                }},
            {field: 'planName', sort: false, align: 'center',title: '计划名称'},
            {field: 'zoneName', sort: false, align: 'center',title: '开发区低效处置项目名称'},
            {field: 'planStartTime', sort: false, align: 'center',title: '计划开始时间'},
            {field: 'planEndTime', sort: false, align: 'center',title: '计划完成时间'},
            {field: 'actStartTime', sort: false, align: 'center',title: '实际开始时间'},
            {field: 'actEndTime', sort: false, align: 'center',title: '实际完成时间'},
            {field: 'planArea', sort: false, align: 'center',title: '计划完成面积'},
            {field: 'currentArea', sort: false, align: 'center',title: '当前已完成面积'},
            {field: 'remArea', sort: false, align: 'center',title: '剩余面积'},
            {field: 'planUnit', sort: false, align: 'center',title: '计划实施单位'},
            {field: 'actUnit', sort: false, align: 'center',title: '实际实施单位'},
            {field: 'curStatus', sort: false, align: 'center',title: '当前状态'},
            {field: 'curProgress', sort: false, align: 'center',title: '当前进展'},
            {field: 'reasonsType', sort: false, align: 'center',title: '困难原因'},
            {field: 'remark', sort: false, align: 'center',title: '备注'}
        ]];
    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + busMainTable.tableId,
        url: Feng.ctxPath + '/plan/selectList?planType=4',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: busMainTable.initColumn(),
        done:function(res, curr, count){
            $("[data-field = 'reasonsType']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("困难原因1");
                }else if($(this).text() == '2'){
                    $(this).text("困难原因2");
                }else if($(this).text() == '3'){
                    $(this).text("困难原因3");
                }else if($(this).text() == '4'){
                    $(this).text("困难原因4");
                }else if($(this).text() == '5'){
                    $(this).text("其他");
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
    busMainTable.search = function () {
        var queryData = {};

        queryData['policyType'] = $('select[name="policyType"]').next().find('.layui-this').attr('lay-value');
        queryData['policyName'] = $('#policyName').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(busMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    table.on('tool(busMainTable)', function (obj) {
        var data = obj.data;//获取监听点击当前行的所有信息[object，object]
        var layEvent = obj.event;
        if (layEvent === 'showDetail') {
            func.open({
                width:  "1800px",
                height: 1500,
                title: '开发区低效处置计划详情',
                content: Feng.ctxPath + '/plan/detail?id=' + data.id,
                tableId: busMainTable.tableId,
                endCallback: function () {
                    //table.reload(shareFileTInfo.tableId);
                }
            });
        }else if(layEvent === 'showPre'){
            window.open(Feng.ctxPath+"/policy/showPre?fileId="+data.fileId);
        }
    });

    /**
     * 弹出添加
     */
    busMainTable.openAddPage = function () {
        func.open({
            width:  "1800px",
            height: 1500,
            title: '新增开发区低效处置计划信息',
            content: Feng.ctxPath + '/plan/add?planType=4',
            tableId: busMainTable.tableId,
            endCallback: function () {
                table.reload(busMainTable.tableId);
            }
        });
    };

    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    busMainTable.jumpEditPage = function (data) {
        func.open({
            width:  "1800px",
            height: 1500,
            title: '开发区低效处置计划编辑',
            content: Feng.ctxPath + '/plan/edit?id=' + data.id,
            tableId: busMainTable.tableId,
            endCallback: function () {
                //table.reload(busMainTable.tableId);
            }
        });
    };
    //btnEdt 编辑按钮点击事件
    $('#btnEdt').click(function () {
        var checkStatus = table.checkStatus(busMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        busMainTable.jumpEditPage(checkStatus.data[0]);
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        busMainTable.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {

        busMainTable.openAddPage();

    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(busMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.fileId);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        ids = ids.join(",");
        layer.confirm('您确定要删除该数据吗？', function (index) {
            $.ajax({
                url: Feng.ctxPath + '/plan/delete',
                type: "POST",
                dataType:"json",
                data:{ids:ids},
                success: function (res) {
                    if(res.code == "500"){
                        Feng.error(res.message);
                    }else{
                        Feng.success("删除成功！");
                        table.reload(busMainTable.tableId);
                    }
                },
                error: function (err) {
                    console.log('err', err);
                }
            });
            layer.close(index);
        });
    });

});
