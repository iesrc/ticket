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

    // 定义 resetForm 方法
    /*var resetForm = function () {
        // 清空单选框的选中状态
        $('input[type=radio][name=gender]').prop('checked', false);
        // 重置表单
        $('form.layui-form input[name=name]').val('');
        $('form.layui-form input[name=identity]').val('');
    };*/
    // 绑定按钮单击事件，弹出表单
    $('button[lay-event="edit"]').click(function() {

        // 从按钮的data属性中获取乘客的信息
        /*var passengerId = $(this).attr('th:data-id');
        var passengerName = $(this).attr('th:data-name');
        var passengerIdentity = $(this).attr('th:data-identity');
        var passengerGender = $(this).attr('th:data-gender');

*/          var passengerId = $(this).data('id');
        var passengerName = $(this).data('name');
        var passengerIdentity = $(this).data('identity');
        var passengerGender = $(this).data('gender');
        var pageNo = parseInt($('#pageNo').val());
        console.log("pageNo" + pageNo);
        console.log(passengerId);
        console.log(passengerName);
        console.log(passengerIdentity);
        console.log(passengerGender);
        var editModal = layer.open({
            type: 1,
            title: '编辑乘客信息',
            area: ['420px', 'auto'],
            content: '<form class="layui-form" action="/ticket/passenger/update">'+
                '<input type="hidden" name="passengerId" value="' + passengerId + '">' +
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">姓名</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="name" required lay-verify="required|nameLength" placeholder="请输入姓名" autocomplete="off" class="layui-input" value="' + passengerName + '">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">身份证号</label>' +
                '<div class="layui-input-block">' +
                '<input type="text" name="identity" required lay-verify="required|identity" placeholder="请输入身份证号码" autocomplete="off" class="layui-input" value="' + passengerIdentity + '">' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<label class="layui-form-label">性别</label>' +
                '<div class="layui-input-block">' +
                '<input type="radio" name="gender" value="男" title="男" '+ (passengerGender == '男'? 'checked' : '')+' class="layui-input" >' +
                '<input type="radio" name="gender" value="女" title="女" '+ (passengerGender == '女'? 'checked' : '')+' class="layui-input" >' +
                '</div>' +
                '</div>'+
                '<div class="layui-form-item">' +
                '<div class="layui-input-block">' +
                '<button class="layui-btn" lay-submit lay-filter="formEdit">提交</button>' +
                '<button type="reset" class="layui-btn layui-btn-primary">刷新</button>'  +
                '</div>' +
                '</div>'+
                '</form>',
        });

        // 监听表单提交事件
        form.on('submit(formEdit)', function(data){
            // 提交表单
            $.ajax({
                url: 'http://localhost:8080/ticket/passenger/update',
                type: 'POST',
                data: data.field,
                success: function (response) {
                    if(response.success) {
                        layer.msg(response.msg, {icon: 1}, function () {
                            // 关闭弹出层
                            // layer.close(editModal);
                            window.location.href = "http://localhost:8080/ticket/passenger/findPassenger?pageNo=" + pageNo;
                            // 重置表单
                            // resetForm();
                        });
                    } else {
                        layer.msg(response.msg, {icon: 2,time : 2000});
                    }
                },
                error: function(xhr, textStatus, errorThrown) {
                    layer.msg('修改乘客信息失败：' + xhr.responseText);
                }
            });
            return false;
        });
    });
});