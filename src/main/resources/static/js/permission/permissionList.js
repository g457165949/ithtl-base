layui.use(['form','layer','table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#permissionList',
        url : '/permission/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        id : "permissionListTable",
        cols: [[
            {field: 'id', title: 'ID', minWidth: 20},
            {field: 'name', title: '角色名', minWidth: 100, align: "center"},
            {field: 'url', title: '路由', minWidth: 100, align: "center"},
            {field: 'type', title: '类型', minWidth: 50, align: "center",templet:function(d){
                return d.type == "1" ?  '<button class="layui-btn layui-btn-xs">菜单</button>'
                    : '<button class="layui-btn layui-btn-xs layui-btn-normal">按钮</button>';
            }},
            {field: 'perms', title: '授权标识', minWidth: 100, align: "center"},
            // {field: 'description', title: '描述', minWidth: 100, align: "center"},
            {title: '操作', minWidth: 175, templet: '#permissionListBar', fixed: "right", align: "center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("permissionListTable",{
                url:'/permission/list',
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: $(".searchVal").val(),  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加用户
    function addData(data){
        if(data != null){
            var url = "edit?id="+data.id+"&parent_id="+data.parentId;
            var title = "修改权限";
        }else{
            var url = "edit";
            var title = "添加权限";
        }

        var index = layui.layer.open({
            title : title,
            type : 2,
            content : url,
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(data){
                    body.find("#id").val(data.id);
                    body.find(".name").val(data.name);
                    body.find(".url").val(data.url);
                    body.find(".sort").val(data.sort);
                    body.find("input[type=radio][value="+data.type+"]").next().find("i").click();
                    body.find(".data").val(data.data);
                    body.find(".perms").val(data.perms);
                    body.find(".parentId").val(data.parentId);
                    body.find(".description").val(data.description);  //邮箱
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回权限列表', '.layui-layer-setwin .layui-layer-close', {
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
        addData();
    });

    //列表操作
    table.on('tool(permissionList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){     //编辑
            addData(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此权限？',{icon:3, title:'提示信息'},function(index){
                $.get("delete/"+data.id,{
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }
    });

})