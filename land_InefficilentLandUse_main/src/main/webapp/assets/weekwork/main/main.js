layui.use(['table', 'admin','laydate', 'ax', 'func','upload'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;
    var upload = layui.upload;

    /**
     * 文件发送管理
     */
    var weekWorkInfo = {
        tableId: "weekWorkMainTable"
    };

    /**
     * 初始化表格的列
     */
    weekWorkInfo.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'year', sort: true, title: '年'},
            {field: 'month', sort: true, title: '月'},
            {field: 'week', sort: true, title: '周'},
            {field: 'departName', sort: true, title: '部门'},
            {field: 'createUserName', sort: true, title: '创建人'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        id: weekWorkInfo.tableId,
        elem: '#' + weekWorkInfo.tableId,
        url: Feng.ctxPath + '/weekWork/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: weekWorkInfo.initColumn()
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
    weekWorkInfo.search = function () {
        var queryData = {};

        queryData['createUserName'] = $('#createUserName').val();
        queryData['deptName'] = $('#deptName').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(weekWorkInfo.tableId, {
            where: queryData, page: {curr: 1}
        });
    };
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    weekWorkInfo.jumpDetailPage = function (data) {
        func.open({
            width: '1500px',
            height: 1200,
            title: '详情',
            content: Feng.ctxPath + '/weekWork/detail?id=' + data.id,
            tableId: weekWorkInfo.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };
    /**
     * 弹出添加
     */
    weekWorkInfo.openAddPage = function () {

        window.location.href = Feng.ctxPath + '/weekWork/add'
    };


    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        weekWorkInfo.search();
    });
    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        $.ajax({
            url: Feng.ctxPath + '/weekWork/checkAdd',
            type: "POST",
            dataType:"json",
            success: function (res) {
                console.log(res);
                if(res.code == "500"){
                    Feng.error(res.message);
                }else{
                    weekWorkInfo.openAddPage();
                }
            },
            error: function (err) {
                console.log('err', err);
            }
        });

    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(weekWorkInfo.tableId);
        var ids = [];
        console.log(checkStatus);
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
                url: Feng.ctxPath + '/weekWork/checkAndDel',
                type: "POST",
                dataType:"json",
                data:{ids:ids},
                success: function (res) {
                    if(res.code == "500"){
                        Feng.error(res.message);
                    }else{
                        Feng.success("删除成功！");
                        table.reload(weekWorkInfo.tableId);
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
    table.on('tool(' + weekWorkInfo.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'detail') {
            weekWorkInfo.jumpDetailPage(data);
        }else if(layEvent === 'excel'){
            weekWorkInfo.exportToExcel(data);
        }
    });
    weekWorkInfo.exportToExcel = function (data) {
        var form=$("<form>");
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");//提交方式为post
        form.attr("action",Feng.ctxPath + "/weekWork/exportToExcel?id="+data.id);//定义action

        $("body").append(form);
        form.submit();
    };

    //执行实例
    var uploadInst = upload.render({
        elem: '#btnImp'
        , url: '/weekWork/uploadExcel'
        ,accept: 'file'
        , done: function (res) {
            if (res.code==500){
                Feng.error("导入失败!" + res.message);
            }else{
                Feng.success("导入成功！");
                $('#btnSearch').click()
            }
        }
        , error: function () {
            Feng.error("导入失败!");
            //请求异常回调
        }
    });

});

