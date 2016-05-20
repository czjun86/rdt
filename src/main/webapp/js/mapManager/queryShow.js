var showOrHide=true;
function showOrhide(){
	if (showOrHide) {
		  $('#titlediv').hide();
		  $(".map_contraction").css({"background":"#fff url("+contextPath+"/scripts/img/screen.png) 6px -28px no-repeat"});
          $("#bigmain").css({"top":"60px"});
		  showOrHide=false;
		} else {
		  $('#titlediv').show();
		  $(".map_contraction").css({"background":"#fff url("+contextPath+"/scripts/img/screen.png) no-repeat scroll 6px 6px"});
		  $("#bigmain").css({"top":"220px"});
		  showOrHide=true;
		}  
}