layui.use(['form', 'layedit', 'laydate'], function(){
    var form = layui.form
        ,layer = layui.layer;



    //自定义验证规则
    form.verify({
        name: function(value) {
            if (value.length < 2) {
                return '姓名至少得2个字符啊';
            }
            if (!/^[\u4e00-\u9fa5a-zA-Z]+$/.test(value)) {
                return '姓名只能包含中文或英文字母';
            }
        }//其中，正则表达式 /^[\u4e00-\u9fa5a-zA-Z]+$/ 表示匹配字符串必须由中文或英文字母组成，
        // 并且最少有一个字符。其中 [\u4e00-\u9fa5] 匹配中文字符，[a-zA-Z] 匹配英文字母
        ,identity: function(value) {
            if (value.length !== 18) {
                return '身份证得18个数字啊';
            }
            if (!/^\d+$/.test(value)) {
                return '身份证号码须为数字';
            }
        }
    });


    //监听提交
    //监听提交
    form.on('submit(addPassengerDemo)', function(data){
        $.ajax({
            url:'http://localhost:8080/ticket/passenger/doAddPassenger',
            type:'POST',
            data:data.field,//表单中的所有数据
            success:function (res) {
                if(res.success){
                    layer.msg(res.msg,{icon:1,time:2000},function(){
                        window.location.href = "http://localhost:8080/ticket/passenger/findPassenger";
                    });
                }else {
                    layer.msg(res.msg,{icon:5,time:2000});
                }
            }
        });
        return false;
    });


});