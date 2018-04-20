(function(win) {
	'use strict';
	
	var DEFAULT_IMG_SERVER = 'http://192.168.1.209:83';
	var hostname = window.location.hostname;
	if (hostname.indexOf(".com") != -1) {
		
	}else{
		DEFAULT_IMG_SERVER = 'http://192.168.1.209:83';
	}
	
	var CORE = win.CORE = win.CORE || {};
	
	/**
	 * 拉起登录界面
	 * @param  {array}   params   传入的参数
	 * @param  {Function} callback 回调方法
	 */
	var showLogin = function(callback) {
		//是否是微信用户
		if (CORE.isWeiXin()) {
			$.router.load('/wxAuth?scope=snsapi_userinfo', true);
			return;
		}
		//拉起登录界面
		var loginHTML = '<div class="popup popup-login" id="J_login_popup">' +
		'<header class="bar bar-nav">' +
		'<a class="icon icon-left pull-left" href="javascript:void(0)"></a>' +
		'<h1 class="title">登录</h1>' +
		'</header>' +
		'<div class="content">' +
		'<div class="login-form">' +
		'<div class="list-block">' +
		'<ul>' +
		'<li>' +
		'<div class="item-content">' +
		'<div class="item-inner">' +
		'<div class="item-input input-clear">' +
		'<input type="text" maxlength="20" id="login_username" placeholder="用户名/手机号" />' +
		'</div>' +
		'</div>' +
		'</div>' +
		'</li>' +
		'<li>' +
		'<div class="item-content">' +
		'<div class="item-inner">' +
		'<div class="item-input input-clear">' +
		'<input type="password" maxlength="18" id="login_password" placeholder="密码 " />' +
		'</div>' +
		'</div>' +
		'</div>' +
		'</li>' +
		'</ul>' +
		'</div>' +
		'</div>' +
		'<div class="content-block">' +
		'<a class="button button-fill button-big" id="J_login_submit">登录</a>' +
		'</div>' +
		'<div class="content-block">' +
		'<a href="#" class="pull-left" id="J_regist">立即注册</a>' +
		'<a href="#" class="pull-right" id="J_forget_pwd">忘记密码</a>' +
		'</div>' +
		'</div>' +
		'</div>';
		if ($('#J_login_popup').length) {
			$.popup('#J_login_popup');
			//清空登录文本框
			$('#login_username').val('');
			$('#login_password').val('');
		} else {
			$.popup(loginHTML, false);
		}

		$('#J_login_submit').die().live('click', function(e) {
			CORE.login(callback);
		});
		$('#J_regist').die().live('click', function() {
			$.closeModal('#J_login_popup');
			$.router.load('/regist.shtml', true);
		});
		$('#J_forget_pwd').die().live('click', function() {
			$.closeModal('#J_login_popup');
			$.router.load('/getpwd.shtml', true);
		});
	};

	/**
	 * 登录
	 */
	var login = function(callback) {
		//如果是在拉起的弹层中登录，则登录后关闭弹层并刷页面
		var userName = $('#login_username').val(),
		password = $('#login_password').val();
		if (!isNotNull(userName)) {
			$.toast('用户名不能为空', 1000, 'warning');
			return;
		}
		if (!isNotNull(password)) {
			$.toast('密码不能为空', 1000, 'warning');
			return;
		}
		//打开加载提示
		$.showIndicator();
		//登录
		$.ajax({
			url: "/system/login",
			type: 'post',
			dataType: 'json',
			data: {
				userName: userName,
				password: password
			},
			success: function(response) {
				//关闭加载提示
				$.hideIndicator();
				if (response.code === 1) {
					$.closeModal('#J_login_popup');
					window.location.reload();
				}else{
					$.toast('用户名或密码错误', 1000, 'warning');
				}
			}
		});
	}


	/**
	 * 检测用户是否登录
	 * callback 登录成功后执行的方法
	 */
	var checkLogin = function(callback) {
		$.ajax({
			url: "/user/checkLogin",
			type: 'post',
			dataType: 'json',
			async: false,
			data: {},
			success: function(response) {
				if (response.code != 1) {
					CORE.showLogin(callback);
				} else {
					if (callback && typeof callback === 'function') {
						callback.call(true, response);
					}
				}
			},
			error: function(xhr, errorType, error) {
				CORE.showLogin(callback);
				$.toast('网络错误，请稍后重试', 2000, 'error');
			}
		});
	};

	/**
	 * 退出登录
	 */
	var loginOut = function(callback) {
		$.ajax({
			url: "/system/checkLogin",
			type: 'post',
			dataType: 'json',
			async: false,
			data: {},
			success: function(response) {
				if (response.code === 1) {
					$.router.load('/', true);
					if(typeof callback == "function"){
						callback(response);
					}
				} else {
					$.toast('注销登录失败', 1000, 'error');
				}
			},
			error: function(xhr, errorType, error) {
				$.toast('网络错误，请稍后重试', 2000, 'error');
			}
		});
	};


	/**
	 * 判断是否是微信打开
	 */
	var isWeiXin = function() {
		var foot_pathname = window.location.pathname;
		if (hostname.indexOf("ub.bxcker.com") == -1) {
			return false;
		}
		var ua = navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == "micromessenger") {
			return true;
		} else {
			return false;
		}
	};


	/**
	 * 非空验证
	 */
	var isNotNull = function(value) {
		if (value === null || value.trim() === '') {
			return false;
		}
		return true;
	};


	/**
	 * 处理url
	 * 转为json
	 */
	var urlToJson = function(url) {
		var index = url.indexOf("?"),
		pathname = url,
		data = {};
		if (index > -1) {
			pathname = url.substr(0, index);
			var query = url.substr(index + 1);
			query = query.split("&");
			var len = query.length;
			for (var i = 0; len > i; i++) {
				var kv = query[i].split("="),
				key = kv[0],
				value = kv[1];
				data[key] = value;
			}
		}
		return {
			url: pathname,
			data: data
		};
	};
	
	
	/**
	 * 上传图片
	 */
	var upload = function(picker, successCallback) {
		//上传
		var uploading = false;
		//android下微信上传只能以二进制流上传
		//android下微信如果mimeTypes是具体的后缀类型，无法检测类型错误
		//部分浏览器，如(chrome)下如果不指定具体文件类型，弹出文件选择框奇慢无比
		var uploadType = 0,
			sendAsBinary = false,
			mimeTypes = 'image/jpg,image/jpeg,image/png';
		if ($.device.android && $.device.isWeixin) {
			uploadType = 1;
			sendAsBinary = true;
			mimeTypes = 'image/*';
		}

		// 初始化Web Uploader
		var uploader = WebUploader.create({
			// 选完文件后，是否自动上传。
			auto: true,
			// 文件接收服务端。
			server: '/file/saveImage',
			// 选择文件的按钮。可选。
			pick: picker,
			resize: true,
			chunked: false,
			// 只允许选择图片文件。
			accept: {
				title: 'Images',
				extensions: 'jpg,jpeg,png',
				//不要采用image/* ,会导致chrome下文件选择框弹出很慢
				mimeTypes: mimeTypes
			},
			fileSingleSizeLimit: 8 * 1024 * 1024, // 单张不能超过8M
			formData: {
				uploadType: uploadType
			},
			compress: {
				width: 1600,
			    height: 1600,
				// 图片质量，只有type为`image/jpeg`的时候才有效。
				quality: 90,
				// 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
				allowMagnify: false,
				// 是否允许裁剪。
				crop: false,
				// 是否保留头部meta信息。
				preserveHeaders: true,
				// 如果发现压缩后文件大小比原来还大，则使用原来图片
				// 此属性可能会影响图片自动纠正功能
				noCompressIfLarger: false,
				// 单位字节，如果图片大小小于此值，不会采用压缩。
				compressSize: 0
			},
			duplicate: true,
			sendAsBinary: sendAsBinary
		});

		//出错时
		uploader.onError = function(code) {
			var str;
			switch (code) {
				case 'Q_EXCEED_NUM_LIMIT':
					str = '图片数量超过限制';
					break;
				case 'F_EXCEED_SIZE':
					str = '图片大小超过限制';
					break;
				case 'Q_EXCEED_SIZE_LIMIT':
					str = '图片总大小超过限制';
					break;
				case 'Q_TYPE_DENIED':
					str = '图片类型不允许';
					break;
				case 'F_DUPLICATE':
					str = '文件选择重复';
					break;
				default:
					str = '未知错误，请重试';
					break;
			}
			//处理报错
			$.toast(str, 2000, 'warning');
		};

		//文件入列就隐藏上传弹层
		uploader.on('fileQueued', function() {
			$.closeModal();
			$.showPreloader('上传中...');
		});

		//上传成功
		uploader.on('uploadSuccess', function(file, response) {
			$.hidePreloader();
			successCallback.call(true, response);
		});

		uploader.on('uploadError', function(file) {
			$.hidePreloader();
			$.toast('上传出错', 1000, 'error');
		});

		uploader.on('startUpload', function() {
			uploading = true;
		});

		uploader.on('uploadComplete', function() {
			uploading = false;
		});
	}

	
	/**
	 * 缓存
	 * @return {[type]} [description]
	 * https://github.com/marcuswestin/store.js
	 */
	var store = function() {
		var doc = win.document,
			localStorageName = 'localStorage',
			scriptTag = 'script',
			storage;

		store.disabled = false;
		store.set = function(key, value) {};
		store.get = function(key, defaultVal) {};
		store.has = function(key) {
			return store.get(key) !== undefined;
		};
		store.remove = function(key) {};
		store.clear = function() {};
		store.transact = function(key, defaultVal, transactionFn) {
			if (transactionFn === null) {
				transactionFn = defaultVal;
				defaultVal = null;
			}
			if (defaultVal === null) {
				defaultVal = {};
			}
			var val = store.get(key, defaultVal);
			transactionFn(val);
			store.set(key, val);
		};
		store.getAll = function() {};
		store.forEach = function() {};

		store.serialize = function(value) {
			return JSON.stringify(value);
		};
		store.deserialize = function(value) {
			if (typeof value != 'string') {
				return undefined;
			}
			try {
				return JSON.parse(value);
			} catch (e) {
				return value || undefined;
			}
		};

		function isLocalStorageNameSupported() {
			try {
				return (localStorageName in win && win[localStorageName]);
			} catch (err) {
				return false;
			}
		}

		if (isLocalStorageNameSupported()) {
			storage = win[localStorageName];
			store.set = function(key, val) {
				if (val === undefined) {
					return store.remove(key);
				}
				storage.setItem(key, store.serialize(val));
				return val;
			};
			store.get = function(key, defaultVal) {
				var val = store.deserialize(storage.getItem(key));
				return (val === undefined ? defaultVal : val);
			};
			store.remove = function(key) {
				storage.removeItem(key);
			};
			store.clear = function() {
				storage.clear();
			};
			store.getAll = function() {
				var ret = {};
				store.forEach(function(key, val) {
					ret[key] = val;
				});
				return ret;
			};
			store.forEach = function(callback) {
				for (var i = 0; i < storage.length; i++) {
					var key = storage.key(i);
					callback(key, store.get(key));
				}
			};
		} else if (doc && doc.documentElement.addBehavior) {
			var storageOwner,
				storageContainer;
			try {
				storageContainer = new ActiveXObject('htmlfile');
				storageContainer.open();
				storageContainer.write('<' + scriptTag + '>document.w=window</' + scriptTag + '><iframe src="/favicon.ico"></iframe>');
				storageContainer.close();
				storageOwner = storageContainer.w.frames[0].document;
				storage = storageOwner.createElement('div');
			} catch (e) {
				// 某些情况下因为安全因素不允许使用 ActiveXObject 时
				storage = doc.createElement('div');
				storageOwner = doc.body;
			}
			var withIEStorage = function(storeFunction) {
				return function() {
					var args = Array.prototype.slice.call(arguments, 0);
					args.unshift(storage);
					storageOwner.appendChild(storage);
					storage.addBehavior('#default#userData');
					storage.load(localStorageName);
					var result = storeFunction.apply(store, args);
					storageOwner.removeChild(storage);
					return result;
				};
			};

			// IE7下key不支持数字或某些特殊字符开头
			var forbiddenCharsRegex = new RegExp("[!\"#$%&'()*+,/\\\\:;<=>?@[\\]^`{|}~]", "g");
			var ieKeyFix = function(key) {
				return key.replace(/^d/, '___$&').replace(forbiddenCharsRegex, '___');
			};
			store.set = withIEStorage(function(storage, key, val) {
				key = ieKeyFix(key);
				if (val === undefined) {
					return store.remove(key);
				}
				storage.setAttribute(key, store.serialize(val));
				storage.save(localStorageName);
				return val;
			});
			store.get = withIEStorage(function(storage, key, defaultVal) {
				key = ieKeyFix(key);
				var val = store.deserialize(storage.getAttribute(key));
				return (val === undefined ? defaultVal : val);
			});
			store.remove = withIEStorage(function(storage, key) {
				key = ieKeyFix(key);
				storage.removeAttribute(key);
				storage.save(localStorageName);
			});
			store.clear = withIEStorage(function(storage) {
				var attributes = storage.XMLDocument.documentElement.attributes;
				storage.load(localStorageName);
				for (var i = attributes.length - 1; i >= 0; i--) {
					storage.removeAttribute(attributes[i].name);
				}
				storage.save(localStorageName);
			});
			store.getAll = function(storage) {
				var ret = {};
				store.forEach(function(key, val) {
					ret[key] = val;
				});
				return ret;
			};
			store.forEach = withIEStorage(function(storage, callback) {
				var attributes = storage.XMLDocument.documentElement.attributes;
				for (var i = 0, attr; attr = attributes[i]; ++i) {
					callback(attr.name, store.deserialize(storage.getAttribute(attr.name)));
				}
			});
		}

		try {
			var testKey = '__storejs__';
			store.set(testKey, testKey);
			if (store.get(testKey) != testKey) {
				store.disabled = true;
			}
			store.remove(testKey);
		} catch (e) {
			store.disabled = true;
		}
		store.enabled = !store.disabled;
		return store;
	};
	
	

	/** 公用方法  */
	CORE.checkLogin = checkLogin;
	CORE.login = login;
	CORE.showLogin = showLogin;
	CORE.loginOut = loginOut;
	CORE.isWeiXin = isWeiXin;
	CORE.isNotNull = isNotNull;
	CORE.urlToJson = urlToJson;
	CORE.store = new store();
	CORE.DEFAULT_IMG_SERVER = DEFAULT_IMG_SERVER;
	CORE.upload = upload;

	
	//当时间缓存大于6天,即清理缓存
	var clearStoreKey = "clearStoreAllKey",
		oldDate = CORE.store.get(clearStoreKey);
	if (oldDate == null) {
		CORE.store.set(clearStoreKey, new Date());
	} else {
		var t_date = new Date(oldDate);
		var days = Math.floor((new Date().getTime() - t_date.getTime()) / (24 * 3600 * 1000));
		//   debugger
		if (days > 6) {
			CORE.store.clear();
		}
	}


})(window);