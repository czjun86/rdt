var formValidate = function () {
    var childValidation = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form = $('#saveChild');
            
            form.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: true, // do not focus the last invalid input
                ignore: "",
                rules: {
                	soft_ver: {//姓名
                		required: true,
                		remote :{
                			url: contextPath + "/version/checkName",
                			type: "post",
                			data: {
                				id : function() {
                					return $( "#id" ).val();
                				},
                				name : function() {
                					return $( "#soft_ver" ).val();
                				}
                			}
                		}
                	}
                },messages: { // 自定义提示信息
                    soft_ver: {//姓名
                		remote : "版本名已存在,请换一个."
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
                        .closest('.validateDiv').removeClass('has-success');
                    $(element)
                        .closest('.validateDiv').addClass('has-error');  
                },

                unhighlight: function (element) { // revert the change done by hightlight
                	//移除错误标记
                	var icon = $(element).parent('.input-icon').children('i');
                	 $(element).closest('.validateDiv').removeClass('has-error');
                	 icon.removeClass("fa-warning");
                },

                success: function (label, element) {
                    var icon = $(element).parent('.input-icon').children('i');
                    //$(element).closest('.validateDiv').removeClass('has-error').addClass('has-success'); // set success class to the control group
                    icon.removeClass("fa-warning").addClass("fa-check");
                }
            });

            
    };
    return {
        //main function to initiate the module
        init: function () {
        	childValidation();
        }
    };
}();
