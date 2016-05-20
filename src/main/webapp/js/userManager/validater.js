var formValidate = function () {
    var userValidation = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form = $('#editUser');
            
            form.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: true, // do not focus the last invalid input
                ignore: "",
                rules: {
                	userName: {//用户登录名
                		required: true,
                		remote :{
                			url: contextPath + "/operator/checkUserName",
                			type: "post",
                			data: {
                				userid : function() {
                					return $( "#userid" ).val();
                				},
                				userName : function() {
                					return $( "#userName" ).val();
                				}
                			}
                		}
                	},
                	name: {//姓名
                		required: true
                	},
                	email: {//邮箱
                		email: true
                	},
                	phone: {//电话
                		phoneNum: true
                	},
                },messages: { // 自定义提示信息
                	userName: {
                		required : "登陆用户名不能为空.",
                		remote : "该用户名已存在，请换一个."
                    },
                    name: {
                    	required : "真实姓名不能为空."
                    },
                    phone: {
                    	phoneNum : "请输入正确的电话号码,座机必须加上区号."
                    }
                },
                //以下进行验证结果提示
                errorPlacement: function (error, element) { 
                	var icon = $(element).parent('.input-icon').children('i');
                    icon.removeClass('fa-check').addClass("fa-warning");  
                    icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
                    //error.insertAfter(element);
                },

                highlight: function (element) { 
				$(element)
                        .closest('.form-group').removeClass('has-success');
                    $(element)
                        .closest('.form-group').addClass('has-error');  
                },

                unhighlight: function (element) { // revert the change done by hightlight
                	//移除错误标记
                	var icon = $(element).parent('.input-icon').children('i');
                	 $(element).closest('.form-group').removeClass('has-error');
                	 icon.removeClass("fa-warning");
                },

                success: function (label, element) {
                    var icon = $(element).parent('.input-icon').children('i');
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                    icon.removeClass("fa-warning").addClass("fa-check");
                }
            });

            
    };
    var updPwdValidation = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form = $('#modfiyPassword');
            
            form.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: true, // do not focus the last invalid input
                ignore: "",
                rules: {
                	oldPwd: {//用户旧密码
                		required: true,
                		remote :{
                			url: contextPath + "/operator/checkOldPwd",
                			type: "post",
                			data: {
                				userid : function() {
                					return $( "#userid" ).val();
                				},
                				password : function() {
                					return $( "#oldPwd" ).val();
                				}
                			}
                		}
                	},
                	newPwd1: {//新密码
                		required: true
                	},
                	newPwd2: {//在次输入新密码
                		equalTo: "#newPwd1"
                	}
                },messages: { // 自定义提示信息
                	oldPwd: {
                		required : "旧密码不能为空.",
                		remote : "旧密码不匹配."
                    },
                    newPwd1: {
                    	required : "新密码不能为空."
                    },
                    newPwd2: {
                    	equalTo: "两次输入的密码不一致."
                    }
                },
                //以下进行验证结果提示
                errorPlacement: function (error, element) { 
                	var icon = $(element).parent('.input-icon').children('i');
                    icon.removeClass('fa-check').addClass("fa-warning");  
                    icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
                    //error.insertAfter(element);
                },

                highlight: function (element) { 
				$(element)
                        .closest('.form-group').removeClass('has-success');
                    $(element)
                        .closest('.form-group').addClass('has-error');  
                },

                unhighlight: function (element) { // revert the change done by hightlight
                	//移除错误标记
                	var icon = $(element).parent('.input-icon').children('i');
                	 $(element).closest('.form-group').removeClass('has-error');
                	 icon.removeClass("fa-warning");
                },

                success: function (label, element) {
                    var icon = $(element).parent('.input-icon').children('i');
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                    icon.removeClass("fa-warning").addClass("fa-check");
                }
            });

            
    };
    return {
        //main function to initiate the module
        init: function () {
        	userValidation();
        },
        initPwd : function () {
        	updPwdValidation();
        }
    };

}();
