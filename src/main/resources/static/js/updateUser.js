layui.use(['layer', 'form'], function() {
    var layer = layui.layer;
    var form = layui.form;

    form.verify({
        username: function(value, item) {
            if (value.length < 2) {
                return '姓名长度不能少于两个字符';
            }if (!/^[\u4e00-\u9fa5a-zA-Z]+$/.test(value)) {
                return '姓名只能包含中文或英文字母';
            }
        },password: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(value.length < 4){
                return '密码不能小于4啊';
            }
        },phone: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(!/^1[3456789]\d{9}$/.test(value)){
                return '请输入正确的手机号码';
            }
        },email: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(!/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(value)){
                return '请输入正确的邮箱格式';
            }
        }

    });


    // 绑定按钮单击事件，弹出表单
    $('button[lay-event="edit"]').click(function() {

        // 从按钮的data属性中获取乘客的信息
        var userId = $(this).data('id');
        var username = $(this).data('username');
        var password = $(this).data('password');
        var phone = $(this).data('phone');
        var email = $(this).data('email');
        var pageNo = parseInt($('#pageNo').val());
        console.log("pageNo" + pageNo);
        console.log(userId);
        console.log(username);
        console.log(password);
        console.log(phone);
        console.log(email);
        var editModal = layer.open({
            type: 1,
            title: '编辑用户信息',
            area: ['420px', 'auto'],
            content:  '<form class="layui-form" action="/ticket/user/update">'+
                '<input type="hidden" name="userId" value="' + userId + '">' +
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">用户名</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="username" required lay-verify="required|username" placeholder="请输入用户名" autocomplete="off" class="layui-input" value="' + username + '">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">密码</label>' +
                '<div class="layui-input-block">' +
                '<input type="password" name="password" required lay-verify="required|password" placeholder="请输入密码" autocomplete="off" class="layui-input" value="' + password + '">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">电话</label>' +
                '<div class="layui-input-block">' +
                '<input type="tel" name="phone" required lay-verify="required|phone" placeholder="请输入电话号码" autocomplete="off" class="layui-input" value="' + phone + '">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">电子邮箱</label>' +
                '<div class="layui-input-block">' +
                '<input type="email" name="email" placeholder="请输入电子邮箱" lay-verify="required|email" autocomplete="off" class="layui-input" value="' + email + '">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<div class="layui-input-block">' +
                '<button class="layui-btn" lay-submit lay-filter="formEdit">提交</button>' +
                '<button type="reset" class="layui-btn layui-btn-primary">重置</button>'  +
                '</div>' +
                '</div>'+
                '</form>'
        });

        // 监听表单提交事件
        form.on('submit(formEdit)', function(data){
            console.log("修改后的数据" + data.field);
            // 提交表单
            $.ajax({
                url: 'http://localhost:8080/ticket/user/update',
                type: 'POST',
                data: data.field,
                success: function (response) {
                    if(response.success) {
                        layer.msg(response.msg, {icon: 1}, function () {
                            // 关闭弹出层
                            // layer.close(editModal);
                            window.location.href = "http://localhost:8080/ticket/user/findAllUsers?pageNo=" + pageNo;
                        });
                    } else {
                        layer.msg(response.msg, {icon: 2,time : 2000});
                    }
                },
                error: function(xhr, textStatus, errorThrown) {
                    layer.msg('修改用户信息失败：' + xhr.responseText);
                }
            });
            return false;
        });
    });
});