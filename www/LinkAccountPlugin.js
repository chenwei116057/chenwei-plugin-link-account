var exec = require('cordova/exec');

var LinkAccountPlugin = {
    init: function (appKey, success, error) {
        exec(success, error, "LinkAccountPlugin", "init", [appKey]);
    },
    getMobileAuth: function (success, error) {
        exec(success, error, "LinkAccountPlugin", "getMobileAuth", []);
    },
    login: function (privacy, success, error) {
        exec(success, error, "LinkAccountPlugin", "login", [privacy.name, privacy.url]);
    }
};
module.exports = LinkAccountPlugin;
