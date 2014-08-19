require.config({
	baseUrl: 'scripts/lib',
	paths: {
        app: '../customerapp',
        tpl: '../customerapp/tpl'
    },
	shim: {
        'backbone': {
            deps: ['underscore', 'jquery1-8'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
        'jquery1-8': {
            exports: 'jQuery'
        }
        
    },
    map: {
        '*': {
            'app/models/merchant': 'app/models/merchants'
        }
    },
	
});

require([ "jquery1-8","backbone","app/router" ], function( $, Backbone, Mobile ) {

  customer_router = new Mobile();
    Backbone.history.start();

  } );

