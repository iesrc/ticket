layui.use(['layer', 'form'], function() {
    var layer = layui.layer;
    var form = layui.form;

    form.verify({
        departure: function(value) {
            if (value && !/^[\u4E00-\u9FA5]{2,}$/.test(value)) {
                return '出发地必须为不少于两个字符的中文';
            }
        },
        destination: function(value, item) {
            if (value && !/^[\u4E00-\u9FA5]{2,}$/.test(value)) {
                return '目的地必须为不少于两个字符的中文';
            }
            if (value === layui.$('#departure').val()) {
                return '目的地不能与出发地相同';
            }
        }
    });



    $('button[lay-event="edit"]').click(function() {

        var routeId = $(this).data('id');
        var departure = $(this).data('departure');
        var destination = $(this).data('destination');
        var pageNo = parseInt($('#pageNo').val());

        console.log(routeId);
        console.log(departure);
        console.log(destination);
        console.log(pageNo);

        var editModal = layer.open({
            type: 1,
            title: '更新路线信息',
            area: ['420px', 'auto'],
            content: '<form class="layui-form" action="/ticket/route/update">'+
                '<input type="hidden" name="routeId" value="' + routeId + '">' +
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">出发地</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="departure" id="departure" required lay-verify="departure" placeholder="请输入出发地" autocomplete="off" class="layui-input" value="'+departure+'">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">目的地</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="destination" required lay-verify="destination" placeholder="请输入目的地" autocomplete="off" class="layui-input" value="'+destination+'">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<div class="layui-input-block">' +
                '<button class="layui-btn" lay-submit lay-filter="formAdd">提交</button>' +
                '<button type="reset" class="layui-btn layui-btn-primary">刷新</button>'  +
                '</div>' +
                '</div>'+
                '</form>',
        });

        // 监听表单提交事件
        form.on('submit(formAdd)', function(data){
            // 提交表单
            $.ajax({
                url: 'http://localhost:8080/ticket/route/update',
                type: 'POST',
                data: data.field,
                success: function (response) {
                    if(response.success) {
                        layer.msg(response.msg, {icon: 1}, function () {
                            // 关闭弹出层
                            // layer.close(editModal);
                            window.location.href = "http://localhost:8080/ticket/route/findAllRoutes?pageNo=" + pageNo;
                        });
                    } else {
                        layer.msg(response.msg, {icon: 2,time : 2000});
                    }
                },
                error: function(xhr, textStatus, errorThrown) {
                    layer.msg('增加路线信息失败：' + xhr.responseText);
                }
            });
            return false;
        });
    });
});