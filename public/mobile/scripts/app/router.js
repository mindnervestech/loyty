define(function (require) {

    "use strict";

    var $					= require('jquery'),
        Backbone    		= require('backbone'),
        $body				= $('body'),
        
        $cities				= $('#city-panel'),
        CityPanel			= require("app/views/city-panel"),
        cityPanel			= new CityPanel({el:$cities}),
        CityWrapper			= require("app/models/city"),
        cities				= new CityWrapper.Cities(),

        $categories			= $('#category-panel'),
        CategoryPanel		= require("app/views/categoriesView"),
        categoryPanel		= new CategoryPanel({el:$categories}),
        CategoryWrapper		= require("app/models/category"),
        categories			= new CategoryWrapper.Categories(),
        
        $merchants			= $('#merchant-panel'),
        MechantPanel		= require("app/views/merchant-panel"),
       // merchantPanel		= new MechantPanel({el:$merchants}),
        MechantWrapper		= require("app/models/merchant"),
        merchants			= new MechantWrapper.Merchants(),
    
	    CandiezPanel		= require("app/views/candiez-panel"),
	    candiezPanel		= new CandiezPanel(),
	    CandyWrapper		= require("app/models/candy"),
	    candiez				= new CandyWrapper.Candiez();
    
    	
	    

    return Backbone.Router.extend({

        routes: {
        	"": "load",
        	"categories/:city": "getCategories",
        	"cities": "getCities",
        	"my-merchants": "myMerchants"
        },
        
        landing: function () {
        	landingView.render();
        },
        load: function () {
        	this.getCities();
        },
        myMerchants: function() {
        	$merchants.empty();
        	require(["app/views/my-merchant-panel", "app/models/merchant"], function (MyMerchantPanel, models) {
        		mymerchants = new models.Merchants();
        		mymerchants.fetchByCustomer({
        			data:{customerPhone:'9890932291'},//TODO: remove HdCd
        			success : function(data) {
        				categoryPanel = new MyMerchantPanel({collection: mymerchants, el: $mymerchants}).render();
        			},
        			error: function(data){
        				
        			}
        		});
        		
        	});
        },
        getCategories: function () {
        	$categories.empty();
        	require(["app/views/categoriesView", "app/models/category"], function (CategoriesView, models) {
        		categories = new models.Categories();
        		categories.fetchByCity(cities.selectedCity.attributes.value,
        			 function() {
        				categoryPanel = new CategoriesView({collection: categories, el: $categories}).render();
        				router.getMerchants()
        			 },
        			 function(data) {
        				 
        			 }
        		);
        	});
        },
        getCategoriesByCity: function (item) {
        	cities.setSelectedCity(item);
        	this.getCategories();
        },
        getMerchantsByCategory: function(item) {
        	categories.setSelectedCategory(item);
        	this.getMerchants();
        },
        populateCandiesForMerchant: function(selectedMerchant,candiezEl) {
        	require(["app/views/candiez-panel", "app/models/candy"], function (CandyPanel, models) {
        		candiez = new models.Candiez();
        		candiez.fetchByMerchant(selectedMerchant,
        			function(){
        				new CandyPanel({collection: candiez, el:candiezEl}).render();
        			},
        			function(){}
        		);
        		
        	});
        },
        getCities: function () {
        	$cities.empty();
        	require(["app/views/city-panel", "app/models/city"], function (CityPanel, models) {
        		cities = new models.Cities();
        		cities.fetchByCountryAndState({
        			data: {country:'IN', state:'All'},
        			success: function() {
        				cityPanel = new CityPanel({collection: cities, el:$cities}).render();
        				router.getCategories();
        			},
        			error: function() {
        				
        			}
        		});
        		
        	});
        },
        
        getMerchants: function () {
        	$merchants.empty();
        	require(["app/views/merchant-panel", "app/models/merchant"], function (MerchantPanel, models) {
        		merchants = new models.Merchants();
        		merchants.fetchByCategoryAndCity(categories.selectedCategoryId,cities.selectedCity.attributes.value,
        			function(){
        				new MerchantPanel({collection: merchants, el:$merchants}).render();
        			},
        			function(){}
        		);
        		
        	});
        }
    });

});