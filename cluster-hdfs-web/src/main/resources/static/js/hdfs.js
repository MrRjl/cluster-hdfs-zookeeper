var vo={};
vo.click="click";
vo.get="GET";
vo.post="POST";
vo.deletes="DELETE";
vo.dataType="json";
vo.options="OPTIONS";
vo.contentType="application/json;charset=utf-8";
vo.json="callback";
vo.htmlNull="";
vo.put="PUT";
vo.pathSprit="/";

//初始化
$(function(){
	$('#directory').val(vo.pathSprit);
	ajaxToGetDir("directory",vo.pathSprit);
})

//进入目录
$("#btn-nav-directory").on(vo.click,function(){
	ajaxToGetDir("directory",$("#directory").val());
	
});

//返回上一级
$("#return_superDir").on(vo.click,function(){
	var str=$("#directory").val(); var url="";
	if(str=="" || str=="/"){
		url="/"; error("已经到最顶部文件夹了!"); return;
	}else{
		var arr=str.split('/');
		if(arr.length==2){
			url="/";
		}else{
			for(var i=0;i<arr.length-1;i++){
				if(arr[i]==""){ continue; }
				url=url+"/"+arr[i];
			}
		}
	}
	ajaxToGetDir("directory",url);
	$("#directory").val(url);
});




//成功提示
function success(msg){
	toastr.success(msg);
}
//失败提示
function error(msg){
	toastr.error(msg);
}
//input inter键
$("#directory").keydown(function(event){
	
	if(event.keyCode==13){
		
		ajaxToGetDir("directory",$("#directory").val());
	}else{
		return;
	}
});
//创建文件夹模态框 取消按钮
$("#mkdir-directory").on(vo.click,function(){
	$("#new_directory").val("");
})



//创建文件夹模态框 创建按钮
$("#btn-create-directory-send").on(vo.click,function(){
	var new_directory=$("#new_directory").val();
	var input=$("#directory").val();
	if(new_directory==""){
		error("请输入需要创建的文件夹名称");
		return;
	}
	if(input==""){
		input="/";
	}else if(input=="/"){
	}else{
		input=input+"/";
	}
	var data=input+new_directory;
	var tbody=$("#table-explorer tbody");
	$.ajax({
        type: vo.post,
        async: true,
        url: "cretedirectory",
        data:{ dir:data },
        success: function (json) {
        	tbody.html("");
        	var data=json;
        	if(data.success==true){
        		$('#btn-create-directory').modal('hide');
        		$("#new_directory").val("");
        		ajaxToGetDir("directory",input);
        	}else{ error("失败"); tbody.html("暂无文件"); }
        },  error: function () { error("失败");  }
    });
})





//table ajax 请求刷新
function ajaxToGetDir(url,pathvalues){
	var tbody=$("#table-explorer tbody");
	$.ajax({
        type: vo.get,
        url: url,
        async: true,
        contentType: vo.contentType,
        dataType: vo.dataType,
        data:{
        	dir:pathvalues
        },
        json: vo.json,
        success: function (json) {
        	var data=json;
        	if(data.success==true){
        		if(data.body.length<1){
        			success("暂无文件");
        			tbody.html("暂无文件");
        		}
        		
        		dataToTbody(data.body,tbody);
        	}else{
        		error("失败");
        		tbody.html("暂无文件");
        	}
        },
        error: function () {
        	error("失败");
        }
    });
}

//table 数据解析
function dataToTbody(data,target){
	target.html(vo.htmlNull);
	for(var a=0;a<data.length;a++){
		var tr=$('<tr>');
		$("<a class='fileName-data' " +
				"fileName='"+data[a].fileName+"' " +
				"isdir='"+data[a].isdir+"'>")
				.appendTo("<td>")
				.appendTo(tr)
				.html(data[a].fileName);
		$("<td>").appendTo(tr).html("");
		$("<td>").appendTo(tr).html(data[a].length+"KB");
		$("<td>").appendTo(tr).html(data[a].modification_times);
		$("<td>").appendTo(tr).html(data[a].block_replication);
		$("<td data-toggle='modal' data-target='#delete-modal' " +
				"fileName='"+data[a].fileName+"'" +
				" isdir='"+data[a].isdir+"'>")
				.appendTo(tr)
				.addClass('glyphicon glyphicon-trash glyphicon-delect')
				.val(data[a].path);
		if(data[a].isdir==false){
			$("<td data-toggle='modal' data-target='#down-modal' fileName='"+data[a].fileName+"' isdir='"+data[a].isdir+"'>")
			.html("下载")
			.appendTo(tr)
			.addClass('glyphicon glyphicon-down')
			.val(data[a].path);
		}
		tr.appendTo(target);
	}
	//删除请求
	$("tr .glyphicon-delect").on(vo.click,function(){
		var val=$(this).val();
		var input=$("#directory").val();
		if(input==""){ input="/"; }
		console.log(input);
		$("#delete-button").one(vo.click,function(){
			dirOperation("delectdirectory",input,val,"#delete-modal","")
		});
		
	});
	
	//下载请求
	$("tr .glyphicon-down").on(vo.click,function(){
		var val=$(this).val();
		var input=$("#directory").val();
		var isDir=$(this).attr("isdir");
		var fileName=$(this).attr("fileName");
		$("#downs-button").one(vo.click,function(){
			if(isDir=="true"){ error("当前点击属于文件夹,无法下载!"); return; }
			clearfileOrHtmlhidde("","#down-modal");
			window.location.href="/downloadFile?dst="+val+"&fileName="+fileName;
			success("成功");
		});
	});
	//点击文件夹
	$(".fileName-data").on(vo.click,function(){
		if(($(this).attr("isdir"))=="false"){ error("当前点击属于文件,无法进入!"); return; }
		var pathvalues = $('#directory').val();
		var  ss=($(this).attr("fileName"));
		if(pathvalues=="/"){ pathvalues=""; }
		$('#directory').val(pathvalues+vo.pathSprit+ss);
		ajaxToGetDir("directory",pathvalues+vo.pathSprit+ss);
	})
}

function Operation(url,path,dst,filename,tg,src){
	var tbody=$("#table-explorer tbody");
	$.ajax({
        type: vo.get,
        url: url,
        async: true,
        data:{ dst:dst, fileName:filename
        },
        success: function (json) {
        	tbody.html("");
        	$(tg).modal('hide');
        	clearfile(src);
        	ajaxToGetDir("directory",path);
        	success("成功");
        },
        error: function () {
        	$(tg).modal('hide');
        	clearfile(src);
        	error("失败");
        }
    });
}
function dirOperation(url,path,datas,tg,src){
	var tbody=$("#table-explorer tbody");
	$.ajax({
        type: vo.post,
        url: url,
        async: true,
        data:{ dir:datas  },
        success: function (json) {
        	tbody.html("");
        	var data=json;
        	if(data.success==true){
        		$(tg).modal('hide');
        		clearfile(src);
        		ajaxToGetDir("directory",path);
        	}else{ 
        		$(tg).modal('hide');
        		clearfile(src);
        		error("失败"); 
        		tbody.html("暂无文件"); }
        },  error: function () { error("失败");  }
    });
	
}
//上传文件
$("#modal-upload-file-button").on(vo.click,function(){
	var formData = new FormData();
	var path=$("#directory").val();
    formData.append("file",$('#modal-upload-file-input')[0].files[0]);
    formData.append("dst",path);
    $.ajax({
        url:"/uploadFile", /*接口域名地址*/
        type:vo.post,
        data: formData,
        contentType: false,
        processData: false,
        success:function(res){
        	clearfileOrHtmlhidde("#modal-upload-file-input","#modal-upload-file");
        	ajaxToGetDir("directory",path);
        	
        },  error: function () { error("失败");  }
    })
});
function clearfile(obj){
    var file = $(obj).parent().find(".fileData");
    $(file).val('');
}
function clearfileOrHtmlhidde(obj,htmlobj){
    var file = $(obj).parent().find(".fileData");
    $(file).val('');
    $(htmlobj).modal('hide');
}

