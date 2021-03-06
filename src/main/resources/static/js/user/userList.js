layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#userList',
        url : '/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'username', title: '用户名', align:"center"},
            {field: 'nickname', title: '用户昵称', align:'center'},
            {field: 'email', title: '用户邮箱', align:'center',templet:function(d){
                return '<a class="layui-blue" href="mailto:'+d.email+'">'+d.email+'</a>';
            }},
            {field: 'description', title: '用户简介', align:'center'},
            {field: 'status', title: '用户状态',  align:'center',templet:function(d){
                    return d.status == "1" ?  "正常使用" : "限制使用";
                }},
            {title: '操作', minWidth:175, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("userListTable",{
                // url:'/user/list',
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    username: $(".searchVal").val(),  //搜索的关键字
                    nickname: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加用户
    function addUser(edit){
        if(edit != null){
            var url = "edit?id="+edit.id;
        }else{
            var url = "edit";
        }

        var index = layui.layer.open({
            title : "添加用户",
            type : 2,
            content : url,
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find("#uid").val(edit.id);  //用户id
                    body.find(".userName").val(edit.username);  //登录名
                    body.find(".nickName").val(edit.nickname);  //昵称
                    body.find(".userEmail").val(edit.email);  //邮箱
                    // body.find(".userSex input[value="+edit.userSex+"]").prop("checked","checked");  //性别
                    // body.find(".userGrade").val(edit.userGrade);  //会员等级
                    body.find(".userStatus option[value="+edit.status+"]").attr("selected",true);    //用户状态
                    body.find(".description").text(edit.description);    //用户简介
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }
    $(".addNews_btn").click(function(){
        addUser();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            ids = [];
        if(data.length > 0) {
            for (let i in data) {
                ids.push(data[i].id);
            }
            layer.confirm('确定删除选中的用户？', {icon: 3, title: '提示信息'}, function (index) {
                $.post("delete",{
                    ids : ids        //将需要删除的newsId作为参数传入
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            })
        }else{
            layer.msg("请选择需要删除的用户");
        }
    })

    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){     //编辑
            addUser(data);
        }else if(layEvent === 'usable'){ //启用禁用
            var _this = $(this),
                usableText = "是否确定禁用此用户？",
                btnText = "已禁用";
                status = 0;
            if(_this.text()=="已禁用"){
                usableText = "是否确定启用此用户？",
                btnText = "已启用";
                status = 1;
            }
            layer.confirm(usableText,{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){
                _this.text(btnText);
                $.post("edit?id="+data.id,{
                    status : status
                },function (data) {
                    console.log(data);
                })
                layer.close(index);
            },function(index){
                layer.close(index);
            });
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此用户？',{icon:3, title:'提示信息'},function(index){
                $.get("delete/"+data.id,{
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }
    });

})