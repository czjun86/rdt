var kpiValidate = function () {
    var kpiValidation = function() {
            var form = $('#kpiForm');
            form.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: true, // do not focus the last invalid input
                ignore: "",
                rules: {
                	kpiRangVal11: {
                		required: true,
                		numberDigits: true,
                		lessTarget:"#kpiRangVal12"
                	},
                	kpiRangVal21: {
                		required: true,
                		numberDigits: true,
                		lessTarget:"#kpiRangVal22"
                	},
                	kpiRangVal31: {
                		required: true,
                		numberDigits: true,
                		lessTarget:"#kpiRangVal32"
                	},
                	kpiRangVal41: {
                		required: true,
                		numberDigits: true,
                		lessTarget:"#kpiRangVal42"
                	},
                	kpiRangVal51: {
                		required: true,
                		numberDigits: true,
                		lessTarget:"#kpiRangVal52"
                	},
                	kpiRangVal61: {
                		required: true,
                		numberDigits: true,
                		lessThan:"#kpiRangVal62"
                	},
                	kpiRangVal62: {
                		required: true,
                		numberDigits: true,
                		greaterThan:"#kpiRangVal61"
                	},
                },messages: { // 自定义提示信息
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
                    //$(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                    icon.removeClass("fa-warning").addClass("fa-check");
                }
            });

            
    };
    return {
        //main function to initiate the module
        init: function () {
        	kpiValidation();
        }
    };

}();
