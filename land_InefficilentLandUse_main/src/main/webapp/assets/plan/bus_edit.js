
layui.use(['table','layer', 'form', 'admin', 'laydate', 'ax', 'formSelects'], function () {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var $ax = layui.ax;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;
    var formSelects = layui.formSelects;
    var isFirst = 0;

    /**
     * 文件发送管理
     */
    var addLandTable = {
        tableId: "addLandTable"
    };

    /**
     * 初始化表格的列
     */
    addLandTable.initColumn = function () {
        return [[
            {fixed: 'left',type: 'checkbox'},
            {field: 'id', hide: true,align: 'center',title: 'ID'},
            {field: 'landCode', hide: true, align: 'center', title: '编码'},
            {field: 'xmc', sort: false,align: 'center', title: '县名称'},
            {field: 'pqbh', hide: true, sort: false,align: 'center', title: '片区编号'},
            {field: 'xmmc', sort: false,align: 'center', title: '项目名称'},
            {field: 'dkbh', sort: false,align: 'center', title: '地块编号'},
            {field: 'dkmj', sort: false,align: 'center', title: '地块面积'},
            {field: 'dlmc', sort: false,align: 'center', title: '大类名称'},
            {field: 'xlmc', sort: false, align: 'center',title: '小类名称'},
            {field: 'xzyt', sort: false, align: 'center',title: '现状用途'},
            {field: 'ghyt', hide: true, sort: false, align: 'center',title: '规划用途'},
            {field: 'remark', sort: false, align: 'center',title: '备注'},
            {field: 'createUserName', hide: true, sort: false,align: 'center', title: '创建人名字'},
            {field: 'createTime',  hide: true,sort: false,align: 'center', title: '创建时间'},
            {field: 'updateUserName',  hide: true,sort: false,align: 'center', title: '修改人名字'},
            {field: 'updateTime',  hide: true,sort: false,align: 'center', title: '修改时间'}

        ]];
    };
    //渲染时间选择框
    laydate.render({
        elem: '#planStartTime',
        range: false,
        min: Feng.currentDate()
    });
    var landCode = $("#landCode").val();
    // 渲染表格
    table.render({
        id: addLandTable.tableId,
        elem: '#' + addLandTable.tableId,
        //url:Feng.ctxPath + '/landdetail/selectList?landCode='+landCode,
        cellMinWidth: 100,
        cols: addLandTable.initColumn(),
        autoSort:false,
        done: function (res, curr, count) {
            // 设置所有的checkbox为选中状态
            //$('[name="layTableCheckbox"][lay-filter="layTableAllChoose"]').prop('checked', true);
            // 同步更新视图
            //form.render('checkbox');
            // 如果需要单独设置某些行的checkbox为选中状态，可以在done回调中通过行数据进行判断
            // 例如，假设返回的数据中有一个字段isSelected表示是否选中
              res.data.forEach(function(item){
                if(item.landCode == landCode && isFirst == 0){ // 根据业务逻辑判断
                  var index = res.data.indexOf(item); // 获取行索引
                  //$('[name="layTableCheckbox"]').eq(index).prop('checked', true);
                    $('tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
                    $('tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
                 /* $(".layui-form-checkbox").eq(index).addClass("layui-form-checked");*/
                }
              });
              form.render('checkbox');
            /*console.log('res=',res)
            res.data.forEach(function (item, index) {
                $('tbody').find('tr[data-index="' + index + '"]').addClass('layui-table-click');
            });
            var rows = $('#data').next().find('.layui-table-box tbody tr')
            if (rows.length > 0) {
                console.log("找到了第1行",rows[0] );

                // 如果想高亮显示该行，可以添加CSS样式或其他操作
                $(rows[0]).addClass('ayui-table-click');
                /!*var data = res.data[0];
                // 行点击事件
                rowClick(data);*!/
            } else {
                console.log("未找到指定行");
            }*/
        }
    });


    var loadDefautData = function (){
        table.reload(addLandTable.tableId, {url:Feng.ctxPath + '/landdetail/selectList?landCode='+landCode,
            page: {curr: 1}
        });
        /*debugger;
        if(isFirst == 0){
            console.log(1111);
            table.reload(addLandTable.tableId, {url:Feng.ctxPath + '/landdetail/selectList?landCode='+landCode,
                page: {curr: 1}
            });
        }else{
            console.log(1111);
            table.reload(addLandTable.tableId, {url:Feng.ctxPath + '/landdetail/selectList',
                page: {curr: 1}
            });
        }*/
    }
    loadDefautData();

    var setDefaultValue = function (){
        var reasonsType = $("#reasonsTypeV").val();
        formSelects.value("reasonsTypeV", reasonsType);
        if(reasonsType != null && reasonsType != ""){
            if("1" === reasonsType){
                $("#reasonsType").append('<option value="1" selected="selected">困难原因1</option>');
                $("#reasonsType").append('<option value="2">困难原因2</option>');
                $("#reasonsType").append('<option value="3">困难原因3</option>');
                $("#reasonsType").append('<option value="4">困难原因4</option>');
                $("#reasonsType").append('<option value="5">其他</option>');
            }else if ("2" === reasonsType){
                $("#reasonsType").append('<option value="1">困难原因1</option>');
                $("#reasonsType").append('<option value="2" selected="selected">困难原因2</option>');
                $("#reasonsType").append('<option value="3">困难原因3</option>');
                $("#reasonsType").append('<option value="4">困难原因4</option>');
                $("#reasonsType").append('<option value="5">其他</option>');
            }else if ("3" === reasonsType){
                $("#reasonsType").append('<option value="1">困难原因1</option>');
                $("#reasonsType").append('<option value="2">困难原因2</option>');
                $("#reasonsType").append('<option value="3" selected="selected">困难原因3</option>');
                $("#reasonsType").append('<option value="4">困难原因4</option>');
                $("#reasonsType").append('<option value="5">其他</option>');
            }else if ("4" === reasonsType){
                $("#reasonsType").append('<option value="1">困难原因1</option>');
                $("#reasonsType").append('<option value="2">困难原因2</option>');
                $("#reasonsType").append('<option value="3">困难原因3</option>');
                $("#reasonsType").append('<option value="4" selected="selected">困难原因4</option>');
                $("#reasonsType").append('<option value="5">其他</option>');
            }else if ("5" === reasonsType){
                $("#reasonsType").append('<option value="1">困难原因1</option>');
                $("#reasonsType").append('<option value="2">困难原因2</option>');
                $("#reasonsType").append('<option value="3">困难原因3</option>');
                $("#reasonsType").append('<option value="4" >困难原因4</option>');
                $("#reasonsType").append('<option value="5" selected="selected">其他</option>');
            }else{
                $("#reasonsType").append('<option value="1">困难原因1</option>');
                $("#reasonsType").append('<option value="2">困难原因2</option>');
                $("#reasonsType").append('<option value="3">困难原因3</option>');
                $("#reasonsType").append('<option value="4">困难原因4</option>');
                $("#reasonsType").append('<option value="5">其他</option>');
            }
        }else{
            $("#reasonsType").append('<option value="1">困难原因1</option>');
            $("#reasonsType").append('<option value="2">困难原因2</option>');
            $("#reasonsType").append('<option value="3">困难原因3</option>');
            $("#reasonsType").append('<option value="4">困难原因4</option>');
            $("#reasonsType").append('<option value="5">其他</option>');
        }
        form.render();
    }
    setDefaultValue();
    /**
     * 点击查询按钮
     */
    addLandTable.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $('#timeLimit').val();
        queryData['landCode'] = '';
        queryData['xmmc'] = $('#xmmc').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        var landStatus = $('select[name="landStatus"]').next().find('.layui-this').attr('lay-value');
        if(value != undefined){
            queryData['xdm'] = value;
        }else{
            queryData['xdm'] = "";
        }
        if(landStatus != undefined){
            queryData['landStatus'] = landStatus;
        }else{
            queryData['landStatus'] = "";
        }
        isFirst = 1;
        table.reload(addLandTable.tableId, {url:'/landdetail/selectList',
            where: queryData, page: {curr: 1}
        });
        $("#landCode").val("");
    };
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        addLandTable.search();
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var landCode = $("#landCode").val();
        if(landCode == ""){
            var checkStatus = table.checkStatus(addLandTable.tableId);
            var ids = [];
            $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
                ids.push(o.landCode);
            });
            if (ids.length != 1) {
                layer.msg('请选择一条数据');
                return false;
            }
            //landCode = ids[0];
            $("#landCode").val(ids[0]);
        }
        var ajax = new $ax(Feng.ctxPath + "/plan/savePlan", function (data) {
            Feng.success("保存成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("保存失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
        //添加 return false 可成功跳转页面
        return false;
    });
});

