define(function (require) {
	var $               = require('jquery1-8'),
    _                   = require('underscore'),
    Backbone            = require('backbone'),
    tpl                 = require('text!tpl/my-candiez-panel.html');
    
	
return Backbone.View.extend({
	_this: this,
	
	initialize: function(){
		
	},
	
	render: function() {
		this.$el.empty();
		console.log(this.collection.models);
		_.templateSettings.variable = "rc";
		template = _.template(tpl,this.collection.models);
		this.$el.html(template);
		return this;
	},
	events :  {
		'click .my-candy-program'	:	"candyProgramSelected"
	},
	
	candyProgramSelected: function(e) {
		var selectedProgram = $(e.target).closest('.my-candy-program').data('index');
		customer_router.setProgramIndex(selectedProgram);
	},
	
	
});



});