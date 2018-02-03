"use strict";

require.config({
    "waitSeconds": 100,
    "paths": {
        "jquery": "libs/jquery/jquery.min",
        "jqueryUI": "libs/jquery/jquery-ui.min",
        "underscore": "libs/underscore/lodash.min",
        "jquery.datatables": "libs/jquery/jquery.dataTables.min",
        //"jquery.ui": "libs/jquery-ui/core",
        "backbone": "libs/backbone/backbone-min",
        "epoxy": "libs/backbone/backbone.epoxy.min",
        "select2": "libs/select2.full.min",
        "routefilter": "libs/backbone/backbone.routefilter.min",
        "bootstrap": "libs/bootstrap/bootstrap.min",
        "text": "libs/backbone/text.min",
        "domReady": "libs/backbone/ready.min",
        "i18next": "libs/i18next/i18next.min",
        "json2": "libs/jquery/json2.min",
        "jstorage": "libs/jquery/jstorage.min",
        "LocalStorage": "base/localstorage/LocalStorage",
        "jqueryBlockUI": "libs/jquery/jquery.blockUI",
        "jquerySlimScroll": "libs/jquery/jquery.slimscroll.min",
        "jqueryEventDrag": "libs/jquery/jquery.event.drag-2.2",
        "backgrid": "libs/backgrid/backgrid"
    },
    urlArgs: "",
    "shim": {
        "underscore": {
            "deps": ["jquery"],
            "exports": "_"
        },
        "backbone": {
            "deps": ["underscore", "jquery", "jqueryUI"],
            "exports": "Backbone"
        },
        "jquery.datatables": {
            "deps": ["jquery"]
        },
        /*"jquery.ui": {
            "deps": ["jquery"]
        },*/
        "bootstrap": {
            "deps": ["jquery", "jqueryUI"]
        },
        "domReady": {
            "deps": ["jquery"]
        },
        "epoxy": {
            "deps": ["underscore", "jquery", "backbone"],
            "exports": "epoxy"
        },
        "i18next": {
            "deps": ["jquery"],
            "exports": "i18next"
        },
        "jquery": {
            "exports": "jquery"
        },
        "jqueryUI": {
            "deps": ["jquery"],
            "exports": "jqueryUI"
        },
        "routefilter": {
            "deps": ["backbone"]
        },
        "json2": {
            "exports": "json2"
        },
        "jstorage": {
            "deps": ["json2", "jquery"],
            "exports": "jstorage"
        },
        "text": {
            "deps": ["underscore", "jquery", "backbone"],
            "exports": "text"
        },
        "jqueryBlockUI": {
            "deps": ["jquery"],
            "exports": "jqueryBlockUI"
        },
        "jquerySlimScroll": {
            "deps": ["jquery"],
            "exports": "jquerySlimScroll"
        },
        "select2": {
            "deps": ["jquery", "bootstrap"]
        },
        "backgrid": {
            "exports": "backgrid",
            "deps": ["jquery", "backbone", "underscore"]
        }
    }
});

require(["jquery", "domReady", "bootstrap", "router", "i18next"],
    function($, domReady, bootstrap, Router, i18next) {

        domReady(function() {
            var router = new Router();
            // Start the history
            // using HTML5 push state

            i18next.init({
                lng: "en",
                debug: true,
                fallbackLng: "en",
                load: "unspecific",
                getAsync: false,
                resGetPath: "static/locale/__lng__/__ns__.json",
                ns: {
                    namespaces: ["translation"],
                    defaultNs: "translation"
                }
            });
            
            Backbone.history.start();
        });
        Backbone.history.navigate("", true);

    });
