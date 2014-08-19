define(function (require) {

    "use strict";

    var $                   = require('jquery'),
        Backbone            = require('backbone'),
        
        Candy = Backbone.Model.extend({
        	initialize: function () {
        		
        	},
        	
        }),
        Candiez = Backbone.Collection.extend({
        	model: Candy,
        	url: "/mobile/candiez",
        	fetchByMerchant: function(_merchant, success, error) {
        		var _this = this;
        		this.fetch({
        			data: {
        				   merchant: _merchant
        				  },
        			success: function(data) {
        				success();
        			},
        			error : function(data) {
        				error(data);
        			}
        		});
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
        	Candy : Candy ,
        	Candiez : Candiez
    	}
});


