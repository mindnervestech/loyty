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
        	url: "/mobile/get-merchant-program-for-customer",
        	fetchByCustomerAndMerchant: function(options) {
        		var _this = this;
        		this.fetch({
        			data: options.data,
        			url: _this.url + "/" + options.data.merchant + "/" + options.data.customerPhone ,
        			success: function(data) {
        				options.success(data);
        			},
        			error : function(data) {
        				options.error(data);
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


