define(function (require) {
	var $               = require('jquery'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/landing.html'),
    template = _.template(tpl);
	
return Backbone.View.extend({
	
	initialize: function(){
	},
	
	render: function() {
		this.$el.empty();
		this.$el.html(template());
		return this;
	}
	
	
});



});