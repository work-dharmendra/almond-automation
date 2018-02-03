/**
 * It defines common method which can be used by all components of stream2ui.
 * It contains very generic methods which can be used by other application outside stream2ui.
 * Do not put any method in this class which is specific to stream2ui.
 * Do not put any method in this class which needs to use this operator.
 */
define("base/module/Util", ["jquery",
    "underscore",
    "i18next"
], function($, _, i18next) {

    function Util() {}

    Util.isNumber = function(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    };

    /**
     * It returns true if variable is not null and not undefined
     */
    Util.isNotNull = function(val) {
        return !(val === undefined || val === null);
    };

    /**
     * It returns true if variable is null or undefined or empty string
     */
    Util.isNull = function(val) {
        return (val === undefined || val === null || val === "");
    };

    /**
     * It returns true if variable is not null and contains some text
     */
    Util.isNotEmpty = function(val) {
        return !(val === undefined || val === null || val === "");
    };

    /**
     * It returns true if variable is either null or contains empty text
     */
    Util.isEmpty = function(val) {
        return (val === undefined || val === null || val === "");
    };

    /**
     * It returns true if array is not null and not undefined and non zero elements
     */
    Util.isNotEmptyArray = function(val) {
        return !(val === undefined || val === null || val.length === 0);
    };

    /**
     * Copied from https://github.com/mrharel/Object-Helper/blob/master/helper-object.js
     * Compare two objects.
     * @method isEqual
     * @param o1 {Object} the object that is compared to
     * @param o2 {Object} the object that is compared with
     * @param cfg {Object} configuration settings:
     *  exclude {Object} key/val map where the key is properties names we want
     *  to exclude from the comparison.
     *  strictMode [Boolean] if true then === will be used to compare premitive types, otherwise
     *  == will be used.
     *  noReverse {Boolean} if this is true then the method will avoid of the reverse comparison.
     *      this will improve the efficiency and speed of the function but could cause to unwanted
     *      results. For example, o1={a:1}; o2={a:1,b:2}; then if we compare o2 to o1 it will match
     *      because o2 has property "a" that has the value 1, but if we compare o1 to o2 it will fail.
     *      this is why we are doing a reverse compare. but the price for the reverse is in performance,
     *      since it is not implemented in the best way and it could reverse the same sub-objects more than
     *      once.
     * @reverse - internal use of the recursion. do not use.
     * @return {Boolean} true if the object are equal.
     */
    Util.isEqual = function(o1, o2, cfg, reverse) {
        cfg = cfg || {};
        cfg.exclude = cfg.exclude || {};

        //first we check the reference. we don't care if null== undefined
        if (cfg.strictMode) {
            if (o1 === o2) return true;
        } else {
            if (o1 == o2) return true;
        }

        if (typeof o1 == "number" || typeof o1 == "string" || typeof o1 == "boolean" || !o1 ||
            typeof o2 == "number" || typeof o2 == "string" || typeof o2 == "boolean" || !o2) {
            return false;
        }

        if (((o1 instanceof Array) && !(o2 instanceof Array)) ||
            ((o2 instanceof Array) && !(o1 instanceof Array))) return false;

        for (var p in o1) {
            if (cfg.exclude[p] || !o1.hasOwnProperty(p)) continue;
            if (!Util.isEqual(o1[p], o2[p], cfg)) return false;
        }
        if (!reverse && !cfg.noReverse) {
            reverse = true;
            return Util.isEqual(o2, o1, cfg, reverse);
        }
        return true;
    };

    Util.addParameter = function(url, param, value) {
        // Using a positive lookahead (?=\=) to find the
        // given parameter, preceded by a ? or &, and followed
        // by a = with a value after than (using a non-greedy selector)
        // and then followed by a & or the end of the string
        var val = new RegExp('(\\?|\\&)' + param + '=.*?(?=(&|$))'),
            qstring = /\?.+$/;

        // Check if the parameter exists
        if (val.test(url)) {
            // if it does, replace it, using the captured group
            // to determine & or ? at the beginning
            return url.replace(val, '$1' + param + '=' + value);
        } else if (qstring.test(url)) {
            // otherwise, if there is a query string at all
            // add the param to the end of it
            return url + '&' + param + '=' + value;
        } else {
            // if there's no query string, add one
            return url + '?' + param + '=' + value;
        }
    };

    Util.getURLParam = function(url, param, value) {

        // get query string part of url into its own variable
        var url = window.location.href;
        if (url.indexOf("?") > -1) {
            var query_string = url.split("?");

            // make array of all name/value pairs in query string
            var params = query_string[1].split("&");

            // loop through the parameters
            var i = 0;
            while (i < params.length) {
                // compare param name against arg passed in
                var param_item = params[i].split("=");
                if (param_item[0] == name) {
                    // if they match, return the value
                    return param_item[1];
                }
                i++;
            }
        }
        return "";
    };

    Util.enableContainsMethodOnString = function() {
        if (!("contains" in String.prototype)) {
            String.prototype.contains = function(str, startIndex) {
                return -1 !== String.prototype.indexOf.call(this, str, startIndex);
            };
        }
    };

    /**
     * Generate random string for given length
     *  Helper function for JS test cases
     */
    Util.getRandomString= function(len) {
        var possible = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        var randomStr = '';
        for (var i = 0; i < len; i++) {
            randomStr += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return randomStr;
    };

    /**
     * It return value of 'key' in object 'obj'.
     * This method is created to get value of nested property in json
     */
    Util.getProperty = function(obj, key) {
        return _(key.split('.')).reduce((obj, key), function(o) {
                return o[key];
        }, obj);
    };

    /**
     * It convert given milliseconds to humar readable format
     */
    Util.convertMilliSec = function(millis) {

        var minutes = Math.floor(millis / 60000);
        var seconds = ((millis % 60000) / 1000).toFixed(0);
        
        if(minutes === 0){
            return seconds + ' ' + i18next.t('app.seconds');    
        } else {
            return minutes + ' ' + i18next.t('app.minutes') + " " + seconds + " " + i18next.t('app.seconds');
        }

    };


    return Util;
});
