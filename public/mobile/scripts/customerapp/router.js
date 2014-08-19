define(function (require) {

    "use strict";

    var $					= require('jquery1-8'),
        Backbone    		= require('backbone'),
        MechantWrapper		= require("app/models/merchant"),
    	$mymerchants		= $('#body'),
	    MyMerchantPanel		= require("app/views/my-merchant-panel"),
	    mymerchantPanel		= new MyMerchantPanel(),
	    mymerchants			= new MechantWrapper.Merchants(),
    
    	$mycandiez			= $('#body'),
    	MyCandiezPanel		= require("app/views/my-candiez-panel"),
    	CandyWrapper		= require("app/models/my-candiez"),
    	mycandiez			= new CandyWrapper.Candiez(),
    	mycandiezpanel		= new MyCandiezPanel(),
    	$mystamp			= $('#body'),
    	selectedMerchant	= new Object();
    
    return Backbone.Router.extend({

    	 initialize: function() {
    		 
    		// Tells Backbone to start watching for hashchange events
            

    	 },
        routes: {
        	"": "load",
        	"my-merchants": "myMerchants",
        	"mycandiez"		:	"myPrograms",
        	"my-stamps"		: "myStamps"
        },
        
        landing: function () {
        	landingView.render();
        },
        load: function () {
        	this.myMerchants();
        },
        myMerchants: function() {
        	
        	require(["app/views/my-merchant-panel", "app/models/merchant"], function (MyMerchantPanel, models) {
        		mymerchants = new models.Merchants();
        		mymerchants.fetchByCustomer({
        			data:{customerPhone:'9890932291'},//TODO: remove HdCd
        			success : function(data) {
        				new MyMerchantPanel({collection: mymerchants, el: $mymerchants}).render();
        			},
        			error: function(data){
        				
        			}
        		});
        		
        	});
        },
        setSelectedMerchant: function(selectedMerchant) {
        	this.selectedMerchant = selectedMerchant;
        	//window.location.hash='#mycandiez';
        	//this.myPrograms();
        },
        myPrograms:function() {
        	console.log(this.selectedMerchant);
        	var _this=this;
        	var selectedMerchant = this.selectedMerchant; 
        	require(["app/views/my-candiez-panel", "app/models/my-candiez"], function (MyCandiezPanel, models) {
        		mycandiez = new models.Candiez();
        		mycandiez.fetchByCustomerAndMerchant({
        			data:{customerPhone:'9890932291', merchant:selectedMerchant},//TODO: remove HdCd
        			success : function(data) {
        				mycandiezpanel = new MyCandiezPanel({collection: mycandiez, el: $mycandiez})
        				_this.changePage(mycandiezpanel);
        			},
        			error: function(data){
        				
        			}
        		});
    		});
        },
        setProgramIndex: function(programIndex) {
        	this.programIndex = programIndex;
        	//this.myStamps();
        },
        myStamps:function(programIndex) {
        	var programIndex = this.programIndex; 
        	var _this=this;
        	require(["app/views/my-stamps-panel"],function (MyStampPanel) {
        		_this.changePage(new MyStampPanel({collection: mycandiez.models[programIndex], el: $mystamp}) );
        		mycandiezpanel.undelegateEvents();
        	});
        },
        changePage:function (page) {
            page.render();
            
        }
    });
});