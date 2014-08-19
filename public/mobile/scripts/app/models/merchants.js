define(function (require) {

    "use strict";

    var $                   = require('jquery'),
        Backbone            = require('backbone'),
        
        Merchant = Backbone.Model.extend({
        	initialize: function () {
        		
        	},
        	
        }),
        Merchants = Backbone.Collection.extend({
        	model: Merchant,
        	url: "/mobile/merchants",
        	selectedMerchantId:"",
        	fetchByCategoryAndCity: function(_category,_city, success, error) {
        		var _this = this;
        		this.fetch({
        			data: {
        				   category: _category,
        				   city:_city
        				  },
        			success: function(data) {
        				success();
        			},
        			error : function(data) {
        				error(data);
        			}
        		});
        	},
        	fetchByCustomer: function (options) {
        		this.fetch({
        			data: options.data,
        			url: "/mobile/get-customer-registered-merchants/" +options.data.customerPhone ,
        			success: function(data) {
        				options.success(data);
        			},
        			error : function(data) {
        				options.error(data);
        			}
        		});
        	},
        	setSelectedMerchant: function(item) {
        		this.selectedMerchantId = item;
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
        	Merchant : Merchant ,
        	Merchants : Merchants
    	}
});


