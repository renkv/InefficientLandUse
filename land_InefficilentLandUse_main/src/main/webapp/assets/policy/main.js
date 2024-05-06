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
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'createUserName',sort: true, title: '创建人'},
            {field: 'updateTime', sort: true, title: '修改时间'},
            {field: 'updateUserName', sort: true, title: '修改人'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    policyMainTable.search = function () {
        var queryData = {};

        queryData['shareNo'] = $('#shareNo').val();
        queryData['shareTitle'] = $('#shareTitle').val();
        queryData['shareType'] = $('#shareType').val();
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
                content: Feng.ctxPath + '/landdetail/detail?id=' + data.id,
                tableId: detailMainTable.tableId,
                endCallback: function () {
                    //table.reload(shareFileTInfo.tableId);
                }
            });
        }else if(layEvent === 'showPre'){
            window.open(Feng.ctxPath+"/landdetail/showOnMap?value="+data.dkbh+"&key=DKBH&xmmc="+data.xmmc);
        }
    });

    /**
     * 弹出添加
     */
    policyMainTable.openAddPage = function () {
        func.open({
            height: 800,
            title: '新增政策',
            content: Feng.ctxPath + '/policy/add',
            tableId: policyMainTable.tableId,
            endCallback: function () {
                table.reload(policyMainTable.tableId);
            }
        });
    };

    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    policyMainTable.jumpEditPage = function (data) {
        func.open({
            height: 800,
            title: '编辑',
            content: Feng.ctxPath + '/fileShare/edit?shareId=' + data.shareId,
            tableId: policyMainTable.tableId,
            endCallback: function () {
                //table.reload(policyMainTable.tableId);
            }
        });
    };
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    policyMainTable.jumpDetailPage = function (data) {
        func.open({
            height: 800,
            title: '详情',
            content: Feng.ctxPath + '/fileShare/detail?shareId=' + data.shareId,
            tableId: policyMainTable.tableId,
            endCallback: function () {
                //table.reload(policyMainTable.tableId);
            }
        });
    };


    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    policyMainTable.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/fileShare/deleteById?shareId="+data.shareId, function (data) {
                Feng.success("删除成功!");
                table.reload(policyMainTable.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.policyMainTable.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + policyMainTable.tableId,
        url: Feng.ctxPath + '/policy/selectList',
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

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        policyMainTable.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {

        policyMainTable.openAddPage();

    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(policyMainTable.tableId);
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
                        table.reload(policyMainTable.tableId);
                    }
                },
                error: function (err) {
                    console.log('err', err);
                }
            });
            layer.close(index);
        });
    });


    // 工具条点击事件
    table.on('tool(' + policyMainTable.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            policyMainTable.jumpEditPage(data);
        } else if (layEvent === 'delete') {
            policyMainTable.onDeleteItem(data);
        }else if (layEvent === 'detail') {
            policyMainTable.jumpDetailPage(data);
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
