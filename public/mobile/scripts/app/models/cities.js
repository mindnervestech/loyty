define(function (require) {

    "use strict";

    var $                   = require('jquery'),
        Backbone            = require('backbone'),
        
        City = Backbone.Model.extend({
        	
        	defaults: {
        		 value: 'Pune',
        		 text: 'Pune'
        	},
        	initialize: function () {
        		
        	},
        	
        }),
        Cities = Backbone.Collection.extend({
        	model: City,
        	url: "/mobile/cities",
        	selectedCity: new City({value: 'Pune', text: 'Pune'}),
    	    cities:[  {value: 'de', text: 'Delhi'},
    	              {value: 'pu', text: 'Pune'},
    	              {value: 'mu', text: 'Mumbai'}
             ],
             
             fetchByJson: function() {
            	this.reset();
            	this.add(this.cities);
            	return this;
             },
             
            setSelectedCity: function(item) {
            	this.selectedCity = new City(item);
            },
            
        	fetchByCountryAndState: function(options) {
        		this.fetch({
        			data: options.data,
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
        	City : City ,
        	Cities : Cities
    	}
});


