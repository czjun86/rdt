var groupValidate = function () {
    var groupValidation = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form = $('#addGroupForm');
            
            form.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: true, // do not focus the last invalid input
                ignore: "",
                rules: {
                	groupName: {//用户登录名
                		required: true,
                		remote :{
                			url: contextPath + "/group/checkGroupName",
                			type: "post",
                			data: {
                				groupName : function() {
                					return $( "#groupName" ).val();
                				}
                			}
                		}
                	}
                },messages: { // 自定义提示信息
                	groupName: {
                		required : "用户组名不能为空.",
                		remote : "该用户组名已存在，请换一个."
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
        	groupValidation();
        }
    };

}();
