$(function () {
   // 加载树形结构
   loadModuleDate();
});


// 定义树形结构对象
var zTreeObj;


/**
 * 加载资源树形数据
 */
function loadModuleDate() {
    // 配置信息对象 zTree的参数配置
    var setting = {
        // 使用复选框
        check: {
            enable:true
        },
        // 使用简单的JSON数据
        data:{
            simpleData:{
                enable: true
            }
        },
        // 绑定函数
        callback: {
            // onCheck函数：当checkbox/radio 被选中或取消选中时触发的函数
            onCheck:zTreeOnCheck
        }
    }

    // 数据
    // 通过ajax查询资源列表
    $.ajax({
       type:"get",
       // 查询所有的资源列表时，传递角色ID，查询当前角色对应的已经授权的资源
       url:ctx + "/module/queryAllModules" ,
       data:{
           roleId:$("[name='roleId']").val()
       },
       dataType:"json",
       success:function (data) {
           // data:查询到的资源列表
           // 加载zTree树插件
           zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
       }
    });
}

/**
 * 当checkbox/radio 被选中或取消选中时触发的函数
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnCheck(event, treeId, treeNode) {
    // 获取所有被勾选的节点集合
    var nodes = zTreeObj.getCheckedNodes(true);
    // 获取所有的资源的id值
    // 判断并遍历选中的节点集合
    if (nodes.length > 0) {
        // 定义资源ID
        var mIds = "mIds=";
        // 遍历节点集合，获取资源的ID
        for (var i = 0; i < nodes.length; i++) {
            if (i < nodes.length - 1) {
                mIds += nodes[i].id + "&mIds=";
            } else {
                mIds += nodes[i].id;
            }
        }
    }

    // 获取需要授权的角色ID的值（隐藏域）
    var roleId = $("[name='roleId']").val();

    // 发送ajax请求，执行角色的授权操作
    $.ajax({
        type: "post",
        url: ctx + "/role/addGrant",
        data:mIds + "&roleId=" + roleId,
        dataType: "json",
        success:function (data) {
            console.log(data);
        }
    });
}