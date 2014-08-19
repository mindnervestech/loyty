define(function (require) {
	var $               = require('jquery'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/city-panel.html'),
    bootstrap			= require('bootstrap.min'),
    Model			    = require('app/models/city');
    
	
return Backbone.View.extend({
	_this: this,
	
	initialize: function(){
		
	},
	
	render: function() {
		this.$el.empty();
		_.templateSettings.variable = "rc";
		template = _.template(tpl,this.collection.models);
		this.$el.html(template);
		_this = this;
		return this;
	},
	
	events: {
		"click .city-item"		: "cityChanged"
	},
	
	cityChanged: function(event) {
		var selVal = $(event.target).data('value');
		var selText = $(event.target).html();
		$('#selectedCity').html(selText);
		router.getCategoriesByCity({'value':selVal,'text':selText});
	}
	
});



});