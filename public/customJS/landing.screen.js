
$(document).ready(function(){
	$("#registration").click(function(){
		$.ajax({
			url:"/registration",
			type:"GET",
			success: function(data){
				$("#registrationHtml").html(data);
				$('#registerModal').modal('show');
			}
		});
	});
	
	$("#forgotPassword").click(function(){
		$.ajax({
			url:"/forgot",
			type:"GET",
			success: function(data){
				$("#registrationHtml").html("");
				$("#registrationHtml").html(data);
				$('#registerModal').modal('show');
			}
		});
	});

	$("#logo").click(function(e){
		e.preventDefault();
		$('html, body').animate({
			scrollTop: $(".body_container").offset().top
		}, 1000);    
	});


	$("#onWhat_is_meant").click(function(e){
		e.preventDefault();
		$('html, body').animate({
			scrollTop: $("#what_is_meant").offset().top
		}, 1000);    
	});


	$("#onHow_it_works").click(function(e){
		e.preventDefault();
		$('html, body').animate({
			scrollTop: $("#how_it_works").offset().top
		}, 1000);    
	});


	$("#onOwn_a_business").click(function(e){
		e.preventDefault();
		$('html, body').animate({
			scrollTop: $("#own_a_business").offset().top
		}, 1000);
	});





});
