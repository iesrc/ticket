layui.use(['table', 'form', 'layer'], function() {
    var table = layui.table;
    var form = layui.form;
    var layer = layui.layer;
    // 监听模态框
    table.on('tool(passengerTable)', function (obj) {
        var data = obj.data;
        if (obj.event === 'edit') {
            // 弹出编辑模态框
            layer.open({
                type: 1,
                content: $('#editPassengerModal'),
                area: ['400px', '300px'],
                success: function (layero, index) {
                    // 给表单赋值
                    form.val('editPassengerForm', {
                        'name': data.name,
                        'identity': data.identity,
                        'gender': data.gender
                    });
                }
            });
            // 监听保存按钮
            form.on('submit(editPassenger)', function (data) {
                // 向后台发送请求保存数据
                $.ajax({
                    url: '/passenger/edit',
                    type: 'POST',
                    data: {
                        'passengerId': data.passengerId,
                        'name': data.field.name,
                        'identity': data.field.identity,
                        'gender': data.field.gender
                    },
                    success: function (res) {
                        if (res.success) {
                            layer.msg(res.msg, {icon: 1, time: 2000}, function () {
                                // 刷新表格数据
                                table.reload('passengerTable');
                                // 关闭模态框
                                layer.closeAll();
                            });
                        } else {
                            layer.msg(res.msg, {icon: 5, time: 2000});
                        }
                    }
                });
                return false;
            });
        }
    });
});