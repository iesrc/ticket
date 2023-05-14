
    layui.use(['laypage', 'layer','form','element'], function(){
    var laypage = layui.laypage;
    var layer = layui.layer;
    var form = layui.form;
    var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
    var pageNo = parseInt($("#pageNo").val());
    var pageSize = parseInt($("#pageSize").val());
    var pageTotal = parseInt($("#pageTotal").val());
    //全选
    //监听指定开关
    /*form.on('select(mySelect)', function(data){
    $("#pageNo").val(1);
    $("#myPassengerForm").submit();
});*/

    //完整功能
    laypage.render({
    elem: 'pageDemo'
    ,count:pageTotal
    ,limit:pageSize
    ,limits:[5,10,20,50,100]
    ,curr:pageNo
    ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
    ,jump: function(obj,first){
    console.log(obj)
    console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
    console.log(obj.limit); //得到每页显示的条数
    //首次不执行
    if(!first){
    $("#pageNo").val(obj.curr);
    $("#pageSize").val(obj.limit);
    // $("#pageTotal").val(obj.count);
    $("#myForm").submit();
}
}
});
});
