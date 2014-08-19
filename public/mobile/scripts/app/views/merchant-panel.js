define(function (require) {
	var $               = require('jquery'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/merchant-panel.html'),
    Model			    = require('app/models/merchant'),
    _thisView			= {},
    CandiezPanel		= require("app/views/candiez-panel");
    
	
return Backbone.View.extend({
	
	
	initialize: function(){
		_thisView = this;
		//this.$el.one('click .merchantSquare',this.merchantSelectEvent);
	},
	
	render: function() {
		this.$el.empty();
		_.templateSettings.variable = "rc";
		console.log(this.collection.models);
		template = _.template(tpl,this.collection.models);
		this.$el.html(template);
		$('#myFlipview').carousel({interval:5000});
		this.$candiez = $('#candiez-panel');
		return this;
	},
	
	events: {
		"click .merchantSquare"		: "merchantSelectEvent"
	},
	
	merchantSelectEvent: function(e) {
		var selectedMerchant = $(e.target).closest('.merchantSquare').data('merchantid');
		merchant = _thisView.collection.get(selectedMerchant).attributes;
		var candiezEl = $('#candiez-panel');
		
		new CandiezPanel({collection: merchant, el:candiezEl}).render();
		
		return false;
		//$('#merchant-panaroma').addClass('animated bounceOutLeft');
		//$('#candiez-panaroma').addClass('animated bounceInLeft');
		//console.log($(candiezEl).html());
		//router.populateCandiesForMerchant(selectedMerchant,candiezEl);
	},
	
	
});



});