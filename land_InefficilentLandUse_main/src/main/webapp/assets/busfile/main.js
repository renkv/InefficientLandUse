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
    var busFileMainTable = {
        tableId: "busFileMainTable"
    };

    /**
     * 初始化表格的列
     */
    busFileMainTable.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'fileSuffix', hide: true, title: '后缀'},
            {field: 'fileYear',width:'20%', sort: false,align: 'center',title: '年份'},
            {field: 'countyName',width:'20%', sort: false, align: 'center',title: '区县'},
            {field: 'fileName',width:'60%',sort: false, align: 'center',title: '文件名称',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showPre">' + d.fileName+ '</a></div>';
                    return html;
                }},
        ]];
    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + busFileMainTable.tableId,
        url: Feng.ctxPath + '/busfile/selectList',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: busFileMainTable.initColumn(),
        done:function(res, curr, count){

        }
    });

    /**
     * 点击查询按钮
     */
    busFileMainTable.search = function () {
        var queryData = {};

        queryData['policyType'] = $('select[name="policyType"]').next().find('.layui-this').attr('lay-value');
        queryData['policyName'] = $('#policyName').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(busFileMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    table.on('tool(busFileMainTable)', function (obj) {
        var data = obj.data;//获取监听点击当前行的所有信息[object，object]
        var layEvent = obj.event;
        if (layEvent === 'showDetail') {
            func.open({
                height: 1000,
                title: '详情',
                content: Feng.ctxPath + '/policy/detail?fileId=' + data.fileId,
                tableId: busFileMainTable.tableId,
                endCallback: function () {
                    //table.reload(shareFileTInfo.tableId);
                }
            });
        }else if(layEvent === 'showPre'){
            var fileSuffix = data.fileSuffix;
            if(fileSuffix.toLowerCase()==='pdf' || fileSuffix.toLowerCase()==='doc'
                || fileSuffix.toLowerCase()==='docx'  || fileSuffix.toLowerCase()==='ofd'){
                window.open(Feng.ctxPath+"/busfile/showPre?id="+data.id);
            }else if(fileSuffix.toLowerCase()==='xls'
                || fileSuffix.toLowerCase()==='xlsx'){
                window.open(Feng.ctxPath+"/busfile/showExcel?id="+data.id);
            }else{
                window.open(Feng.ctxPath+"/busfile/download?id="+data.id);
            }
        }
    });

    /**
     * 弹出添加
     */
    busFileMainTable.openAddPage = function () {
        func.open({
            height: 800,
            title: '新增工作资料',
            content: Feng.ctxPath + '/busfile/add',
            tableId: busFileMainTable.tableId,
            endCallback: function () {
                table.reload(busFileMainTable.tableId);
            }
        });
    };

    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    busFileMainTable.jumpEditPage = function (data) {
        func.open({
            height: 800,
            title: '编辑',
            content: Feng.ctxPath + '/policy/edit?fileId=' + data.fileId,
            tableId: busFileMainTable.tableId,
            endCallback: function () {
                //table.reload(busFileMainTable.tableId);
            }
        });
    };
    //btnEdt 编辑按钮点击事件
    $('#btnEdt').click(function () {
        var checkStatus = table.checkStatus(busFileMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        busFileMainTable.jumpEditPage(checkStatus.data[0]);
    });
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    busFileMainTable.jumpDetailPage = function (data) {
        func.open({
            height: 800,
            title: '详情',
            content: Feng.ctxPath + '/fileShare/detail?shareId=' + data.shareId,
            tableId: busFileMainTable.tableId,
            endCallback: function () {
                //table.reload(busFileMainTable.tableId);
            }
        });
    };

    $(document).on('click', '[name="btnYear"]', function() {
        $(this).addClass("layui-btn-normal");
        $(this).removeClass( "layui-btn-primary");
        $(this).siblings('.layui-btn').removeClass('layui-btn-normal').addClass('layui-btn-primary');
        var year = $(this).val();
        var queryData = {};
        queryData['fileYear'] = year;
        table.reload(busFileMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    });


    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        busFileMainTable.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {

        busFileMainTable.openAddPage();

    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(busFileMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        ids = ids.join(",");
        layer.confirm('您确定要删除该数据吗？', function (index) {
            $.ajax({
                url: Feng.ctxPath + '/busfile/delete',
                type: "POST",
                dataType:"json",
                data:{ids:ids},
                success: function (res) {
                    if(res.code == "500"){
                        Feng.error(res.message);
                    }else{
                        Feng.success("删除成功！");
                        table.reload(busFileMainTable.tableId);
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
