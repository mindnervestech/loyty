define(function (require) {
	var $               = require('jquery1-8'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/my-stamp-panel.html'),
    BS					= require('bootstrap.min');
    
	
return Backbone.View.extend({
	_this: this,
	
	initialize: function(){
		
	},
	
	render: function() {
		console.log(this.collection);
		this.$el.empty();
		_.templateSettings.variable = "rc";
		template = _.template(tpl,this.collection.attributes.stamps);
		this.$el.html(template);
		$('#programID').val(this.collection.attributes.programId);
		return this;
	},
	events : {
		"click .stampable"		: "manageStamping",
		"click .clickable"		: "manageStamping",
		"click #stampButton"	: "stampOn",
		"click #stamp-back"		: "back"
	},
	back : function(e) {
		//customer_router.myPrograms();
	},
	manageStamping : function(e) {
		var index = $(e.target).closest('.stampable').data('index');
		$('#index').val(index);
		$('#type').val("STAMP");
		$("#merchantCodePopup" ).modal( "show" );
	},
	
	stampOn: function(e) {
		if($("#merchantCode").val().toString().length <6) {
			$('#message-area').html("Code should be 6 Character");
			return;
		}
		
		$.ajax({
			url:'/mobile/stamp-on',
			type: 'POST',
			data: {
				merchantCode: $("#merchantCode").val(),
				index		: $("#index").val(),
				type		: $("#type").val(),
				programID	: $("#programID").val(),
				customer	: '9890932291'
			},
			success: function(data){
				$('#message-area').html(data.responseText);
				$("#merchantCode").val("");
				$('#stamp_' + $("#index").val()).not('.redeemable').attr("src","images/seal_stamp.png");
				$('.stamp_'+$("#index").val() + '_smiley').show();
				$('.stamp_'+$("#index").val() + '_smiley').css("display" , "block");
				$("#merchantCodePopup" ).popup( "close" );
			},
			error: function(data){
				console.log(data.responseText);
				$('#message-area').html(data.responseText);
				$("#merchantCode").val("");
			}
		});
	}
});



});