define(function (require) {

    "use strict";

    var $                   = require('jquery'),
        Backbone            = require('backbone'),
        
        Category = Backbone.Model.extend({
        	initialize: function () {
        		
        	},
        	
        }),
        Categories = Backbone.Collection.extend({
        	model: Category,
        	url: "/mobile/categories",
        	selectedCategoryId:"",
        	fetchByCity: function(_city, successCallback, errorCallback) {
        		var _this = this;
        		this.fetch({
        			data: {city: _city},
        			success: function(data) {
        				successCallback();
        			},
        			error : function(data) {
        				errorCallback();
        			}
        		});
        	},
        	setSelectedCategory: function(item) {
        		this.selectedCategoryId = item;
        	}
        	
        }),
        
        originalSync = Backbone.sync;

		Backbone.sync = function (method, model, options) {
		    if (method === "read") {
		        options.dataType = "json";
		        return originalSync.apply(Backbone, arguments);
		    }
		}

        return {
        	Category : Category ,
        	Categories : Categories
    	}
});


