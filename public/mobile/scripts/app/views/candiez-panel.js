define(function (require) {
	var $               = require('jquery'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/candiez-panel.html'),
    Model			    = require('app/models/candy'),
    bs					= require('bootstrap');
    
	
return Backbone.View.extend({
	
	initialize: function(item){
	},
	
	render: function() {
		this.$el.empty();
		_.templateSettings.variable = "rc";
		console.log(this.collection);
		template = _.template(tpl,this.collection);
		this.$el.html(template);
		$('#candy-modal').modal('show');
		
		return this;
	},
	
	
});



});