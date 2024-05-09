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
    var planMainTable = {
        tableId: "planMainTable"
    };

    /**
     * 初始化表格的列
     */
    planMainTable.initColumn = function () {
        return [[

            {field: 'id', hide: true,align: 'center',fixed: 'left', title: 'ID'},
            {field: 'landCode', align: 'center',fixed: 'left', title: '地块编码'},
            {field: 'xmc', sort: false,align: 'center',merge:true, fixed: 'left',title: '县名称'},
            {field: 'pqbh', sort: false,align: 'center',merge:true,fixed: 'left', title: '片区编号'},
            {field: 'xmmc', sort: false,align: 'center',fixed: 'left', title: '项目名称',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showRec">' + d.xmmc+ '</a></div>';
                    return html;
                }
            },
            {field: 'dkbh', sort: false,align: 'center', title: '地块编号',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showMap">' + d.dkbh+ '</a></div>';
                    return html;
                }
            },
            {field: 'dkmj', sort: false,align: 'center', title: '地块面积'},
            {field: 'dlmc', sort: false,align: 'center', title: '大类名称'},
            {field: 'xlmc', sort: false, align: 'center',title: '小类名称'},
            {field: 'xzyt', sort: false, align: 'center',title: '现状用途'},
            {field: 'ghyt', sort: false, align: 'center',title: '规划用途'},
            {field: 'planType', sort: false, align: 'center',title: '计划类型'},
            {field: 'planName', sort: false, align: 'center',title: '计划名称'},
            {field: 'planStartTime', sort: false, align: 'center',title: '计划开始时间'},
            {field: 'planEndTime', sort: false, align: 'center',title: '计划完成时间'},
            {field: 'actStartTime', sort: false, align: 'center',title: '实际开始时间'},
            {field: 'actEndTime', sort: false, align: 'center',title: '实际完成时间'},
            {field: 'planArea', sort: false, align: 'center',title: '计划完成面积'},
            {field: 'currentArea', sort: false, align: 'center',title: '当前已完成面积'},
            {field: 'remArea', sort: false, align: 'center',title: '剩余面积'},
            {field: 'curStatus', sort: false, align: 'center',title: '当前状态'},
            {field: 'curProgress', sort: false, align: 'center',title: '当前进展'},
            {field: 'reasonsType', sort: false, align: 'center',title: '困难原因'},
            {field: 'remark', sort: false, align: 'center',title: '备注'}
        ]];
    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + planMainTable.tableId,
        url: Feng.ctxPath + '/plan/selectList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: planMainTable.initColumn(),
        done:function(res, curr, count){
            $("[data-field = 'planType']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("低效企业");
                }else if($(this).text() == '2'){
                    $(this).text("城中村改造");
                }else if($(this).text() == '3'){
                    $(this).text("城市更新");
                }else if($(this).text() == '4'){
                    $(this).text("开发区低效处置");
                }else if($(this).text() == '5'){
                    $(this).text("其他处置");
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
    planMainTable.search = function () {
        var queryData = {};

        queryData['policyType'] = $('select[name="policyType"]').next().find('.layui-this').attr('lay-value');
        queryData['policyName'] = $('#policyName').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(planMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    table.on('tool(planMainTable)', function (obj) {
        var data = obj.data;//获取监听点击当前行的所有信息[object，object]
        var layEvent = obj.event;
        if (layEvent === 'showDetail') {
            func.open({
                height: 1000,
                title: '详情',
                content: Feng.ctxPath + '/policy/detail?fileId=' + data.fileId,
                tableId: planMainTable.tableId,
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
    planMainTable.openAddPage = function () {
        func.open({
            height: 800,
            title: '新增政策',
            content: Feng.ctxPath + '/policy/add',
            tableId: planMainTable.tableId,
            endCallback: function () {
                table.reload(planMainTable.tableId);
            }
        });
    };

    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    planMainTable.jumpEditPage = function (data) {
        func.open({
            height: 800,
            title: '编辑',
            content: Feng.ctxPath + '/policy/edit?fileId=' + data.fileId,
            tableId: planMainTable.tableId,
            endCallback: function () {
                //table.reload(planMainTable.tableId);
            }
        });
    };
    //btnEdt 编辑按钮点击事件
    $('#btnEdt').click(function () {
        var checkStatus = table.checkStatus(planMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        planMainTable.jumpEditPage(checkStatus.data[0]);
    });
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    planMainTable.jumpDetailPage = function (data) {
        func.open({
            height: 800,
            title: '详情',
            content: Feng.ctxPath + '/fileShare/detail?shareId=' + data.shareId,
            tableId: planMainTable.tableId,
            endCallback: function () {
                //table.reload(planMainTable.tableId);
            }
        });
    };


    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    planMainTable.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/fileShare/deleteById?shareId="+data.shareId, function (data) {
                Feng.success("删除成功!");
                table.reload(planMainTable.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.planMainTable.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };



    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        planMainTable.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {

        planMainTable.openAddPage();

    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(planMainTable.tableId);
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
                url: Feng.ctxPath + '/policy/delete',
                type: "POST",
                dataType:"json",
                data:{ids:ids},
                success: function (res) {
                    if(res.code == "500"){
                        Feng.error(res.message);
                    }else{
                        Feng.success("删除成功！");
                        table.reload(planMainTable.tableId);
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
