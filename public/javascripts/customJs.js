	$(document).ready(function() {
			
			var weekStatus = false;
			var lastUser = false;
			var toDOListUser = false;
			
			$("div.footerDiv").mouseenter(function() {
					 $( this ).css("background-color", "rgba(0, 0, 0, 0.4)");
				 })
				 .mouseleave(function() {
					 $( this ).css("background-color", "rgba(0, 0, 0, 0.1)");
			});
			
			$("#closeBtnWeeklyStatus").on('click', function(){
   				
				$("#weeklyStatus").hide();
			});
			
			$("#closeBtnLastUser").on('click', function(){
   				$("#lastUser").hide();
			});
			
			$("#closeBtnToDoList").on('click', function(){
				$("#toDoList").hide();
   				
			});
			
			$("#hideBtnWeeklyStatus").on('click', function(){
				if(weekStatus == false){
					$("#hideBtnWeeklyStatus").attr("src","assets/images/down.png");
					$("#listWeeklyStatus").hide();
					weekStatus = true;
				}
				else{
					weekStatus = false;
					$("#hideBtnWeeklyStatus").attr("src","assets/images/up.png");
					$("#listWeeklyStatus").show();
				}
			});
			$("#hideBtnLastUser").on('click', function(){
				if(lastUser == false){
					$("#hideBtnLastUser").attr("src","assets/images/down.png");
					$("#listUserStatus").hide();
					lastUser = true;
				}
				else{
					lastUser = false;
					$("#hideBtnLastUser").attr("src","assets/images/up.png");
					$("#listUserStatus").show();
				}
   				
			});
			$("#hideBtnToDoList").on('click', function(){
				if(toDOListUser == false){
					$("#hideBtnToDoList").attr("src","assets/images/down.png");
					$("#listToDo").hide();
					toDOListUser = true;
				}
				else{
					toDOListUser = false;
					$("#hideBtnToDoList").attr("src","assets/images/up.png");
					$("#listToDo").show();
				}
   				
			});
			
			
			$("#inputToday").on('click', function(){
				if (this.checked) {
           			$("#spanToday").css("text-decoration","line-through");
        		}
				else{
					$("#spanToday").css("text-decoration","none");
				}
				 
			});
			
			$("#inputTodayNew").on('click', function(){
				if (this.checked) {
           			$("#spanTodayNew").css("text-decoration","line-through");
        		}
				else{
					$("#spanTodayNew").css("text-decoration","none");
				}
				 
			});
			
			$("#inputTommorow").on('click', function(){
				if (this.checked) {
           			$("#spanTommorow").css("text-decoration","line-through");
        		}
				else{
					$("#spanTommorow").css("text-decoration","none");
				}
				 
			});
			$("#inputTommorowNew").on('click', function(){
				if (this.checked) {
           			$("#spanTommorowNew").css("text-decoration","line-through");
        		}
				else{
					$("#spanTommorowNew").css("text-decoration","none");
				}
				 
			});
			$("#inputThisWeek").on('click', function(){
				if (this.checked) {
           			$("#spanThisWeek").css("text-decoration","line-through");
        		}
				else{
					$("#spanThisWeek").css("text-decoration","none");
				}
				 
			});
			$("#inputThisWeekNew").on('click', function(){
				if (this.checked) {
           			$("#spanThisWeekNew").css("text-decoration","line-through");
        		}
				else{
					$("#spanThisWeekNew").css("text-decoration","none");
				}
				 
			});
			$("#inputThisMonth").on('click', function(){
				if (this.checked) {
           			$("#spanThisMonth").css("text-decoration","line-through");
        		}
				else{
					$("#spanThisMonth").css("text-decoration","none");
				}
				 
			});
			$("#inputThisMonthNew").on('click', function(){
				if (this.checked) {
           			$("#spanThisMonthNew").css("text-decoration","line-through");
        		}
				else{
					$("#spanThisMonthNew").css("text-decoration","none");
				}
				 
			});
			
			
		});