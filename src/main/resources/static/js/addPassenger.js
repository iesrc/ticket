layui.use(['layer', 'form'], function() {
    var layer = layui.layer;
    var form = layui.form;

    form.verify({
        nameLength: function(value) {
            if (value.length < 2) {
                return '姓名至少得2个字符啊';
            }
            if (!/^[\u4e00-\u9fa5a-zA-Z]+$/.test(value)) {
                return '姓名只能包含中文或英文字母';
            }
        }//其中，正则表达式 /^[\u4e00-\u9fa5a-zA-Z]+$/ 表示匹配字符串必须由中文或英文字母组成，
        // 并且最少有一个字符。其中 [\u4e00-\u9fa5] 匹配中文字符，[a-zA-Z] 匹配英文字母
        ,identity: function(value, item) {
            // if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(value)) {
            if (value.length < 18) {
                return '身份证号格式不正确';
            }if (!/^\d+$/.test(value)) {
                return '身份证号码须为数字';
            }
        }
    });


    $('button[lay-event="add"]').click(function() {

        var passengerId = $(this).data('id');
        console.log(passengerId);

        var editModal = layer.open({
            type: 1,
            title: '增加乘客信息',
            area: ['420px', 'auto'],
            content: '<form class="layui-form" action="/ticket/passenger/doAddPassenger">'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">姓名</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="name" required lay-verify="required|nameLength" placeholder="请输入姓名" autocomplete="off" class="layui-input">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">身份证号</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="identity" required lay-verify="required|identity" placeholder="请输入身份证号码" autocomplete="off" class="layui-input" >' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">性别</label>' +
                '<div class="layui-input-block">' +
                '<input type="radio" name="gender" value="男" title="男" class="layui-input" checked>' +
                '<input type="radio" name="gender" value="女" title="女" class="layui-input" >' +
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
                url: 'http://localhost:8080/ticket/passenger/doAddPassenger',
                type: 'POST',
                data: data.field,
                dataType: 'json', // 指定响应数据类型为 JSON
                success: function (response) {
                    // 处理响应数据
                    if (response.success) {
                        // 弹出成功提示信息
                        layer.msg(response.msg, {icon: 1}, function () {
                            // 关闭弹出层
                            window.location.href = "http://localhost:8080/ticket/passenger/findPassenger";
                        });
                    } else {
                        // 弹出失败提示信息
                        layer.msg(response.msg, {icon: 2, time: 2000});
                    }
                },
                error: function(xhr, status, error) {
                    // 显示错误消息
                    layer.msg('添加失败：' + error, {icon: 2, time: 1000});
                }
            });
            return false;
        });
    });
});