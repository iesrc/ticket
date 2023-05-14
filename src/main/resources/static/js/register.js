
// 注册函数
function register() {
    $.ajax({
        url: '/doRegister',
        type: 'POST',
        data: $('#register-form').serialize(),
        success: function(data) {

            // console.log($('#register-success-modal').html());
            if (data.success) {

                // 关闭已经打开的所有弹出层
                // layer.closeAll();

                // 注册成功，显示模态框
               /* layui.use('layer', function() {
                    var layer = layui.layer;
                    layer.open({
                        type: 1,
                        title: false,
                        closeBtn: false,
                        shadeClose: true,
                        area: ['300px', '200px'],
                        content: '<div style="text-align:center;padding-top:60px;"><p>恭喜您，注册成功！</p><button class="layui-btn  layui-btn-warm" onclick="window.location.href=\'/login\';">确定</button></div>',
                        end: function() {
                            window.location.href = '/login';
                        }
                    });
                });*/
                layer.alert("注册成功",{icon:1});
            } else {
                // 注册失败，显示错误信息
                layer.msg('注册失败：' + data.msg);
            }
        },
        error: function(xhr, textStatus, errorThrown) {
            // 注册失败，显示错误信息
            layer.msg('注册失败：' + xhr.responseText);
        }
    });



}
