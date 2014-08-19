define(function (require) {
	var $               = require('jquery'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/my-merchant-panel.html');
	
return Backbone.View.extend({
	_this: this,
	
	initialize: function(){
		
	},
	
	render: function() {
		this.$el.empty();
		_.templateSettings.variable = "rc";
		console.log(this.collection.models);
		template = _.template(tpl,this.collection.models);
		this.$el.html(template);
		return this;
	},
	
	events: {
		'click .my-merchant'	: 'merchantSelected'
	},
	
	merchantSelected: function(e) {
		var selectedMerchant = $(e.target).closest('.my-merchant').data('value');
		customer_router.setSelectedMerchant(selectedMerchant);
	}
	
});



});