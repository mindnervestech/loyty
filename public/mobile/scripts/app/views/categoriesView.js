define(function (require) {
	var $               = require('jquery'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/categories-panel.html');
    
	
return Backbone.View.extend({
	
	initialize: function(){
	
	},
	
	render: function() {
		this.$el.empty();
		_.templateSettings.variable = "rc";
		template = _.template(tpl,this.collection.models);
		this.$el.html(template);
		this.collection.setSelectedCategory(this.$el.first('.category-nav-bar').data('categoryid'));
		return this;
	},
	
	events: {
		"click .category-nav-bar"	: "setCategory"
	},
	
	setCategory: function(e) {
		var selectedCategory = $(e.target).closest('.category-nav-bar').data('categoryid');
		router.getMerchantsByCategory(selectedCategory);
	}
	
});


});