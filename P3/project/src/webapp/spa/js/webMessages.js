
webMsgs = angular.module('webMessages', []);


webMsgs.service('remoteMsgs', ['$http', '$window', function($http, $window) {
    var apiUrl = '../api';

    this.getUser = function(successfn,errorfn) {
        $http.get(apiUrl + '/user').then(
            function (resp) { successfn(resp.data); },
            function (resp) {
                var msg = 'Status code: ' + resp.statusText + '. ' + resp.data;
                errorfn ? errorfn(msg) : $window.alert (msg);
            }
        );
    };

    this.getAccount = function(accname, successfn,errorfn) {
        $http.get(apiUrl + '/accounts/' + accname).then(
            function (resp) { successfn(resp.data); },
            function (resp) {
                var msg = 'Status code: ' + resp.statusText + '. ' + resp.data;
                errorfn ? errorfn(msg) : $window.alert (msg);
            }
        );
    };
}]);


