define(function (require) {
	var $               = require('jquery'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/my-merchant-panel.html'),
    Jqm				    = require('jquery.mobile-1.3.2.min');
	
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
	}
	
});



});