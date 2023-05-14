
    // 时间选择器
    layui.use(['laydate', 'form'], function () {
    var laydate = layui.laydate;
    var form = layui.form;
    //执行一个laydate实例
    laydate.render({
    elem: '#departureTimeInput', //指定元素
    type: 'date',
    min:0
});

    //监听表单提交
        form.verify({
            departureTimeInput: function(value, item) {
                if (value.length === 0) {
                    return "发车时间不能为空";
                }
            }
        });

        form.on('submit', function() {
            var departureTimeInput = $('#departureTimeInput').val();
            if (departureTimeInput.length === 0) {
                layer.msg("发车时间不能为空");
                return false;
            }
            return true;
        });


});

