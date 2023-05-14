
layui.use(['layer', 'form','laydate'], function() {
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
        elem: '#date-input',//指定元素
        type: 'datetime', // 设置时间类型为 datetime，可选值有：year/month/date/time/datetime
        trigger: 'click',
        done: function(value, date){ // 选择完日期后的回调函数
            console.log('选中的时间是：' + value);
        }
    });

    // 绑定出发时间输入框的点击事件
   /* $('#departureTime').click(function(){
        laydate.render({
            elem: this, //指定元素
            type: 'datetime', // 设置时间类型为 datetime，可选值有：year/month/date/time/datetime
            done: function(value, date){ // 选择完日期后的回调函数
                console.log('选中的时间是：' + value);
            }
        }).show();
    });*/

    form.verify({
        title: function(value, item) {
            if (value.length < 1) {
                return '标题不能为空!';
            }
        },
        content: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(value.length < 1){
                return '内容不能为空!';
            }
        }

    });



    // 绑定按钮单击事件，弹出表单
    $('button[lay-event="add"]').click(function() {

        var routeList = $('button[lay-event="add"]').attr('data-routeList');
        var pageTotal = parseInt($('#pageTotal').val());
        console.log("routeList",routeList);
        console.log(pageTotal);

        var addModal = layer.open({
            type: 1,
            title: '添加车次信息',
            area: ['420px', 'auto'],
            content:  '<form class="layui-form" action="/ticket/admin/addSchedule">'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">路线</label>' +
                '<div class="layui-input-block ">' +
                '<select name="routeId" required lay-verify="required" lay-search>' +
                '<option value="">请选择路线</option>' +
                /* 遍历路线列表 */
                /* 选项的值为路线ID，文本为起点站和终点站的组合 */
                /* 例如：北京 - 上海 */
                /* 如果数据中有其他属性可以根据需要添加到option中 */
                /* 例如：routeList[i].departure + ' - ' + routeList[i].destination */
                /* 注意：这里的routeList是JS变量，不需要使用th:each */
                '<option th:each="route : ${routeList}" th:value="${route.routeId}" th:text="${route.departure} + \' - \' + ${route.destination}"></option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">出发时间</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="departureTime" id="date-input"   required lay-verify="required" placeholder="yyyy-MM-dd HH:mm:ss" autocomplete="off" class="layui-input">' +
                '</div>' +
                '</div>' +
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">行驶时间</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="duration" required lay-verify="required" placeholder="请输入行驶时间如：3.5h" autocomplete="off" class="layui-input">' +
                '</div>' +
                '</div>' +
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">车牌号码</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="busNumber" required lay-verify="required" placeholder="请输入车牌号码如：粤A12345" autocomplete="off" class="layui-input">' +
                '</div>' +
                '</div>' +
                '<div class="layui-form-item">' +
                '<div class="layui-input-block">' +
                '<button class="layui-btn" lay-submit lay-filter="formAdd">提交</button>' +
                '<button type="reset" class="layui-btn layui-btn-primary">重置</button>' +
                '</div>' +
                '</div>'+
                '</form>'
        });

        // 监听表单提交事件
        form.on('submit(formAdd)', function(data){
            console.log("修改后的数据" + data.field);
            // 提交表单
            $.ajax({
                url: 'http://localhost:8080/ticket/admin/addSchedule',
                type: 'POST',
                data: data.field,
                success: function (response) {
                    if(response.success) {
                        layer.msg(response.msg, {icon: 1}, function () {
                            // 关闭弹出层
                            // layer.close(editModal);
                            window.location.href = "http://localhost:8080/ticket/news/findAllNews?pageTotal=" + pageTotal;
                        });
                    } else {
                        layer.msg(response.msg, {icon: 2,time : 2000});
                    }
                },
                error: function(xhr, textStatus, errorThrown) {
                    layer.msg('增加用户信息失败：' + xhr.responseText);
                }
            });
            return false;
        });
    });
});