/**
 * 个人中心
 * @param page
 */

//缓存key
var KEY_FRIEND_DATA = "friendKey";


//首页
function RouterIndex(page) {
	CORE.checkLogin(function(){
		
	});

}; 

//注册
function RouterRegister(page) {
	$('#J_submit').on('click', function(event) {
		var userName = $('#userName').val(),
		mobile = $('#mobile').val(),
		password = $('#password').val();
		surePassword = $('#surePassword').val();

		if (!CORE.isNotNull(userName)) {
			$.toast('用户名不能为空', 1000, 'warning');
			return;
		}
		if (!CORE.isNotNull(mobile)) {
			$.toast('手机号不能为空', 1000, 'warning');
			return;
		}
		if (!CORE.isNotNull(password)) {
			$.toast('密码不能为空', 1000, 'warning');
			return;
		}
		if (password.trim().length < 6) {
			$.toast('密码长度至少6位', 1000, 'warning');
			return;
		}
		if (surePassword.trim().length < 6) {
			$.toast('确认密码不能为空', 1000, 'warning');
			return;
		}
		if (password.trim() != surePassword.trim()) {
			$.toast('两次输入密码不同', 1000, 'warning');
			return;
		}
		//保存
		$.ajax({
			url: "/user/register",
			type: 'post',
			dataType: 'json',
			data: {
				userName: userName,
				mobile: mobile,
				password: password,
			},
			success: function(response) {
				if (response.code === 1) {
					$.confirm('注册成功，是否去登录?', function () {
						$.router.load('/message/index.shtml',true);
					});
				}else{
					$.toast(response.message, 1000, 'warning');
				}
			}
		});
	});

};

//个人中心
function RouterUserIndex(page) {
	CORE.checkLogin(function(){
		loadInfo();
	});

	$('#myInfo').on('click', function(event) {
		$.router.load('/user/myinfo.shtml');
	});
	
    
//	$(page).on('click','.item-media img',function () {
//		$.photoBrowser({
//			photos : [
//			          {
//			        	  url: $(this).attr('src'),
//			          },
//			          ],
//			          theme: 'dark',
//			          type: 'standalone',
//			          toolbar: false,
//			          ofText: '头像'
//		}).open();
//		$(document).find('.photo-browser-current').remove();
//		$(document).find('.photo-browser-total').remove();
//    });
	
	
    //点击时打开图片浏览器
	$(page).on('click','.item-media img',function () {
		var photos = [$(this).attr('src')];
		var photoBrowser = $.photoBrowser({
			photos: photos,
			theme: 'dark',
			toolbar: false,
			navbar: false,
			onClick: function(swiper, e) {
				photoBrowser.close();
			}
		});
		photoBrowser.open();
	});
	

	function loadInfo(){
		$.ajax({
			url: "/user/myinfo",
			type: 'post',
			dataType: 'json',
			data: {},
			success: function(response) {
				if (response.code === 1) {
					var user = response.outData;
					if(user.imgUrl){
						$('#fileImgId').attr('src', CORE.DEFAULT_IMG_SERVER + user.imgUrl);
					}
					$('#nickName').html(user.nickName);
					$('#userName').html('账号：' + user.userName);
				}else{
					$.toast(response.message, 1000, 'warning');
				}
			}
		});
	}

}

//个人资料
function RouterUserMyInfo(page){

	CORE.checkLogin(function(){
		loadInfo();
	});

	function loadInfo(){
		$.ajax({
			url: "/user/myinfo",
			type: 'post',
			dataType: 'json',
			data: {},
			success: function(response) {
				if (response.code === 1) {
					var user = response.outData;
					if(user.imgUrl){
						$('#fileImgId').attr('src', CORE.DEFAULT_IMG_SERVER + user.imgUrl);
					}
					$('#nickName').html(user.nickName);
					$('#userName').html(user.userName);
					$('#sex').html(user.sex);
					if(user.city){
						$('#city').html(user.city);
					}else{
						$('#city').html('未填写');
					}
					if(user.description){
						$('#description').html(user.description);
					}else{
						$('#description').html('未填写');
					}

				}else{
					$.toast(response.message, 1000, 'warning');
				}
			}
		});
	}

	function showHtml(title,input){
		var loginHTML = '<div class="popup popup-login" id="J_change_popup">' +
		'<header class="bar bar-nav">' +
		'<a class="icon icon-left pull-left close-popup" href="javascript:void(0)"></a>' +
		'<button class="button pull-right" id="J_save">保存</button>' + 
		'<h1 class="title">'+ title +'</h1>' +
		'</header>' +
		'<div class="content">' +
		'<div class="login-form">' +
		'<div class="list-block">' +
		'<ul>' +
		'<li>' +
		'<div class="item-content">' +
		'<div class="item-inner">' +
		'<div class="item-input">' +
		//'<input type="text" maxlength="30" id="J_value" />' +
		//'<textarea maxlength="50" id="J_value"></textarea>' +
		input +
		'</div>' +
		'</div>' +
		'</div>' +
		'</li>' +
		'</ul>' +
		'</div>' +
		'</div>' +
		'</div>' +
		'</div>';

		$.popup(loginHTML, true);

		$('#J_save').on('click', function(){
			var txt = null;
			var name = '';
			if(title === '更改名称') {
				txt = $('#J_value').val();
				name = 'nickName';
			}else{
				txt = $('#J_value').val();
				name = 'description';

			}
			update(name, txt);
			if(title === '更改名称') {
				$('#nickName').html(txt);
			}else{
				$('#description').html(txt);
			}
			$.closeModal('#J_change_popup');
		});
	}


	$(page).on('click','#nickName', function () {
		showHtml('更改名称','<input type="text" maxlength="20" value="'+ $(this).html() +'" id="J_value" />');
	});

	$(page).on('click','#description', function () {
		var txt = $(this).html();
		if(txt === '未填写'){
			txt = '';
		}
		showHtml('个性签名','<textarea maxlength="30" id="J_value">'+ txt +'</textarea>');
	});


	$("#sex").picker({
		toolbarTemplate: '<header class="bar bar-nav close-sex">\
			<button class="button button-link pull-right">确定</button>\
			<h1 class="title">请选择</h1>\
			</header>',
			cols: [
			       {
			    	   textAlign: 'center',
			    	   values: ['男', '女', '未知'],
			    	   cssClass: 'picker-items-col-normal'
			       }
			       ]
	});

	$(document).on("click", ".close-sex", function() {
		var pickerToClose = $('.picker-modal.modal-in');
		$.closeModal(pickerToClose);
		var sex = $('.picker-selected').attr('data-picker-value');
		$('#sex').html(sex);
		update('sex', sex);
	});

	$("#city").cityPicker({
		value: ['广东', '深圳', '南山区']
	});

	$(document).on("click", ".close-picker", function() {
		var city = '';
		for(var i = 0; i < $('.picker-selected').length; i++){
			city += ' ' + $('.picker-selected').eq(i).attr('data-picker-value');
		}
		$('#city').html(city);
		update('address', city);
	});


	function update(name, value){
		if (!CORE.isNotNull(value)) {
			$.toast('参数错误', 1000, 'warning');
			return;
		}
		$.ajax({
			url: "/user/update",
			type: 'post',
			dataType: 'json',
			data: {name: name,value: value},
			success: function(response) {
				if(response.code != 1){
					$.toast(response.message, 1000, 'warning');
				}
			}
		});
	}

	//头像上传
	CORE.upload('#uploadFile', function(response) {
		if (response.code == 1) {
			$('#fileImgId').attr('src', CORE.DEFAULT_IMG_SERVER + response.outData.imgUrl);
			uploadFileing = false;
			$.hidePreloader();
		} else {
			uploadFileing = false;
			$.hidePreloader();
			$.toast('头像上传出错', 1000, 'error');
		}
	});
}


//联系人
function RouterFriendsIndex(page){
	CORE.checkLogin(function(){
		loadInfo(0,'');
	});
	
	
	$(page).bind('input propertychange', "#search", function(){
		var fName = $('#search').val();
		loadInfo(0, fName.trim());
	});
	
	function loadInfo(pageNo,fName){
		$('#J_users li').remove();
		$.ajax({
			url: "/user/friendList",
			type: 'post',
			dataType: 'json',
			data: {
				fName:fName,
				pageNo: pageNo,
				pageSize: 10,
			},
			success: function(response) {
				if(response.code === 1){
					if(!response.outData.data){
						return;
					}
					$.each(response.outData.data,function(index, value){
						var src = '/resources/images/head.jpg';
						if(value.head){
							src = CORE.DEFAULT_IMG_SERVER + value.head;
						}
						var li = '<li>\
							        <div class="item-content user-li" data-id="'+ value.id +'">\
					                  <div class="item-inner row">\
						           	     <img class="col-15" src="'+ src +'">\
							             <div class="col-85 font-size">'+ value.name +'</div>\
						              </div>\
					                 </div>\
				                   </li>';
						$('#J_users').append(li);
					});
				}else{
					$.toast(response.message, 1000, 'error');
				}
			}
		});
	}
	
	$(page).on("click", "#J_Friend", function(){
		$.router.load('/friends/addfriend.shtml',true);
	});
	
	
	$(page).on("click", ".user-li", function(){
		var fid = $(this).attr('data-id');
		CORE.store.set(KEY_FRIEND_DATA,{fid: fid});
		$.router.load('/friends/frendinfo.shtml', true);
	});
	
}


//添加朋友
function RouterFriendsAdd(page){
	CORE.checkLogin(function(){
		loadInfo();
	});
	
	function loadInfo(){
		$('.list-group ul li').remove();
		$.ajax({
			url: "/user/newFriendList",
			type: 'post',
			dataType: 'json',
			data: {},
			success: function(response) {
				if(response.code === 1){
					if(!response.outData.data){
						return;
					}
					$.each(response.outData.data,function(index, value){
						var src = '/resources/images/head.jpg';
						if(value.head){
							src = CORE.DEFAULT_IMG_SERVER + value.head;
						}
						var add = '';
						if(value.agree === 0){
							add = '<button class="button button-link" id="J_agree" data-id="'+ value.id +'">同意</button>&nbsp;';
							add += '<button class="button button-link" id="J_noAgree" data-id="'+ value.id +'">拒绝</button>';
						}else if(value.agree === 1){
							add = '<div class="item-title-right">已同意</div>';
						}else if(value.agree === 2){
							add = '<div class="item-title-right">已拒绝</div>';
						}
						var li = '<li class="item-content"><div class="item-media">' +
						'<img class="col-15" src="'+ src +'" width="38px">' +
						'</div><div class="item-inner">' +
						'<div class="item-title" id="nickName">'+ value.fName +'</div>' +
						'<div class="item-after">' + add +
						'</div></div></li>';
						$('.list-group ul').append(li);
					});
				}else{
					$.toast(response.message, 1000, 'error');
				}
			}
		});
	}

	//收索
	$(page).on("click", "#search", function(){
		var value = $('#searchVal').val();
		if(CORE.isNotNull(value)){
			loadFind(value);
		}
	});
	
	//同意
	$(document).on("click", "#J_agree", function(){
		var id = $(this).attr('data-id');
		if(id){
			$.ajax({
				url: "/user/agreeFriend",
				type: 'post',
				dataType: 'json',
				data: {id: id, agree: 1},
				success: function(response) {
					if(response.code === 1){
						$('#J_agree').parent().html('<div class="item-title-right">已同意</div>');
					}else{
						$.toast(response.message, 1000, 'error');
					}
				}
			});
		}else{
			$.toast('操作失败', 1000, 'error');
		}
	});
	//拒绝
    $(document).on("click", "#J_noAgree", function(){
    	var id = $(this).attr('data-id');
    	if(id){
			$.ajax({
				url: "/user/agreeFriend",
				type: 'post',
				dataType: 'json',
				data: {id: id, agree: 2},
				success: function(response) {
					if(response.code === 1){
						$('#J_noAgree').parent().html('<div class="item-title-right">已拒绝</div>');
					}else{
						$.toast(response.message, 1000, 'error');
					}
				}
			});
		}else{
			$.toast('操作失败', 1000, 'error');
		}
	});
	//添加
	$(document).on("click", "#J_add", function(){
		var fid = $(this).attr('data-id');
		if(fid){
			$.ajax({
				url: "/user/add",
				type: 'post',
				dataType: 'json',
				data: {fid: fid},
				success: function(response) {
					if(response.code === 1){
						$.toast('发送成功,等待好友同意', 1000, 'error');
						$('#J_add').parent().html('<div class="item-title-right">待同意</div>');
					}else{
						$.toast(response.message, 1000, 'error');
					}
				}
			});
		}else{
			$.toast('添加失败', 1000, 'error');
		}
		
	});

	function loadFind(value){
		$('.list-group ul li').remove();
		$.ajax({
			url: "/user/find",
			type: 'post',
			dataType: 'json',
			data: {value: value,status:0},
			success: function(response) {
				var li = '<li><div class="item-content"><div class="item-inner">' +
			        '<div class="item-title"></div>' +
					'<div class="item-title">该用户不存在</div>' +
					'<div class="item-title"></div>' +
				    '</div></div></li>';
				var user = response.outData;
				if(response.code === 1 && user.nickName){
					var src = '/resources/images/head.jpg';
					if(user.head){
						src = user.head;
					}
					var add = '<button class="button button-link" id="J_add" data-id="'+ user.uid +'">添加</button>';
					if(user.status === true){
						add = '';
					}
					li = '<li class="item-content"><div class="item-media">' +
					'<img class="col-15" src="'+ src +'" width="38px">' +
					'</div><div class="item-inner">' +
					'<div class="item-title" id="nickName">'+ user.nickName +'</div>' +
					'<div class="item-after">' + add +
					'</div></div></li>';
				}
				$('.list-group ul').append(li);
			}
		});
	}
}


//朋友资料
function RouterFriendsInfo(page){
	
	var fid = CORE.store.get(KEY_FRIEND_DATA).fid;
	
	CORE.checkLogin(function(){
		if(fid){
			loadFriend(fid);
		}
	});
	
	function loadFriend(fid){
		$.ajax({
			url: "/user/find",
			type: 'post',
			dataType: 'json',
			data: {fid: fid, status:1},
			success: function(response) {
				if(response.code === 1){
					var user = response.outData;
					if(!user.userName){
						return;
					}
					var src = '/resources/images/head.jpg';
					if(user.head){
						src = CORE.DEFAULT_IMG_SERVER + user.head;
 					}
					$('#fileImgId').attr('src',src);
					if(user.fName){
						$('#fName').html(user.fName);
					}
					$('#userName').html('账号：' + user.userName);
					$('#nickName').html('昵称：' + user.nickName);
					$('#sex').html(user.sex);
					$('#sex').html(user.sex);
					$('#address').html(user.address);
					if(user.photos){
						$('#photos img').remove();
						$.each(user.photos,function(index, value){
							$('#photos').append('<img width="60px" src="'+ CORE.DEFAULT_IMG_SERVER + value +'">&nbsp;');
						});
					}
					$('#description').html(user.description);
				}else{
					$.toast(response.message, 1000, 'error');
				}
			}
		});
	}
	
	$(page).on('click','#setFName',function () {
		var fName = $('#fName').html();
		showHtml(fName);
	});
	
	//去发信息
	$(page).on('click','#J_send_message',function () {
		var fname = $('#fName').html();
		if(!CORE.isNotNull(fname)){
			fname = $('#nickName').html().replace('昵称：','');
		}
		CORE.store.set(KEY_FRIEND_DATA,{
			fid: fid,
			fname: fname
		});
		$.router.load('/message/sendmsg.shtml', true);
	});
	
	//点击时打开图片浏览器
	$(page).on('click','.item-media img',function () {
		var photos = [$(this).attr('src')];
		var photoBrowser = $.photoBrowser({
			photos: photos,
			theme: 'dark',
			toolbar: false,
			navbar: false,
			onClick: function(swiper, e) {
				photoBrowser.close();
			}
		});
		photoBrowser.open();
	});
	
	
	function showHtml(fName){
		var loginHTML = '<div class="popup popup-login" id="J_change_popup">' +
		'<header class="bar bar-nav">' +
		'<a class="icon icon-left pull-left close-popup" href="javascript:void(0)"></a>' +
		'<button class="button pull-right" id="J_save">保存</button>' + 
		'<h1 class="title">信息备注</h1>' +
		'</header>' +
		'<div class="content">' +
		'<div class="login-form">' +
		'<div class="list-block">' +
		'<ul>' +
		'<li>' +
		'<div class="item-content">' +
		'<div class="item-inner">' +
		'<div class="item-input">' +
		'<input type="text" maxlength="20" id="J_value" placeholder="请输入备注信息" />' +
		'</div>' +
		'</div>' +
		'</div>' +
		'</li>' +
		'</ul>' +
		'</div>' +
		'</div>' +
		'</div>' +
		'</div>';

		$.popup(loginHTML, true);
		$('#J_value').val(fName);

		$('#J_save').on('click', function(){
			var fdName = $('#J_value').val();
			$.ajax({
				url: "/user/updateFrienddName",
				type: 'post',
				dataType: 'json',
				data: { 
					fid: fid, 
					fName:fdName
				},
				success: function(response) {
					if(response.code === 1){
						$('#fName').html(fdName);
						$.closeModal('#J_change_popup');
					}else{
						$.toast(response.message, 1000, 'error');
					}
				}
			});
		});
	}
}

//发送消息
function RouterMessageSend(page){
	var fid = CORE.store.get(KEY_FRIEND_DATA).fid;
	var fname = CORE.store.get(KEY_FRIEND_DATA).fname;
	
	CORE.checkLogin(function(response){
		loadInfo(0);
		scroll();
		clientInit(fid,response.outData.uid);
	});
	
	//初始化
	function loadInfo(pageNo){
		if (pageNo === 0) {
			$("#pageNo").val(pageNo);
			$(".message-list li").remove();
		}
		$('.title').html(fname);
		$.ajax({
			url: "/user/readMessageList",
			type: 'post',
			dataType: 'json',
			data: { 
				fid: fid,
				pageNo: pageNo,
				pageSize: 20
			},
			success: function(response) {
				if(response.code === 1 && response.outData.data){
					if(response.code === 1 && response.outData.data){
						var li = '';
						$.each(response.outData.data,function(index, value){
							var src = '/resources/images/head.jpg';
							if(value.head){
								var src = CORE.DEFAULT_IMG_SERVER + value.head;
							}
							li = '<li data-id="' + value.msgId + '"><img class="item-head-'+ value.status +'" src="'+ src +'">' +
							'<div class="item-ico-'+ value.status +'"></div>' +
							'<div class="item-message-'+ value.status +'">'+ value.content +'</div>' +
							'</li>' + li;
						});
						var h = $('.message-list').height();
						$('.message-list').prepend(li);
						if(pageNo === 0){
							scroll();
						}else{
							$('#J_message_scroll').scrollTop($('.message-list').height() - h - 55);
						}
						//翻页
						maxItems = response.outData.totalPage;
						lastIndex = pageNo + 1;
						$('#pageNo').val(lastIndex);
					}
				}
			}
		});
	}
	
	//输入信息
	$(page).bind('input propertychange', '#J_message', function() {
		var msg = $(this).val().trim();
		if(msg.length > 0){
			$('#J_sendMsg').removeClass('pull-right-message-disabled');
			$('#J_sendMsg').addClass('pull-right-message-enabled');
		}else{
			$('#J_sendMsg').removeClass('pull-right-message-enabled');
			$('#J_sendMsg').addClass('pull-right-message-disabled');
		}
	});
	
	//输入文字时，滚动底部
	$(page).on('click', '#J_message', function() {
		scroll();
	});
	
	
	//查看好友资料
	$(page).on('click', '.item-head-left', function() {
		$.router.load("/friends/frendinfo.shtml");
	});
	
	
	
	//发送
	$(page).on('click', '#J_sendMsg', function() {
		var msg = $('#J_message').val();
		if(msg.trim().length > 0){
			if(msg.length === 1){
				msg += '&nbsp;&nbsp;';
			}
			$.ajax({
				url: "/user/addMessage",
				type: 'post',
				dataType: 'json',
				data: { 
					fid: fid, 
					message: msg
				},
				success: function(response) {
					if(response.code === 1){
						var src = '/resources/images/head.jpg';
						if(response.outData.head){
							var src = CORE.DEFAULT_IMG_SERVER + response.outData.head;
						}
						var li = '<li><img class="item-head-right" src="'+ src +'">' +
						'<div class="item-ico-right"></div>' +
						'<div class="item-message-right">'+ msg +'</div>' +
						'</li>';
						$('.message-list').append(li);
						
						
						$('#J_message').val('');
						$('#J_sendMsg').removeClass('pull-right-message-enabled');
						$('#J_sendMsg').addClass('pull-right-message-disabled');
						scroll();
					}else{
						$.toast(response.message, 1000, 'error');
					}
				}
			});
		}
	});
	
	//自动滚动
	function scroll(){
		var h = $('.message-list').height();
		$('#J_message_scroll').scrollTop(h);
	}
	
	//获取未读信息
	function getNoReadMessage(){
		$.ajax({
			url: "/user/noReadMessageList",
			type: 'post',
			dataType: 'json',
			data: { 
				fid: fid
			},
			success: function(response) {
				if(response.code === 1 && response.outData.data){
					var li = '';
					$.each(response.outData.data,function(index, value){
						var src = '/resources/images/head.jpg';
						if(value.head){
							var src = CORE.DEFAULT_IMG_SERVER + value.head;
						}
						li = '<li data-id="' + value.msgId + '"><img class="item-head-left" src="'+ src +'">' +
						'<div class="item-ico-left"></div>' +
						'<div class="item-message-left">'+ value.content +'</div>' +
						'</li>' + li;
					});
					$('.message-list').append(li);

					//滚动条到达底部自动滚动到底部
					if ($('#J_message_scroll').scrollTop() >= scrollTopHeight) {
						scroll();
					}
				}
			}
		});
	}
	
	//开始定时请求信息
	/*var interval = setInterval(function(){
		getNoReadMessage();
	}, 3000);*/
	
	var client;
	function clientInit(fid,uid){
		if(!window.WebSocket){
			console.log('此浏览器不支持 WebSocket');
			return;
		}
		var url = 'ws://localhost:61614/stomp',
		username = 'admin',
		password = 'admin',
		queueName = 'message.queue-' + fid + '-' + uid;
		
		if(!client){
			client = Stomp.client(url);
		}
		client.connect(username, password, function(frame){
			client.subscribe(queueName, function(message) {
				getNoReadMessage();
			});
		});
	}
	
	
	//清除setInterval
	$(page).on('click', '.disable-router', function() {
		//clearInterval(interval);
		//关闭监听
		client.close();
	});
	
	
	
	//-------------------上拉滚动刷新开始-----------------------//
	// 最多可加载的条目
	var maxItems = 100;
	// 上次加载的序号
	var lastIndex = 0;
	$(page).on("refresh", "#J_message_scroll", function() {
		setTimeout(function() {
			if (lastIndex >= maxItems) {
				$.pullToRefreshDone();
				return;
			}
			loadInfo($("#pageNo").val());
			// 隐藏加载符号
			$.pullToRefreshDone();
		}, 500);
	});
	
	
	var scrollTopHeight = 0;
	$(page).on("infinite", "#J_message_scroll", function() {
		if(scrollTopHeight < $('#J_message_scroll').scrollTop()){
			scrollTopHeight = $('#J_message_scroll').scrollTop();
		}
	});
}


//对话框
function RouterMessageIndex(page){
	CORE.checkLogin(function(){
		loadInfo();
	});
	
	function loadInfo(){
		$('#J_message_list ul li').remove();
		$.ajax({
			url: "/user/messageList",
			type: 'post',
			dataType: 'json',
			data: {},
			success: function(response) {
				if(response.code === 1 && response.outData.data){
					$.each(response.outData.data,function(index, value){
						var src = '/resources/images/head.jpg';
						if(value.head){
							var src = CORE.DEFAULT_IMG_SERVER + value.head;
						}
						var li = '<li data-fid="'+ value.fid +'"><div class="item-content">' +
						'<div class="item-inner row"><img class="col-15" src="'+ src +'"><div class="col-85">' +
						'<div class="row">' +
						'<span class="item-left col-50 font-size" id="fname'+ value.fid +'">'+ value.fname +'</span>' +
						'<span class="item-right col-50 font-size-left">'+ value.time +'</span>' +
						'</div>' +
						'<div class="row">' +
						'<span class="item-left col-90 font-size-left">'+ value.content +'</span>' +
						'</div>' +
						'</div></div></div></li>';
						$('#J_message_list ul').append(li);
					});
					//获取未读信息数量
					getNoReadMsg();
				}
			}
		});
	}
	
	
	//定时刷新
	var interval = setInterval(function(){
		getNoReadMsg();
	}, 3000);
	
	//获取未读信息数量
	function getNoReadMsg(){
		$.ajax({
			url: "/user/noReadMessageNum",
			type: 'post',
			dataType: 'json',
			data: {},
			success: function(response) {
				if(response.code === 1 && response.outData.data){
					var num = 0;
					$.each(response.outData.data,function(index, value){
						num += value.msgNum;
						$('li[data-fid="'+ value.fid +'"]').find('img').prev().remove();
						$('li[data-fid="'+ value.fid +'"]').find('img').before('<span class="message-badge">'+ value.msgNum +'</span>');
					});
					if(num > 0){
						$('.tab-item .badge').remove();
						$('#J_site_footer .tab-item').eq(0).append('<span class="badge">'+ num +'</span>');
					}
				}
			}
		});
		
	}
	
	
	//去发信息
	$(page).on('click','.item-content',function () {
		var fid = $(this).parent().attr('data-fid');
		var fname = $('#fname'+fid).html();
		CORE.store.set(KEY_FRIEND_DATA,{
			fid: fid,
			fname: fname
		});
		clearInterval(interval);
		$.router.load('/message/sendmsg.shtml', true);
	});
	
	$(page).on('click','#J_site_footer .tab-item',function () {
		clearInterval(interval);
	});
	
}



