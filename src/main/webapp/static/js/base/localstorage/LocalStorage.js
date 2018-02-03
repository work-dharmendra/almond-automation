define(["jquery", "backbone", "jstorage", "json2"],
    function($, BackBone) {
        var LocalStorage = (function() {

            var previousKey = "",

                _get = function(key) {
                    return $.jStorage.get(key);
                },

                _set = function(key, value) {
                    $.jStorage.set(key, value);
                },

                _deleteKey = function(key) {
                    $.jStorage.deleteKey(key);
                },

                _flush = function(key) {
                    $.jStorage.flush();
                },

                _getUniqueKey = function() {
                    var locationHash = location.hash,
                        randomNumber = Math.random(),
                        webStorageKey = "";


                    if (previousKey !== "") {
                        _stopListening(previousKey);
                    }

                    var currentUniqueKey = _get("currentUniqueKey");

                    if (currentUniqueKey === null) {

                        _set("currentUniqueKey", randomNumber + "!@#$%^&*publisherportal!@#$%^&*" + $("#user").val());

                        webStorageKey = randomNumber;
                    } else {

                        var userName = currentUniqueKey.split("!@#$%^&*")[2];

                        if (userName != $("#user").val()) {
                            _set("currentUniqueKey", randomNumber + "!@#$%^&*publisherportal!@#$%^&*" + $("#user").val());

                            webStorageKey = randomNumber;
                        } else {
                            webStorageKey = currentUniqueKey.split("!@#$%^&*")[0];
                        }
                    }

                    previousKey = webStorageKey + "!@#$%^&*publisherportal";

                    _listenToKeyChange(previousKey);


                    return webStorageKey + "!@#$%^&*publisherportal";
                },

                _getSubKey = function(subKeyName) {
                    var webStorageKey = _getUniqueKey(),
                        cachedData = _get(webStorageKey);

                    if (cachedData) {
                        if (cachedData.hasOwnProperty(subKeyName)) {
                            return cachedData[subKeyName];
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                },

                _removeDrillDownKeys = function() {
                    var webStorageKey = _getUniqueKey(),
                        cachedData = _get(webStorageKey);

                    if (cachedData) {
                        _.each(cachedData, function(value, key, list) {
                            if (key.indexOf("drillDown") != -1 || key.indexOf("modified") != -1) {

                                delete cachedData[key];
                            }
                        });

                        _set(webStorageKey, cachedData);

                    }


                },

                _removeDrillDownNavOptions = function() {
                    var webStorageKey = _getUniqueKey(),
                        cachedData = _get(webStorageKey);

                    if (cachedData) {

                        if (cachedData.hasOwnProperty("drillDownNavOptions")) {
                            delete cachedData["drillDownNavOptions"];
                        }

                        _set(webStorageKey, cachedData);
                    }
                },

                _listenToKeyChange = function(key) {
                    $.jStorage.listenKeyChange(key, function(key, action) {});
                },

                _stopListening = function(key) {
                    $.jStorage.stopListening(key);
                };


            return {
                get: _get,
                set: _set,
                deleteKey: _deleteKey,
                getUniqueKey: _getUniqueKey,
                getSubKey: _getSubKey,
                removeDrillDownKeys: _removeDrillDownKeys,
                removeDrillDownNavOptions: _removeDrillDownNavOptions,
                flush: _flush
            };


        })();


        return LocalStorage;
    });
