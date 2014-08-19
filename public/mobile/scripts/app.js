require.config({
	baseUrl: 'scripts/lib',
	paths: {
        app: '../app',
        tpl: '../app/tpl'
    },
	shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
        'bootstrap.min': {
        	deps: ['jquery']
        },
        'jquery': {
            exports: 'jQuery'
        }
        
    },
    map: {
        '*': {
            'app/models/category': 'app/models/categories',
            'app/models/city': 'app/models/cities',
            'app/models/merchant': 'app/models/merchants',
            'app/models/candy': 'app/models/candiez',
            'bootstrap': 'bootstrap.min'
        }
    },
	
});

require(['jquery', 'backbone', 'app/router'], function ($, Backbone, Router) {
    router = new Router();
   
    Backbone.history.start();
});