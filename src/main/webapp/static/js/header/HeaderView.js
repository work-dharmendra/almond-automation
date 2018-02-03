define([
        "jquery",
        "underscore",
        "common/view/BaseView",
        'text!header/tpl/header.html',
        "common/module/Util",
        "i18next"
    ],
    function($, _, BaseView, HeaderTpl, Util, i18next) {
        var HeaderView = BaseView.extend({

            events: {
                "click #project": "project"
            },

            /**
             * Initialize the login view
             */
            initialize: function(options) {
                console.log("LoginView.initialize : starts");
                //this.model = new UserModel();
                this.render();
            },

            /**
             * Render method for login view
             */
            render: function() {
                var template = _.template(HeaderTpl);
                this.$el.html(template({
                    i18next: i18next
                }));
                return this;
            },

            
        });

        return HeaderView;
    });
