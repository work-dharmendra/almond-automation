define([
        "jquery",
        "underscore",
        "base/view/BaseView",
        'text!header/tpl/header.html',
        "base/module/Util",
        "i18next"
    ],
    function($, _, BaseView, HeaderTpl, Util, i18next) {
        var HeaderView = BaseView.extend({

            events: {
                //"click .header_menu": "header"
            },

            /**
             * Initialize the header view
             */
            initialize: function(options) {
                console.log("HeaderView.initialize : starts");
                if(Util.isNull(options) || Util.isNull(options.header)){
                    this.header = '#project';
                } else {
                    this.header = options.header;
                }
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

            /*header: function(event){
                this.$('.header_menu').parent().removeClass('active');
                $(event.target).parent().addClass('active');
            }*/

        });

        return HeaderView;
    });
