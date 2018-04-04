var exec = require('cordova/exec');

exports.storage = function(arg0, success, error) {
    exec(success, error, "AchieveJurisdiction", "storage", [arg0]);
};
exports.exitApp = function(arg0, success, error) {
    exec(success, error, "AchieveJurisdiction", "exitApp", [arg0]);
};
