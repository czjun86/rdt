/**
 * 分页脚本,使用本脚本需在你的页面引入skin/default.css样式文件,同时还需引入js/ejs_production.js和当前插件
 * scripts/table-pages.js 使用： PageUtils.init({ "sync" : true,
 * //异步或者form提交,默认form提交 "method" : 1, //当sync为true时此参数需传递调用的方法 
 * "methodParameter":["par1","par2",...], //给 method 指定的方法提供参数
 * "lengthMenu" : [5, 15, 20, "All" ], // 页大小,All实际值为-1 "defaultLength" : 15, // 默认页大小
 * "pageIndex" : 0, // 默认当前页 "total" : 0, // 默认总页数 "displayContainer" : "", //
 * 要显示的容器 "form" : "", // 页面提交表单 "language" : "zh" // 语言 zh or en //语言 });
 * 
 * @author tian.bo
 */
(function($) {

	var _configs = {
		"sync" : true, // true表单提交 false函数提交,默认form提交
		"method" : -1, // 当sync为true时此参数设为-1,否则函数名
		"methodParameter" : [],
		"lengthMenu" : [ 10, 15, 20, 50 ], // 页大小,All实际值为-1
		//"lengthMenu" : [ 5, 15, 20, "All" ], // 页大小,All实际值为-1
		"defaultLength" : 15, // 默认页大小
		"pageIndex" : 0, // 默认当前页
		"total" : 0, // 默认总页数
		"displayContainer" : "", // 要显示的容器
		"form" : "", // 页面提交表单,如果sync为false时此参数可以不填,否则填写需提交的表单id
		"language" : "zh" // 语言 zh or en //语言
	};

	var PageUtils = {
		/**
		 * 初始化并显示分页插件
		 * 
		 * @param configs
		 */
		init : function(configs) {
			this.validate(configs);
			new TablePage(configs).show();
		},
		/**
		 * 参数验证
		 * 
		 * @param configs
		 */
		validate : function(configs) {
			var flag;
			if (!configs) {
				throw new Error(
						"Structure parameters can't be empty,Please check the parameters");
			}
			if (configs.sync != undefined && configs.sync == true) {
				if (!configs.form || $.trim(configs.form) == '') {
					throw new Error(
							"form can't be empty,Please check the parameters");
				}
			}
			if (!configs.displayContainer
					|| $.trim(configs.displayContainer) == '') {
				throw new Error(
						"displayContainer can't be empty,Please check the parameters");
			}
			if (!configs.sync) {
				flag = Object.prototype.toString.call(configs.method) === '[object Function]';
				if (!flag) {
					throw new Error("method is not function,Please check the parameters");
				}
			}
		}
	};

	/**
	 * 构造函数
	 */
	function TablePage(configs) {
		this.configs = _configs;
		this.html = "";
		this.totalPage = 0;
		this.currPage = 0;
		this.pageSize = this.configs['defaultLength'];
		this.init(configs);
		this.calculate();

	}

	TablePage.prototype = {
		/**
		 * 初始化方法
		 * 
		 * @param configs
		 */
		init : function(configs) {
			for ( var i in configs) {
				this.configs[i] = configs[i];
			}
			this.sync = this.configs['sync'] == true ? true : false;
		},

		/**
		 * 显示分页插件
		 */
		show : function() {
			this.html += new EJS({
				url : '../js/common/page.ejs'
			}).render(this);
			$("#" + this.configs['displayContainer']).empty().html(this.html);
			
			//goto 创建select2
			var config = this.configs;
			var page = this.currPage;
			var size = this.pageSize;
			var tpage = this.totalPage;
			var sync = this.sync;
			$(".goto").val(page);
			var datas = [];
			for(var i=1; i<=tpage; i++){
				var dt = new Object();
				dt.id = i+"";
				dt.text = i+"";
				dt.selected = true;
				datas[i-1] = dt;
			}
			$(".goto").select2({data:datas}).change(function(){
				//绑定选择事件
				var formid = config.form;
				var form = $("#" + formid);
				var ipageIndex = form.find("input[name='pageIndex']");
				ipageIndex.val(this.value);
				if (sync) {
					var formid = config.form;
					$("#modal-backdrop").show();//打开遮挡层
					$("#" + formid).submit();
				} else {
					var fn = config.form.method;
					var par = config.methodParameter;
					var parameter=par.split(",");
					if(Object.prototype.toString.call(fn) === '[object Function]'){
						if(parameter.length>0){
							fn(parameter);
						}else{
							fn();
						}
					}
				}
			});
			
			//创建select2
			$(".go").val(size);
			var datas2 = [];
			for(var i=0; i<config.lengthMenu.length; i++){
				var dt = new Object();
				dt.id = config.lengthMenu[i]+"";
				dt.text = config.lengthMenu[i]+"";
				datas2[i] = dt;
			}
			$(".go").select2({data:datas2}).change(function(){
				//绑定选择事件
				var formid = config.form;
				var form = $("#" + formid);
				var ipageSize = form.find("input[name='pageSize']");
				ipageSize.val(this.value);
				
				if (sync) {
					var formid = config.form;
					$("#modal-backdrop").show();//打开遮挡层
					$("#" + formid).submit();
				} else {
					var fn = config.form.method;
					var par = config.methodParameter;
					var parameter=par.split(",");
					if(Object.prototype.toString.call(fn) === '[object Function]'){
						if(parameter.length>0){
							fn(parameter);
						}else{
							fn();
						}
					}
				}
			});
		},
		/**
		 * 计算
		 */
		calculate : function() {
			this.currPage = this.configs['pageIndex'];
			var resultCount = this.configs['total'];
			var pageSize = this.configs['defaultLength'];
			this.totalPage = resultCount;
			this.pageSize = pageSize;
			var pageArrs = [];
			for ( var i = 1; i <= this.totalPage; i++) {
				pageArrs.push(i);
			}
			var start = 1;
			var end = this.totalPage;
			var left = 5; // 当前页的左边显示5页,总页数大于10的情况下
			var right = 4; // 当前页的右边显示4页,总页数大于10页的情况下
			var sum = 10; // 最多显示10页
			if (this.totalPage > sum) {
				var step1 = this.currPage - left;
				var step2 = this.currPage + right;
				if (step1 > 0) {
					start = step1;
				}
				if (step2 < this.totalPage && this.currPage > 5) {
					end = step2;
				}
				if (step2 > this.totalPage) {
					end = this.totalPage;
					start = end - sum + 1;
				}
				if (this.currPage <= left + 1) {
					end = sum;
				}
			}
			this.start = start;
			this.end = end;
		}

	};
	window.PageUtils = PageUtils;
})(jQuery);
