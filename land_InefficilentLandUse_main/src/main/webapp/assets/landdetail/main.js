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
    var threeMainTable = {
        tableId: "threeMainTable"
    };

    /**
     * 初始化表格的列
     */
    threeMainTable.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true,align: 'center', title: 'ID'},
            {field: 'xmc', sort: false,align: 'center', title: '县名称'},
            {field: 'pqbh', sort: false,align: 'center', title: '片区编号'},
            {field: 'xmmc', sort: false,align: 'center', title: '项目名称'},
            {field: 'xzdm', sort: false,align: 'center', title: '乡镇代码'},
            {field: 'xzmc', sort: false,align: 'center', title: '乡镇名称'},
            {field: 'dkbh', sort: false,align: 'center', title: '地块编号'},
            {field: 'dkmj', sort: false,align: 'center', title: '地块面积'},
            {field: 'dldm', sort: false,align: 'center', title: '大类代码'},
            {field: 'dlmc', sort: false,align: 'center', title: '大类名称'},
            {field: 'xldm', sort: false, align: 'center',title: '小类代码'},
            {field: 'xlmc', sort: false, align: 'center',title: '小类名称'},
            {field: 'xzyt', sort: false, align: 'center',title: '现状用途'},
            {field: 'xzrjl', sort: false, align: 'center',title: '现状容积率'},
            {field: 'xzjzmd', sort: false, align: 'center',title: '现状建筑密度'},
            {field: 'kfql', sort: false, align: 'center',title: '开发潜力'},
            {field: 'zkfsx', sort: false, align: 'center',title: '再开发时序'},
            {field: 'pqmj', sort: false, align: 'center',title: '片区面积'},
            {field: 'zb', sort: false, align: 'center',title: '坐标'},
            {field: 'createUserName', sort: false,align: 'center', title: '创建人名字'},
            {field: 'createTime', sort: false,align: 'center', title: '创建时间'},
            {field: 'updateUserName', sort: false,align: 'center', title: '修改人名字'},
            {field: 'updateTime', sort: false,align: 'center', title: '修改时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        id: threeMainTable.tableId,
        elem: '#' + threeMainTable.tableId,
        url: Feng.ctxPath + '/landdetail/selectList?category='+$('#category').val(),
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: threeMainTable.initColumn()
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
    threeMainTable.search = function () {
        var queryData = {};

        queryData['createUserName'] = $('#createUserName').val();
        queryData['deptName'] = $('#deptName').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(threeMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    threeMainTable.jumpDetailPage = function (data) {
        func.open({
            height: 800,
            title: '详情',
            content: Feng.ctxPath + '/landInfo/detail?id=' + data.id,
            tableId: threeMainTable.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };
    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    threeMainTable.jumpEditPage = function (data) {
        func.open({
            height: 800,
            title: '编辑',
            content: Feng.ctxPath + '/landInfo/edit?id=' + data.id,
            tableId: threeMainTable.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };
    /**
     * 弹出添加
     */
    threeMainTable.openAddPage = function () {
        func.open({
            height: 800,
            title: '新增三调中低效用地项目',
            content: Feng.ctxPath + '/landInfo/add?category=1',
            tableId: threeMainTable.tableId,
            endCallback: function () {
                table.reload(threeMainTable.tableId);
            }
        });
        //window.location.href = Feng.ctxPath + '/landInfo/add?category=1'
    };
    /**
     * 导出按钮
     */
    threeMainTable.exportToExcel = function () {
        var createUserName = $('#createUserName').val();
        var deptName = $('#deptName').val();
        var category = $('#category').val();
        var form=$("<form>");
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");//提交方式为post
        form.attr("action",Feng.ctxPath + "/landInfo/exportToExcel?createUserName="+createUserName+'&deptName='+deptName+'&category='+category);//定义action

        $("body").append(form);
        form.submit();
    };


    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        threeMainTable.search();
    });
    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        threeMainTable.openAddPage();
    });
    // 导出按钮点击事件
    $('#btnOut').click(function () {
        threeMainTable.exportToExcel();
    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(threeMainTable.tableId);
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
                url: Feng.ctxPath + '/landInfo/delete',
                type: "POST",
                dataType:"json",
                data:{ids:ids},
                success: function (res) {
                    if(res.code == "500"){
                        Feng.error(res.message);
                    }else{
                        Feng.success("删除成功！");
                        table.reload(threeMainTable.tableId);
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
    table.on('tool(' + threeMainTable.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            threeMainTable.jumpEditPage(data);
        }else if (layEvent === 'detail') {
            threeMainTable.jumpDetailPage(data);
        }
    });

    //执行实例
    var uploadInst = upload.render({
        elem: '#btnImp'
        , url: '/landdetail/uploadExcel'
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

