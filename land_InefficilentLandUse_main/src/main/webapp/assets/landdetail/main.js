layui.config({
}).extend({
    tableMerge: 'tableMerge'
});
layui.use(['table', 'admin','laydate','tableMerge', 'ax', 'func','upload'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var tableMerge = layui.tableMerge;
    var tips;


    /**
     * 文件发送管理
     */
    var detailMainTable = {
        tableId: "detailMainTable"
    };

    /**
     * 初始化表格的列
     */
    detailMainTable.initColumn = function () {
        return [[
            {fixed: 'left',type: 'checkbox'},
            {field: 'id', hide: true,align: 'center',fixed: 'left', title: 'ID'},
            {field: 'landCode', hide: true,align: 'center',fixed: 'left', title: '编码'},
            {field: 'xmc', sort: false,align: 'center',merge:true, fixed: 'left',title: '县名称'},
     /*       {field: 'pqbh', sort: false,align: 'center',merge:true,fixed: 'left', title: '片区编号'},*/
            {field: 'xmmc', sort: false,align: 'center',merge:true,fixed: 'left', title: '项目名称',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showRec">' + d.xmmc+ '</a></div>';
                    return html;
                }
                },
          /*  {field: 'xzdm', sort: false,align: 'center', title: '乡镇代码'},*/
          /*  {field: 'xzmc', sort: false,align: 'center',merge:true, fixed: 'left',title: '乡镇名称'},*/
            {field: 'dkbh', sort: false,align: 'center', title: '地块编号',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showMap">' + d.dkbh+ '</a></div>';
                    return html;
                }
                },
            {field: 'category', sort: false,merge:true,align: 'center', title: '地块类型'},
            {field: 'dkmj', sort: false,align: 'center', title: '地块面积'},
        /*    {field: 'dldm', sort: false,align: 'center', title: '大类代码'},*/
            /*{field: 'dlmc', sort: false,merge:true,align: 'center', title: '大类名称'},*/
    /*        {field: 'xldm', sort: false, align: 'center',title: '小类代码'},*/
            {field: 'xlmc',width:250, sort: false,merge:true, align: 'center',title: '小类名称',
                templet:function (d){
                    var html = '<div><a rel="nofollow"  href="javascript:void(0);" >('+d.dlmc+')' + d.xlmc+ '</a></div>';
                    return html;
                }
            },
            {field: 'xzyt', sort: false,merge:true, align: 'center',title: '现状用途'},
            {field: 'ghyt',width:180, sort: false, align: 'center',title: '规划用途'},
            {field: 'landStatus', sort: false,merge:true,align: 'center', title: '再开发模式'},
            {field: 'zkfsx', width:180,sort: false, align: 'center',title: '再开发时序'},
            {field: 'sfss', sort: false, align: 'center',title: '实施状态'},
            {field: 'wssyy', sort: false, align: 'center',title: '未实施原因'},
            {field: 'zzss',hide:true,width:150, sort: false, align: 'center',title: '是否正在实施'},
            {field: 'kssj', sort: false, align: 'center',title: '开始时间'},
            {field: 'ywc', hide:true,sort: false, align: 'center',title: '是否已完成'},
            {field: 'wcsj', sort: false, align: 'center',title: '完成时间'},
            {field: 'remark', sort: false, align: 'center',title: '备注'},
/*            {field: 'xzrjl', sort: false, align: 'center',title: '现状容积率'},
            {field: 'xzjzmd', sort: false, align: 'center',title: '现状建筑密度'},
            {field: 'kfql', sort: false, align: 'center',title: '开发潜力'},
            {field: 'zkfsx', sort: false, align: 'center',title: '再开发时序'},
            {field: 'pqmj', sort: false, align: 'center',title: '片区面积'},*/
            {field: 'createUserName',hide: true, sort: false,align: 'center', title: '创建人名字'},
            {field: 'createTime',hide: true, sort: false,align: 'center', title: '创建时间'},
            {field: 'updateUserName',hide: true, sort: false,align: 'center', title: '修改人名字'},
            {field: 'updateTime',hide: true, sort: false,align: 'center', title: '修改时间'}
            /*,
            {align: 'center', toolbar: '#tableBar', title: '操作'}*/
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        id: detailMainTable.tableId,
        elem: '#' + detailMainTable.tableId,
        url: Feng.ctxPath + '/landdetail/selectList?category='+$('#category').val(),
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: detailMainTable.initColumn(),
        done:function (){
            tableMerge.render(this);
            $("[data-field = 'category']").children().each(function(){
                if($(this).text() == 'towns'){
                    $(this).text("低效城镇");
                }else if($(this).text() == 'industries'){
                    $(this).text("低效产业");
                }else if($(this).text() == 'villages'){
                    $(this).text("低效村庄");
                }
            });
            $("[data-field = 'landStatus']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("收储再开发");
                    $(this).css("color","green");
                }else if($(this).text() == '2'){
                    $(this).text("自主开发");
                    $(this).css("color","green");
                }else if($(this).text() == '3'){
                    $(this).text("技术提升");
                    $(this).css("color","green");
                }else if($(this).text() == '4'){
                    $(this).text("复垦耕地");
                    $(this).css("color","green");
                }else if($(this).text() == '5'){
                    $(this).text("司法处置或转让");
                    $(this).css("color","green");
                }else if($(this).text() == ''){
                    $(this).text("待处置");
                    $(this).css("color","red");
                }
            });
            $("[data-field = 'sfss']").children().each(function(){
                if($(this).text() == '0'){
                    $(this).text("未实施");
                }else if($(this).text() == '1'){
                    $(this).text("正在实施");
                }else if($(this).text() == '2'){
                    $(this).text("实施已完成");
                }
            });
            $("[data-field = 'wssyy']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("未到计划的再开发时序");
                }else if($(this).text() == '2'){
                    $(this).text("企业原因导致无法实施");
                }else if($(this).text() == '3'){
                    $(this).text("政府原因导致无法实施");
                }
            });
            /*$("[data-field = 'zzss']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("是");
                }else if($(this).text() == '0'){
                    $(this).text("否");
                }
            });
            $("[data-field = 'ywc']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("是");
                }else if($(this).text() == '0'){
                    $(this).text("否");
                }
            });*/
            //autoFixedHeight(this.elem[0]);
        }
    });

    //固定行随单元格自动调整
    function autoFixedHeight(tableElem) {
        // 获取表格div
        var $tableView = $(tableElem).next(".layui-table-view");
        // 获取两侧浮动栏
        var $fixed = $tableView.find(".layui-table-fixed");
        var dataIndex;
        var trHeight;
        // 遍历tr 修正浮动栏行高
        $tableView.find(".layui-table-main").find("tr").each(function () {
            dataIndex = $(this).attr("data-index");
            trHeight = $(this).css("height");
            $fixed.find("tr[data-index=" + dataIndex + "]").css("height", trHeight);
        });
    }

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });
    table.on('tool(detailMainTable)', function (obj) {
        var data = obj.data;//获取监听点击当前行的所有信息[object，object]
        var layEvent = obj.event;
        if (layEvent === 'showRec') {
            func.open({
                height: 1000,
                title: '详情',
                content: Feng.ctxPath + '/landdetail/detail?id=' + data.id,
                tableId: detailMainTable.tableId,
                endCallback: function () {
                    //table.reload(shareFileTInfo.tableId);
                }
            });
        }else if(layEvent === 'showMap'){
            window.open(Feng.ctxPath+"/landdetail/showOnMap?value="+data.dkbh+"&key=DKBH&xmmc="+data.xmmc);
        }
    });
    /*// 工具条点击事件
    table.on('tool(' + detailMainTable.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            detailMainTable.jumpEditPage(data);
        }else if (layEvent === 'detail') {
            detailMainTable.jumpDetailPage(data);
        }else if(layEvent === 'location'){
            detailMainTable.jumpLocationPage(data);
        }
    });*/

    /**
     * 点击查询按钮
     */
    detailMainTable.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $('#timeLimit').val();
        queryData['xmmc'] = $('#xmmc').val();
        queryData['landStatus'] = $('#landStatus').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        if(value != undefined){
            queryData['xdm'] = value;
        }else{
            queryData['xdm'] = "";
        }
        var landType = $('select[name="landType"]').next().find('.layui-this').attr('lay-value');
        if(landType != undefined){
            queryData['landType'] = landType;
        }else{
            queryData['landType'] = "";
        }

        table.reload(detailMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    detailMainTable.jumpDetailPage = function (data) {
        func.open({
            height: 1000,
            title: '详情',
            content: Feng.ctxPath + '/landdetail/detail?id=' + data.id,
            tableId: detailMainTable.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };
    detailMainTable.jumpLocationPage = function (data) {
        window.open(Feng.ctxPath+"/landdetail/showOnMap?value="+data.dkbh+"&key=DKBH&xmmc="+data.xmmc);
    };
    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    detailMainTable.jumpEditPage = function (data) {
        func.open({
            height: 1000,
            title: '编辑',
            content: Feng.ctxPath + '/landdetail/edit?id=' + data.id,
            tableId: detailMainTable.tableId,
            endCallback: function () {
                //table.reload(shareFileTInfo.tableId);
            }
        });
    };
    /**
     * 弹出添加
     */
    detailMainTable.openAddPage = function () {
        func.open({
            height: 1000,
            title: '新增低效用地项目',
            content: Feng.ctxPath + '/landdetail/add',
            tableId: detailMainTable.tableId,
            endCallback: function () {
                table.reload(detailMainTable.tableId);
            }
        });
        //window.location.href = Feng.ctxPath + '/landInfo/add?category=1'
    };
    /**
     * 导出按钮
     */
    detailMainTable.exportToExcel = function () {
        var createUserName = $('#createUserName').val();
        var deptName = $('#deptName').val();
        var category = $('#category').val();
        var limitTime = $('#timeLimit').val();
        var form=$("<form>");
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");//提交方式为post
        form.attr("action",Feng.ctxPath + "/landdetail/exportToExcel?createUserName="+createUserName+'&deptName='+deptName+'&category='+category+'&limitTime='+limitTime);//定义action
        $("body").append(form);
        form.submit();
    };
    // 导出按钮点击事件
    $('#btn111').click(function () {
        detailMainTable.exportToExcel();
    });


    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        detailMainTable.search();
    });
    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        detailMainTable.openAddPage();
    });
    // 导出按钮点击事件
    $('#btnOut').click(function () {
        detailMainTable.exportToExcel();
    });
    //btnEdt 编辑按钮点击事件
    $('#btnEdt').click(function () {
        var checkStatus = table.checkStatus(detailMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        debugger;
        detailMainTable.jumpEditPage(checkStatus.data[0]);
    });
    //btnDet 详情按钮点击事件
    $('#btnDet').click(function () {
        var checkStatus = table.checkStatus(detailMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        detailMainTable.jumpDetailPage(checkStatus.data[0]);
    });
    //btnDet 详情按钮点击事件
    $('#btnMap').click(function () {
        var checkStatus = table.checkStatus(detailMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        detailMainTable.jumpLocationPage(checkStatus.data[0]);
    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(detailMainTable.tableId);
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
                url: Feng.ctxPath + '/landdetail/delete',
                type: "POST",
                dataType:"json",
                data:{ids:ids},
                success: function (res) {
                    if(res.code == "500"){
                        Feng.error(res.message);
                    }else{
                        Feng.success("删除成功！");
                        table.reload(detailMainTable.tableId);
                    }
                },
                error: function (err) {
                    console.log('err', err);
                }
            });
            layer.close(index);
        });


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

