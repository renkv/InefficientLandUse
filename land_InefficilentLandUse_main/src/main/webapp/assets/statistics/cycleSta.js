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
            {field: 'xmc', sort: false,align: 'center', title: '县名称'},
            {field: 'year', sort: false,align: 'center', title: '年'},
            {field: 'landStatus', sort: false,align: 'center', title: '处置类型'},
            {field: 'dlmc', sort: false,align: 'center', title: '大类名称'},
            {field: 'xlmc', sort: false,align: 'center',title: '小类名称'},
            {field: 'xzyt', sort: false, align: 'center',title: '现状用途'},
            {field: 'landNum', sort: false, align: 'center',title: '数量'},
            {field: 'landArea', sort: false, align: 'center',title: '面积'},
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        id: detailMainTable.tableId,
        elem: '#' + detailMainTable.tableId,
        url: Feng.ctxPath + '/statistics/cycleStaList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: detailMainTable.initColumn(),
        done:function (){
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
    detailMainTable.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $('#timeLimit').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        if(value != undefined){
            queryData['xdm'] = value;
        }else{
            queryData['xdm'] = "";
        }
        var landType = $('select[name="landStatus"]').next().find('.layui-this').attr('lay-value');
        if(landType != undefined){
            queryData['landStatus'] = landType;
        }else{
            queryData['landStatus'] = "";
        }

        table.reload(detailMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
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
    $('#btnOut').click(function () {
        detailMainTable.exportToExcel();
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        detailMainTable.search();
    });






});

