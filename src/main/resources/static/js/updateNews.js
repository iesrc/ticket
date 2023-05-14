layui.use(['layer', 'form'], function() {
    var layer = layui.layer;
    var form = layui.form;

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
    $('button[lay-event="edit"]').click(function() {

        var id = $(this).data('id');
        var title = $(this).data('title');
        var content = $(this).data('content');
        var pageNo = parseInt($('#pageNo').val());

        var pageTotal = parseInt($('#pageTotal').val());
        console.log(content);

        var addModal = layer.open({
            type: 1,
            title: '修改公告信息',
            area: ['420px', 'auto'],
            content:  '<form class="layui-form" action="/ticket/news/update">'+
                '<input type="hidden" name="id" value="' + id + '">' +
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">标题</label>' +
                '<div class="layui-input-block ">' +
                '<input  type="text" name="title" value="' + title + '"required lay-verify="required|title" placeholder="请输入标题" autocomplete="off" class="layui-input" >' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">内容</label>' +
                '<div class="layui-input-block " >' +
                '<textarea  name="content"  rows="10" cols="25" required lay-verify="required|content" placeholder="请输入内容" autocomplete="off" class="layui-textarea" style="height: 150px;" >' + content + '</textarea>' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<div class="layui-input-block">' +
                '<button class="layui-btn" lay-submit lay-filter="formAdd">提交</button>' +
                '<button type="reset" class="layui-btn layui-btn-primary">重置</button>'  +
                '</div>' +
                '</div>'+
                '</form>'
        });

        // 监听表单提交事件
        form.on('submit(formAdd)', function(data){
            console.log("修改后的数据" + data.field);
            // 提交表单
            $.ajax({
                url: 'http://localhost:8080/ticket/news/update',
                type: 'POST',
                data: data.field,
                success: function (response) {
                    if(response.success) {
                        layer.msg(response.msg, {icon: 1}, function () {
                            // 关闭弹出层
                            // layer.close(editModal);
                            window.location.href = "http://localhost:8080/ticket/news/findAllNews?pageNo=" + pageNo;
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